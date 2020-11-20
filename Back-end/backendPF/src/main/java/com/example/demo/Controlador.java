package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
@RequestMapping("/api")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Controlador {
    HashMap datos;

    @Autowired
    private ManejarJson accesoMongoDB;

    public Controlador() {
        this.accesoMongoDB = new ManejarJson();
        this.datos = new HashMap();
    }

    @GetMapping("/")
    public String rest() {
        return "rest1copia";
    }

    @RequestMapping(value = "/pedidoALaBase/{idRestaurante}/{idMesa}", method = RequestMethod.PUT)
    public ResponseEntity<Object> obtenerPedidoDeLaApi(@RequestBody HashMap platosPedido, @PathVariable int idMesa, @PathVariable int idRestaurante){
        this.accesoMongoDB.insertarPlatosPedido(idMesa, idRestaurante, platosPedido);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/dataRest/seccionesPlatos/{idRestaurante}", method = RequestMethod.GET)
    public ResponseEntity<Object> enviarSeccionesPlatosAlaAPI(@PathVariable int idRestaurante){
        return new ResponseEntity<>(accesoMongoDB.seccionesPlatosAlaAPI(idRestaurante), HttpStatus.OK);
    }

    @RequestMapping(value = "/dataRest/tipos", method = RequestMethod.POST)
    public ResponseEntity<Object> enviarTiposAlaAPI(){
        return new ResponseEntity<>(accesoMongoDB.tiposPlatosAlaAPI(), HttpStatus.OK);
    }

    @RequestMapping(value = "/dataRest/dataUser/{idRestaurante}", method = RequestMethod.GET)
    public ResponseEntity<Object> enviarDataUserAlaAPI(@PathVariable int idRestaurante){
        return new ResponseEntity<>(accesoMongoDB.dataUserAlaAPI(idRestaurante), HttpStatus.OK);
    }

    @RequestMapping(value = "/dataRest/platosYaPedidos/{idRestaurante}/{idMesa}", method = RequestMethod.GET)
    public ResponseEntity<Object> enviarPlatosYaPedidosAlaAPI(@PathVariable int idRestaurante, @PathVariable int idMesa){
        return new ResponseEntity<>(accesoMongoDB.platosYApedidosAlaAPI(idMesa,idRestaurante), HttpStatus.OK);
    }

    //APP

    @RequestMapping(value = "/javaAPP/seccionesPlatos/{idRestaurante}", method = RequestMethod.GET)
    public ResponseEntity<Object> enviarSeccionesPlatosAlaAPIjava(@PathVariable int idRestaurante){
        return new ResponseEntity<>(accesoMongoDB.seccionesPlatosAlaAPIjava(idRestaurante), HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/login/{username}/{password}", method = RequestMethod.GET)
    public ResponseEntity<Integer> loginAppGestion(@PathVariable String username, @PathVariable String password){
        return new ResponseEntity<>(accesoMongoDB.login(username, password), HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/actualizarPerfil/nombre/{username}/{idRestaurante}", method = RequestMethod.PUT)
    public ResponseEntity<Object> actualizarNombreRest(@PathVariable String username, @PathVariable int idRestaurante){
        accesoMongoDB.actualizarNombreRest(username, idRestaurante);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/actualizarPerfil/img/{img}/{idRestaurante}", method = RequestMethod.PUT)
    public ResponseEntity<Object> actualizarImgRest(@PathVariable String img, @PathVariable int idRestaurante){
        accesoMongoDB.actualizarIMGRest(img, idRestaurante);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/gestionarPedidos/cobrar/{idPedido}/{idRestaurante}", method = RequestMethod.PUT)
    public ResponseEntity<Object> cobrar(@PathVariable int idPedido, @PathVariable int idRestaurante){
        accesoMongoDB.actualizarEstadoPedido(idPedido, idRestaurante);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/gestionarPedidos/actualizarPedido/{idPedido}/{idRestaurante}", method = RequestMethod.PUT)
    public ResponseEntity<Object> actualizarPedido(@RequestBody HashMap pedido, @PathVariable int idPedido, @PathVariable int idRestaurante){
        accesoMongoDB.actualizarPedido(idPedido, idRestaurante,pedido);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/gestionarMesas/agregarMesa/{idRestaurante}", method = RequestMethod.PUT)
    public ResponseEntity<Object> agregarMesa(@RequestBody HashMap mesa, @PathVariable int idRestaurante){
        accesoMongoDB.agregarMesa(mesa, idRestaurante);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/gestionarMesas/estado/{estadoMesa}/{idMesa}/{idRestaurante}", method = RequestMethod.PUT)
    public ResponseEntity<Object> agregarMesa(@PathVariable int idMesa, @PathVariable boolean estadoMesa,@PathVariable int idRestaurante){
        accesoMongoDB.cambiarEstadoMesa(estadoMesa, idMesa, idRestaurante);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/gestionarMesas/qr/{qr}/{idMesa}/{idRestaurante}", method = RequestMethod.PUT)
    public ResponseEntity<Object> agregarMesa(@PathVariable String qr, @PathVariable int idMesa,@PathVariable int idRestaurante){
        accesoMongoDB.cambiarQR(qr, idMesa, idRestaurante);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/gestionarMesas/borrarMesa/{idMesa}/{idRestaurante}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> agregarMesa(@PathVariable int idMesa,@PathVariable int idRestaurante){
        accesoMongoDB.borrarMesa(idMesa, idRestaurante);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/gestionarMenu/borrarMenu/{idRestaurante}", method = RequestMethod.PUT)
    public ResponseEntity<Object> borrarMenu(@PathVariable int idRestaurante){
        accesoMongoDB.borrarMenu(idRestaurante);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/gestionarMenu/agregarSeccion/{nombre}/{idRestaurante}", method = RequestMethod.PUT)
    public ResponseEntity<Object> borrarMenu(@PathVariable String nombre, @PathVariable int idRestaurante){
        accesoMongoDB.agregarSeccionPlatos(nombre,idRestaurante);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/gestionarMenu/editarNombreSeccion/{nombre}/{nombreNuevo}/{idRestaurante}", method = RequestMethod.PUT)
    public ResponseEntity<Object> editarNombreSeccion(@PathVariable String nombre, @PathVariable String nombreNuevo,@PathVariable int idRestaurante){
        accesoMongoDB.editarNombreSeccionPlatos(nombre,nombreNuevo,idRestaurante);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/gestionarMenu/borrarSeccion/{nombre}/{idRestaurante}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> borrarSeccion(@PathVariable String nombre,@PathVariable int idRestaurante){
        accesoMongoDB.borrarSeccionPlato(nombre,idRestaurante);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/gestionarMenu/actualizarPlatos/{nombre}/{idRestaurante}", method = RequestMethod.PUT)
    public ResponseEntity<Object> actualizarPlatos(@RequestBody HashMap platos, @PathVariable String nombre,@PathVariable int idRestaurante){
        accesoMongoDB.actualizarPlatos(nombre,platos,idRestaurante);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/obtenerMesas/{idRestaurante}", method = RequestMethod.GET)
    public ResponseEntity<Object> obtenerMesas(@PathVariable int idRestaurante){
        return new ResponseEntity<>(accesoMongoDB.obtenerMesas(idRestaurante), HttpStatus.OK);
    }

    @RequestMapping(value = "/javaAPP/obtenerPedidos/{idRestaurante}", method = RequestMethod.GET)
    public ResponseEntity<Object> obtenerPedidos(@PathVariable int idRestaurante){
        return new ResponseEntity<>(accesoMongoDB.obtenerPedidos(idRestaurante), HttpStatus.OK);
    }

}

