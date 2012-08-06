package com.philihp.bj;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Deck extends ArrayList<Card> {
	
	private static final int DECKSIZE = Face.values().length * Suit.values().length;
	
	private int initialSize;
	
	public Deck() {
		super(DECKSIZE);
		for (Suit suit : Suit.values()) {
			for (Face face : Face.values()) {
				add(new Card(suit, face));
			}
		}
		
		this.initialSize = this.size();
	}
	
	public Deck(Random randomizer, int numberOfDecks) {
		super(DECKSIZE * numberOfDecks);
		for(int i = 0; i < numberOfDecks; i++) {
			addAll(new Deck());
		}
		Collections.shuffle(this, randomizer);
		
		this.initialSize = this.size();
	}

	public int getInitialSize() {
		return this.initialSize;
	}
	
	public Card draw() {
		return remove(size()-1);
	}
}
