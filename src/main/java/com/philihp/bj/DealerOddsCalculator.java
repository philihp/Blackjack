package com.philihp.bj;

import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Computes the probability distribution over dealer final hand values for each
 * possible up-card, given a shoe size and soft-17 rule. Outcome buckets are:
 * 17, 18, 19, 20, 21, BUST (-1), BLACKJACK (99).
 *
 * Exhaustive enumeration over the remaining shoe weighted by draw probability.
 */
public final class DealerOddsCalculator {

    private static final int BUST = -1;
    private static final int BLACKJACK = 99;

    private final int shoeSize;
    private final Response soft17;

    public DealerOddsCalculator(int shoeSize, Response soft17) {
        this.shoeSize = shoeSize;
        this.soft17 = soft17;
    }

    public Map<Card, Map<Integer, Double>> compute() {
        Map<Card, Map<Integer, Double>> result = new TreeMap<>();
        for (Card up : Card.values()) {
            result.put(up, emptyDistribution());
        }

        EnumMap<Card, Integer> shoe = freshShoe(shoeSize);
        int total = shoe.values().stream().mapToInt(Integer::intValue).sum();

        for (Card up : Card.values()) {
            int upCount = shoe.get(up);
            double pUp = (double) upCount / total;
            shoe.put(up, upCount - 1);

            EnumMap<Card, Double> startHand = new EnumMap<>(Card.class);
            startHand.put(up, 1.0);

            enumerate(up, valueOf(startHand), softAcesOf(startHand), shoe, total - 1, pUp, result.get(up));

            shoe.put(up, upCount);
        }
        return result;
    }

    private void enumerate(Card upCard,
                           int handValue,
                           int softAces,
                           EnumMap<Card, Integer> shoe,
                           int remaining,
                           double probability,
                           Map<Integer, Double> outcomes) {
        if (shouldStand(handValue, softAces)) {
            int bucket = bucketFor(handValue, upCard);
            outcomes.merge(bucket, probability, Double::sum);
            return;
        }
        for (Card next : Card.values()) {
            int avail = shoe.get(next);
            if (avail == 0) continue;
            double p = probability * ((double) avail / remaining);
            int newValue = handValue + next.getValue();
            int newSoft = softAces + (next == Card._A ? 1 : 0);
            if (newValue > 21 && newSoft > 0) {
                newValue -= 10;
                newSoft--;
            }
            shoe.put(next, avail - 1);
            enumerate(upCard, newValue, newSoft, shoe, remaining - 1, p, outcomes);
            shoe.put(next, avail);
        }
    }

    private boolean shouldStand(int value, int softAces) {
        if (value > 21) return true;
        if (value > 17) return true;
        if (value < 17) return false;
        // value == 17
        boolean soft = softAces > 0;
        return !soft || soft17 == Response.S;
    }

    private int bucketFor(int value, Card upCard) {
        if (value > 21) return BUST;
        if (value == 21 && (upCard == Card._A || upCard == Card._T)) return BLACKJACK;
        return value;
    }

    private static EnumMap<Card, Integer> freshShoe(int decks) {
        EnumMap<Card, Integer> shoe = new EnumMap<>(Card.class);
        for (Card c : Card.values()) {
            shoe.put(c, (c == Card._T ? 16 : 4) * decks);
        }
        return shoe;
    }

    private static Map<Integer, Double> emptyDistribution() {
        Map<Integer, Double> m = new TreeMap<>();
        for (int v = 17; v <= 21; v++) m.put(v, 0.0);
        m.put(BUST, 0.0);
        m.put(BLACKJACK, 0.0);
        return m;
    }

    private static int valueOf(EnumMap<Card, Double> startHand) {
        int v = 0;
        for (Map.Entry<Card, Double> e : startHand.entrySet()) v += e.getKey().getValue();
        return v;
    }

    private static int softAcesOf(EnumMap<Card, Double> startHand) {
        return startHand.containsKey(Card._A) ? 1 : 0;
    }

    public static void main(String[] args) {
        int shoeSize = args.length > 0 ? Integer.parseInt(args[0]) : 6;
        Response soft17 = args.length > 1 ? Response.valueOf(args[1]) : Response.H;
        Map<Card, Map<Integer, Double>> probs = new DealerOddsCalculator(shoeSize, soft17).compute();
        for (var e : probs.entrySet()) {
            for (var e2 : e.getValue().entrySet()) {
                System.out.println(e.getKey() + " " + e2.getKey() + " " + e2.getValue());
            }
        }
    }
}
