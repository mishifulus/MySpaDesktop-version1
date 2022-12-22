/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.utl.mySpa.gui;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import org.utl.mySpa.core.model.Sala;
import org.utl.mySpa.core.model.Sucursal;

public class ModuloSalaController implements Initializable{

    @FXML
    private JFXTextField txtCodigo;

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtDescripcion;

    @FXML
    private JFXTextField txtRutaFoto;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXButton btnModificar;

    @FXML
    private TableView<Sala> tblDetalleSala;

    @FXML
    private TableColumn<Sala, Integer> colId;

    @FXML
    private TableColumn<Sala, String> colNombre;

    @FXML
    private TableColumn<Sala, String> colDescripcion;

    @FXML
    private TableColumn<Sala, String> colRutaFoto;

    @FXML
    private TableColumn<Sala, String> colSucursal;

    @FXML
    private TableColumn<Sala, Integer> colEstatus;

    @FXML
    private JFXButton btnEliminar;

    @FXML
    private JFXTextField txtEstatus;

    @FXML
    private JFXTextField txtBuscar;

    @FXML
    private JFXButton btnBuscar;

    @FXML
    private ImageView imgFoto;

    @FXML
    private JFXComboBox<Sucursal> cmbSucursal;

    @FXML
    private JFXButton btnVerActivos;

    @FXML
    private JFXButton btnVerInactivos;

    @FXML
    private JFXButton btnCargarFoto;

    Client client;
    Sala salaSeleccionada;
    ObservableList<Sala> listaSala;
    ObservableList<Sucursal> sucursales;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaSala = FXCollections.observableArrayList();
        sucursales = FXCollections.observableArrayList();
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
        this.colRutaFoto.setCellValueFactory(new PropertyValueFactory("rutaFoto"));
        this.colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
        this.colSucursal.setCellValueFactory(
                sucursal -> {
                    SimpleObjectProperty objSOP = new SimpleObjectProperty();
                    objSOP.setValue(sucursal.getValue().getSucursal().getNombre());
                    return objSOP;
                }
        );
        cargarTablaSalas(1);
        cargarSucursales();
        txtCodigo.setEditable(false);
        txtEstatus.setEditable(false);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
    }
    
    public void cargarTablaSalas(int estatus) {
        limpiarCampos();
        WebTarget target;
        String jsonRespuesta;
        try {
            client = ClientBuilder.newClient();
            target = client.target("http://localhost:8084/MySpa/api/").path("sala").path("getAll").queryParam("estatus", estatus);
            jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class);
            Gson objGS = new Gson();
            Sala[] salaArreglo = objGS.fromJson(jsonRespuesta, Sala[].class);
            listaSala = FXCollections.observableArrayList(salaArreglo);
            tblDetalleSala.setItems(listaSala);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Hubo un error al intentar establecer "
                    + "conexión con el servidor");
            alert.show();
        }
    }
    
    public void buscarSala() {
        WebTarget target;
        String jsonRespuesta;
        try {
            client = ClientBuilder.newClient();
            target = client.target("http://localhost:8084/MySpa/api/").path("sala").path("search").queryParam("filter", txtBuscar.getText());
            jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class);
            if (jsonRespuesta != null) {
                Gson objGS = new Gson();
                Sala[] salaArreglo = objGS.fromJson(jsonRespuesta, Sala[].class);
                listaSala = FXCollections.observableArrayList(salaArreglo);
                tblDetalleSala.setItems(listaSala);
            } else if (jsonRespuesta.equalsIgnoreCase("[]")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sala");
                alert.setHeaderText("¡Lo Sentimos!");
                alert.setContentText("No se encontrarón coincidencias");
                alert.showAndWait();
            } else if (jsonRespuesta.startsWith("{\"error\"")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Hubo un Error");
                alert.setContentText("Intentelo de nuevo más tarde");
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eliminarSala() {
        String idSala = txtCodigo.getText();

        if (idSala.length() > 0) {
            WebTarget target;
            String jsonRespuesta;
            client = ClientBuilder.newClient();
            target = client.target("http://localhost:8084/MySpa/api/").path("sala").path("delete").queryParam("id", txtCodigo.getText());
            jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class);
            if (jsonRespuesta.startsWith("{\"result\"")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("¡Exito!");
                alert.setContentText("La eliminación resultó exitosa");
                alert.hide();
                alert.show();
                limpiarCampos();
                cargarTablaSalas(1);
            } else if (jsonRespuesta.startsWith("{\"error\"")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Hubo un Error");
                alert.setContentText("Intentelo de nuevo más tarde");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Hubo un Error");
            alert.setContentText("Elija una sala para eliminar");
            alert.show();
        }
    }
    
    public void limpiarCampos() {
        txtBuscar.setText("");
        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtEstatus.setText("");
        txtNombre.setText("");
        txtRutaFoto.setText("");
        cmbSucursal.valueProperty().set(null);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
    }
    
    public void cargarSucursales() {
        WebTarget target;
        String jsonRespuesta;

        try {
            client = ClientBuilder.newClient();
            target = client.target("http://localhost:8084/MySpa/api/").path("sucursal").path("getAllSinFiltro");
            jsonRespuesta = target.request(MediaType.APPLICATION_JSON).get(String.class);
            Gson objGS = new Gson();
            Sucursal[] sucursalArreglo = objGS.fromJson(jsonRespuesta, Sucursal[].class);
            sucursales = FXCollections.observableArrayList(sucursalArreglo);
            cmbSucursal.setItems(sucursales);
        } catch (Exception e) {

        }
    }
    
    public void guardarSala() {
        WebTarget target;

        Sala sala = new Sala();
        Sucursal sucursal = cmbSucursal.getValue();

        sala.setSucursal(sucursal);
        sala.setId(0);
        sala.setNombre(txtNombre.getText());
        sala.setDescripcion(txtDescripcion.getText());
        sala.setEstatus(1);
        sala.setFoto("");
        sala.setRutaFoto("");

        Gson gson = new Gson();

        String salaString = gson.toJson(sala);
        target = client.target("http://localhost:8084/MySpa/api/sala/insert");
        Form map = new Form();
        map.param("sala", salaString);
        String response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(map, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
        System.out.println(response);
    }
    
    public void seleccionarSala() {
        limpiarCampos();
        btnModificar.setDisable(false);
        btnEliminar.setDisable(false);
        Sucursal s = null;

        salaSeleccionada = tblDetalleSala.getSelectionModel().getSelectedItem();
        txtCodigo.setText(String.valueOf(salaSeleccionada.getId()));
        txtNombre.setText(salaSeleccionada.getNombre());
        txtDescripcion.setText(salaSeleccionada.getDescripcion());
        txtEstatus.setText(String.valueOf(salaSeleccionada.getEstatus()));
        txtRutaFoto.setText(salaSeleccionada.getRutaFoto());
        cmbSucursal.getSelectionModel().select(salaSeleccionada.getSucursal().getId() - 1);
    }
    
    public void modificarSala() {
        WebTarget target;
        if (!"".equals(txtCodigo.getText())) {
            Sala sala = new Sala();
            Sucursal sucursal = cmbSucursal.getValue();
            sala.setSucursal(sucursal);
            sala.setId(Integer.parseInt(txtCodigo.getText()));
            sala.setNombre(txtNombre.getText());
            sala.setDescripcion(txtDescripcion.getText());
            sala.setEstatus(1);
            sala.setFoto("");
            sala.setRutaFoto("");

            Gson gson = new Gson();
            String sal = gson.toJson(sala);
            target = client.target("http://localhost:8084/MySpa/api/sala/update");
            Form map = new Form();
            map.param("sala", sal);
            String response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(map, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
            System.out.println(response);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sala modificada correctamente");
            alert.show();
            limpiarCampos();
            cargarTablaSalas(1);
        }
        else{
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Sala no encontrada, imposible modificar");
            alerta.show();
        }
    }
    
    public void cargarActivos() {
        cargarTablaSalas(1);
        btnGuardar.setDisable(false);
    }
    
    public void cargarInactivos() {
        cargarTablaSalas(0);
        btnModificar.setDisable(true);
        btnGuardar.setDisable(true);
    }
    
}
