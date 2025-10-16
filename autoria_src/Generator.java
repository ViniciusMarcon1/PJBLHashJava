import java.util.Random;
import model.Registro;

public class Generator {

    
    public static Registro[] gerarRegistros(int n, long seed) {
        Random rng = new Random(seed);
        Registro[] arr = new Registro[n];
        for (int i = 0; i < n; i++) {
            int codigo = 100_000_000 + rng.nextInt(900_000_000);
            arr[i] = new Registro(codigo);
        }
        return arr;
    }

    












}
