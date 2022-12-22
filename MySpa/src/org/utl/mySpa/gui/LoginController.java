/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.utl.mySpa.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author marti
 */
public class LoginController implements Initializable{
    //DataBinding
    @FXML
    private JFXTextField txtUsuario;

    @FXML
    private JFXTextField txtContrasennia;

    @FXML
    private JFXButton btnIngresar;

    @FXML
    private JFXButton btnSalir;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void validarUsuario()
    {
        String user = txtUsuario.getText();
        String pass = txtContrasennia.getText();
        System.out.println(user + " - " + pass);
        
        if("admin".equals(user) && "123".equals(pass))
        {
            try
            {
                //Abre Principal
                Parent principal = FXMLLoader.load(getClass().getResource("/org/utl/mySpa/gui/fxml/PrincipalWindow.fxml"));
                Scene scene = new Scene(principal);
                scene.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Sistema de control");
                stage.setResizable(true);
                stage.show();
                
                //Cierra Login
                Stage cerrarStage = (Stage) btnIngresar.getScene().getWindow();
                cerrarStage.close();
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al validar los datos. Intenta de nuevo");
                alert.show();
                e.printStackTrace();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Datos Incorrectos. Intenta de nuevo");
            alert.showAndWait();
        }
    }
    
    public void salir()
    {
        Stage cerrarStage = (Stage) btnSalir.getScene().getWindow();
        cerrarStage.close();
    }
}
