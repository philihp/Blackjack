package com.philihp.bj;

import java.util.ArrayList;
import java.util.List;
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

    private final GameConfig config;
    private final Random randomizer;
    private final Player player;
    private final Player dealer;

    public Blackjack(GameConfig config, Random randomizer, Player player) {
        this.config = config;
        this.randomizer = randomizer;
        this.player = player;
        this.dealer = new DealerPlayer(config.dealerSoft17());
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

            while ((float) deck.size() / deck.getInitialSize() > config.cutCardPenetration()) {
                handsPlayed++;

                List<Hand> playerHands = new ArrayList<>(1);
                Hand initial = newHand(player.bet(), deck.draw(), deck.draw(), false);
                playerHands.add(initial);
                Hand dealerHand = newHand(0, deck.draw(), deck.draw(), false);
                money -= playerHands.get(0).getBet();

                if (dealerHand.getValue() == 21) {
                    if (done.test(handsPlayed)) break outer;
                    continue;
                }

                money += playoutPlayer(deck, playerHands, dealerHand);
                playoutDealer(deck, dealerHand);

                for (Hand h : playerHands) {
                    money += payout(h, dealerHand);
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

    private float playoutPlayer(Deck deck, List<Hand> playerHands, Hand dealerHand) {
        int i = 0;
        float money = 0;
        do {
            Hand playerHand = playerHands.get(i);
            while (true) {
                Response response = player.prompt(
                        playerHand, dealerHand, playerHands.size() < config.limitOnResplits());

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
                    Hand left = newHand(playerHand.getBet(), playerHand.get(0), deck.draw(), true);
                    Hand right = newHand(playerHand.getBet(), playerHand.get(1), deck.draw(), true);
                    playerHands.set(i, left);
                    playerHands.add(right);
                    playerHand = left;
                }
            }
        } while (++i < playerHands.size());
        return money;
    }

    private void playoutDealer(Deck deck, Hand dealerHand) {
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

    private Hand newHand(int bet, Card holeCard, Card showCard, boolean split) {
        Hand h = new Hand(bet, holeCard, showCard, split);
        h.setDoubleAfterSplitAllowed(config.doubleAfterSplit());
        return h;
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
