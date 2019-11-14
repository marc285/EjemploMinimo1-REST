package edu.upc.dsa.models;

public class Producto {
    private String idProducto;
    private int numventas;
    private double precio;

    public Producto(){

    }

    public Producto(String id, double prec){
        this();
        this.setID(id);
        this.setVentas(0);
        this.setPrecio(prec);
    }

    public String getID(){ return this.idProducto; }
    public void setID(String id){ this.idProducto = id; };

    public int getVentas(){ return this.numventas; }
    public void setVentas(int v){ this.numventas = v; };

    public double getPrecio(){ return this.precio; }
    public void setPrecio(double pr){ this.precio = pr; };

    public void incrementarVentas(int n){ this.numventas = this.numventas + n; }

}
