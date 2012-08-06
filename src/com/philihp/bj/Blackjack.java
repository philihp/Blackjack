package com.philihp.bj;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Blackjack {

	public static Random random;

	public static void main(String[] args) {
		if (args.length == 1) {
			random = new Random(Integer.parseInt(args[0]));
		} else {
			random = new Random();
		}


		Player player = new ZeroMemoryPlayer();
		Player dealer = new DealerPlayer();
		float money = 0; // 1,000,000
		int handsPlayed = 0;
		
		for(int j = 0; j < 1000000; j++) {
			Deck deck = new Deck(random, 6); // 6
			while (deck.size() * 2 > deck.getInitialSize()) {
				handsPlayed++;
	
				List<Hand> playerHands = new ArrayList<Hand>(1);
				playerHands.add(new Hand(player.bet(), deck.draw(), deck.draw(), false));
				Hand dealerHand = new Hand(deck.draw(), deck.draw());
				money -= playerHands.get(0).getBet();
				
				int i = 0;
				do {
					Hand playerHand = playerHands.get(i);
					dealPlayer: for (;;) {
						switch (player.prompt(playerHand, dealerHand)) {
						case DH:
							if(playerHand.canDoubleDown()) {
								money -= playerHand.getBet();
								playerHand.addBet(playerHand.getBet());
								playerHand.add(deck.draw());
								break dealPlayer;
							}
							// else just fall through to Hit
						case H:
							playerHand.add(deck.draw());
							break;
						case DS:
							if(playerHand.canDoubleDown()) {
								money -= playerHand.getBet();
								playerHand.addBet(playerHand.getBet());
								playerHand.add(deck.draw());
								break dealPlayer;
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
				
	
				dealDealer: for (;;) {
					switch (dealer.prompt(null, dealerHand)) {
					case H:
						dealerHand.add(deck.draw());
						break;
					case S:
						break dealDealer;
					}
	
					if (dealerHand.getValue() > 21) // bust!
						break dealDealer;
				}
	
				for(Hand playerHand : playerHands) {
					money += payout(playerHand, dealerHand);
				}
	
			}
		}
		
		System.out.println("Hands Played: "+handsPlayed);
		System.out.println("Net Money: "+money);
		System.out.println("House Edge: "+(money/handsPlayed));
	}

	private static float payout(Hand playerHand,
			Hand dealerHand) {
		if(playerHand.isBlackjack() && dealerHand.isBlackjack()) {
			//System.out.println("--- \tPush, both blackjacks");
			return playerHand.getBet();
		}
		else if(playerHand.isBlackjack() && dealerHand.isBlackjack() == false) {
			//System.out.println("Win \tPlayer BJ "+playerHand);
			return playerHand.getBet()*2.5f;
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
