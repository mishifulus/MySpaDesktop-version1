
package org.utl.mySpa.gui;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import org.utl.mySpa.core.model.Persona;
import org.utl.mySpa.core.model.Usuario;
import org.utl.mySpa.core.model.Cliente;

public class ModuloClienteController implements Initializable{

    @FXML
    private JFXTextField txtNombre;

    @FXML
    private JFXTextField txtApeP;

    @FXML
    private JFXTextField txtEstatus;

    @FXML
    private JFXTextField txtApeM;

    @FXML
    private JFXTextField txtRFC;

    @FXML
    private JFXTextField txtTelefono;

    @FXML
    private JFXTextField txtCorreo;

    @FXML
    private JFXTextField txtDomicilio;

    @FXML
    private JFXTextField txtUsuario;

    @FXML
    private JFXPasswordField txtContrasenia;

    @FXML
    private JFXComboBox<String> cmbSexo;

    @FXML
    private JFXButton btnEliminar;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private JFXTextField txtBuscar;

    @FXML
    private JFXButton btnBuscar;

    @FXML
    private JFXButton btnModificar;
    @FXML
    private JFXButton btnInactivos;
    
    @FXML
    private JFXButton btnActivos;

    @FXML
    private JFXTextField txtIdCliente;

    @FXML
    private JFXTextField txtIdPersona;

    @FXML
    private JFXTextField txtIdUsuario;

    @FXML
    private JFXTextField txtNumCliente;

    @FXML
    private TableView<Cliente> tblDetalleClientes;

    @FXML
    private TableColumn<Cliente, String> colNombre;

    @FXML
    private TableColumn<Cliente, String> colTelefono;

    @FXML
    private TableColumn<Cliente, String> colIdCliente;

    @FXML
    private TableColumn<Cliente, String> colCorreo;

    @FXML
    private TableColumn<Cliente, String> colEstatus;

    @FXML
    private TableColumn<Cliente, String> colSexo;
    
    ObservableList<String> sexos = FXCollections.observableArrayList("Hombre", "Mujer", "Otro");
    Cliente clienteSeleccionado;
    WebTarget target;
    Client client = ClientBuilder.newClient();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.colIdCliente.setCellValueFactory(new PropertyValueFactory<>("numeroUni"));
        this.colNombre.setCellValueFactory(
                nombre -> {
                    SimpleObjectProperty objSOP = new SimpleObjectProperty();
                    objSOP.setValue(nombre.getValue().getPersona().getNombre()
                            + " " + nombre.getValue().getPersona().getApellidoP()
                            + " " + nombre.getValue().getPersona().getApellidoM());
                    return objSOP;
                }
        );
        this.colSexo.setCellValueFactory(
                genero
                -> {
            SimpleObjectProperty objSOP = new SimpleObjectProperty();
            objSOP.setValue(genero.getValue().getPersona().getGenero());
            return objSOP;
        });
        this.colTelefono.setCellValueFactory(
                telefono
                -> {
            SimpleObjectProperty objSOP = new SimpleObjectProperty();
            objSOP.setValue(telefono.getValue().getPersona().getTelefono());
            return objSOP;
        }
        );
        this.colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
        this.colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        cmbSexo.setItems(sexos);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        btnGuardar.setDisable(false);
        cargarTablaClientes();
    }
    
    public void cargarTablaClientes() {
        try {
            limpiarCampos();
            String response = client.target("http://localhost:8084/MySpa/api/cliente/getAll")
                    .queryParam("estatus", 1)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(String.class);
            Gson gson = new Gson();
            Cliente[] clientesArreglo = gson.fromJson(response, Cliente[].class);
            tblDetalleClientes.getItems().setAll(clientesArreglo);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Hubo un error al intentar estabelecer "
                    + "conexión con el servidor");
            alert.show();
        }
    }
    
    public void cargarTablaClientesIna() {
        try {
            limpiarCampos();
            String response = client.target("http://localhost:8084/MySpa/api/cliente/getAll")
                    .queryParam("estatus", 0)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(String.class);
            Gson gson = new Gson();
            Cliente[] clientesArreglo = gson.fromJson(response, Cliente[].class);
            tblDetalleClientes.getItems().setAll(clientesArreglo);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Hubo un error al intentar establecer "
                    + "conexión con el servidor");
            alert.show();
        }
        
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
        btnGuardar.setDisable(true);
    }
    
    public void limpiarCampos() {
        txtIdCliente.setText("");
        txtIdPersona.setText("");
        txtIdUsuario.setText("");
        txtNumCliente.setText("");
        txtCorreo.setText("");
        txtApeM.setText("");
        txtApeP.setText("");
        txtUsuario.setText("");
        txtTelefono.setText("");
        txtEstatus.setText("");
        txtRFC.setText("");
        txtNombre.setText("");
        txtContrasenia.setText("");
        txtDomicilio.setText("");
        cmbSexo.valueProperty().set(null);

        btnModificar.setDisable(true);
        btnGuardar.setDisable(false);
        btnEliminar.setDisable(true);
    }

    public void selecionarClientes() {
        limpiarCampos();
        clienteSeleccionado = tblDetalleClientes.getSelectionModel().getSelectedItem();
        txtApeM.setText(clienteSeleccionado.getPersona().getApellidoM());
        txtNombre.setText(clienteSeleccionado.getPersona().getNombre());
        txtApeP.setText(clienteSeleccionado.getPersona().getApellidoP());
        txtRFC.setText(clienteSeleccionado.getPersona().getRfc());
        txtDomicilio.setText(clienteSeleccionado.getPersona().getDomicilio());
        txtNumCliente.setText(clienteSeleccionado.getNumeroUni());
        txtCorreo.setText(clienteSeleccionado.getCorreo());
        txtTelefono.setText(clienteSeleccionado.getPersona().getTelefono());
        txtUsuario.setText(clienteSeleccionado.getUsuario().getNombreUsu());
        txtContrasenia.setText(clienteSeleccionado.getUsuario().getContrasenia());
        String estatusBebe = String.valueOf(clienteSeleccionado.getEstatus());
        if (estatusBebe.equals("1"))
        {
            txtEstatus.setText("Activo");
        }
        else
        {
            txtEstatus.setText("Inactivo");
        }
        txtIdCliente.setText(String.valueOf(clienteSeleccionado.getId()));
        txtIdPersona.setText(String.valueOf(clienteSeleccionado.getPersona().getId()));
        txtIdUsuario.setText(String.valueOf(clienteSeleccionado.getUsuario().getId()));
        if ("M".equals(clienteSeleccionado.getPersona().getGenero())) {
            cmbSexo.setValue("Hombre");
        } else if ("F".equals(clienteSeleccionado.getPersona().getGenero())) {
            cmbSexo.setValue("Mujer");
        } else {
            cmbSexo.setValue("Otro");
        }
        
        btnModificar.setDisable(false);
        btnEliminar.setDisable(false);
        btnGuardar.setDisable(true);
    }
    
    public void eliminarCliente() {
        String id = txtIdCliente.getText();
        if (id.length() > 0) {
            try {
                String response = client.target("http://localhost:8084/MySpa/api/cliente/delete")
                        .queryParam("id", id)
                        .request(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .get(String.class);
                System.out.println(response);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente eliminado correctamente");
                alert.show();
                limpiarCampos();
                cargarTablaClientes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cliente no encontrado, imposible eliminar");
            alert.show();
        }
    }
    
    public void modificarCliente() {
        if (!"".equals(txtIdCliente.getText())) {
            Cliente cliente = new Cliente();
            Persona persona = new Persona();
            Usuario usuario = new Usuario();
            cliente.setId(Integer.parseInt(txtIdCliente.getText()));
            persona.setNombre(txtNombre.getText());
            persona.setApellidoP(txtApeP.getText());
            persona.setApellidoM(txtApeM.getText());
            if (cmbSexo.getValue().equals("Mujer")) {
                persona.setGenero("F");
            } else if (cmbSexo.getValue().equals("Hombre")) {
                persona.setGenero("M");
            } else {
                persona.setGenero("O");
            }
            persona.setRfc(txtRFC.getText());
            persona.setDomicilio(txtDomicilio.getText());
            persona.setTelefono(txtTelefono.getText());
            cliente.setEstatus(1);
            usuario.setNombreUsu(txtUsuario.getText());
            cliente.setCorreo(txtCorreo.getText());
            usuario.setContrasenia(txtContrasenia.getText());
            usuario.setRol("Cliente");
            usuario.setToken("");
            usuario.setId(Integer.parseInt(txtIdUsuario.getText()));
            persona.setId(Integer.parseInt(txtIdPersona.getText()));
            cliente.setPersona(persona);
            cliente.setUsuario(usuario);
            cliente.setNumeroUni(txtNumCliente.getText());
            Gson gson = new Gson();
            String c = gson.toJson(cliente);
            target = client.target("http://localhost:8084/MySpa/api/cliente/update");
            Form map = new Form();
            map.param("cliente", c);
            String response = target.request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(map, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
            System.out.println(response);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente modificado correctamente");
            alert.show();
            limpiarCampos();
            cargarTablaClientes();
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Cliente no encontrado, imposible modificar");
            alerta.show();
        }
    }
    
    public void guardarCliente() {
        System.out.println(txtIdCliente.getText());
        try {
            Cliente cliente = new Cliente();
            Persona persona = new Persona();
            Usuario usuario = new Usuario();
            if ("".equals(txtIdCliente.getText())) {
                cliente.setId(0);
                persona.setId(0);
                usuario.setId(0);
                persona.setNombre(txtNombre.getText());
                persona.setApellidoP(txtApeP.getText());
                persona.setApellidoM(txtApeM.getText());
                if (cmbSexo.getValue().equals("Mujer")) {
                    persona.setGenero("F");
                } else if (cmbSexo.getValue().equals("Hombre")) {
                    persona.setGenero("M");
                } else {
                    persona.setGenero("O");
                }
                persona.setRfc(txtRFC.getText());
                persona.setDomicilio(txtDomicilio.getText());
                persona.setTelefono(txtTelefono.getText());
                cliente.setEstatus(1);
                usuario.setNombreUsu(txtUsuario.getText());
                cliente.setCorreo(txtCorreo.getText());
                usuario.setContrasenia(txtContrasenia.getText());
                usuario.setRol("Cliente");
                usuario.setToken("");
                cliente.setPersona(persona);
                cliente.setUsuario(usuario);
                cliente.setNumeroUni("");
                Gson gson = new Gson();
                String c = gson.toJson(cliente);
                System.out.println(c);
                target = client.target("http://localhost:8084/MySpa/api/cliente/insert");
                Form map = new Form();
                map.param("cliente", c);
                String response = target.request(MediaType.APPLICATION_JSON)
                        .post(Entity.entity(map, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
                System.out.println(response);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cliente insertado correctamente");
                alert.show();
                cargarTablaClientes();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Este cliente ya existe,imposible insertar");
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void buscarCliente() {
        String filtro = txtBuscar.getText();

        try {
            String response = client.target("http://localhost:8084/MySpa/api/cliente/search")
                    .queryParam("filter", filtro)
                    .request(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .get(String.class
                    );
            if (response.equals("[]")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No se encontarón resultados");
                alert.show();
                
            } else {
                Gson gson = new Gson();
                Cliente[] clientesArreglo = gson.fromJson(response, Cliente[].class
            );
            tblDetalleClientes.getItems().setAll(clientesArreglo);
            }
            txtBuscar.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
