package com.company;

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

import java.util.ArrayList;
import java.util.HashSet;

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

    public ArrayList<Pedido> obtenerPedidos(String nombreCollection){
        ArrayList<Pedido> pedidos = new ArrayList<>();
        MongoCollection collection = this.base.getCollection(nombreCollection);




        return pedidos;
    }

    public /*HashSet<Plato>*/void obtenerPlatos(){
        MongoCollection collection = this.base.getCollection("restaurante");
        HashSet<Plato> platos = new HashSet<>();

        String json = "{_id:0, platos:1, agregados: 1}";
        Bson bson =  BasicDBObject.parse( json );
        FindIterable resultado = collection.find(requisitosLogin).projection(bson);

        MongoCursor iterator = resultado.iterator();

        while (iterator.hasNext()){
            Document document = (Document) iterator.next();
            System.out.println(document);
        }/*pasarlo a objeto mirar video nadia*/
    }

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
