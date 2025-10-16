import io.DataReader;
import structure.HashFunction;
import structure.HashTable;

import hash.MixHash;
import hash.MultiplicationHash;

import hash.DoubleHashStepMix;
import structure.HashTableRehash;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class MainBenchmark {

    // Mesmos tamanhos de tabela
    static final int[] TABLE_SIZES = { 10_000, 100_000, 10_000_000 };

    static final String[] DATA_FILES = {
            "data_100k_seed10.txt",
            "data_1M_seed10.txt",
            "data_10M_seed10.txt"
    };
    static final int[] DATA_COUNTS = { 100_000, 1_000_000, 10_000_000 };

    static final String[] FUNC_NAMES = { "MixHash", "MultiHash", "Rehash(Double)" };
    static final String[] FUNC_KIND  = { "CHAIN",   "CHAIN",     "REHASH"        };

    // Encadeamento: funções h(x)
    static final HashFunction H_CHAIN_0 = new MixHash();
    static final HashFunction H_CHAIN_1 = new MultiplicationHash();

    // Rehashing: h1 (base) e h2 (passo 1..m-1) + limite de carga p/ resize
    static final HashFunction H1_REHASH = new MixHash();
    static final DoubleHashStepMix H2_STEP = new DoubleHashStepMix();
    static final double LOAD_FACTOR_LIMIT = 0.75;

    // Arquivo de saída
    static final String REPORT_FILE = "benchmark_report_seed10.txt";

    public static void main(String[] args) throws Exception {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(REPORT_FILE, false)))) {

            // Cabeçalho
            out.println("Relatório de Benchmark --> Tabelas Hash (Encadeamento + Rehashing)");
            out.println("Data/Hora: " + LocalDateTime.now());
            out.println("Datasets: 100k, 1M, 10M (seed = 10)");
            out.println("Funções: " + FUNC_NAMES[0] + ", " + FUNC_NAMES[1] + ", " + FUNC_NAMES[2]);
            out.println();
            out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            out.println("Função            | Size      | Dados     | Insert(ms) | Search(ms) | Collisions     | top3(b#len)                         | gaps[min  max  avg]");
            out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            out.flush();

            // Loop: função (3) × tamanhos de tabela (3) × datasets (3) = 27 linhas
            for (int f = 0; f < FUNC_NAMES.length; f++) {
                final String fname = FUNC_NAMES[f];
                final String fkind = FUNC_KIND[f];

                System.out.println("\n==============================");
                System.out.println("FUNÇÃO: " + fname + "  [" + fkind + "]");
                System.out.println("==============================");

                for (int size : TABLE_SIZES) {
                    for (int di = 0; di < DATA_FILES.length; di++) {
                        final String file = DATA_FILES[di];
                        final int expectedCount = DATA_COUNTS[di];

                        double insertMs, searchMs;
                        long collisions;

                        String top3Str = "[-]";     
                        String gapsStr = "[-]";    

                        if ("CHAIN".equals(fkind)) {
                            // Seleciona a função hash do encadeamento
                            HashFunction hf = (f == 0) ? H_CHAIN_0 : H_CHAIN_1;

                            // Tabela com encadeamento
                            HashTable table = new HashTable(size, hf);

                            long t0 = System.nanoTime();
                            DataReader.readAndInsert(file, table, null, null);
                            long t1 = System.nanoTime();
                            insertMs = (t1 - t0) / 1_000_000.0;

                            collisions = table.getCollisions();

                            long s0 = System.nanoTime();
                            searchAllFromFile(file, table, expectedCount);
                            long s1 = System.nanoTime();
                            searchMs = (s1 - s0) / 1_000_000.0;

                            // Estatísticas de distribuição (só encadeamento)
                            int[] lengths = table.bucketLengths();
                            Top3 top = top3Buckets(lengths);
                            GapStats gs = gapStats(lengths);
                            top3Str = String.format("[%d#%d, %d#%d, %d#%d]",
                                    top.idx1, top.len1, top.idx2, top.len2, top.idx3, top.len3);
                            gapsStr = String.format("[%4d %4d %6.2f]", gs.min, gs.max, gs.avg);

                        } else { // REHASH = double hashing + resize
                            HashTableRehash dh = new HashTableRehash(size, H1_REHASH, H2_STEP, LOAD_FACTOR_LIMIT);

                            long t0 = System.nanoTime();
                            DataReader.readAndInsert(file, dh, null, null);
                            long t1 = System.nanoTime();
                            insertMs = (t1 - t0) / 1_000_000.0;

                            collisions = dh.getCollisions();

                            long s0 = System.nanoTime();
                            searchAllFromFile(file, dh, expectedCount);
                            long s1 = System.nanoTime();
                            searchMs = (s1 - s0) / 1_000_000.0;

                            // Mantemos formato da linha; top3/gaps ficam "[-]"
                        }

                        String line = String.format(
                                "%-16s | m=%-8d | N=%-9d | %10.3f | %10.3f | %-14d | %-35s | %-18s",
                                fname, size, expectedCount, insertMs, searchMs, collisions, top3Str, gapsStr
                        );
                        System.out.println(line);
                        out.println(line);
                        out.flush();
                    }
                }
            }

            out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            out.println("Fim do relatório.");
            out.flush();
        }
    }

    // Auxiliares de busca (p/ encadeamento)
    private static void searchAllFromFile(String filepath, HashTable table, int expectedCount) throws Exception {
        int seen = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                int code = Integer.parseInt(line.trim());
                boolean present = table.contains(code);
                seen++;
            }
        }
    }

    // Auxiliar de busca (rehashing)
    private static void searchAllFromFile(String filepath, HashTableRehash table, int expectedCount) throws Exception {
        int seen = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                int code = Integer.parseInt(line.trim());
                boolean present = table.contains(code);
                seen++;
            }
        }
    }

    //Top-3 maiores listas (só encadeamento)
    static class Top3 { int idx1,len1, idx2,len2, idx3,len3; }
    private static Top3 top3Buckets(int[] lengths) {
        Top3 r = new Top3();
        r.idx1 = r.idx2 = r.idx3 = -1;
        r.len1 = r.len2 = r.len3 = 0;
        for (int i = 0; i < lengths.length; i++) {
            int len = lengths[i];
            if (len > r.len1) { r.len3=r.len2; r.idx3=r.idx2; r.len2=r.len1; r.idx2=r.idx1; r.len1=len; r.idx1=i; }
            else if (len > r.len2) { r.len3=r.len2; r.idx3=r.idx2; r.len2=len; r.idx2=i; }
            else if (len > r.len3) { r.len3=len; r.idx3=i; }
        }
        return r;
    }

    //Gaps entre buckets ocupados (só encadeamento)
    static class GapStats { int min, max; double avg; }
    private static GapStats gapStats(int[] lengths) {
        GapStats g = new GapStats();
        int prev = -1;
        int sum = 0, count = 0;
        g.min = Integer.MAX_VALUE; g.max = 0;

        for (int i = 0; i < lengths.length; i++) {
            if (lengths[i] > 0) {
                if (prev != -1) {
                    int gap = i - prev - 1;
                    if (gap < g.min) g.min = gap;
                    if (gap > g.max) g.max = gap;
                    sum += gap; count++;
                }
                prev = i;
            }
        }
        if (count == 0) { g.min = 0; g.max = 0; g.avg = 0.0; }
        else { g.avg = (double) sum / count; }
        return g;
    }
}
