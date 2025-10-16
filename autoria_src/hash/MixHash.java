package hash;

import structure.HashFunction;


public class MixHash implements HashFunction {
    @Override
    public int hash(int key, int tableSize) {
        int x = key;
        x ^= (x >>> 16);
        x *= 0x7feb352d;
        x ^= (x >>> 15);
        x *= 0x846ca68b;
        x ^= (x >>> 16);
        
        int nonNeg = x & 0x7fffffff;
        return nonNeg % tableSize;
    }
}
