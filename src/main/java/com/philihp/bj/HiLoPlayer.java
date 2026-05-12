package com.philihp.bj;

import static com.philihp.bj.Response.DH;
import static com.philihp.bj.Response.DS;
import static com.philihp.bj.Response.H;
import static com.philihp.bj.Response.P;
import static com.philihp.bj.Response.RH;
import static com.philihp.bj.Response.S;

public class HiLoPlayer implements Player {

    private static final Response[][] HARD = {
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
    private static final Response[][] SOFT = {
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
    private static final Response[][] PAIR = {
        { P, P, P, P, P, P, H, H, H, H }, //2,2
        { P, P, P, P, P, P, H, H, H, H }, //3,3
        { H, H, H, P, P, H, H, H, H, H }, //4,4
        {DH,DH,DH,DH,DH,DH,DH,DH, H, H }, //5,5
        { P, P, P, P, P, H, H, H, H, H }, //6,6
        { P, P, P, P, P, P, H, H, H, H }, //7,7
        { P, P, P, P, P, P, P, P, P, P }, //8,8
        { P, P, P, P, P, S, P, P, S, S }, //9,9
        { S, S, S, S, S, S, S, S, S, S }, //T,T
        { P, P, P, P, P, P, P, P, P, P }  //A,A
    };

    private static final BasicStrategy STRATEGY = new BasicStrategy(HARD, SOFT, PAIR);

    private int count;

    @Override
    public int bet() {
        return count > -14 ? 1 : 100;
    }

    @Override
    public Response prompt(Hand playerHand, Hand dealerHand, boolean canSplit) {
        return STRATEGY.decide(playerHand, dealerHand, canSplit);
    }

    @Override
    public void observe(Card card) {
        count += switch (card) {
            case _2, _3, _4, _5, _6 -> 1;
            case _7, _8, _9 -> 0;
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
