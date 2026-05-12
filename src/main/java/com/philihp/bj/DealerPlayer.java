package com.philihp.bj;

public class DealerPlayer implements Player {

    private final Response soft17;

    public DealerPlayer(Response soft17) {
        if (soft17 != Response.H && soft17 != Response.S) {
            throw new IllegalArgumentException("Dealer soft-17 rule must be H or S, got " + soft17);
        }
        this.soft17 = soft17;
    }

    @Override
    public int bet() {
        throw new UnsupportedOperationException("Dealer doesn't bet");
    }

    @Override
    public Response prompt(Hand playerHand, Hand dealerHand, boolean canSplit) {
        int v = dealerHand.getValue();
        if (v < 17) return Response.H;
        if (v > 17) return Response.S;
        return dealerHand.isSoft() ? soft17 : Response.S;
    }

    @Override
    public void observe(Card card) {
        // dealer doesn't track count
    }

    @Override
    public void resetCount(int decks) {
        // dealer doesn't track count
    }
}
