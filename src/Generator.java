import java.util.Random;
import model.Registro;

public class Generator {

    // Gera N registros com 9 dígitos usando seed fixa.
    public static Registro[] gerarRegistros(int n, long seed) {
        Random rng = new Random(seed);
        Registro[] arr = new Registro[n];
        for (int i = 0; i < n; i++) {
            int codigo = 100_000_000 + rng.nextInt(900_000_000);
            arr[i] = new Registro(codigo);
        }
        return arr;
    }

    /* TESTE DE GERAçÃO E REPLICABILIDADE DOS DADOS -> NÃO DESCOMENTAR
    public static void main(String[] args) {
        final long SEED = 10L;

        Registro[] data_100k  = gerarRegistros(100_000, SEED);
        // Registro[] data_1M   = gerarRegistros(1_000_000, SEED);
        // Registro[] data_10M   = gerarRegistros(10_000_000, SEED);

        // Exibir amostra de dados:
        for (int i = 0; i < Math.min(10, data_100k.length); i++) {
            System.out.println(data_100k[i]);
        }
    } */
}
