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
}
