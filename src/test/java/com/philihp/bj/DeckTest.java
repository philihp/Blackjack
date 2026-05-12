package com.philihp.bj;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DeckTest {

    @Test
    void sixDeckShoeHas312Cards() {
        Deck d = new Deck(6, null);
        assertEquals(312, d.size());
        assertEquals(312, d.getInitialSize());
    }

    @Test
    void singleDeckHasCorrectCardMultiplicities() {
        Deck d = new Deck(1, null);
        EnumMap<Card, Integer> counts = new EnumMap<>(Card.class);
        for (Card c : Card.values()) counts.put(c, 0);
        for (Card c : d) counts.merge(c, 1, Integer::sum);

        for (Card c : Card.values()) {
            int expected = (c == Card._T) ? 16 : 4;
            assertEquals(expected, counts.get(c), "wrong count for " + c);
        }
    }

    @Test
    void shuffleIsDeterministicWithSeededRandom() {
        Deck a = new Deck(1, null);
        Deck b = new Deck(1, null);
        a.shuffle(new Random(42L));
        b.shuffle(new Random(42L));
        assertEquals(a, b);
    }

    @Test
    void shuffleProducesDifferentOrderingsWithDifferentSeeds() {
        Deck a = new Deck(1, null);
        Deck b = new Deck(1, null);
        a.shuffle(new Random(1L));
        b.shuffle(new Random(2L));
        assertNotEquals(a, b);
    }

    @Test
    void drawNotifiesPlayer() {
        CountingPlayer p = new CountingPlayer();
        Deck d = new Deck(1, p);
        d.draw();
        d.draw();
        d.draw();
        assertEquals(3, p.observed);
    }

    @Test
    void drawReducesSizeButNotInitialSize() {
        Deck d = new Deck(1, null);
        int before = d.size();
        d.draw();
        assertEquals(before - 1, d.size());
        assertEquals(before, d.getInitialSize());
    }

    private static final class CountingPlayer implements Player {
        int observed;
        @Override public int bet() { return 0; }
        @Override public Response prompt(Hand p, Hand d, boolean canSplit) { return Response.S; }
        @Override public void observe(Card card) { observed++; }
        @Override public void resetCount(int decks) { }
    }
}
