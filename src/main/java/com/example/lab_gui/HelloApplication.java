package com.example.lab_gui;

import com.example.lab_gui.controller.MenuController;
import com.example.lab_gui.controller.MessageController;
import com.example.lab_gui.controller.UserController;
import com.example.lab_gui.service.Service;
import com.example.lab_gui.validators.UtilizatorValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.example.lab_gui.repository.dataBase.UserRepoDb;

import java.io.IOException;


public class HelloApplication extends Application {
    Service service;
    @Override
    public void start(Stage stage) throws IOException {
        String username="postgres";
        String pasword="snitel67";
        String url="jdbc:postgresql://localhost:5434/GUIlab";

      //UserRepoDb repoDb=new UserRepoDb(url,username,pasword,new UtilizatorValidator());
        service=new Service<>();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(HelloApplication.class.getResource("views/hello-view.fxml"));
        HBox loginLayout=fxmlLoader.load();

        UserController userController=fxmlLoader.getController();
        userController.setUp(service);
     //   MenuController menuController=fxmlLoader.getController();
        Scene scene = new Scene(loginLayout, 600, 400);
        stage.setScene(scene);
        stage.show();


 /*       Stage stage1=new Stage();
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(HelloApplication.class.getResource("views/mesaje.fxml"));
        HBox layout=loader.load();
        MessageController messageController=loader.getController();

        Scene scene1=new Scene(layout,600,400);
        stage1.setScene(scene1);
        stage1.show();*/
/*        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/friend.fxml"));
        HBox root=loader.load();
        MenuController menuControllerController=loader.getController();

        Stage stage1=new Stage();
        stage1.setScene(new Scene(root,800,600));
        stage1.show();*/
    }

    public static void main(String[] args) {
        launch();
    }


}