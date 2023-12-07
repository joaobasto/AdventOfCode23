package org.example.exercise2;


public enum Card {
    ACE(14, 'A'),
    KING(13, 'K'),
    QUEEN(12, 'Q'),
    JACK(1, 'J'),
    TEN(10, 'T'),
    NINE(9, '9'),
    EIGHT(8, '8'),
    SEVEN(7, '7'),
    SIX(6, '6'),
    FIVE(5, '5'),
    FOUR(4, '4'),
    THREE(3, '3'),
    TWO(2, '2');
    int value;
    char character;

    private Card(int value, char character) {
        this.value = value;
        this.character = character;
    }

    public static Card getCardFromChar(char c) {
        for (Card card : Card.values()) {
            if(card.character == c) {
                return card;
            }
        }
        return null;
    }
}
