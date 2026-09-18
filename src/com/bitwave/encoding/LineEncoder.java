package com.bitwave.encoding;

public class LineEncoder {

    public double[] encodeNRZ(int[] bits) {
        double[] signal = new double[bits.length];
        for (int i = 0; i < bits.length; i++) {
            signal[i] = (bits[i] == 1) ? 1.0 : -1.0;
        }
        return signal;
    }

    public double[] encodeManchester(int[] bits) {
        double[] signal = new double[bits.length * 2];
        for (int i = 0; i < bits.length; i++) {
            if (bits[i] == 1) {
                signal[i * 2] = 1.0;
                signal[i * 2 + 1] = -1.0;
            } else {
                signal[i * 2] = -1.0;
                signal[i * 2 + 1] = 1.0;
            }
        }
        return signal;
    }
}
