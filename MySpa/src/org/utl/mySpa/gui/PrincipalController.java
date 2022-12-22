/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.utl.mySpa.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author marti
 */
public class PrincipalController implements Initializable{

    @FXML AnchorPane pnlPrincipal;
    
    @FXML
    private Parent moduloEmpleado;
    
    @FXML
    private Parent moduloProducto;
    
    @FXML
    private Parent moduloTratamiento;
    
    @FXML
    private Parent moduloSucursal;
    
    @FXML
    private Parent moduloCliente;
    
    @FXML
    private Parent moduloSala;
    
    @FXML
    private ModuloEmpleadoController moduloEmpleadoController;
    
    @FXML
    private ModuloProductoController moduloProductoController;
    
    @FXML
    private ModuloTratamientoController moduloTratamientoController;
    
    @FXML
    private ModuloSucursalController moduloSucursalController;
    
    @FXML
    private ModuloClienteController moduloClienteController;
    
    @FXML
    private ModuloSalaController moduloSalaController;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}
