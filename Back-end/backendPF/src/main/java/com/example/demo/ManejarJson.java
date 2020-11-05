package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
public class ManejarJson {
    private AccesoMongoDB mongo;

    public void insertarPlatoPedido(int mesa, int idRest, HashMap platoPedido) throws IOException {
//        File json = new File(".\\src\\main\\resources\\files\\jsonPedidos.json");
        File json = new File(".\\src\\main\\resources\\files\\collectionRestaurante.json");
        Bson filtro1= Filters.eq("id", 1);
        Pedido pedidoMONGO = mongo.obtenerPedido(1, 2, filtro1);//deberia de traer solo los abiertos

        ObjectMapper mapper1 = new ObjectMapper();
        //HashMap platoPedido = mapper1.readValue(json, HashMap.class);

        Document doc = new Document(platoPedido);
        ArrayList<PlatoPedido> platoPedidos = mongo.serializarPlatoPedido(doc);

        if ((mongo.obtenerPedido(idRest,mesa, filtro1)) == null){
            mongo.agregarPedido(new Pedido(mesa, platoPedidos, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), mongo.obtenerPedidosSize(filtro1)/*arreglar en mongo*/), filtro1);
        }else{
            mongo.insertarPlatosPedido(platoPedidos, mongo.obtenerPedido(idRest,mesa, filtro1).getnPedido(), filtro1);
        }
    }

    public HashMap<String, Object> seccionesPlatosAlaAPI(int id){
        //ObjectMapper mapper = new ObjectMapper();
        Bson filtro1= Filters.eq("id", id);

        HashMap<String, Object> jsonASER = new HashMap<>();
        jsonASER.put("seccionesPlatos", mongo.obtenerSecciones(filtro1));
        return jsonASER;
        /*try {
            mapper.writeValue(new File(".\\src\\main\\resources\\files\\seccionesPlatos.json"), jsonASER);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public HashMap<String, Object> tiposPlatosAlaAPI(){
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> jsonASER = new HashMap<>();

        jsonASER.put("tiposDeComida", mongo.obtenerTiposPlatos());
        return jsonASER;
        /*try {
            mapper.writeValue(new File(".\\src\\main\\resources\\files\\tiposDeComida.json"), jsonASER);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public HashMap<String, Object> dataUserAlaAPI(int id){
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> jsonASER = new HashMap<>();
        Bson filtro1= Filters.eq("id", id);

        jsonASER.put("dataUser", mongo.obtenerDataUser(filtro1));
        return jsonASER;
        /*try {
            mapper.writeValue(new File(".\\src\\main\\resources\\files\\dataUser.json"), jsonASER);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public HashMap<String, Object> platosYApedidosAlaAPI(int mesa, int idRest){
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> jsonASER = new HashMap<>();
        Bson filtro1= Filters.eq("id", 1);

        try {
            jsonASER.put("platosYaPedidos", mongo.obtenerPedido(idRest,mesa, filtro1).getPlatos());
        }catch (NullPointerException e){
            jsonASER.put("platosYaPedidos", new ArrayList<>());
            e.printStackTrace();
        }
        return jsonASER;
        /*try {
            mapper.writeValue(new File(".\\src\\main\\resources\\files\\pedidosYaPedidos.json"), jsonASER);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
// db.restaurante.aggregate([{$match:{id:2}},{ $project: { pedidos: { $filter: { input: "$pedidos", as: "pedido", cond: { $eq: [ "$$pedido.abierto", true ] } } }, _id:0 } } ]) -> devuelve los pedidos del id : 1 que esten abiertos
//crear metodo en acceso mongo para identificar el restaurante que se quiere