package com.company;

import javax.swing.*;
import java.io.File;

public class Mesa {
    public int numMesa;
    public boolean Ocupada = false;
    public File QR;

}

    /*public boolean comprobarMesa(int mesa) {
        for (Mesa mesaAux : this.mesas) {
            if (mesaAux.getnMesa() == mesa && !mesaAux.isOcupada()) {
                return false;
            }
        }
        return true;
    }*/

/*public void ocuparMesa(JFrame ventana, JPanel panelFeedBack, JPanel panelIngresar, JButton boton10, JButton boton11, JTextField textField, JLabel labelIngresar, JLabel labelFeedBack, boolean ocupar) {
        cleanPanel(panelIngresar, new Component[]{labelIngresar, textField});

        final HashSet<Mesa> mesasClon = (HashSet<Mesa>) this.mesas.clone();
        final ArrayList<Ocupacion> ocupacionesClon = this.ocupaciones;

        JButton botonIngresar = new JButton("INGRESAR");
        botonIngresar.setBounds(ventana.getWidth()/2-boton10.getWidth()-10, boton10.getY()+30, boton10.getWidth(), boton10.getHeight());
        botonIngresar.setVisible(true);
        panelIngresar.add(botonIngresar);

        JTextField textFieldMesa = new JTextField();
        textFieldMesa.setSize(500,50);
        textFieldMesa.setLocation(ventana.getWidth()/2-textFieldMesa.getWidth()/2, ventana.getHeight()/2);
        textFieldMesa.setVisible(true);
        panelIngresar.add(textFieldMesa);

        JLabel labelMesa = new JLabel();
        labelMesa.setVisible(true);
        labelMesa.setSize(500, 50);
        labelMesa.setText("Ingrese la mesa deseada");
        labelMesa.setLocation(ventana.getWidth()/2-labelMesa.getWidth()/2, ventana.getHeight()/2-50);
        panelIngresar.add(labelMesa);

        panelIngresar.setVisible(true);
        ventana.add(panelIngresar);

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

                ventana.remove(panelIngresar);
                panelIngresar.setVisible(false);

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

/*FUNCION DESOCUPAR*/
       /* boton2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                restaurante.ocuparMesa(ventana, panelFeedBack, panelIngresar, boton10, boton11, textField, labelIngresar, labelFeedBack, false);

                ventana.remove(panelMenu);
                panelMenu.setVisible(false);

                boton11.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ventana.remove(panelFeedBack);
                        panelFeedBack.setVisible(false);
                        panelFeedBack.remove(boton11);
                        ventana.add(panelMenu);
                        panelMenu.setVisible(true);
                    }
                });
            }
        });*/