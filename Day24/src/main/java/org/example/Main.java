package org.example;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise1();
        exercise2();
    }

    private static void exercise1() throws IOException {
        System.out.println("Solving Day 24 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        List<Hailstone> hailstones = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(" @ ");
            String[] positions = data[0].split(", ");
            String[] speeds = data[1].split(", ");
            Hailstone hailstone = new Hailstone(Double.parseDouble(positions[0]),
                    Double.parseDouble(positions[1]), Double.parseDouble(positions[2]), Double.parseDouble(speeds[0]),
                    Double.parseDouble(speeds[1]), Double.parseDouble(speeds[2]));
            hailstones.add(hailstone);
        }

        long countIntersections = 0;
        for (int i = 0; i < hailstones.size(); i++) {
            for(int j = i +1; j < hailstones.size(); j++) {
                if(intersectInRange(hailstones.get(i), hailstones.get(j), 200000000000000D, 400000000000000D)) {
                    countIntersections++;
                }
            }
        }

        System.out.println("Total number of valid intersections is: " + countIntersections);
    }

    //I used vector calculus for this. A trajectory can be defined as (posX, posY) + k*(velX,velY).
    //Then we can equal the 2 trajectories and solve for the two k variables, which will eventually
    //give us the intersect point
    private static boolean intersectInRange(Hailstone hailstone1, Hailstone hailstone2, double min, double max) {
        //first handling some edges that I got from solving the equation system (branches to avoid divisions by zero)
        if (hailstone1.velX == 0) {
            if (hailstone2.velX == 0) {
                if (hailstone1.posX == hailstone2.posX) {
                    return true; //the trajectories are even completely equal in this case
                } else {
                    return false;
                }
            } else {
                //ignoring the possibility of hailstone1.velY being 0 because it would mean it is stopped
                double k1 = (hailstone2.posY - hailstone1.posY + (hailstone2.velY/hailstone2.velX)*(hailstone1.posX-hailstone2.posX));
                k1 = k1/hailstone1.velY;
                double intersectX = hailstone1.posX + k1 * hailstone1.velX;
                double intersectY = hailstone1.posY + k1 * hailstone1.velY;
                if(intersectX >= min && intersectX <= max && intersectY >= min && intersectY <= max) {
                    double k2 = (hailstone1.posX + k1 * hailstone1.velX - hailstone2.posX)/hailstone2.velX;
                    return k1>=0 && k2>=0;
                }
                else {
                    return false;
                }
            }
        }

        double tempValue = hailstone2.velY - hailstone1.velY * (hailstone2.velX/hailstone1.velX);
        if (tempValue == 0) {
            if (hailstone1.posY + (hailstone2.posX/hailstone1.velX) - (hailstone1.posX/hailstone1.velX) == 0) {
                return true; //in this case they are always in the same place
            } else {
                return false;
            }
        }

        double k2 = hailstone1.posY - hailstone2.posY + hailstone1.velY * ((hailstone2.posX - hailstone1.posX)/hailstone1.velX);
        k2 = k2 / (hailstone2.velY - hailstone1.velY * (hailstone2.velX/hailstone1.velX));
        double intersectX = hailstone2.posX + k2 * hailstone2.velX;
        double intersectY = hailstone2.posY + k2 * hailstone2.velY;
        if(intersectX >= min && intersectX <= max && intersectY >= min && intersectY <= max) {
            double k1 = (hailstone2.posX + k2 * hailstone2.velX - hailstone1.posX)/hailstone1.velX;
            return k1>=0 && k2>=0;
        }
        else {
            return false;
        }
    }

    //I tried to create a system of equations to solve this, matching the position of the rock with 3 stones.
    //Given that we have 3 dimensions, this gives us 9 equations. And 9 unknown variables (position and velocity
    //dimensions of the stone and the three collision times).
    //The problem is that the equations are non-linear. So I implemented an approximate solver for the system of equations.
    //Basically, I wrote the equations placing 0 on one side of the equation and  determine the gradients
    //of the error in each equation in function of the variables, to know how to iteratively move.
    //It is an implementation of the Gradient Descent Algorithm.
    private static void exercise2() throws IOException {
        System.out.println("Solving Day 24 Challenge 2: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        List<Hailstone> hailstones = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(" @ ");
            String[] positions = data[0].split(", ");
            String[] speeds = data[1].split(", ");
            Hailstone hailstone = new Hailstone(Double.parseDouble(positions[0]),
                    Double.parseDouble(positions[1]), Double.parseDouble(positions[2]), Double.parseDouble(speeds[0]),
                    Double.parseDouble(speeds[1]), Double.parseDouble(speeds[2]));
            hailstones.add(hailstone);
        }

        //get an initial estimation of the rock position and velocity and time collisions
        //it will hold the average of the values for all hailstones, so that it is in an initial position
        //close to the real one. Initial times will be velocity also because I am guessing they will be of
        //the same order of magnitude
        Solution rock = getInitialSolution(hailstones);

        Double error = Double.MAX_VALUE;

        Double learningStep = 0.0001D;
        //while the error is bigger than a very small value
        while (error > 0.05) {
            Double previousError = error;
            //get the error for the 9 equations and sum it
            Double error1 = Math.pow(rock.posX + rock.t1 * rock.velX - hailstones.get(0).posX - rock.t1 * hailstones.get(0).velX, 2D);
            Double error2 = Math.pow(rock.posY + rock.t1 * rock.velY - hailstones.get(0).posY - rock.t1 * hailstones.get(0).velY, 2D);
            Double error3 = Math.pow(rock.posZ + rock.t1 * rock.velZ - hailstones.get(0).posZ - rock.t1 * hailstones.get(0).velZ, 2D);
            Double error4 = Math.pow(rock.posX + rock.t2 * rock.velX - hailstones.get(1).posX - rock.t2 * hailstones.get(1).velX, 2D);
            Double error5 = Math.pow(rock.posY + rock.t2 * rock.velY - hailstones.get(1).posY - rock.t2 * hailstones.get(1).velY, 2D);
            Double error6 = Math.pow(rock.posZ + rock.t2 * rock.velZ - hailstones.get(1).posZ - rock.t2 * hailstones.get(1).velZ, 2D);
            Double error7 = Math.pow(rock.posX + rock.t3 * rock.velX - hailstones.get(2).posX - rock.t3 * hailstones.get(2).velX, 2D);
            Double error8 = Math.pow(rock.posY + rock.t3 * rock.velY - hailstones.get(2).posY - rock.t3 * hailstones.get(2).velY, 2D);
            Double error9 = Math.pow(rock.posZ + rock.t3 * rock.velZ - hailstones.get(2).posZ - rock.t3 * hailstones.get(2).velZ, 2D);
            error = error1 + error2 + error3 + error4 + error5 + error6 + error7 + error8 + error9;

            if (error > previousError) {
                break;
            }

            //calculate the gradient for each of the 9 variables
            Double gradPosX = 2 * (rock.posX + rock.t1 * rock.velX - hailstones.get(0).posX - rock.t1 * hailstones.get(0).velX) * 1 +
                    2 * (rock.posX + rock.t2 * rock.velX - hailstones.get(1).posX - rock.t2 * hailstones.get(1).velX) * 1 +
                    2 * (rock.posX + rock.t3 * rock.velX - hailstones.get(2).posX - rock.t3 * hailstones.get(2).velX) * 1;
            Double gradPosY = 2 * (rock.posY + rock.t1 * rock.velY - hailstones.get(0).posY - rock.t1 * hailstones.get(0).velY) * 1 +
                    2 * (rock.posY + rock.t2 * rock.velY - hailstones.get(1).posY - rock.t2 * hailstones.get(1).velY) * 1 +
                    2 * (rock.posY + rock.t3 * rock.velY - hailstones.get(2).posY - rock.t3 * hailstones.get(2).velY) * 1;
            Double gradPosZ = 2 * (rock.posZ + rock.t1 * rock.velZ - hailstones.get(0).posZ - rock.t1 * hailstones.get(0).velZ) * 1 +
                    2 * (rock.posZ + rock.t2 * rock.velZ - hailstones.get(1).posZ - rock.t2 * hailstones.get(1).velZ) * 1 +
                    2 * (rock.posZ + rock.t3 * rock.velZ - hailstones.get(2).posZ - rock.t3 * hailstones.get(2).velZ) * 1;
            Double gradVelX = 2 * (rock.posX + rock.t1 * rock.velX - hailstones.get(0).posX - rock.t1 * hailstones.get(0).velX) * rock.t1 +
                    2 * (rock.posX + rock.t2 * rock.velX - hailstones.get(1).posX - rock.t2 * hailstones.get(1).velX) * rock.t2 +
                    2 * (rock.posX + rock.t3 * rock.velX - hailstones.get(2).posX - rock.t3 * hailstones.get(2).velX) * rock.t3;
            Double gradVelY = 2 * (rock.posY + rock.t1 * rock.velY - hailstones.get(0).posY - rock.t1 * hailstones.get(0).velY) * rock.t1 +
                    2 * (rock.posY + rock.t2 * rock.velY - hailstones.get(1).posY - rock.t2 * hailstones.get(1).velY) * rock.t2 +
                    2 * (rock.posY + rock.t3 * rock.velY - hailstones.get(2).posY - rock.t3 * hailstones.get(2).velY) * rock.t3;
            Double gradVelZ = 2 * (rock.posZ + rock.t1 * rock.velZ - hailstones.get(0).posZ - rock.t1 * hailstones.get(0).velZ) * rock.t1 +
                    2 * (rock.posZ + rock.t2 * rock.velZ - hailstones.get(1).posZ - rock.t2 * hailstones.get(1).velZ) * rock.t2 +
                    2 * (rock.posZ + rock.t3 * rock.velZ - hailstones.get(2).posZ - rock.t3 * hailstones.get(2).velZ) * rock.t3;
            Double gradT1 = 2 * (rock.posX + rock.t1 * rock.velX - hailstones.get(0).posX - rock.t1 * hailstones.get(0).velX) * (rock.velX - hailstones.get(0).velX) +
                    2 * (rock.posY + rock.t1 * rock.velY - hailstones.get(0).posY - rock.t1 * hailstones.get(0).velY) * (rock.velY - hailstones.get(0).velY) +
                    2 * (rock.posZ + rock.t1 * rock.velZ - hailstones.get(0).posZ - rock.t1 * hailstones.get(0).velZ) * (rock.velZ - hailstones.get(0).velZ);
            Double gradT2 = 2 * (rock.posX + rock.t2 * rock.velX - hailstones.get(1).posX - rock.t2 * hailstones.get(1).velX) * (rock.velX - hailstones.get(1).velX) +
                    2 * (rock.posY + rock.t2 * rock.velY - hailstones.get(1).posY - rock.t2 * hailstones.get(1).velY) * (rock.velY - hailstones.get(1).velY) +
                    2 * (rock.posZ + rock.t2 * rock.velZ - hailstones.get(1).posZ - rock.t2 * hailstones.get(1).velZ) * (rock.velZ - hailstones.get(1).velZ);
            Double gradT3 = 2 * (rock.posX + rock.t3 * rock.velX - hailstones.get(2).posX - rock.t3 * hailstones.get(2).velX) * (rock.velX - hailstones.get(2).velX) +
                    2 * (rock.posY + rock.t3 * rock.velY - hailstones.get(2).posY - rock.t3 * hailstones.get(2).velY) * (rock.velY - hailstones.get(2).velY) +
                    2 * (rock.posZ + rock.t3 * rock.velZ - hailstones.get(2).posZ - rock.t3 * hailstones.get(2).velZ) * (rock.velZ - hailstones.get(2).velZ);

            //update the variables
            rock.posX = rock.posX - learningStep * gradPosX;
            rock.posY = rock.posY - learningStep * gradPosY;
            rock.posZ = rock.posZ - learningStep * gradPosZ;
            rock.velX = rock.velX - learningStep * gradVelX;
            rock.velY = rock.velY - learningStep * gradVelY;
            rock.velZ = rock.velZ - learningStep * gradVelZ;
            rock.t1 = rock.t1 - learningStep * gradT1;
            rock.t2 = rock.t2 - learningStep * gradT2;
            rock.t3 = rock.t3 - learningStep * gradT3;

            System.out.println("Current error is: " + error);
        }

    }

    private static Solution getInitialSolution(List<Hailstone> hailstones) {
        double posX = hailstones.stream().map(hailstone -> hailstone.posX).reduce(0D, Double::sum);
        posX = posX/ hailstones.size();
        double posY = hailstones.stream().map(hailstone -> hailstone.posY).reduce(0D, Double::sum);
        posY = posY/ hailstones.size();
        double posZ = hailstones.stream().map(hailstone -> hailstone.posZ).reduce(0D, Double::sum);
        posZ = posZ/ hailstones.size();
        double velX = hailstones.stream().map(hailstone -> hailstone.velX).reduce(0D, Double::sum);
        velX = velX/ hailstones.size();
        double velY = hailstones.stream().map(hailstone -> hailstone.velY).reduce(0D, Double::sum);
        velY = velY/ hailstones.size();
        double velZ = hailstones.stream().map(hailstone -> hailstone.velZ).reduce(0D, Double::sum);
        velZ = velZ/ hailstones.size();

        return new Solution(posX,posY,posZ,velX,velY,velZ,velX, velY, velZ);
    }


}