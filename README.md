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

__Note__: These results use load factor of `1.0` (equals `capacity - 1` in code), which is totally unreal and
synthetic setting.

| Benchmark                                 | (count) | Mode | Cnt |      Score |   |     Error | Units |
|:----------------------------------------- | -------:|:----:| ---:| ----------:|:-:| ---------:|:----- |
| IntDictBenchmark.getClosed                |     127 | avgt |   5 |      0.989 | ± |     0.010 | us/op |
| IntDictBenchmark.getClosed                |   16535 | avgt |   5 |    357.963 | ± |   140.879 | us/op |
| IntDictBenchmark.getClosed                | 1048575 | avgt |   5 |  83261.233 | ± |  5591.723 | us/op |
| IntDictBenchmark.getOpen                  |     127 | avgt |   5 |      2.313 | ± |     0.018 | us/op |
| IntDictBenchmark.getOpen                  |   16535 | avgt |   5 |    356.559 | ± |   102.913 | us/op |
| IntDictBenchmark.getOpen                  | 1048575 | avgt |   5 |  25792.847 | ± |  1204.984 | us/op |
| IntDictBenchmark.missClosed               |     127 | avgt |   5 |      0.932 | ± |     0.009 | us/op |
| IntDictBenchmark.missClosed               |   16535 | avgt |   5 |    437.232 | ± |    23.184 | us/op |
| IntDictBenchmark.missClosed               | 1048575 | avgt |   5 | 111428.388 | ± |  1506.839 | us/op |
| IntDictBenchmark.missOpen                 |     127 | avgt |   5 |     12.692 | ± |     0.375 | us/op |
| IntDictBenchmark.missOpen                 |   16535 | avgt |   5 |    661.002 | ± |    93.186 | us/op |
| IntDictBenchmark.missOpen                 | 1048575 | avgt |   5 | 108710.498 | ± |  5165.610 | us/op |
| IntDictBenchmark.putClosed                |     127 | avgt |   5 |      4.992 | ± |     0.043 | us/op |
| IntDictBenchmark.putClosed                |   16535 | avgt |   5 |   1019.516 | ± |    24.240 | us/op |
| IntDictBenchmark.putClosed                | 1048575 | avgt |   5 | 241567.732 | ± |  9732.676 | us/op |
| IntDictBenchmark.putOpen                  |     127 | avgt |   5 |      4.442 | ± |     0.046 | us/op |
| IntDictBenchmark.putOpen                  |   16535 | avgt |   5 |   1153.167 | ± |    33.175 | us/op |
| IntDictBenchmark.putOpen                  | 1048575 | avgt |   5 | 197239.626 | ± |  1403.676 | us/op |
| IntDictBenchmark.putAllocatedClosed       |     127 | avgt |   5 |      2.568 | ± |     0.135 | us/op |
| IntDictBenchmark.putAllocatedClosed       |   16535 | avgt |   5 |    639.909 | ± |    70.176 | us/op |
| IntDictBenchmark.putAllocatedClosed       | 1048575 | avgt |   5 | 201603.683 | ± | 46738.860 | us/op |
| IntDictBenchmark.putAllocatedOpen         |     127 | avgt |   5 |      3.332 | ± |     0.057 | us/op |
| IntDictBenchmark.putAllocatedOpen         |   16535 | avgt |   5 |    772.453 | ± |   121.198 | us/op |
| IntDictBenchmark.putAllocatedOpen         | 1048575 | avgt |   5 | 164735.061 | ± |  1855.963 | us/op |
| IntDictBenchmark.putAndGetClosed          |     127 | avgt |   5 |      8.139 | ± |     0.090 | us/op |
| IntDictBenchmark.putAndGetClosed          |   16535 | avgt |   5 |   2148.704 | ± |    47.109 | us/op |
| IntDictBenchmark.putAndGetClosed          | 1048575 | avgt |   5 | 452310.557 | ± | 12042.556 | us/op |
| IntDictBenchmark.putAndGetOpen            |     127 | avgt |   5 |      6.905 | ± |     0.268 | us/op |
| IntDictBenchmark.putAndGetOpen            |   16535 | avgt |   5 |   1915.632 | ± |    38.699 | us/op |
| IntDictBenchmark.putAndGetOpen            | 1048575 | avgt |   5 | 343079.123 | ± | 11880.949 | us/op |
| IntDictBenchmark.putAndGetAllocatedClosed |     127 | avgt |   5 |      5.491 | ± |     0.329 | us/op |
| IntDictBenchmark.putAndGetAllocatedClosed |   16535 | avgt |   5 |   1542.745 | ± |   735.734 | us/op |
| IntDictBenchmark.putAndGetAllocatedClosed | 1048575 | avgt |   5 | 340482.744 | ± | 29770.310 | us/op |
| IntDictBenchmark.putAndGetAllocatedOpen   |     127 | avgt |   5 |     11.563 | ± |     0.099 | us/op |
| IntDictBenchmark.putAndGetAllocatedOpen   |   16535 | avgt |   5 |   1852.522 | ± |   619.203 | us/op |
| IntDictBenchmark.putAndGetAllocatedOpen   | 1048575 | avgt |   5 | 333182.320 | ± |  1811.787 | us/op |


### Description

- `get*(count)` -- gets `count` of random existing keys from pre-generated dictionary of 2^20 entries;
- `miss*(count)` -- gets `count` of random missing keys from pre-generated dictionary of 2^20 entries;
- `put*(count)` -- put into empty dict `count` of key-value pair from pre-generated arrays of 2^20 elements;
- `putAndGet*(count)` -- selects `count` of keys from pregenerated array, 
  shuffles them and puts 3/4 of them into empty dict, then shuffles again 
  and gets 3/4 of keys;
- `*Allocated*` -- the same as `put*|putAndGet*` but with capacity set to `count`.

### System specs:
- CPU: `2,3 GHz 8-Core Intel Core i9`
- RAM: `16 GB 2400 MHz DDR4`
- OS: `macOS Big Sur 11.2.3`
