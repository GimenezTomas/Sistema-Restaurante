package com.company;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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

    public static void agregarMesas(HashSet<Mesa> mesas,int textfield) {
        for (int i = 1; i <= textfield; i++) {
            Mesa mesa = new Mesa();
            mesas.add(mesa);
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

    public static void generarQr(HashSet<Mesa> mesas) throws IOException {
        for (Mesa mesa : mesas) {
            if (mesa.Ocupada == false){
                String details = "https://www.instagram.com/inaaetchegoyen";
                ByteArrayOutputStream out = QRCode.from(details).to(ImageType.PNG).stream();
                File f = new File("C:\\Users\\iniae\\OneDrive\\Documentos\\GitHub\\ProyectoFinal\\APP\\src\\com\\company\\images\\Qr\\"+"Mesa"+mesa.getNumMesa()+".png");
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(out.toByteArray());
                fos.flush();
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


