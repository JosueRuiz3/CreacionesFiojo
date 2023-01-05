package com.example.creacionesfiojo.model;

public class pared {

    String nombre, pared, precio, dimensiones, material ;


    public pared() {
    }

    public pared(String nombre, String pared, String precio, String dimensiones, String material) {
        this.nombre = nombre;
        this.pared = pared;
        this.precio = precio;
        this.dimensiones = dimensiones;
        this.material = material;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPared() {
        return pared;
    }

    public void setPared(String pared) {
        this.pared = pared;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(String dimensiones) {
        this.dimensiones = dimensiones;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
