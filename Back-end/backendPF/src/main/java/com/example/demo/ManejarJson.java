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

    public ManejarJson(){
        mongo = new AccesoMongoDB();
    }

    public void insertarPlatoPedido(int mesa, int idRest, HashMap platoPedido) throws IOException {
        Bson filtro1= Filters.eq("id", 1);

        ArrayList<PlatoPedido> platoPedidos = new ArrayList<>();

        Document document = new Document("platos",platoPedido.get("platos"));
        ArrayList<Document> documents = (ArrayList<Document>) document.get("platos");

        for (int i = 0; i < documents.size() ; i++) {
            File archivo = new File(".\\src\\main\\resources\\arch.json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(archivo, documents.get(i));

            HashMap plato = objectMapper.readValue(archivo, HashMap.class);

            archivo.delete();
            File archivo1 = new File(".\\src\\main\\resources\\arch.json");

            HashMap<String, Float> agregados = new HashMap<>();

            Document document1 = new Document("agregados",plato.get("agregados"));
            ArrayList<Document> documents1 = (ArrayList<Document>) document1.get("agregados");

            for (int j = 0; j <documents1.size() ; j++) {
                objectMapper.writeValue(archivo1, documents1.get(j));
                HashMap agregadosMap = objectMapper.readValue(archivo1, HashMap.class);
                agregados.put(agregadosMap.get("nombre").toString(), Float.parseFloat(agregadosMap.get("precio").toString()));
            }

            platoPedidos.add(new PlatoPedido(plato.get("nombre").toString(), Float.parseFloat(plato.get("precio").toString()), agregados, new Date(), false));
            archivo1.delete();
        }
        if ((mongo.obtenerPedido(idRest,mesa, filtro1)) == null){
            mongo.agregarPedido(new Pedido(mesa, platoPedidos, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), mongo.obtenerPedidosSize(filtro1)), filtro1);
        }else{
            mongo.insertarPlatosPedido(platoPedidos, mongo.obtenerPedido(idRest,mesa, filtro1).getnPedido(), filtro1);
        }
    }

    public HashMap<String, Object> seccionesPlatosAlaAPI(int id){
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
            jsonASER.put("platosYaPedidos", mongo.obtenerPedido(idRest,mesa, filtro1).getPlatos());
        }catch (NullPointerException e){
            jsonASER.put("platosYaPedidos", new ArrayList<>());
            e.printStackTrace();
        }
        return jsonASER;
    }

    //APP

    public Boolean login(String user, String pass){
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