package com.bitwave.channel;

import java.util.Random;

public class NoisyChannel {
    private final Random random = new Random();

    public int[] introduceNoise(int[] encodedStream, double errorProbability) {
        int[] noisyStream = new int[encodedStream.length];
        System.arraycopy(encodedStream, 0, noisyStream, 0, encodedStream.length);

        for (int i = 0; i < noisyStream.length; i++) {
            if (random.nextDouble() < errorProbability) {
                noisyStream[i] = (noisyStream[i] == 1) ? 0 : 1;
            }
        }
        return noisyStream;
    }
}
