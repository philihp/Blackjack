package com.philihp.bj;

import java.util.Random;

public final class Blackjack {

    public record GameConfig(
            int shoeSize,
            Response dealerSoft17,
            float blackjackPayout,
            boolean doubleAfterSplit,
            float cutCardPenetration,
            int minBet,
            int limitOnResplits
    ) {
        public static GameConfig defaults() {
            return new GameConfig(6, Response.H, 1.5f, true, 0.66667f, 10, 4);
        }
    }

    public record Result(long handsPlayed, long money, double houseEdgePercent, double elapsedSeconds) { }

    private static final int MAX_PLAYER_HANDS = 8;

    private final GameConfig config;
    private final Random randomizer;
    private final Player player;
    private final Player dealer;

    private final Hand[] playerHandPool = new Hand[MAX_PLAYER_HANDS];
    private final Hand dealerHand = new Hand();
    private int numHands;

    public Blackjack(GameConfig config, Random randomizer, Player player) {
        this.config = config;
        this.randomizer = randomizer;
        this.player = player;
        this.dealer = new DealerPlayer(config.dealerSoft17());
        for (int i = 0; i < MAX_PLAYER_HANDS; i++) {
            playerHandPool[i] = new Hand();
        }
    }

    public Result runForSeconds(double seconds) {
        long startTime = System.nanoTime();
        return runUntil(handsPlayed -> elapsedSeconds(startTime) > seconds, startTime);
    }

    public Result runForHands(long maxHands) {
        long startTime = System.nanoTime();
        return runUntil(handsPlayed -> handsPlayed >= maxHands, startTime);
    }

    private Result runUntil(java.util.function.LongPredicate done, long startTime) {
        long money = 0;
        long handsPlayed = 0;

        outer:
        while (true) {
            Deck deck = new Deck(config.shoeSize(), player);
            deck.shuffle(randomizer);
            player.resetCount(config.shoeSize());
            int cutCardThreshold = (int) (deck.getInitialSize() * config.cutCardPenetration());

            while (deck.size() > cutCardThreshold) {
                handsPlayed++;

                numHands = 1;
                Hand initial = playerHandPool[0];
                resetHand(initial, player.bet(), deck.draw(), deck.draw(), false);
                resetHand(dealerHand, 0, deck.draw(), deck.draw(), false);
                money -= initial.getBet();

                if (dealerHand.getValue() == 21) {
                    if (done.test(handsPlayed)) break outer;
                    continue;
                }

                money += playoutPlayer(deck);
                playoutDealer(deck);

                for (int i = 0; i < numHands; i++) {
                    money += payout(playerHandPool[i], dealerHand);
                }

                if (done.test(handsPlayed)) break outer;
            }

            if (done.test(handsPlayed)) break;
        }

        double elapsed = elapsedSeconds(startTime);
        double houseEdge = handsPlayed == 0 ? 0.0
                : 100.0 * (double) money / (handsPlayed * (long) config.minBet());
        return new Result(handsPlayed, money, houseEdge, elapsed);
    }

    int payout(Hand playerHand, Hand dealerHand) {
        if (playerHand.isSurrendered()) {
            return playerHand.getBet() / 2;
        }
        boolean playerBJ = playerHand.isBlackjack();
        boolean dealerBJ = dealerHand.isBlackjack();
        if (playerBJ && dealerBJ) return playerHand.getBet();
        if (playerBJ) return (int) (playerHand.getBet() * (1 + config.blackjackPayout()));
        if (dealerBJ) return 0;
        int pv = playerHand.getValue();
        int dv = dealerHand.getValue();
        if (pv > 21) return 0;
        if (dv > 21) return playerHand.getBet() * 2;
        if (pv > dv) return playerHand.getBet() * 2;
        if (pv < dv) return 0;
        return playerHand.getBet();
    }

    private float playoutPlayer(Deck deck) {
        int i = 0;
        float money = 0;
        do {
            Hand playerHand = playerHandPool[i];
            while (true) {
                Response response = player.prompt(
                        playerHand, dealerHand, numHands < config.limitOnResplits());

                if (response == Response.RH) {
                    if (playerHand.size() == 2 && !playerHand.isSplit()) {
                        playerHand.surrender();
                        break;
                    }
                    response = Response.H;
                }
                if (response == Response.DH) {
                    if (playerHand.canDoubleDown()) {
                        int bet = playerHand.getBet();
                        money -= bet;
                        playerHand.addBet(bet);
                        playerHand.add(deck.draw());
                        break;
                    }
                    response = Response.H;
                }
                if (response == Response.H) {
                    playerHand.add(deck.draw());
                    if (playerHand.getValue() > 21) break;
                    continue;
                }
                if (response == Response.DS) {
                    if (playerHand.canDoubleDown()) {
                        int bet = playerHand.getBet();
                        money -= bet;
                        playerHand.addBet(bet);
                    }
                    response = Response.S;
                }
                if (response == Response.S) {
                    break;
                }
                if (response == Response.P) {
                    money -= playerHand.getBet();
                    Card leftCard = playerHand.get(0);
                    Card rightCard = playerHand.get(1);
                    int bet = playerHand.getBet();
                    Hand left = playerHand;
                    Hand right = playerHandPool[numHands++];
                    resetHand(left, bet, leftCard, deck.draw(), true);
                    resetHand(right, bet, rightCard, deck.draw(), true);
                }
            }
        } while (++i < numHands);
        return money;
    }

    private void playoutDealer(Deck deck) {
        while (true) {
            Response response = dealer.prompt(null, dealerHand, false);
            if (response == Response.H) {
                dealerHand.add(deck.draw());
                if (dealerHand.getValue() > 21) break;
            } else if (response == Response.S) {
                break;
            } else {
                throw new IllegalStateException("Dealer should only Hit or Stay. " + dealerHand);
            }
        }
    }

    private void resetHand(Hand h, int bet, Card holeCard, Card showCard, boolean split) {
        h.reset(bet, holeCard, showCard, split);
        h.setDoubleAfterSplitAllowed(config.doubleAfterSplit());
    }

    private static double elapsedSeconds(long startTime) {
        return (System.nanoTime() - startTime) / 1_000_000_000.0;
    }

    public static void main(String[] args) {
        int seconds = args.length > 0 ? Integer.parseInt(args[0]) : 5;
        long seed = args.length > 1 ? Long.parseLong(args[1]) : System.nanoTime();

        System.out.println("Running for " + seconds + " seconds (seed=" + seed + ")...");

        GameConfig config = GameConfig.defaults();
        Random rng = new Random(seed);
        Player player = new ZeroMemoryPlayer(config.minBet());
        Blackjack sim = new Blackjack(config, rng, player);
        Result r = sim.runForSeconds(seconds);

        System.out.println("Hands Played:    " + r.handsPlayed());
        System.out.println("Money:           " + r.money());
        System.out.println("Min-Bet:         " + config.minBet());
        System.out.println("House Edge %:    " + r.houseEdgePercent());
        System.out.println("...in " + r.elapsedSeconds() + " seconds");
    }
}
