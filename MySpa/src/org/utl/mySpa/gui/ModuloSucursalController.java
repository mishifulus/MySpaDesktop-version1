
package org.utl.mySpa.gui;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.utl.mySpa.core.model.Sucursal;

public class ModuloSucursalController implements Initializable {

    @FXML
    private JFXTextField txtCodigoSucursal;

    @FXML
    private JFXTextField txtNombreSucursal;

    @FXML
    private JFXTextField txtDomicilioSucursal;

    @FXML
    private JFXTextField txtEstatusSucursal;

    @FXML
    private JFXTextField txtLatitudSucursal;

    @FXML
    private JFXTextField txtLongitudSucursal;

    @FXML
    private JFXButton btnGuardarSucursal;

    @FXML
    private JFXButton btnModificarSucursal;

    @FXML
    private JFXButton btnEliminarSucursal;

    @FXML
    private JFXTextField txtBuscar;

    @FXML
    private JFXButton btnBuscar;
    
    @FXML
    private TableView<Sucursal> tblDetalleSucursal;
    
    @FXML
    private TableColumn<Sucursal, Integer> colCodigo;

    @FXML
    private TableColumn<Sucursal, String> colNombre;

    @FXML
    private TableColumn<Sucursal, String> colDomicilio;

    @FXML
    private TableColumn<Sucursal, Double> colLatitud;

    @FXML
    private TableColumn<Sucursal, Double> colLongitud;

    @FXML
    private TableColumn<Sucursal, Integer> colEstatus;
    
    @FXML
    private JFXButton btnActivos;

    @FXML
    private JFXButton btnInactivos;
    
    //Definir nuestra estructura de datos que contendra todos los registros del modulo de sucursal
    ObservableList<Sucursal> listaSucursal;
    Sucursal sucursalSeleccionada;

    Client client;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaSucursal = FXCollections.observableArrayList();
        this.colCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colDomicilio.setCellValueFactory(new PropertyValueFactory<>("domicilio"));
        this.colLatitud.setCellValueFactory(new PropertyValueFactory<>("latitud"));
        this.colLongitud.setCellValueFactory(new PropertyValueFactory<>("longitud"));
        this.colEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        cargarTablaSucursales(1);
        txtCodigoSucursal.setEditable(false);
        txtEstatusSucursal.setEditable(false);
        btnModificarSucursal.setDisable(true);
        btnEliminarSucursal.setDisable(true);
    }
    
    @FXML
    private void cargarTablaSucursales(int estatus) 
    {
        limpiarCampos();
        WebTarget target;
        String jsonRespuesta;
        try
        {
            client = ClientBuilder.newClient();
            target = client.target("http://localhost:8084/MySpa/api").path("sucursal").path("getAll").queryParam("estatus", estatus);
            jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class);
            Gson objGS = new Gson();
            Sucursal[] sucursalArreglo = objGS.fromJson(jsonRespuesta, Sucursal[].class);
            listaSucursal = FXCollections.observableArrayList(sucursalArreglo);
            tblDetalleSucursal.setItems(listaSucursal);
        }catch(Exception ex)
        {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Datos Sucursal");
            alert.setHeaderText("¡Lo Sentimos!");
            alert.setContentText("Ocurrió un error inesperado");
            alert.showAndWait();
        }
    }
    
    @FXML
    public void buscarSucursal(ActionEvent event) 
    {
        WebTarget target;
        String jsonRespuesta;
        try
        {
            client = ClientBuilder.newClient();
            target = client.target("http://localhost:8084/MySpa/api").path("sucursal").path("search").queryParam("filter", txtBuscar.getText());
            jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class);
            if(jsonRespuesta != null)
            {
                Gson objGS = new Gson ();
                Sucursal[] sucursalArreglo = objGS.fromJson(jsonRespuesta, Sucursal[].class);
                listaSucursal = FXCollections.observableArrayList(sucursalArreglo);
                tblDetalleSucursal.setItems(listaSucursal);
            }
            if(jsonRespuesta.equalsIgnoreCase("[]"))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucursal");
                alert.setHeaderText("¡Lo Sentimos!");
                alert.setContentText("No se encontrarón coincidencias");
                alert.showAndWait();
            }
            if(jsonRespuesta.startsWith("{\"error\""))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Hubo un Error");
                alert.setContentText("Intentelo de nuevo más tarde");
                alert.show();
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    @FXML
    public void eliminarSucursal(ActionEvent event)
    {
        String idSucursal = txtCodigoSucursal.getText();
        
        if(idSucursal.length() > 0)
        {
            WebTarget target;
            String jsonRespuesta;
            client = ClientBuilder.newClient();
            target = client.target("http://localhost:8084/MySpa/api").path("sucursal").path("delete").queryParam("id", txtCodigoSucursal.getText());
            jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class);
            if(jsonRespuesta.startsWith("{\"result\""))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("¡Exito!");
                alert.setContentText("La eliminación resultó exitosa");
                alert.hide();
                alert.show();
                cargarTablaSucursales(1);
            }
            if(jsonRespuesta.startsWith("{\"error\""))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Hubo un Error");
                alert.setContentText("Intentelo de nuevo más tarde");
                alert.show();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Hubo un Error");
            alert.setContentText("Elija una sucursal para eliminar");
            alert.show();
        }
    }
    
    @FXML
    public void guardarSucursal(ActionEvent event)
    {
        WebTarget target;
        String jsonRespuesta;
        client = ClientBuilder.newClient();

        if(txtCodigoSucursal.getText() == null || txtCodigoSucursal.getText().trim().isEmpty() && txtNombreSucursal.getText().trim().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("¡Error!");
            alert.setContentText("No hay datos suficientes");
            alert.show();
            return;
        }
        
        if(txtCodigoSucursal.getText() == null || txtCodigoSucursal.getText().trim().isEmpty())
        {
            txtCodigoSucursal.setText("0");
        }
        Sucursal objS = new Sucursal();
        objS.setId(Integer.parseInt(txtCodigoSucursal.getText()));
        objS.setNombre(txtNombreSucursal.getText());
        objS.setDomicilio(txtDomicilioSucursal.getText());
        objS.setLatitud(Double.parseDouble(txtLatitudSucursal.getText()));
        objS.setLongitud(Double.parseDouble(txtLongitudSucursal.getText()));
        objS.setEstatus(1);

        Gson objGson = new Gson();
        String s = objGson.toJson(objS);

        String camino = String.valueOf(s);
        try {
            camino = URLEncoder.encode(camino, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        if(objS.getId() == 0)
        {
            target = client.target("http://localhost:8084/MySpa/api").path("sucursal").path("insert").queryParam("s", camino);
            jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class);

            if(jsonRespuesta.startsWith("{\"idGenerado\""))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("¡Exito!");
                alert.setContentText("La inserción resultó exitosa");
                alert.hide();
                alert.show();
                cargarTablaSucursales(1);
            }
            if(jsonRespuesta.startsWith("{\"error\""))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Hubo un Error");
                alert.setContentText("Intentelo de nuevo, más tarde");
                alert.show();
            }
        }
    }
    
    @FXML
    void modificarSucursal(ActionEvent event) 
    {
        WebTarget target;
        String jsonRespuesta;
        client = ClientBuilder.newClient();
        Sucursal objS = new Sucursal();
        objS.setId(Integer.parseInt(txtCodigoSucursal.getText()));
        objS.setNombre(txtNombreSucursal.getText());
        objS.setDomicilio(txtDomicilioSucursal.getText());
        objS.setLatitud(Double.parseDouble(txtLatitudSucursal.getText()));
        objS.setLongitud(Double.parseDouble(txtLongitudSucursal.getText()));
        objS.setEstatus(1);
        
        if(objS.getId() > 0)
        {
            Gson objGson = new Gson();
            String s = objGson.toJson(objS);

            String camino = String.valueOf(s);
            try {
                camino = URLEncoder.encode(camino, "UTF-8").replace("+", "%20");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }

            target = client.target("http://localhost:8084/MySpa/api").path("sucursal").path("update").queryParam("s", camino);
            jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class);
            if(jsonRespuesta.startsWith("{\"result\""))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("¡Exito!");
                alert.setContentText("La modificación resultó exitosa");
                alert.hide();
                alert.show();
                cargarTablaSucursales(1);
            }
            if(jsonRespuesta.startsWith("{\"error\""))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Hubo un Error");
                alert.setContentText("Intentelo de nuevo, más tarde");
                alert.show();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Hubo un Error");
            alert.setContentText("Elija una sucursal para modificar");
            alert.show();
        }
    }
    
    @FXML
    public void seleccionSucursal() 
    {
        limpiarCampos();
        btnModificarSucursal.setDisable(false);
        btnEliminarSucursal.setDisable(false);
        sucursalSeleccionada = tblDetalleSucursal.getSelectionModel().getSelectedItem();
        txtCodigoSucursal.setText(String.valueOf(sucursalSeleccionada.getId()));
        txtNombreSucursal.setText(sucursalSeleccionada.getNombre());
        txtDomicilioSucursal.setText(sucursalSeleccionada.getDomicilio());
        txtLatitudSucursal.setText(String.valueOf(sucursalSeleccionada.getLatitud()));
        txtLongitudSucursal.setText(String.valueOf(sucursalSeleccionada.getLongitud()));
        String estatusBebe = String.valueOf(sucursalSeleccionada.getEstatus());
        if (estatusBebe.equals("1"))
        {
            txtEstatusSucursal.setText("Activo");
        }
        else
        {
            txtEstatusSucursal.setText("Inactivo");
        }
    }
    
    @FXML
    public void limpiarCampos() 
    {
        btnModificarSucursal.setDisable(true);
        btnEliminarSucursal.setDisable(true);
        txtCodigoSucursal.setText("");
        txtNombreSucursal.setText("");
        txtDomicilioSucursal.setText("");
        txtLatitudSucursal.setText("");
        txtLongitudSucursal.setText("");
        txtEstatusSucursal.setText("");
    }
    
    @FXML
    public void verActivos(ActionEvent event) 
    {
        cargarTablaSucursales(1);
        btnGuardarSucursal.setDisable(false);
    }
    
    @FXML
    public void verInactivos(ActionEvent event) 
    {
        cargarTablaSucursales(0);
        btnModificarSucursal.setDisable(true);
        btnEliminarSucursal.setDisable(true);
        btnGuardarSucursal.setDisable(true);
    }
    
}
