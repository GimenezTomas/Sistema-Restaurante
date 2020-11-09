package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
@RequestMapping("/api")
@RestController
public class Controlador {

    @Autowired
    private ManejarJson accesoMongoDB;

    public Controlador() {
        this.accesoMongoDB = new ManejarJson();
    }

    @GetMapping("/")
    public String rest() {
        return "rest1copia";
    }

    /*@RequestMapping(value = "/dataRest", method = RequestMethod.POST)
    public ResponseEntity<Object> enviarData(@RequestParam int idMesa, @RequestParam int idRestaurante){
        HashMap<String, Object> dataUser = accesoMongoDB.dataUserAlaAPI(idRestaurante);
        HashMap<String, Object> seccionesPlatosAlaAPI = accesoMongoDB.seccionesPlatosAlaAPI(idRestaurante);
        HashMap<String, Object> platosYaPedidos = accesoMongoDB.platosYApedidosAlaAPI(idMesa, idRestaurante);
        HashMap<String, Object> tiposBox = accesoMongoDB.tiposPlatosAlaAPI();

        HashMap<String, Object> paquete = new HashMap<>();
        paquete.put("dataUser", dataUser);
        paquete.put("seccionesPlatos", seccionesPlatosAlaAPI);
        paquete.put("platosYaPedidos", platosYaPedidos);
        paquete.put("tiposBox", tiposBox);
        return new ResponseEntity<>(paquete, HttpStatus.OK);
    }*/

    @RequestMapping(value = "/pedidoALaBase", method = RequestMethod.POST)
    public ResponseEntity<Object> obtenerPedidoDeLaApi(@RequestBody HashMap platosPedido, @RequestParam int idMesa, @RequestParam int idRestaurante){
        try {
            this.accesoMongoDB.insertarPlatoPedido(idMesa, idRestaurante, platosPedido);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/dataRest/seccionesPlatos", method = RequestMethod.POST)
    public ResponseEntity<Object> seccionesPlatosAlaAPI(@RequestParam int idRestaurante){
        return new ResponseEntity<>(accesoMongoDB.seccionesPlatosAlaAPI(idRestaurante), HttpStatus.OK);
    }

    @RequestMapping(value = "/dataRest/tipos", method = RequestMethod.POST)
    public ResponseEntity<Object> tiposAlaAPI(){
        return new ResponseEntity<>(accesoMongoDB.tiposPlatosAlaAPI(), HttpStatus.OK);
    }

    @RequestMapping(value = "/dataRest/dataUser", method = RequestMethod.POST)
    public ResponseEntity<Object> dataUserAlaAPI(@RequestParam int idRestaurante){
        return new ResponseEntity<>(accesoMongoDB.dataUserAlaAPI(idRestaurante), HttpStatus.OK);
    }

    @RequestMapping(value = "/dataRest/platosYaPedidos", method = RequestMethod.POST)
    public ResponseEntity<Object> platosYaPedidosAlaAPI(@RequestParam int idRestaurante, @RequestParam int idMesa){
        return new ResponseEntity<>(accesoMongoDB.platosYApedidosAlaAPI(idMesa,idRestaurante), HttpStatus.OK);
    }
}

