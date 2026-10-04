package com.daa;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Benchmark {

    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};
    private static final int WARMUP_RUNS = 1;
    private static final int MEASURED_RUNS = 5;

    public static class ResultEntry {
        String workload;
        String variant;
        String structure;
        int n;
        double timeMs;
        long steps;
        long moves;
        long comparisons;

        public ResultEntry(String workload, String variant, String structure, int n, double timeMs, long steps, long moves, long comparisons) {
            this.workload = workload;
            this.variant = variant;
            this.structure = structure;
            this.n = n;
            this.timeMs = timeMs;
            this.steps = steps;
            this.moves = moves;
            this.comparisons = comparisons;
        }
    }

    public static void main(String[] args) {
        List<ResultEntry> allResults = new ArrayList<>();

        File resultsDir = new File("results");
        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }

        for (int n : SIZES) {
            System.out.println("Running benchmarks for n = " + n + "...");

            runListWorkload(allResults, "W1", "-", "DynamicArray", n);
            runListWorkload(allResults, "W1", "-", "MyLinkedList", n);

            runSearchWorkload(allResults, "W2", "-", "DynamicArray", n);
            runSearchWorkload(allResults, "W2", "-", "MyLinkedList", n);

            runInsertRemoveWorkload(allResults, "W3", "head", "DynamicArray", n);
            runInsertRemoveWorkload(allResults, "W3", "head", "MyLinkedList", n);
            runInsertRemoveWorkload(allResults, "W3", "middle", "DynamicArray", n);
            runInsertRemoveWorkload(allResults, "W3", "middle", "MyLinkedList", n);

            runHeapWorkload(allResults, "W4", "-", "MinHeap", n);
        }

        try (FileWriter writer = new FileWriter("results/results.csv")) {
            writer.write("workload,variant,structure,n,time_ms,steps,moves,comparisons\n");
            for (ResultEntry r : allResults) {
                writer.write(String.format(Locale.US, "%s,%s,%s,%d,%.4f,%d,%d,%d\n",
                        r.workload, r.variant, r.structure, r.n, r.timeMs, r.steps, r.moves, r.comparisons));
            }
            System.out.println("Benchmark finished successfully! Results saved to results/results.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static IntList createStructure(String name, int capacity, OperationCounter counter) {
        if ("DynamicArray".equals(name)) {
            return new DynamicArray(capacity, counter);
        } else if ("MyLinkedList".equals(name)) {
            return new MyLinkedList(counter);
        }
        throw new IllegalArgumentException("Unknown structure: " + name);
    }

    private static void runListWorkload(List<ResultEntry> results, String workload, String variant, String structName, int n) {
        List<double[]> times = new ArrayList<>();
        List<long[]> countersList = new ArrayList<>();

        for (int run = 0; run < WARMUP_RUNS + MEASURED_RUNS; run++) {
            OperationCounter counter = new OperationCounter();
            IntList list = createStructure(structName, n, counter);
            Random randGen = new Random(42);

            // Заполнение
            for (int i = 0; i < n; i++) {
                list.add(randGen.nextInt(1_000_000));
            }

            // Генерируем 10 000 случайных индексов для get
            int[] indices = new int[10_000];
            for (int i = 0; i < 10_000; i++) {
                indices[i] = randGen.nextInt(n);
            }

            counter.reset();
            long start = System.nanoTime();
            for (int i = 0; i < 10_000; i++) {
                list.get(indices[i]);
            }
            long end = System.nanoTime();

            if (run >= WARMUP_RUNS) {
                times.add(new double[]{(end - start) / 1_000_000.0});
                countersList.add(new long[]{counter.getSteps(), counter.getMoves(), counter.getComparisons()});
            }
        }
        saveMedian(results, workload, variant, structName, n, times, countersList);
    }

    private static void runSearchWorkload(List<ResultEntry> results, String workload, String variant, String structName, int n) {
        List<double[]> times = new ArrayList<>();
        List<long[]> countersList = new ArrayList<>();

        for (int run = 0; run < WARMUP_RUNS + MEASURED_RUNS; run++) {
            OperationCounter counter = new OperationCounter();
            IntList list = createStructure(structName, n, counter);
            Random randGen = new Random(42);

            int[] values = new int[n];
            for (int i = 0; i < n; i++) {
                values[i] = randGen.nextInt(1_000_000);
                list.add(values[i]);
            }

            int[] queries = new int[1_000];
            for (int i = 0; i < 500; i++) {
                queries[i] = values[randGen.nextInt(n)];
                queries[500 + i] = -(randGen.nextInt(1_000_000) + 1);
            }

            counter.reset();
            long start = System.nanoTime();
            for (int i = 0; i < 1_000; i++) {
                list.contains(queries[i]);
            }
            long end = System.nanoTime();

            if (run >= WARMUP_RUNS) {
                times.add(new double[]{(end - start) / 1_000_000.0});
                countersList.add(new long[]{counter.getSteps(), counter.getMoves(), counter.getComparisons()});
            }
        }
        saveMedian(results, workload, variant, structName, n, times, countersList);
    }

    private static void runInsertRemoveWorkload(List<ResultEntry> results, String workload, String variant, String structName, int n) {
        List<double[]> times = new ArrayList<>();
        List<long[]> countersList = new ArrayList<>();

        for (int run = 0; run < WARMUP_RUNS + MEASURED_RUNS; run++) {
            OperationCounter counter = new OperationCounter();
            IntList list = createStructure(structName, n, counter);
            Random randGen = new Random(42);

            for (int i = 0; i < n; i++) {
                list.add(randGen.nextInt(1_000_000));
            }

            int targetIndex = "head".equals(variant) ? 0 : list.size() / 2;

            counter.reset();
            long start = System.nanoTime();

            for (int i = 0; i < 1_000; i++) {
                list.add(targetIndex, randGen.nextInt(1_000_000));
            }
            for (int i = 0; i < 1_000; i++) {
                list.remove(targetIndex);
            }
            long end = System.nanoTime();

            if (run >= WARMUP_RUNS) {
                times.add(new double[]{(end - start) / 1_000_000.0});
                countersList.add(new long[]{counter.getSteps(), counter.getMoves(), counter.getComparisons()});
            }
        }
        saveMedian(results, workload, variant, structName, n, times, countersList);
    }

    private static void runHeapWorkload(List<ResultEntry> results, String workload, String variant, String structName, int n) {
        List<double[]> times = new ArrayList<>();
        List<long[]> countersList = new ArrayList<>();

        for (int run = 0; run < WARMUP_RUNS + MEASURED_RUNS; run++) {
            OperationCounter counter = new OperationCounter();
            MinHeap heap = new MinHeap(n, counter);
            Random randGen = new Random(42);

            int[] values = new int[n];
            for (int i = 0; i < n; i++) {
                values[i] = randGen.nextInt(1_000_000);
            }

            counter.reset();
            long start = System.nanoTime();

            for (int i = 0; i < n; i++) {
                heap.insert(values[i]);
            }

            int prev = Integer.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                int curr = heap.extractMin();
                if (curr < prev) {
                    throw new IllegalStateException("Heap order violated: " + curr + " < " + prev);
                }
                prev = curr;
            }

            long end = System.nanoTime();

            if (run >= WARMUP_RUNS) {
                times.add(new double[]{(end - start) / 1_000_000.0});
                countersList.add(new long[]{counter.getSteps(), counter.getMoves(), counter.getComparisons()});
            }
        }
        saveMedian(results, workload, variant, structName, n, times, countersList);
    }

    private static void saveMedian(List<ResultEntry> results, String workload, String variant, String structName, int n,
                                   List<double[]> times, List<long[]> countersList) {
        int runs = times.size();
        double[] sortedTimes = new double[runs];
        for (int i = 0; i < runs; i++) sortedTimes[i] = times.get(i)[0];
        Arrays.sort(sortedTimes);
        double medianTime = sortedTimes[runs / 2];

        long[] medianCounters = new long[3];
        for (int c = 0; c < 3; c++) {
            long[] metricVals = new long[runs];
            for (int i = 0; i < runs; i++) metricVals[i] = countersList.get(i)[c];
            Arrays.sort(metricVals);
            medianCounters[c] = metricVals[runs / 2];
        }

        results.add(new ResultEntry(workload, variant, structName, n, medianTime, medianCounters[0], medianCounters[1], medianCounters[2]));
    }
}