package io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DataStreamer {

    public static void generateDirectToTxt(int n, long seed, String filepath) throws IOException {
        Random rng = new Random(seed);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            for (int i = 0; i < n; i++) {
                int codigo = 100_000_000 + rng.nextInt(900_000_000);
                bw.write(String.format("%09d", codigo)); 
                bw.newLine();
            }
        }

        System.out.println("Arquivo salvo com " + n + " registros em: " + filepath);
    }
}