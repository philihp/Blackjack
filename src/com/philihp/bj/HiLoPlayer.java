package com.philihp.bj;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.philihp.bj.Card.*;
import static com.philihp.bj.Response.*;

public class HiLoPlayer implements Player {
	
	private int count = 0;;

	private static final Response[][] HARD_RESPONSE = {
		{ H, H, H, H, H, H, H, H, H, H },
		{ H, H, H, H, H, H, H, H, H, H },
		{ H, H, H, H, H, H, H, H, H, H },
		{ H, H, H, H, H, H, H, H, H, H },
		{ H, H, H, H, H, H, H, H, H, H },
		{ H,DH,DH,DH,DH, H, H, H, H, H },
		{DH,DH,DH,DH,DH,DH,DH,DH, H, H },
		{DH,DH,DH,DH,DH,DH,DH,DH,DH, H },
		{ H, H, S, S, S, H, H, H, H, H },
		{ S, S, S, S, S, H, H, H, H, H },
		{ S, S, S, S, S, H, H, H, H, H },
		{ S, S, S, S, S, H, H, H, H, H },
		{ S, S, S, S, S, H, H, H, H, H },
		{ S, S, S, S, S, S, S, S, S, S },
		{ S, S, S, S, S, S, S, S, S, S },
		{ S, S, S, S, S, S, S, S, S, S },
		{ S, S, S, S, S, S, S, S, S, S },
		{ S, S, S, S, S, S, S, S, S, S }
	};
	private static final Response[][] SOFT_RESPONSE = {
		{ H, H, H,DH,DH, H, H, H, H, H },
		{ H, H, H,DH,DH, H, H, H, H, H },
		{ H, H,DH,DH,DH, H, H, H, H, H },
		{ H, H,DH,DH,DH, H, H, H, H, H },
		{ H,DH,DH,DH,DH, H, H, H, H, H },
		{DS,DS,DS,DS,DS, S, S, H, H, H },
		{ S, S, S, S,DS, S, S, S, S, S },
		{ S, S, S, S, S, S, S, S, S, S },
		{ S, S, S, S, S, S, S, S, S, S }
	};
	private static final Response[][] PAIR_RESPONSE = {
		{ P, P, P, P, P, P, H, H, H, H },
		{ P, P, P, P, P, P, H, H, H, H },
		{ H, H, H, P, P, H, H, H, H, H },
		{DH,DH,DH,DH,DH,DH,DH,DH, H, H },
		{ P, P, P, P, P, H, H, H, H, H },
		{ P, P, P, P, P, P, H, H, H, H },
		{ P, P, P, P, P, P, P, P, P, P },
		{ P, P, P, P, P, S, P, P, S, S },
		{ S, S, S, S, S, S, S, S, S, S },
		{ P, P, P, P, P, P, P, P, P, P }
	};
	
	public HiLoPlayer() {
	}

	public int bet() {
		if(count > -14) return 1;
		else return 100;
	}
	
	public Response prompt(Hand playerHand, Hand dealerHand, boolean canSplit) {
		Response response = null;
		if(playerHand.isPair()) {
			response = PAIR_RESPONSE
					[playerHand.getShowCard().getTableOrdinal()]
					[dealerHand.getShowCard().getTableOrdinal()];
		}
		else if(playerHand.isSoft()) {
			response = SOFT_RESPONSE
					[playerHand.getValue()-13]
					[dealerHand.getShowCard().getTableOrdinal()];
		}
		else {
			response = HARD_RESPONSE
					[playerHand.getValue()-4]
					[dealerHand.getShowCard().getTableOrdinal()];
		}

		if(response == Response.P && canSplit == false) {
			response = HARD_RESPONSE
					[playerHand.getValue()-4]
					[dealerHand.getShowCard().getTableOrdinal()];
		}
		
		return response;
	}
	
	public void notify(Card card) {
		switch(card) {
		case _2:
		case _3:
		case _4:
		case _5:
		case _6:
			count++;
			break;
		case _7:
		case _8:
		case _9:
			break;
		case _A:
		case _T:
		case _J:
		case _K:
		case _Q:
			count--;
		}
	}
	
	public void resetCount(int decks) {
		switch(decks) {
		case 1 : count = -1; break;
		case 2 : count = -5; break;
		case 3 :
		case 4 : count = -12; break;
		case 5 :
		case 6 : count = -20; break;
		default: count = -27; break;
		}
	}
	
}
