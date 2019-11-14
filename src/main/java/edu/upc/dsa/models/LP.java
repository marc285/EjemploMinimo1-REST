package edu.upc.dsa.models;

public class LP{ //Dupla Pedido-Cantidad
    private int numPedidos;
    private String idProducto;

    public LP(){

    }

    public LP(int n, String id) {
        this();
        this.setNumPedidos(n);
        this.setIDProducto(id);
    }

    public int getNumPedidos(){ return this.numPedidos; }
    public void setNumPedidos(int n){ this.numPedidos = n; }

    public String getIDProducto(){ return this.idProducto; }
    public void setIDProducto(String id){ this.idProducto = id; };
}
