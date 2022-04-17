package edu.udb.parcial2.datos;

public class RegistroCompra {
    private int id;
    private double precio;
    String key;

    public RegistroCompra(){

    }

    public RegistroCompra(int id, double precio){
        this.id = id;
        this.precio = precio;
    }

    public int getId() { return id; }
    public void setId(int id) {
        this.id = id;
    }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getKey() { return key; }
    public void setKey(String key) {
        this.key = key;
    }
}
