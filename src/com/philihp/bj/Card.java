package com.philihp.bj;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Card {

	private final Face face;
	private final Suit suit;
	
	public Card(Suit suit, Face face) {
		this.suit = suit;
		this.face = face;
	}

	public Face getFace() {
		return this.face;
	}
	public Suit getSuit() {
		return this.suit;
	}

	public String toString() {
		return ""+suit.toString()+face.toString()+"";
	}
	
}
