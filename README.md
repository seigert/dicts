Naive Open/Closed Hashing
========================

Implementation of naive `int -> any` dictionaries with open or closed hashing
and benchmarks.

Prerequisites
-------------

1. Java  >= 1.8, [Java 11](https://openjdk.java.net/projects/jdk/11/) recommended:
   ```shell
   ~ > brew install openjdk@11
   ```

2. [SBT](https://www.scala-sbt.org/):
   ```shell
   ~ > brew install sbt
   ```

Usage
-----

1. To run benchmarks:
   ```shell
   ~ > sbt dicts-benchmark/jmh:run
   ```

2. To run tests if necessary:
   ```shell
   ~ > sbt test
   ```

Current results
---------------

| Benchmark                        | (count) | Mode | Cnt |      Score |   |     Error | Units |
|:-------------------------------- | -------:|:----:| ---:| ----------:|:-:| ---------:|:----- |
| IntDictBenchmark.getClosed       |     100 | avgt |   5 |      0.639 | ± |     0.047 | us/op |
| IntDictBenchmark.getClosed       |   10000 | avgt |   5 |    168.438 | ± |     5.173 | us/op |
| IntDictBenchmark.getClosed       | 1000000 | avgt |   5 |  74691.046 | ± |  3658.072 | us/op |
| IntDictBenchmark.getOpen         |     100 | avgt |   5 |      1.240 | ± |     0.589 | us/op |
| IntDictBenchmark.getOpen         |   10000 | avgt |   5 |    222.665 | ± |   182.425 | us/op |
| IntDictBenchmark.getOpen         | 1000000 | avgt |   5 |  84564.127 | ± | 34570.821 | us/op |
| IntDictBenchmark.putAndGetClosed |     100 | avgt |   5 |     12.756 | ± |     2.507 | us/op |
| IntDictBenchmark.putAndGetClosed |   10000 | avgt |   5 |   1119.816 | ± |    23.877 | us/op |
| IntDictBenchmark.putAndGetClosed | 1000000 | avgt |   5 | 411801.810 | ± | 19206.254 | us/op |
| IntDictBenchmark.putAndGetOpen   |     100 | avgt |   5 |     12.721 | ± |     0.392 | us/op |
| IntDictBenchmark.putAndGetOpen   |   10000 | avgt |   5 |   1173.760 | ± |   148.190 | us/op |
| IntDictBenchmark.putAndGetOpen   | 1000000 | avgt |   5 | 370752.864 | ± | 25351.316 | us/op |
| IntDictBenchmark.putClosed       |     100 | avgt |   5 |      4.567 | ± |     0.476 | us/op |
| IntDictBenchmark.putClosed       |   10000 | avgt |   5 |    633.909 | ± |    52.791 | us/op |
| IntDictBenchmark.putClosed       | 1000000 | avgt |   5 | 242968.389 | ± | 21246.547 | us/op |
| IntDictBenchmark.putOpen         |     100 | avgt |   5 |      3.984 | ± |     0.585 | us/op |
| IntDictBenchmark.putOpen         |   10000 | avgt |   5 |    662.746 | ± |    37.432 | us/op |
| IntDictBenchmark.putOpen         | 1000000 | avgt |   5 | 131030.565 | ± |  8267.218 | us/op |


### Description

- `get*(count)` -- gets `count` of random keys from pre-generated dictionary of 2^20 entries;
- `put*(count)` -- put into empty dict `count` of key-value pair from pre-generated arrays of 2^20 elements;
- `putAndGet*(count)` -- selects `count` of keys from pregenerated array, 
  shuffles them and puts 3/4 of them into empty dict, then shuffles again 
  and gets 3/4 of keys.

### System specs:
- CPU: `2,3 GHz 8-Core Intel Core i9`
- RAM: `16 GB 2400 MHz DDR4`
- OS: `macOS Big Sur 11.2.3`
