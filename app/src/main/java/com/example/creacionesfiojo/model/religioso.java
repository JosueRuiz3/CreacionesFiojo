package com.example.creacionesfiojo.model;

public class religioso {

    String nombre, religioso, precio, dimensiones, material ;

    public religioso() {
    }

    public religioso(String nombre, String religioso, String precio, String dimensiones, String material) {
        this.nombre = nombre;
        this.religioso = religioso;
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

    public String getReligioso() {
        return religioso;
    }

    public void setReligioso(String religioso) {
        this.religioso = religioso;
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
