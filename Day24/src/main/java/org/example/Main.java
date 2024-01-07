package org.example;

import Jama.Matrix;

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
    //The problem is that the equations are non-linear.
    //By re-arranging the 3 equations of the first hailstone with t on the LHS and the rest of the RHS, and then
    //transforming them to RHS of each equation must be equal to the other, we get non-linear equation on
    //velocity in an axis multiplied by position in another. But we can do this process for multiple hailstones,
    //and then when we sum the equations of two hailstones together the non-linear terms will cancel each other.
    //And we will have linear equations, with four unknown variables (speed and position in two dimensions).
    //We need to do the equation for 4 different pairs of hailstones to have the solution.
    //Written below is just the equations that I got from solving the system by hand to obtain the variables.
    //I used a lib called JAMA (https://math.nist.gov/javanumerics/jama/) that I found online
    //to have a linear equation system solver.
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

        /*
        double[][] array = {{1.,2.,3},{4.,5.,6.},{7.,8.,10.}};
        Matrix A = new Matrix(array);
        Matrix b = Matrix.random(3,1);
        Matrix x = A.solve(b);
        Matrix Residual = A.times(x).minus(b);
        double rnorm = Residual.normInf();
         */

        //this system will give us posX, posY, velX, velY
        Hailstone hailstone1 = hailstones.get(0);
        Hailstone hailstone2 = hailstones.get(1);
        Hailstone hailstone3 = hailstones.get(2);
        Hailstone hailstone4 = hailstones.get(3);
        Hailstone hailstone5 = hailstones.get(4);

        double[][] coefficients = {{
            (hailstone1.velY - hailstone2.velY),
            (hailstone2.velX - hailstone1.velX),
            (hailstone2.posY - hailstone1.posY),
            (hailstone1.posX - hailstone2.posX)},{
            (hailstone2.velY - hailstone3.velY),
            (hailstone3.velX - hailstone2.velX),
            (hailstone3.posY - hailstone2.posY),
            (hailstone2.posX - hailstone3.posX)},{
            (hailstone3.velY - hailstone4.velY),
            (hailstone4.velX - hailstone3.velX),
            (hailstone4.posY - hailstone3.posY),
            (hailstone3.posX - hailstone4.posX)},{
            (hailstone4.velY - hailstone5.velY),
            (hailstone5.velX - hailstone4.velX),
            (hailstone5.posY - hailstone4.posY),
            (hailstone4.posX - hailstone5.posX)}};

        double[][] bValues = {{
                (hailstone1.velY * hailstone1.posX - hailstone1.velX * hailstone1.posY - hailstone2.velY * hailstone2.posX + hailstone2.velX * hailstone2.posY)},{
                (hailstone2.velY * hailstone2.posX - hailstone2.velX * hailstone2.posY - hailstone3.velY * hailstone3.posX + hailstone3.velX * hailstone3.posY)},{
                (hailstone3.velY * hailstone3.posX - hailstone3.velX * hailstone3.posY - hailstone4.velY * hailstone4.posX + hailstone4.velX * hailstone4.posY)},{
                (hailstone4.velY * hailstone4.posX - hailstone4.velX * hailstone4.posY - hailstone5.velY * hailstone5.posX + hailstone5.velX * hailstone5.posY)}};

        Matrix A = new Matrix(coefficients);
        Matrix b = new Matrix(bValues);
        Matrix x = A.solve(b);
        int debug = 1;
        double posX = x.get(0,0);
        double posY = x.get(1, 0);
        double velX = x.get(2, 0);
        double velY = x.get(3, 0);

        //now I will solve another still to get the position and velocity for Z, we could use the values we already
        //have to create a simpler equation system. But this code runs fast so no need to have extra work creating the equations
        //for that.

        double[][] coefficients2 = {{
                (hailstone1.velZ - hailstone2.velZ),
                (hailstone2.velY - hailstone1.velY),
                (hailstone2.posZ - hailstone1.posZ),
                (hailstone1.posY - hailstone2.posY)},{
                (hailstone2.velZ - hailstone3.velZ),
                (hailstone3.velY - hailstone2.velY),
                (hailstone3.posZ - hailstone2.posZ),
                (hailstone2.posY - hailstone3.posY)},{
                (hailstone3.velZ - hailstone4.velZ),
                (hailstone4.velY - hailstone3.velY),
                (hailstone4.posZ - hailstone3.posZ),
                (hailstone3.posY - hailstone4.posY)},{
                (hailstone4.velZ - hailstone5.velZ),
                (hailstone5.velY - hailstone4.velY),
                (hailstone5.posZ - hailstone4.posZ),
                (hailstone4.posY - hailstone5.posY)}};

        double[][] bValues2 = {{
                (hailstone1.velZ * hailstone1.posY - hailstone1.velY * hailstone1.posZ - hailstone2.velZ * hailstone2.posY + hailstone2.velY * hailstone2.posZ)},{
                (hailstone2.velZ * hailstone2.posY - hailstone2.velY * hailstone2.posZ - hailstone3.velZ * hailstone3.posY + hailstone3.velY * hailstone3.posZ)},{
                (hailstone3.velZ * hailstone3.posY - hailstone3.velY * hailstone3.posZ - hailstone4.velZ * hailstone4.posY + hailstone4.velY * hailstone4.posZ)},{
                (hailstone4.velZ * hailstone4.posY - hailstone4.velY * hailstone4.posZ - hailstone5.velZ * hailstone5.posY + hailstone5.velY * hailstone5.posZ)}};

        Matrix A2 = new Matrix(coefficients2);
        Matrix b2 = new Matrix(bValues2);
        Matrix x2 = A2.solve(b2);
        double posZ = x2.get(1, 0);
        double velZ = x2.get(3, 0);

        double sum = posX + posY + posZ;
        System.out.printf("Sum of initial coordinates of the rock is: %f\n", sum);
    }


}