package com.company;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.sun.jdi.connect.spi.Connection;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;

public class AccesoMongoDB {
    private Connection connection;
    private MongoDatabase base;
    private String host;
    private int puerto;
    private String usuario;
    private String password;

    public static Bson requisitosLogin;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    private MongoClient mongoClient;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public MongoDatabase getBase() {
        return base;
    }

    public void setBase(MongoDatabase base) {
        this.base = base;
    }

    public AccesoMongoDB(String nombreBase/*, String host, int puerto*/) {
        MongoClient mongo = new MongoClient(/*host, puerto*/);//si no le pasas nada funciona tambien porque esta dentro de tu computadora, es decir es LOCAL
        this.mongoClient = mongo;
        this.base = this.mongoClient.getDatabase(nombreBase);
    }

    public ArrayList<Pedido> obtenerPedidos() {//resolver la fecha de los platos
        MongoCollection collection = this.base.getCollection("restaurante");
        ArrayList<Pedido> pedidos = new ArrayList<>();

        String json = "{_id:0, pedidos:1}";
        Bson bson = BasicDBObject.parse(json);
        FindIterable resultado = collection.find(requisitosLogin).projection(bson);

        MongoCursor iterator = resultado.iterator();

        while (iterator.hasNext()) {
            Document document = (Document) iterator.next();
            ArrayList<Document> documents = (ArrayList<Document>) document.get("pedidos");

            for (Document dataPlato : documents) {
                ArrayList<Document> platosDoc = (ArrayList<Document>) dataPlato.get("platos");
                ArrayList<PlatoPedido> platos = new ArrayList<>();

                for (Document dataPLATO : platosDoc){
                    ArrayList<Document> agregadosDoc = (ArrayList<Document>) dataPLATO.get("agregados");
                    HashMap<String, Float> agregados = new HashMap<>();
                    if (agregadosDoc != null){
                        for (Document dataAgregado : agregadosDoc) {
                            agregados.put(dataAgregado.getString("nombre"), Float.parseFloat(dataAgregado.get("precio").toString()));
                        }
                    }
                    Date date = new Date();
                    try{
                        System.out.println("sa "+dataPLATO.getString("fecha"));
                        date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dataPLATO.getString("fecha"));
                    }catch (ParseException e){
                        System.out.println("cause");
                        e.getCause();
                        System.out.println("message");
                        e.getMessage();
                        System.out.println("entre");
                    }
                    System.out.println(date+" date");
                    System.out.println("dateFormat: "+ new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date));
                    platos.add(new PlatoPedido(dataPLATO.getString("nombrePlato"), Float.parseFloat(dataPLATO.get("precio").toString()), agregados, date));
                }
                pedidos.add(new Pedido(dataPlato.getInteger("nMesa"), platos, dataPlato.getString("fecha"), dataPlato.getInteger("nPedido")));
            }
        }
        return pedidos;
    }

    public HashSet<Plato> obtenerPlatos(){
        MongoCollection collection = this.base.getCollection("restaurante");
        HashSet<Plato> platos = new HashSet<>();

        String json = "{_id:0, platos:1, agregados: 1}";
        Bson bson =  BasicDBObject.parse( json );
        FindIterable resultado = collection.find(requisitosLogin).projection(bson);

        MongoCursor iterator = resultado.iterator();

        while (iterator.hasNext()){
            Document document = (Document) iterator.next();

            ArrayList<Document> doc = (ArrayList<Document>) document.get("platos");

            for (Document docAUX: doc){

                HashSet<TipoAgregados> tiposAgregados = new HashSet<>();
                ArrayList<Document> agregadosDoc = (ArrayList<Document>) docAUX.get("agregados");

                for (Document agregadosDocAux : agregadosDoc){

                    ArrayList<Document> agregadoDoc = (ArrayList<Document>) agregadosDocAux.get("agregado");
                    HashMap<String, Float> agregados = new HashMap<>();

                    for (Document agregadoDocAux : agregadoDoc){

                        agregados.put(agregadoDocAux.getString("nombre"), Float.parseFloat(agregadoDocAux.get("precio").toString()));

                    }

                    tiposAgregados.add(new TipoAgregados(agregadosDocAux.getString("tipo"), agregadosDocAux.getBoolean("indispensable"), agregados));

                }
                platos.add(new Plato(docAUX.getString("nombre"), Float.parseFloat(docAUX.get("precio").toString()), new File(docAUX.get("imagen").toString()), docAUX.getString("descripcion"), docAUX.getString("demora"), tiposAgregados));
            }
        }
        return platos;
    }/*String nombre, float precio, File img, String descripcion, String tiempoDemora, HashSet<TipoAgregados> agregados*/

    public boolean login(String username, String password){
        MongoCollection collection = this.base.getCollection("restaurante");
        ArrayList<Bson> filtros = new ArrayList<>();

        Bson filtro1= Filters.eq("user", username);
        Bson filtro2 = Filters.eq("password", password);
        Bson filtroA = Filters.elemMatch("login", filtro1);
        Bson filtroB = Filters.elemMatch("login", filtro2);

        filtros.add(filtroA);
        filtros.add(filtroB);

        requisitosLogin = and(filtros);

        FindIterable resultado = collection.find(requisitosLogin);

        MongoCursor iterator = resultado.iterator();

        while (iterator.hasNext()){
            Document document = (Document) iterator.next();

            this.usuario = username;
            this.password = password;

            return true;
        }
        return false;
    }
}
