package com.philihp.bj;

import com.philihp.bj.Blackjack.GameConfig;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PayoutTest {

    private final Blackjack sim = new Blackjack(
            GameConfig.defaults(), new Random(0), new ZeroMemoryPlayer(10));

    private Hand hand(int bet, Card... cards) {
        Hand h = new Hand(bet, cards[0], cards[1], false);
        for (int i = 2; i < cards.length; i++) h.add(cards[i]);
        return h;
    }

    @Test
    void bothBlackjackIsPush() {
        Hand p = hand(10, Card._A, Card._T);
        Hand d = hand(0, Card._A, Card._T);
        assertEquals(10, sim.payout(p, d));
    }

    @Test
    void playerBlackjackPays3to2() {
        Hand p = hand(10, Card._A, Card._T);
        Hand d = hand(0, Card._T, Card._9);
        assertEquals(25, sim.payout(p, d));  // 10 * (1 + 1.5) = 25
    }

    @Test
    void dealerBlackjackPlayerLoses() {
        Hand p = hand(10, Card._T, Card._9);
        Hand d = hand(0, Card._A, Card._T);
        assertEquals(0, sim.payout(p, d));
    }

    @Test
    void playerBustLoses() {
        Hand p = hand(10, Card._T, Card._6);
        p.add(Card._T);
        Hand d = hand(0, Card._T, Card._9);
        assertEquals(0, sim.payout(p, d));
    }

    @Test
    void dealerBustPlayerWins() {
        Hand p = hand(10, Card._T, Card._9);
        Hand d = hand(0, Card._T, Card._6);
        d.add(Card._T);
        assertEquals(20, sim.payout(p, d));
    }

    @Test
    void higherTotalWins() {
        Hand p = hand(10, Card._T, Card._9);
        Hand d = hand(0, Card._T, Card._8);
        assertEquals(20, sim.payout(p, d));
    }

    @Test
    void lowerTotalLoses() {
        Hand p = hand(10, Card._T, Card._8);
        Hand d = hand(0, Card._T, Card._9);
        assertEquals(0, sim.payout(p, d));
    }

    @Test
    void equalTotalsPush() {
        Hand p = hand(10, Card._T, Card._9);
        Hand d = hand(0, Card._T, Card._9);
        assertEquals(10, sim.payout(p, d));
    }

    @Test
    void surrenderReturnsHalfBet() {
        Hand p = hand(10, Card._T, Card._6);
        p.surrender();
        Hand d = hand(0, Card._T, Card._9);
        assertEquals(5, sim.payout(p, d));
    }
}
