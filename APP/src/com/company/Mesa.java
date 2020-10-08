package com.company;

import javax.swing.*;
import java.io.File;
import java.util.HashSet;

public class Mesa {
    public int numMesa;
    public boolean Ocupada;
    public File QR;
    public static int count = 1;

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


/*public void ocuparMesa(JFrame ventana, JPanel panelFeedBack, JPanel panelIngresar, JButton boton10, JButton boton11, JTextField textField, JLabel labelIngresar, JLabel labelFeedBack, boolean ocupar) {
        botonIngresar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int vueltas=0;
                boolean ok=true;
                HashMap<String, Integer> pedido = new HashMap<>();// nombre plato/cantidad

                final int mesa = Integer.parseInt(textFieldMesa.getText());
                for (Mesa mesaAux : mesasClon) {
                    if (mesaAux.getnMesa() == mesa ) {
                        if ((ocupar && !mesaAux.isOcupada()) || !ocupar)
                        {
                            ok = false;
                            mesaAux.setOcupada(ocupar);
                        }
                        break;
                    }
                }

                if(ok && ocupar)
                {
                    labelFeedBack.setText("La mesa esta ocupada o no se encuentra");
                }
                else if(!ok && !ocupar){
                    labelFeedBack.setText("La mesa se desocupo");
                }
                else if(!ok && ocupar){
                    labelFeedBack.setText("La mesa se ocupo");
                    ocupacionesClon.add(new Ocupacion(mesa));
                    HashMap<String, Object> newOcupacion = new HashMap<>();
                    newOcupacion.put("idOcupacion", ocupacionesClon.get(ocupacionesClon.size()-1).getnOcupacion());
                    newOcupacion.put("idMesa", mesa);
                    newOcupacion.put("fecha", ocupacionesClon.get(ocupacionesClon.size()-1).getFecha());
                    System.out.println("entre linea 511");
                }
                else{
                     labelFeedBack.setText("La mesa no existe o esta ocupada");
                 }

                labelFeedBack.setVisible(true);
                boton11.setText("SALIR");
                boton11.setVisible(true);
                panelFeedBack.add(boton11);

                panelFeedBack.setVisible(true);
                ventana.add(panelFeedBack);

            }
        });
    }*/