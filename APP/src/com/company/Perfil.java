package com.company;

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Perfil {
    public File FotoPerfil;
    public int respuesta;
    public  String nombreRest;

    public void chooser(String extension, String descripcion, String errorMessage, File file){
        JFileChooser chooser = new JFileChooser(".");
        FileNameExtensionFilter formato = new FileNameExtensionFilter("png", "png");

        chooser.setFileFilter(formato);

        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        respuesta = chooser.showOpenDialog(null);

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            File copiaFt = chooser.getSelectedFile();
            if (copiaFt.isFile() && (copiaFt.getName().endsWith("."+extension))){
                file = chooser.getSelectedFile();
            }
            else{
                JOptionPane.showMessageDialog(null, "Use una foto PNG");
            }
        }
    }
}


