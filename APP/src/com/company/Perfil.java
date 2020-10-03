package pruebas;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Perfil {
    private static File FotoPerfil;
    private static int respuesta;
    private static String nombreRest;

    public static String getNombreRest() {
        return nombreRest;
    }
    public static void setNombreRest(String nombreRest) {
        Perfil.nombreRest = nombreRest;
    }
    public static File getFotoPerfil() {
        return FotoPerfil;
    }
    public static void setFotoPerfil(File fotoPerfil) {
        FotoPerfil = fotoPerfil;
    }

    public static class Ventana1 extends JFrame {
        public Ventana1() {
            super("Restaurante");
            setSize(400,300);
            setVisible(true);
        }
    }

public void Chooser(){
        JFileChooser chooser = new JFileChooser(".");
        FileNameExtensionFilter formato = new FileNameExtensionFilter("png", "png");
        chooser.setFileFilter(formato);

        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        respuesta = chooser.showOpenDialog(null);

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            File copiaFt = chooser.getSelectedFile();
                if (copiaFt.isFile() && (copiaFt.getName().endsWith(".png"))){
                FotoPerfil = chooser.getSelectedFile();
                System.out.println(FotoPerfil);
            }
            else{
                JOptionPane.showMessageDialog(null, "Use una foto PNG");
            }
        }
    }


    public static void main(String[] args) throws ClassNotFoundException {
        Perfil r1 = new Perfil();
        Ventana1 v1 = new Ventana1();
        v1.setSize(1350,730);
        v1.setLayout(null);
        v1.setVisible(true);
        v1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel PanelFoto = new JPanel();
        PanelFoto.setName("foto");
        PanelFoto.setSize(1350, 700);
        PanelFoto.setLayout(null);
        PanelFoto.setVisible(true);
        v1.add(PanelFoto);

        JButton boton1 = new JButton("Perfil");
        boton1.setLocation(150, 200);
        boton1.setSize(200, 50);
        boton1.setName("perfil");
        boton1.setVisible(true);
        PanelFoto.add(boton1);

        JButton boton2 = new JButton("Ingrese la foto");
        boton2.setLocation(150, 200);
        boton2.setSize(200, 50);
        boton2.setName("Ft");
        boton2.setVisible(true);

        JTextField texto1 = new JTextField();
        texto1.setLocation(500, 200);
        texto1.setSize(500, 50);
        texto1.setName("name");
        texto1.setVisible(true);

        JButton boton3 = new JButton("Guardar cambios");
        boton3.setLocation(300, 300);
        boton3.setSize(200, 50);
        boton3.setName("Guardar");
        boton3.setVisible(true);


        boton1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PanelFoto.remove(boton1);
                    PanelFoto.add(boton2);
                    PanelFoto.add(texto1);
                    PanelFoto.add(boton3);


                    boton2.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            r1.Chooser();
                        }
                    });

                    boton3.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            nombreRest = texto1.getText();
                            System.out.println(nombreRest);
                            System.out.println("Se guardo el nombre y la foto");
                            PanelFoto.removeAll();
                        }
                    });
                }
        });
    }
}


