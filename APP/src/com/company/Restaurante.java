package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;

public class Restaurante {
    private HashSet<Mesa> mesas = new HashSet<>();
    private HashSet<Plato> platos = new HashSet<>();
    private ArrayList<Pedido> pedidos = new ArrayList<>();
    private String nombre;
    private ArrayList<Ocupacion> ocupaciones = new ArrayList<>();
    public static SimpleDateFormat dateFormatSQL = new SimpleDateFormat("yyyy-MM-dd");
    public static HashMap<String, Font> fuentes = new HashMap<>();

    //GETTERS && SETTERS

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

    public HashSet<Plato> getPlatos() {
        return platos;
    }

    public void setPlatos(HashSet<Plato> platos) {
        this.platos = platos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    //CONSTRUCTOR

    public Restaurante (String nombre){
        this.nombre = nombre;
    }

    public void cleanPanel(JPanel panelIngresar, Component components[]){
        for (int i = 0; i < panelIngresar.getComponents().length ; i++) {
            if (panelIngresar.getComponent(i).getName() != null) {
                boolean check=true;
                for (Component componente : components){
                    if (panelIngresar.getComponent(i).getName().equals(componente.getName())) {
                        check = false;
                    }
                }
                if(check){
                    panelIngresar.remove(panelIngresar.getComponent(i));
                    i--;
                }
                else
                {
                    panelIngresar.getComponent(i).setVisible(false);
                }
            }
            else
            {
                panelIngresar.remove(panelIngresar.getComponent(i));
                i--;
            }
        }
    }

    public boolean comprobarMesa(int mesa) {
        for (Mesa mesaAux : this.mesas) {
            if (mesaAux.getnMesa() == mesa && !mesaAux.isOcupada()) {
                return false;
            }
        }
        return true;
    }

    public void platoMasPedido(JFrame ventana,JPanel panelMenu,JPanel panelIngresar,JButton boton11, JLabel labelFeedBack, boolean masMenos) {

        JButton botonAgr= new JButton("INGRESAR");
        botonAgr.setBounds(ventana.getWidth()/2+10, boton11.getY(), boton11.getWidth(), boton11.getHeight());
        botonAgr.setVisible(true);
        panelIngresar.add(botonAgr);

        JButton botonSalir = new JButton("SALIR");
        botonSalir.setBounds(ventana.getWidth()/2-boton11.getWidth()-10, boton11.getY(), boton11.getWidth(), boton11.getHeight());
        botonSalir.setVisible(true);
        panelIngresar.add(botonSalir);

        JComboBox fechas = new JComboBox();
        fechas.setName("comboboxFechas");
        fechas.setBounds(ventana.getWidth()/2-150, 120,300,100);
        fechas.addItem(dateFormatSQL.format(new Date()));

        for(Pedido pedido: this.pedidos){
            boolean check = true;
            for (int i = 0; i <fechas.getItemCount() ; i++) {
                if (fechas.getItemAt(i)==pedido.getFecha()){
                    check=false;
                }
            }
            if (check){
                fechas.addItem(pedido.getFecha());
            }
        }

        fechas.setVisible(true);
        panelIngresar.add(fechas);
        panelIngresar.setVisible(true);
        ventana.add(panelIngresar);

        botonSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.remove(panelIngresar);
                panelIngresar.setVisible(false);

                panelIngresar.removeAll();
                ventana.add(panelMenu);
                panelMenu.setVisible(true);
            }
        });
        botonAgr.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final HashMap<String, Integer> platosAux = new HashMap<>();
                for (Plato plato : platos){
                    platosAux.put(plato.getNombre(), 0);
                }
                for(Pedido pedido: pedidos){
                    if (pedido.getFecha().equals(fechas.getSelectedItem())){
                        for(Map.Entry<String, Integer> plato: pedido.getPlatos().entrySet())
                        {
                            platosAux.put(plato.getKey(), platosAux.get(plato.getKey())+plato.getValue());
                        }
                    }
                }
                String platoGanador  = "";
                int contador = 0;
                for(Map.Entry<String, Integer> plato: platosAux.entrySet()){
                    if (masMenos) {
                        if (plato.getValue() >= contador) {
                            platoGanador = plato.getKey();
                            contador = plato.getValue();
                        }
                    }
                    else
                    {
                        if (plato.getValue() <= contador || contador == 0){
                            platoGanador = plato.getKey();
                            contador = plato.getValue();
                        }
                    }
                }
                if (masMenos) {
                    labelFeedBack.setText("El pedido mas solicitado fue " + platoGanador + ", pedidos: " + contador);
                }
                else {
                    labelFeedBack.setText("El pedido menos solicitado fue " + platoGanador + ", pedidos: " + contador);
                }
                labelFeedBack.setVisible(true);
                boton11.setVisible(true);

                panelIngresar.removeAll();
                panelIngresar.add(botonSalir);
                panelIngresar.add(labelFeedBack);
            }
        });
    }

    public void entregarPedido(JFrame ventana,JPanel panelMenu, JPanel panelFeedBack, JPanel panelIngresar, JButton boton10, JButton boton11, JTextField textField, JLabel labelIngresar, JLabel labelFeedBack) {
        JButton boton = new JButton("AGREGAR");
        boton.setBounds(ventana.getWidth()/2+10, boton10.getY()+30, boton10.getWidth(), boton10.getHeight());
        boton.setVisible(true);
        panelIngresar.add(boton);

        JButton botonOut = new JButton("SALIR");
        botonOut.setBounds(ventana.getWidth()/2-boton10.getWidth()-10, boton10.getY()+30, boton10.getWidth(), boton10.getHeight());
        botonOut.setVisible(true);
        panelIngresar.add(botonOut);

        labelIngresar.setText("Selecciona el pedido a entregar");
        labelIngresar.setLocation(ventana.getWidth()/2-labelIngresar.getWidth()/2, 20);
        labelIngresar.setVisible(true);

        JComboBox menuPedidos = new JComboBox();
        menuPedidos.setName("comboboxMenu");
        menuPedidos.setBounds(ventana.getWidth()/2-150, 120,300,100);

        for(Pedido pedidoAux : this.pedidos){
            if(!pedidoAux.isEntregado()){
                menuPedidos.addItem("PEDIDO N°"+pedidoAux.getnPedido());
            }
        }

        menuPedidos.setVisible(true);

        panelIngresar.add(menuPedidos);
        panelIngresar.setVisible(true);

        ventana.add(panelIngresar);

        final ArrayList<Pedido> pedidos = (ArrayList<Pedido>) this.pedidos.clone();
        botonOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.remove(panelIngresar);
                panelIngresar.setVisible(false);
                cleanPanel(panelIngresar, new Component[]{labelIngresar, textField});
                ventana.add(panelMenu);
                panelMenu.setVisible(true);
            }
        });
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Pedido pedido : pedidos) {
                    if (pedido.getnPedido() == Integer.parseInt(menuPedidos.getSelectedItem().toString().substring(9))) {
                        pedido.setEntregado(true);
                        break;
                    }
                }
                ventana.remove(panelIngresar);
                panelIngresar.setVisible(false);

                labelFeedBack.setVisible(true);
                labelFeedBack.setText("El pedido se entrego correctamente");
                boton11.setVisible(true);
                panelFeedBack.add(boton11);

                panelFeedBack.setVisible(true);
                ventana.add(panelFeedBack);
                cleanPanel(panelIngresar, new Component[]{labelIngresar, textField});
            }
        });
    }

    public String proximoPedido() {
        String pedidoProx = "NO HAY PEDIDOS PENDIENTES";
        for (int i = 0; i <this.pedidos.size() ; i++) {
            if(!this.pedidos.get(i).isEntregado()){
                pedidoProx = ""+this.pedidos.get(i).getnPedido();
                break;
            }
        }
        return pedidoProx;
    }

    public boolean agregarPlato(String nombre, String precio, String imagen, String descripcion, String tiempoDemora){
        boolean ok = true;
        for (Plato plato: this.platos) {
            if (plato.getNombre().equals(nombre)){
                ok = false;
            }
        }
        if (ok) {
            this.platos.add(new Plato(nombre, Float.parseFloat(precio), new File(imagen), descripcion, tiempoDemora));
            /*agregar en mongo*/
        }
        else{
            JOptionPane.showMessageDialog(null, "El plato ya existe");
        }

        return ok;
    }

    public boolean agregarPlato(Plato newPlato){
        boolean ok = true;
        for (Plato plato: this.platos) {
            if (plato.getNombre().equals(newPlato.getNombre())){
                ok = false;
            }
        }
        if (ok) {
            this.platos.add(newPlato);
            /*agregar en mongo*/
        }

        return ok;
    }
    public void ocuparMesa(JFrame ventana, JPanel panelFeedBack, JPanel panelIngresar, JButton boton10, JButton boton11, JTextField textField, JLabel labelIngresar, JLabel labelFeedBack, boolean ocupar) {
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
    }

    public void agregarMesa(Mesa mesa){
        this.mesas.add(mesa);
    }

    public void crearMenuAgregados(JPanel panelAgregados, JFrame frameAgregados, Plato plato){
        cleanPanel(panelAgregados, new Component[]{});

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

        botonSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                crearMenuAgregados(panelAgregados, frameAgregados, plato);
            }
        });
        botonTipoAgregado.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cleanPanel(panelAgregados, new Component[]{});

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
                                /*agregar que se fije si hay una seccion con el mismo nombre*/
                                /*HashMap<String, Float> agregado=new HashMap<>();
                                HashMap<Boolean, HashMap<String, Float>> seccion = new HashMap<>();
                                seccion.put(ok, agregado);*/
                                plato.getAgregados().add(new TipoAgregados(textFieldNuevaSeccion.getText(), ok));
                                //plato.getAgregados().put(textFieldNuevaSeccion.getText(), seccion);
                                crearMenuAgregados(panelAgregados, frameAgregados, plato);
                            }
                        }
                    }
                });
            }
        });
        botonAgregado.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cleanPanel(panelAgregados, new Component[]{});

                panelAgregados.add(labelAgregados2);

                JLabel labelNuevaSeccion = new JLabel("Ingresa la seccion de agregados que desees");
                labelNuevaSeccion.setName("labelNuevaSeccion");
                labelNuevaSeccion.setVisible(true);
                labelNuevaSeccion.setFont(fuentes.get("Garamond"));
                labelNuevaSeccion.setBounds(panelAgregados.getWidth()/2-125, 200, 250, 50);
                panelAgregados.add(labelNuevaSeccion);

                JComboBox comboBoxImportancia = new JComboBox();
                comboBoxImportancia.setName("comboboxFechas");
                comboBoxImportancia.setBounds(panelAgregados.getWidth()/2-100, labelNuevaSeccion.getY() + labelNuevaSeccion.getHeight() + 2, 200, 20);
                comboBoxImportancia.setVisible(true);

                for(TipoAgregados agregados: plato.getAgregados()){
                    comboBoxImportancia.addItem(agregados.getNombre());
                }

                panelAgregados.add(comboBoxImportancia);

                JLabel labelNuevoAgregado = new JLabel("Ingresa el nuevo agregado, ej: bolognesa");
                labelNuevoAgregado.setName("labelNuevoAgregado");
                labelNuevoAgregado.setVisible(true);
                labelNuevoAgregado.setFont(fuentes.get("Garamond"));
                labelNuevoAgregado.setBounds(panelAgregados.getWidth()/2-150, comboBoxImportancia.getY()+comboBoxImportancia.getHeight()+20, 350, 50);
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
                labelNuevoAgregadoPrecio.setBounds(panelAgregados.getWidth()/2-150, textFieldNuevoAgregado.getY()+textFieldNuevoAgregado.getHeight()+20, 350, 50);
                panelAgregados.add(labelNuevoAgregadoPrecio);

                JTextField textFieldAgregadoPrecio = new JTextField();
                textFieldAgregadoPrecio.setSize(450, 40);
                textFieldAgregadoPrecio.setLocation(panelAgregados.getWidth() / 2 - 230, labelNuevoAgregadoPrecio.getY() + labelNuevoAgregadoPrecio.getHeight() + 2);
                textFieldAgregadoPrecio.setVisible(true);
                textFieldAgregadoPrecio.setName("textFieldAgregadoPrecio");
                panelAgregados.add(textFieldAgregadoPrecio);

                panelAgregados.add(botonSalir);

                JButton botonAgSeccion = new JButton("Agregar");
                botonAgSeccion.setBounds(panelAgregados.getWidth()/2+25, 550, 100, 50);
                botonAgSeccion.setVisible(true);
                panelAgregados.add(botonAgSeccion);

                botonAgSeccion.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(!comboBoxImportancia.getSelectedItem().equals("") && !textFieldNuevoAgregado.getText().equals("") && !textFieldNuevoAgregado.getText().equals("")){
                            try{
                                boolean ok=true;
                                Float.parseFloat(textFieldAgregadoPrecio.getText());
                                /*for(Map.Entry<String, HashMap<Boolean, HashMap<String, Float>>> seccion: plato.getAgregados().entrySet()){
                                    if(seccion.getKey().equals(comboBoxImportancia.getSelectedItem())){
                                        for(Map.Entry<Boolean, HashMap<String, Float>> sec: seccion.getValue().entrySet()){
                                            for(Map.Entry<String, Float> agregado : sec.getValue().entrySet() ){
                                                if(agregado.getKey().equals(textFieldNuevoAgregado.getText())){
                                                    ok=false;
                                                    JOptionPane.showMessageDialog(null, "Este agregado ya existe");
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }*/

                                for(TipoAgregados agregados : plato.getAgregados()){
                                    for (String agregado: agregados.getAgregados().keySet()){
                                        if (agregado.equals(textFieldNuevoAgregado.getText())){
                                            ok=false;
                                            JOptionPane.showMessageDialog(null, "Este agregado ya existe");
                                            break;
                                        }
                                    }
                                }

                                if (ok){
                                    /*for(Map.Entry<String, HashMap<Boolean, HashMap<String, Float>>> seccion: plato.getAgregados().entrySet()){
                                        if(seccion.getKey().equals(comboBoxImportancia.getSelectedItem())){
                                            for(Map.Entry<Boolean, HashMap<String, Float>> sec: seccion.getValue().entrySet()){
                                                sec.getValue().put(textFieldNuevoAgregado.getText(), Float.parseFloat(textFieldAgregadoPrecio.getText()));
                                                break;
                                            }
                                        }
                                    }*/
                                    for(TipoAgregados agregados: plato.getAgregados()){
                                        if(agregados.getNombre().equals(comboBoxImportancia.getSelectedItem())){
                                            agregados.getAgregados().put(textFieldNuevoAgregado.getText(), Float.parseFloat(textFieldAgregadoPrecio.getText()));
                                        }
                                    }

                                    cleanPanel(panelAgregados, new Component[]{});
                                    crearMenuAgregados(panelAgregados,frameAgregados, plato);
                                }
                            }
                            catch (NumberFormatException ex){
                                JOptionPane.showMessageDialog(null, "La sintaxis del precio es incorrecta, ej: 185.15");
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "El formulario esta incompleto");
                        }
                    }
                });
            }
        });

        botonEditTipoAgregado.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /*usar scrollbar, poner el nombre, precio, al lado un simbolo boton editar y por ultimo un boton delete*/
            }
        });

        botonEditAgregado.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
    }

    public void gestionarRestaurante(JFrame ventana, JPanel panelIngresar){

        JPanel panel = new JPanel();
        panel.setName("panelGR");
        panel.setSize(1350, 700);
        panel.setLayout(null);
        panel.setVisible(true);
        ventana.add(panel);

        JButton boton1 = new JButton("AÑADIR PLATO");
        boton1.setBounds(ventana.getWidth()/2-100, ventana.getHeight()/2-200, 200, 100);
        boton1.setVisible(true);
        panel.add(boton1);

        JButton boton2 = new JButton("EDITAR PLATO");
        boton2.setBounds(ventana.getWidth()/2-100, ventana.getHeight()/2-50, 200, 100);
        boton2.setVisible(true);
        panel.add(boton2);

        JButton boton3 = new JButton("VER PLATO");
        boton3.setBounds(ventana.getWidth()/2-100, ventana.getHeight()/2+100, 200, 100);
        boton3.setVisible(true);
        panel.add(boton3);

        panel.setVisible(true);
        ventana.add(panelIngresar);

        boton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cleanPanel(panel, new Component[]{});

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

                JLabel labelImagen = new JLabel("Imagen: ( ej: C:\\Users\\Restaurante\\Pancho.png )");
                labelImagen.setBounds(ventana.getWidth() / 2 - 350, textFieldPrecio.getY() + textFieldPrecio.getHeight() + 20, 700, 30);
                labelImagen.setVisible(true);
                labelImagen.setName("labelImagen");
                panel.add(labelImagen);

                JTextField textFieldImagen = new JTextField();
                textFieldImagen.setSize(700, 40);
                textFieldImagen.setLocation(ventana.getWidth() / 2 - 350, labelImagen.getY() + labelImagen.getHeight() + 2);
                textFieldImagen.setVisible(true);
                textFieldImagen.setName("textFieldImagen");
                panel.add(textFieldImagen);

                JLabel labelAgregados = new JLabel("El plato va a tener agregados?");
                labelAgregados.setBounds(ventana.getWidth() / 2 - 350, textFieldImagen.getY() + textFieldImagen.getHeight() + 20, 700, 30);
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

                JButton botonAgregar = new JButton("AGREGAR");
                botonAgregar.setBounds(ventana.getWidth() / 2 + 50, opciones.getY() + opciones.getHeight() + 30, 150, 50);
                botonAgregar.setVisible(true);
                panel.add(botonAgregar);

                JButton botonOut = new JButton("SALIR");
                botonOut.setBounds(ventana.getWidth() / 2 - 200, opciones.getY() + opciones.getHeight() + 30, 150, 50);
                botonOut.setVisible(true);
                panel.add(botonOut);

                botonOut.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ventana.remove(panel);

                        cleanPanel(panel, new Component[]{});
                        gestionarRestaurante(ventana, panelIngresar);
                    }
                });

                botonAgregar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        HashMap<String, String> datosNewPlato = new HashMap<>();
                        boolean ok = true;

                        if(textFieldTiempoDemora.getText().equals("")||textFieldPrecio.getText().equals("")||textFieldDescripcion.getText().equals("") || textFieldImagen.getText().equals("") || textFieldName.getText().equals("")){
                            ok = false;
                            JOptionPane.showMessageDialog(null, "Formulario incompleto");
                        }
                        else {
                            try{
                                float a = Float.parseFloat(textFieldPrecio.getText());
                                if(a<0){
                                    ok=false;
                                    JOptionPane.showMessageDialog(null, "El precio es menor a 0");
                                }
                            }catch (NumberFormatException ex){
                                ok = false;
                                JOptionPane.showMessageDialog(null, "La sintaxis del precio es incorrecta, ej: 200.0");
                            }
                            if(!new File(textFieldImagen.getText()).exists() || (!new File(textFieldImagen.getText()).getName().substring(new File(textFieldImagen.getText()).getName().length()-4).equals(".png")) && (!new File(textFieldImagen.getText()).getName().substring(new File(textFieldImagen.getText()).getName().length()-4).equals(".png")) && (!new File(textFieldImagen.getText()).getName().substring(new File(textFieldImagen.getText()).getName().length()-4).equals(".jpg"))){
                                ok = false;
                                JOptionPane.showMessageDialog(null, "El archivo no es una imagen o la ubicacion es erronea, las imagenes solo pueden ser png o jpg");
                            }
                        }

                        if(ok){
                            datosNewPlato.put("Nombre", textFieldName.getText());
                            datosNewPlato.put("Descripcion", textFieldDescripcion.getText());
                            datosNewPlato.put("TiempoDemora", textFieldTiempoDemora.getText());
                            datosNewPlato.put("Precio", textFieldPrecio.getText());
                            datosNewPlato.put("Imagen", textFieldImagen.getText());
                        }
                        if (ok && opciones.getSelectedItem().equals("SI")){

                            JFrame frameAgregados = new JFrame("AGREGADOS");
                            frameAgregados.setSize(500, 730);
                            frameAgregados.setLayout(null);
                            frameAgregados.setVisible(true);

                            JPanel panelAgregados = new JPanel();
                            panelAgregados.setName("menu");
                            panelAgregados.setSize(frameAgregados.getSize());
                            panelAgregados.setLayout(null);
                            panelAgregados.setVisible(true);

                            Plato plato = new Plato(datosNewPlato.get("Nombre"), Float.parseFloat(datosNewPlato.get("Precio")), new File(datosNewPlato.get("Imagen")), datosNewPlato.get("Descripcion"), datosNewPlato.get("TiempoDemora"));

                            crearMenuAgregados(panelAgregados, frameAgregados, plato);

                            frameAgregados.add(panelAgregados);

                            frameAgregados.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent event) {
                                    agregarPlato(plato);
                                }
                            });
                        }
                        else if(ok && !opciones.getSelectedItem().equals("SI")){
                            if(agregarPlato(datosNewPlato.get("Nombre"), datosNewPlato.get("Precio"), datosNewPlato.get("Imagen"), datosNewPlato.get("Descripcion"), datosNewPlato.get("TiempoDemora"))){
                                JOptionPane.showMessageDialog(null, "El plato se agrego correctamente");
                            }
                        }
                    }
                });
            }
        });
    }

    //public void crearPanelMenu(){}

    public static void main(String[] args) {
        Restaurante restaurante = new Restaurante("La cantina");

        fuentes.put("Times New Roman", new Font("Times New Roman", Font.BOLD, 40));
        fuentes.put("Garamond", new Font("Garamond", Font.BOLD, 15));

        /*restaurante.agregarPlato("Milanesa con puré de papas", 0.0f);
        restaurante.agregarPlato("Ravioles rellenos con carne", 0f);
        restaurante.agregarPlato("Pizza a la Piedra", 0f);
        restaurante.agregarPlato("Polenta con salsa Fileto", 0f);
        restaurante.agregarPlato("Arroz primavera", 0f);*/

        for (int i = 0; i < 6; i++) {
            restaurante.agregarMesa(new Mesa());
        }

        /*VENTANA*/
        JFrame ventana = new JFrame("RESTAURANTE");
        ventana.setSize(1350, 730);
        ventana.setLayout(null);
        ventana.setVisible(true);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*PANELES*/
        JPanel panelMenu = new JPanel();
        panelMenu.setName("menu");
        panelMenu.setSize(1350, 700);
        panelMenu.setLayout(null);
        panelMenu.setVisible(true);
        ventana.add(panelMenu);

        JPanel panelIngresar = new JPanel();
        panelIngresar.setSize(1400, 700);
        panelIngresar.setLayout(null);
        panelIngresar.setVisible(false);
        panelIngresar.setName("ingresar");

        JPanel panelFeedBack = new JPanel();
        panelFeedBack.setSize(1400, 700);
        panelFeedBack.setLayout(null);
        panelFeedBack.setVisible(false);
        panelFeedBack.setName("feedback");

        /*LABELs*/

        JLabel nombreSistema = new JLabel();
        nombreSistema.setText(restaurante.getNombre());
        nombreSistema.setSize(250, 50);
        nombreSistema.setLocation(ventana.getWidth() / 2 - nombreSistema.getWidth() / 2, 100);
        nombreSistema.setFont(fuentes.get("Times New Roman"));
        panelMenu.add(nombreSistema);

        JLabel labelIngresar = new JLabel();
        labelIngresar.setSize(500, 50);
        labelIngresar.setLocation(ventana.getWidth() / 2 - labelIngresar.getWidth() / 2, ventana.getHeight() / 2 - 50);
        labelIngresar.setVisible(false);
        labelIngresar.setName("labelIngresar");
        panelIngresar.add(labelIngresar);

        JLabel labelFeedBack = new JLabel();
        labelFeedBack.setSize(500, 50);
        labelFeedBack.setLocation(ventana.getWidth() / 2 - labelFeedBack.getWidth() / 2, ventana.getHeight() / 2 - 50);
        labelFeedBack.setVisible(false);
        labelFeedBack.setName("labelFeedBack");
        panelFeedBack.add(labelFeedBack);

        /*TEXTFIELDS*/

        JTextField textField = new JTextField();
        textField.setSize(500, 50);
        textField.setLocation(ventana.getWidth() / 2 - textField.getWidth() / 2, ventana.getHeight() / 2);
        textField.setVisible(false);
        textField.setName("textFieldIngresar");
        panelIngresar.add(textField);

        /*BOTONES*/

        JButton boton1 = new JButton("INGRESE LA MESA A OCUPAR");
        boton1.setLocation(150, 200);
        boton1.setSize(200, 50);
        boton1.setName("IngresarMesa");
        boton1.setVisible(true);
        panelMenu.add(boton1);

        JButton boton2 = new JButton("INGRESE LA MESA A DESOCUPAR");
        boton2.setSize(200, 50);
        boton2.setLocation((1200 / 2 - boton2.getWidth() / 2) + 100, 200);
        boton2.setName("DesocuparMesa");
        boton2.setVisible(true);
        panelMenu.add(boton2);

        JButton boton3 = new JButton("GESTIONAR MENU");
        boton3.setSize(200, 50);
        boton3.setLocation(1250 - boton3.getWidth(), 200);
        boton3.setVisible(true);
        boton3.setName("HacerPedido");
        panelMenu.add(boton3);

        JButton boton4 = new JButton("PLATO MAS PEDIDO");
        boton4.setSize(200, 50);
        boton4.setLocation(150, 50 + boton1.getHeight() + boton1.getY());
        boton4.setVisible(true);
        boton4.setName("MasPedido");
        panelMenu.add(boton4);

        JButton boton5 = new JButton("PLATO MENOS PEDIDO");
        boton5.setSize(200, 50);
        boton5.setLocation((1200 / 2 - boton2.getWidth() / 2) + 100, 50 + boton2.getHeight() + boton2.getY());
        boton5.setVisible(true);
        boton5.setName("MenosPedido");
        panelMenu.add(boton5);

        JButton boton6 = new JButton("MESA MAS OCUPADA");
        boton6.setSize(200, 50);
        boton6.setLocation(1250 - boton3.getWidth(), 50 + boton3.getHeight() + boton3.getY());
        boton6.setVisible(true);
        boton6.setName("MesaMasPedida");
        panelMenu.add(boton6);

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

        JButton boton10 = new JButton("INGRESAR");
        boton10.setSize(200, 50);
        boton10.setLocation(ventana.getWidth() / 2 - boton10.getWidth() / 2, ventana.getHeight() / 2 + textField.getHeight() + 50);
        boton10.setVisible(false);

        JButton boton11 = new JButton();
        boton11.setSize(200, 50);
        boton11.setLocation(ventana.getWidth() / 2 - boton10.getWidth() / 2, ventana.getHeight() / 2 + textField.getHeight() + 50);
        boton11.setVisible(false);
        boton11.setName("boton11");

        /* FUNCION OCUPAR*/
        boton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                restaurante.ocuparMesa(ventana, panelFeedBack, panelIngresar, boton10, boton11, textField, labelIngresar, labelFeedBack, true);

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
        });
        /*FUNCION DESOCUPAR*/
        boton2.addMouseListener(new MouseAdapter() {
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
        });
        /*GESTIONAR RESTAURANTE*/
        boton3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                restaurante.cleanPanel(panelIngresar, new Component[]{});

                ventana.remove(panelMenu);
                panelMenu.setVisible(false);

                restaurante.gestionarRestaurante(ventana, panelIngresar);
                /*boton11.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ventana.remove(panelFeedBack);
                        panelFeedBack.setVisible(false);
                        panelFeedBack.remove(boton11);
                        ventana.add(panelMenu);
                        panelMenu.setVisible(true);
                    }
                });*/
            }
        });
        boton4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                restaurante.cleanPanel(panelIngresar, new Component[]{labelIngresar, textField});
                restaurante.cleanPanel(panelFeedBack, new Component[]{labelFeedBack, boton11});
                ventana.remove(panelMenu);
                panelMenu.setVisible(false);

                restaurante.platoMasPedido(ventana, panelMenu, panelIngresar, boton11, labelFeedBack, true);

            }
        });
        boton5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ventana.remove(panelMenu);
                panelMenu.setVisible(false);

                restaurante.platoMasPedido(ventana, panelMenu, panelIngresar, boton11, labelFeedBack, false);

            }
        });
        boton6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /*restaurante.mesaMasOcupada(ventana, panelMenu,panelIngresar, panelMenu, boton11, "INGRESAR", labelFeedBack, labelIngresar, textField);

                ventana.remove(panelMenu);
                panelMenu.setVisible(false);*/
            }
        });
        boton7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                restaurante.cleanPanel(panelIngresar, new Component[]{labelIngresar, textField});

                ventana.remove(panelMenu);
                panelMenu.setVisible(false);

                restaurante.entregarPedido(ventana, panelMenu,panelFeedBack, panelIngresar, boton10, boton11, textField, labelIngresar, labelFeedBack);

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
        });
        boton8.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                ventana.remove(panelMenu);
                panelMenu.setVisible(false);

                labelFeedBack.setText("EL proximo pedido es: "+ restaurante.proximoPedido());
                panelFeedBack.add(boton11);
                labelFeedBack.setVisible(true);
                boton11.setText("SALIR");
                boton11.setVisible(true);

                panelFeedBack.setVisible(true);
                ventana.add(panelFeedBack);

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
        });
    }
}
 /*
    averiguar bien lo del modal porque si bien se puede hacer con Jdialog al no tener el boton para ocultar no se cargan los datos
    Tomás:
    -Desarrollar "editar plato" de 0
    -Desarrollar "Ver plato" de 0
    -Desarrollar la logica en añadir plato
        -Desarrollar "AGREGADOS" (hacer que sea un modal, que se bloquee la ventana donde se ingresan los datos hasta que se cierre "agregados")
            -Desarrollar "Editar tipo agregado"
            -Desarrollar "Editar agregado"
 */
//C:\Users\Familia Gimenez\Documents\GitHub\ProyectoFinal\Front-end\Images\ñoquis.jpg