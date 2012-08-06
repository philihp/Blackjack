package com.philihp.bj;
public interface Player {

	public int bet();

	public Response prompt(Hand playerHand, Hand dealerHand);

}