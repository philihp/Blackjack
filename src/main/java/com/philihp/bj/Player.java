package com.philihp.bj;

public interface Player {

    int bet();

    Response prompt(Hand playerHand, Hand dealerHand, boolean canSplit);

    void observe(Card card);

    void resetCount(int decks);
}
