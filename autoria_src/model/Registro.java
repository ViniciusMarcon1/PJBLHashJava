package model;

public class Registro {
    int codigo; 

    public Registro(int codigo) {
        this.codigo = codigo;
    }

    
    public int getCodigo() {
        return codigo;
    }

    
    public String getCodigoString() {
        return String.format("%09d", codigo);
    }

    @Override
    public String toString() {
        return getCodigoString();
    }
}