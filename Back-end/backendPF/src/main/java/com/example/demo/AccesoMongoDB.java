package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.connection.Connection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AccesoMongoDB {
    private MongoDatabase base;
    private Connection connection;
    private String host;
    private int puerto;

    public MongoDatabase getBase() {
        return base;
    }

    public void setBase(MongoDatabase base) {
        this.base = base;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

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

    //public static Bson requisitosLogin;
    public AccesoMongoDB(/*String base*//*, String host, int puerto*/) {
        MongoClient mongoClient = new MongoClient();
        this.base = mongoClient.getDatabase(/*base*/"proyectoFinal");
        /*this.host = host;
        this.puerto = puerto;*/
    }

    public ArrayList<PlatoPedido> serializarPlatoPedido(Document dataPlato){
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
                date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dataPLATO.getString("fecha"));
            }catch (ParseException e){
                e.getCause();
                e.getMessage();
            }
            platos.add(new PlatoPedido(dataPLATO.getString("nombrePlato"), Float.parseFloat(dataPLATO.get("precio").toString()), agregados, date, dataPLATO.getBoolean("entregado")));
        }
        return platos;
    }

    public ArrayList<Plato> obtenerPlatosArr(Bson requisitosLogin){
        MongoCollection collection = this.base.getCollection("restaurante");
        ArrayList<Plato> platos = new ArrayList<>();

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
    }

    public ArrayList<HashMap<String, Object>> platosMONGO(HashSet<Plato>platos){
        ArrayList<HashMap<String, Object>> platosMon = new ArrayList<>();
        for (Plato plato : platos) {
            HashMap<String, Object> platoAtributos = new HashMap<>();
            platoAtributos.put("nombre", plato.getNombre());
            platoAtributos.put("precio", plato.getPrecio());
            platoAtributos.put("descripcion", plato.getDescripcion());
            platoAtributos.put("demora", plato.getTiempoDemora());
            platoAtributos.put("imagen", plato.getImg());

            ArrayList<HashMap<String, Object>> agregadosSeccion = new ArrayList<>();

            for (TipoAgregados agregadoSeccion : plato.getAgregados()) {

                ArrayList<Map<String, Object>> agregados = new ArrayList<>();

                for (Map.Entry<String, Float> agregadoAUX : agregadoSeccion.getAgregados().entrySet()) {

                    HashMap<String, Object> agregado = new HashMap<>();
                    agregado.put("nombre", agregadoAUX.getKey());
                    agregado.put("precio", agregadoAUX.getValue());
                    agregados.add(agregado);
                }
                HashMap<String, Object> agS = new HashMap<>();
                agS.put("tipo", agregadoSeccion.getNombre());
                agS.put("indispensable", agregadoSeccion.getIndispensable());
                agS.put("agregado", agregados);
                agregadosSeccion.add(agS);
            }

            platoAtributos.put("agregados", agregadosSeccion);
            platosMon.add(platoAtributos);
        }
        return platosMon;
    }

    public ArrayList<HashMap<String, Object>> platosPedidoMONGO(ArrayList<PlatoPedido>platos){
        ArrayList<HashMap<String, Object>> platosMon = new ArrayList<>();
        for (PlatoPedido plato : platos) {
            HashMap<String, Object> platoAtributos = new HashMap<>();
            platoAtributos.put("nombrePlato", plato.getNombre());
            platoAtributos.put("precio", plato.getPrecio());
            platoAtributos.put("fecha", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(plato.getFecha()));
            platoAtributos.put("entregado", plato.isEntregado());

            ArrayList<Map<String, Object>> agregados = new ArrayList<>();

            for (Map.Entry<String, Float> agregadoAUX : plato.getAgregados().entrySet()) {
                HashMap<String, Object> agregado = new HashMap<>();
                agregado.put("nombre", agregadoAUX.getKey());
                agregado.put("precio", agregadoAUX.getValue());
                agregados.add(agregado);
            }

            platoAtributos.put("agregados", agregados);
            platosMon.add(platoAtributos);
        }
        return platosMon;
    }

    public Pedido obtenerPedido(int idRestaurante, int nMesa, Bson requisitosLogin){
        MongoCollection  collection = this.base.getCollection("restaurante");

        Bson filtro1= Filters.eq("id", idRestaurante);
        String json = "{_id:0, pedidos:{$elemMatch:{nMesa:"+nMesa+", abierto:true}}}";
        Bson bson = BasicDBObject.parse(json);
        FindIterable resultado = collection.find(filtro1/*o requisitosLogin*/).projection(bson);

        MongoCursor iterator = resultado.iterator();

        while (iterator.hasNext()) {
            Document document = (Document) iterator.next();
            ArrayList<Document> documents = (ArrayList<Document>) document.get("pedidos");
            if (documents!=null) {
                for (Document dataPlato : documents) {
                    ArrayList<Document> platosDoc = (ArrayList<Document>) dataPlato.get("platos");
                    ArrayList<PlatoPedido> platos = new ArrayList<>();

                    for (Document dataPLATO : platosDoc) {
                        ArrayList<Document> agregadosDoc = (ArrayList<Document>) dataPLATO.get("agregados");
                        HashMap<String, Float> agregados = new HashMap<>();
                        if (agregadosDoc != null) {
                            for (Document dataAgregado : agregadosDoc) {
                                agregados.put(dataAgregado.getString("nombre"), Float.parseFloat(dataAgregado.get("precio").toString()));
                            }
                        }
                        Date date = new Date();
                        try {
                            date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dataPLATO.getString("fecha"));
                        } catch (ParseException e) {
                            e.getCause();
                            e.getMessage();
                        }
                        platos.add(new PlatoPedido(dataPLATO.getString("nombrePlato"), Float.parseFloat(dataPLATO.get("precio").toString()), agregados, date, dataPLATO.getBoolean("entregado")));
                    }
                    return new Pedido(dataPlato.getInteger("nMesa"), platos, dataPlato.getString("fecha"), dataPlato.getInteger("nPedido"));
                }
            }
        }
        return null;
    }

    public void agregarPedido(Pedido pedido, Bson requisitosLogin){
        HashMap<String, Object> pedidoAtributos = new HashMap<>();
        pedidoAtributos.put("nPedido", pedido.getnPedido());
        pedidoAtributos.put("nMesa", pedido.getnMesa());
        pedidoAtributos.put("abierto", pedido.isAbierto());
        pedidoAtributos.put("fecha", pedido.getFecha());
        pedidoAtributos.put("platos", platosPedidoMONGO(pedido.getPlatos()));

        try {
            ObjectMapper mapper = new ObjectMapper();
            File json = new File(".\\src\\com\\company\\pedidos.json");

            mapper.writeValue(json, pedidoAtributos);

            ObjectMapper mapper1 = new ObjectMapper();
            HashMap pedidoMap = mapper1.readValue(json, HashMap.class);
            json.delete();

            Document pedidosDoc = new Document("pedidos", pedidoMap);
            Document operacion = new Document("$push", pedidosDoc);

            UpdateResult result = this.getBase().getCollection("restaurante").updateOne(requisitosLogin, operacion);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertarPlatosPedido(ArrayList<PlatoPedido> platoPedidoList, int nPedido, Bson requisitosLogin) {
        for (int i = 0; i < platoPedidoList.size(); i++) {
            String ruta = "pedidos." + (nPedido - 1) + ".platos";
            Document rutaDoc = new Document(ruta, platosPedidoMONGO(platoPedidoList).get(i));

            Document operacion = new Document("$push", rutaDoc);

            this.base.getCollection("restaurante").updateOne(requisitosLogin, operacion);
        }
    }//pregunatarle a piñeyro si esta forma puede llegar a treaer problemas(se puede llegar a perder un pedido o que se cambie el orden)

    public int obtenerPedidosSize(Bson requisitosLogin){
        int size = 0;

        Bson $if = BasicDBObject.parse("{ $cond: { if: { $isArray: \"$pedidos\" }, then: { $size: \"$pedidos\" }, else: 0} }");
        Collection sizeInCollection = this.base.getCollection("restaurante").aggregate(Arrays.asList(Aggregates.match(requisitosLogin),Aggregates.project( Projections.fields( Projections.excludeId(), Projections.include("pedidos"), Projections.computed("pedidos", $if))))).into(new ArrayList());
        Iterator sizeInIterator = sizeInCollection.iterator();
        while (sizeInIterator.hasNext()){
            Document sizeInDocument = (Document) sizeInIterator.next();
            System.out.println(sizeInDocument.get("pedidos"));
            size = sizeInDocument.getInteger("pedidos");
        }
//db.restaurante.aggregate([{$match:{id:1}},{$project: {_id:0, pedidos: { $cond: { if: { $isArray: "$pedidos" }, then: { $size: "$pedidos" }, else: 0} } } }] )

        return size;
    }

    public ArrayList<HashMap<String, String>> obtenerTiposPlatos(){
        MongoCollection collection = this.base.getCollection("tiposDeComida");

        ArrayList<HashMap<String, String>> tiposList = new ArrayList<>();

        String json = "{_id:0, nombre:1, logo:1}";
        Bson bson =  BasicDBObject.parse( json );
        FindIterable resultado = collection.find().projection(bson);

        MongoCursor iterator = resultado.iterator();

        while (iterator.hasNext()){
            HashMap<String, String> tipos = new HashMap<>();
            Document document = (Document) iterator.next();
            tipos.put("nombre", document.getString("nombre"));
            tipos.put("logo", document.getString("logo"));
            tiposList.add(tipos);
        }

        return tiposList;
    }
    public ArrayList<SeccionesPlatos> obtenerSecciones(Bson requisitosLogin){
        ArrayList<SeccionesPlatos> seccionesPlatos = new ArrayList<>();

        MongoCollection collection = this.base.getCollection("restaurante");

        String json = "{_id:0, seccionesPlatos:1}";
        Bson bson =  BasicDBObject.parse( json );
        FindIterable resultado = collection.find(requisitosLogin).projection(bson);

        MongoCursor iterator = resultado.iterator();

        while(iterator.hasNext()){
            Document document = (Document) iterator.next();

            ArrayList<Document> documentss = (ArrayList<Document>) document.get("seccionesPlatos");
            for (Document doca :documentss){
                SeccionesPlatos seccionesPlatos1 = new SeccionesPlatos(doca.get("nombre").toString());
                ArrayList<Plato>platos = new ArrayList<>();

                ArrayList<Document> doc = (ArrayList<Document>) doca.get("platos");

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
                seccionesPlatos1.getPlatos().addAll(platos);
                seccionesPlatos.add(seccionesPlatos1);
            }
        }
        return  seccionesPlatos;
    }
    public HashMap<String, String> obtenerDataUser(Bson requisitosLogin){
        MongoCollection collection = this.base.getCollection("restaurante");

        FindIterable resultado = collection.find(requisitosLogin);

        MongoCursor iterator = resultado.iterator();

        HashMap<String,String> userData = new HashMap<>();

        while (iterator.hasNext()){
            Document document = (Document) iterator.next();
            userData.put("nombre", document.getString("nombre"));
            userData.put("logo", document.getString("logo"));
            userData.put("direccion",document.getString("direccion"));
        }
        return userData;
    }

}

//db.restaurante.aggregate([{$match:{id:1}},{$project: {_id:0, pedidos: { $cond: { if: { $isArray: "$pedidos" }, then: { $size: "$pedidos" }, else: 0} } } }] )
// db.restaurante.aggregate([{$match:{id:1}},{ $project: { pedidos: { $filter: { input: "$pedidos", as: "pedido", cond: { $eq: [ "$$pedido.abierto", true ] } } }, _id:0 } } ]) -> obtener platos abiertos