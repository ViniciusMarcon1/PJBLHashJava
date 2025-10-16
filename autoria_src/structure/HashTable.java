package structure;

import model.Registro;

public class HashTable {
    LinkedList[] table; 
    int size;
    long total_collisions = 0;
    HashFunction hashFunction;

    public HashTable(int size, HashFunction hashFunction1) {
        this.size = size;
        this.table = new LinkedList[size];
        this.hashFunction = hashFunction1;
    }

    
    public long getCollisions() { return total_collisions; }
    public int getSize() { return size; }

    
    public void insert(Registro reg) {
        int key = reg.getCodigo();
        int index = hashFunction.hash(key, size);
        if (index < 0) index += size;

        if (table[index] == null) {   
            
            table[index] = new LinkedList();
        }
        total_collisions += table[index].insertAndCount(reg); 
    }

    
    public boolean contains(int code) {
        int index = hashFunction.hash(code, size);
        if (index < 0) index += size;

        if (table[index] == null) return false;
        return table[index].contains(code);
    }

    
    public int[] bucketLengths() {
        int[] lengths = new int[size];
        for (int i = 0; i < size; i++) {
            if (table[i] == null) {
                lengths[i] = 0; 
            } else {
                lengths[i] = table[i].getSize(); 
            }
        }
        return lengths;
    }
}
