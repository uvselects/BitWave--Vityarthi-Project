package com.bitwave;

import com.bitwave.analysis.BERAnalyzer;
import com.bitwave.channel.NoisyChannel;
import com.bitwave.encoding.HammingCode;
import com.bitwave.encoding.LineEncoder;
import com.bitwave.exceptions.InvalidSignalException;
import com.bitwave.exceptions.TransmissionException;
import com.bitwave.receiver.Receiver;
import com.bitwave.signal.SignalGenerator;
import com.bitwave.signal.Waveform;
import com.bitwave.util.FileHandler;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SignalGenerator generator = new SignalGenerator();
        LineEncoder lineEncoder = new LineEncoder();
        HammingCode hamming = new HammingCode();
        NoisyChannel channel = new NoisyChannel();
        Receiver receiver = new Receiver();
        BERAnalyzer analyzer = new BERAnalyzer();
        FileHandler fileHandler = new FileHandler();

        System.out.println("==================================================");
        System.out.println("      BitWave - Digital Communication Simulator   ");
        System.out.println("==================================================");

        try {
            System.out.println("\n--- Step 1: Signal Configuration ---");
            System.out.println("1. Sine");
            System.out.println("2. Square");
            System.out.println("3. Sawtooth");
            System.out.print("Select waveform type (1-3): ");
            int waveChoice = scanner.nextInt();

            Waveform selectedWaveform;
            switch (waveChoice) {
                case 1:
                    selectedWaveform = Waveform.SINE;
                    break;
                case 2:
                    selectedWaveform = Waveform.SQUARE;
                    break;
                case 3:
                    selectedWaveform = Waveform.SAWTOOTH;
                    break;
                default:
                    throw new InvalidSignalException("Invalid waveform selection. Must be 1, 2, or 3.");
            }

            System.out.print("Enter signal frequency (Hz): ");
            double frequency = scanner.nextDouble();

            System.out.print("Enter sampling rate (Hz) [Must be >= " + (2 * frequency) + " Hz]: ");
            double samplingRate = scanner.nextDouble();

            System.out.print("Enter duration (seconds): ");
            int duration = scanner.nextInt();

            double[] analogSignal = generator.generateAnalogSignal(selectedWaveform, frequency, samplingRate, duration);
            int[] rawBits = generator.toBitstream(analogSignal);
            System.out.println("Sampled " + rawBits.length + " bits from the analog signal.");

            System.out.println("\n--- Step 2: Line & Channel Encoding ---");
            System.out.println("1. NRZ (Non-Return-to-Zero)");
            System.out.println("2. Manchester");
            System.out.print("Select line encoding scheme (1-2): ");
            int lineChoice = scanner.nextInt();

            double[] lineEncodedSignal;
            String lineEncodingName;
            if (lineChoice == 1) {
                lineEncodedSignal = lineEncoder.encodeNRZ(rawBits);
                lineEncodingName = "NRZ";
            } else if (lineChoice == 2) {
                lineEncodedSignal = lineEncoder.encodeManchester(rawBits);
                lineEncodingName = "Manchester";
            } else {
                throw new InvalidSignalException("Invalid line encoding selection. Must be 1 or 2.");
            }
            System.out.println("Signal encoded using " + lineEncodingName + " (" + lineEncodedSignal.length + " voltage levels generated).");

            int[] encodedBits = hamming.encodeStream(rawBits);
            System.out.println("Hamming (7,4) forward error correction applied: " + encodedBits.length + " encoded bits.");

            System.out.println("\n--- Step 3: Channel Simulation ---");
            System.out.print("Enter channel bit-flip probability (0.0 to 1.0): ");
            double errorProb = scanner.nextDouble();
            if (errorProb < 0.0 || errorProb > 1.0) {
                throw new InvalidSignalException("Probability must be between 0.0 and 1.0.");
            }

            int[] corruptedBits = channel.introduceNoise(encodedBits, errorProb);
            int channelBitFlips = analyzer.countErrors(encodedBits, corruptedBits);
            System.out.println("Bits transmitted through noisy channel. Noise introduced " + channelBitFlips + " bit flips.");

            System.out.println("\n--- Step 4: Receiver & Error Correction ---");
            int[] recoveredBits = receiver.processIncomingStream(corruptedBits);
            double finalBER = analyzer.calculateBER(rawBits, recoveredBits);
            int remainingErrors = analyzer.countErrors(rawBits, recoveredBits);

            StringBuilder report = new StringBuilder();
            report.append("==================================================\n");
            report.append("            BITWAVE SIMULATION REPORT             \n");
            report.append("==================================================\n");
            report.append("Waveform Type: ").append(selectedWaveform).append("\n");
            report.append("Frequency: ").append(frequency).append(" Hz\n");
            report.append("Sampling Rate: ").append(samplingRate).append(" Hz\n");
            report.append("Duration: ").append(duration).append(" s\n");
            report.append("Raw Bitstream Size: ").append(rawBits.length).append(" bits\n");
            report.append("Line Encoding: ").append(lineEncodingName).append("\n");
            report.append("Hamming Encoded Stream: ").append(encodedBits.length).append(" bits\n");
            report.append("Channel Error Rate: ").append(String.format("%.2f%%", errorProb * 100)).append("\n");
            report.append("Channel Bit Flips: ").append(channelBitFlips).append(" bits\n");
            report.append("Residual Errors After Decoding: ").append(remainingErrors).append(" bits\n");
            report.append("Final Bit Error Rate (BER): ").append(String.format("%.4f", finalBER)).append("\n");
            report.append("Correction Status: ");
            if (finalBER == 0.0) {
                report.append("SUCCESS - All channel errors corrected.\n");
            } else {
                report.append("PARTIAL - Multi-bit errors exceeded Hamming (7,4) single-bit correction capacity.\n");
            }
            report.append("==================================================\n");

            System.out.print("\n" + report.toString());
            fileHandler.saveSimulationReport(report.toString());

        } catch (InvalidSignalException | TransmissionException e) {
            System.err.println("\nSimulation Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("\nError: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
