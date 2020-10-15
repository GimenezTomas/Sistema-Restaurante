package com.company;

import javax.swing.*;
import java.io.File;
import java.util.HashSet;

public class Mesa {
    public int numMesa;
    public boolean Ocupada;
    public File QR;
    public static int count = 1;

    public Mesa(int id, File qr, boolean ocupada){
        this.numMesa = id;
        this.Ocupada = ocupada;
        this.QR = qr;
        count = id+1;
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
            mesa.numMesa = count++;
            mesa.Ocupada = false;
            mesas.add(mesa);
        }
        System.out.println(mesas.size());
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

}