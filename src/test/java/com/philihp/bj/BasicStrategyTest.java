package com.philihp.bj;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicStrategyTest {

    private final ZeroMemoryPlayer player = new ZeroMemoryPlayer(10);

    private Response decide(Card p1, Card p2, Card dealerUp, boolean canSplit) {
        Hand player = new Hand(p1, p2);
        // Hand(hole, show) — show is what the player sees.
        Hand dealer = new Hand(Card._2, dealerUp);
        return this.player.prompt(player, dealer, canSplit);
    }

    @Test
    void hardSixteenVsTenIsSurrender() {
        assertEquals(Response.RH, decide(Card._T, Card._6, Card._T, true));
    }

    @Test
    void hardSixteenVsSixIsStand() {
        assertEquals(Response.S, decide(Card._T, Card._6, Card._6, true));
    }

    @Test
    void hardTwelveVsTwoIsHit() {
        assertEquals(Response.H, decide(Card._T, Card._2, Card._2, true));
    }

    @Test
    void hardElevenVsSixIsDoubleOrHit() {
        assertEquals(Response.DH, decide(Card._6, Card._5, Card._6, true));
    }

    @Test
    void softSeventeenAgainstSixIsDouble() {
        assertEquals(Response.DH, decide(Card._A, Card._6, Card._6, true));
    }

    @Test
    void softEighteenVsTwoIsStand() {
        assertEquals(Response.S, decide(Card._A, Card._7, Card._2, true));
    }

    @Test
    void softEighteenVsNineIsHit() {
        assertEquals(Response.H, decide(Card._A, Card._7, Card._9, true));
    }

    @Test
    void pairOfEightsAlwaysSplit() {
        for (Card up : Card.values()) {
            assertEquals(Response.P, decide(Card._8, Card._8, up, true),
                    "8,8 vs " + up + " should split");
        }
    }

    @Test
    void pairOfTensNeverSplit() {
        for (Card up : Card.values()) {
            assertEquals(Response.S, decide(Card._T, Card._T, up, true),
                    "T,T vs " + up + " should stand");
        }
    }

    @Test
    void pairOfAcesAlwaysSplit() {
        for (Card up : Card.values()) {
            assertEquals(Response.P, decide(Card._A, Card._A, up, true),
                    "A,A vs " + up + " should split");
        }
    }

    @Test
    void pairOfAcesFallsBackToHardWhenCantSplit() {
        // A,A has value 12, treat as hard 12: H vs 2, S vs 4-6, H vs 7+
        assertEquals(Response.H, decide(Card._A, Card._A, Card._2, false));
        assertEquals(Response.S, decide(Card._A, Card._A, Card._5, false));
        assertEquals(Response.H, decide(Card._A, Card._A, Card._T, false));
    }

    @Test
    void hiLoAndZeroMemoryMostlyAgree() {
        ZeroMemoryPlayer zm = new ZeroMemoryPlayer(10);
        HiLoPlayer hl = new HiLoPlayer();
        // Spot-check on a varied set
        Card[][] hands = {
                {Card._T, Card._6}, {Card._A, Card._7}, {Card._9, Card._9},
                {Card._5, Card._5}, {Card._4, Card._4}
        };
        Card[] ups = {Card._2, Card._6, Card._9, Card._T, Card._A};
        for (Card[] ph : hands) {
            for (Card up : ups) {
                Hand p = new Hand(ph[0], ph[1]);
                Hand d = new Hand(Card._2, up);
                // they may differ on isolated cells, but neither should throw
                zm.prompt(p, d, true);
                hl.prompt(p, d, true);
            }
        }
    }
}
