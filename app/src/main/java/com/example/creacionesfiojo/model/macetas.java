package com.example.creacionesfiojo.model;

public class macetas {

    String nombre, macetas, precio, dimensiones, material ;

    public macetas() {
    }

    public macetas(String nombre, String macetas, String precio, String dimensiones, String material) {
        this.nombre = nombre;
        this.macetas = macetas;
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

    public String getMacetas() {
        return macetas;
    }

    public void setMacetas(String macetas) {
        this.macetas = macetas;
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
