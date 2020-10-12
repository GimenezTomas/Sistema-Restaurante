package com.company;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.sun.jdi.connect.spi.Connection;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class AccesoMongoDB {
    private Connection connection;
    private DB base;
    private MongoClient mongoClient;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public DB getBase() {
        return base;
    }

    public void setBase(DB base) {
        this.base = base;
    }

    public AccesoMongoDB(String nombreBase) {
        try {
            MongoClient mongo = new MongoClient();
            this.mongoClient = mongo;
            this.base = this.mongoClient.getDB(nombreBase);
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
    }

    public void obtenerPedidos(String nombreCollection, ArrayList<Pedido>pedidos){
        try{
            DBCollection collection = this.base.getCollection(nombreCollection);
            B
        }catch (UnknownHostException e){

        }
    }
}
