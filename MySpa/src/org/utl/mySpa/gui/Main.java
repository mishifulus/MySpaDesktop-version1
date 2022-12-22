/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.utl.mySpa.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author marti
 */
public class Main extends Application{

    Parent root;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/org/utl/mySpa/gui/fxml/Login.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");
        
        primaryStage.setTitle("My Spa");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
}