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
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Restaurante {
    private HashSet<Mesa> mesas = new HashSet<>();
    private HashSet<Plato> platos = new HashSet<>();
    private ArrayList<Pedido> pedidos = new ArrayList<>();
    private ArrayList<Ocupacion> ocupaciones = new ArrayList<>();
    private String nombre;
    private File logo;
    private String direccion;
    public static SimpleDateFormat dateFormatSQL = new SimpleDateFormat("yyyy-MM-dd");
    public static HashMap<String, Font> fuentes = new HashMap<>();

    //GETTERS && SETTERS

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
    public ArrayList<Ocupacion> getOcupaciones() {
        return ocupaciones;
    }
    public void setOcupaciones(ArrayList<Ocupacion> ocupaciones) {
        this.ocupaciones = ocupaciones;
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
    public HashSet<Plato> getPlatos() {
        return platos;
    }
    public void setPlatos(HashSet<Plato> platos) {
        this.platos = platos;
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
                System.out.println("entre al ok=false, newPlato: "+newPlato.getNombre()+" "+newPlato.getAgregados().size());
                System.out.println("plato: "+plato.getNombre()+" "+plato.getAgregados().size());
                for(TipoAgregados agregado: newPlato.getAgregados()){
                    if(!plato.getAgregados().contains(agregado)){
                        plato.getAgregados().add(agregado);
                    }
                }
                break;
            }
        }
        if (ok) {
            this.platos.add(newPlato);
            /*agregar en mongo*/
        }

        return ok;
    }

    public void clickEditAgregados(JFrame frameAgregados, JPanel panelAgregados, JLabel labelAgregados2, Plato plato, JButton botonSalir){
        panelAgregados.setPreferredSize(new Dimension(530,730));

        panelAgregados.removeAll();
        frameAgregados.remove(panelAgregados);

        panelAgregados.add(labelAgregados2);

        JLabel labelExplicacion = new JLabel("<html><body> Si quieres cambiar el nombre de la seccion, reescribelo en el input y<br> preciona el lapiz. En caso de querer borrarlo, presiona la cruz <br></body></html>");
        labelExplicacion.setName("labelExplicacion");
        labelExplicacion.setVisible(true);
        labelExplicacion.setFont(fuentes.get("Garamond"));
        labelExplicacion.setBounds(panelAgregados.getWidth()/2-225, labelAgregados2.getY()+labelAgregados2.getHeight()+30, 450, 50);
        panelAgregados.add(labelExplicacion);

        int vueltas = 1;

        for(Plato platoActual: platos){
            if (platoActual.getNombre().equals(plato.getNombre())){
                if (plato.getAgregados().size()>platoActual.getAgregados().size()){
                    agregarPlato(plato);
                }
                if (platoActual.getAgregados().size()==0){
                    labelExplicacion.setText("No hay secciones que editar"+plato.getAgregados().size());
                    break;
                }
                else {
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

                        JButton botonEdit = new JButton(new ImageIcon("C:\\Users\\Familia Gimenez\\Documents\\GitHub\\ProyectoFinal\\APP\\src\\com\\company\\images\\pencil.png"));
                        botonEdit.setBounds(opciones.getX() + opciones.getWidth() + 1, textFieldSeccion.getY(), 50, 50);
                        botonEdit.setVisible(true);
                        panelAgregados.add(botonEdit);

                        JButton botonDelete = new JButton(new ImageIcon("C:\\Users\\Familia Gimenez\\Documents\\GitHub\\ProyectoFinal\\APP\\src\\com\\company\\images\\delete.png"));
                        botonDelete.setBounds(botonEdit.getX() + botonEdit.getWidth() + 1, botonEdit.getY(), 50, 50);
                        botonDelete.setVisible(true);
                        panelAgregados.add(botonDelete);

                        vueltas++;
                        panelAgregados.add(textFieldSeccion);

                        botonEdit.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                int coincidencias=0;

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
                                    clickEditAgregados(frameAgregados, panelAgregados, labelAgregados2, plato, botonSalir);
                                }
                                //platos.remove(plato);
                            }
                        });
                    }
                }
                break;
            }
        }
        botonSalir.setLocation(panelAgregados.getWidth()/2-botonSalir.getWidth()/2, panelAgregados.getComponent(panelAgregados.getComponents().length-1).getY()+panelAgregados.getComponent(panelAgregados.getComponents().length-1).getHeight()+50);
        panelAgregados.add(botonSalir);
        panelAgregados.setPreferredSize(new Dimension(530, botonSalir.getHeight()+botonSalir.getY()+50));
        JScrollPane scrollBar = new JScrollPane(panelAgregados, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//C:\Users\Familia Gimenez\Documents\Tomás\IACeh\tareas\tarea2 wps\Captura.png
        panelAgregados.setVisible(true);
        frameAgregados.add(scrollBar);
    }

    public void clickEditAgregado(JFrame frameAgregados, JPanel panelAgregados, JLabel labelAgregados2, Plato plato, JButton botonSalir) {
        panelAgregados.setPreferredSize(new Dimension(530,730));

        panelAgregados.removeAll();
        frameAgregados.remove(panelAgregados);

        panelAgregados.add(labelAgregados2);

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
        for (Plato platoActual : platos) {
            if (platoActual.getNombre().equals(plato.getNombre())) {
                if (plato.getAgregados().size() > platoActual.getAgregados().size()) {
                    agregarPlato(plato);
                }
                if (platoActual.getAgregados().size() == 0) {
                    labelExplicacion.setText("No hay agregados que editar" + plato.getAgregados().size());
                    break;
                } else {
                    for (TipoAgregados agregados : platoActual.getAgregados()) {
                        opciones.addItem(agregados.getNombre());
                    }
                    panelAgregados.add(opciones);

                    JButton buttonElegir = new JButton(new ImageIcon("C:\\Users\\Familia Gimenez\\Documents\\GitHub\\ProyectoFinal\\APP\\src\\com\\company\\images\\check.png"));
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

                                            JButton botonEdit = new JButton(new ImageIcon("C:\\Users\\Familia Gimenez\\Documents\\GitHub\\ProyectoFinal\\APP\\src\\com\\company\\images\\pencil.png"));
                                            botonEdit.setBounds(textFieldPrecio.getX() + textFieldPrecio.getWidth() + 1, textFieldPrecio.getY(), 50, 50);
                                            botonEdit.setVisible(true);
                                            panelAgregados.add(botonEdit);

                                            JButton botonDelete = new JButton(new ImageIcon("C:\\Users\\Familia Gimenez\\Documents\\GitHub\\ProyectoFinal\\APP\\src\\com\\company\\images\\delete.png"));
                                            botonDelete.setBounds(botonEdit.getX() + botonEdit.getWidth() + 1, textFieldPrecio.getY(), 50, 50);
                                            botonDelete.setVisible(true);
                                            panelAgregados.add(botonDelete);

                                            botonEdit.addMouseListener(new MouseAdapter() {
                                                @Override
                                                public void mouseClicked(MouseEvent e) {
                                                    if (textFieldNombre.getText().equals("") || textFieldPrecio.equals("")) {
                                                        JOptionPane.showMessageDialog(null, "formulario incompleto");
                                                    } else {
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
                                                        clickEditAgregados(frameAgregados, panelAgregados, labelAgregados2, plato, botonSalir);
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
        botonSalir.setLocation(panelAgregados.getWidth() / 2 - botonSalir.getWidth() / 2, panelAgregados.getComponent(panelAgregados.getComponents().length - 1).getY() + panelAgregados.getComponent(panelAgregados.getComponents().length - 1).getHeight() + 50);
        panelAgregados.add(botonSalir);
        panelAgregados.setPreferredSize(new Dimension(530, botonSalir.getHeight() + botonSalir.getY() + 50));
        JScrollPane scrollBar = new JScrollPane(panelAgregados, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//C:\Users\Familia Gimenez\Documents\Tomás\IACeh\tareas\tarea2 wps\Captura.png
        frameAgregados.add(scrollBar);
    }

    public void crearMenuAgregados(JPanel panelAgregados, JFrame frameAgregados, Plato plato){
        panelAgregados.removeAll();
        panelAgregados.setPreferredSize(new Dimension(530,730));
        //cleanPanel(panelAgregados, new Component[]{});

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
                panelAgregados.removeAll();
                //cleanPanel(panelAgregados, new Component[]{});

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
                                plato.getAgregados().add(new TipoAgregados(textFieldNuevaSeccion.getText(), ok));
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
                panelAgregados.removeAll();
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
                            try {
                                boolean ok = true;
                                float precio = Float.parseFloat(textFieldAgregadoPrecio.getText());
                                if (precio < 0) {
                                    JOptionPane.showMessageDialog(null, "El precio debe ser mayor a 0");
                                } else {
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
                                        //cleanPanel(panelAgregados, new Component[]{});
                                        crearMenuAgregados(panelAgregados, frameAgregados, plato);
                                    }
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
                clickEditAgregados(frameAgregados, panelAgregados, labelAgregados2, plato, botonSalir);
            }
        });

        botonEditAgregado.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickEditAgregado(frameAgregados, panelAgregados, labelAgregados2, plato, botonSalir);
            }
        });
    }
    public void editarPlato(JFrame ventana, JPanel panel){
        //ventana.remove(panel);
        //System.out.println(ventana.getContentPane().getComponents().length);

        /*while (ventana.getContentPane().getComponents().length>0){
            ventana.getContentPane().remove(ventana.getContentPane().getComponents().length-1);
        }*/

        panel.removeAll();

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
                ventana.remove(panel);
                gestionarRestaurante(ventana);
            }
        });

        if (platos.size()==0){
            labelExplicacion.setText("No hay platos que editar");
            botonSalir.setLocation(panel.getWidth() / 2 - botonSalir.getWidth() / 2, 500);
            ventana.add(panel);
            System.out.println(ventana.getContentPane().getComponents().length+"linea758");

        }else{
            System.out.println(ventana.getContentPane().getComponents().length);

            int vueltas=1;
            for (Plato platosAux: this.platos){
                JTextField jtxtNombre = new JTextField();
                jtxtNombre.setSize(250, 50);
                if (vueltas == 1) {
                    jtxtNombre.setLocation(85, labelExplicacion.getY() + labelExplicacion.getHeight() + 15);
                } else {
                    jtxtNombre.setLocation(85, Math.round((labelExplicacion.getY() + labelExplicacion.getHeight() + 15) * (vueltas / 1.8f)) + 85);
                }
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
                jtxtDemora.setText(platosAux.getTiempoDemora());
                jtxtDemora.setVisible(true);
                panel.add(jtxtDemora);

                JTextField jtxtImg = new JTextField();
                jtxtImg.setSize(120, 50);
                jtxtImg.setLocation(jtxtDemora.getX()+jtxtDemora.getWidth(), jtxtDemora.getY());
                jtxtImg.setText(platosAux.getImg().getPath());
                jtxtImg.setVisible(true);
                panel.add(jtxtImg);

                JButton botonAg = new JButton("Edit agregados");
                botonAg.setBounds(jtxtImg.getX() + jtxtImg.getWidth() + 1, jtxtDemora.getY(), 150, 50);
                botonAg.setVisible(true);
                panel.add(botonAg);

                JButton botonEdit = new JButton(new ImageIcon("C:\\Users\\Familia Gimenez\\Documents\\GitHub\\ProyectoFinal\\APP\\src\\com\\company\\images\\pencil.png"));
                botonEdit.setBounds(botonAg.getX() + botonAg.getWidth() + 1, botonAg.getY(), 50, 50);
                botonEdit.setVisible(true);
                panel.add(botonEdit);

                JButton botonDelete = new JButton(new ImageIcon("C:\\Users\\Familia Gimenez\\Documents\\GitHub\\ProyectoFinal\\APP\\src\\com\\company\\images\\delete.png"));
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
                                    for (Plato platoAux: platos){
                                        if (platoAux.getNombre().equals(jtxtNombre.getText())){
                                            coicidencias++;
                                        }
                                    }
                                    if (coicidencias>=2){
                                        JOptionPane.showMessageDialog(null, "El nombre del plato ya esta usado");
                                    }else{
                                        platosAux.setNombre(jtxtNombre.getText());
                                        platosAux.setDescripcion(jtxtDescripcion.getText());
                                        platosAux.setTiempoDemora(jtxtDemora.getText());
                                        platosAux.setPrecio(Float.parseFloat(jtxtPrecio.getText()));
                                        platosAux.setImg(new File(jtxtImg.getText()));
                                        JOptionPane.showMessageDialog(null, "Se cambio correctamente");
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
                        if (confirmD == JOptionPane.YES_OPTION) {
                            platos.remove(platosAux);
                            jtxtDemora.setVisible(false);
                            jtxtDescripcion.setVisible(false);
                            jtxtImg.setVisible(false);
                            jtxtNombre.setVisible(false);
                            jtxtPrecio.setVisible(false);
                            botonDelete.setVisible(false);
                            botonEdit.setVisible(false);
                            botonAg.setVisible(false);
                            ventana.remove(panel);
                            editarPlato(ventana, panel);
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
                        crearMenuAgregados(panelAgregados, frameAgregados,platosAux);
                    }
                });
            }
            /*botonSalir.setLocation(panel.getWidth() / 2 - botonSalir.getWidth() / 2, panel.getComponent(panel.getComponents().length - 1).getY() + panel.getComponent(panel.getComponents().length - 1).getHeight() + 50);
            panel.setPreferredSize(new Dimension(1350, botonSalir.getHeight() + botonSalir.getY() + 50));
            JScrollPane scrollBar = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            panel.setVisible(true);
            System.out.println(ventana.getContentPane().getComponents().length);
            ventana.add(scrollBar);*/
        }
    }
    public void gestionarRestaurante(JFrame ventana) {
        JPanel panel = new JPanel();
        panel.setName("panelGR");
        panel.setSize(1350, 700);
        panel.setLayout(null);
        panel.setVisible(false);
        ventana.add(panel);

        JButton boton1 = new JButton("AÑADIR PLATO");
        boton1.setBounds(ventana.getWidth() / 2 - 100, ventana.getHeight() / 2 - 300, 200, 100);
        boton1.setVisible(true);
        panel.add(boton1);

        JButton boton2 = new JButton("EDITAR PLATO");
        boton2.setBounds(ventana.getWidth() / 2 - 100, ventana.getHeight() / 2 - 125, 200, 100);
        boton2.setVisible(true);
        panel.add(boton2);

        JButton boton3 = new JButton("BORRAR MENU");
        boton3.setBounds(ventana.getWidth() / 2 - 100, ventana.getHeight() / 2 + 25, 200, 100);
        boton3.setVisible(true);
        panel.add(boton3);

        JButton salir = new JButton("Salir");
        salir.setBounds(ventana.getWidth() / 2 - 100, ventana.getHeight() / 2 + 200, 200, 100);
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
        boton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panel.removeAll();

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
                botonImagen.setBounds(textFieldImagen.getX()+textFieldImagen.getWidth(), textFieldImagen.getY(), 150, 40);
                botonImagen.setVisible(true);
                panel.add(botonImagen);

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

                botonImagen.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        textFieldImagen.setText(chooser("png", "png", "La imagen no es png").getPath());
                    }
                });

                botonOut.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ventana.remove(panel);

                        panel.removeAll();
                        //cleanPanel(panel, new Component[]{});
                        gestionarRestaurante(ventana);
                    }
                });

                botonAgregar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        HashMap<String, String> datosNewPlato = new HashMap<>();
                        boolean ok = true;

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
                            datosNewPlato.put("Nombre", textFieldName.getText());
                            datosNewPlato.put("Descripcion", textFieldDescripcion.getText());
                            datosNewPlato.put("TiempoDemora", textFieldTiempoDemora.getText());
                            datosNewPlato.put("Precio", textFieldPrecio.getText());
                            datosNewPlato.put("Imagen", textFieldImagen.getText());
                        }
                        if (ok && opciones.getSelectedItem().equals("SI")) {

                            JFrame frameAgregados = new JFrame("AGREGADOS");
                            frameAgregados.setSize(500, 730);
                            frameAgregados.setVisible(true);

                            JPanel panelAgregados = new JPanel();
                            panelAgregados.setName("menu");
                            panelAgregados.setSize(frameAgregados.getSize());
                            panelAgregados.setLayout(null);
                            panelAgregados.setVisible(true);

                            Plato plato = new Plato(datosNewPlato.get("Nombre"), Float.parseFloat(datosNewPlato.get("Precio")), new File(datosNewPlato.get("Imagen")), datosNewPlato.get("Descripcion"), datosNewPlato.get("TiempoDemora"));

                            crearMenuAgregados(panelAgregados, frameAgregados, plato);

                            frameAgregados.add(panelAgregados);

                            agregarPlato(datosNewPlato.get("Nombre"), datosNewPlato.get("Precio"), datosNewPlato.get("Imagen"), datosNewPlato.get("Descripcion"), datosNewPlato.get("TiempoDemora"));

                            frameAgregados.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent event) {
                                    agregarPlato(plato);
                                }
                            });
                        } else if (ok && !opciones.getSelectedItem().equals("SI")) {
                            if (agregarPlato(datosNewPlato.get("Nombre"), datosNewPlato.get("Precio"), datosNewPlato.get("Imagen"), datosNewPlato.get("Descripcion"), datosNewPlato.get("TiempoDemora"))) {
                                JOptionPane.showMessageDialog(null, "El plato se agrego correctamente");
                            }
                        }
                    }
                });
            }
        });
        boton2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                editarPlato(ventana, panel);
            }
        });
        boton3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int confirmD = JOptionPane.showConfirmDialog(null, "¿Estas seguro que quieres borrarlo? se borraran todos los platos del menu");
                if (confirmD== JOptionPane.YES_NO_OPTION){
                    platos.clear();
                    JOptionPane.showMessageDialog(null, "Se borraron todos los platos, la cantidad de platos es "+platos.size());
                }
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

        JButton agregar = new JButton("AGREGAR");
        agregar.setSize(200, 50);
        agregar.setLocation(ventana.getWidth() / 2 - 100, ventana.getHeight() - 400);
        agregar.setVisible(true);
        agregar.setName("agregar");

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

        JButton borrarM = new JButton("BORRAR MESA ESPECIFICA");
        borrarM.setLocation(150, 50 + agregarM.getHeight() + agregarM.getY());
        borrarM.setSize(200, 50);
        borrarM.setName("borrarM");
        borrarM.setVisible(true);

        JButton borrarNmesas = new JButton("BORRAR VARIAS MESAS");
        borrarNmesas.setLocation((1200 / 2 - ocuparM.getWidth() / 2) + 100, 50 + ocuparM.getHeight() + ocuparM.getY());
        borrarNmesas.setSize(200, 50);
        borrarNmesas.setName("borrarNmesas");
        borrarNmesas.setVisible(true);

        JButton Qrs = new JButton("QRS");
        Qrs.setLocation(1250 - desocuparM.getWidth(), 50 + desocuparM.getHeight() + desocuparM.getY());
        Qrs.setSize(200, 50);
        Qrs.setName("Qrs");
        Qrs.setVisible(true);

        JTextField textMesas = new JTextField();
        textMesas.setLocation(ventana.getWidth() / 2 - 250,200);
        textMesas.setSize(500, 50);
        textMesas.setName("nMesas");
        textMesas.setVisible(true);

        ventana.add(panelMesas);
        panelMesas.add(salir);
        panelMesas.add(agregarM);
        panelMesas.add(ocuparM);
        panelMesas.add(desocuparM);
        panelMesas.add(borrarM);
        panelMesas.add(borrarNmesas);
        panelMesas.add(Qrs);
        panelMesas.setVisible(true);

        agregarM.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                panelMesas.removeAll();
                panelMesas.add(textMesas);
                //panelMesas.add();
                panelMesas.add(agregar);
                panelMesas.add(salir);

                agregar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e){
                        if (esNumero(textMesas.getText())) {
                            int n = Integer.parseInt(textMesas.getText());
                            Mesa.agregarMesas(n,mesas);
                            panelMesas.removeAll();
                            ventana.add(panelMesas);
                            panelMesas.add(salir);
                            panelMesas.add(agregarM);
                            panelMesas.add(ocuparM);
                            panelMesas.add(desocuparM);
                            panelMesas.add(borrarM);
                            panelMesas.add(borrarNmesas);
                            panelMesas.add(Qrs);
                            panelMesas.setVisible(true);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Ingrese un Numero entero");
                        }
                    }
                });
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

        botonp2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logo = chooser("png", "Png", "Use una foto PNG");
                ImageIcon ft = new ImageIcon(logo.getPath());
                Icon ftito = new ImageIcon(ft.getImage().getScaledInstance(imgPerfil.getWidth(),imgPerfil.getHeight(),Image.SCALE_DEFAULT));
                imgPerfil.setText(null);
                imgPerfil.setIcon(ftito);
            }
        });

        botonp3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nombre= texto1.getText();
                System.out.println(nombre);
                System.out.println("Se guardo correctamente");
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

        JLabel nombreSistema = new JLabel("que pasa pa");
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

        JButton boton4 = new JButton("--");
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
        boton7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /*restaurante.cleanPanel(panelIngresar, new Component[]{labelIngresar, textField});

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
                });*/
            }
        });
        boton8.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
/*
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
                });*/
            }
        });
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
        //Perfil p1 = new Perfil();

        fuentes.put("Times New Roman", new Font("Times New Roman", Font.BOLD, 40));
        fuentes.put("Garamond", new Font("Garamond", Font.BOLD, 15));

        /*VENTANA*/
        JFrame ventana = new JFrame("RESTAURANTE");
        ventana.setSize(1350, 730);
        ventana.setLayout(null);
        ventana.setVisible(true);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*PANELES*/

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

        JButton boton10 = new JButton("INGRESAR");
        boton10.setSize(200, 50);
        boton10.setLocation(ventana.getWidth() / 2 - boton10.getWidth() / 2, ventana.getHeight() / 2 + textField.getHeight() + 50);
        boton10.setVisible(false);

        JButton boton11 = new JButton("SALIR");
        boton11.setSize(200, 50);
        boton11.setLocation(ventana.getWidth() / 2 - boton10.getWidth() / 2, ventana.getHeight() / 2 + textField.getHeight() + 50);
        boton11.setVisible(true);
        boton11.setName("boton11");

        restaurante.panelMenu(ventana);
    }
}
//editar tipo agregado error en salir (debe ser por el scrollbar)
// en editar plato hay que hacer que ande el scrollbar, esta comentado
//corregir boton salir cuando esta el scrollbar, imagino que tiene que ver con el preferredSize. Agregue preferredSize al principio de varias funciones, verificar que no se haya roto nada y ver si corrigio el error