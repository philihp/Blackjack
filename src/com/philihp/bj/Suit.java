package com.philihp.bj;

public enum Suit {
	CLUB(""),
	DIAMOND(""),
	HEART(""),
	SPADE("");
	
	private String glyph;
	private Suit(String glyph) {
		this.glyph = glyph;
	}
	public String toString() {
		return glyph;
	}
}
