package com.philihp.bj;

public class DealerPlayer implements Player {
	
	public DealerPlayer() {
	}

	public int bet() {
		throw new RuntimeException("Dealer doesn't bet.");
	}
	
	public Response prompt(Hand playerHand, Hand dealerHand) {
		if(dealerHand.getValue() < 17) return Response.H;
		else if(dealerHand.getValue() > 17) return Response.S;
		else if(dealerHand.isSoft()) return Response.H;
		else return Response.S;
	}
	
	
}
