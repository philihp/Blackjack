package com.philihp.bj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck extends ArrayList<Card> {

    private static final long serialVersionUID = 1L;

    private static final int CARDS_PER_SINGLE_DECK = 52;
    private static final int SUITS_PER_DECK = 4;

    private final int initialSize;
    private final Player player;

    public Deck(int numberOfDecks, Player player) {
        super(CARDS_PER_SINGLE_DECK * numberOfDecks);
        this.player = player;
        for (int d = 0; d < numberOfDecks; d++) {
            for (int s = 0; s < SUITS_PER_DECK; s++) {
                for (Card card : Card.values()) {
                    add(card);
                    if (card == Card._T) {
                        add(card);
                        add(card);
                        add(card);
                    }
                }
            }
        }
        this.initialSize = size();
    }

    public void shuffle(Random randomizer) {
        Collections.shuffle(this, randomizer);
    }

    public int getInitialSize() {
        return initialSize;
    }

    public Card draw() {
        Card card = remove(size() - 1);
        if (player != null) player.observe(card);
        return card;
    }

    public double getProbability(Card card) {
        long count = stream().filter(c -> c == card).count();
        return (double) count / size();
    }
}
