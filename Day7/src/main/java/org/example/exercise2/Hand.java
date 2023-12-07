package org.example.exercise2;



import java.util.*;

public class Hand implements Comparable<Hand>{

    Long bid;
    EnumMap<Card, Integer> countByCard = new EnumMap<>(Card.class);
    int totalJacks = 0;
    List<Card> cards = new ArrayList<>();
    int value;

    public Hand(String cards, Long bid) {
        this.bid = bid;
        char[] chars = cards.toCharArray();
        for(char c : chars) {
            Card card = Card.getCardFromChar(c);
            if(!card.equals(Card.JACK)) {
                countByCard.compute(card, (k, v) -> v == null ? 1 : v + 1);
            } else {
                totalJacks++;
            }
            this.cards.add(card);
        }
        this.value = calculateHandValue();
    }

    @Override
    public int compareTo(Hand hand) {
        if(this.value == hand.value) {
            for(int i = 0; i < cards.size(); i++) {
                if(cards.get(i).value == hand.cards.get(i).value) {
                    continue;
                }
                return cards.get(i).value - hand.cards.get(i).value;
            }
            return 0;
        }
        return this.value - hand.value;
    }

    private int calculateHandValue() {
        int maxAmmountOfEqualCards = countByCard.isEmpty() ? 0 :
                countByCard.values().stream().mapToInt(v -> v).max().getAsInt();
        maxAmmountOfEqualCards += totalJacks;
        if(maxAmmountOfEqualCards == 1) {
            return HandType.HIGH_CARD.value;
        }
        if(maxAmmountOfEqualCards == 5) {
            return HandType.FIVE_OF_A_KIND.value;
        }
        if(maxAmmountOfEqualCards == 4) {
            return HandType.FOUR_OF_A_KIND.value;
        }
        if(maxAmmountOfEqualCards == 3) {
            if(totalJacks == 0) {
                boolean hasPair = countByCard.values().stream().anyMatch(value -> value.equals(2));
                return hasPair ? HandType.FULL_HOUSE.value : HandType.THREE_OF_A_KIND.value;
            }
            if(totalJacks == 1) {
                boolean hasTwoPair = countByCard.values().stream().filter(value -> value.equals(2)).count() == 2;
                return hasTwoPair ? HandType.FULL_HOUSE.value : HandType.THREE_OF_A_KIND.value;
            }
            return HandType.THREE_OF_A_KIND.value;
        }
        if(maxAmmountOfEqualCards == 2) {
            boolean hasTwoPair = countByCard.values().stream().filter(value -> value.equals(2)).count() == 2;
            return hasTwoPair ? HandType.TWO_PAIRS.value : HandType.ONE_PAIR.value;
        }
        return 0;
    }
}