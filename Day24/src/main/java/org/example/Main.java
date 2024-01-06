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
                if(intersectInRange(hailstones.get(i), hailstones.get(j), 7, 27)) {
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
                    return true;
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
            return true;
        }
        else {
            return false;
        }
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 24 Challenge 1: ");

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {

        }
    }


}