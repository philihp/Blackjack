package com.philihp.bj;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.philihp.bj.Face.*;
import static com.philihp.bj.Response.*;

public class ZeroMemoryPlayer implements Player {

	private static final Response[][] HARD_RESPONSE = {
		{ H, H, H, H, H, H, H, H, H, H },
		{ H, H, H, H, H, H, H, H, H, H },
		{ H, H, H, H, H, H, H, H, H, H },
		{ H, H, H, H, H, H, H, H, H, H },
		{ H, H, H, H, H, H, H, H, H, H },
		{ H,DH,DH,DH,DH, H, H, H, H, H },
		{DH,DH,DH,DH,DH,DH,DH,DH, H, H },
		{DH,DH,DH,DH,DH,DH,DH,DH,DH,DH },
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
		{ H, H, P, P, P, P, H, H, H, H },
		{ H, H, P, P, P, P, H, H, H, H },
		{ H, H, H, H, H, H, H, H, H, H },
		{DH,DH,DH,DH,DH,DH,DH,DH, H, H },
		{ H, P, P, P, P, H, H, H, H, H },
		{ P, P, P, P, P, P, H, H, H, H },
		{ P, P, P, P, P, P, P, P, P, P },
		{ P, P, P, P, P, S, P, P, S, S },
		{ S, S, S, S, S, S, S, S, S, S },
		{ P, P, P, P, P, P, P, P, P, P }
	};
	
	public ZeroMemoryPlayer() {
	}

	public int bet() {
		return 1; // 15
		//max is 2000
	}
	
	public Response prompt(Hand playerHand, Hand dealerHand) {
		//System.out.print("P: "+playerHand+" ("+playerHand.getValue()+")\t D: "+dealerHand.get(0).getFace()+" "+(playerHand.isSplit()?"split":"unsplit"));
		Response r = sub(playerHand, dealerHand);
		//System.out.println("\t ==> "+r);
		return r;
	}

	private Response sub(Hand playerHand, Hand dealerHand) {
		//player may double on 10 and 11 only
		if(playerHand.isPair()) {
			return PAIR_RESPONSE
					[playerHand.getShowCard().getFace().getTableOrdinal()]
					[dealerHand.getShowCard().getFace().getTableOrdinal()];
		}
		else if(playerHand.isSoft()) {
			return SOFT_RESPONSE
					[playerHand.getValue()-13]
					[dealerHand.getShowCard().getFace().getTableOrdinal()];
		}
		else {
			if(playerHand.getValue()-4 == 30) System.out.println(playerHand);
			return HARD_RESPONSE
					[playerHand.getValue()-4]
					[dealerHand.getShowCard().getFace().getTableOrdinal()];
		}
	}
	
}
