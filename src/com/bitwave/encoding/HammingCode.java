package com.bitwave.encoding;

public class HammingCode {

    public int[] encodeStream(int[] rawBits) {
        int paddedLength = (int) (Math.ceil(rawBits.length / 4.0) * 4);
        int[] data = new int[paddedLength];
        System.arraycopy(rawBits, 0, data, 0, rawBits.length);

        int totalBlocks = paddedLength / 4;
        int[] encodedStream = new int[totalBlocks * 7];

        for (int b = 0; b < totalBlocks; b++) {
            int[] d = new int[4];
            System.arraycopy(data, b * 4, d, 0, 4);
            int[] blockEncoded = encodeBlock(d);
            System.arraycopy(blockEncoded, 0, encodedStream, b * 7, 7);
        }
        return encodedStream;
    }

    private int[] encodeBlock(int[] d) {
        int[] c = new int[7];
        c[2] = d[0];
        c[4] = d[1];
        c[5] = d[2];
        c[6] = d[3];

        c[0] = c[2] ^ c[4] ^ c[6];
        c[1] = c[2] ^ c[5] ^ c[6];
        c[3] = c[4] ^ c[5] ^ c[6];
        return c;
    }

    public int[] decodeAndCorrectBlock(int[] block) {
        int p1 = block[0] ^ block[2] ^ block[4] ^ block[6];
        int p2 = block[1] ^ block[2] ^ block[5] ^ block[6];
        int p3 = block[3] ^ block[4] ^ block[5] ^ block[6];

        int errorPos = p1 + (p2 * 2) + (p3 * 4);

        if (errorPos != 0 && errorPos <= 7) {
            block[errorPos - 1] = (block[errorPos - 1] == 1) ? 0 : 1;
        }

        return new int[] { block[2], block[4], block[5], block[6] };
    }
}
