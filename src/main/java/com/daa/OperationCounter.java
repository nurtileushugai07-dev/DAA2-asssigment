package com.daa;

public class OperationCounter {
    private long steps = 0;
    private long moves = 0;
    private long comparisons = 0;

    public void addStep() { steps++; }
    public void addSteps(long count) { steps += count; }

    public void addMove() { moves++; }
    public void addMoves(long count) { moves += count; }

    public void addComparison() { comparisons++; }
    public void addComparisons(long count) { comparisons += count; }

    public void reset() {
        steps = 0;
        moves = 0;
        comparisons = 0;
    }

    public long getSteps() { return steps; }
    public long getMoves() { return moves; }
    public long getComparisons() { return comparisons; }
}