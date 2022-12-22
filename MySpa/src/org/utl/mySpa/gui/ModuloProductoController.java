/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.utl.mySpa.gui;

import com.google.gson.Gson;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.utl.mySpa.core.model.Producto;

public class ModuloProductoController implements Initializable {

    @FXML
    private JFXButton btnBuscar;

    @FXML
    private JFXTextField txtIdProducto;

    @FXML
    private JFXTextField txtBuscar;

    @FXML
    private JFXButton btnVerInactivos;

    @FXML
    private JFXButton btnVerActivos;

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtPrecioUso;

    @FXML
    private JFXTextField txtMarca;

    @FXML
    private JFXTextField txtEstatus;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnModificar;

    @FXML
    private JFXButton btnEliminar;

    @FXML
    private TableView<Producto> tblProductos;

    @FXML
    private TableColumn<Producto, Integer> colIdProducto;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Float> colPrecioUso;

    @FXML
    private TableColumn<Producto, String> colMarca;

    @FXML
    private TableColumn<Producto, Integer> colEstatus;
    
    //Definir nuestra estructura de datos que contendrá todos lod registros del módulo PRODUCTO
    ObservableList<Producto> listaProductos;
    Producto productoSeleccionado;
    
    //Nos ayuda a conectarnos con el servicio
    Client client = ClientBuilder.newClient();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        //Inicializamos nuestra estructura para poder empezar a cargar elementos o items
        listaProductos = FXCollections.observableArrayList();
        
        this.colIdProducto.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        this.colPrecioUso.setCellValueFactory(new PropertyValueFactory<>("precioUso"));
        this.colEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
       
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        txtEstatus.setVisible(false);
        btnGuardar.setDisable(false);
        txtIdProducto.setVisible(false);
        cargarTablaProducto();
    }
    
    @FXML
    public void guardarProducto()
    {
        try
        {
            Producto producto = new Producto();
            producto.setId(0);
            producto.setNombre(txtNombre.getText());
            producto.setMarca(txtMarca.getText());
            producto.setPrecioUso(Float.parseFloat(txtPrecioUso.getText()));
            producto.setEstatus(1);
            
            Gson gson = new Gson();
            String p = gson.toJson(producto); //Convertir en cadena
            
            String codificacion = String.valueOf(p);
            try{
            codificacion = URLEncoder.encode(codificacion, "UTF-8").replace("+", "%20"); //Remplazar los espacios
            }catch (UnsupportedEncodingException ex)
            {
                Logger.getLogger(ModuloProductoController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String response = client.target("http://localhost:8084/MySpa/api")
                    .path("producto")
                    .path("insert")
                    .queryParam("p", codificacion)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(String.class);
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("OK");
                alert.setContentText("Registro insertado con éxito");
                alert.showAndWait();
            
            cargarTablaProducto();
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void cargarTablaProducto()
    {
        limpiarCampos();
        WebTarget target; //Porque el servicio está en la web
        String jsonRespuesta; //Para cambiar el json a una cadena
        
        try
        {
            client = ClientBuilder.newClient(); //Preparamos a cliente para usarse
            target = client.target("http://localhost:8084/MySpa/api") //Dirección de la API, peticiones individuales
                    .path("producto").path("getAll") //Clase y método
                    .queryParam("estatus","1"); //Parametros necesarios
            jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class); //Convertimos el json de respuesta en una cadena con la clase String
            Gson objGS = new Gson(); //Creamos objeto un objeto de gson para almacenar
            Producto[] productosArreglo = objGS.fromJson(jsonRespuesta, Producto[].class); //Arreglo de empleados con el formato de la clase empleado y la información que obtuvimos en respuesta
            
            //Como el Observable no es compatible con gson, primero vaciamos en ArrayList, luego alObservable
            listaProductos = FXCollections.observableArrayList(productosArreglo);
            tblProductos.setItems(listaProductos);
            
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void limpiarCampos()
    {
        txtIdProducto.setText("");
        txtNombre.setText("");
        txtMarca.setText("");
        txtPrecioUso.setText("");
        txtEstatus.setText("");
        txtBuscar.setText("");
        
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        txtEstatus.setVisible(false);
        btnGuardar.setDisable(false);
        txtIdProducto.setVisible(false);
    }
    
    @FXML
    public void seleccionarProducto()
    {
        limpiarCampos();
        productoSeleccionado = tblProductos.getSelectionModel().getSelectedItem();
        txtIdProducto.setText(String.valueOf(productoSeleccionado.getId()));
        txtNombre.setText(productoSeleccionado.getNombre());
        txtMarca.setText(productoSeleccionado.getMarca());
        txtPrecioUso.setText(String.valueOf(productoSeleccionado.getPrecioUso()));
        String estatusBebe = String.valueOf(productoSeleccionado.getEstatus());
        if (estatusBebe.equals("1"))
        {
            txtEstatus.setText("Activo");
        }
        else
        {
            txtEstatus.setText("Inactivo");
        }
        
        btnModificar.setDisable(false);
        btnEliminar.setDisable(false);
        btnGuardar.setDisable(true);
        txtEstatus.setVisible(true);
        txtIdProducto.setVisible(true);
    }
    
    @FXML
    public void modificarProducto()
    {
        if (txtIdProducto.getText() != "")
        {
            try {
                Producto producto = new Producto();
                producto.setId(Integer.parseInt(txtIdProducto.getText()));
                producto.setNombre(txtNombre.getText());
                producto.setMarca(txtMarca.getText());
                producto.setPrecioUso(Float.parseFloat(txtPrecioUso.getText()));
                producto.setEstatus(1);

                Gson gson = new Gson();
                String p = gson.toJson(producto); //Convertir en cadena

                String codificacion = String.valueOf(p);
                try {
                    codificacion = URLEncoder.encode(codificacion, "UTF-8").replace("+", "%20"); //Remplazar los espacios
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(ModuloProductoController.class.getName()).log(Level.SEVERE, null, ex);
                }

                String response = client.target("http://localhost:8084/MySpa/api")
                        .path("producto")
                        .path("update")
                        .queryParam("p", codificacion)
                        .request(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .get(String.class);
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("OK");
                alert.setContentText("Registro modificado con éxito");
                alert.showAndWait();

                cargarTablaProducto();

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
    
    @FXML
    public void eliminarProducto()
    {
        if (txtIdProducto.getText() != "")
        {
            WebTarget target; //Porque el servicio está en la web
            String jsonRespuesta; //Para cambiar el json a una cadena
            String id = txtIdProducto.getText();
            
            try
            {
                client = ClientBuilder.newClient(); //Preparamos a cliente para usarse
                target = client.target("http://localhost:8084/MySpa/api") //Dirección de la API, peticiones individuales
                        .path("producto").path("delete") //Clase y método
                        .queryParam("id",id); //Parametros necesarios
                jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class); //Convertimos el json de respuesta en una cadena con la clase String
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("OK");
                alert.setContentText("Registro eliminado con éxito");
                alert.showAndWait();
                
                cargarTablaProducto();
                
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
    
    @FXML
    public void buscarProducto()
    {
        WebTarget target; //Porque el servicio está en la web
        String jsonRespuesta; //Para cambiar el json a una cadena
        String filtro = txtBuscar.getText();
        limpiarCampos();
        try
        {
            client = ClientBuilder.newClient(); //Preparamos a cliente para usarse
            target = client.target("http://localhost:8084/MySpa/api") //Dirección de la API, peticiones individuales
                    .path("producto").path("search") //Clase y método
                    .queryParam("filter",filtro); //Parametros necesarios
            jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class); //Convertimos el json de respuesta en una cadena con la clase String
            Gson objGS = new Gson(); //Creamos objeto un objeto de gson para almacenar
            Producto[] productosArreglo = objGS.fromJson(jsonRespuesta, Producto[].class); //Arreglo de empleados con el formato de la clase empleado y la información que obtuvimos en respuesta
            
            //Como el Observable no es compatible con gson, primero vaciamos en ArrayList, luego alObservable
            listaProductos = FXCollections.observableArrayList(productosArreglo);
            tblProductos.setItems(listaProductos);
            
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void verInactivos()
    {
        limpiarCampos();
        WebTarget target; //Porque el servicio está en la web
        String jsonRespuesta; //Para cambiar el json a una cadena
        
        try
        {
            client = ClientBuilder.newClient(); //Preparamos a cliente para usarse
            target = client.target("http://localhost:8084/MySpa/api") //Dirección de la API, peticiones individuales
                    .path("producto").path("getAll") //Clase y método
                    .queryParam("estatus","0"); //Parametros necesarios
            jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class); //Convertimos el json de respuesta en una cadena con la clase String
            Gson objGS = new Gson(); //Creamos objeto un objeto de gson para almacenar
            Producto[] productosArreglo = objGS.fromJson(jsonRespuesta, Producto[].class); //Arreglo de empleados con el formato de la clase empleado y la información que obtuvimos en respuesta
            
            //Como el Observable no es compatible con gson, primero vaciamos en ArrayList, luego alObservable
            listaProductos = FXCollections.observableArrayList(productosArreglo);
            tblProductos.setItems(listaProductos);
            
            btnModificar.setDisable(true);
            btnEliminar.setDisable(true);
            btnGuardar.setDisable(true);
            txtEstatus.setVisible(true);
            txtIdProducto.setVisible(true);
            
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
} 