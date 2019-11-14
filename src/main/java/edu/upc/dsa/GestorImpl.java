package edu.upc.dsa;

import java.util.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import edu.upc.dsa.models.Pedido;
import edu.upc.dsa.models.LP;
import edu.upc.dsa.models.Producto;
import edu.upc.dsa.models.Usuario;

public class GestorImpl implements Gestor {

    //--Singleton: Referencia privada estatica a la unica instancia de la clase--//
    private static GestorImpl instancia;

    private static Logger log = LogManager.getLogger(GestorImpl.class);

    //---Estructuras de datos (atributos) del gestor---//
    private HashMap<String, Usuario> tablaUsuarios;
    private Queue<Pedido> colaPedidos;
    private List<Producto> listaProductos;

    //----------Singleton: constructor privado----------//
    private GestorImpl(){
        this.tablaUsuarios = new HashMap<String,Usuario>();
        this.colaPedidos = new LinkedList<Pedido>();
        this.listaProductos = new LinkedList<Producto>();
    }

    //---Singleton: metodo de acceso estqtico que devuelve una referencia a la (unica) instancia---//
    public static GestorImpl getInstance(){
        if (instancia == null) instancia = new GestorImpl();
        return instancia;
    }

    //------------------Metodos (no estaticos) del gestor------------------------//
    public LinkedList<Producto> productosOrdPrecio() throws NoProductosException{
        //ORDEN ASCENDENTE
        if(checkProductos()){
            log.info("Lista de productos normal: ");
            log.info(logListaProductos((LinkedList<Producto>) this.listaProductos));

            List<Producto> res = this.listaProductos;
            res.sort(new Comparator<Producto>() {
                @Override
                public int compare(Producto p1, Producto p2) {
                    return Double.compare(p1.getPrecio(), p2.getPrecio());
                }
            });

            log.info("Lista de productos ordenada por precio, orden ascendente: ");
            log.info(logListaProductos((LinkedList<Producto>)res));

            return (LinkedList<Producto>) res;
        }
        else{
            log.error("Error. Lista de productos vacía.");
            return null;
            /*throw new NoProductosException();*/
        }
    }

    public void anotarPedido(Pedido p) {
        colaPedidos.add(p);

        log.info("Nuevo pedido añadido a la cola: ");
        log.info(logPedido(p));

        log.info("Cola actual: ");
        log.info(logColaPedidos((LinkedList<Pedido>) this.colaPedidos));
    }

    public void servirPedido() throws NoPedidosException{
        if(chechPedidos()){
            Pedido p = this.colaPedidos.poll();
            log.info("Pedido servido: ");
            log.info(logPedido(p));
            procesarPedido(p);

            log.info("Cola actual: ");
            log.info(logColaPedidos((LinkedList<Pedido>) this.colaPedidos));
        }
        else {
            log.error("Error. Cola de pedidos vacía.");
            /*throw new NoPedidosException();*/
        }
    }

    public LinkedList<Pedido> pedidosPorUsuario(String idUser) throws NoUsuarioException, NoPedidosException{
        if(tablaUsuarios.get(idUser) == null){
            log.error("Error. No se ha encontrado al usuario.");
            /*throw new NoUsuarioException();*/
            return null;
        }
        else
        if(tablaUsuarios.get(idUser).getRegistroPedidos() == null) {
            log.error("Error. Cola de pedidos del usuario vacía.");
            /*throw new NoPedidosException();*/
            return null;
        }
        else {
            log.info("Historial de pedidos del usuario " + tablaUsuarios.get(idUser).getNombre() + " :");
            log.info(logHistorialPedidos(tablaUsuarios.get(idUser)));
            return this.tablaUsuarios.get(idUser).getRegistroPedidos();
        }
    }

    public LinkedList<Producto> productosOrdVentas() throws NoProductosException{
        //ORDEN DESCENDENTE
        if(checkProductos()){
            log.info("Lista de productos normal: ");
            log.info(logListaProductos((LinkedList<Producto>) this.listaProductos));

            List<Producto> res = this.listaProductos;
            res.sort(new Comparator<Producto>() {
                @Override
                public int compare(Producto p1, Producto p2) {
                    return Integer.compare(p2.getVentas(), p1.getVentas());
                }
            });

            log.info("Lista de productos ordenada por ventas, orden descendente: ");
            log.info(logListaProductos((LinkedList<Producto>)res));

            return (LinkedList<Producto>) res;
        }
        else{
            log.error("Error. Lista de productos vacía.");
            /*throw new NoProductosException();*/
            return null;
        }
    }

    //-----------------------------Funciones para modificar los atributos----------------------------//
    public void addUsuario(String k, String nom){
        Usuario u = new Usuario(k,nom);
        this.tablaUsuarios.put(k,u);
    }
    public HashMap<String, Usuario> getUsuarios(){
        return this.tablaUsuarios;
    }

    public void addProducto(String nombre, double precio) {
        Producto p = new Producto(nombre, precio);
        this.listaProductos.add(p);
    }
    public LinkedList<Producto> getProductos(){
        return (LinkedList<Producto>) this.listaProductos;
    }

    public Producto getProducto(String idProducto) {
        for (Producto p: this.listaProductos) {
            if (p.getID().equals(idProducto)) return p;
        }
        return null;
    }
    public LinkedList<Pedido> getPedidos(){
        return (LinkedList<Pedido>) this.colaPedidos;
    }

    public void liberarRecursos() {
        this.tablaUsuarios.clear();
        this.colaPedidos.clear();
        this.listaProductos.clear();
    }


    //----------------------------Funciones auxiliares---------------------------------//
    private boolean chechPedidos(){ //TRUE SI HAY PEDIDOS (NO ESTÁ VACÍA)
        return this.colaPedidos.size() != 0;
    }

    private boolean checkProductos(){ //TRUE SI HAY PRODUCTOS (NO ESTÁ VACÍA)
        return this.listaProductos.size() != 0;
    }

    private void procesarPedido(Pedido p){
        String idusr = p.getIDUsuario();
        Usuario usr = this.tablaUsuarios.get(idusr);
        usr.registrarPedido(p);

        for (int i = 0; i < p.getLPsize(); i++){
            LP lp = p.getLP(i);
            String idprod = lp.getIDProducto();
            int num = lp.getNumPedidos();

            //Buscar producto
            for (Producto pr : this.listaProductos) {
                if (pr.getID().equals(idprod))
                    pr.incrementarVentas(num);
            }
        }
    }

    //---------------------Funciones auxiliares para Log info-------------------------//
    private String logListaProductos (LinkedList<Producto> lista){
        String res = "";
        for (Producto pr : lista){
            res = res + "Producto: " + pr.getID() + " | Numero de ventas: " + pr.getVentas() + " | Precio: " + pr.getPrecio() + " // ";
        }
        return res;
    }

    private String logPedido(Pedido p){
        String res = "Usuario: " + this.tablaUsuarios.get(p.getIDUsuario()).getNombre() + " // ";
        for(int i = 0; i < p.getLPsize(); i++){
            res = res + " Producto: " + p.getLP(i).getIDProducto() + " , Cantidad: " + p.getLP(i).getNumPedidos() + " ||";
        }
        return res;
    }

    private String logHistorialPedidos(Usuario u){
        String res = "";
        LinkedList<Pedido> lista = u.getRegistroPedidos();
        for(int i = 0; i < lista.size(); i++){
            res = res + "Pedido " + i + " :";
            for (int j = 0; j < lista.get(i).getLPsize() ; j++){
                res = res + " Producto: " + lista.get(i).getLP(j).getIDProducto() + " , Cantidad: " + lista.get(i).getLP(j).getNumPedidos() + " ||";
            }
            res = res + "---|| ";
        }
        return res;
    }

    private String logColaPedidos(LinkedList<Pedido> cola){
        String res = "";
        for (Pedido pe : cola){
            res = res + logPedido(pe) + "---|| ";
        }
        return res;
    }
}
