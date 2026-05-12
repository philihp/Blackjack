package com.philihp.bj.bench;

import com.philihp.bj.Blackjack;
import com.philihp.bj.Blackjack.GameConfig;
import com.philihp.bj.Blackjack.Result;
import com.philihp.bj.ZeroMemoryPlayer;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, jvmArgsAppend = {"-Xms512m", "-Xmx512m"})
@State(Scope.Benchmark)
public class BlackjackBenchmark {

    static final int HANDS_PER_OP = 50_000;

    @Param({"42"})
    public long seed;

    private GameConfig config;

    @Setup
    public void setup() {
        config = GameConfig.defaults();
    }

    @Benchmark
    @OperationsPerInvocation(HANDS_PER_OP)
    public void zeroMemory_50kHands(Blackhole bh) {
        Blackjack sim = new Blackjack(config, new Random(seed), new ZeroMemoryPlayer(config.minBet()));
        Result r = sim.runForHands(HANDS_PER_OP);
        bh.consume(r);
    }
}
