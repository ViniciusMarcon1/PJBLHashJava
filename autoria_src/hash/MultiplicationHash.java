 package hash;

import structure.HashFunction;


public class MultiplicationHash implements HashFunction {
    private static final double A = 0.6180339887498949; 

    @Override
    public int hash(int key, int tableSize) {
        
        double prod = key * A;
        double frac = prod - Math.floor(prod); 
        int idx = (int) (tableSize * frac); 
        
        if (idx < 0) idx += tableSize;
        if (idx >= tableSize) idx = tableSize - 1;
        return idx;
    }
}
