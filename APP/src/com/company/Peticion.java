package com.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import org.bson.Document;

import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.*;

public interface Peticion {
    static HashSet<Mesa> obtenerMesas(){
        HashSet<Mesa> mesas = new HashSet<>();

        CloseableHttpResponse response = null;

        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:8080/api/javaAPP/obtenerMesas/1");
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String resultado = EntityUtils.toString(entity);

            ObjectMapper objectMapper = new ObjectMapper();
            HashMap mesasMap = objectMapper.readValue(resultado, HashMap.class);

            Document document = new Document(mesasMap);
            ArrayList<Document> documents = (ArrayList<Document>) document.get("mesas");

            for (int i = 0; i < documents.size() ; i++) {
                String mesaJson = new ObjectMapper().writeValueAsString(documents.get(i));
                HashMap mesaMap = objectMapper.readValue(mesaJson, HashMap.class);
                mesas.add(new Mesa(Integer.parseInt(mesaMap.get("numMesa").toString()), new File(mesaMap.get("qr").toString()), Boolean.parseBoolean(mesaMap.get("ocupada").toString())));
            }

            response.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mesas;
    }

    static int login(String user, String password){
        CloseableHttpResponse response = null;

        int resultado = 0;
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:8080/api/javaAPP/login/"+user+"/"+password);
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            resultado = Integer.parseInt(EntityUtils.toString(entity));

            response.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    static void dataUser(Restaurante restaurante){
        CloseableHttpResponse response = null;

        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:8080/api/dataRest/dataUser/"+restaurante.getId());
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String resultado = EntityUtils.toString(entity);

            ObjectMapper objectMapper = new ObjectMapper();
            HashMap dataMap = objectMapper.readValue(resultado, HashMap.class);
            dataMap = objectMapper.readValue(new ObjectMapper().writeValueAsString(dataMap.get("dataUser")), HashMap.class);
            restaurante.setDireccion(dataMap.get("direccion").toString());
            restaurante.setLogo(new File(dataMap.get("logo").toString()));
            restaurante.setNombre(dataMap.get("nombre").toString());

            response.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static ArrayList<Pedido> obtenerPedidos(int id){
        CloseableHttpResponse response = null;
        ArrayList<Pedido> pedidos = new ArrayList<>();
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:8080/api/javaAPP/obtenerPedidos/"+id);
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String resultado = EntityUtils.toString(entity);

            ObjectMapper objectMapper = new ObjectMapper();
            HashMap pedidosMap = objectMapper.readValue(resultado, HashMap.class);

            Document document = new Document(pedidosMap);
            ArrayList<Document> documents = (ArrayList<Document>) document.get("pedidos");

            for (int i = 0; i < documents.size(); i++) {
                Document pedido = new Document(objectMapper.readValue(new ObjectMapper().writeValueAsString(documents.get(i)), HashMap.class));
                ArrayList<Document> platosDoc = (ArrayList<Document>) pedido.get("platos");

                ArrayList<PlatoPedido>platoPedidoList = new ArrayList<>();
                for (int j = 0; j < platosDoc.size(); j++) {
                    Document plato = new Document(objectMapper.readValue(new ObjectMapper().writeValueAsString(platosDoc.get(j)), HashMap.class));
                    Document agregadosDoc = new Document(objectMapper.readValue(new ObjectMapper().writeValueAsString(plato.get("agregados")), HashMap.class));
                    HashMap<String, Float> agregados = new HashMap<>();
                    for (Map.Entry agregado : agregadosDoc.entrySet()){
                        agregados.put(agregado.getKey().toString(), Float.parseFloat(agregado.getValue().toString()));
                    }
                    platoPedidoList.add(new PlatoPedido(plato.getString("nombre"), Float.parseFloat(plato.get("precio").toString()), agregados, new SimpleDateFormat("dd-mmmm-yyyy HH:mm:ss").parse(plato.getString("fecha")), plato.getBoolean("entregado")));
                }
                pedidos.add(new Pedido(pedido.getInteger("nMesa"), platoPedidoList, pedido.getString("fecha"), pedido.getInteger("nPedido")));
            }

            response.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    static ArrayList<SeccionesPlatos> obtenerSeccionesPlatos(int id){
        CloseableHttpResponse response = null;
        ArrayList<SeccionesPlatos>seccionesPlatos = new ArrayList<>();
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:8080/api/dataRest/seccionesPlatos/"+id);
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String resultado = EntityUtils.toString(entity);

            ObjectMapper objectMapper = new ObjectMapper();
            HashMap seccionesMap = objectMapper.readValue(resultado, HashMap.class);
            //seccionesMap = objectMapper.readValue(new ObjectMapper().writeValueAsString(seccionesMap.get("seccionesPlatos")), HashMap.class);

            Document document = new Document(seccionesMap);
            ArrayList<Document> documents = (ArrayList<Document>) document.get("seccionesPlatos");

            for (int i = 0; i < documents.size(); i++) {
                Document seccion = new Document(objectMapper.readValue(new ObjectMapper().writeValueAsString(documents.get(i)), HashMap.class));
                ArrayList<Document> platosDoc = (ArrayList<Document>) seccion.get("platos");
                ArrayList<Plato>platosList = new ArrayList<>();
                for (int j = 0; j < platosDoc.size(); j++) {
                    Document plato = new Document(objectMapper.readValue(new ObjectMapper().writeValueAsString(platosDoc.get(j)), HashMap.class));
                    ArrayList<Document> tiposAgregadosDoc = (ArrayList<Document>) plato.get("agregados");
                    HashSet<TipoAgregados> tipoAgregados = new HashSet<>();
                    for (int k = 0; k < tiposAgregadosDoc.size(); k++) {
                        Document tipoAgregadoDoc = new Document(objectMapper.readValue(new ObjectMapper().writeValueAsString(tiposAgregadosDoc.get(k)), HashMap.class));
                        Document agregadosDoc = new Document(objectMapper.readValue(new ObjectMapper().writeValueAsString(tipoAgregadoDoc.get("agregados")), HashMap.class));

                        HashMap<String, Float> agregados = new HashMap<>();
                        for (Map.Entry agregado : agregadosDoc.entrySet()){
                            agregados.put(agregado.getKey().toString(), Float.parseFloat(agregado.getValue().toString()));
                        }
                        tipoAgregados.add(new TipoAgregados(tipoAgregadoDoc.getString("nombre"), tipoAgregadoDoc.getBoolean("indispensable"), agregados));
                    }

                    platosList.add(new Plato(plato.getString("nombre"), Float.parseFloat(plato.get("precio").toString()), new File(plato.getString("img")),plato.getString("descripcion"), plato.getString("tiempoDemora"), tipoAgregados));
                }
                seccionesPlatos.add(new SeccionesPlatos(seccion.getString("nombre"), platosList));
            }
            response.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return seccionesPlatos;
    }
}
