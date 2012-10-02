package com.philihp.bj;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Blackjack {
	
	/**
	 * Number of decks in a shoe
	 */
	public static int SHOE_SIZE = 6;
	
	/**
	 * Dealer Hits or Stands on a soft 17
	 */
	public static Response SOFT17 = Response.H;
	
	/**
	 * Blackjack Payout
	 */
	public static float BLACKJACK_PAYOUT = 3f/2f;
	
	/**
	 * Can Double-Down after Split?
	 */
	public static boolean DOUBLE_AFTER_SPLIT = false;
	
	/**
	 * House limit on number of resplits (max this many hands)
	 */
	public static int LIMIT_ON_RESPLITS = 4;

	public static Random random;

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		if (args.length == 1) {
			random = new Random(Integer.parseInt(args[0]));
		} else {
			random = new Random();
		}


		Player player = new REKOPlayer();
		Player dealer = new DealerPlayer();
		float money = 0; // 1,000,000
		int handsPlayed = 0;
		
		for(int shoesPlayed = 0;shoesPlayed < 1000000; shoesPlayed++) {
			Deck deck = new Deck(random, SHOE_SIZE, player);
			player.resetCount(SHOE_SIZE);
			while ((float)deck.size() / deck.getInitialSize() > 0.50f) {
				handsPlayed++;
	
				List<Hand> playerHands = new ArrayList<Hand>(1);
				playerHands.add(new Hand(player.bet(), deck.draw(), deck.draw(), false));
				Hand dealerHand = new Hand(deck.draw(), deck.draw());
				money -= playerHands.get(0).getBet();
				
				money += playoutPlayer(player, deck, playerHands, dealerHand);
	
				playoutDealer(deck, dealer, dealerHand);
	
				for(Hand playerHand : playerHands) {
					money += payout(playerHand, dealerHand);
				}
			}
		}
		
		System.out.println("Hands Played:    "+handsPlayed);
		System.out.println("Money:           "+money);
		System.out.println("House Edge:      "+(-100*money/handsPlayed)+"%");
		System.out.println("...in "+((System.nanoTime()-startTime)/10000000/100f)+" seconds");
	}

	private static float playoutPlayer(Player player, Deck deck,
			List<Hand> playerHands, Hand dealerHand) {
		int i = 0;
		float money = 0;
		do {
			Hand playerHand = playerHands.get(i);
			dealPlayer: for (;;) {
				switch (player.prompt(playerHand, dealerHand, playerHands.size() < LIMIT_ON_RESPLITS)) {
				case DH:
					if(playerHand.canDoubleDown()) {
						int bet = playerHand.getBet();
						money -= bet;
						playerHand.addBet(bet);
						playerHand.add(deck.draw());
						break dealPlayer;
					}
					// else just fall through to Hit
				case H:
					playerHand.add(deck.draw());
					break;
				case DS:
					if(playerHand.canDoubleDown()) {
						int bet = playerHand.getBet();
						money -= bet;
						playerHand.addBet(bet);
					}
					// else just fall through to Stand
				case S:
					break dealPlayer;
				case P:
					money -= playerHand.getBet();
					Hand left = new Hand(playerHand.getBet(), playerHand.get(0), deck.draw(), true);
					Hand right = new Hand(playerHand.getBet(), playerHand.get(1), deck.draw(), true);
					playerHands.set(i--, left);
					playerHands.add(right);
					break dealPlayer;
				}

				if (playerHand.getValue() > 21) // bust!
					break dealPlayer;
			}
		}
		while(++i < playerHands.size());
		return money;
	}

	private static void playoutDealer(Deck deck, Player dealer, Hand dealerHand) {
		dealDealer: for (;;) {
			switch (dealer.prompt(null, dealerHand, false)) {
			case H:
				dealerHand.add(deck.draw());
				break;
			case S:
				break dealDealer;
			default:
				throw new RuntimeException("Dealer should never do anything but Hit or Stay. " + dealerHand);
			}

			if (dealerHand.getValue() > 21) // bust!
				break dealDealer;
		}
	}

	private static float payout(Hand playerHand,
			Hand dealerHand) {
		if(playerHand.isBlackjack() && dealerHand.isBlackjack()) {
			//System.out.println("--- \tPush, both blackjacks");
			return playerHand.getBet();
		}
		else if(playerHand.isBlackjack() && dealerHand.isBlackjack() == false) {
			//System.out.println("Win \tPlayer BJ "+playerHand);
			return playerHand.getBet()*(1+BLACKJACK_PAYOUT);
		}
		else if(playerHand.isBlackjack() == false && dealerHand.isBlackjack()) {
			//System.out.println("Lose \tDealer BJ "+dealerHand);
			return 0;
		}
		else if (playerHand.getValue() > 21) {
			//System.out.println("Lose \tPlayer Bust "+playerHand.getValue());
			return 0;
		}
		else if (dealerHand.getValue() > 21) {
			//System.out.println("Win \tDealer Bust "+dealerHand.getValue());
			return playerHand.getBet()*2;
		}
		else if(playerHand.getValue() > dealerHand.getValue()) {
			//System.out.println("Win \tplayer="+playerHand.getValue()+", dealer="+dealerHand.getValue());
			return playerHand.getBet()*2;
		}
		else if(playerHand.getValue() < dealerHand.getValue()) {
			//System.out.println("Lose \tplayer="+playerHand.getValue()+", dealer="+dealerHand.getValue());
			return 0;
		}
		else if (playerHand.getValue() == dealerHand.getValue()) {
			//System.out.println("--- \tPush @ " + dealerHand.getValue());
			return playerHand.getBet();
		}
		else throw new RuntimeException("Player: "+playerHand + ", Dealer: "+dealerHand);
	}


}
