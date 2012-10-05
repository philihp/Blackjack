package com.philihp.bj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Stack;
import java.util.TreeMap;

public class DealerOddsCalculator {
	
	public static class State implements Cloneable, Comparable<State> {
		
		public Hand hand;
		public Deck deck;
		public double probability;
		
		public State() {
			hand = new Hand();
			deck = new Deck(Blackjack.SHOE_SIZE, null);
			probability = 0;
		}
		
		private State(boolean b) {
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {
			State newState = new State(false);
			newState.hand = (Hand)hand.clone();
			newState.deck = (Deck)deck.clone();
			return newState;
		}

		@Override
		public int compareTo(State o) {
			if(o.probability < this.probability) return -1;
			else if(o.probability > this.probability) return 1;
			else return 0;
		}
		
		
		
	}
	
	public static void main(String[] args) throws CloneNotSupportedException {
		
		final Random rnd = new Random();

		Stack<State> unfinishedHands = new Stack<State>();
		Stack<State> finishedHands = new Stack<State>();
		
		for(final Card c : Card.values()) {
			State state = new State() {{
				probability = deck.getProbability(c);
				deck.remove(c);
				deck.shuffle(rnd);
				hand.add(c);
			}};
			unfinishedHands.add(state);
		}
		
		while(unfinishedHands.isEmpty() == false) {
			
			State state = unfinishedHands.pop();
			
			if(state.hand.getValue() >= 18 ||
			   state.hand.getValue() == 17 && state.hand.isSoft() == false ||
			   state.hand.getValue() == 17 && state.hand.isSoft() == true && Blackjack.SOFT17 == Response.S) { 
				// is it a bust, or >= 17? if so, stay
				finishedHands.push(state);
			}
			else {
				// otherwise show possibilities for all hits
				for(Card c : Card.values()) {
					State newState = (State)state.clone();
					newState.probability = state.probability * newState.deck.getProbability(c);
					newState.deck.remove(c);
					newState.deck.shuffle(rnd);
					newState.hand.add(c);
					unfinishedHands.push(newState);
				}
			}
		}
		
		Collections.sort(finishedHands);
		
		Map<Card,Map<Integer,Double>> probs = new TreeMap<Card,Map<Integer,Double>>() {{
			for(Card c : Card.values()) {
				Map<Integer,Double> submap = new TreeMap<Integer,Double>() {{
					for(int v = 17; v <= 21; v++) {
						put(v,0d);
					}
					put(-1, 0d);
					put(99, 0d);
				}};
				put(c, submap);
			}
		}};
		
		for(State state : finishedHands) {
			int value = state.hand.getValue();
			if(value > 21) value = -1; //-1 is bust
			if(state.hand.isBlackjack()) value = 99; // 99 is blackjack
			double baseProb = probs.get(state.hand.get(0)).get(value);
			probs.get(state.hand.get(0)).put(value, baseProb + state.probability);
		}
		
		for(Entry<Card,Map<Integer,Double>> e : probs.entrySet()) {
			for(Entry<Integer,Double> e2 : e.getValue().entrySet()) {
				System.out.println(e.getKey()+" "+e2.getKey() + " " + e2.getValue());
			}
		}
		
	}

}
