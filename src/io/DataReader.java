package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import model.Registro;
import structure.HashTable;
import structure.HashTableRehash;

public class DataReader {

    // Lê cada linha do arquivo e insere o registro em até 3 tabelas hash (Use null nas tabelas que não quiser preencher)
    public static void readAndInsert(String filepath, HashTable tabela1, HashTable tabela2, HashTable tabela3)
            throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    int codigo = Integer.parseInt(line);
                    Registro reg = new Registro(codigo);

                    if (tabela1 != null) tabela1.insert(reg);
                    if (tabela2 != null) tabela2.insert(new Registro(codigo));
                    if (tabela3 != null) tabela3.insert(new Registro(codigo));
                }
            }
        }
    }

    // Segundo metodo para o REHASHING
    public static void readAndInsert(String filepath, structure.HashTableRehash t1, structure.HashTableRehash t2, structure.HashTableRehash t3)
              throws java.io.IOException {
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                int code = Integer.parseInt(line.trim());
                model.Registro r = new model.Registro(code);
                if (t1 != null) t1.insert(r);
                if (t2 != null) t2.insert(new model.Registro(code));
                if (t3 != null) t3.insert(new model.Registro(code));
            }
        }
    }

}