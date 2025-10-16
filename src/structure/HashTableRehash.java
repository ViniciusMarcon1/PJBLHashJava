package structure;

import model.Registro;

/**
 * Tabela hash com endereçamento aberto (double hashing) e resize automático.
 * - Armazena diretamente os Registros em um vetor de slots (sem listas).
 * - Cresce quando load factor ultrapassa o limiar (ex.: 0.75).
 * - Contabiliza "colisões" como slots ocupados sondados antes de inserir.
 */
public class HashTableRehash {

    private Registro[] table;
    private int size;
    private int count = 0;
    private long total_collisions = 0;
    private int resizeCount = 0;

    private final HashFunction h1;
    private final HashFunction h2;

    // Parâmetro: fator de carga alvo para crescer
    private final double LOAD_FACTOR_LIMIT;

    public HashTableRehash(int initialSize, HashFunction h1, HashFunction h2) {
        this(initialSize, h1, h2, 0.75);
    }

    public HashTableRehash(int initialSize, HashFunction h1, HashFunction h2, double loadFactorLimit) {
        this.size = initialSize;
        this.table = new Registro[this.size];
        this.h1 = h1;
        this.h2 = h2;
        this.LOAD_FACTOR_LIMIT = loadFactorLimit;
    }

    // Getters
    public int getSize(){
        return size;
    }
    public int getCount(){
        return count;
    }
    public double loadFactor(){
        return (double) count/size;
    }
    public long getCollisions(){
        return total_collisions;
    }
    public int getResizeCount(){
        return resizeCount;
    }

    // InserçÃo
    public void insert(Registro reg) {
        // 1) cresce se ultrapassar o limiar (antes de inserir)
        if ((count + 1) > (int)(LOAD_FACTOR_LIMIT * size)) {
            resize(nextPrime(size * 2)); // cresce (p/ primo próximo) mantendo dados
        }

        final int key = reg.getCodigo();
        int base = normalize(h1.hash(key, size), size);
        int step = normalize(h2.hash(key, size), size);
        if (step == 0) step = 1;

        int probesOccupied = 0;

        for (int i = 0; i < size; i++) {
            int idx = (int)((base + (long)i * step) % size);
            Registro slot = table[idx];

            if (slot == null) {
                table[idx] = reg;
                count++;
                total_collisions += probesOccupied;
                return;
            }
            if (slot.getCodigo() == key) {
                // já existe; não duplica
                total_collisions += probesOccupied;
                return;
            }
            // ocupada por outra chave -> colisão
            probesOccupied++;
        }
    }

    public boolean contains(int code) {
        int base = normalize(h1.hash(code, size), size);
        int step = normalize(h2.hash(code, size), size);
        if (step == 0) step = 1;

        for (int i = 0; i < size; i++) {
            int idx = (int)((base + (long)i * step) % size);
            Registro slot = table[idx];
            if (slot == null) {
                // sem remoção: null encerra a busca
                return false;
            }
            if (slot.getCodigo() == code) return true;
        }
        return false;
    }

    // Helpers internos
    private void resize(int newSize) {
        Registro[] old = this.table;
        int oldSize = this.size;

        Registro[] novo = new Registro[newSize];

        // re-insere todos os elementos (sem contar colisões aqui)
        for (int i = 0; i < oldSize; i++) {
            Registro r = old[i];
            if (r != null) {
                reinsertInto(novo, newSize, r);
            }
        }

        this.table = novo;
        this.size = newSize;
        this.resizeCount++;
        // count permanece o mesmo
    }

    private void reinsertInto(Registro[] arr, int arrSize, Registro reg) {
        final int key = reg.getCodigo();
        int base = normalize(h1.hash(key, arrSize), arrSize);
        int step = normalize(h2.hash(key, arrSize), arrSize);
        if (step == 0) step = 1;

        for (int i = 0; i < arrSize; i++) {
            int idx = (int)((base + (long)i * step) % arrSize);
            if (arr[idx] == null) {
                arr[idx] = reg;
                return;
            }
            // não contamos colisão no resize
        }
        // improvável se o newSize foi escolhido adequadamente
        throw new IllegalStateException("Falha ao reinserir durante resize (arrSize=" + arrSize + ")");
    }

    private int normalize(int idx, int m) {
        idx %= m;
        return (idx < 0) ? idx + m : idx;
    }

    // Primos simples:
    private int nextPrime(int n) {
        if (n <= 2) return 2;
        if ((n & 1) == 0) n++;
        while (!isPrime(n)) n += 2;
        return n;
    }
    private boolean isPrime(int x) {
        if (x < 2) return false;
        if ((x & 1) == 0) return x == 2;
        for (int d = 3; (long)d * d <= x; d += 2) {
            if (x % d == 0) return false;
        }
        return true;
    }
}