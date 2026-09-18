package com.bitwave.analysis;

public class BERAnalyzer {

    public double calculateBER(int[] originalBits, int[] processedBits) {
        int minLength = Math.min(originalBits.length, processedBits.length);
        if (minLength == 0) {
            return 0.0;
        }

        int errorCount = 0;
        for (int i = 0; i < minLength; i++) {
            if (originalBits[i] != processedBits[i]) {
                errorCount++;
            }
        }

        return (double) errorCount / minLength;
    }

    public int countErrors(int[] originalBits, int[] processedBits) {
        int minLength = Math.min(originalBits.length, processedBits.length);
        int errorCount = 0;
        for (int i = 0; i < minLength; i++) {
            if (originalBits[i] != processedBits[i]) {
                errorCount++;
            }
        }
        return errorCount;
    }
}
