package com.company;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.IterableSerializer;
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
import com.sun.jdi.connect.spi.Connection;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;


import javax.print.Doc;
import javax.swing.text.html.HTMLDocument;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Projections.*;

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
//db.restaurante.aggregate([{$match:{id:1}},{ $project: { pedidos: { $filter: { input: "$pedidos", as: "pedido", cond: { $eq: [ "$$pedido.abierto", true ] } } }, _id:0 } } ])
        String json = "{_id:0, pedidos:1}";
        Bson bson = BasicDBObject.parse(json);

        Bson $if = BasicDBObject.parse("{ $filter: { input: \"$pedidos\", as: \"pedido\", cond: { $eq: [ \"$$pedido.abierto\", true ] } } }");
        Collection as = collection.aggregate(Arrays.asList(Aggregates.match(requisitosLogin),Aggregates.project( Projections.fields( Projections.excludeId(), Projections.include("pedidos"), Projections.computed("pedidos", $if))))).into(new ArrayList());

        Iterator ad = as.iterator();

        Iterator iterator = as.iterator();

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
                        date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dataPLATO.getString("fecha"));
                    }catch (ParseException e){
                        e.getCause();
                        e.getMessage();
                    }catch (DateTimeParseException e){
                        e.printStackTrace();
                    }
                    platos.add(new PlatoPedido(dataPLATO.getString("nombrePlato"), Float.parseFloat(dataPLATO.get("precio").toString()), agregados, date, dataPLATO.getBoolean("entregado")));
                }
                pedidos.add(new Pedido(dataPlato.getInteger("nMesa"), platos, dataPlato.getString("fecha"), dataPlato.getInteger("nPedido")));
            }
        }
        return pedidos;
    }

    public void obtenerDataUser(Restaurante restaurante){
        MongoCollection collection = this.base.getCollection("restaurante");

        FindIterable resultado = collection.find(requisitosLogin);

        MongoCursor iterator = resultado.iterator();

        while (iterator.hasNext()){
            Document document = (Document) iterator.next();
            restaurante.setLogo(new File(document.getString("logo")));
            restaurante.setNombre(document.getString("nombre"));
            restaurante.setDireccion(document.getString("direccion"));
        }
    }

    public void actualizarDataUser(Restaurante restaurante){
        HashMap<String, String> data = new HashMap<>();
        data.put("nombre",restaurante.getNombre());
        data.put("direccion", restaurante.getDireccion());
        data.put("logo", restaurante.getLogo().getName());

        Document operacion = new Document("$set", data);

        UpdateResult result = this.getBase().getCollection("restaurante").updateOne(requisitosLogin, operacion);

    }

    public HashSet<Mesa> obtenerMesas(){
        MongoCollection collection = this.base.getCollection("restaurante");
        HashSet<Mesa> mesas = new HashSet<>();

        String json = "{_id:0, mesas:1}";
        Bson bson =  BasicDBObject.parse( json );
        FindIterable resultado = collection.find(requisitosLogin).projection(bson);

        MongoCursor iterator = resultado.iterator();

        while (iterator.hasNext()){
            Document document = (Document) iterator.next();

            ArrayList<Document> doc = (ArrayList<Document>) document.get("mesas");

            for (Document mesaDoc: doc){
                if (mesaDoc.get("qr") == null){
                    mesas.add(new Mesa(mesaDoc.getInteger("numMesa"), mesaDoc.getBoolean("ocupada")));
                }
                else{
                    mesas.add(new Mesa(mesaDoc.getInteger("numMesa"), new File(mesaDoc.getString("qr")), mesaDoc.getBoolean("ocupada")));
                }
            }
        }

        return mesas;
    }

    public ArrayList<Mesa> obtenerMesasArr(){
        MongoCollection collection = this.base.getCollection("restaurante");
        ArrayList<Mesa> mesas = new ArrayList<>();

        String json = "{_id:0, mesas:1}";
        Bson bson =  BasicDBObject.parse( json );
        FindIterable resultado = collection.find(requisitosLogin).projection(bson);

        MongoCursor iterator = resultado.iterator();

        while (iterator.hasNext()){
            Document document = (Document) iterator.next();

            ArrayList<Document> doc = (ArrayList<Document>) document.get("mesas");

            for (Document mesaDoc: doc) {
                System.out.println(mesaDoc.getString("qr"));
                if (mesaDoc.get("qr")!=null) {
                    mesas.add(new Mesa(mesaDoc.getInteger("numMesa"), new File(mesaDoc.getString("qr")), mesaDoc.getBoolean("ocupada")));
                }else{
                    mesas.add(new Mesa(mesaDoc.getInteger("numMesa"), mesaDoc.getBoolean("ocupada")));
                }
            }
        }

        return mesas;
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
    }

    public ArrayList<Plato> obtenerPlatosArr(){
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

    public void actualizarSeccionesPlatos(ArrayList<SeccionesPlatos> seccionesPlatos){
        try {
            ArrayList<HashMap<String, Object>> secAtributos = new ArrayList<>();

            for (SeccionesPlatos seccionesPlatos1 : seccionesPlatos){
                HashMap<String, Object> seccion = new HashMap<>();
                seccion.put("nombre", seccionesPlatos1.getNombre());
                seccion.put("platos", platosMONGO(seccionesPlatos1.getPlatos()));
                secAtributos.add(seccion);
            }

            ObjectMapper mapper = new ObjectMapper();
            File json = new File(".\\src\\com\\company\\secciones.json");

            HashMap<String, Object> jsonSerializar = new HashMap<>();
            jsonSerializar.put("seccionesPlatos", secAtributos);

            mapper.writeValue(json, jsonSerializar);

            ObjectMapper mapper1 = new ObjectMapper();
            HashMap platosMAP = mapper1.readValue(json, HashMap.class);
            json.delete();

            Document mesasDoc = new Document(platosMAP);
            Document operacion = new Document("$set", mesasDoc);

            UpdateResult result = this.getBase().getCollection("restaurante").updateOne(requisitosLogin, operacion);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<SeccionesPlatos> obtenerSecciones(){
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

    public void actualizarMesa(Mesa mesa){//corregir
        ArrayList<Mesa>mesas;
        mesas = obtenerMesasArr();

        for (int i = 0; i <mesas.size() ; i++) {
            if (mesas.get(i).getNumMesa()==mesa.getNumMesa()){
                HashMap<String, Object> mesaAtributos = new HashMap<>();
                mesaAtributos.put("numMesa", mesa.getNumMesa());
                mesaAtributos.put("qr", mesa.getQR());
                mesaAtributos.put("ocupada", mesa.isOcupada());

                try {
                    ObjectMapper mapper = new ObjectMapper();
                    File json = new File(".\\src\\com\\company\\mesas.json");

                    mapper.writeValue(json, mesaAtributos);

                    ObjectMapper mapper1 = new ObjectMapper();
                    HashMap mesasMAP = mapper1.readValue(json, HashMap.class);
                    json.delete();

                    Document mesasDoc = new Document("mesas."+i,mesasMAP);
                    Document operacion = new Document("$set", mesasDoc);

                    UpdateResult result = this.getBase().getCollection("restaurante").updateOne(requisitosLogin, operacion);
                }catch (JsonProcessingException e){
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
//db.restaurante.update({id:1},{$set:{"mesas.1":{"numMesa":2,"qr":"C:\Users\Familia Gimenez\Documents\GitHub\ProyectoFinal\APP\qr1.png", "ocupada":false}}})
    }
    public void actualizarMesas(HashSet<Mesa>mesas){
        try {
            ObjectMapper mapper = new ObjectMapper();
            File json = new File(".\\src\\com\\company\\mesas.json");

            HashMap<String, Object> jsonSerializar = new HashMap<>();
            jsonSerializar.put("mesas", mesas);

            mapper.writeValue(json, jsonSerializar);

            ObjectMapper mapper1 = new ObjectMapper();
            HashMap mesasMAP = mapper1.readValue(json, HashMap.class);
            json.delete();

            Document mesasDoc = new Document(mesasMAP);
            Document operacion = new Document("$set", mesasDoc);

            UpdateResult result = this.getBase().getCollection("restaurante").updateOne(requisitosLogin, operacion);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*public void actualizarPlatos(HashSet<Plato>platos){
        try {
            ObjectMapper mapper = new ObjectMapper();
            File json = new File(".\\src\\com\\company\\platos.json");

            HashMap<String, Object> jsonSerializar = new HashMap<>();
            jsonSerializar.put("platos", platosMONGO(platos));

            mapper.writeValue(json, jsonSerializar);

            ObjectMapper mapper1 = new ObjectMapper();
            HashMap platosMAP = mapper1.readValue(json, HashMap.class);
            json.delete();

            Document mesasDoc = new Document(platosMAP);
            Document operacion = new Document("$set", mesasDoc);

            UpdateResult result = this.getBase().getCollection("restaurante").updateOne(requisitosLogin, operacion);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public ArrayList<HashMap<String, Object>> platosMONGO(ArrayList<Plato>platos){
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

    public void actualizarPlato(Plato plato, String nombrePlatoViejo/*esto es porque si el nombre cambia no hay forma de encontrar el plato, ya que decidi no agregarle un id al plato*/){
        ArrayList<Plato> platos = obtenerPlatosArr();

        for (int i = 0; i < platos.size(); i++) {
            if (platos.get(i).getNombre().equals(nombrePlatoViejo)){
                HashMap<String, Object> platoAtributos = new HashMap<>();
                platoAtributos.put("nombre", plato.getNombre());
                platoAtributos.put("precio", plato.getPrecio());
                platoAtributos.put("descripcion",plato.getDescripcion());
                platoAtributos.put("demora", plato.getTiempoDemora());
                platoAtributos.put("imagen", plato.getImg());

                ArrayList<HashMap<String, Object>> agregadosSeccion = new ArrayList<>();

                for (TipoAgregados agregadoSeccion : plato.getAgregados()){

                    ArrayList<Map<String, Object>> agregados = new ArrayList<>();

                    for (Map.Entry<String, Float> agregadoAUX : agregadoSeccion.getAgregados().entrySet()){

                        HashMap<String, Object> agregado = new HashMap<>();
                        agregado.put("nombre", agregadoAUX.getKey());
                        agregado.put("precio", agregadoAUX.getValue());
                        agregados.add(agregado);
                    }
                    HashMap<String,Object> agS = new HashMap<>();
                    agS.put("tipo", agregadoSeccion.getNombre());
                    agS.put("indispensable", agregadoSeccion.getIndispensable());
                    agS.put("agregado", agregados);
                    agregadosSeccion.add(agS);
                }

                platoAtributos.put("agregados", agregadosSeccion);

                try {
                    ObjectMapper mapper = new ObjectMapper();
                    File json = new File(".\\src\\com\\company\\platos.json");

                    mapper.writeValue(json, platoAtributos);

                    ObjectMapper mapper1 = new ObjectMapper();
                    HashMap platosMap = mapper1.readValue(json, HashMap.class);
                    json.delete();

                    Document platosDoc = new Document("platos." + i, platosMap);
                    Document operacion = new Document("$set", platosDoc);

                    UpdateResult result = this.getBase().getCollection("restaurante").updateOne(requisitosLogin, operacion);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }
        }
    }
    public void actualizarPedido(Pedido pedido){
        ArrayList<Pedido> pedidos = obtenerPedidos();

        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getnPedido() == pedido.getnPedido()){
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

                    Document pedidosDoc = new Document("pedidos." + i, pedidoMap);
                    Document operacion = new Document("$set", pedidosDoc);

                    UpdateResult result = this.getBase().getCollection("restaurante").updateOne(requisitosLogin, operacion);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }
        }
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
            //Document document = (Document) iterator.next();

            this.usuario = username;
            this.password = password;

            return true;
        }
        return false;
    }
}
