package com.example.creacionesfiojo.model;

public class jardin {
    String nombre, jardin, precio, dimensiones, material ;


    public jardin() {
    }

    public jardin(String nombre, String jardin, String precio, String dimensiones, String material) {
        this.nombre = nombre;
        this.jardin = jardin;
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

    public String getJardin() {
        return jardin;
    }

    public void setJardin(String jardin) {
        this.jardin = jardin;
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
