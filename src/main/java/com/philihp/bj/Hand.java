package com.philihp.bj;

import java.util.ArrayList;

public class Hand extends ArrayList<Card> {

    private static final long serialVersionUID = 1L;

    private int value;
    private int softAces;
    private final boolean split;
    private boolean pair;
    private int bet;
    private boolean surrendered;
    private boolean doubleAfterSplitAllowed = true;

    public Hand(int bet, Card holeCard, Card showCard, boolean split) {
        this.bet = bet;
        this.split = split;
        add(showCard);
        add(holeCard);
        this.pair = (holeCard == showCard);
    }

    public Hand(Card holeCard, Card showCard) {
        this(0, holeCard, showCard, false);
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
        if (size() != 2) return false;
        if (split && !doubleAfterSplitAllowed) return false;
        return true;
    }

    public boolean isBlackjack() {
        return size() == 2 && getValue() == 21;
    }

    public Card getShowCard() {
        if (isEmpty()) throw new IllegalStateException("No card dealt yet");
        return get(0);
    }

    @Override
    public boolean add(Card card) {
        if (card == Card._A) softAces++;
        value += card.getValue();
        if (value > 21 && softAces > 0) {
            value -= 10;
            softAces--;
        }
        return super.add(card);
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
        for (Card card : this) s.append(card);
        return s.toString();
    }

    public void surrender() {
        surrendered = true;
    }

    public boolean isSurrendered() {
        return surrendered;
    }
}
