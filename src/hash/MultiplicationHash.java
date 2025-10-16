 package hash;

import structure.HashFunction;

// Metodo da multiplicação (Knuth). Índice = floor(m * frac( key * A )), onde A ~ (sqrt(5)-1)/2.
public class MultiplicationHash implements HashFunction {
    private static final double A = 0.6180339887498949; // (√5 - 1)/2

    @Override
    public int hash(int key, int tableSize) {
        // usa double para capturar a fração; normaliza se negativo
        double prod = key * A;
        double frac = prod - Math.floor(prod); // parte fracionária (mais robusto que % 1)
        int idx = (int) (tableSize * frac); // 0..m-1
        // por segurança (edge cases numéricos)
        if (idx < 0) idx += tableSize;
        if (idx >= tableSize) idx = tableSize - 1;
        return idx;
    }
}
