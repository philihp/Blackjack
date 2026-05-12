package com.philihp.bj;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HandTest {

    @Test
    void hardTotal() {
        Hand h = new Hand(Card._9, Card._7);
        assertEquals(16, h.getValue());
        assertFalse(h.isSoft());
    }

    @Test
    void blackjackOnDeal() {
        Hand h = new Hand(Card._A, Card._T);
        assertTrue(h.isBlackjack());
        assertEquals(21, h.getValue());
    }

    @Test
    void notBlackjackWhenThreeCardTwentyOne() {
        Hand h = new Hand(Card._7, Card._7);
        h.add(Card._7);
        assertEquals(21, h.getValue());
        assertFalse(h.isBlackjack());
    }

    @Test
    void softAceDemotesOnBust() {
        Hand h = new Hand(Card._A, Card._6);   // soft 17
        assertEquals(17, h.getValue());
        assertTrue(h.isSoft());
        h.add(Card._T);                          // would be 27, demote ace
        assertEquals(17, h.getValue());
        assertFalse(h.isSoft());
    }

    @Test
    void pairDetectedAtCreation() {
        Hand pair = new Hand(Card._8, Card._8);
        assertTrue(pair.isPair());
        Hand notPair = new Hand(Card._8, Card._9);
        assertFalse(notPair.isPair());
    }

    @Test
    void canDoubleDownOnlyOnTwoCards() {
        Hand h = new Hand(Card._5, Card._6);
        assertTrue(h.canDoubleDown());
        h.add(Card._2);
        assertFalse(h.canDoubleDown());
    }

    @Test
    void doubleAfterSplitDisabled() {
        Hand h = new Hand(10, Card._5, Card._5, true);
        h.setDoubleAfterSplitAllowed(false);
        assertFalse(h.canDoubleDown());
    }

    @Test
    void surrenderFlag() {
        Hand h = new Hand(Card._T, Card._6);
        assertFalse(h.isSurrendered());
        h.surrender();
        assertTrue(h.isSurrendered());
    }

    @Test
    void showCardIsFirst() {
        Hand h = new Hand(Card._A, Card._T);   // hole=A, show=T per ctor
        assertEquals(Card._T, h.getShowCard());
    }

    @Test
    void betArithmetic() {
        Hand h = new Hand(10, Card._5, Card._5, false);
        assertEquals(10, h.getBet());
        h.addBet(10);
        assertEquals(20, h.getBet());
    }

    @Test
    void aceAcePairIsSoft() {
        Hand h = new Hand(Card._A, Card._A);
        assertEquals(12, h.getValue());
        assertTrue(h.isSoft());
        assertTrue(h.isPair());
    }
}
