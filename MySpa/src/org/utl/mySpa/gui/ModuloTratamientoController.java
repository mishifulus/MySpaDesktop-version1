
package org.utl.mySpa.gui;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.utl.mySpa.core.model.Tratamiento;

public class ModuloTratamientoController implements Initializable {

    @FXML
    private TableView<Tratamiento> tbTratamientos;

    @FXML
    private TableColumn<Tratamiento, Integer> colidTratamientos;

    @FXML
    private TableColumn<Tratamiento, String> colNombre;
    
    @FXML
    private TableColumn<Tratamiento, String> colDescripcion;
    
    @FXML
    private TableColumn<Tratamiento, Float> colCosto;
    
    @FXML
    private TableColumn<Tratamiento, Integer> colEstatus;
    
    @FXML
    private JFXTextField txtEstatus;
    
    @FXML
    private JFXTextField txtNombre;
    
    @FXML
    private JFXTextField txtDescripcion;
    
    @FXML
    private JFXTextField txtCosto;
    
    @FXML
    private JFXTextField txtBuscar;
    
    @FXML
    private Label txtidTratamiento;
    
    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnModificar;

    @FXML
    private JFXButton btnEliminar;
    
    Tratamiento tratamientoSeleccionado;
    
    Client client = ClientBuilder.newClient();
    
    ObservableList<Tratamiento> listaTratamientos;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaTratamientos = FXCollections.observableArrayList();
        
        this.colidTratamientos.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        this.colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));
        this.colEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        txtEstatus.setVisible(false);
        btnGuardar.setDisable(false);
        cargarTablaTratamiento();
    }
    
    @FXML
    public void guardarTratamiento(){
        
        try {
            
            Tratamiento tratamiento = new Tratamiento();
            
            tratamiento.setId(0);
            
            tratamiento.setNombre(txtNombre.getText());
            tratamiento.setDescripcion(txtDescripcion.getText());
            tratamiento.setCosto(Float.parseFloat(txtCosto.getText()));
            tratamiento.setEstatus(1);
            
            Gson objGS = new Gson();
            String t = objGS.toJson(tratamiento);
            
            String route = String.valueOf(t);
            try {
                route = URLEncoder.encode(route, "UTF-8").replace("+", "%20");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ModuloTratamientoController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String response = client.target("http://localhost:8084/MySpa/api")
                    .path("tratamiento")
                    .path("insert")
                    .queryParam("t", route)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(String.class);
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("OK");
                alert.setContentText("Registro insertado con éxito");
                alert.showAndWait();
            
            cargarTablaTratamiento();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void modificarTratamiento(){
        
        if(txtidTratamiento.getText() != ""){
            
            try{
            Tratamiento tratamiento = new Tratamiento();
            
            tratamiento.setId(Integer.parseInt(txtidTratamiento.getText()));
            tratamiento.setNombre(txtNombre.getText());
            tratamiento.setDescripcion(txtDescripcion.getText());
            tratamiento.setCosto(Float.parseFloat(txtCosto.getText()));
            tratamiento.setEstatus(1);
            
            Gson objGS = new Gson();
            String t = objGS.toJson(tratamiento);
            
            String route = String.valueOf(t);
            try {
                route = URLEncoder.encode(route, "UTF-8").replace("+", "%20");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ModuloTratamientoController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String response = client.target("http://localhost:8084/MySpa/api")
                    .path("tratamiento")
                    .path("update")
                    .queryParam("t", route)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(String.class);
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("OK");
                alert.setContentText("Registro modificado con éxito");
                alert.showAndWait();
            
            cargarTablaTratamiento();
            
            }catch(Exception e)
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
    public void eliminarTratamiento(){
        
        if (txtidTratamiento.getText() != "")
        {
           WebTarget target; //Porque el servicio está en la web
           String jsonRespuesta; //Para cambiar el json a una cadena
           String id = txtidTratamiento.getText();
           
           try {
                client = ClientBuilder.newClient(); //Preparamos a cliente para usarse
                String response = client.target("http://localhost:8084/MySpa/api/tratamiento/delete")
                        .queryParam("id", id)
                        .request(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .get(String.class);
                
                System.out.println(""+response);
                
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("OK");
                alert.setContentText("Registro eliminado con éxito");
                alert.showAndWait();
                
                cargarTablaTratamiento();
                
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
    public void buscarTratamiento(){
    
        String filter = txtBuscar.getText();
        limpiarCampos();
        try {
            
            String response = client.target("http://localhost:8084/MySpa/api/tratamiento/search")
                    .queryParam("filter", filter)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(String.class);
            
            Gson objGS = new Gson();
            Tratamiento[] tratamientosArreglo = objGS.fromJson(response, Tratamiento[].class);
            tbTratamientos.getItems().setAll(tratamientosArreglo);
            
            //limpiarCampos();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void cargarTablaTratamiento(){
        
        limpiarCampos();
        WebTarget target;
        String json;
        
        try {
            
            client = ClientBuilder.newClient();
            target = client.target("http://localhost:8084/MySpa/api").path("tratamiento").path("getAll").queryParam("estatus", "1");
            json = target.request(MediaType.APPLICATION_JSON).get(String.class);
            Gson objGS = new Gson();
            Tratamiento[] tratamientosArreglo = objGS.fromJson(json, Tratamiento[].class);

            listaTratamientos = FXCollections.observableArrayList(tratamientosArreglo);
            tbTratamientos.setItems(listaTratamientos);
            
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void verInactivos(){
        
        limpiarCampos();
        WebTarget target;
        String json;
        
        try {
            
            client = ClientBuilder.newClient();
            target = client.target("http://localhost:8084/MySpa/api").path("tratamiento").path("getAll").queryParam("estatus", "0");
            json = target.request(MediaType.APPLICATION_JSON).get(String.class);
            Gson objGS = new Gson();
            Tratamiento[] tratamientosArreglo = objGS.fromJson(json, Tratamiento[].class);

            listaTratamientos = FXCollections.observableArrayList(tratamientosArreglo);
            tbTratamientos.setItems(listaTratamientos);
            
            btnModificar.setDisable(true);
            btnEliminar.setDisable(true);
            btnGuardar.setDisable(true);
            txtEstatus.setVisible(true);
            
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void limpiarCampos(){
        
        txtidTratamiento.setText("");
        txtEstatus.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtCosto.setText("");
        txtBuscar.setText("");
        
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        txtEstatus.setVisible(false);
        btnGuardar.setDisable(false);
    }
    
    @FXML
    public void seleccionarTratamiento(){
    
        limpiarCampos();
        
        tratamientoSeleccionado = tbTratamientos.getSelectionModel().getSelectedItem();
        txtidTratamiento.setText(String.valueOf(tratamientoSeleccionado.getId()));
        String estatusBebe = String.valueOf(tratamientoSeleccionado.getEstatus());
        if (estatusBebe.equals("1"))
        {
            txtEstatus.setText("Activo");
        }
        else
        {
            txtEstatus.setText("Inactivo");
        }
        txtNombre.setText(tratamientoSeleccionado.getNombre());
        txtDescripcion.setText(tratamientoSeleccionado.getDescripcion());
        txtCosto.setText(String.valueOf(tratamientoSeleccionado.getCosto()));
        
        btnModificar.setDisable(false);
        btnEliminar.setDisable(false);
        btnGuardar.setDisable(true);
        txtEstatus.setVisible(true);
    }
}
