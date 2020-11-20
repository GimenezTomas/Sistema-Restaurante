package com.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mesa implements Comparable<Mesa>{
    private int numMesa;
    private boolean Ocupada;
    private File QR;
    public static int count = 1;


    public int getNumMesa() {
        return numMesa;
    }
    public void setNumMesa(int numMesa) {
        this.numMesa = numMesa;
    }
    public boolean isOcupada() {
        return Ocupada;
    }
    public void setOcupada(boolean ocupada) {
        Ocupada = ocupada;
    }
    public File getQR() {
        return QR;
    }
    public void setQR(File QR) {
        this.QR = QR;
    }

    public Mesa(int id, File qr, boolean ocupada){
        this.numMesa = id;
        this.Ocupada = ocupada;
        this.QR = qr;
        if (id>count) {
            count = id+1;
        }
    }

    public Mesa(int id, boolean ocupada){
    this.numMesa = id;
    this.Ocupada = ocupada;
    if (id>count) {
        count = id+2;
        }
    }

    public Mesa() {
        this.numMesa = count++;
        this.Ocupada = false;
    }

    public static boolean comprobarMesa(HashSet<Mesa> mesas, int textfield){
        for (Mesa mesa : mesas) {
            if (textfield == mesa.numMesa) {
                return true;
            }
        }
        return false;
    }

    public static void agregarMesas(HashSet<Mesa> mesas,int textfield, int idR) {
        for (int i = 1; i <= textfield; i++) {
            Mesa mesa = new Mesa();
            mesas.add(mesa);
            Peticion.putConJson(mesa, "http://localhost:8888/api/javaAPP/gestionarMesas/agregarMesa/"+idR);
        }
    }

    public static void ocuparMesas(HashSet<Mesa>mesas, int textfield) {
        for (Mesa mesa : mesas) {
            if (textfield == mesa.numMesa && mesa.Ocupada == false) {
                mesa.Ocupada = true;
                JOptionPane.showMessageDialog(null, "La mesa se ocupo correctamente");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "La mesa esta ocupada");
    }

    public static void desocuparMesas(HashSet<Mesa>mesas, int textfield){
        for (Mesa mesa : mesas) {
            if (textfield == mesa.numMesa && mesa.Ocupada == true) {
                mesa.Ocupada = false;
                JOptionPane.showMessageDialog(null, "La mesa se desocupo correctamente");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "La mesa ya esta desocupada");
    }

    public static void generarQr(HashSet<Mesa> mesas, int idR) throws IOException {
        for (Mesa mesa : mesas) {
            if (mesa.QR == null){
                String url = "http://192.168.0.43/php/rest1copia.php?mesa="+mesa.getNumMesa()+"&restaurante="+idR;
                ByteArrayOutputStream out = QRCode.from(String.valueOf(url)).to(ImageType.PNG).stream();
                File imgQr = new File(".\\src\\com\\company\\images\\qr\\"+"Mesa"+mesa.getNumMesa()+".png");
                FileOutputStream fos = new FileOutputStream(imgQr);
                fos.write(out.toByteArray());
                fos.flush();
                mesa.QR = imgQr;
                Peticion.putSinJson("http://localhost:8888/api/javaAPP/gestionarMesas/qr/"+imgQr.getPath()+"/"+idR);
            }
        }

    }

    @Override
    public int compareTo(Mesa o){
        if (o.getNumMesa()>numMesa){
            return -1;
        }else if (o.getNumMesa()<numMesa){
            return 0;
        }else{
            return 1;
        }
    }
}


