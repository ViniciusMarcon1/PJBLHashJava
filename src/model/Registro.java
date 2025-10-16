package model;

public class Registro {
    int codigo; // 9 dígitos né Andrey (vai detonar minha RAM, mds...)

    public Registro(int codigo) {
        this.codigo = codigo;
    }

    //Getter
    public int getCodigo() {
        return codigo;
    }

    //Para exibir com zeros à esquerda (sempre 9 dígitos)
    public String getCodigoString() {
        return String.format("%09d", codigo);
    }

    @Override
    public String toString() {
        return getCodigoString();
    }
}