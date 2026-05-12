package com.philihp.bj;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class Deck implements Iterable<Card> {

    private static final int CARDS_PER_SINGLE_DECK = 52;
    private static final int SUITS_PER_DECK = 4;
    private static final Card[] CARD_VALUES = Card.values();

    private final Card[] cards;
    private final int initialSize;
    private final Player player;
    private int top;

    public Deck(int numberOfDecks, Player player) {
        this.player = player;
        int total = CARDS_PER_SINGLE_DECK * numberOfDecks;
        this.cards = new Card[total];
        int idx = 0;
        for (int d = 0; d < numberOfDecks; d++) {
            for (int s = 0; s < SUITS_PER_DECK; s++) {
                for (Card card : CARD_VALUES) {
                    cards[idx++] = card;
                    if (card == Card._T) {
                        cards[idx++] = card;
                        cards[idx++] = card;
                        cards[idx++] = card;
                    }
                }
            }
        }
        this.initialSize = idx;
        this.top = idx;
    }

    public void shuffle(Random randomizer) {
        for (int i = top - 1; i > 0; i--) {
            int j = randomizer.nextInt(i + 1);
            Card tmp = cards[i];
            cards[i] = cards[j];
            cards[j] = tmp;
        }
    }

    public int size() {
        return top;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public Card draw() {
        Card card = cards[--top];
        if (player != null) player.observe(card);
        return card;
    }

    public double getProbability(Card card) {
        int count = 0;
        for (int i = 0; i < top; i++) {
            if (cards[i] == card) count++;
        }
        return (double) count / top;
    }

    @Override
    public Iterator<Card> iterator() {
        return new Iterator<>() {
            int i = 0;
            @Override public boolean hasNext() { return i < top; }
            @Override public Card next() {
                if (i >= top) throw new NoSuchElementException();
                return cards[i++];
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deck other)) return false;
        if (this.top != other.top) return false;
        for (int i = 0; i < top; i++) {
            if (cards[i] != other.cards[i]) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 1;
        for (int i = 0; i < top; i++) h = 31 * h + cards[i].hashCode();
        return h ^ Integer.hashCode(top);
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(cards, top));
    }
}
