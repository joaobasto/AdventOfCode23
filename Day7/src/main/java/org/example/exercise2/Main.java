package org.example.exercise2;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        exercise2();
    }

    private static void exercise2() throws IOException {
        System.out.println("Solving Day 7 Challenge 2: ");

        ClassLoader classLoader = org.example.exercise1.Main.class.getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        List<Hand> hands = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String[] handInfo = line.split(" ");
            String cards = handInfo[0];
            Long bid = Long.valueOf(handInfo[1]);
            hands.add(new Hand(cards, bid));
        }
        Collections.sort(hands);
        Long rank = 1L;
        Long totalWinnings = 0L;
        for(Hand hand : hands) {
            totalWinnings += hand.bid * rank;
            rank++;
        }
        System.out.println("The total winnings are: " + totalWinnings);
    }
}