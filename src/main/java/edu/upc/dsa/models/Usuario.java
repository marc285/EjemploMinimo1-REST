package edu.upc.dsa.models;

import java.util.LinkedList;
import java.util.List;

public class Usuario {
    private String idUsuario;
    private String nombre;
    private List<Pedido> registro = null; //Historico de pedidos del usuario

    public Usuario(){

    }

    public Usuario (String id, String nom) {
        this();
        this.setID(id);
        this.setNombre(nom);
        registro = new LinkedList<Pedido>();
    }

    public String getID(){
        return this.idUsuario;
    }
    public void setID(String id){ this.idUsuario = id; };

    public String getNombre(){
        return this.nombre;
    }
    public void setNombre(String nom){ this.nombre = nom; };

    public void registrarPedido(Pedido p){ registro.add(p); }
    public LinkedList<Pedido> getRegistroPedidos(){ return (LinkedList<Pedido>) this.registro; }

}
