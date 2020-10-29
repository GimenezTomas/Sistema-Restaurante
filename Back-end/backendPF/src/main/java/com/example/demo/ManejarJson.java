package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ManejarJson {
    private AccesoMongoDB mongo;

    public void insertarPlatoPedido(int mesa, int idRest) throws IOException {
//        File json = new File(".\\src\\main\\resources\\files\\jsonPedidos.json");
        File json = new File(".\\src\\main\\resources\\files\\collectionRestaurante.json");
        Bson filtro1= Filters.eq("id", 1);
        Pedido pedidoMONGO = mongo.obtenerPedido(1, 2, filtro1);//deberia de traer solo los abiertos

        ObjectMapper mapper1 = new ObjectMapper();
        HashMap platoPedido = mapper1.readValue(json, HashMap.class);

        Document doc = new Document(platoPedido);
//primero serializar platoPedido y desp fijarse si hay pedido, sino crearlo
        ArrayList<PlatoPedido> platoPedidos = mongo.serializarPlatoPedido(doc);
        Pedido pedido = mongo.obtenerPedido(idRest,mesa, filtro1);
        if (pedido == null){
            mongo.agregarPedido();
        }
        /*if (pedidoMONGO.getnPedido()==0){
            pedidoMONGO = new Pedido(1,new ArrayList<PlatoPedido>(), new Date());
        }

        for (Pedido pedidoMongo : pedidosMONGO){
            if ()
        }*/
    }
    public static void main(String [] args) throws IOException {
        ManejarJson m = new ManejarJson();
        m.insertarPlatoPedido();
    }
}
//crear metodo en acceso mongo para identificar el restaurante que se quiere