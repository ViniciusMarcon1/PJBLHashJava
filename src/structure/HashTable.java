package structure;

import model.Registro;

public class HashTable {
    LinkedList[] table;         // vetor de listas encadeadas (cada bucket guarda Registro)
    int size;
    long total_collisions = 0;
    HashFunction hashFunction;

    public HashTable(int size, HashFunction hashFunction1) {
        this.size = size;
        this.table = new LinkedList[size];
        this.hashFunction = hashFunction1;
    }

    // Getters
    public long getCollisions() { return total_collisions; }
    public int getSize() { return size; }

    // Insere um Registro na tabela e soma as colisões do bucket
    public void insert(Registro reg) {
        int key = reg.getCodigo();                    // hash é calculado a partir do código
        int index = hashFunction.hash(key, size);
        if (index < 0) index += size;

        if (table[index] == null) {   // cria a O bucket se tiver elementos nesse index
            table[index] = new LinkedList();
        }
        total_collisions += table[index].insertAndCount(reg); // conta nós visitados no bucket
    }

    // Verifica se existe um registro com este código (busca ordenada no bucket)
    public boolean contains(int code) {
        int index = hashFunction.hash(code, size);
        if (index < 0) index += size;

        if (table[index] == null) return false;
        return table[index].contains(code);
    }

    // Retorna o tamanho (número de registros) de cada bucket
    public int[] bucketLengths() {
        int[] lengths = new int[size];
        for (int i = 0; i < size; i++) {
            if (table[i] == null) {
                lengths[i] = 0; // bucket tá vazio
            } else {
                lengths[i] = table[i].getSize(); // quantidade de nós (registros) no bucket
            }
        }
        return lengths;
    }
}