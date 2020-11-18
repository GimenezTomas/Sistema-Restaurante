package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    public ManejarJson(){
        mongo = new AccesoMongoDB();
    }

    public void insertarPlatosPedido(int mesa, int idRest, HashMap platoPedido){
        Bson filtro1= Filters.eq("id", 1);

        Document document = new Document("platos",platoPedido.get("platos"));
        ArrayList<Document> documents = (ArrayList<Document>) document.get("platos");

        ArrayList<HashMap> platos = new ArrayList<>();
        for (int i = 0; i <documents.size() ; i++) {
            try {
                HashMap plato = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(documents.get(i)), HashMap.class);
                plato.put("fecha", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
                plato.put("entregado", false);
                platos.add(plato);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        HashMap hashMap = new HashMap<>();
        hashMap.put("platos", platos);
        if ((mongo.obtenerPedido(idRest,mesa, filtro1)) == null){
            HashMap<String, Object> pedidoAtributos = new HashMap<>();
            pedidoAtributos.put("nPedido", mongo.obtenerPedidosSize(filtro1)+1);
            pedidoAtributos.put("nMesa", mesa);
            pedidoAtributos.put("abierto", true);
            pedidoAtributos.put("fecha", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
            pedidoAtributos.put("platos", hashMap.get("platos"));
            mongo.agregarPedido(pedidoAtributos, filtro1);
        }else{
            mongo.insertarPlatosPedido(hashMap, mongo.obtenerPedido(idRest,mesa, filtro1).getnPedido(), filtro1);
        }
    }

    public HashMap<String, Object> seccionesPlatosAlaAPI(int id){
        Bson filtro1= Filters.eq("id", id);

        HashMap<String, Object> jsonASER = new HashMap<>();
        jsonASER.put("seccionesPlatos", mongo.obtenerSeccionesAPI(filtro1));
        return jsonASER;
    }

    public HashMap<String, Object> seccionesPlatosAlaAPIjava(int id){
        Bson filtro1= Filters.eq("id", id);

        HashMap<String, Object> jsonASER = new HashMap<>();
        jsonASER.put("seccionesPlatos", mongo.obtenerSecciones(filtro1));
        return jsonASER;
    }

    public HashMap<String, Object> tiposPlatosAlaAPI(){
        HashMap<String, Object> jsonASER = new HashMap<>();

        jsonASER.put("tiposDeComida", mongo.obtenerTiposPlatos());
        return jsonASER;
    }

    public HashMap<String, Object> dataUserAlaAPI(int id){
        HashMap<String, Object> jsonASER = new HashMap<>();
        Bson filtro1= Filters.eq("id", id);

        jsonASER.put("dataUser", mongo.obtenerDataUser(filtro1));
        return jsonASER;
    }

    public HashMap<String, Object> platosYApedidosAlaAPI(int mesa, int idRest){
        HashMap<String, Object> jsonASER = new HashMap<>();
        Bson filtro1= Filters.eq("id", 1);

        try {
            Object object = mongo.obtenerPedidoJ(idRest,mesa, filtro1);
            if (object != null) {
                jsonASER.put("platosYaPedidos", /*mongo.obtenerPedidoJ(idRest,mesa, filtro1)*/object);
            }else{
                jsonASER.put("platosYaPedidos", /*mongo.obtenerPedidoJ(idRest,mesa, filtro1)*/new ArrayList<>());
            }
        }catch (NullPointerException e){
            //jsonASER.put("platosYaPedidos", new Object());
            e.printStackTrace();
        }
        return jsonASER;
    }

    //APP

    public int login(String user, String pass){
        return mongo.login(user, pass);
    }

    public void actualizarNombreRest(String nombre, int id){
        Bson filtro1= Filters.eq("id", id);
        mongo.actualizarNombreRest(nombre, filtro1);
    }

    public void actualizarIMGRest(String img, int id){
        Bson filtro1= Filters.eq("id", id);
        mongo.actualizarImgRest(img, filtro1);
    }

    public void actualizarEstadoPedido(int idPed, int id){
        Bson filtro1= Filters.eq("id", id);
        mongo.actualizarEstadoPedido(idPed, filtro1);
    }

    public void actualizarPedido(int idPed, int id, HashMap pedido){
        Bson filtro1= Filters.eq("id", id);
        mongo.actualizarPedido(idPed-1, pedido, filtro1);
    }

    public void agregarMesa(HashMap mesa, int id){
        Bson filtro1= Filters.eq("id", id);
        mongo.agregarMesa(mesa, filtro1);
    }

    public void cambiarEstadoMesa(boolean estado, int idMesa, int id){
        Bson filtro1= Filters.eq("id", id);
        mongo.cambiarEstadoMesa(estado, idMesa, filtro1);
    }

    public void cambiarQR(String qr, int nMesa, int id){
        Bson filtro1= Filters.eq("id", id);
        mongo.cambiarQR(qr, nMesa, filtro1);
    }

    public void borrarMesa(int nMesa, int id){
        Bson filtro1= Filters.eq("id", id);
        mongo.borrarMesa(nMesa, filtro1);
    }

    public void borrarMenu(int id){
        Bson filtro1= Filters.eq("id", id);
        mongo.borrarMenu(filtro1);
    }

    public void agregarSeccionPlatos(String nombre, int id){
        Bson filtro1= Filters.eq("id", id);
        mongo.agregarSeccionPlatos(nombre, filtro1);
    }

    public void editarNombreSeccionPlatos(String nombre, String nombreNuevo,int id){
        Bson filtro1= Filters.eq("id", id);
        mongo.editarNombreSeccion(nombre, nombreNuevo,filtro1);
    }

    public void borrarSeccionPlato(String nombre, int id){
        Bson filtro1= Filters.eq("id", id);
        mongo.borrarSeccion(nombre, filtro1);
    }

    public void actualizarPlatos(String nombre, HashMap platos, int id){
        Bson filtro1= Filters.eq("id", id);
        mongo.actualizarPlatos(platos, nombre, filtro1);
    }

    public HashMap obtenerMesas(int id){
        Bson filtro1= Filters.eq("id", id);
        return mongo.obtenerMesas(filtro1);
    }

    public HashMap obtenerPedidos(int id){
        Bson filtro1= Filters.eq("id", id);
        return mongo.obtenerPedidos(filtro1);
    }
}
// db.restaurante.aggregate([{$match:{id:2}},{ $project: { pedidos: { $filter: { input: "$pedidos", as: "pedido", cond: { $eq: [ "$$pedido.abierto", true ] } } }, _id:0 } } ]) -> devuelve los pedidos del id : 1 que esten abiertos
//crear metodo en acceso mongo para identificar el restaurante que se quiere