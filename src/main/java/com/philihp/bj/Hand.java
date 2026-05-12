package com.philihp.bj;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Hand implements Iterable<Card> {

    private static final int MAX_CARDS = 12;

    private final Card[] cards = new Card[MAX_CARDS];
    private int size;
    private int value;
    private int softAces;
    private boolean split;
    private boolean pair;
    private int bet;
    private boolean surrendered;
    private boolean doubleAfterSplitAllowed = true;

    public Hand(int bet, Card holeCard, Card showCard, boolean split) {
        reset(bet, holeCard, showCard, split);
    }

    public Hand(Card holeCard, Card showCard) {
        this(0, holeCard, showCard, false);
    }

    public Hand() {
    }

    public void reset(int bet, Card holeCard, Card showCard, boolean split) {
        this.bet = bet;
        this.split = split;
        this.size = 0;
        this.value = 0;
        this.softAces = 0;
        this.surrendered = false;
        this.doubleAfterSplitAllowed = true;
        addInternal(showCard);
        addInternal(holeCard);
        this.pair = (holeCard == showCard);
    }

    public int getBet() {
        return bet;
    }

    public void addBet(int extra) {
        this.bet += extra;
    }

    public boolean isSplit() {
        return split;
    }

    public void setDoubleAfterSplitAllowed(boolean allowed) {
        this.doubleAfterSplitAllowed = allowed;
    }

    public boolean canDoubleDown() {
        if (size != 2) return false;
        if (split && !doubleAfterSplitAllowed) return false;
        return true;
    }

    public boolean isBlackjack() {
        return size == 2 && value == 21;
    }

    public Card getShowCard() {
        if (size == 0) throw new IllegalStateException("No card dealt yet");
        return cards[0];
    }

    public Card get(int i) {
        return cards[i];
    }

    public int size() {
        return size;
    }

    public boolean add(Card card) {
        addInternal(card);
        return true;
    }

    private void addInternal(Card card) {
        if (card == Card._A) softAces++;
        value += card.getValue();
        if (value > 21 && softAces > 0) {
            value -= 10;
            softAces--;
        }
        cards[size++] = card;
    }

    public int getValue() {
        return value;
    }

    public boolean isSoft() {
        return softAces > 0;
    }

    public boolean isPair() {
        return pair;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < size; i++) s.append(cards[i]);
        return s.toString();
    }

    public void surrender() {
        surrendered = true;
    }

    public boolean isSurrendered() {
        return surrendered;
    }

    @Override
    public Iterator<Card> iterator() {
        return new Iterator<>() {
            int i = 0;
            @Override public boolean hasNext() { return i < size; }
            @Override public Card next() {
                if (i >= size) throw new NoSuchElementException();
                return cards[i++];
            }
        };
    }
}
