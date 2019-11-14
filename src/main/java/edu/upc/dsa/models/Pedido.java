package edu.upc.dsa.models;

import java.util.LinkedList;
import java.util.List;

public class Pedido {
    private String iduser;
    private List<LP> lps = null;

    public Pedido(){

    }

    public Pedido(String idUser, List<LP> lps){
        this();
        this.setIDUsuario(iduser);
        this.addLPS(lps);
    }

    public String getIDUsuario(){ return this.iduser; }
    public void setIDUsuario(String id){ this.iduser = id; };

    public void addLP(int n, String idprod){
        LP lp = new LP (n, idprod);
        this.lps.add(lp);
    }

    public LP getLP(int i){ return this.lps.get(i); }

    public void addLPS(List<LP> lps){
        this.lps = lps;
    }

    public List<LP> getLPS() {return this.lps; };

    public int getLPsize(){ return this.lps.size(); }
}
