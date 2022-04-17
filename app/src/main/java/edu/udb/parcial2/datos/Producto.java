package edu.udb.parcial2.datos;

public class Producto {
    private String nombre;
    private String detalle;
    private String marca;
    private Double precio;
    String key;

    public Producto(){

    }

    public Producto(String nombre, String detalle, String marca, Double precio){
        this.nombre = nombre;
        this.detalle = detalle;
        this.marca = marca;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
