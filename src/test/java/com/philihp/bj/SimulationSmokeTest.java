package com.philihp.bj;

import com.philihp.bj.Blackjack.GameConfig;
import com.philihp.bj.Blackjack.Result;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimulationSmokeTest {

    private static final long HANDS = 5_000;

    @Test
    void seededRunIsReproducible() {
        Result r1 = run(42L);
        Result r2 = run(42L);
        assertEquals(r1.handsPlayed(), r2.handsPlayed());
        assertEquals(r1.money(), r2.money());
    }

    @Test
    void zeroMemoryHouseEdgeInPlausibleRange() {
        Result r = run(2024L);
        assertTrue(r.handsPlayed() > 0, "should have played at least one hand");
        double edge = r.houseEdgePercent();
        // Wide sanity band — 5000 hands has huge variance, we only guard
        // against catastrophic regressions.
        assertTrue(edge > -50 && edge < 50,
                "house edge %% out of sanity range: " + edge);
    }

    @Test
    void hiLoSimulationRunsWithoutError() {
        Blackjack sim = new Blackjack(GameConfig.defaults(), new Random(99L), new HiLoPlayer());
        Result r = sim.runForHands(HANDS);
        assertTrue(r.handsPlayed() >= HANDS);
    }

    @Test
    void rekoSimulationRunsWithoutError() {
        Blackjack sim = new Blackjack(GameConfig.defaults(), new Random(99L), new REKOPlayer());
        Result r = sim.runForHands(HANDS);
        assertTrue(r.handsPlayed() >= HANDS);
    }

    private Result run(long seed) {
        GameConfig config = GameConfig.defaults();
        Blackjack sim = new Blackjack(config, new Random(seed), new ZeroMemoryPlayer(config.minBet()));
        return sim.runForHands(HANDS);
    }
}
