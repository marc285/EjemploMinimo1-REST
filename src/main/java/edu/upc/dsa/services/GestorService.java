package edu.upc.dsa.services;

import edu.upc.dsa.*;
import edu.upc.dsa.models.Pedido;
import edu.upc.dsa.models.LP;
import edu.upc.dsa.models.Producto;
import edu.upc.dsa.models.Usuario;

import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Api(value = "/gestor", description = "Endpoint to Gestor Service")
@Path("/gestor")
public class GestorService {

    private Gestor gestor;

    public GestorService() throws NoPedidosException {
        this.gestor = GestorImpl.getInstance();
        if (gestor.getProductos().size() == 0 && gestor.getUsuarios().size() == 0 && gestor.getPedidos().size() == 0){
            //SET UP DE LAS ESTRUCTURAS
            gestor.addUsuario("12345", "Toni");

            gestor.addProducto("chocolatina", 1);
            gestor.addProducto("cocacola", 1.5);
            gestor.addProducto("bocadillo_jamon", 3.5);
            gestor.addProducto("zumo_naranja", 1.2);

            List<LP> lps = new LinkedList<LP>();
            lps.add(new LP(3,"chocolatina"));
            lps.add(new LP(2,"zumo_naranja"));
            lps.add(new LP(1, "bocadillo_jamon"));

            Pedido p = new Pedido("12345",lps);

            /*p.addLP(3, "chocolatina");
            p.addLP(2, "zumo_naranja");
            p.addLP(1, "bocadillo_jamon");
            */

            gestor.anotarPedido(p);
            gestor.servirPedido();
        }
    }


    @GET
    @ApiOperation(value = "Listado de productos ordenados por Precio (orden ascendente)", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Producto.class, responseContainer="List"),
            @ApiResponse(code = 404, message= "Lista de productos no encontrada (está vacía)")
    })
    @Path("/getProdOrdPrec")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProdOrdPrec() throws NoProductosException {

        LinkedList<Producto> prods = this.gestor.productosOrdPrecio();
        GenericEntity<LinkedList<Producto>> entity = new GenericEntity<LinkedList<Producto>>(prods) {};

        if(this.gestor.getProductos() != null)
            return Response.status(201).entity(entity).build();
        else
            return Response.status(404).entity(entity).build();
    }


    @GET
    @ApiOperation(value = "Listado de productos ordenados por Número de ventas (orden descendente)", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Producto.class, responseContainer="List"),
            @ApiResponse(code = 404, message= "Lista de productos no encontrada (está vacía)")
    })
    @Path("/getProdOrdVentas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProdOrdVentas() throws NoProductosException {

        LinkedList<Producto> prods = this.gestor.productosOrdVentas();
        GenericEntity<LinkedList<Producto>> entity = new GenericEntity<LinkedList<Producto>>(prods) {};

        if(this.gestor.getProductos() != null)
            return Response.status(201).entity(entity).build();
        else
            return Response.status(404).entity(entity).build();
    }


    @GET
    @ApiOperation(value = "Listado (Historial) de Pedidos de un Usuario dado", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Pedido.class, responseContainer="List"),
            @ApiResponse(code = 404, message= "Usuario no encontrado. No existe o la tabla de usuarios está vacía.")
    })
    @Path("/getPedidosUsuario/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPedidosUsr(@PathParam("id") String id) throws NoPedidosException, NoUsuarioException {

        LinkedList<Pedido> pedidos = this.gestor.pedidosPorUsuario(id);
        GenericEntity<LinkedList<Pedido>> entity = new GenericEntity<LinkedList<Pedido>>(pedidos) {};

        if(this.gestor.getUsuarios().get(id) != null)
            return Response.status(201).entity(entity).build();
        else
            return Response.status(404).entity(entity).build();
    }


    @POST
    @ApiOperation(value = "Anotar un pedido", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Pedido.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/anotarPedido")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response anotarPed( Pedido p
            /*@ApiParam(value = "ID del Usuario", required = true, ) String idusr
            @ApiParam(value = "Lista de productos del pedido", required = true) List<LP> listaprod*/ )
    {

        /*Pedido p = new Pedido();
        p.setIDUsuario(idusr);
        for(LP lp : listaprod){
            p.addLP(lp.getNumPedidos(),lp.getIDProducto());
        }*/
        if (p.getLPsize() == 0 || p.getIDUsuario() == null)  return Response.status(500).entity(p).build();
        this.gestor.anotarPedido(p);
        return Response.status(201).entity(p).build();
    }


    @PUT
    @ApiOperation(value = "Servir un pedido", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
            @ApiResponse(code = 404, message = "Cola de pedidos vacía")
    })
    @Path("/servirPedido")
    public Response servirPed() throws NoPedidosException {
        if(gestor.getPedidos().size() != 0) {
            gestor.servirPedido();
            return Response.status(201).build();
        }
        else
            return Response.status(404).build();
    }


    @DELETE
    @ApiOperation(value = "Liberar recursos", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful"),
    })
    @Path("/liberarRecursos")
    public Response liberarRecursos() {
        liberarRecursos();
        return Response.status(201).build();
    }
}
