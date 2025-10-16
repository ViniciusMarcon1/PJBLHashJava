package hash;

import structure.HashFunction;

//Passo do double hashing: retorna um valor em 1..m-1, usa uma pequena mistura de bits para derivar o passo.
public class DoubleHashStepMix implements HashFunction {
    @Override
    public int hash(int key, int tableSize) {
        int x = key;
        x ^= (x >>> 16);
        x *= 0x7feb352d;
        x ^= (x >>> 15);
        x *= 0x846ca68b;
        x ^= (x >>> 16);

        int nonNeg = x & 0x7fffffff;
        // garante passo ∈ [1, m-1] (se m==1, devolve 1 por segurança)
        return (tableSize > 1) ? (nonNeg % (tableSize - 1)) + 1 : 1;
    }
}
