package com.example.lab_gui.controller;

import com.example.lab_gui.entities.Friendship;
import com.example.lab_gui.entities.User;
import com.example.lab_gui.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MenuController {

    public Button denyRequestBtn;
    public Button confirmRequestBtn;
    @FXML
    Label firstNameLabet;
    @FXML
    Label lastNameLabet;
    @FXML
    TextField lastNameField;
    @FXML
    TextField firstNameField;
    @FXML
    VBox friendView;
    ObservableList<User> userObservableList = FXCollections.observableArrayList();
    @FXML
    Button requestBtn;
    @FXML
    Button friendBtn;
    @FXML
    Button addBtn;
    @FXML
    Button delBtn;
    @FXML
    TableView<User> friendsTable;

    @FXML
    TableColumn<User,String> FirstNameCol;

    @FXML
    TableColumn<User,String> LastNameCol;
    Service<Long,User> service;

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setService(Service<Long, User> service) {

        this.service = service;
        userObservableList.setAll(getUserList());
    }

    @FXML
    public void initialize(){
        FirstNameCol.setCellValueFactory((new PropertyValueFactory<User,String>("first_name")));
        LastNameCol.setCellValueFactory((new PropertyValueFactory<User,String >("last_name")));
        friendsTable.setItems(userObservableList);
        friendView.setVisible(false);
    }
    private List<User> getUserList(){
        List<User> users = new ArrayList<User>(service.getAll().values());

        return users
                .stream()
                .map(n-> new User(n.getPasswd(),n.getFirst_name(),n.getLast_name()))
                .collect(Collectors.toList());
    }
    public void populateTable(){

        userObservableList.setAll(new ArrayList<>(getUserList()));

    }
    public void onFriendClick(){
        friendView.setVisible(true);
        addBtn.setDisable(false);
        addBtn.setText("Add");
        confirmRequestBtn.setVisible(false);
        denyRequestBtn.setVisible(false);
        User user =friendsTable.getSelectionModel().getSelectedItem();
        User userWithId=service.valid(user.getFirst_name()+user.getLast_name(),user.getPasswd());
        firstNameField.setText(user.getFirst_name());
        lastNameField.setText(user.getLast_name());
        Long status=0L;
        for(Friendship friendship:service.getAllFriens().values()){
            if(currentUser.getId()==friendship.getId_1()&&userWithId.getId()== friendship.getId_2()){
                status=friendship.getStatus();
                break;
            }
/*            else if(currentUser.getId()==friendship.getId_2()&&userWithId.getId()== friendship.getId_1()){
                status=friendship.getStatus();
                break;
            }*/
        }
        if(status==2L){
            addBtn.setText("Sent");
            addBtn.setDisable(true);

        }
        else if (status==3L){
            addBtn.setText("Requested");
            addBtn.setDisable(true);
            confirmRequest();
        }
        else if (status==1L){
            addBtn.setText("Friends");
            addBtn.setDisable(true);
        }
    }

    public void confirmRequest(){
        confirmRequestBtn.setVisible(true);
        denyRequestBtn.setVisible(true);
    }

    public void onClickAcceptFriend(){

        User friend=friendsTable.getSelectionModel().getSelectedItem();
        User userWithId=service.valid(friend.getFirst_name()+friend.getLast_name(),friend.getPasswd());
        for(Friendship friendship:service.getAllFriens().values()){
            if(currentUser.getId()==friendship.getId_1()&&userWithId.getId()== friendship.getId_2()){
                service.deteteFriendship(friendship.getId());

                service.addFriendship(friendship.getId_1(), friendship.getId_2(), 1L);
                break;
            }
/*            else if(currentUser.getId()==friendship.getId_2()&&userWithId.getId()== friendship.getId_1()){
                service.deteteFriendship(friendship.getId());
                service.addFriendship(friendship.getId_1(), friendship.getId_2(), 1L);
                break;
            }*/
            onFriendClick();
        }
    }

    public void onClickRejectFriend(){
        User friend=friendsTable.getSelectionModel().getSelectedItem();
        User userWithId=service.valid(friend.getFirst_name()+friend.getLast_name(),friend.getPasswd());
        for(Friendship friendship:service.getAllFriens().values()){
            if(currentUser.getId()==friendship.getId_1()&&userWithId.getId()== friendship.getId_2()){
                service.deteteFriendship(friendship.getId());
                friendship.setStatus(0L);
                break;
            }
/*            else if(currentUser.getId()==friendship.getId_2()&&userWithId.getId()== friendship.getId_1()){
                service.deteteFriendship(friendship.getId());
                friendship.setStatus(0L);
                break;
            }*/
        }
    }
    public void onClickAdd(){
        User friend=friendsTable.getSelectionModel().getSelectedItem();
        User userWithId=service.valid(friend.getFirst_name()+friend.getLast_name(),friend.getPasswd());
        service.addFriendship(getCurrentUser().getId(),userWithId.getId(),2L);
        service.addFriendship(userWithId.getId(),getCurrentUser().getId(),3L);

    }
}
