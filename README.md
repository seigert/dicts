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

| Benchmark                        | (count) | Mode | Cnt |      Score |   |      Error | Units |
|:-------------------------------- | -------:|:----:| ---:| ----------:|:-:| ----------:|:----- |
| IntDictBenchmark.getClosed       |     100 | avgt |   5 |      0.499 | ± |      0.222 | us/op |
| IntDictBenchmark.getClosed       |   10000 | avgt |   5 |    161.199 | ± |      5.456 | us/op |
| IntDictBenchmark.getClosed       | 1000000 | avgt |   5 |  72248.870 | ± |   1630.405 | us/op |
| IntDictBenchmark.getOpen         |     100 | avgt |   5 |      0.692 | ± |      0.002 | us/op |
| IntDictBenchmark.getOpen         |   10000 | avgt |   5 |     98.069 | ± |      1.692 | us/op |
| IntDictBenchmark.getOpen         | 1000000 | avgt |   5 |  28652.053 | ± |   4073.779 | us/op |
| IntDictBenchmark.putAndGetClosed |     100 | avgt |   5 |     11.272 | ± |      0.261 | us/op |
| IntDictBenchmark.putAndGetClosed |   10000 | avgt |   5 |   1063.505 | ± |     22.771 | us/op |
| IntDictBenchmark.putAndGetClosed | 1000000 | avgt |   5 | 505588.231 | ± | 226029.402 | us/op |
| IntDictBenchmark.putAndGetOpen   |     100 | avgt |   5 |     12.183 | ± |      0.457 | us/op |
| IntDictBenchmark.putAndGetOpen   |   10000 | avgt |   5 |   1156.424 | ± |     10.007 | us/op |
| IntDictBenchmark.putAndGetOpen   | 1000000 | avgt |   5 | 362075.122 | ± |  22836.840 | us/op |
| IntDictBenchmark.putClosed       |     100 | avgt |   5 |      4.392 | ± |      0.062 | us/op |
| IntDictBenchmark.putClosed       |   10000 | avgt |   5 |    647.702 | ± |    101.856 | us/op |
| IntDictBenchmark.putClosed       | 1000000 | avgt |   5 | 249306.398 | ± |  37794.823 | us/op |
| IntDictBenchmark.putOpen         |     100 | avgt |   5 |      4.171 | ± |      0.058 | us/op |
| IntDictBenchmark.putOpen         |   10000 | avgt |   5 |    701.909 | ± |     22.678 | us/op |
| IntDictBenchmark.putOpen         | 1000000 | avgt |   5 | 142789.894 | ± |   5158.147 | us/op |


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
