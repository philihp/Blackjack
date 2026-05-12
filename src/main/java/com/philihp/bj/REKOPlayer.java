package com.philihp.bj;

import static com.philihp.bj.Response.DH;
import static com.philihp.bj.Response.DS;
import static com.philihp.bj.Response.H;
import static com.philihp.bj.Response.P;
import static com.philihp.bj.Response.S;

public class REKOPlayer implements Player {

    private static final Response[][] HARD = {
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
    private static final Response[][] SOFT = {
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
    private static final Response[][] PAIR = {
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

    private static final BasicStrategy STRATEGY = new BasicStrategy(HARD, SOFT, PAIR);

    private int count;

    @Override
    public int bet() {
        return count < 8 ? 1 : 750;
    }

    @Override
    public Response prompt(Hand playerHand, Hand dealerHand, boolean canSplit) {
        return STRATEGY.decide(playerHand, dealerHand, canSplit);
    }

    @Override
    public void observe(Card card) {
        count += switch (card) {
            case _2, _3, _4, _5, _6, _7 -> 1;
            case _8, _9 -> 0;
            case _T, _A -> -1;
        };
    }

    @Override
    public void resetCount(int decks) {
        count = switch (decks) {
            case 1 -> -1;
            case 2 -> -5;
            case 3, 4 -> -12;
            case 5, 6 -> -20;
            default -> -27;
        };
    }

    int getCount() {
        return count;
    }
}
