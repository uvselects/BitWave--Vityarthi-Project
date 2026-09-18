package com.bitwave.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileHandler {

    public void saveSimulationReport(String reportContent) {
        try {
            File directory = new File("data");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File file = new File(directory, "Simulation_Report_" + timestamp + ".txt");

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(reportContent);
                System.out.println("Simulation report saved to: " + file.getPath());
            }

        } catch (IOException e) {
            System.err.println("Failed to save simulation report: " + e.getMessage());
        }
    }
}
