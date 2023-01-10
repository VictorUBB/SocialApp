package com.example.lab_gui.controller;

import com.example.lab_gui.entities.Friendship;
import com.example.lab_gui.entities.Mesaj;
import com.example.lab_gui.entities.User;
import com.example.lab_gui.service.Service;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageController {
    public ListView<String> messageList;
    public TextFlow textFlowLst;
    public VBox mesajeBox;
    public VBox friendsBox;
    public ListView<HBox> listViewFinds;
    public TextField textMsgField;
    public Label userNameLabel;

    User currenUser;
    Service<Long, User> service=new Service<>();

    public Service<Long, User> getService() {
        return service;
    }

    public void setService(Service<Long, User> service) {
        this.service = service;
    }

    public User getCurrenUser() {
        return currenUser;
    }

    public void setCurrenUser(User currenUser) {
        this.currenUser = currenUser;
    }


    public void initialize(){
        //messageList.getItems().add("MEsaj 1");
      //  messageList.getItems().add("Mesaj 2");
/*        HBox text1=createText("asaadSdAsdasd sDsAD asdasd jakhdkjshajd asdhk ahskdha ",true);
        HBox text2=createText("asdasdsadasasdasd",false);
        mesajeBox.setSpacing(5L);
        mesajeBox.getChildren().setAll(text1,text2);
        HBox newhb=createUserLabel();
        HBox newew=createUserLabel();

       // friendsBox.getChildren().setAll(newhb,newew);
        listViewFinds.getItems().setAll(newhb,newew);*/

      //  Map<Long,Friendship> friendsList=service.getAllFriens();
//        List<Friendship> friendshipList=new ArrayList<>(service.getAllFriens().values());
//        friendshipList.stream()
//                .filter(n->n.getId_1()==currenUser.getId())
//                .collect(Collectors.toList());
//        for (Friendship friendship:friendshipList){
//            listViewFinds.getItems().setAll(createUserLabel(service.findUser(friendship.getId_2())));
//        }
    };

    public void poulate(){
        List<Friendship> friendshipList=new ArrayList<>(service.getAllFriens().values());

       List<HBox>hBoxList=new ArrayList<>();
        for (Friendship friendship:friendshipList){
            if (friendship.getId_1()==currenUser.getId()&&friendship.getStatus()==1L)
                hBoxList.add(createUserLabel(service.findUser(friendship.getId_2())));
        }
        listViewFinds.getItems().setAll(hBoxList);
    }
    private static HBox createText(String text, boolean right) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMinWidth(200);
        label.setPrefWidth(200);
        label.setMaxWidth(200);
        label.setPadding(new Insets(5));
        label.setStyle("-fx-background-color: " + (right ? "#f0e246;" : "white;") + "-fx-background-radius: 10;");
        HBox pane = new HBox(label);
        pane.setAlignment(right ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        return pane;


    }

    public void onClickShowPastMessages(){
        Map<Long, Mesaj> mesajMap=service.getMesaje();
        List<Mesaj> mesajList=new ArrayList<>(service.getMesaje().values());
        mesajList.stream()
                .filter(n->n.getFrom()== currenUser.getId()||n.getTo()==currenUser.getId());

    }




    private HBox createUserLabel(User friend) {
/*  <HBox prefHeight="44.0" prefWidth="458.0">
               <children>
                  <ImageView fitHeight="45.0" fitWidth="60.0" pickOnBounds="true" preserveRatio="true" />
                  <Label prefHeight="43.0" prefWidth="318.0" text="User Name">
                     <font>
                        <Font size="24.0" />
                     </font>
                  </Label>
               </children>
            </HBox>*/

     HBox newBox=new HBox();
     newBox.setPrefWidth(100.0);
     newBox.setPrefHeight(44.0);
     ImageView imageView=new ImageView();
     imageView.setImage(new Image("images/person.jpg"));
     imageView.setFitHeight(45.0);
     imageView.setFitWidth(60.0);

     VBox labelBox=new VBox();

     Label labelName=new Label();
     labelName.setPrefHeight(43.0);
     labelName.setPrefWidth(100.0);
     labelName.setText(friend.getFirst_name()+" "+friend.getLast_name());

     Label labelInfo=new Label();
        labelInfo.setPrefHeight(43.0);
        labelInfo.setPrefWidth(100.0);
        labelInfo.setText("Infomatiii");

        labelBox.getChildren().setAll(labelName,labelInfo);
        newBox.getChildren().setAll(imageView,labelBox);
     return newBox;

    }


    public void onClickGetMessages(){
       // friendsBox.getChildren().clear();
        mesajeBox.getChildren().clear();
        HBox hBox=listViewFinds.getSelectionModel().getSelectedItem();
        VBox vBox=(VBox) hBox.getChildren().get(1);

        Map<Long,Mesaj> mesajMap=service.getMesaje();
        Label useName=(Label) vBox.getChildren().get(0);
        String userName=useName.getText();
        String[] nameLst=(userName.split(" "));
        userNameLabel.setText(userName);
        User user=service.findByName((String) nameLst[0],nameLst[1]);
        //User user=service.findByName(nameLst[0],nameLst[1]);
        int index=0;
        for(Mesaj mesaj:mesajMap.values()){
            if(mesaj.getFrom()==currenUser.getId()&&mesaj.getTo()==user.getId()){
                mesajeBox.getChildren().add(createText(mesaj.getMesaj(),true));
            }
            else if(mesaj.getFrom()==user.getId()&&mesaj.getTo()==currenUser.getId()){
                mesajeBox.getChildren().add(createText(mesaj.getMesaj(),false));
            }

        }

    }

    public void onEnterAddMsg(){

        HBox hBox=listViewFinds.getSelectionModel().getSelectedItem();
        VBox vBox=(VBox) hBox.getChildren().get(1);

        Label useName=(Label) vBox.getChildren().get(0);
        String userName=useName.getText();
        String[] nameLst=(userName.split(" "));
        User user=service.findByName((String) nameLst[0],nameLst[1]);
        service.addMsg(textMsgField.getText(), currenUser.getId(), user.getId());
        textMsgField.clear();
        onClickGetMessages();
    }
}
