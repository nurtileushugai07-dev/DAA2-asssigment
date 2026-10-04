# DAA Assignment 2 — Data Structures (In-Memory Workload Engine)

Own implementations of `DynamicArray`, `MyLinkedList` and `MinHeap` (all store primitive `int`),
with operation counters (steps / moves / comparisons), JUnit 5 tests, a benchmark and plots.

## Requirements

- JDK 25
- Maven
- Python 3 with `pandas` and `matplotlib` (only for plots): `pip install pandas matplotlib`

## Project structure

```
src/main/java/com/daa/   DynamicArray, MyLinkedList, MinHeap, IntList, OperationCounter, Benchmark
src/test/java/com/daa/   DynamicArrayTest, MyLinkedListTest, MinHeapTest
results/results.csv      benchmark output
results/plots/           PNG charts
generate_plots.py        builds charts from results.csv
REPORT.md                report
```

## Build

```
mvn compile
```

## Run tests

```
mvn test
```

## Run the benchmark

```
mvn compile
java -cp target/classes com.daa.Benchmark
```

Writes `results/results.csv` with columns
`workload,variant,structure,n,time_ms,steps,moves,comparisons`.

Settings: `Random(42)`, n = 100, 1 000, 10 000, 100 000, 1 warm-up run (discarded) and
5 measured runs per case, the median is saved. The benchmark can take a few minutes
(`MyLinkedList.get` on n = 100 000).

## Build the plots

```
python generate_plots.py
```

Charts are saved to `results/plots/`.

## Git

Branches: `main`, `feature/array`, `feature/list`, `feature/heap`, `feature/metrics`.
Release tag: `v1.0` (on `main`).