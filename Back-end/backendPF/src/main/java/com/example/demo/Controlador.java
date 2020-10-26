package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.util.JSONParseException;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class Controlador {

    @Autowired
    private AccesoMongoDB accesoMongoDB;

    @Autowired
    private Servicio servicioParaSubirArchivos;

    private File json;

    public Controlador(AccesoMongoDB accesoMongoDB) {
        this.json = new File(".src/main/resources/files/archivoJSON");
        this.accesoMongoDB = accesoMongoDB;
        this.acces = new AccesoMongoDB("proyectoFInal");
        this.servicioParaSubirArchivos = new Servicio();
    }

    @GetMapping("/")
    public String rest() {
        return "rest";
    }

    @PostMapping("procesar")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {

        try {
            procesarArchivo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String,Object> datos = this.obtenerInformacionSolicitada();
        return new ResponseEntity<>(datos, HttpStatus.OK);
    }

    public void procesarArchivo(MultipartFile archivo) throws IOException {
        servicioParaSubirArchivos.guardarArchivo(archivo);
        this.guardarDatosALaBase();
    }

    public void mandarPedidosAPI(int nPedido){
        Pedido pedido =  accesoMongoDB.obtenerPedido(nPedido);

        if (pedido.getnPedido()>0) {
            ArrayList<HashMap<String, Object>> platoJSONformat = accesoMongoDB.platosPedidoMONGO(pedido.getPlatos());
            File jsonPedido = new File(".src/main/resources/files/jsonPedido");

            try{
                ObjectMapper mapper = new ObjectMapper();

                mapper.writeValue(jsonPedido, platoJSONformat);
            }catch (JsonProcessingException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            System.out.println("No existe ese pedido");
        }


    }

    public List<Plato> obtenerListaDePedidos(){

        List<Pedido> pedidoList= new ArrayList<>();

        Iterator<String[]> iterador = archivoCSV.obtenerIterador();
    }

    public void guardarPedidos(){
        List<Socio> sociosList = this.obtenerListaDeSocios();
        accesoABaseDeDatos.insertarSocios(sociosList);
    }

    public HashMap<String,Long> obtenerEquipoConCantidadDeSocios(){

        HashMap<String,Long> equiposConCantidadDeSocios = new HashMap<>();
        HashSet<String> equipos = this.accesoABaseDeDatos.obtenerEquipos();

        for (String nombreDeEquipo : equipos) {
            long cantidadDeSocios = this.obtenerCantidadDeSocios(nombreDeEquipo);
            equiposConCantidadDeSocios.put(nombreDeEquipo,cantidadDeSocios);
        }
        System.out.println(equiposConCantidadDeSocios);
        return equiposConCantidadDeSocios;
    }

    public List<String> obtenerEquiposDeMayorAMenorSegunSocios(){

        HashMap<String,Long> equiposConSocios = this.obtenerEquipoConCantidadDeSocios();

        List<Long> cantidadesDeSocios = new ArrayList<>(equiposConSocios.values());
        Collections.sort(cantidadesDeSocios);

        List<String> nombresDeEquipos = new ArrayList<>();

        for (int i = cantidadesDeSocios.size() - 1; i >= 0; i--) {
            for (Map.Entry<String,Long> elemento : equiposConSocios.entrySet()){
                if (elemento.getValue() == cantidadesDeSocios.get(i)){
                    nombresDeEquipos.add(elemento.getKey());
                }
            }
        }

        System.out.println("equiposOrdenados" + nombresDeEquipos);
        return nombresDeEquipos;
    }

    public long obtenerCantidadTotalDeRegistros(){
        long cantidadTotal = accesoABaseDeDatos.obtenerCantidadDeDocumentos();
        return cantidadTotal;
    }

    public int obtenerCantidadDeSocios(String nombreDeEquipo){
        List<Socio> sociosDeEquipo = this.obtenerSociosDeEquipo(nombreDeEquipo);
        int cantidadDeSocios = sociosDeEquipo.size();
        return cantidadDeSocios;
    }

    public List<Socio> obtenerSociosDeEquipo(String nombreDeEquipo){

        HashMap<String,String> filtro = new HashMap<>();
        filtro.put("equipo",nombreDeEquipo);
        List<Socio> sociosDeEquipo = accesoABaseDeDatos.obtenerSociosCon(filtro);

        return sociosDeEquipo;
    }

    public int obtenerMenorEdadRegistradaPorEquipo(String nombreDeEquipo){
        List<Socio> socios = this.obtenerSociosDeEquipo(nombreDeEquipo);
        socios = Lista.ordenarMenorAMayorSegunEdad(socios);
        int menorEdadRegistrada = socios.get(0).getEdad();
        return menorEdadRegistrada;
    }

    public int obtenerMayorEdadRegistradaPorEquipo(String nombreDeEquipo){
        List<Socio> socios = this.obtenerSociosDeEquipo(nombreDeEquipo);
        socios = Lista.ordenarMayorAMenorSegunEdad(socios);
        int menorEdadRegistrada = socios.get(0).getEdad();
        return menorEdadRegistrada;
    }

    public float obtenerEdadPromedioDeSocios(String nombreDeEquipo){

        List<Socio> sociosDeEquipo = this.obtenerSociosDeEquipo(nombreDeEquipo);
        float edadPromedio = this.obtenerEdadPromedio(sociosDeEquipo);

        return edadPromedio;
    }

    public float obtenerEdadPromedio(List<Socio> socios){

        long cantidadDeSocios = socios.size();
        long sumaTotalDeEdades = 0;

        for (Socio socio : socios) {
            sumaTotalDeEdades += socio.getEdad();
        }

        float edadPromedio = sumaTotalDeEdades / cantidadDeSocios;
        return edadPromedio;
    }



    public List<HashMap<String,Object>> obtenerCienPrimerosSociosCasadosUniversitarios(){

        HashMap<String,String> requisitos = new HashMap<>();
        requisitos.put("estadoCivil","Casado");
        requisitos.put("nivelDeEstudios","Universitario");

        List<Socio> sociosBuscados = accesoABaseDeDatos.obtenerSociosCon(requisitos);
        sociosBuscados = Lista.ordenarMayorAMenorSegunEdad(sociosBuscados);

        List<Socio> cienPrimeros = new ArrayList<>();

        if (sociosBuscados.size() >=100 ) {
            cienPrimeros = sociosBuscados.subList(0, 100);
        } else {
            cienPrimeros = sociosBuscados;
        }

        List<HashMap<String,Object>> datos = new ArrayList<>();

        for (Socio socio : cienPrimeros) {
            HashMap<String,Object> datosDeSocio = new HashMap<>();
            datosDeSocio.put("nombre",socio.getNombre());
            datosDeSocio.put("edad",socio.getEdad());
            datosDeSocio.put("equipo",socio.getEquipo());
            datos.add(datosDeSocio);
        }

        return datos;
    }

    public List<String> obtenerCincoNombresMasComunes(String nombreDeEquipo){

        HashMap<String,String> requisitos = new HashMap<>();
        requisitos.put("equipo",nombreDeEquipo);

        List<Socio> sociosBuscados = accesoABaseDeDatos.obtenerSociosCon(requisitos);
        List<String> nombresDeSociosBuscados = Lista.obtenerNombresDeSocios(sociosBuscados);

        SortedMap aparicionesSegunNombre = new TreeMap(java.util.Collections.reverseOrder());

        for (String nombre : nombresDeSociosBuscados) {
            int apariciones = Collections.frequency(nombresDeSociosBuscados,nombre);
            aparicionesSegunNombre.put(nombre,apariciones);
        }

        nombresDeSociosBuscados = new ArrayList<>(aparicionesSegunNombre.keySet());
        nombresDeSociosBuscados = nombresDeSociosBuscados.subList(0,5);

        return nombresDeSociosBuscados;
    }

    public List<HashMap<String,Object>> obtenerInfoGeneralDeEquipos(){

        List<HashMap<String,Object>> equipos = new ArrayList<>();
        List<String> equiposRegistrados = this.obtenerEquiposDeMayorAMenorSegunSocios();

        for (String nombreEquipo : equiposRegistrados) {

            HashMap<String,Object> infoEquipo = new HashMap<>();
            infoEquipo.put("nombre",nombreEquipo);

            int cantidadDeSocios = this.obtenerCantidadDeSocios(nombreEquipo);
            infoEquipo.put("cantidadDeSocios",cantidadDeSocios);

            float edadPromedio = this.obtenerEdadPromedioDeSocios(nombreEquipo);
            infoEquipo.put("edadPromedio",edadPromedio);

            int menorEdadRegistrada = this.obtenerMenorEdadRegistradaPorEquipo(nombreEquipo);
            infoEquipo.put("menorEdadRegistrada",menorEdadRegistrada);

            int mayorEdadRegistrada = this.obtenerMayorEdadRegistradaPorEquipo(nombreEquipo);
            infoEquipo.put("mayorEdadRegistrada",mayorEdadRegistrada);

            equipos.add(infoEquipo);

        }

        return equipos;
    }

    public Map<String,Object> obtenerInformacionSolicitada(){

        Map<String,Object> informacion = new HashMap<>();

        informacion.put("cantidadTotalDePersonasRegistradas",this.obtenerCantidadTotalDeRegistros());
        informacion.put("edadPromedioDeSociosDeRacing",this.obtenerEdadPromedioDeSocios("Racing"));
        informacion.put("cienPrimerasPersonas",this.obtenerCienPrimerosSociosCasadosUniversitarios());
        informacion.put("cincoNombresMasComunesDeRiver",this.obtenerCincoNombresMasComunes("River"));
        informacion.put("listaDeEquiposSegunCantidadDeSocios",this.obtenerInfoGeneralDeEquipos());

        return informacion;
    }

}

