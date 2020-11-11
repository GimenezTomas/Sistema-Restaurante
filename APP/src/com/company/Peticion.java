package com.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashSet;

public interface Peticion {
    static HashSet<Mesa> obtenerMesa(){
        HashSet<Mesa> mesas = new HashSet<>();

        CloseableHttpResponse response = null;

        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:8080/api/javaAPP/obtenerMesas/1");
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String resultado = EntityUtils.toString(entity);
            System.out.println(resultado);
            response.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mesas;
    }
}
