# BitWave: Digital Communication Simulator

BitWave is a console-based Java application designed to simulate and analyze the transmission of digital data over a noisy communication channel, featuring line encoding and forward error correction via (7,4) Hamming code.

The project models the core stages of a physical layer communication system: continuous signal generation, Nyquist-compliant sampling, analog-to-digital bit conversion, line encoding (NRZ and Manchester), forward error correction (FEC) encoding, stochastic bit-flip channel noise, syndrome-based error correction at the receiver, and Bit Error Rate (BER) analysis.

---

## Features

- **Waveform Synthesis**: Generates Sine, Square, and Sawtooth waveforms across configurable frequencies and durations.
- **Nyquist Rate Validation**: Enforces the Nyquist-Shannon sampling theorem ($F_s \ge 2 \cdot F_{signal}$) with custom exception handling for under-sampled signals.
- **Signal Digitization**: Converts continuous analog samples into a discrete binary bitstream.
- **Line Encoding**: Supports Non-Return-to-Zero (NRZ) and Manchester bipolar line encoding schemes.
- **Forward Error Correction (FEC)**: Implements systematic (7,4) Hamming code to encode 4-bit nibbles into 7-bit codewords with parity bits.
- **Stochastic Channel Noise**: Simulates a Binary Symmetric Channel (BSC) with user-defined bit-flip error probabilities.
- **Syndrome Decoding & Correction**: Computes error syndromes to locate and correct single-bit transmission errors per 7-bit block.
- **Performance Analytics**: Calculates raw channel bit flips, residual uncorrected bit errors, and the final Bit Error Rate (BER).
- **Automated Logging**: Saves timestamped execution reports containing all simulation parameters and recovery metrics in the `data/` directory.

---

## Tech Stack

| Component | Specification |
|---|---|
| **Language** | Java (JDK 8 or higher; tested on JDK 21 and JDK 26) |
| **Libraries** | Java Standard Library (`java.util`, `java.io`, `java.time`) |
| **Build Tool** | `javac` |
| **Execution** | Java Runtime (`java`) |
| **External Dependencies** | None (Zero third-party dependencies) |

---

## Project Structure

```text
BitWave/
├── bin/
├── data/
│   └── Simulation_Report_*.txt
├── src/
│   └── com/
│       └── bitwave/
│           ├── Main.java
│           ├── analysis/
│           │   └── BERAnalyzer.java
│           ├── channel/
│           │   └── NoisyChannel.java
│           ├── encoding/
│           │   ├── HammingCode.java
│           │   └── LineEncoder.java
│           ├── exceptions/
│           │   ├── InvalidSignalException.java
│           │   └── TransmissionException.java
│           ├── receiver/
│           │   └── Receiver.java
│           ├── signal/
│           │   ├── SignalGenerator.java
│           │   └── Waveform.java
│           └── util/
│               └── FileHandler.java
├── .gitignore
├── project_report.md
├── README.md
└── statement.md
```

---

## How to Run

### 1. Clone the repository
Clone the project to your local machine:
```bash
git clone https://github.com/uvselects/BitWave--Vityarthi-Project.git
```

### 2. Open the project root folder
Open your terminal (PowerShell, Command Prompt, or bash) and navigate to the project directory:
```bash
cd BitWave--Vityarthi-Project
```

### 3. Compile the project
Compile all source files into the `bin/` directory:
```bash
javac -sourcepath src -d bin src/com/bitwave/Main.java
```

### 4. Run the application
Run the compiled `Main` class:
```bash
java -cp bin com.bitwave.Main
```

---

## How to Use

When prompted in the console, provide the following parameters:

1. **Waveform Type**:
   - `1` → Sine
   - `2` → Square
   - `3` → Sawtooth
2. **Signal Frequency (Hz)**: Base frequency of the analog waveform (e.g., `5.0`).
3. **Sampling Rate (Hz)**: Sampling frequency. Must satisfy the Nyquist condition ($F_s \ge 2 \cdot F_{signal}$).
4. **Signal Duration (seconds)**: Integer duration of the transmission (e.g., `2`).
5. **Line Encoding Scheme**:
   - `1` → NRZ (Non-Return-to-Zero)
   - `2` → Manchester
6. **Channel Error Probability**: Value between `0.0` and `1.0` representing the bit-flip probability (e.g., `0.02` for 2% noise).

---

## Testing & Validation

### Test Case 1: Standard Run with Error Correction
- **Waveform**: `1` (Sine)
- **Frequency**: `5.0 Hz`
- **Sampling Rate**: `20.0 Hz` (Satisfies $20 \ge 2 \times 5$)
- **Duration**: `2 s`
- **Line Encoding**: `1` (NRZ)
- **Channel Error Probability**: `0.02` (2%)
- **Expected Outcome**: Bitstream sampled (40 bits). Hamming encoding expands to 70 bits. Single-bit errors introduced by noise are corrected by the receiver. Final BER = `0.0000`. Report generated in `data/`.

### Test Case 2: Nyquist-Shannon Violation
- **Waveform**: `1` (Sine)
- **Frequency**: `10.0 Hz`
- **Sampling Rate**: `15.0 Hz`
- **Duration**: `1 s`
- **Expected Outcome**: Program intercepts invalid parameters and throws `InvalidSignalException` with the message:
  ```text
  Simulation Error: Nyquist Shannon violation: Sampling rate (15.0 Hz) must be at least twice the signal frequency (10.0 Hz).
  ```
  The program terminates safely without crashing.

### Test Case 3: Noise-Free Ideal Channel
- **Waveform**: `2` (Square)
- **Frequency**: `4.0 Hz`
- **Sampling Rate**: `16.0 Hz`
- **Duration**: `1 s`
- **Line Encoding**: `2` (Manchester)
- **Channel Error Probability**: `0.00`
- **Expected Outcome**: Zero bits flipped in the channel. Reconstructed bitstream matches the original with BER = `0.0000`.

---

## Sample Console Output

```text
==================================================
      BitWave - Digital Communication Simulator   
==================================================

--- Step 1: Signal Configuration ---
1. Sine
2. Square
3. Sawtooth
Select waveform type (1-3): 1
Enter signal frequency (Hz): 5.0
Enter sampling rate (Hz) [Must be >= 10.0 Hz]: 20.0
Enter duration (seconds): 2
Sampled 40 bits from the analog signal.

--- Step 2: Line & Channel Encoding ---
1. NRZ (Non-Return-to-Zero)
2. Manchester
Select line encoding scheme (1-2): 1
Signal encoded using NRZ (40 voltage levels generated).
Hamming (7,4) forward error correction applied: 70 encoded bits.

--- Step 3: Channel Simulation ---
Enter channel bit-flip probability (0.0 to 1.0): 0.02
Bits transmitted through noisy channel. Noise introduced 2 bit flips.

--- Step 4: Receiver & Error Correction ---

==================================================
            BITWAVE SIMULATION REPORT             
==================================================
Waveform Type: SINE
Frequency: 5.0 Hz
Sampling Rate: 20.0 Hz
Duration: 2 s
Raw Bitstream Size: 40 bits
Line Encoding: NRZ
Hamming Encoded Stream: 70 bits
Channel Error Rate: 2.00%
Channel Bit Flips: 2 bits
Residual Errors After Decoding: 0 bits
Final Bit Error Rate (BER): 0.0000
Correction Status: SUCCESS - All channel errors corrected.
==================================================
Simulation report saved to: data\Simulation_Report_20260918_120402.txt
```

---

## Key Learnings

- Bridged theoretical communications engineering principles (Nyquist criterion, signal sampling, Hamming distance, syndrome decoding) with core software engineering practices.
- Implemented modular object-oriented design in Java with custom checked exceptions, package encapsulation, and clean file I/O.
- Validated error detection and correction trade-offs in discrete noisy communication channels.
