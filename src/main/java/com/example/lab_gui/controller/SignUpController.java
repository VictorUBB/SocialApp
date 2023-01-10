package com.example.lab_gui.controller;

import com.example.lab_gui.HelloApplication;
import com.example.lab_gui.entities.User;
import com.example.lab_gui.service.Service;
import com.example.lab_gui.validators.UtilizatorValidator;
import com.example.lab_gui.validators.ValidationException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SignUpController {
    public TextField firstNameLabel;
    public TextField lastNameLabel;
    public TextField passwordLabel;
    public Button createBtn;
    public PasswordField confirmPasswordLabel;

    private UtilizatorValidator validator=new UtilizatorValidator();

    Service service;
    public void initialize(){

    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void onClickCreate() throws IOException {
            String firstName=firstNameLabel.getText().toString();
            String lastName=lastNameLabel.getText().toString();
            String pasword=passwordLabel.getText().toString();
            String confPaswd=confirmPasswordLabel.getText().toString();
            User user=new User(pasword,firstName,lastName);
            if(Validate(user,confPaswd)){
                service.add(firstName,lastName,pasword);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(HelloApplication.class.getResource("views/friend_table.fxml"));
                HBox loginLayout=loader.load();

                MenuController userController=loader.getController();
                userController.setService(new Service<>());
                // userController.init();
                userController.setCurrentUser(service.valid(firstName+lastName,pasword));


                Stage stage=new Stage();
                Scene scene = new Scene(loginLayout, 650, 400);
                stage.setScene(scene);
                stage.show();

                Stage thisStage = (Stage) createBtn.getScene().getWindow();
                thisStage.close();
            }

    }

    public boolean Validate(User user,String confPasswd){
        try{
            validator.validate(user);
            if(Objects.equals(user.getPasswd(), "") ||user.getPasswd().length()<3){
                throw new ValidationException("Password to short");
            }
            if(!Objects.equals(user.getPasswd(), confPasswd)){
                throw new ValidationException("Password doesn't match");
            }
            return true;
        }
        catch (ValidationException ve){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid input");
            alert.setHeaderText("Try again!");
            alert.setContentText(ve.getMessage().toString());
            alert.show();
            return false;
        }

    }
}
