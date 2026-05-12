# Blackjack

Monte Carlo Blackjack simulator written in Java. Plays large numbers of hands
against a configurable dealer rule set using basic strategy or one of a few
card-counting strategies, and reports the resulting house edge.

## Requirements

- Java 21
- Maven 3.9+

## Build

```sh
mvn package
```

This produces a runnable shaded jar at `target/blackjack-1.0.0-SNAPSHOT.jar`.

## Run

```sh
# Run with defaults (5 seconds, random seed)
java -jar target/blackjack-1.0.0-SNAPSHOT.jar

# Run for 30 seconds with a fixed seed for reproducibility
java -jar target/blackjack-1.0.0-SNAPSHOT.jar 30 42
```

Output looks like:

```
Running for 30 seconds (seed=42)...
Hands Played:    1234567
Money:           -6420
Min-Bet:         10
House Edge %:    -0.052...
...in 30.01 seconds
```

`House Edge %` is the player's net result as a percentage of total wagered
min-bets (negative = the house wins on average, which is the normal outcome
for basic strategy without card counting).

## Test

```sh
mvn test
```

## Game configuration

Defaults (see `Blackjack.GameConfig#defaults`):

| Parameter            | Default | Notes                                        |
|----------------------|---------|----------------------------------------------|
| Shoe size            | 6 decks |                                              |
| Dealer on soft 17    | Hit     | `Response.H` or `Response.S`                 |
| Blackjack payout     | 3:2     |                                              |
| Double after split   | true    |                                              |
| Cut card penetration | 66.67%  | Deal until shoe is below this fraction       |
| Minimum bet          | 10      |                                              |
| Resplit limit        | 4 hands |                                              |

To run with different settings or a different player strategy, construct a
`Blackjack` instance directly:

```java
var config = Blackjack.GameConfig.defaults();
var sim = new Blackjack(config, new Random(42), new HiLoPlayer());
var result = sim.runForHands(1_000_000);
```

## Strategies

- **`ZeroMemoryPlayer`** — basic strategy lookup tables, no counting. Always
  bets the minimum.
- **`HiLoPlayer`** — Hi-Lo running count, bets 1 unit on a negative count and
  100 units when the count goes deeply negative.
- **`REKOPlayer`** — REKO variant of card counting, bets 1 vs 750.

All three delegate decision logic to the shared `BasicStrategy` class; only the
lookup tables and bet sizing differ.

## Dealer odds calculator

`DealerOddsCalculator` is a separate analysis tool that exhaustively computes
the dealer's probability distribution over final hand values, given each
possible up-card:

```sh
mvn exec:java -Dexec.mainClass=com.philihp.bj.DealerOddsCalculator
```
