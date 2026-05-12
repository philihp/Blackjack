package com.philihp.bj;

/**
 * Lookup-table-driven blackjack decision logic. Each instance owns its own
 * hard/soft/pair tables so individual player strategies can tune cells.
 */
public final class BasicStrategy {

    private final Response[][] hard;
    private final Response[][] soft;
    private final Response[][] pair;

    public BasicStrategy(Response[][] hard, Response[][] soft, Response[][] pair) {
        this.hard = hard;
        this.soft = soft;
        this.pair = pair;
    }

    public Response decide(Hand playerHand, Hand dealerHand, boolean canSplit) {
        int dealerCol = dealerHand.getShowCard().getTableOrdinal();

        if (playerHand.isPair() && canSplit) {
            return pair[playerHand.getShowCard().getTableOrdinal()][dealerCol];
        }
        if (!playerHand.isPair() && playerHand.isSoft()) {
            return soft[playerHand.getValue() - 13][dealerCol];
        }
        return hard[playerHand.getValue() - 4][dealerCol];
    }
}
