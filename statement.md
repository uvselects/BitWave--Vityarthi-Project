# Project Statement: BitWave

## Problem Statement

In physical and wireless digital communication systems, data transmitted across channels is subject to thermal noise, electromagnetic interference, and signal attenuation. These transmission impairments corrupt transmitted bits, altering ones to zeros and vice versa. Without robust detection and correction mechanisms, corrupted data leads to transmission retransmissions, degraded throughput, and system failure.

Evaluating channel behavior and error-correcting codes on physical hardware testbenches (such as spectrum analyzers, arbitrary waveform generators, and FPGA boards) is costly, hardware-constrained, and impractical for rapid exploratory analysis. 

BitWave solves this problem by providing a modular software simulation environment in Java. It enables students and researchers to generate analog signals, sample and digitize them, apply line encoding and forward error correction (FEC), transmit data through a noisy channel model, and observe the detection and correction of transmission errors in real time.

---

## Scope of the Project

BitWave implements a software model of the digital communications physical layer pipeline:

$$\text{Source Waveform} \rightarrow \text{Sampler} \rightarrow \text{Digitizer} \rightarrow \text{Line Encoder} \rightarrow \text{Hamming Encoder} \rightarrow \text{Noisy Channel} \rightarrow \text{Receiver / Decoder} \rightarrow \text{Analytics}$$

### In Scope
- Synthesis of Sine, Square, and Sawtooth periodic analog waveforms.
- Mathematical validation of the Nyquist-Shannon sampling theorem ($F_s \ge 2 \cdot F_{signal}$).
- Bipolar digitization of sampled waveforms into binary bitstreams.
- Line encoding using Non-Return-to-Zero (NRZ) and Manchester schemes.
- Channel coding using systematic (7,4) Hamming code for forward error correction.
- Binary Symmetric Channel (BSC) simulation with stochastic bit flipping based on user-configured error probability.
- Parity-check syndrome computation for single-bit error detection and bit-level correction.
- Bit Error Rate (BER) analysis comparing pre-channel and post-correction bitstreams.
- Persistent logging of run metrics and simulation results in timestamped text files.

### Out of Scope
- RF modulation and passband carrier transmission (QAM, PSK, FSK).
- Burst-error channels and multi-bit correction codes (e.g., Reed-Solomon, LDPC).
- Real-time hardware audio/network card interfacing.

---

## Target Users

1. **Undergraduate Engineering Students (ECE / CSE / IT)**:
   - Students studying Digital Communications, Signals and Systems, or Computer Networks who need an interactive simulation to bridge theory with code.
2. **Educators and Lab Instructors**:
   - Instructors demonstrating Nyquist sampling criteria, aliasing boundaries, and Hamming distance error correction.
3. **Academic Evaluators**:
   - Evaluators assessing object-oriented architecture, modular package design, custom exception handling, and clean software practices in Java.

---

## High-Level Features

- **Multi-Waveform Signal Synthesis**: Interactive selection of fundamental waveforms with custom frequency and duration parameters.
- **Strict Sampling Verification**: Prevents aliasing by enforcing $F_s \ge 2 \cdot F_{signal}$ via custom checked exceptions.
- **Bipolar Line Coding**: Provides both NRZ and Manchester level representations of digital data.
- **Hamming (7,4) Forward Error Correction**: Automatically pads bitstreams into 4-bit data nibbles, generates parity bits, and corrects all single-bit corruptions per 7-bit block.
- **Parametric Channel Noise**: Configurable bit-error probability allowing users to simulate clean to highly noisy environments.
- **Autonomous Recovery & Analytics**: Automatically computes syndromes, isolates corrupted bit positions, corrects bit flips, and calculates the final Bit Error Rate.
- **Automatic Report Generation**: Exports complete simulation telemetry into formatted text files in the `data/` directory.
