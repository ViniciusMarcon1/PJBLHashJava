import io.DataStreamer;

public class MainStreamAll {
    public static void main(String[] args) throws Exception {
        long SEED = 10L;
        DataStreamer.generateDirectToTxt(100_000, SEED, "data_100k_seed10.txt");
        DataStreamer.generateDirectToTxt(1_000_000, SEED, "data_1M_seed10.txt");
        DataStreamer.generateDirectToTxt(10_000_000, SEED, "data_10M_seed10.txt");
        System.out.println("Arquivos gerados com sucesso.");
}
}
