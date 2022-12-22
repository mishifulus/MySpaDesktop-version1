/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.utl.mySpa.gui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import org.utl.mySpa.core.model.Empleado;
import javafx.scene.image.ImageView;
//Para utilizar los servicios del cliente
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.utl.mySpa.core.model.Persona;
import org.utl.mySpa.core.model.Usuario;

/**
 *
 * @author marti
 */
public class ModuloEmpleadoController implements Initializable{

    @FXML
    private TableView<Empleado> tblEmpleados;

    @FXML
    private TableColumn<Empleado, Integer> colIdEmpleado;

    @FXML
    private TableColumn<Empleado, String> colNombreCompleto;

    @FXML
    private TableColumn<Empleado, String> colSexo;

    @FXML
    private TableColumn<Empleado, String> colRFC;

    @FXML
    private TableColumn<Empleado, String> colTelefono;

    @FXML
    private TableColumn<Empleado, String> colDomicilio;

    @FXML
    private TableColumn<Empleado, String> colPuesto;

    @FXML
    private TableColumn<Empleado, Integer> colEstatus;

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtApellidoP;

    @FXML
    private JFXTextField txtApellidoM;

    @FXML
    private JFXTextField txtRFC;

    @FXML
    private JFXTextField txtTelefono;

    @FXML
    private JFXTextField txtDomicilio;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXComboBox<String> cmbGenero;

    @FXML
    private JFXButton btnEliminar;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXTextField txtUsuario;

    @FXML
    private JFXTextField txtBuscar;

    @FXML
    private JFXButton btnBuscar;

    @FXML
    private JFXButton btnModificar;

    @FXML
    private JFXTextField txtEstatus;

    @FXML
    private JFXTextField txtPuesto;

    @FXML
    private JFXTextField txtRutaFoto;

    @FXML
    private Label txtIdEmpleado;
    
    @FXML
    private Label txtIdPersona;
    
    @FXML
    private Label txtIdUsuario;

    @FXML
    private ImageView imgEmpleado;

    @FXML
    private JFXButton btnCargar;

    @FXML
    private JFXButton btnVerInactivos;

    @FXML
    private JFXButton btnVerActivos;
    
    @FXML
    private JFXTextField txtNumEmpleado;
    
    //Definir nuestra estructura de datos que contendrá todos lod registros del módulo EMPLEADO
    ObservableList<Empleado> listaEmpleados;
    
    ObservableList<String> sexo = FXCollections.observableArrayList("Hombre", "Mujer", "Otro");
    
    Empleado empleadoSeleccionado;
    
    //Nos ayuda a conectarnos con el servicio
    Client client = ClientBuilder.newClient();
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        //Inicializamos nuestra estructura para poder empezar a cargar elementos o items
        listaEmpleados = FXCollections.observableArrayList();
        
        this.colIdEmpleado.setCellValueFactory(new PropertyValueFactory<>("id")); //Tiene que coincidir con el modelo
        this.colNombreCompleto.setCellValueFactory(
                nombre
                -> { //Operador lambda de tipo Tarjet(apuntador), separa la declaración de parametros de la función que usaremos para encontrar los valores ANTES
                    SimpleObjectProperty objSOP = new SimpleObjectProperty<>();//Cuerpo lambda //Objetos EXCLUSIVOS de javaFX //Envuelve atributos
                    objSOP.setValue(nombre.getValue().getPersona().getNombre()+
                            " " + nombre.getValue().getPersona().getApellidoP() +
                            " " + nombre.getValue().getPersona().getApellidoM());
                    return objSOP; //Envuelve los datos para que el lambda pueda mandarlos como parametros
                }
        );
        this.colSexo.setCellValueFactory(
                genero
                -> {
                    SimpleObjectProperty objSOP = new SimpleObjectProperty<>();
                    objSOP.setValue(genero.getValue().getPersona().getGenero());
                    return objSOP;
                });
        this.colRFC.setCellValueFactory(
                rfc
                -> {
                    SimpleObjectProperty objSOP = new SimpleObjectProperty<>();
                    objSOP.setValue(rfc.getValue().getPersona().getRfc());
                    return objSOP;
                });
        this.colTelefono.setCellValueFactory(
                telefono
                -> {
                    SimpleObjectProperty objSOP = new SimpleObjectProperty<>();
                    objSOP.setValue(telefono.getValue().getPersona().getTelefono());
                    return objSOP;
                });
        this.colDomicilio.setCellValueFactory(
                domicilio
                -> {
                    SimpleObjectProperty objSOP = new SimpleObjectProperty<>();
                    objSOP.setValue(domicilio.getValue().getPersona().getDomicilio());
                    return objSOP;
                });
        this.colPuesto.setCellValueFactory(new PropertyValueFactory<>("puesto"));
        this.colEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        btnGuardar.setDisable(false);
        txtEstatus.setVisible(false);
        txtNumEmpleado.setVisible(false);
        cmbGenero.setItems(sexo);
        cargarTablaEmpleado();
        
    }
    
    public void guardarEmpleado()
    {
        WebTarget target; //Porque el servicio está en la web
        
        try
        {
            Empleado empleado = new Empleado();
            Persona persona = new Persona();
            Usuario usuario = new Usuario();
            
            empleado.setId(0);
            persona.setId(0);
            usuario.setId(0);
            
            persona.setNombre(txtNombre.getText());
            persona.setApellidoP(txtApellidoP.getText());
            persona.setApellidoM(txtApellidoM.getText());
            if(cmbGenero.getValue().equals("Mujer"))
            {
                persona.setGenero("F");
            }
            else if (cmbGenero.getValue().equals("Hombre"))
            {
                persona.setGenero("M");
            }
            else
            {
                persona.setGenero("O");
            }
            persona.setDomicilio(txtDomicilio.getText());
            persona.setTelefono(txtTelefono.getText());
            persona.setRfc(txtRFC.getText());

            usuario.setNombreUsu(txtUsuario.getText());
            usuario.setContrasenia(txtPassword.getText());
            usuario.setRol(txtPuesto.getText());
            
            empleado.setEstatus(1);
            empleado.setFoto("");
            empleado.setRutaFoto(txtRutaFoto.getText());
            empleado.setPuesto(txtPuesto.getText());

            empleado.setPersona(persona);
            empleado.setUsuario(usuario);
            empleado.setNumEmpleado("");
            
            Gson gson = new Gson();
            
            String e = gson.toJson(empleado);
            target = client.target("http://localhost:8084/MySpa/api")
                    .path("empleado").path("insert"); //Clase y método
            Form map = new Form();
            map.param("empleado", e);
            
            String response = target.request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(map, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("OK");
                alert.setContentText("Registro agregado con éxito");
                alert.showAndWait();
            
            cargarTablaEmpleado();
            
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    public void cargarTablaEmpleado()
    {
        limpiarCampos();
        WebTarget target; //Porque el servicio está en la web
        String jsonRespuesta; //Para cambiar el json a una cadena
        
        try
        {
            limpiarCampos();
            
            client = ClientBuilder.newClient(); //Preparamos a cliente para usarse
            target = client.target("http://localhost:8084/MySpa/api") //Dirección de la API, peticiones individuales
                    .path("empleado").path("getAll") //Clase y método
                    .queryParam("e","1"); //Parametros necesarios
            jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class); //Convertimos el json de respuesta en una cadena con la clase String
            Gson objGS = new Gson(); //Creamos objeto un objeto de gson para almacenar
            Empleado[] empleadosArreglo = objGS.fromJson(jsonRespuesta, Empleado[].class); //Arreglo de empleados con el formato de la clase empleado y la información que obtuvimos en respuesta
            
            //Como el Observable no es compatible con gson, primero vaciamos en ArrayList, luego alObservable
            listaEmpleados = FXCollections.observableArrayList(empleadosArreglo);
            tblEmpleados.setItems(listaEmpleados);
            
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void limpiarCampos()
    {
        txtApellidoM.setText("");
        txtApellidoP.setText("");
        txtBuscar.setText("");
        txtDomicilio.setText("");
        txtEstatus.setText("");
        txtNombre.setText("");
        txtPassword.setText("");
        txtPuesto.setText("");
        txtRFC.setText("");
        txtRutaFoto.setText("");
        txtTelefono.setText("");
        txtUsuario.setText("");
        cmbGenero.valueProperty().set(null);
        txtIdEmpleado.setText("");
        txtIdPersona.setText("");
        txtIdUsuario.setText("");
        txtNumEmpleado.setText("");
        Image image = new Image("/org/utl/mySpa/gui/resources/empleado.png");
        imgEmpleado.setImage(image);
        
        btnModificar.setDisable(true);
        btnGuardar.setDisable(false);
        btnEliminar.setDisable(true);
        txtEstatus.setVisible(false);
        txtNumEmpleado.setVisible(false);
    }
    
    public void seleccionarEmpleado()
    {
        limpiarCampos();
        empleadoSeleccionado = tblEmpleados.getSelectionModel().getSelectedItem();
        txtIdEmpleado.setText(String.valueOf(empleadoSeleccionado.getId()));
        txtIdPersona.setText(String.valueOf(empleadoSeleccionado.getPersona().getId()));
        txtIdUsuario.setText(String.valueOf(empleadoSeleccionado.getUsuario().getId()));
        txtNumEmpleado.setText(empleadoSeleccionado.getNumEmpleado());
        txtPuesto.setText(empleadoSeleccionado.getPuesto());
        txtRutaFoto.setText(empleadoSeleccionado.getRutaFoto());
        String estatusBebe = String.valueOf(empleadoSeleccionado.getEstatus());
        if (estatusBebe.equals("1"))
        {
            txtEstatus.setText("Activo");
        }
        else
        {
            txtEstatus.setText("Inactivo");
        }
        txtApellidoM.setText(empleadoSeleccionado.getPersona().getApellidoM());
        txtApellidoP.setText(empleadoSeleccionado.getPersona().getApellidoP());
        txtNombre.setText(empleadoSeleccionado.getPersona().getNombre());
        String generito = empleadoSeleccionado.getPersona().getGenero();
        if (generito.equals("F"))
        {
            cmbGenero.valueProperty().set("Mujer");
        }
        else if (generito.equals("M"))
        {
            cmbGenero.valueProperty().set("Hombre");
        }
        else
        {
            cmbGenero.valueProperty().set("Otro");
        }
        txtDomicilio.setText(empleadoSeleccionado.getPersona().getDomicilio());
        txtTelefono.setText(empleadoSeleccionado.getPersona().getTelefono());
        txtRFC.setText(empleadoSeleccionado.getPersona().getRfc());
        
        txtUsuario.setText(empleadoSeleccionado.getUsuario().getNombreUsu());
        txtPassword.setText(empleadoSeleccionado.getUsuario().getContrasenia());
        
        
        btnModificar.setDisable(false);
        btnEliminar.setDisable(false);
        btnGuardar.setDisable(true);
        txtEstatus.setVisible(true);
        txtNumEmpleado.setVisible(true);
        
        if (empleadoSeleccionado.getRutaFoto() != null || empleadoSeleccionado.getRutaFoto() != "")
        {
            Image image = new Image("file:" + empleadoSeleccionado.getRutaFoto());
            imgEmpleado.setImage(image);
        }
        
    }
    
    public void modificarEmpleado()
    {
        if (txtIdEmpleado.getText() != "")
        {
            WebTarget target; //Porque el servicio está en la web

            try {
                Empleado empleado = new Empleado();
                Persona persona = new Persona();
                Usuario usuario = new Usuario();
                
                empleado.setId(Integer.parseInt(txtIdEmpleado.getText()));
                persona.setId(Integer.parseInt(txtIdPersona.getText()));
                usuario.setId(Integer.parseInt(txtIdUsuario.getText()));

                persona.setNombre(txtNombre.getText());
                persona.setApellidoP(txtApellidoP.getText());
                persona.setApellidoM(txtApellidoM.getText());
                if (cmbGenero.getValue().equals("Mujer")) {
                    persona.setGenero("F");
                } else if (cmbGenero.getValue().equals("Hombre")) {
                    persona.setGenero("M");
                } else {
                    persona.setGenero("O");
                }
                persona.setDomicilio(txtDomicilio.getText());
                persona.setTelefono(txtTelefono.getText());
                persona.setRfc(txtRFC.getText());

                usuario.setNombreUsu(txtUsuario.getText());
                usuario.setContrasenia(txtPassword.getText());
                usuario.setRol(txtPuesto.getText());

                empleado.setEstatus(1);
                empleado.setFoto("");
                empleado.setRutaFoto(txtRutaFoto.getText());
                empleado.setPuesto(txtPuesto.getText());

                empleado.setPersona(persona);
                empleado.setUsuario(usuario);
                empleado.setNumEmpleado(txtNumEmpleado.getText());

                Gson gson = new Gson();

                String e = gson.toJson(empleado);
                target = client.target("http://localhost:8084/MySpa/api")
                        .path("empleado").path("update"); //Clase y método
                Form map = new Form();
                map.param("empleado", e);

                String response = target.request(MediaType.APPLICATION_JSON)
                        .post(Entity.entity(map, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("OK");
                alert.setContentText("Registro modificado con éxito");
                alert.showAndWait();

                cargarTablaEmpleado();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("Seleccione un registro de la tabla");
                alert.showAndWait();
        }
    }
    
    public void eliminarEmpleado()
    {
        if (txtIdEmpleado.getText() != "")
        {
            WebTarget target; //Porque el servicio está en la web
            String jsonRespuesta; //Para cambiar el json a una cadena
            String id = txtIdEmpleado.getText();
            
            try
            {
                client = ClientBuilder.newClient(); //Preparamos a cliente para usarse
                target = client.target("http://localhost:8084/MySpa/api") //Dirección de la API, peticiones individuales
                        .path("empleado").path("delete") //Clase y método
                        .queryParam("id",id); //Parametros necesarios
                jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class); //Convertimos el json de respuesta en una cadena con la clase String
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("OK");
                alert.setContentText("Registro eliminado con éxito");
                alert.showAndWait();
                
                cargarTablaEmpleado();
                
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("Seleccione un registro de la tabla");
                alert.showAndWait();
        }
    }
    
    public void buscarEmpleado()
    {
        WebTarget target; //Porque el servicio está en la web
        String jsonRespuesta; //Para cambiar el json a una cadena
        String filtro = txtBuscar.getText();
        limpiarCampos();
        
        try
        {
            client = ClientBuilder.newClient(); //Preparamos a cliente para usarse
            target = client.target("http://localhost:8084/MySpa/api") //Dirección de la API, peticiones individuales
                    .path("empleado").path("search") //Clase y método
                    .queryParam("filter",filtro); //Parametros necesarios
            jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class); //Convertimos el json de respuesta en una cadena con la clase String
            Gson objGS = new Gson(); //Creamos objeto un objeto de gson para almacenar
            Empleado[] empleadosArreglo = objGS.fromJson(jsonRespuesta, Empleado[].class); //Arreglo de empleados con el formato de la clase empleado y la información que obtuvimos en respuesta
            
            //Como el Observable no es compatible con gson, primero vaciamos en ArrayList, luego alObservable
            listaEmpleados = FXCollections.observableArrayList(empleadosArreglo);
            tblEmpleados.setItems(listaEmpleados);
            
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void verInactivos()
    {
        limpiarCampos();
        WebTarget target; //Porque el servicio está en la web
        String jsonRespuesta; //Para cambiar el json a una cadena
        
        try
        {
            client = ClientBuilder.newClient(); //Preparamos a cliente para usarse
            target = client.target("http://localhost:8084/MySpa/api") //Dirección de la API, peticiones individuales
                    .path("empleado").path("getAll") //Clase y método
                    .queryParam("e","0"); //Parametros necesarios
            jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class); //Convertimos el json de respuesta en una cadena con la clase String
            Gson objGS = new Gson(); //Creamos objeto un objeto de gson para almacenar
            Empleado[] empleadosArreglo = objGS.fromJson(jsonRespuesta, Empleado[].class); //Arreglo de empleados con el formato de la clase empleado y la información que obtuvimos en respuesta
            
            //Como el Observable no es compatible con gson, primero vaciamos en ArrayList, luego alObservable
            listaEmpleados = FXCollections.observableArrayList(empleadosArreglo);
            tblEmpleados.setItems(listaEmpleados);
            
            btnModificar.setDisable(true);
            btnEliminar.setDisable(true);
            btnGuardar.setDisable(true);
            txtEstatus.setVisible(true);
            txtNumEmpleado.setVisible(true);
            
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
