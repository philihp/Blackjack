package com.philihp.bj;
import java.util.ArrayList;
import java.util.List;


public class Hand extends ArrayList<Card> implements Cloneable{
	
	private int value;
	private int softAces;
	private boolean split;
	private boolean pair;
	private int bet;
	private boolean surrendered;
	
	public Hand() {
		value = 0;
		softAces = 0;
		bet = 0;
		split = false;
		surrendered = false;
		pair = false;
	}

	public Hand(int bet, Card holeCard, Card showCard, boolean split) {
		super();
		this.bet = bet;
		this.split = split;
		add(showCard);
		add(holeCard);
		pair = holeCard == showCard;
	}
	
	public Hand(Card holeCard, Card showCard) {
		this(0, holeCard, showCard, false);
	}
	
	public int getBet() {
		return bet;
	}
	
	public void addBet(int bet) {
		this.bet += bet;
	}
	
	public boolean isSplit() {
		return split;
	}
	
	public boolean canDoubleDown() {
		if(size() != 2) return false;
		if(split && Blackjack.DOUBLE_AFTER_SPLIT == false) return false;
		return true;
//		int value = getValue();
//		return (value == 10 || value == 11);
	}
	
	public boolean isBlackjack() {
		return size() == 2 && getValue() == 21;
	}
	
	public Card getShowCard() {
		if(size() == 0) throw new RuntimeException("No hole card delt yet");
		return get(0);
	}
	
	@Override
	public boolean add(Card card) {
		if(card == Card._A)
			softAces++;
		addValue(card.getValue());
		
		if(getValue() > 21 && isSoft()) {
			addValue(-10);
			softAces--;
		}
		return super.add(card);
	}

	private void addValue(int value) {
		this.value += value;
	}

	public int getValue() {
		return value;
	}
	
	public boolean isSoft() {
		return softAces > 0;
	}
	
	public boolean isPair() {
		return pair;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(Card card: this) {
			s.append(card.toString());
		}
		return s.toString();
	}
	
	public void surrender() {
		surrendered = true;
	}
	
	public boolean isSurrendered() {
		return surrendered;
	}
	
}
