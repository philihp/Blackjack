package com.philihp.bj;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Deck extends ArrayList<Card> implements Cloneable {
	
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
		add(Card._T);
		add(Card._T);
		add(Card._T);
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
		add(Card._T);
		add(Card._T);
		add(Card._T);
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
		add(Card._T);
		add(Card._T);
		add(Card._T);
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
		add(Card._T);
		add(Card._T);
		add(Card._T);
		add(Card._A);
		this.initialSize = this.size();
	}
	
	public Deck(int numberOfDecks, Player player) {
		super(DECKSIZE * numberOfDecks);
		for(int i = 0; i < numberOfDecks; i++) {
			addAll(new Deck());
		}
		this.initialSize = this.size();
		this.player = player;
	}
	
	public void shuffle(Random randomizer) {
		Collections.shuffle(this, randomizer);
	}

	public int getInitialSize() {
		return this.initialSize;
	}
	
	public Card draw() {
		Card card = remove(size()-1);
		player.notify(card);
		return card;
	}
	
	public double getProbability(Card card) {
		short count = 0;
		for(Card c : this) {
			if(c == card) count++;
		}
		return (double)count / size();
	}
	
	public boolean remove(Card card) {
		for(int i = 0; i < size(); i++) {
			Card c = get(i);
			if(c == card) {
				remove(i);
				return true;
			}
		}
		return false;
	}

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
}
