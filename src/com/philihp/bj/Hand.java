package com.philihp.bj;
import java.util.ArrayList;
import java.util.List;


public class Hand extends ArrayList<Card>{
	
	private int value;
	private boolean soft;
	private boolean split;
	private boolean pair;
	private int bet;

	public Hand(int bet, Card holeCard, Card showCard, boolean split) {
		super();
		value = 0;
		soft = false;
		this.bet = bet;
		this.split = split;
		add(showCard);
		add(holeCard);
		pair = holeCard.getFace() == showCard.getFace();
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
		return size() == 2 && !split && (getValue() == 10 || getValue() == 11);
	}
	
	public boolean isBlackjack() {
		return size() == 2 && !split && getValue() == 21;
	}
	
	public Card getShowCard() {
		if(size() == 0) throw new RuntimeException("No hole card delt yet");
		return get(0);
	}
	
	@Override
	public boolean add(Card card) {
		if(card.getFace() == Face._A)
			soft = true;
		addValue(card.getFace().getValue());
		if(getValue() > 21 && isSoft()) {
			addValue(getValue() - 10);
			setSoft(false);
		}
		return super.add(card);
	}

	private void addValue(int value) {
		this.value += value;
	}

	public int getValue() {
		return value;
	}
	
	private void setSoft(boolean soft) {
		this.soft = soft;
	}
	
	public boolean isSoft() {
		return soft;
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
	
}
