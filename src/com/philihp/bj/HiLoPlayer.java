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
		{ H, H, H, H, H, H, H, H, H, H }, //4
		{ H, H, H, H, H, H, H, H, H, H }, //5
		{ H, H, H, H, H, H, H, H, H, H }, //6
		{ H, H, H, H, H, H, H, H, H, H }, //7
		{ H, H, H, H, H, H, H, H, H, H }, //8
		{ H,DH,DH,DH,DH, H, H, H, H, H }, //9
		{DH,DH,DH,DH,DH,DH,DH,DH, H, H }, //10
		{DH,DH,DH,DH,DH,DH,DH,DH,DH,DH }, //11
		{ H, H, S, S, S, H, H, H, H, H }, //12
		{ S, S, S, S, S, H, H, H, H, H }, //13
		{ S, S, S, S, S, H, H, H, H, H }, //14
		{ S, S, S, S, S, H, H, H,RH, H }, //15
		{ S, S, S, S, S, H, H,RH,RH,RH }, //16
		{ S, S, S, S, S, S, S, S, S, S }, //17
		{ S, S, S, S, S, S, S, S, S, S }, //18
		{ S, S, S, S, S, S, S, S, S, S }, //19
		{ S, S, S, S, S, S, S, S, S, S }, //20
		{ S, S, S, S, S, S, S, S, S, S }  //21
	};
	private static final Response[][] SOFT_RESPONSE = {
		{ H, H, H,DH,DH, H, H, H, H, H }, //13
		{ H, H, H,DH,DH, H, H, H, H, H }, //14
		{ H, H,DH,DH,DH, H, H, H, H, H }, //15
		{ H, H,DH,DH,DH, H, H, H, H, H }, //16
		{ H,DH,DH,DH,DH, H, H, H, H, H }, //17
		{ S,DS,DS,DS,DS, S, S, H, H, H }, //18
		{ S, S, S, S, S, S, S, S, S, S }, //19
		{ S, S, S, S, S, S, S, S, S, S }, //20
		{ S, S, S, S, S, S, S, S, S, S }  //21
	};
	private static final Response[][] PAIR_RESPONSE = {
		{ P, P, P, P, P, P, H, H, H, H }, //2
		{ P, P, P, P, P, P, H, H, H, H }, //3
		{ H, H, H, P, P, H, H, H, H, H }, //4
		{DH,DH,DH,DH,DH,DH,DH,DH, H, H }, //5
		{ P, P, P, P, P, H, H, H, H, H }, //6
		{ P, P, P, P, P, P, H, H, H, H }, //7
		{ P, P, P, P, P, P, P, P, P, P }, //8
		{ P, P, P, P, P, S, P, P, S, S }, //9
		{ S, S, S, S, S, S, S, S, S, S }, //T
		{ P, P, P, P, P, P, P, P, P, P }  //A
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
