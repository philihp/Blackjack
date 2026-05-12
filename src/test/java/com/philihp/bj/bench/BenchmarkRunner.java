package com.philihp.bj.bench;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        OptionsBuilder builder = new OptionsBuilder();
        builder.include(BlackjackBenchmark.class.getSimpleName());
        if (args.length > 0) {
            builder.resultFormat(ResultFormatType.JSON);
            builder.result(args[0]);
        }
        Options opt = builder.build();
        new Runner(opt).run();
    }
}
