package com.company;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Restaurante {
    private HashSet<Mesa> mesas = new HashSet<>();
    private ArrayList<SeccionesPlatos> seccionesPlatos = new ArrayList<>();
    private ArrayList<Pedido> pedidos = new ArrayList<>();
    private String nombre;
    private File logo;
    private String direccion;
    private int id;
    public static HashMap<String, Font> fuentes = new HashMap<>();

    //GETTERS && SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<SeccionesPlatos> getSeccionesPlatos() {
        return seccionesPlatos;
    }

    public void setSeccionesPlatos(ArrayList<SeccionesPlatos> seccionesPlatos) {
        this.seccionesPlatos = seccionesPlatos;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public File getLogo() {
        return logo;
    }
    public void setLogo(File logo) {
        this.logo = logo;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }
    public void setPedidos(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
    public HashSet<Mesa> getMesas() {
        return mesas;
    }
    public void setMesas(HashSet<Mesa> mesas) {
        this.mesas = mesas;
    }

    //CONSTRUCTOR

    public static boolean esNumero(String cadena) {

        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }

    public File chooser(String extension, String descripcion, String errorMessage){
        JFileChooser chooser = new JFileChooser(".");
        FileNameExtensionFilter formato = new FileNameExtensionFilter(descripcion, extension);

        int respuesta;

        chooser.setFileFilter(formato);

        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        respuesta = chooser.showOpenDialog(null);

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            File copiaFt = chooser.getSelectedFile();
            if (copiaFt.isFile() && (copiaFt.getName().endsWith(".png"))){
                return chooser.getSelectedFile();
            }
            else{
                JOptionPane.showMessageDialog(null, errorMessage);
                return null;
            }
        }else{
            return null;
        }
    }

    public void clearPanel(JPanel panelIngresar, Component comp[]){
        HashSet<String> nombres = new HashSet<>();

        for (Component component: comp){
            nombres.add(component.getName());
        }

        for (Component component : panelIngresar.getComponents()){
            if (nombres.contains(component.getName())){
                nombres.remove(component.getName());
            }else{
                panelIngresar.remove(component);
            }
        }
    }
    public void entregarPedido(JFrame ventana) {
        this.pedidos = Peticion.obtenerPedidos(this.id);

        final HashSet<PlatoPedido> platosChecked = new HashSet<>();

        JPanel panelIngresar = new JPanel();
        panelIngresar.setSize(1400, 700);
        panelIngresar.setLayout(null);
        panelIngresar.setVisible(false);
        panelIngresar.setName("ingresar");

        JButton boton = new JButton("AGREGAR");
        boton.setVisible(true);
        boton.setName("boton");
        panelIngresar.add(boton);

        JButton botonOut = new JButton("SALIR");
        botonOut.setVisible(true);
        botonOut.setName("botonOUT");
        botonOut.setBounds(ventana.getWidth()/2-250, 550, 200, 50);
        panelIngresar.add(botonOut);

        JLabel labelIngresar = new JLabel("Selecciona el pedido a entregar");
        labelIngresar.setSize(200, 50);
        labelIngresar.setName("labelIngresar");
        labelIngresar.setLocation(ventana.getWidth()/2-labelIngresar.getWidth()/2, 20);
        labelIngresar.setVisible(true);

        JComboBox menuPedidos = new JComboBox();
        menuPedidos.setName("comboboxMenu");
        menuPedidos.setBounds(ventana.getWidth()/2-200, 120,300,100);

        boolean ceroPlatos=true;
        for(Pedido pedidoAux : this.pedidos){
            for (PlatoPedido plato : pedidoAux.getPlatos()) {
                if (!plato.isEntregado()) {
                    menuPedidos.addItem("PEDIDO N°" + pedidoAux.getnPedido());
                    ceroPlatos = false;
                    break;
                }
            }
        }

        if (ceroPlatos){
            JLabel label = new JLabel("No hay mesas con platos sin entregar");
            label.setBounds(panelIngresar.getWidth()/2 - 350, 100, 800, 50);
            label.setFont(fuentes.get("Times New Roman"));
            label.setVisible(true);
            panelIngresar.add(label);

            panelIngresar.setVisible(true);
            labelIngresar.setVisible(false);
        }else{
            panelIngresar.setVisible(true);

            menuPedidos.setVisible(true);

            JButton buttonOK = new JButton(new ImageIcon(".\\src\\com\\company\\images\\check.png"));
            buttonOK.setBounds(menuPedidos.getX()+menuPedidos.getWidth(), menuPedidos.getY(), 100, 100);
            buttonOK.setVisible(true);
            panelIngresar.add(buttonOK);

            panelIngresar.add(labelIngresar);
            panelIngresar.add(menuPedidos);

            buttonOK.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ventana.getContentPane().removeAll();
                    panelIngresar.setVisible(false);
                    JLabel labelP = new JLabel("Platos");
                    labelP.setFont(fuentes.get("Garamond"));
                    labelP.setBounds(ventana.getWidth()/2-450, buttonOK.getY()+buttonOK.getHeight()+30, 200, 100);
                    labelP.setVisible(true);
                    labelP.setName("labelPlatos");
                    panelIngresar.add(labelP);

                    JLabel labelEntregado = new JLabel("Entregado");
                    labelEntregado.setFont(fuentes.get("Garamond"));
                    labelEntregado.setBounds(ventana.getWidth()/2+350, buttonOK.getY()+buttonOK.getHeight()+30, 100, 50);
                    labelEntregado.setVisible(true);
                    labelEntregado.setName("labelEntregado");
                    panelIngresar.add(labelEntregado);

                    Component componentes[]={boton, botonOut, buttonOK, labelIngresar, labelEntregado, labelP, menuPedidos};
                    clearPanel(panelIngresar, componentes);

                    for (Pedido pedido : pedidos) {
                        if (pedido.getnPedido() == Integer.parseInt(menuPedidos.getSelectedItem().toString().substring(9))) {

                            int vueltas = 1;

                            for (PlatoPedido plato: pedido.getPlatos()){
                                if (!plato.isEntregado()){
                                    String agregadosString = "";
                                    for (String agregado : plato.getAgregados().keySet()){
                                        agregadosString = agregadosString + " " + agregado;
                                    }
                                    JLabel labelPlatos = new JLabel(plato.getNombre() + ", agregados:"+agregadosString);
                                    labelPlatos.setSize(800, 35);
                                    labelPlatos.setLocation(ventana.getWidth()/2-labelPlatos.getWidth()/2-50, Math.round((labelP.getY() + labelP.getHeight())+((labelP.getHeight()/2)*(vueltas-1))));

                                    Border border = BorderFactory.createLineBorder(Color.black, 1);

                                    labelPlatos.setBorder(border);
                                    labelPlatos.setBackground(Color.white);
                                    labelPlatos.setOpaque(true);
                                    labelPlatos.setVisible(true);
                                    panelIngresar.add(labelPlatos);

                                    JCheckBox checkBox = new JCheckBox();
                                    checkBox.setBounds(labelPlatos.getWidth()+labelPlatos.getX()+25, labelPlatos.getY(), 50, 35);
                                    checkBox.setVisible(true);
                                    panelIngresar.add(checkBox);

                                    checkBox.addMouseListener(new MouseAdapter() {
                                        @Override
                                        public void mouseClicked(MouseEvent e) {
                                            if (checkBox.isSelected()){
                                                platosChecked.add(plato);
                                                System.out.println("se agrego");
                                            }else{
                                                platosChecked.remove(plato);
                                                System.out.println("se borro");
                                            }
                                        }
                                    });

                                    vueltas++;
                                }
                            }
                            break;
                        }
                    }
                    botonOut.setBounds(ventana.getWidth()/2-250, panelIngresar.getComponent(panelIngresar.getComponents().length-1).getY()+panelIngresar.getComponent(panelIngresar.getComponents().length-1).getHeight() + 50, 200, 50);
                    boton.setBounds(ventana.getWidth()/2-50, panelIngresar.getComponent(panelIngresar.getComponents().length-1).getY()+panelIngresar.getComponent(panelIngresar.getComponents().length-1).getHeight() + 50, 200, 50);
                    //boton.setBounds(700, 50, 200, 50);
                    panelIngresar.setVisible(true);

                    //scrollbar
                    panelIngresar.setPreferredSize(new Dimension(1350, botonOut.getY()+botonOut.getHeight()+30));
                    JScrollPane scrollPane = new JScrollPane(panelIngresar, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    ventana.remove(panelIngresar);
                    ventana.add(scrollPane);
                }
            });
        }

        ventana.add(panelIngresar);

        botonOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.getContentPane().removeAll();

                panelMenu(ventana);
            }
        });
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Pedido pedido = null;
                platosChecked.forEach(platoPedido -> System.out.println(platoPedido.getNombre()));
                for (PlatoPedido platoPedido : platosChecked){
                    for (Pedido pedidosAux : pedidos) {
                        if (pedidosAux.getnPedido() == Integer.parseInt(menuPedidos.getSelectedItem().toString().substring(9))) {
                            pedido = pedidosAux;
                            for (PlatoPedido platoFromPedido : pedidosAux.getPlatos()){
                                if (platoFromPedido==platoPedido){
                                    platoFromPedido.setEntregado(true);
                                }
                            }
                            for (PlatoPedido platoFromPedido : pedidosAux.getPlatos()){
                                System.out.println(platoFromPedido.isEntregado()+" plato is entregado");
                            }
                        }
                    }
                }
                Peticion.putConJson(parseDatePedido(pedido), "http://localhost:8888/api/javaAPP/gestionarPedidos/actualizarPedido/"+pedido.getnPedido()+"/"+id);

                JOptionPane.showMessageDialog(null, "Se entregaron los pedidos correctamente!!!");
                ventana.getContentPane().removeAll();
                panelMenu(ventana);
            }
        });
    }

    public Object parseDatePedido (Pedido pedido1){
        ArrayList<String> fechas = new ArrayList<>();
        Document pedidoObj = new Document();

        for(PlatoPedido platoPedido : pedido1.getPlatos()){
            fechas.add(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(platoPedido.getFecha()));
            //System.out.println(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(platoPedido.getFecha()));
            //System.out.println(fechas.size());
            System.out.println(platoPedido.getFecha());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Document pedido = null;
        try {
            pedido = new Document( objectMapper.readValue(new ObjectMapper().writeValueAsString(pedido1), HashMap.class));

            ArrayList<Document> platosDoc = (ArrayList<Document>) pedido.get("platos");

            ArrayList<Document> platos = new ArrayList<>();
            for (int j = 0; j < platosDoc.size(); j++) {
                Document plato = new Document(objectMapper.readValue(new ObjectMapper().writeValueAsString(platosDoc.get(j)), HashMap.class));

                Document agregadosDoc = new Document(objectMapper.readValue(new ObjectMapper().writeValueAsString(plato.get("agregados")), HashMap.class));
                ArrayList<HashMap<String, Object>>agregadosList = new ArrayList<>();
                for (Map.Entry agregado : agregadosDoc.entrySet()){
                    HashMap<String, Object> agregados = new HashMap<>();
                    agregados.put("nombre", agregado.getKey().toString());
                    agregados.put("precio", Float.parseFloat(agregado.getValue().toString()));
                    agregadosList.add(agregados);
                }
                plato.put("nombrePlato", plato.get("nombre").toString());
                plato.remove("nombre");
                plato.put("agregados", agregadosList);
                plato.put("fecha", fechas.get(j));
                platos.add(plato);
            }
            pedidoObj.append("fecha", pedido.getString("fecha"));
            pedidoObj.append("nMesa", pedido.getInteger("nMesa"));
            pedidoObj.append("nPedido", pedido.getInteger("nPedido"));
            pedidoObj.append("platos", platos);
            pedidoObj.append("abierto", pedido.getBoolean("abierto"));
            System.out.println("pedidoObj: "+pedidoObj);
        } catch (JsonProcessingException e) {
                e.printStackTrace();
        }
        return pedidoObj;
    }

    public void proximoPedido(JFrame ventana) {
        this.pedidos = Peticion.obtenerPedidos(this.id);

        JPanel panel = new JPanel();
        panel.setSize(1350, 700);
        panel.setLayout(null);
        panel.setVisible(false);
        ventana.add(panel);

        JLabel labelExplicacion = new JLabel("PROXIMOS PLATOS");
        labelExplicacion.setName("labelExplicacion");
        labelExplicacion.setVisible(true);
        labelExplicacion.setFont(fuentes.get("Times New Roman"));
        labelExplicacion.setBounds(panel.getWidth()/2-225, 50, 500, 100);
        panel.add(labelExplicacion);

        JButton botonOut = new JButton("SALIR");
        botonOut.setVisible(true);
        botonOut.setName("botonOUT");
        botonOut.setBounds(ventana.getWidth()/2-250, 550, 200, 50);
        panel.add(botonOut);

        botonOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.getContentPane().removeAll();
                panelMenu(ventana);
            }
        });
        final ArrayList<PlatoPedido> platosP = new ArrayList<>();

        for (Pedido pedido : pedidos){
            for (PlatoPedido platoPedido: pedido.getPlatos()){
                if (!platoPedido.isEntregado()) {
                    platosP.add(platoPedido);
                }
            }
        }
        if (platosP.size()==0){
            labelExplicacion.setText("No hay platos pendientes");
            panel.setVisible(true);
        }else {

            Collections.sort(platosP);
            JLabel labelP = new JLabel("Platos");
            labelP.setFont(fuentes.get("Garamond"));
            labelP.setBounds(ventana.getWidth() / 2 - 450, labelExplicacion.getY() + labelExplicacion.getHeight() + 30, 200, 100);
            labelP.setVisible(true);
            labelP.setName("labelPlatos");
            panel.add(labelP);

            JLabel labelEntregado = new JLabel("Mesa");
            labelEntregado.setFont(fuentes.get("Garamond"));
            labelEntregado.setBounds(ventana.getWidth() / 2 + 350, labelExplicacion.getY() + labelExplicacion.getHeight() + 30, 100, 50);
            labelEntregado.setVisible(true);
            labelEntregado.setName("labelEntregado");
            panel.add(labelEntregado);

            int vueltas = 0;
            for (PlatoPedido platoOrdenado : platosP) {
                vueltas++;
                for (Pedido pedido : pedidos) {
                    for (PlatoPedido platoPedido : pedido.getPlatos()) {
                        if (platoOrdenado == platoPedido) {
                            String agregadosString = "";
                            for (String agregado : platoOrdenado.getAgregados().keySet()) {
                                agregadosString = agregadosString + " " + agregado;
                            }
                            JLabel labelPlatos = new JLabel(platoOrdenado.getNombre() + ", agregados:" + agregadosString);
                            labelPlatos.setSize(750, 35);
                            labelPlatos.setLocation(ventana.getWidth() / 2 - labelPlatos.getWidth() / 2 - 75, Math.round((labelP.getY() + labelP.getHeight()) + ((labelP.getHeight() / 2) * (vueltas-1))));
                            Border border = BorderFactory.createLineBorder(Color.black, 1);

                            labelPlatos.setBorder(border);
                            labelPlatos.setBackground(Color.white);
                            labelPlatos.setOpaque(true);
                            labelPlatos.setVisible(true);
                            panel.add(labelPlatos);

                            JLabel labelPedidos = new JLabel(pedido.getnMesa() + "");
                            labelPedidos.setSize(35, 35);
                            labelPedidos.setLocation(labelPlatos.getX() + labelPlatos.getWidth() + 65, labelPlatos.getY());
                            labelPedidos.setVisible(true);
                            panel.add(labelPedidos);

                            break;
                        }
                    }
                }
            }
            botonOut.setBounds(ventana.getWidth()/2-250, panel.getComponent(panel.getComponents().length-1).getY()+panel.getComponent(panel.getComponents().length-1).getHeight() + 50, 200, 50);
            //botonOut.setBounds(700, 50, 200, 50);
            panel.setVisible(true);
            //scrollbar
                    panel.setPreferredSize(new Dimension(1350, botonOut.getY()+botonOut.getHeight()+30));
                    JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    ventana.remove(panel);
                    ventana.add(scrollPane);
        }
    }

    public boolean agregarPlato(String nombre, String precio, String imagen, String descripcion, String tiempoDemora, String opcionesSec){
        boolean ok = true;
        for (SeccionesPlatos seccionPlato : seccionesPlatos){
            for (Plato plato: seccionPlato.getPlatos()) {
                if (plato.getNombre().equals(nombre)){
                    ok = false;
                    break;
                }
            }
        }

        if (ok) {
            for (SeccionesPlatos seccionPlato : seccionesPlatos){
                if (seccionPlato.getNombre().equals(opcionesSec)) {
                    Plato plato = new Plato(nombre, Float.parseFloat(precio), new File(imagen), descripcion, tiempoDemora);
                    seccionPlato.getPlatos().add(plato);
                    Peticion.putConJson(seccionPlato, "http://localhost:8888/api/javaAPP/gestionarMenu/actualizarPlatos/"+seccionPlato.getNombre()+"/"+id);
                }
            }
            //mongo.actualizarSeccionesPlatos(seccionesPlatos);
        }
        else{
            JOptionPane.showMessageDialog(null, "El plato ya existe");
        }

        return ok;
    }

    public boolean agregarPlato(Plato newPlato, String opcionesSec){
        boolean ok = true;
        for (SeccionesPlatos seccionPlato : seccionesPlatos){
            for (Plato plato: seccionPlato.getPlatos()) {
                if (plato.getNombre().equals(newPlato.getNombre())){
                    ok = false;
                    for(TipoAgregados agregado: newPlato.getAgregados()){
                        if(!plato.getAgregados().contains(agregado)){
                            plato.getAgregados().add(agregado);
                        }
                    }
                    break;
                }
            }
        }

        if (ok) {
            for (SeccionesPlatos seccionPlato : seccionesPlatos){
                if (seccionPlato.getNombre().equals(opcionesSec)) {
                    seccionPlato.getPlatos().add(newPlato);
                    Peticion.putConJson(seccionPlato, "http://localhost:8888/api/javaAPP/gestionarMenu/actualizarPlatos/"+seccionPlato.getNombre()+"/"+id);
                }
            }
            //mongo.actualizarSeccionesPlatos(seccionesPlatos);
            //this.mongo.actualizarPlatos(platos);
        }

        return ok;
    }

    public void clickEditAgregados(JFrame frameAgregados, JLabel labelAgregados2, Plato plato){
        JPanel panelAgregados = new JPanel();
        panelAgregados.setName("menu");
        panelAgregados.setSize(frameAgregados.getSize());
        panelAgregados.setLayout(null);
        panelAgregados.setVisible(true);

        frameAgregados.getContentPane().removeAll();

        panelAgregados.add(labelAgregados2);

        JLabel labelExplicacion = new JLabel("<html><body> Si quieres cambiar el nombre de la seccion, reescribelo en el input y<br> preciona el lapiz. En caso de querer borrarlo, presiona la cruz <br></body></html>");
        labelExplicacion.setName("labelExplicacion");
        labelExplicacion.setVisible(true);
        labelExplicacion.setFont(fuentes.get("Garamond"));
        labelExplicacion.setBounds(panelAgregados.getWidth()/2-225, labelAgregados2.getY()+labelAgregados2.getHeight()+30, 450, 50);
        panelAgregados.add(labelExplicacion);

        JButton botonSalir = new JButton("Salir");
        botonSalir.setBounds(panelAgregados.getWidth()/2-125, 550, 100, 50);
        botonSalir.setVisible(true);

        int vueltas = 1;
        for (SeccionesPlatos seccionPlato : seccionesPlatos){
            for(Plato platoActual: seccionPlato.getPlatos()){
                if (platoActual.getNombre().equals(plato.getNombre())){
                    if (plato.getAgregados().size()>platoActual.getAgregados().size()){
                        agregarPlato(plato, seccionPlato.getNombre());
                    }
                    /*if (platoActual.getAgregados().size()==0){
                        labelExplicacion.setText("No hay secciones que editar"+plato.getAgregados().size());
                        break;
                    }*/
                    //else {
                        for(TipoAgregados agregados:platoActual.getAgregados()) {
                            JTextField textFieldSeccion = new JTextField();
                            textFieldSeccion.setSize(200, 50);
                            if (vueltas == 1) {
                                textFieldSeccion.setLocation(25, labelExplicacion.getY() + labelExplicacion.getHeight() + 15);
                            } else {
                                textFieldSeccion.setLocation(25, Math.round((labelExplicacion.getY() + labelExplicacion.getHeight() + 15) * (vueltas / 1.8f)) + 85);
                            }
                            textFieldSeccion.setVisible(true);
                            textFieldSeccion.setName("textFieldSeccion" + vueltas);
                            textFieldSeccion.setText(agregados.getNombre());
                            panelAgregados.add(textFieldSeccion);

                            JComboBox opciones = new JComboBox();
                            opciones.setName("comboBoxOpciones");
                            opciones.setBounds(textFieldSeccion.getWidth() + textFieldSeccion.getX() + 1, textFieldSeccion.getY(), 100, 50);
                            if (agregados.getIndispensable()) {
                                opciones.addItem("SI");
                                opciones.addItem("NO");
                            } else {
                                opciones.addItem("NO");
                                opciones.addItem("SI");
                            }
                            opciones.setVisible(true);
                            panelAgregados.add(opciones);

                            JButton botonEdit = new JButton(new ImageIcon(".\\src\\com\\company\\images\\pencil.png"));
                            botonEdit.setBounds(opciones.getX() + opciones.getWidth() + 1, textFieldSeccion.getY(), 50, 50);
                            botonEdit.setVisible(true);
                            panelAgregados.add(botonEdit);

                            JButton botonDelete = new JButton(new ImageIcon(".\\src\\com\\company\\images\\delete.png"));
                            botonDelete.setBounds(botonEdit.getX() + botonEdit.getWidth() + 1, botonEdit.getY(), 50, 50);
                            botonDelete.setVisible(true);
                            panelAgregados.add(botonDelete);

                            vueltas++;
                            panelAgregados.add(textFieldSeccion);

                            botonEdit.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    int coincidencias=0;
                                    textFieldSeccion.setText(textFieldSeccion.getText().substring(0,1).toUpperCase()+textFieldSeccion.getText().substring(1));

                                    for (TipoAgregados ag:platoActual.getAgregados()){
                                        if (ag.getNombre().equals(textFieldSeccion.getText())){
                                            coincidencias++;
                                            break;
                                        }
                                    }
                                    if (coincidencias<2){
                                        if(opciones.getSelectedItem().equals("SI")){
                                            agregados.setIndispensable(true);
                                        }
                                        else{
                                            agregados.setIndispensable(false);
                                        }
                                        JOptionPane.showMessageDialog(null, "se cambio correctamente");
                                        agregados.setNombre(textFieldSeccion.getText());
                                    }
                                    else{
                                        JOptionPane.showMessageDialog(null, "El nombre ya esta usado");
                                    }
                                }
                            });
                            botonDelete.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    int confirmD = JOptionPane.showConfirmDialog(null, "¿Estas seguro que quieres borrarlo? se borraran todos los agregados que le correspondan");
                                    if(confirmD == JOptionPane.YES_OPTION){
                                        platoActual.getAgregados().remove(agregados);
                                        textFieldSeccion.setVisible(false);
                                        botonDelete.setVisible(false);
                                        botonEdit.setVisible(false);
                                        opciones.setVisible(false);
                                        frameAgregados.getContentPane().removeAll();
                                        clickEditAgregados(frameAgregados, labelAgregados2, plato);
                                    }
                                    //platos.remove(plato);
                                }
                            });
                        }
                    //}
                    break;
                }
            }
        }

        botonSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frameAgregados.getContentPane().removeAll();
                panelAgregados.setPreferredSize(null);

                crearMenuAgregados(frameAgregados, plato);
            }
        });
        botonSalir.setLocation(panelAgregados.getWidth()/2-botonSalir.getWidth()/2, panelAgregados.getComponent(panelAgregados.getComponents().length-1).getY()+panelAgregados.getComponent(panelAgregados.getComponents().length-1).getHeight()+50);
        panelAgregados.add(botonSalir);
        panelAgregados.setPreferredSize(new Dimension(530, botonSalir.getHeight()+botonSalir.getY()+50));
        JScrollPane scrollBar = new JScrollPane(panelAgregados, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//C:\Users\Familia Gimenez\Documents\Tomás\IACeh\tareas\tarea2 wps\Captura.png
        panelAgregados.setVisible(true);
        frameAgregados.add(scrollBar);
    }

    public void clickEditAgregado(JFrame frameAgregados, JLabel labelAgregados2, Plato plato) {
        JPanel panelAgregados = new JPanel();
        panelAgregados.setName("menu");
        panelAgregados.setSize(frameAgregados.getSize());
        panelAgregados.setLayout(null);
        panelAgregados.setVisible(true);

        frameAgregados.getContentPane().removeAll();

        panelAgregados.add(labelAgregados2);

        JButton botonSalir = new JButton("Salir");
        botonSalir.setBounds(panelAgregados.getWidth()/2-125, 550, 100, 50);
        botonSalir.setVisible(true);

        JLabel labelExplicacion = new JLabel("<html><body> Si quieres cambiar el nombre del agregado, reescribelo en el input y<br> preciona el lapiz. En caso de querer borrarlo, presiona la cruz <br></body></html>");
        labelExplicacion.setName("labelExplicacion");
        labelExplicacion.setVisible(true);
        labelExplicacion.setFont(fuentes.get("Garamond"));
        labelExplicacion.setBounds(panelAgregados.getWidth() / 2 - 225, labelAgregados2.getY() + labelAgregados2.getHeight() + 30, 450, 50);
        panelAgregados.add(labelExplicacion);

        int vueltas = 1;

        JComboBox opciones = new JComboBox();
        opciones.setName("comboBoxOpciones");
        opciones.setBounds(labelExplicacion.getWidth() / 2 - 125, labelExplicacion.getY() + labelExplicacion.getHeight() + 20, 200, 50);
        for (SeccionesPlatos seccionPlato: seccionesPlatos){
            for (Plato platoActual : seccionPlato.getPlatos()) {
                if (platoActual.getNombre().equals(plato.getNombre())) {
                    if (plato.getAgregados().size() > platoActual.getAgregados().size()) {
                        agregarPlato(plato, seccionPlato.getNombre());
                    }
                    if (platoActual.getAgregados().size() == 0) {
                        labelExplicacion.setText("No hay agregados que editar" + plato.getAgregados().size());
                        break;
                    } else {
                        for (TipoAgregados agregados : platoActual.getAgregados()) {
                            opciones.addItem(agregados.getNombre());
                        }
                        panelAgregados.add(opciones);

                        JButton buttonElegir = new JButton(new ImageIcon(".\\src\\com\\company\\images\\check.png"));
                        buttonElegir.setBounds(opciones.getX() + opciones.getWidth() + 1, opciones.getY(), 50, 50);
                        buttonElegir.setVisible(true);
                        panelAgregados.add(buttonElegir);

                        buttonElegir.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                for (TipoAgregados agregados : platoActual.getAgregados()) {
                                    if (agregados.getNombre().equals(opciones.getSelectedItem())) {
                                        if (agregados.getAgregados().size()==0){
                                            labelExplicacion.setText("No hay agregados que editar" + plato.getAgregados().size());
                                        }else{
                                            int vueltas = 1;
                                            for (Map.Entry<String, Float> agregado : agregados.getAgregados().entrySet()) {
                                                JTextField textFieldNombre = new JTextField();
                                                textFieldNombre.setSize(120, 50);
                                                if (vueltas == 1) {
                                                    textFieldNombre.setLocation(80, opciones.getY() + opciones.getHeight() + 15);
                                                } else {
                                                    textFieldNombre.setLocation(80, Math.round((opciones.getY() + opciones.getHeight() + 15) * (vueltas / 1.8f)) + 85);
                                                }
                                                textFieldNombre.setVisible(true);
                                                textFieldNombre.setName("textFieldSeccion" + vueltas);
                                                textFieldNombre.setText(agregado.getKey());
                                                panelAgregados.add(textFieldNombre);

                                                JTextField textFieldPrecio = new JTextField();
                                                textFieldPrecio.setSize(120, 50);
                                                textFieldPrecio.setLocation(textFieldNombre.getX() + textFieldNombre.getWidth(), textFieldNombre.getY());
                                                textFieldPrecio.setVisible(true);
                                                textFieldPrecio.setName("textFieldSeccion" + vueltas);
                                                textFieldPrecio.setText(agregado.getValue()+"");
                                                panelAgregados.add(textFieldPrecio);

                                                JButton botonEdit = new JButton(new ImageIcon(".\\src\\com\\company\\images\\pencil.png"));
                                                botonEdit.setBounds(textFieldPrecio.getX() + textFieldPrecio.getWidth() + 1, textFieldPrecio.getY(), 50, 50);
                                                botonEdit.setVisible(true);
                                                panelAgregados.add(botonEdit);

                                                JButton botonDelete = new JButton(new ImageIcon(".\\src\\com\\company\\images\\delete.png"));
                                                botonDelete.setBounds(botonEdit.getX() + botonEdit.getWidth() + 1, textFieldPrecio.getY(), 50, 50);
                                                botonDelete.setVisible(true);
                                                panelAgregados.add(botonDelete);

                                                botonEdit.addMouseListener(new MouseAdapter() {
                                                    @Override
                                                    public void mouseClicked(MouseEvent e) {
                                                        if (textFieldNombre.getText().equals("") || textFieldPrecio.equals("")) {
                                                            JOptionPane.showMessageDialog(null, "formulario incompleto");
                                                        } else {
                                                            textFieldNombre.setText(textFieldNombre.getText().substring(0,1).toUpperCase()+textFieldNombre.getText().substring(1));
                                                            try {
                                                                float precio = Float.parseFloat(textFieldPrecio.getText());
                                                                if (precio<0){
                                                                    JOptionPane.showMessageDialog(null, "El precio debe ser mayor a 0");
                                                                }
                                                                int coincidencias=0;
                                                                for (Map.Entry<String, Float> ag : agregados.getAgregados().entrySet()){
                                                                    if (ag.getKey().equals(textFieldNombre.getText())){
                                                                        coincidencias++;
                                                                    }
                                                                }
                                                                if (coincidencias>=2){
                                                                    JOptionPane.showMessageDialog(null, "El nombre ya esta usado");
                                                                }
                                                                else{
                                                                    JOptionPane.showMessageDialog(null, "Se cambio correctamente");
                                                                    agregados.getAgregados().put(textFieldNombre.getText(), precio);
                                                                    agregados.getAgregados().remove(agregado.getKey());
                                                                }
                                                            } catch (NumberFormatException ex) {
                                                                JOptionPane.showMessageDialog(null, "La sintaxis del precio es incorrecta, ej:80.15");
                                                            }
                                                        }
                                                    }
                                                });
                                                botonDelete.addMouseListener(new MouseAdapter() {
                                                    @Override
                                                    public void mouseClicked(MouseEvent e) {
                                                        int confirmD = JOptionPane.showConfirmDialog(null, "¿Estas seguro que quieres borrarlo? ");
                                                        if (confirmD == JOptionPane.YES_OPTION) {
                                                            agregados.getAgregados().remove(agregado.getKey());
                                                            textFieldNombre.setVisible(false);
                                                            textFieldPrecio.setVisible(false);
                                                            botonDelete.setVisible(false);
                                                            botonEdit.setVisible(false);
                                                            frameAgregados.getContentPane().removeAll();
                                                            clickEditAgregado(frameAgregados, labelAgregados2, plato);
                                                        }
                                                    }
                                                });
                                                vueltas++;
                                            }
                                        }
                                        break;
                                    }
                                }
                                botonSalir.setLocation(panelAgregados.getWidth() / 2 - botonSalir.getWidth() / 2, panelAgregados.getComponent(panelAgregados.getComponents().length - 1).getY() + panelAgregados.getComponent(panelAgregados.getComponents().length - 1).getHeight() + 50);
                            }
                        });
                        break;
                    }
                }
            }
        }

        botonSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frameAgregados.getContentPane().removeAll();
                panelAgregados.setPreferredSize(new Dimension(500, 730));

                crearMenuAgregados(frameAgregados, plato);
            }
        });
        frameAgregados.getContentPane().removeAll();
        botonSalir.setLocation(panelAgregados.getWidth() / 2 - botonSalir.getWidth() / 2, panelAgregados.getComponent(panelAgregados.getComponents().length - 1).getY() + panelAgregados.getComponent(panelAgregados.getComponents().length - 1).getHeight() + 50);
        panelAgregados.add(botonSalir);
        panelAgregados.setPreferredSize(new Dimension(530, botonSalir.getHeight() + botonSalir.getY() + 50));
        JScrollPane scrollBar = new JScrollPane(panelAgregados, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//C:\Users\Familia Gimenez\Documents\Tomás\IACeh\tareas\tarea2 wps\Captura.png
        frameAgregados.add(scrollBar);
    }

    public void crearMenuAgregados( JFrame frameAgregados, Plato plato){
        frameAgregados.getContentPane().removeAll();

        JPanel panelAgregados = new JPanel();
        panelAgregados.setName("menu");
        panelAgregados.setSize(frameAgregados.getSize());
        panelAgregados.setLayout(null);
        panelAgregados.setVisible(false);

        JLabel labelAgregados2 = new JLabel("AGREGADOS");
        labelAgregados2.setName("labelAgregados2");
        labelAgregados2.setVisible(true);
        labelAgregados2.setFont(fuentes.get("Times New Roman"));
        labelAgregados2.setBounds(panelAgregados.getWidth()/2-150, 20, 300, 50);
        panelAgregados.add(labelAgregados2);

        JButton botonTipoAgregado = new JButton("Añadir tipo de agregado");
        botonTipoAgregado.setBounds(panelAgregados.getWidth()/2-100, labelAgregados2.getHeight()+labelAgregados2.getY()+120, 200, 50);
        botonTipoAgregado.setVisible(true);
        panelAgregados.add(botonTipoAgregado);

        JButton botonAgregado = new JButton("Añadir agregado");
        botonAgregado.setBounds(panelAgregados.getWidth()/2-100, botonTipoAgregado.getHeight()+botonTipoAgregado.getY()+40, 200, 50);
        botonAgregado.setVisible(true);
        panelAgregados.add(botonAgregado);

        JButton botonEditTipoAgregado = new JButton("Editar tipo de agregado");
        botonEditTipoAgregado.setBounds(panelAgregados.getWidth()/2-100, botonAgregado.getHeight()+botonAgregado.getY()+40, 200, 50);
        botonEditTipoAgregado.setVisible(true);
        panelAgregados.add(botonEditTipoAgregado);

        JButton botonEditAgregado = new JButton("Editar agregado");
        botonEditAgregado.setBounds(panelAgregados.getWidth()/2-100, botonEditTipoAgregado.getHeight()+botonEditTipoAgregado.getY()+40, 200, 50);
        botonEditAgregado.setVisible(true);
        panelAgregados.add(botonEditAgregado);

        JButton botonSalir = new JButton("Salir");
        botonSalir.setBounds(panelAgregados.getWidth()/2-125, 550, 100, 50);
        botonSalir.setVisible(true);

        panelAgregados.setVisible(true);
        frameAgregados.add(panelAgregados);

        botonSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                crearMenuAgregados(frameAgregados, plato);
            }
        });
        botonTipoAgregado.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panelAgregados.removeAll();

                panelAgregados.add(labelAgregados2);

                JLabel labelNuevaSeccion = new JLabel("Ingresa la nueva seccion de agregados que desees");
                labelNuevaSeccion.setName("labelNuevaSeccion");
                labelNuevaSeccion.setVisible(true);
                labelNuevaSeccion.setFont(fuentes.get("Garamond"));
                labelNuevaSeccion.setBounds(panelAgregados.getWidth()/2-175, 200, 350, 50);
                panelAgregados.add(labelNuevaSeccion);

                JTextField textFieldNuevaSeccion = new JTextField();
                textFieldNuevaSeccion.setSize(450, 40);
                textFieldNuevaSeccion.setLocation(panelAgregados.getWidth() / 2 - 230, labelNuevaSeccion.getY() + labelNuevaSeccion.getHeight() + 2);
                textFieldNuevaSeccion.setVisible(true);
                textFieldNuevaSeccion.setName("textFieldNuevaSeccion");
                panelAgregados.add(textFieldNuevaSeccion);

                JLabel labelImportancia = new JLabel("¿Esta nueva seccion debe ser completada de forma obligatoria?");
                labelImportancia.setName("labelImportancia");
                labelImportancia.setVisible(true);
                labelImportancia.setFont(fuentes.get("Garamond"));
                labelImportancia.setBounds(panelAgregados.getWidth()/2-200, textFieldNuevaSeccion.getY() + textFieldNuevaSeccion.getHeight() + 30, 400, 50);
                panelAgregados.add(labelImportancia);


                JComboBox comboBoxImportancia = new JComboBox();
                comboBoxImportancia.setName("comboboxFechas");
                comboBoxImportancia.setBounds(panelAgregados.getWidth()/2-100, labelImportancia.getY() + labelImportancia.getHeight() + 2, 200, 20);
                comboBoxImportancia.addItem("NO");
                comboBoxImportancia.addItem("SI");
                comboBoxImportancia.setVisible(true);
                panelAgregados.add(comboBoxImportancia);

                JButton botonAgSeccion = new JButton("Agregar");
                botonAgSeccion.setBounds(panelAgregados.getWidth()/2+25, 550, 100, 50);
                botonAgSeccion.setVisible(true);
                panelAgregados.add(botonAgSeccion);

                panelAgregados.add(botonSalir);

                botonAgSeccion.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(textFieldNuevaSeccion.getText().equals("")){
                            JOptionPane.showMessageDialog(null, "Formulario incompleto");
                        }
                        else{
                            boolean ok=true, check=true;
                            textFieldNuevaSeccion.setText(textFieldNuevaSeccion.getText().substring(0,1).toUpperCase()+textFieldNuevaSeccion.getText().substring(1));

                            for(TipoAgregados agregados: plato.getAgregados()){
                                if (agregados.getNombre().equals(textFieldNuevaSeccion.getText())){
                                    check=false;
                                }
                            }
                            if (!check){
                                JOptionPane.showMessageDialog(null, "Esta seccion ya existe");
                            }
                            else{
                                if(comboBoxImportancia.getSelectedItem().equals("NO")){
                                    ok=false;
                                }
                                plato.getAgregados().add(new TipoAgregados(textFieldNuevaSeccion.getText(), ok));
                                crearMenuAgregados(frameAgregados, plato);
                            }
                        }
                    }
                });
            }
        });
        botonAgregado.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (plato.getAgregados().size()==0){
                    JOptionPane.showMessageDialog(null, "No hay tipos a los que agregarle un agregado");
                }else {

                    panelAgregados.removeAll();

                    panelAgregados.add(labelAgregados2);

                    JLabel labelNuevaSeccion = new JLabel("Ingresa la seccion de agregados que desees");
                    labelNuevaSeccion.setName("labelNuevaSeccion");
                    labelNuevaSeccion.setVisible(true);
                    labelNuevaSeccion.setFont(fuentes.get("Garamond"));
                    labelNuevaSeccion.setBounds(panelAgregados.getWidth() / 2 - 125, 200, 250, 50);
                    panelAgregados.add(labelNuevaSeccion);

                    JComboBox comboBoxImportancia = new JComboBox();
                    comboBoxImportancia.setName("comboboxFechas");
                    comboBoxImportancia.setBounds(panelAgregados.getWidth() / 2 - 100, labelNuevaSeccion.getY() + labelNuevaSeccion.getHeight() + 2, 200, 20);
                    comboBoxImportancia.setVisible(true);

                    for (TipoAgregados agregados : plato.getAgregados()) {
                        comboBoxImportancia.addItem(agregados.getNombre());
                    }

                    panelAgregados.add(comboBoxImportancia);

                    JLabel labelNuevoAgregado = new JLabel("Ingresa el nuevo agregado, ej: bolognesa");
                    labelNuevoAgregado.setName("labelNuevoAgregado");
                    labelNuevoAgregado.setVisible(true);
                    labelNuevoAgregado.setFont(fuentes.get("Garamond"));
                    labelNuevoAgregado.setBounds(panelAgregados.getWidth() / 2 - 150, comboBoxImportancia.getY() + comboBoxImportancia.getHeight() + 20, 350, 50);
                    panelAgregados.add(labelNuevoAgregado);

                    JTextField textFieldNuevoAgregado = new JTextField();
                    textFieldNuevoAgregado.setSize(450, 40);
                    textFieldNuevoAgregado.setLocation(panelAgregados.getWidth() / 2 - 230, labelNuevoAgregado.getY() + labelNuevoAgregado.getHeight() + 2);
                    textFieldNuevoAgregado.setVisible(true);
                    textFieldNuevoAgregado.setName("textFieldNuevoAgregado");
                    panelAgregados.add(textFieldNuevoAgregado);

                    JLabel labelNuevoAgregadoPrecio = new JLabel("Ingresa el precio del agregado, ej: 145.15");
                    labelNuevoAgregadoPrecio.setName("labelNuevoAgregadoPrecio");
                    labelNuevoAgregadoPrecio.setVisible(true);
                    labelNuevoAgregadoPrecio.setFont(fuentes.get("Garamond"));
                    labelNuevoAgregadoPrecio.setBounds(panelAgregados.getWidth() / 2 - 150, textFieldNuevoAgregado.getY() + textFieldNuevoAgregado.getHeight() + 20, 350, 50);
                    panelAgregados.add(labelNuevoAgregadoPrecio);

                    JTextField textFieldAgregadoPrecio = new JTextField();
                    textFieldAgregadoPrecio.setSize(450, 40);
                    textFieldAgregadoPrecio.setLocation(panelAgregados.getWidth() / 2 - 230, labelNuevoAgregadoPrecio.getY() + labelNuevoAgregadoPrecio.getHeight() + 2);
                    textFieldAgregadoPrecio.setVisible(true);
                    textFieldAgregadoPrecio.setName("textFieldAgregadoPrecio");
                    panelAgregados.add(textFieldAgregadoPrecio);

                    panelAgregados.add(botonSalir);

                    JButton botonAgSeccion = new JButton("Agregar");
                    botonAgSeccion.setBounds(panelAgregados.getWidth() / 2 + 25, 550, 100, 50);
                    botonAgSeccion.setVisible(true);
                    panelAgregados.add(botonAgSeccion);

                    botonAgSeccion.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (!comboBoxImportancia.getSelectedItem().equals("") && !textFieldNuevoAgregado.getText().equals("") && !textFieldNuevoAgregado.getText().equals("")) {
                                try {
                                    boolean ok = true;
                                    float precio = Float.parseFloat(textFieldAgregadoPrecio.getText());
                                    if (precio < 0) {
                                        JOptionPane.showMessageDialog(null, "El precio debe ser mayor a 0");
                                    } else {
                                        textFieldNuevoAgregado.setText(textFieldNuevoAgregado.getText().substring(0,1).toUpperCase()+textFieldNuevoAgregado.getText().substring(1));
                                        for (TipoAgregados agregados : plato.getAgregados()) {
                                            for (String agregado : agregados.getAgregados().keySet()) {
                                                if (agregado.equals(textFieldNuevoAgregado.getText())) {
                                                    ok = false;
                                                    JOptionPane.showMessageDialog(null, "Este agregado ya existe");
                                                    break;
                                                }
                                            }
                                        }

                                        if (ok) {
                                            for (TipoAgregados agregados : plato.getAgregados()) {
                                                if (agregados.getNombre().equals(comboBoxImportancia.getSelectedItem())) {
                                                    agregados.getAgregados().put(textFieldNuevoAgregado.getText(), Float.parseFloat(textFieldAgregadoPrecio.getText()));
                                                }
                                            }
                                            panelAgregados.removeAll();
                                            crearMenuAgregados(frameAgregados, plato);
                                        }
                                    }
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(null, "La sintaxis del precio es incorrecta, ej: 185.15");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "El formulario esta incompleto");
                            }
                        }
                    });
                }
            }
        });

        botonEditTipoAgregado.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /*usar scrollbar, poner el nombre, precio, al lado un simbolo boton editar y por ultimo un boton delete*/
                if (plato.getAgregados().size()==0){
                    JOptionPane.showMessageDialog(null,"No hay tipos que editar");
                }else{
                    clickEditAgregados(frameAgregados, labelAgregados2, plato );
                }
            }
        });

        botonEditAgregado.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickEditAgregado(frameAgregados, labelAgregados2, plato);
            }
        });
    }
    public void editarPlato(JFrame ventana){
        JPanel panel = new JPanel();
        panel.setName("panelGR");
        panel.setSize(1350, 700);
        panel.setLayout(null);
        panel.setVisible(false);

        ventana.getContentPane().removeAll();

        JLabel labelPlatos = new JLabel("PLATOS");
        labelPlatos.setName("labelPlatos");
        labelPlatos.setVisible(true);
        labelPlatos.setFont(fuentes.get("Times New Roman"));
        labelPlatos.setBounds(panel.getWidth()/2-100, 20, 300, 50);
        panel.add(labelPlatos);

        JLabel labelExplicacion = new JLabel("<html><body> Si quieres cambiar el nombre de la seccion, reescribelo en el input y<br> preciona el lapiz. En caso de querer borrarlo, presiona la cruz <br></body></html>");
        labelExplicacion.setName("labelExplicacion");
        labelExplicacion.setVisible(true);
        labelExplicacion.setFont(fuentes.get("Garamond"));
        labelExplicacion.setBounds(panel.getWidth()/2-225, labelPlatos.getY()+labelPlatos.getHeight()+30, 450, 50);
        panel.add(labelExplicacion);

        JButton botonSalir = new JButton("Salir");
        botonSalir.setBounds(panel.getWidth()/2-125, 550, 100, 50);
        botonSalir.setVisible(true);
        panel.add(botonSalir);

        botonSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.getContentPane().removeAll();
                gestionarRestaurante(ventana);
            }
        });

        int platosSize = 0;

        for (SeccionesPlatos seccionPlato : seccionesPlatos){
            platosSize+=seccionPlato.getPlatos().size();
        }
        if (platosSize==0){
            labelExplicacion.setText("No hay platos que editar");
            botonSalir.setLocation(panel.getWidth() / 2 - botonSalir.getWidth() / 2, 500);
            panel.setVisible(true);
            ventana.add(panel);
        }else{
            JComboBox opcionesSec = new JComboBox();
            opcionesSec.setBounds(ventana.getWidth()/2-175, labelExplicacion.getY() + labelExplicacion.getHeight() + 50, 300, 50);
            seccionesPlatos.forEach(seccion -> opcionesSec.addItem(seccion.getNombre()));
            opcionesSec.setVisible(true);
            panel.add(opcionesSec);

            JButton buttonOK = new JButton(new ImageIcon(".\\src\\com\\company\\images\\check.png"));
            buttonOK.setBounds(opcionesSec.getX()+opcionesSec.getWidth(), opcionesSec.getY(), 50, 50);
            buttonOK.setName("buttonOK");
            buttonOK.setVisible(true);
            panel.add(buttonOK);

            buttonOK.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (SeccionesPlatos seccionPlato : seccionesPlatos){
                        if (seccionPlato.getNombre().equals(opcionesSec.getSelectedItem().toString())){
                            int vueltas=1;
                            for (Plato platosAux: seccionPlato.getPlatos()){
                                JTextField jtxtNombre = new JTextField();
                                jtxtNombre.setSize(250, 50);
                                jtxtNombre.setLocation(85, Math.round((opcionesSec.getY() + opcionesSec.getHeight())+((120)*(vueltas-1)))+20);
                                jtxtNombre.setText(platosAux.getNombre());
                                jtxtNombre.setVisible(true);
                                panel.add(jtxtNombre);

                                JTextField jtxtPrecio = new JTextField();
                                jtxtPrecio.setSize(80, 50);
                                jtxtPrecio.setLocation(jtxtNombre.getX()+jtxtNombre.getWidth(), jtxtNombre.getY());
                                jtxtPrecio.setText(platosAux.getPrecio()+"");
                                jtxtPrecio.setVisible(true);
                                panel.add(jtxtPrecio);

                                JTextField jtxtDescripcion = new JTextField();
                                jtxtDescripcion.setSize(400, 50);
                                jtxtDescripcion.setLocation(jtxtPrecio.getX()+jtxtPrecio.getWidth(), jtxtNombre.getY());
                                jtxtDescripcion.setText(platosAux.getDescripcion());
                                jtxtDescripcion.setVisible(true);
                                panel.add(jtxtDescripcion);

                                JTextField jtxtDemora= new JTextField();
                                jtxtDemora.setSize(80, 50);
                                jtxtDemora.setLocation(jtxtDescripcion.getX()+jtxtDescripcion.getWidth(), jtxtDescripcion.getY());
                                jtxtDemora.setText(platosAux.getDemora());
                                jtxtDemora.setVisible(true);
                                panel.add(jtxtDemora);

                                JTextField jtxtImg = new JTextField();
                                jtxtImg.setSize(120, 50);
                                jtxtImg.setLocation(jtxtDemora.getX()+jtxtDemora.getWidth(), jtxtDemora.getY());
                                jtxtImg.setText(platosAux.getImagen().getPath());
                                jtxtImg.setVisible(true);
                                panel.add(jtxtImg);

                                JButton botonAg = new JButton("Edit agregados");
                                botonAg.setBounds(jtxtImg.getX() + jtxtImg.getWidth() + 1, jtxtDemora.getY(), 150, 50);
                                botonAg.setVisible(true);
                                panel.add(botonAg);

                                JButton botonEdit = new JButton(new ImageIcon(".\\src\\com\\company\\images\\pencil.png"));
                                botonEdit.setBounds(botonAg.getX() + botonAg.getWidth() + 1, botonAg.getY(), 50, 50);
                                botonEdit.setVisible(true);
                                panel.add(botonEdit);

                                JButton botonDelete = new JButton(new ImageIcon(".\\src\\com\\company\\images\\delete.png"));
                                botonDelete.setBounds(botonEdit.getX() + botonEdit.getWidth() + 1, botonAg.getY(), 50, 50);
                                botonDelete.setVisible(true);
                                panel.add(botonDelete);
                                vueltas++;

                                botonEdit.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        if (jtxtDemora.getText().equals("") || jtxtDescripcion.getText().equals("") || jtxtImg.getText().equals("") || jtxtNombre.getText().equals("") || jtxtPrecio.getText().equals("")) {
                                            JOptionPane.showMessageDialog(null, "El formulario esta incompleto");
                                        }else if (!new File(jtxtImg.getText()).exists()){
                                            JOptionPane.showMessageDialog(null, "El archivo no existe o no es png, ( ej: C:\\Users\\Restaurante\\Pancho.png )");
                                        }
                                        else {
                                            try {
                                                if(Float.parseFloat(jtxtPrecio.getText())<0){
                                                    JOptionPane.showMessageDialog(null, "El precio no puede ser menor a 0");
                                                }else{
                                                    int coicidencias=0;
                                                    for (SeccionesPlatos seccionesPlatos : seccionesPlatos){
                                                        if (seccionPlato.getNombre().equals(opcionesSec.getSelectedItem().toString())){
                                                            for (Plato platoAux: seccionesPlatos.getPlatos()){
                                                                if (platoAux.getNombre().equals(jtxtNombre.getText())){
                                                                    coicidencias++;
                                                                }
                                                            }
                                                        }
                                                    }

                                                    if (coicidencias>=2){
                                                        JOptionPane.showMessageDialog(null, "El nombre del plato ya esta usado");
                                                    }else{
                                                        jtxtNombre.setText(jtxtNombre.getText().substring(0,1).toUpperCase()+jtxtNombre.getText().substring(1));
                                                        String nombreViejo = platosAux.getNombre();
                                                        platosAux.setNombre(jtxtNombre.getText());
                                                        platosAux.setDescripcion(jtxtDescripcion.getText());
                                                        platosAux.setDemora(jtxtDemora.getText());
                                                        platosAux.setPrecio(Float.parseFloat(jtxtPrecio.getText()));
                                                        platosAux.setImagen(new File(jtxtImg.getText()));
                                                        JOptionPane.showMessageDialog(null, "Se cambio correctamente");
                                                        //mongo.actualizarPlato(platosAux, nombreViejo);
                                                        //mongo.actualizarSeccionesPlatos(seccionesPlatos);
                                                        Peticion.putConJson(seccionPlato, "http://localhost:8888/api/javaAPP/gestionarMenu/actualizarPlatos/"+seccionPlato.getNombre()+"/"+id);
                                                        //Peticion.putConJson(platosAux, "http://localhost:8888/api/javaAPP/gestionarMenu/editarNombreSeccion/"+opcionesSec.getSelectedItem().toString()+"/"+id);
                                                    }
                                                }
                                            } catch (NumberFormatException ex) {
                                                JOptionPane.showMessageDialog(null, "La sintaxis del precio es incorrecta, ej: 180.15");
                                            }
                                        }
                                    }
                                });
                                botonDelete.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        int confirmD = JOptionPane.showConfirmDialog(null, "¿Estas seguro que quieres borrarlo? ");
                                        String seccionName = "";
                                        if (confirmD == JOptionPane.YES_OPTION) {
                                            for (SeccionesPlatos seccionPlato : seccionesPlatos){
                                                if (seccionPlato.getNombre().equals(opcionesSec.getSelectedItem().toString())){
                                                    seccionPlato.getPlatos().remove(platosAux);
                                                    seccionName = seccionPlato.getNombre();
                                                }
                                            }
                                            Peticion.putConJson(seccionPlato, "http://localhost:8888/api/javaAPP/gestionarMenu/actualizarPlatos/"+seccionPlato.getNombre()+"/"+id);
                                            //Peticion.putConJson(platosAux, "http://localhost:8888/api/javaAPP/gestionarMenu/editarNombreSeccion/"+seccionName+"/"+id);
                                            //mongo.actualizarSeccionesPlatos(seccionesPlatos);
                                            //mongo.actualizarPlatos(platos);
                                            jtxtDemora.setVisible(false);
                                            jtxtDescripcion.setVisible(false);
                                            jtxtImg.setVisible(false);
                                            jtxtNombre.setVisible(false);
                                            jtxtPrecio.setVisible(false);
                                            botonDelete.setVisible(false);
                                            botonEdit.setVisible(false);
                                            botonAg.setVisible(false);
                                            ventana.getContentPane().removeAll();
                                            editarPlato(ventana);
                                        }
                                    }
                                });
                                botonAg.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        JFrame frameAgregados = new JFrame("AGREGADOS");
                                        frameAgregados.setSize(500, 730);
                                        frameAgregados.setVisible(true);

                                        JPanel panelAgregados = new JPanel();
                                        panelAgregados.setName("menu");
                                        panelAgregados.setSize(frameAgregados.getSize());
                                        panelAgregados.setLayout(null);
                                        panelAgregados.setVisible(true);

                                        frameAgregados.add(panelAgregados);
                                        crearMenuAgregados(frameAgregados,platosAux);
                                    }
                                });
                            }
                        }
                    }
                    botonSalir.setLocation(panel.getWidth() / 2 - botonSalir.getWidth() / 2, panel.getComponent(panel.getComponents().length - 1).getY() + panel.getComponent(panel.getComponents().length - 1).getHeight() + 50);
                    panel.setPreferredSize(new Dimension(1350, botonSalir.getHeight() + botonSalir.getY() + 50));
                    JScrollPane scrollBar = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    panel.setVisible(true);
                    ventana.getContentPane().removeAll();
                    ventana.add(scrollBar);
                }
            });
            botonSalir.setLocation(panel.getWidth() / 2 - botonSalir.getWidth() / 2, panel.getComponent(panel.getComponents().length - 1).getY() + panel.getComponent(panel.getComponents().length - 1).getHeight() + 50);
            panel.setPreferredSize(new Dimension(1350, botonSalir.getHeight() + botonSalir.getY() + 50));
            JScrollPane scrollBar = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            panel.setVisible(true);
            ventana.getContentPane().removeAll();

            ventana.add(scrollBar);
        }
    }

    public void editarSeccionPlato(JFrame ventana){
        JPanel panel = new JPanel();
        panel.setName("menu");
        panel.setSize(ventana.getSize());
        panel.setLayout(null);
        panel.setVisible(true);

        ventana.getContentPane().removeAll();

        JLabel labelSeccion = new JLabel("SECCIONES PLATO");
        labelSeccion.setName("labelAgregados2");
        labelSeccion.setVisible(true);
        labelSeccion.setFont(fuentes.get("Times New Roman"));
        labelSeccion.setBounds(panel.getWidth()/2-225, 20, 450, 50);

        panel.add(labelSeccion);

        JLabel labelExplicacion = new JLabel("<html><body> Si quieres cambiar el nombre de la seccion, reescribelo en el input y<br> preciona el lapiz. En caso de querer borrarlo, presiona la cruz <br></body></html>");
        labelExplicacion.setName("labelExplicacion");
        labelExplicacion.setVisible(true);
        labelExplicacion.setFont(fuentes.get("Garamond"));
        labelExplicacion.setBounds(panel.getWidth()/2-225, labelSeccion.getY()+labelSeccion.getHeight()+30, 450, 50);
        panel.add(labelExplicacion);

        JButton botonSalir = new JButton("Salir");
        botonSalir.setBounds(panel.getWidth()/2-125, 550, 100, 50);
        botonSalir.setVisible(true);

        int vueltas = 1;

        for(SeccionesPlatos seccionActual: seccionesPlatos){
            JTextField textFieldSeccion = new JTextField();
            textFieldSeccion.setSize(600, 50);
            if (vueltas == 1) {
                textFieldSeccion.setLocation(ventana.getWidth()/2-302, labelExplicacion.getY() + labelExplicacion.getHeight() + 15);
            } else {
                textFieldSeccion.setLocation(ventana.getWidth()/2-302, Math.round((labelExplicacion.getY() + labelExplicacion.getHeight() + 15) * (vueltas / 1.8f)) + 85);
            }
            textFieldSeccion.setVisible(true);
            textFieldSeccion.setName("textFieldSeccion" + vueltas);
            textFieldSeccion.setText(seccionActual.getNombre());
            panel.add(textFieldSeccion);

            JButton botonEdit = new JButton(new ImageIcon(".\\src\\com\\company\\images\\pencil.png"));
            botonEdit.setBounds(textFieldSeccion.getX() + textFieldSeccion.getWidth() + 1, textFieldSeccion.getY(), 50, 50);
            botonEdit.setVisible(true);
            panel.add(botonEdit);

            JButton botonDelete = new JButton(new ImageIcon(".\\src\\com\\company\\images\\delete.png"));
            botonDelete.setBounds(botonEdit.getX() + botonEdit.getWidth() + 1, botonEdit.getY(), 50, 50);
            botonDelete.setVisible(true);
            panel.add(botonDelete);

            vueltas++;
            panel.add(textFieldSeccion);

            botonEdit.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    boolean ok = true;

                    if (textFieldSeccion.getText().equals("")){
                        JOptionPane.showMessageDialog(null, "Input vacio");
                    }else {
                        String nombre = textFieldSeccion.getText().toUpperCase().charAt(0)+textFieldSeccion.getText().toLowerCase().substring(1);
                        for (SeccionesPlatos ag : seccionesPlatos) {
                            if (ag.getNombre().equals(nombre)) {
                                ok=false;
                            }
                        }
                        if (ok) {
                            JOptionPane.showMessageDialog(null, "se cambio correctamente");
                            Peticion.putSinJson("http://localhost:8888/api/javaAPP/gestionarMenu/editarNombreSeccion/"+seccionActual.getNombre()+"/"+nombre+"/"+id);
                            seccionActual.setNombre(nombre);
                        } else {
                            JOptionPane.showMessageDialog(null, "El nombre ya esta usado");
                        }
                    }
                }
            });
            botonDelete.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int confirmD = JOptionPane.showConfirmDialog(null, "¿Estas seguro que quieres borrarlo? se borraran todos los platos que le correspondan");
                    if(confirmD == JOptionPane.YES_OPTION){
                        Peticion.deleteSinJson("http://localhost:8888/api/javaAPP/gestionarMenu/borrarSeccion/"+seccionActual.getNombre()+"/"+id);
                        seccionesPlatos.remove(seccionActual);
                        //mongo.actualizarSeccionesPlatos(seccionesPlatos);
                        textFieldSeccion.setVisible(false);
                        botonDelete.setVisible(false);
                        botonEdit.setVisible(false);
                        textFieldSeccion.setVisible(false);
                        ventana.getContentPane().removeAll();
                        editarSeccionPlato(ventana);
                    }
                }
            });
                    }
        botonSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.getContentPane().removeAll();
                panel.setPreferredSize(null);

                gestionarRestaurante(ventana);
            }
        });
        botonSalir.setLocation(panel.getWidth()/2-botonSalir.getWidth()/2, panel.getComponent(panel.getComponents().length-1).getY()+panel.getComponent(panel.getComponents().length-1).getHeight()+50);
        panel.add(botonSalir);
        panel.setPreferredSize(new Dimension(1350, botonSalir.getHeight()+botonSalir.getY()+50));
        JScrollPane scrollBar = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.setVisible(true);
        ventana.add(scrollBar);
    }

    public void  agregarSeccionPlato(JFrame ventana){
        JPanel panel = new JPanel();
        panel.setName("panelGR");
        panel.setSize(1350, 700);
        panel.setLayout(null);
        panel.setVisible(true);
        ventana.add(panel);

        JLabel labelTitulo = new JLabel("¡INGRESÁ LA NUEVA SECCIÓN!");
        labelTitulo.setBounds(ventana.getWidth() / 2 - 300, 100, 700, 50);
        labelTitulo.setVisible(true);
        labelTitulo.setFont(fuentes.get("Times New Roman"));
        panel.add(labelTitulo);

        JLabel labelNombre = new JLabel("nombre de la sección");
        labelNombre.setBounds(ventana.getWidth() / 2 - 250, 300, 200, 15);
        labelNombre.setVisible(true);
        panel.add(labelNombre);

        JTextField textField = new JTextField();
        textField.setBounds(labelNombre.getX(), labelNombre.getY()+labelNombre.getHeight()+5, 500, 50);
        textField.setVisible(true);
        panel.add(textField);

        JButton botonSalir = new JButton("Salir");
        botonSalir.setBounds(panel.getWidth()/2-125, 490, 100, 50);
        botonSalir.setVisible(true);
        panel.add(botonSalir);

        JButton botonAgregar = new JButton("Agregar");
        botonAgregar.setBounds(panel.getWidth()/2+25, 490, 100, 50);
        botonAgregar.setVisible(true);
        panel.add(botonAgregar);

        botonSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.remove(panel);

                gestionarRestaurante(ventana);
            }
        });

        botonAgregar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(textField.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Formulario incompleto o nombre ya existente");
                }else{
                    String nombre = textField.getText().toUpperCase().charAt(0)+textField.getText().toLowerCase().substring(1);
                    boolean ok = true;
                    for (SeccionesPlatos seccionPlato : seccionesPlatos){
                        if (seccionPlato.getNombre().equals(nombre)){
                            JOptionPane.showMessageDialog(null, "Formulario incompleto o nombre ya usado");
                            ok = false;
                            break;
                        }
                    }
                    if (ok){
                        seccionesPlatos.add(new SeccionesPlatos(nombre));
                        Peticion.putSinJson("http://localhost:8888/api/javaAPP/gestionarMenu/agregarSeccion/"+nombre+"/"+id);
                        //mongo.actualizarSeccionesPlatos(seccionesPlatos);
                        JOptionPane.showMessageDialog(null,"Se agrego correctamente!");
                    }
                }
            }
        });
    }

    public void gestionarRestaurante(JFrame ventana) {
        JPanel panel = new JPanel();
        panel.setName("panelGR");
        panel.setSize(1350, 700);
        panel.setLayout(null);
        panel.setVisible(false);
        ventana.add(panel);

        JButton boton1 = new JButton("AÑADIR PLATO");
        boton1.setBounds(ventana.getWidth() / 2 + 50, ventana.getHeight() / 2 - 100, 200, 50);
        boton1.setVisible(true);
        panel.add(boton1);

        JButton boton2 = new JButton("EDITAR PLATO");
        boton2.setBounds(ventana.getWidth() / 2 + 50, ventana.getHeight() / 2 - 25, 200, 50);
        boton2.setVisible(true);
        panel.add(boton2);

        JButton boton3 = new JButton("BORRAR MENU");
        boton3.setBounds(ventana.getWidth() / 2 + 50, ventana.getHeight() / 2 + 50, 200, 50);
        boton3.setVisible(true);
        panel.add(boton3);

        JButton boton4 = new JButton("AGREGAR SECCIONES");
        boton4.setBounds(ventana.getWidth() / 2 - 250, ventana.getHeight() / 2 - 100, 200, 50);
        boton4.setVisible(true);
        panel.add(boton4);

        JButton boton5 = new JButton("EDITAR SECCIONES");
        boton5.setBounds(ventana.getWidth() / 2 - 250, ventana.getHeight() / 2 - 25, 200, 50);
        boton5.setVisible(true);
        panel.add(boton5);

        JButton salir = new JButton("Salir");
        salir.setBounds(ventana.getWidth() / 2 - 250, ventana.getHeight() / 2 + 50, 200, 50);
        salir.setVisible(true);
        panel.add(salir);

        panel.setVisible(true);

        salir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.remove(panel);
                panelMenu(ventana);
            }
        });
        final boolean[] abierto = {true};

        boton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (seccionesPlatos.size() > 0) {
                    panel.removeAll();

                    final int[] platosSize = {0};

                    JLabel labelTitulo = new JLabel("¡INGRESAR EL NUEVO PLATO!");
                    labelTitulo.setBounds(ventana.getWidth() / 2 - 100, 20, 200, 15);
                    labelTitulo.setVisible(true);
                    panel.add(labelTitulo);

                    JLabel labelNombre = new JLabel("Nombre:");
                    labelNombre.setBounds(ventana.getWidth() / 2 - 350, labelTitulo.getY() + labelTitulo.getHeight() + 10, 200, 30);
                    labelNombre.setVisible(true);
                    labelNombre.setName("labelName");
                    panel.add(labelNombre);

                    JTextField textFieldName = new JTextField();
                    textFieldName.setSize(700, 40);
                    textFieldName.setLocation(ventana.getWidth() / 2 - 350, labelNombre.getY() + labelNombre.getHeight() + 2);
                    textFieldName.setVisible(true);
                    textFieldName.setName("textFieldName");
                    panel.add(textFieldName);

                    JLabel labelDescripcion = new JLabel("Descripcion:");
                    labelDescripcion.setBounds(ventana.getWidth() / 2 - 350, textFieldName.getY() + textFieldName.getHeight() + 20, 200, 30);
                    labelDescripcion.setVisible(true);
                    labelDescripcion.setName("labelDescripcion");
                    panel.add(labelDescripcion);

                    JTextField textFieldDescripcion = new JTextField();
                    textFieldDescripcion.setSize(700, 40);
                    textFieldDescripcion.setLocation(ventana.getWidth() / 2 - 350, labelDescripcion.getY() + labelDescripcion.getHeight() + 2);
                    textFieldDescripcion.setVisible(true);
                    textFieldDescripcion.setName("textFieldDescripcion");
                    panel.add(textFieldDescripcion);
                    JLabel labelTiempoDemora = new JLabel("Tiempo de demora(aprox): (ej: 25min)");
                    labelTiempoDemora.setBounds(ventana.getWidth() / 2 - 350, textFieldDescripcion.getY() + textFieldDescripcion.getHeight() + 20, 500, 30);
                    labelTiempoDemora.setVisible(true);
                    labelTiempoDemora.setName("labelTiempoDemora");
                    panel.add(labelTiempoDemora);

                    JTextField textFieldTiempoDemora = new JTextField();
                    textFieldTiempoDemora.setSize(700, 40);
                    textFieldTiempoDemora.setLocation(ventana.getWidth() / 2 - 350, labelTiempoDemora.getY() + labelTiempoDemora.getHeight() + 2);
                    textFieldTiempoDemora.setVisible(true);
                    textFieldTiempoDemora.setName("textFieldTiempoDemora");
                    panel.add(textFieldTiempoDemora);

                    JLabel labelPrecio = new JLabel("Precio: ( ej: 1956.65 )");
                    labelPrecio.setBounds(ventana.getWidth() / 2 - 350, textFieldTiempoDemora.getY() + textFieldTiempoDemora.getHeight() + 20, 200, 30);
                    labelPrecio.setVisible(true);
                    labelPrecio.setName("labelPrecio");
                    panel.add(labelPrecio);

                    JTextField textFieldPrecio = new JTextField();
                    textFieldPrecio.setSize(700, 40);
                    textFieldPrecio.setLocation(ventana.getWidth() / 2 - 350, labelPrecio.getY() + labelPrecio.getHeight() + 2);
                    textFieldPrecio.setVisible(true);
                    textFieldPrecio.setName("textFieldPrecio");
                    panel.add(textFieldPrecio);

                    JLabel labelImagen = new JLabel("Imagen: las imagenes solo pueden ser png, toca el boton o pega la ruta en el input");
                    labelImagen.setBounds(ventana.getWidth() / 2 - 350, textFieldPrecio.getY() + textFieldPrecio.getHeight() + 20, 700, 30);
                    labelImagen.setVisible(true);
                    labelImagen.setName("labelImagen");
                    panel.add(labelImagen);

                    JTextField textFieldImagen = new JTextField();
                    textFieldImagen.setSize(550, 40);
                    textFieldImagen.setLocation(ventana.getWidth() / 2 - 350, labelImagen.getY() + labelImagen.getHeight() + 2);
                    textFieldImagen.setVisible(true);
                    textFieldImagen.setName("textFieldImagen");
                    panel.add(textFieldImagen);

                    JButton botonImagen = new JButton("Ingresa la imagen");
                    botonImagen.setBounds(textFieldImagen.getX() + textFieldImagen.getWidth(), textFieldImagen.getY(), 150, 40);
                    botonImagen.setVisible(true);
                    panel.add(botonImagen);

                    JLabel labelAgregados = new JLabel("El plato va a tener agregados?");
                    labelAgregados.setBounds(ventana.getWidth() / 2 - 350, textFieldImagen.getY() + textFieldImagen.getHeight() + 20, 150, 30);
                    labelAgregados.setVisible(true);
                    labelAgregados.setName("labelAgregados");
                    panel.add(labelAgregados);

                    JComboBox opciones = new JComboBox();
                    opciones.setName("comboboxFechas");
                    opciones.setBounds(ventana.getWidth() / 2 - 350, labelAgregados.getY() + labelAgregados.getHeight() + 2, 100, 20);
                    opciones.addItem("NO");
                    opciones.addItem("SI");
                    opciones.setVisible(true);
                    panel.add(opciones);

                    JLabel labelSecciones = new JLabel("Elegi la sección a la que correspondera este plato?");
                    labelSecciones.setBounds(opciones.getX() + opciones.getWidth() + 90, labelAgregados.getY(), 700, 30);
                    labelSecciones.setVisible(true);
                    labelSecciones.setName("labelSecciones");
                    panel.add(labelSecciones);

                    JComboBox opcionesSec = new JComboBox();
                    opcionesSec.setBounds(labelSecciones.getX(), labelAgregados.getY() + labelAgregados.getHeight() + 2, 400, 20);
                    seccionesPlatos.forEach(seccion -> opcionesSec.addItem(seccion.getNombre()));
                    opcionesSec.setVisible(true);
                    panel.add(opcionesSec);

                    JButton botonAgregar = new JButton("AGREGAR");
                    botonAgregar.setBounds(ventana.getWidth() / 2 + 50, opciones.getY() + opciones.getHeight() + 30, 150, 50);
                    botonAgregar.setVisible(true);
                    panel.add(botonAgregar);

                    JButton botonOut = new JButton("SALIR");
                    botonOut.setBounds(ventana.getWidth() / 2 - 200, opciones.getY() + opciones.getHeight() + 30, 150, 50);
                    botonOut.setVisible(true);
                    panel.add(botonOut);

                    botonImagen.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            textFieldImagen.setText(chooser("png", "png", "La imagen no es png").getPath());
                        }
                    });

                    botonOut.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            for (SeccionesPlatos sec : seccionesPlatos){
                                if (sec.getNombre().equals(opcionesSec.getSelectedItem().toString())){
                                    if (platosSize[0] != sec.getPlatos().size()) {
                                       // mongo.actualizarSeccionesPlatos(seccionesPlatos);
                                    }
                                }
                            }
                            ventana.remove(panel);

                            panel.removeAll();
                            gestionarRestaurante(ventana);
                        }
                    });

                    botonAgregar.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {

                            HashMap<String, String> datosNewPlato = new HashMap<>();
                            boolean ok = true;
                            for (SeccionesPlatos sec : seccionesPlatos){
                                if (opcionesSec.getSelectedItem().toString().equals(sec.getNombre())){
                                    platosSize[0] = sec.getPlatos().size();
                                }
                            }

                            if (textFieldTiempoDemora.getText().equals("") || textFieldPrecio.getText().equals("") || textFieldDescripcion.getText().equals("") || textFieldImagen.getText().equals("") || textFieldName.getText().equals("")) {
                                ok = false;
                                JOptionPane.showMessageDialog(null, "Formulario incompleto");
                            } else {
                                try {
                                    float a = Float.parseFloat(textFieldPrecio.getText());
                                    if (a < 0) {
                                        ok = false;
                                        JOptionPane.showMessageDialog(null, "El precio es menor a 0");
                                    }
                                } catch (NumberFormatException ex) {
                                    ok = false;
                                    JOptionPane.showMessageDialog(null, "La sintaxis del precio es incorrecta, ej: 200.0");
                                }
                                if (!new File(textFieldImagen.getText()).exists() || (!new File(textFieldImagen.getText()).getName().substring(new File(textFieldImagen.getText()).getName().length() - 4).equals(".png")) && (!new File(textFieldImagen.getText()).getName().substring(new File(textFieldImagen.getText()).getName().length() - 4).equals(".png")) && (!new File(textFieldImagen.getText()).getName().substring(new File(textFieldImagen.getText()).getName().length() - 4).equals(".jpg"))) {
                                    ok = false;
                                    JOptionPane.showMessageDialog(null, "El archivo no es una imagen o la ubicacion es erronea, las imagenes solo pueden ser png o jpg");
                                }
                            }

                            if (ok) {
                                textFieldName.setText(textFieldName.getText().substring(0,1).toUpperCase()+textFieldName.getText().substring(1));
                                datosNewPlato.put("Nombre", textFieldName.getText());
                                datosNewPlato.put("Descripcion", textFieldDescripcion.getText());
                                datosNewPlato.put("TiempoDemora", textFieldTiempoDemora.getText());
                                datosNewPlato.put("Precio", textFieldPrecio.getText());
                                datosNewPlato.put("Imagen", textFieldImagen.getText());
                            }
                            if (ok && opciones.getSelectedItem().equals("SI") && abierto[0]) {
                                abierto[0] = false;
                                JFrame frameAgregados = new JFrame("AGREGADOS");
                                frameAgregados.setSize(500, 730);
                                frameAgregados.setVisible(true);

                                JPanel panelAgregados = new JPanel();
                                panelAgregados.setName("menu");
                                panelAgregados.setSize(frameAgregados.getSize());
                                panelAgregados.setLayout(null);
                                panelAgregados.setVisible(true);

                                Plato plato = new Plato(datosNewPlato.get("Nombre"), Float.parseFloat(datosNewPlato.get("Precio")), new File(datosNewPlato.get("Imagen")), datosNewPlato.get("Descripcion"), datosNewPlato.get("TiempoDemora"));

                                //if (abierto[0]) {
                                    crearMenuAgregados(frameAgregados, plato);
                                    frameAgregados.add(panelAgregados);
                                //}

                                abierto[0] = false;

                                agregarPlato(datosNewPlato.get("Nombre"), datosNewPlato.get("Precio"), datosNewPlato.get("Imagen"), datosNewPlato.get("Descripcion"), datosNewPlato.get("TiempoDemora"), opcionesSec.getSelectedItem().toString());

                                frameAgregados.addWindowListener(new WindowAdapter() {
                                    @Override
                                    public void windowClosing(WindowEvent event) {
                                        agregarPlato(plato, opcionesSec.getSelectedItem().toString());
                                    }
                                });
                            } else if (ok && !opciones.getSelectedItem().equals("SI")) {
                                if (agregarPlato(datosNewPlato.get("Nombre"), datosNewPlato.get("Precio"), datosNewPlato.get("Imagen"), datosNewPlato.get("Descripcion"), datosNewPlato.get("TiempoDemora"), opcionesSec.getSelectedItem().toString())) {
                                    JOptionPane.showMessageDialog(null, "El plato se agrego correctamente");
                                }
                            }
                        }
                    });
                }else{
                    JOptionPane.showMessageDialog(null, "no hay secciones a las que agregar platos. Por favor crea una sección primero");
                }
            }
        });
        boton2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                editarPlato(ventana);
            }
        });
        boton3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int confirmD = JOptionPane.showConfirmDialog(null, "¿Estas seguro que quieres borrarlo? se borraran todos los platos del menu");
                if (confirmD== JOptionPane.YES_NO_OPTION){
                    seccionesPlatos.clear();
                    Peticion.putSinJson("http://localhost:8888/api/javaAPP/gestionarMenu/borrarMenu/"+id);
                    //mongo.actualizarSeccionesPlatos(seccionesPlatos);
                    JOptionPane.showMessageDialog(null, "Se borraron todos los platos y sus secciones");
                }
            }
        });
        boton4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.remove(panel);

                agregarSeccionPlato(ventana);
            }
        });
        boton5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.remove(panel);

                editarSeccionPlato(ventana);
            }
        });
    }



    public void gestionarMesas(JFrame ventana){
        while (ventana.getComponents().length>1){
            ventana.remove(ventana.getComponents().length-1);
        }

        JPanel panelMesas = new JPanel();
        panelMesas.setSize(1400, 700);
        panelMesas.setLayout(null);
        panelMesas.setVisible(false);
        panelMesas.setName("mesas");

        JButton salir = new JButton("SALIR");
        salir.setSize(200, 50);
        salir.setLocation(ventana.getWidth() / 2 - 100, ventana.getHeight() - 300);
        salir.setVisible(true);
        salir.setName("boton11");

        JButton salir1 = new JButton("SALIR");
        salir1.setSize(200, 50);
        salir1.setLocation(ventana.getWidth() / 2 - 200, ventana.getHeight() - 200);
        salir1.setVisible(true);
        salir1.setName("salir1");

        JButton salir2 = new JButton("SALIR");
        salir2.setSize(500, 50);
        salir2.setVisible(true);
        salir2.setName("salir2");

        JButton agregar = new JButton("AGREGAR");
        agregar.setSize(200, 50);
        agregar.setLocation(ventana.getWidth() / 2 - 100, ventana.getHeight() - 400);
        agregar.setVisible(true);
        agregar.setName("agregar");

        JButton ocupar = new JButton("OCUPAR");
        ocupar.setSize(200, 50);
        ocupar.setLocation(ventana.getWidth() / 2 - 100, ventana.getHeight() - 400);
        ocupar.setVisible(true);
        ocupar.setName("ocupar");

        JButton desocupar = new JButton("DESOCUPAR");
        desocupar.setSize(200, 50);
        desocupar.setLocation(ventana.getWidth() / 2 - 100, ventana.getHeight() - 400);
        desocupar.setVisible(true);
        desocupar.setName("desocupar");

        JButton agregarM = new JButton("AGREGAR MESAS");
        agregarM.setLocation(150, 200);
        agregarM.setSize(200, 50);
        agregarM.setName("agregarM");
        agregarM.setVisible(true);

        JButton ocuparM = new JButton("OCUPAR MESA");
        ocuparM.setLocation((1200 / 2 - agregarM.getWidth() / 2) + 100, 200);
        ocuparM.setSize(200, 50);
        ocuparM.setName("ocuparM");
        ocuparM.setVisible(true);

        JButton desocuparM = new JButton("DESOCUPAR MESA");
        desocuparM.setLocation(1250 - ocuparM.getWidth(), 200);
        desocuparM.setSize(200, 50);
        desocuparM.setName("desocuparM");
        desocuparM.setVisible(true);

        JButton borrarM = new JButton("BORRAR MESAS");
        borrarM.setLocation(150, 50 + agregarM.getHeight() + agregarM.getY());
        borrarM.setSize(200, 50);
        borrarM.setName("borrarM");
        borrarM.setVisible(true);

        JButton qrs = new JButton("QRS");
        qrs.setLocation((1200 / 2 - ocuparM.getWidth() / 2) + 100, 50 + ocuparM.getHeight() + ocuparM.getY());
        qrs.setSize(200, 50);
        qrs.setName("qrs");
        qrs.setVisible(true);

        JTextField textMesas = new JTextField();
        textMesas.setLocation(ventana.getWidth() / 2 - 250,200);
        textMesas.setSize(500, 50);
        textMesas.setName("nMesas");
        textMesas.setVisible(true);

        JLabel mesasAdd = new JLabel("Cuantas mesas quiere agregar?");
        mesasAdd.setSize(500, 50);
        mesasAdd.setLocation(ventana.getWidth() / 2 - 250,150);
        mesasAdd.setVisible(true);
        mesasAdd.setName("perfiLabel");

        JLabel mesasOcup = new JLabel("Que mesa quiere ocupar?");
        mesasOcup.setSize(500, 50);
        mesasOcup.setLocation(ventana.getWidth() / 2 - 250,150);
        mesasOcup.setVisible(true);
        mesasOcup.setName("perfiLabel");

        JLabel mesasDesocup = new JLabel("Que mesa quiere desocupar?");
        mesasDesocup.setSize(500, 50);
        mesasDesocup.setLocation(ventana.getWidth() / 2 - 250,150);
        mesasDesocup.setVisible(true);
        mesasDesocup.setName("perfiLabel");

        JButton borro = new JButton("BORRAR");
        borro.setSize(200, 50);
        borro.setLocation(salir1.getX() + 200, salir1.getY());
        borro.setVisible(true);
        borro.setName("borrar");

        JLabel labelM = new JLabel("MESAS");
        labelM.setName("labelMesas");
        labelM.setVisible(true);
        labelM.setFont(fuentes.get("Times New Roman"));
        labelM.setBounds(ventana.getWidth()/2-100, 20, 300, 120);


        ventana.add(panelMesas);
        panelMesas.add(salir);
        panelMesas.add(agregarM);
        panelMesas.add(ocuparM);
        panelMesas.add(desocuparM);
        panelMesas.add(borrarM);
        panelMesas.add(qrs);
        panelMesas.setVisible(true);

        agregarM.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                panelMesas.removeAll();
                panelMesas.add(textMesas);
                panelMesas.add(mesasAdd);
                panelMesas.add(agregar);
                panelMesas.add(salir1);

                agregar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e){
                        if (esNumero(textMesas.getText())) {
                            int n = Integer.parseInt(textMesas.getText());
                            Mesa.agregarMesas(mesas,n, id);
                            //mongo.actualizarMesas(mesas);
                            JOptionPane.showMessageDialog(null, "Se agregaron "+n+" mesas");
                            ventana.remove(panelMesas);
                            gestionarMesas(ventana);;
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Ingrese un Numero entero");
                        }
                    }
                });
            }
        });

        ocuparM.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                panelMesas.removeAll();
                panelMesas.add(textMesas);
                panelMesas.add(mesasOcup);
                panelMesas.add(ocupar);
                panelMesas.add(salir1);

                ocupar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e){
                        if (esNumero(textMesas.getText())) {
                            int n = Integer.parseInt(textMesas.getText());
                            if (Mesa.comprobarMesa(mesas,n)){
                                Mesa.ocuparMesas(mesas,n);
                                for (Mesa mesa : mesas){
                                    if (mesa.getNumMesa() == n){
                                        Peticion.putSinJson("http://localhost:8888/api/javaAPP/gestionarMesas/estado/"+mesa.isOcupada()+"/"+mesa.getNumMesa()+"/"+id);
                                        break;
                                    }
                                }
                                ventana.remove(panelMesas);
                                gestionarMesas(ventana);
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "La mesa no existe en el restaurante");
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Ingrese un Numero entero");
                        }
                    }
                });
            }
        });

        desocuparM.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                panelMesas.removeAll();
                panelMesas.add(textMesas);
                panelMesas.add(desocupar);
                panelMesas.add(mesasDesocup);
                panelMesas.add(salir1);

                desocupar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e){
                        if (esNumero(textMesas.getText())) {
                            int n = Integer.parseInt(textMesas.getText());
                            if (Mesa.comprobarMesa(mesas,n)){
                                Mesa.desocuparMesas(mesas,n);
                                for (Mesa mesa : mesas){
                                    if (mesa.getNumMesa() == n){
                                        Peticion.putSinJson("http://localhost:8888/api/javaAPP/gestionarMesas/estado/"+mesa.isOcupada()+"/"+mesa.getNumMesa()+"/"+id);
                                        // mongo.actualizarMesa(mesa);
                                        break;
                                    }
                                }
                                for (int i = pedidos.size()-1; i > 0 ; i--) {
                                    if (pedidos.get(i).isAbierto() && pedidos.get(i).getnMesa() == n){
                                        pedidos.get(i).setAbierto(false);
                                        Peticion.putSinJson("http://localhost:8888/api/javaAPP/gestionarPedidos/cobrar/"+pedidos.get(i).getnPedido()+"/"+id);
                                        //Peticion.cobrar(id, pedidos.get(i).getnPedido());
                                    }
                                }
                                ventana.remove(panelMesas);
                                gestionarMesas(ventana);
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "La mesa no existe en el restaurante");
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Ingrese un Numero entero");
                        }
                    }
                });
            }
        });

        borrarM.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                panelMesas.removeAll();
                int vueltas = 0;

                List<Mesa> mesasort = new ArrayList<>(mesas);
                Collections.sort(mesasort);
                panelMesas.add(labelM);
                panelMesas.add(salir2);
                for(Mesa mesa : mesasort) {
                    JButton nMesa = new JButton();
                    nMesa.setSize(150, 50);
                    nMesa.setVisible(true);
                    nMesa.setName("borrar");
                    panelMesas.add(nMesa);

                    nMesa.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            int BM = JOptionPane.showConfirmDialog(null,"Estas seguro que quiere borrar esta mesa?");
                            if (BM == JOptionPane.YES_OPTION){
                                mesas.remove(mesa);
                                Peticion.deleteSinJson("http://localhost:8888/api/javaAPP/gestionarMesas/borrarMesa/"+mesa.getNumMesa()+"/"+id);
                                panelMesas.remove(nMesa);
                               // mongo.actualizarMesas(mesas);
                                File imagen = new File(".\\src\\com\\company\\images\\qr\\"+"Mesa"+mesa.getNumMesa()+".png");
                                imagen.delete();
                            }
                        }
                    });

                    nMesa.setLocation(ventana.getWidth() / 2 - 100, Math.round((labelM.getY() + labelM.getHeight()+15)+((labelM.getHeight()/2)*(vueltas))));
                    nMesa.setText("Mesa numero " + mesa.getNumMesa());
                    vueltas++;
                }
                salir2.setLocation(panelMesas.getWidth() / 2 - salir2.getWidth() / 2, panelMesas.getComponent(panelMesas.getComponents().length - 1).getY() + panelMesas.getComponent(panelMesas.getComponents().length - 1).getHeight() + 100);
                panelMesas.setPreferredSize(new Dimension(1350, salir2.getHeight() + salir2.getY() + 50));
                JScrollPane scrollBar = new JScrollPane(panelMesas, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                panelMesas.setVisible(true);
                ventana.add(scrollBar);
            }
        });

        qrs.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int GQR = JOptionPane.showConfirmDialog(null,"Se generaran los codigos para todas las mesas que no tengan Qr, Esta seguro?");
                if (GQR == JOptionPane.YES_OPTION){
                    try {
                        Mesa.generarQr(mesas, id);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                   // mongo.actualizarMesas(mesas);
                }
            }
        });

        salir2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.getContentPane().removeAll();
                ventana.remove(panelMesas);
                gestionarMesas(ventana);
            }
        });

        salir1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.getContentPane().removeAll();
                ventana.remove(panelMesas);
                gestionarMesas(ventana);
            }
        });

        salir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.remove(panelMesas);
                panelMenu(ventana);
            }
        });

    }

    public void manejarPerfil(JFrame ventana){
        JPanel panelPerfil = new JPanel();
        panelPerfil.setSize(1400, 700);
        panelPerfil.setLayout(null);
        panelPerfil.setVisible(false);
        panelPerfil.setName("perfil");

        JButton botonp2 = new JButton("INGRESE LA FOTO");
        botonp2.setLocation(150, 300);
        botonp2.setSize(200, 50);
        botonp2.setName("Ft");
        botonp2.setVisible(true);

        JButton botonp3 = new JButton("GUARDAR CAMBIOS");
        botonp3.setLocation(950,500);
        botonp3.setSize(200, 50);
        botonp3.setName("Guardar");
        botonp3.setVisible(true);

        JLabel perfiLabel = new JLabel("Ingrese el nombre de su restaurante");
        perfiLabel.setSize(500, 50);
        perfiLabel.setLocation(150,150);
        perfiLabel.setVisible(true);
        perfiLabel.setName("perfiLabel");
        panelPerfil.add(perfiLabel);

        JLabel imgPerfil = new JLabel();
        imgPerfil.setSize(150,150);
        imgPerfil.setLocation(450, 300);

        JTextField texto1 = new JTextField();
        texto1.setLocation(150, 200);
        texto1.setSize(500, 50);
        texto1.setName("name");
        texto1.setText(nombre);
        texto1.setVisible(true);

        ventana.add(panelPerfil);
        panelPerfil.add(perfiLabel);
        panelPerfil.add(imgPerfil);
        panelPerfil.add(botonp2);
        panelPerfil.add(texto1);
        panelPerfil.add(botonp3);
        panelPerfil.setVisible(true);

        Restaurante restaurante = this;

        final File[] file = {logo};
        botonp2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                file[0] = chooser("png", "Png", "Use una foto PNG");
                ImageIcon ft = new ImageIcon(logo.getPath());
                Icon ftito = new ImageIcon(ft.getImage().getScaledInstance(imgPerfil.getWidth(),imgPerfil.getHeight(),Image.SCALE_DEFAULT));
                imgPerfil.setText(null);
                imgPerfil.setIcon(ftito);
            }
        });

        botonp3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (nombre != texto1.getText()){
                    nombre= texto1.getText();
                    Peticion.putSinJson("http://localhost:8888/api/javaAPP/actualizarPerfil/nombre/"+nombre+"/"+id);
                    //Peticion.actualizarNombreRest(id, nombre);
                }
                if (logo != file[0]){
                    logo = file[0];
                    Peticion.actualizarImagenRest(id, logo.getPath());
                }
               // mongo.actualizarDataUser(restaurante);
                JOptionPane.showMessageDialog(null, "se guardo correctamente");
                ventana.remove(panelPerfil);
                panelMenu(ventana);
            }
        });
    }

    public void panelMenu(JFrame ventana){
        while (ventana.getComponents().length>1){
            ventana.remove(ventana.getComponents().length-1);
        }

        JPanel panelMenu = new JPanel();
        panelMenu.setName("menu");
        panelMenu.setSize(1350, 700);
        panelMenu.setLayout(null);
        panelMenu.setVisible(false);

        JLabel nombreSistema = new JLabel(this.nombre);
        nombreSistema.setSize(250, 50);
        nombreSistema.setLocation(ventana.getWidth() / 2 - nombreSistema.getWidth() / 2, 100);
        nombreSistema.setFont(fuentes.get("Times New Roman"));
        panelMenu.add(nombreSistema);

        JButton boton1 = new JButton("GESTIONAR MESAS");
        boton1.setLocation(150, 200);
        boton1.setSize(200, 50);
        boton1.setName("gestionarMesas");
        boton1.setVisible(true);
        panelMenu.add(boton1);

        JButton boton2 = new JButton("--");
        boton2.setSize(200, 50);
        boton2.setLocation((1200 / 2 - boton2.getWidth() / 2) + 100, 200);
        boton2.setName("--");
        boton2.setVisible(true);
        panelMenu.add(boton2);

        JButton boton3 = new JButton("GESTIONAR MENU");
        boton3.setSize(200, 50);
        boton3.setLocation(1250 - boton3.getWidth(), 200);
        boton3.setVisible(true);
        boton3.setName("HacerPedido");
        panelMenu.add(boton3);

        JButton boton4 = new JButton("COBRAR");
        boton4.setSize(200, 50);
        boton4.setLocation(150, 50 + boton1.getHeight() + boton1.getY());
        boton4.setVisible(true);
        boton4.setName("--");
        panelMenu.add(boton4);

        JButton boton5 = new JButton("--");
        boton5.setSize(200, 50);
        boton5.setLocation((1200 / 2 - boton2.getWidth() / 2) + 100, 50 + boton2.getHeight() + boton2.getY());
        boton5.setVisible(true);
        boton5.setName("--");
        panelMenu.add(boton5);

        JButton boton7 = new JButton("ENTREGAR PEDIDO");
        boton7.setSize(200, 50);
        boton7.setLocation(150, 50 + boton4.getHeight() + boton4.getY());
        boton7.setVisible(true);
        boton7.setName("EntregarPedido");
        panelMenu.add(boton7);

        JButton boton8 = new JButton("PROXIMO PEDIDO");
        boton8.setSize(200, 50);
        boton8.setLocation((1200 / 2 - boton2.getWidth() / 2) + 100, 50 + boton5.getHeight() + boton5.getY());
        boton8.setVisible(true);
        panelMenu.add(boton8);

        JButton botonp = new JButton("EDITAR PERFIL");
        botonp.setLocation(1250 - boton3.getWidth(), 50 + boton3.getHeight() + boton3.getY());
        botonp.setSize(200, 50);
        botonp.setName("perfil");
        botonp.setVisible(true);
        panelMenu.add(botonp);

        panelMenu.setVisible(true);
        ventana.add(panelMenu);

        botonp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.remove(panelMenu);
                panelMenu.setVisible(false);

                manejarPerfil(ventana);
            }
        });

        /* GESTIONAR MESAS*/
        boton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.remove(panelMenu);
                panelMenu.setVisible(false);

                gestionarMesas(ventana);
            }
        });

        /*GESTIONAR MENU*/
        boton3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                ventana.remove(panelMenu);
                panelMenu.setVisible(false);

                gestionarRestaurante(ventana);
            }
        });
        boton4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.remove(panelMenu);
                cobrar(ventana);
            }
        });
        boton7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.remove(panelMenu);
                entregarPedido(ventana);
            }
        });
        boton8.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.remove(panelMenu);
                proximoPedido(ventana);
            }
        });
    }

    public void cobrar(JFrame ventana){
        JPanel panelIngresar = new JPanel();
        panelIngresar.setSize(1400, 700);
        panelIngresar.setLayout(null);
        panelIngresar.setVisible(false);
        panelIngresar.setName("ingresar");
        ventana.add(panelIngresar);

        JButton boton = new JButton("COBRAR");
        boton.setVisible(true);
        boton.setName("boton");
        panelIngresar.add(boton);

        JButton botonOut = new JButton("SALIR");
        botonOut.setVisible(true);
        botonOut.setName("botonOUT");
        botonOut.setBounds(ventana.getWidth()/2-250, 550, 200, 50);
        panelIngresar.add(botonOut);

        JLabel labelIngresar = new JLabel("Selecciona el pedido a cobrar");
        labelIngresar.setSize(200, 50);
        labelIngresar.setName("labelIngresar");
        labelIngresar.setLocation(ventana.getWidth()/2-labelIngresar.getWidth()/2, 20);
        labelIngresar.setVisible(true);

        JComboBox menuPedidos = new JComboBox();
        menuPedidos.setName("comboboxMenu");
        menuPedidos.setBounds(ventana.getWidth()/2-200, 120,300,100);

        boolean ceroPedidos=true;
        for(Pedido pedidoAux : this.pedidos){
            if (pedidoAux.isAbierto()){
                menuPedidos.addItem("MESA "+pedidoAux.getnMesa()/*+" (pedido n°" + pedidoAux.getnPedido() + " )"*/);
                ceroPedidos = false;
            }
        }

        if (ceroPedidos){
            JLabel label = new JLabel("No hay pedidos sin cobrar");
            label.setBounds(panelIngresar.getWidth()/2 - 350, 100, 800, 50);
            label.setFont(fuentes.get("Times New Roman"));
            label.setVisible(true);
            panelIngresar.add(label);

            panelIngresar.setVisible(true);
            labelIngresar.setVisible(false);
        }else{

            menuPedidos.setVisible(true);

            JButton buttonOK = new JButton(new ImageIcon(".\\src\\com\\company\\images\\check.png"));
            buttonOK.setBounds(menuPedidos.getX()+menuPedidos.getWidth(), menuPedidos.getY(), 100, 100);
            buttonOK.setName("buttonOK");
            buttonOK.setVisible(true);
            panelIngresar.add(buttonOK);

            panelIngresar.add(labelIngresar);
            panelIngresar.add(menuPedidos);

            panelIngresar.setVisible(true);

            buttonOK.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ventana.getContentPane().removeAll();

                    float total = 0;

                    panelIngresar.setVisible(false);

                    JLabel labelP = new JLabel("Platos");
                    labelP.setFont(fuentes.get("Garamond"));
                    labelP.setBounds(ventana.getWidth()/2-150, buttonOK.getY()+buttonOK.getHeight()+30, 200, 100);
                    labelP.setVisible(true);
                    labelP.setName("labelPlatos");
                    panelIngresar.add(labelP);

                    JLabel labelEntregado = new JLabel("Precio");
                    labelEntregado.setFont(fuentes.get("Garamond"));
                    labelEntregado.setBounds(ventana.getWidth()/2+80, labelP.getY(), 100, 100);
                    labelEntregado.setVisible(true);
                    labelEntregado.setName("labelEntregado");
                    panelIngresar.add(labelEntregado);

                    JLabel labelDetalle = new JLabel("Detalle");
                    labelDetalle.setFont(fuentes.get("Garamond"));
                    labelDetalle.setBounds(ventana.getWidth()/2+150, labelP.getY(), 100, 100);
                    labelDetalle.setVisible(true);
                    labelDetalle.setName("labelDetalle");
                    panelIngresar.add(labelDetalle);

                    Component componentes[]={boton, botonOut, buttonOK, labelIngresar, labelDetalle,labelEntregado, labelP, menuPedidos};
                    clearPanel(panelIngresar, componentes);

                    int npedido = 0;
                    for (Pedido pedido: pedidos){
                        if (pedido.getnMesa() == Integer.parseInt(menuPedidos.getSelectedItem().toString().substring(5)) && pedido.isAbierto()){
                            npedido = pedido.getnPedido();
                        }
                    }

                    JFrame frameAgregados = new JFrame("DETALLE PLATO");
                    frameAgregados.setSize(500, 730);
                    frameAgregados.setVisible(false);

                    for (Pedido pedido : pedidos) {
                        if (pedido.getnPedido() == npedido) {
                            int vueltas = 1;

                            for (PlatoPedido plato: pedido.getPlatos()){

                                JLabel labelPlatos = new JLabel(plato.getNombre());
                                labelPlatos.setSize(200, 35);
                                labelPlatos.setLocation(labelP.getX(), Math.round((labelP.getY() + labelP.getHeight())+((labelP.getHeight()/2)*(vueltas-1))));

                                Border border = BorderFactory.createLineBorder(Color.black, 1);

                                labelPlatos.setBorder(border);
                                labelPlatos.setBackground(Color.white);
                                labelPlatos.setOpaque(true);
                                labelPlatos.setVisible(true);
                                panelIngresar.add(labelPlatos);

                                float precioFinalPlato = plato.getPrecio();
                                for (Float agregadoPrecio: plato.getAgregados().values()){
                                    precioFinalPlato = precioFinalPlato + agregadoPrecio;
                                }

                                JLabel labelPrecio = new JLabel(precioFinalPlato + "");
                                labelPrecio.setSize(35, 35);
                                labelPrecio.setLocation(labelPlatos.getX() + labelPlatos.getWidth() + 30, labelPlatos.getY());
                                labelPrecio.setVisible(true);
                                panelIngresar.add(labelPrecio);

                                JButton botonDetalle = new JButton(new ImageIcon(".\\src\\com\\company\\images\\factura.png"));
                                botonDetalle.setBounds(labelDetalle.getX()+9, labelPrecio.getY(), 35, 35);
                                botonDetalle.setVisible(true);
                                panelIngresar.add(botonDetalle);

                                botonDetalle.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        JPanel panelDetalle = new JPanel();
                                        frameAgregados.remove(panelDetalle);
                                        panelDetalle.setSize(frameAgregados.getSize());
                                        panelDetalle.setVisible(false);
                                        panelDetalle.setLayout(null);

                                        JLabel labelNombre = new JLabel("Nombre");
                                        labelNombre.setFont(fuentes.get("Garamond"));
                                        labelNombre.setBounds(frameAgregados.getWidth()/2-150, 50, 200, 100);
                                        labelNombre.setVisible(true);
                                        labelNombre.setName("labelNombre");
                                        panelDetalle.add(labelNombre);

                                        JLabel labelPre = new JLabel("Precio");
                                        labelPre.setFont(fuentes.get("Garamond"));
                                        labelPre.setBounds(frameAgregados.getWidth()/2+80, labelNombre.getY(), 100, 100);
                                        labelPre.setVisible(true);
                                        labelPre.setName("labelPre");
                                        panelDetalle.add(labelPre);

                                        JLabel labelPlatos = new JLabel("PLATO: "+plato.getNombre());
                                        labelPlatos.setSize(200, 35);
                                        labelPlatos.setLocation(labelNombre.getX(), Math.round((labelNombre.getY() + labelNombre.getHeight())));
                                        labelPlatos.setBorder(border);
                                        labelPlatos.setBackground(Color.white);
                                        labelPlatos.setOpaque(true);
                                        labelPlatos.setVisible(true);
                                        panelDetalle.add(labelPlatos);

                                        JLabel labelPrecio = new JLabel(plato.getPrecio()+"");
                                        labelPrecio.setSize(35, 35);
                                        labelPrecio.setLocation(labelPlatos.getX() + labelPlatos.getWidth() + 30, labelPlatos.getY());
                                        labelPrecio.setVisible(true);
                                        panelDetalle.add(labelPrecio);

                                        int v = 1;
                                        for (Map.Entry<String, Float> agregado : plato.getAgregados().entrySet()){
                                            JLabel labelAgregado = new JLabel("AGREGADO: "+agregado.getKey());
                                            labelAgregado.setSize(200, 35);
                                            labelAgregado.setLocation(labelNombre.getX(), Math.round((labelNombre.getY() + labelNombre.getHeight()+((labelNombre.getHeight()/2)*(v)))));
                                            labelAgregado.setBorder(border);
                                            labelAgregado.setBackground(Color.white);
                                            labelAgregado.setOpaque(true);
                                            labelAgregado.setVisible(true);
                                            panelDetalle.add(labelAgregado);

                                            JLabel labelAgPrecio = new JLabel(agregado.getValue()+"");
                                            labelAgPrecio.setSize(35, 35);
                                            labelAgPrecio.setLocation(labelAgregado.getX() + labelAgregado.getWidth() + 30, labelAgregado.getY());
                                            labelAgPrecio.setVisible(true);
                                            panelDetalle.add(labelAgPrecio);

                                            v++;
                                        }
                                        frameAgregados.getContentPane().removeAll();
                                        panelDetalle.setPreferredSize(new Dimension(1350, panelDetalle.getComponent(panelDetalle.getComponents().length-1).getY()+200));
                                            JScrollPane scroll = new JScrollPane(panelDetalle, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                                            frameAgregados.remove(panelDetalle);
                                            frameAgregados.add(scroll);
                                        panelDetalle.setVisible(true);
                                        //frameAgregados.add(panelDetalle);
                                        frameAgregados.setVisible(true);
                                    }
                                });

                                total = total + precioFinalPlato;

                                vueltas++;
                            }
                            break;
                        }
                    }

                    JLabel labelPrecioTotal = new JLabel("TOTAL: "+total);
                    labelPrecioTotal.setBounds(ventana.getWidth()/2-150, panelIngresar.getComponent(panelIngresar.getComponents().length-1).getY()+panelIngresar.getComponent(panelIngresar.getComponents().length-1).getHeight() + 50, 400, 50);
                    labelPrecioTotal.setFont(fuentes.get("Times New Roman"));
                    labelPrecioTotal.setVisible(true);
                    panelIngresar.add(labelPrecioTotal);

                    botonOut.setBounds(ventana.getWidth()/2-250, panelIngresar.getComponent(panelIngresar.getComponents().length-1).getY()+panelIngresar.getComponent(panelIngresar.getComponents().length-1).getHeight() + 50, 200, 50);
                    boton.setBounds(ventana.getWidth()/2+50, panelIngresar.getComponent(panelIngresar.getComponents().length-1).getY()+panelIngresar.getComponent(panelIngresar.getComponents().length-1).getHeight() + 50, 200, 50);
                    //boton.setBounds(700, 50, 200, 50);
                    panelIngresar.setVisible(true);

                    //scrollbar
                    panelIngresar.setPreferredSize(new Dimension(1350, botonOut.getY()+botonOut.getHeight()+30));
                    JScrollPane scrollPane = new JScrollPane(panelIngresar, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    ventana.remove(panelIngresar);
                    ventana.add(scrollPane);
                }
            });
            boton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int confirmD = JOptionPane.showConfirmDialog(null, "¿Estas seguro que quieres cobrarlo");
                    if(confirmD == JOptionPane.YES_OPTION){
                        int npedido = 0;
                        for (Pedido pedido: pedidos){
                            if (pedido.getnMesa() == Integer.parseInt(menuPedidos.getSelectedItem().toString().substring(5)) && pedido.isAbierto()){
                                npedido = pedido.getnPedido();
                            }
                        }
                        for (Pedido pedido: pedidos){
                            if (npedido == pedido.getnPedido()){
                                pedido.setAbierto(false);
                                Peticion.putSinJson("http://localhost:8888/api/javaAPP/gestionarPedidos/cobrar/"+pedido.getnPedido()+"/"+id);
                                //Peticion.cobrar(id, pedido.getnPedido());
                                ventana.getContentPane().removeAll();
                                panelMenu(ventana);
                            }
                        }
                    }
                }
            });
        }
        botonOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.getContentPane().removeAll();
                panelMenu(ventana);
            }
        });
    }

    public void cargarDatos(){
        this.mesas = Peticion.obtenerMesas();
        Peticion.dataUser(this);
        this.pedidos = Peticion.obtenerPedidos(this.id);
        this.seccionesPlatos = Peticion.obtenerSeccionesPlatos(this.id);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        Restaurante restaurante = new Restaurante();

        Login login = new Login(600, 600, "Iniciar sesión");
        login.getVentana().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                if (login.isSesion()) {

                    restaurante.setId(login.getId());
                    restaurante.cargarDatos();
                    fuentes.put("Times New Roman", new Font("Times New Roman", Font.BOLD, 40));
                    fuentes.put("Garamond", new Font("Garamond", Font.BOLD, 15));

                    JFrame ventana = new JFrame("RESTAURANTE");
                    ventana.setSize(1350, 730);
                    ventana.setVisible(true);
                    ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                    ventana.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent windowEvent) {
                            //restaurante.mongo.actualizarSeccionesPlatos(restaurante.getSeccionesPlatos());
                            //restaurante.mongo.actualizarMesas(restaurante.getMesas());
                        }
                    });

                    restaurante.panelMenu(ventana);
                }
            }
        });
    }
}

//hacer que se pueda abrir agregados solo una vez
//hacer los cambios necesarios en acceso mongo (platos)
