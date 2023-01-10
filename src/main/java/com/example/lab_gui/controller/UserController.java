package com.example.lab_gui.controller;

import com.example.lab_gui.HelloApplication;
import com.example.lab_gui.entities.User;
import com.example.lab_gui.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class UserController {


    Service<Long,User> service;
    @FXML
    PasswordField passwordField;

    @FXML
    TextField textField;

    @FXML
    Button logInButton;

    @FXML
    Button signInButton;

    public UserController() {
    }

    public void setUp(Service<Long, User> service){
        this.service=service;
    }

    public void init(){

    }

    public void handleLogIn() throws IOException {


            if(textField.getText()==""||passwordField.getText()==""){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hellow");
                alert.setHeaderText("Fail");
                alert.setContentText("Invalid user name");
                alert.show();
                return;
            }

            String userName=textField.getText();
            String passwd=passwordField.getText();
            if(service.valid(userName,passwd)!=null){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(HelloApplication.class.getResource("views/friend_table.fxml"));
                HBox loginLayout=loader.load();

                MenuController userController=loader.getController();

                userController.setService(new Service<>());
               // userController.init();
                userController.setCurrentUser(service.valid(userName,passwd));


                Stage stage=new Stage();
                Scene scene = new Scene(loginLayout, 600, 400);
                stage.setScene(scene);
                stage.show();

                Stage thisStage = (Stage) textField.getScene().getWindow();
                thisStage.close();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hellow");
                alert.setHeaderText("Fail");
                alert.setContentText("Invalid user name");
                alert.show();
                return;
            }




     //   service.add(userName,userName,passwd);
    }

    public void handleSignIn() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(HelloApplication.class.getResource("views/sign_up.fxml"));
        AnchorPane signInLayout=loader.load();

        //MenuController userController=loader.getController();
        //userController.setService(new Service<>());
        // userController.init();
        //serController.setCurrentUser(service.valid(userName,passwd));

        SignUpController signUpController=loader.getController();
        signUpController.setService(new Service<>());
        Stage stage=new Stage();
        Scene scene = new Scene(signInLayout, 250, 400);
        stage.setScene(scene);
        stage.show();
    }

}
