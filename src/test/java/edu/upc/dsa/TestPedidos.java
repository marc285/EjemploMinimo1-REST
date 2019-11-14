package edu.upc.dsa;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.upc.dsa.models.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TestPedidos {

    public GestorImpl gestor = null;
    public Pedido p = null;

    @Before
    public void setUp (){
        gestor = GestorImpl.getInstance();

        gestor.addUsuario("12345","Toni");

        gestor.addProducto("chocolatina",1);
        gestor.addProducto("cocacola",1.5);
        gestor.addProducto("bocadillo_jamon", 3.5);
        gestor.addProducto("zumo_naranja", 1.2);

        List<LP> lps = new LinkedList<LP>();
        lps.add(new LP(3,"chocolatina"));
        lps.add(new LP(2,"zumo_naranja"));
        lps.add(new LP(1, "bocadillo_jamon"));

        Pedido p = new Pedido("12345",lps);

        /*p = new Pedido("12345");*/

        /*p.addLP(3, "chocolatina");
        p.addLP(2, "zumo_naranja");
        p.addLP(1, "bocadillo_jamon");
        */

        gestor.anotarPedido(p);
    }

    @After
    public void tearDown (){
        gestor.liberarRecursos();
    }

    @Test
    public void anotarPedidoTest(){
        //Comprobar que se ha hecho bien el pedido
        Assert.assertEquals("12345", p.getIDUsuario());
        Assert.assertEquals("chocolatina", p.getLP(0).getIDProducto());
        Assert.assertEquals("bocadillo_jamon", p.getLP(p.getLPsize() - 1).getIDProducto());

        //Comprobamos que se han introducido bien los datos en las estructuras del gestor
        Assert.assertEquals("Toni", gestor.getUsuarios().get("12345").getNombre());
        Assert.assertEquals("zumo_naranja", gestor.getProductos().get(gestor.getProductos().size() - 1).getID());
        Assert.assertEquals(1.2, 1.2, gestor.getProductos().get(gestor.getProductos().size() - 1).getPrecio());
    }

    @Test
    public void servirPedidoTest() throws NoPedidosException {
        gestor.servirPedido();
        Assert.assertEquals(3, gestor.getProducto("chocolatina").getVentas());
        Assert.assertEquals(1, gestor.getProducto("bocadillo_jamon").getVentas());

        //COMPROBAR HISTORICO DE PEDIDOS DE USUARIO
        //Ya se hace en la funcion comprobar pedidos por usuario
    }

    @Test (expected = NoPedidosException.class)
    public void noPedidosExceptionTest() throws NoPedidosException{
        gestor.servirPedido();
        gestor.servirPedido();
    }

    @Test
    public void ordenarProdPrecioTest() throws NoProductosException { //ORDENAR ASC
        LinkedList<Producto> prueba = gestor.productosOrdPrecio();
        Assert.assertEquals("chocolatina", prueba.getFirst().getID());
        Assert.assertEquals("bocadillo_jamon", prueba.getLast().getID());
    }

    @Test
    public void ordenarProdVentasTest() throws NoProductosException, NoPedidosException { //ORDENAR DESC
        gestor.servirPedido(); //Puede lanzar un NoPedidosException porque se prueba el servirPedido para incrementar num de ventas en los productos
        LinkedList<Producto> prueba = gestor.productosOrdVentas();
        Assert.assertEquals("chocolatina", prueba.getFirst().getID());
        Assert.assertEquals("cocacola", prueba.getLast().getID());
    }

    @Test (expected = NoProductosException.class)
    public void noProductosExceptionTest() throws NoProductosException{
        gestor.getProductos().clear();
        gestor.productosOrdPrecio();
        gestor.productosOrdVentas();
    }

    @Test
    public void pedidosPorUsuarioTest() throws NoPedidosException, NoUsuarioException{
        //Se comprueba tanto el registro de pedidos en el historico del usuario como la búsqueda del registro de un usuario especifcado
        gestor.servirPedido();

        LinkedList<Pedido> prueba = gestor.pedidosPorUsuario("12345");
        Assert.assertEquals(1, prueba.size());
        Assert.assertEquals(3, prueba.get(0).getLPsize());

        Assert.assertEquals("chocolatina", prueba.get(0).getLP(0).getIDProducto());
        Assert.assertEquals(3,prueba.get(0).getLP(0).getNumPedidos());

        Assert.assertEquals("bocadillo_jamon", prueba.get(0).getLP(2).getIDProducto());

        //-------Probamos a meter un nuevo pedido--------//
        List<LP> lps = new LinkedList<LP>();
        lps.add(new LP(1, "bocata_jamon"));
        lps.add(new LP(1, "cocacola"));

        Pedido pruebapedido = new Pedido("12345",lps);

        /*Pedido pruebapedido = new Pedido("12345");
        pruebapedido.addLP();
        pruebapedido.addLP();
         */

        gestor.anotarPedido(pruebapedido);
        gestor.servirPedido(); //El pedido anterior ya ha sido servido (quitado de la cola) asi que ahora debería ser servido el nuevo pedido

        LinkedList<Pedido> prueba2 = gestor.pedidosPorUsuario("12345");
        Assert.assertEquals(2, prueba2.size());
        Assert.assertEquals(2, prueba2.get(1).getLPsize());

        Assert.assertEquals("cocacola", prueba2.get(1).getLP(1).getIDProducto());
    }

    @Test (expected = NoUsuarioException.class)
    public void noUsuarioExceptionIDERRORTest() throws NoUsuarioException, NoPedidosException {
        gestor.pedidosPorUsuario("1234");
    }

    @Test (expected = NoUsuarioException.class)
    public void noUsuarioExceptionEMPTYTABLETest() throws NoUsuarioException, NoPedidosException{
        gestor.getUsuarios().clear();
        gestor.pedidosPorUsuario("12345");
    }

}
