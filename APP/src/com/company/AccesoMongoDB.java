package com.company;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.jdi.connect.spi.Connection;
import org.bson.BSON;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class AccesoMongoDB {
    private Connection connection;
    private MongoDatabase base;
    private String host;
    private int puerto;

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

    public AccesoMongoDB(String nombreBase, String host, int puerto) {
        MongoClient mongo = new MongoClient(host, puerto);//si no le pasas nada funciona tambien porque esta dentro de tu computadora, es decir es LOCAL
        this.mongoClient = mongo;
        this.base = this.mongoClient.getDatabase(nombreBase);
    }

    public ArrayList<Pedido> obtenerPedidos(String nombreCollection){
        ArrayList<Pedido> pedidos = new ArrayList<>();
        MongoCollection collection = this.base.getCollection(nombreCollection);

        


        return pedidos;
    }
}
