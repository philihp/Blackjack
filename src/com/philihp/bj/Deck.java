package com.philihp.bj;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Deck extends ArrayList<Card> {
	
	private static final int DECKSIZE = Card.values().length * Suit.values().length;
	
	private int initialSize;
	
	private Player player;
	
	public Deck() {
		super(DECKSIZE);
		add(Card._2);
		add(Card._3);
		add(Card._4);
		add(Card._5);
		add(Card._6);
		add(Card._7);
		add(Card._8);
		add(Card._9);
		add(Card._T);
		add(Card._J);
		add(Card._Q);
		add(Card._K);
		add(Card._A);
		add(Card._2);
		add(Card._3);
		add(Card._4);
		add(Card._5);
		add(Card._6);
		add(Card._7);
		add(Card._8);
		add(Card._9);
		add(Card._T);
		add(Card._J);
		add(Card._Q);
		add(Card._K);
		add(Card._A);
		add(Card._2);
		add(Card._3);
		add(Card._4);
		add(Card._5);
		add(Card._6);
		add(Card._7);
		add(Card._8);
		add(Card._9);
		add(Card._T);
		add(Card._J);
		add(Card._Q);
		add(Card._K);
		add(Card._A);
		add(Card._2);
		add(Card._3);
		add(Card._4);
		add(Card._5);
		add(Card._6);
		add(Card._7);
		add(Card._8);
		add(Card._9);
		add(Card._T);
		add(Card._J);
		add(Card._Q);
		add(Card._K);
		add(Card._A);
		this.initialSize = this.size();
	}
	
	public Deck(Random randomizer, int numberOfDecks, Player player) {
		super(DECKSIZE * numberOfDecks);
		for(int i = 0; i < numberOfDecks; i++) {
			addAll(new Deck());
		}
		Collections.shuffle(this, randomizer);
		
		this.initialSize = this.size();
		this.player = player;
	}

	public int getInitialSize() {
		return this.initialSize;
	}
	
	public Card draw() {
		Card card = remove(size()-1);
		player.notify(card);
		return card;
	}
}
