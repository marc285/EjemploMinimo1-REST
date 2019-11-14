package edu.upc.dsa;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import edu.upc.dsa.models.Pedido;
import edu.upc.dsa.models.LP;
import edu.upc.dsa.models.Producto;
import edu.upc.dsa.models.Usuario;

public interface Gestor {
    public LinkedList<Producto> productosOrdPrecio() throws NoProductosException;
    public void anotarPedido(Pedido p);
    public void servirPedido() throws NoPedidosException;
    public LinkedList<Pedido> pedidosPorUsuario(String idUser) throws NoUsuarioException, NoPedidosException; //idUser es la key del HashMap
    public LinkedList<Producto> productosOrdVentas() throws NoProductosException;

    public void addUsuario(String k, String nom);
    public HashMap<String, Usuario> getUsuarios();
    public void addProducto(String nombre, double precio);
    public LinkedList<Producto> getProductos();
    public Producto getProducto(String idProducto);
    public LinkedList<Pedido> getPedidos();

    public void liberarRecursos();
}
