package structure;

@FunctionalInterface
public interface HashFunction {
    int hash(int key, int tableSize);
}
