package com.example.lab_gui.controller;

import com.example.lab_gui.HelloApplication;
import com.example.lab_gui.entities.Friendship;
import com.example.lab_gui.entities.User;
import com.example.lab_gui.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MenuController {

    public Button denyRequestBtn;
    public Button confirmRequestBtn;
    public Button allBtn;
    public Text sendRequestLabel;
    public Label friensSiceLabel;
    public TextField searchBtn;
    public Button SignOutBtn;
    public Button sendMsgBtn;

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

        this.currentUser = service.valid(currentUser.getFirst_name()+currentUser.getLast_name(),currentUser.getPasswd());
        userObservableList.setAll(getUserList());
        userObservableList.remove(currentUser);
    }

    public void setService(Service<Long, User> service) {

        this.service = service;


    }

    @FXML
    public void initialize(){
        FirstNameCol.setCellValueFactory((new PropertyValueFactory<User,String>("first_name")));
        LastNameCol.setCellValueFactory((new PropertyValueFactory<User,String >("last_name")));
        ObservableList<User> userObservableList1 =userObservableList;
        userObservableList1.remove(currentUser);
        friendsTable.setItems(userObservableList1);
        friendView.setVisible(false);
        searchBtn.textProperty().addListener(o->handleFilter());
    }
    public void handleFilter(){
        Predicate<User> p1=n->n.getFirst_name().startsWith(searchBtn.getText());
        Predicate<User> p2=n->n!=currentUser;
        userObservableList.setAll(getUserList()
                .stream()
                .filter(p1.and(p2))
                .collect(Collectors.toList()));
        userObservableList.remove(currentUser);
        userObservableList.remove(currentUser);
    }
    private List<User> getUserList(){
        List<User> users = new ArrayList<User>(service.getAll().values());
        users.remove(currentUser);
        Predicate<User> userPredicate=n->n!=currentUser;
        return users
                .stream()
                .map(n-> new User(n.getPasswd(),n.getFirst_name(),n.getLast_name()))
                .filter(userPredicate)
                .collect(Collectors.toList());

    }
    public void populateTable(){

        userObservableList.setAll(new ArrayList<>(getUserList()));

    }
    public void onFriendClick(){
        friendView.setVisible(true);
        addBtn.setDisable(false);
        delBtn.setDisable(true);
        sendRequestLabel.setVisible(false);
        friensSiceLabel.setVisible(false);
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
          //  delBtn.setDisable(true);
            delBtn.setText("Cancel");

        }
        else if (status==3L){
            addBtn.setText("Requested");
            addBtn.setDisable(true);
            delBtn.setDisable(true);
            confirmRequest();
        }
        else if (status==1L){
            addBtn.setText("Friends");
            addBtn.setDisable(true);
            friensSiceLabel.setVisible(true);
            friendsSince();
        }
    }

    public void onClickSignOut() throws IOException {

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(HelloApplication.class.getResource("views/hello-view.fxml"));
        HBox loginLayout=loader.load();

        UserController userController=loader.getController();
        userController.setUp(new Service<>());
        Stage stage=new Stage();
        Scene scene = new Scene(loginLayout, 600, 400);
        stage.setScene(scene);
        stage.show();
        Stage thisStage = (Stage) SignOutBtn.getScene().getWindow();
        thisStage.close();
    }
    public void friendsSince(){
        User friend=friendsTable.getSelectionModel().getSelectedItem();
        User userWithId=service.valid(friend.getFirst_name()+friend.getLast_name(),friend.getPasswd());
        Friendship friendship=service.findFriendshipByIds(currentUser.getId(), userWithId.getId());
        friensSiceLabel.setText("Friens since "+friendship.getFriendsFrom());
    }
    public void onClickDelete(){
        if(Objects.equals(delBtn.getText(), "Delete")) {
            User friend = friendsTable.getSelectionModel().getSelectedItem();
            User userWithId = service.valid(friend.getFirst_name() + friend.getLast_name(), friend.getPasswd());
            Friendship fromUserToFriend = service.findFriendshipByIds(currentUser.getId(), userWithId.getId());
            Friendship fromFriendToUser = service.findFriendshipByIds(userWithId.getId(), currentUser.getId());
            service.deteteFriendship(fromFriendToUser.getId());
            service.deteteFriendship(fromUserToFriend.getId());
            service.addFriendship(fromUserToFriend.getId_1(), fromUserToFriend.getId_2(), 0L);
            service.addFriendship(fromUserToFriend.getId_2(), fromUserToFriend.getId_1(), 0L);
            // onFriendClick();
            addBtn.setDisable(false);
            addBtn.setText("Add");
            delBtn.setDisable(true);
            friensSiceLabel.setVisible(false);
        }
        else {
            User friend=friendsTable.getSelectionModel().getSelectedItem();
            User userWithId=service.valid(friend.getFirst_name()+friend.getLast_name(),friend.getPasswd());

            //reset the status of the friendship
            Friendship fromUserToFriend=service.findFriendshipByIds(currentUser.getId(), userWithId.getId());
            Friendship fromFriendToUser=service.findFriendshipByIds(userWithId.getId(), currentUser.getId());
            service.deteteFriendship(fromUserToFriend.getId());
            service.addFriendship(fromUserToFriend.getId_1(), fromUserToFriend.getId_2(),0L );

            service.deteteFriendship(fromFriendToUser.getId());
            service.addFriendship(fromFriendToUser.getId_1(), fromFriendToUser.getId_2(), 0L);
            addBtn.setDisable(false);
            addBtn.setText("Add");
            delBtn.setDisable(true);
            sendRequestLabel.setVisible(false);
            denyRequestBtn.setVisible(false);
            confirmRequestBtn.setVisible(false);
            delBtn.setText("Delete");
        }
    }
    public void confirmRequest(){
        confirmRequestBtn.setVisible(true);
        denyRequestBtn.setVisible(true);
        sendRequestLabel.setVisible(true);

    }

    public void onClickAcceptFriend(){
        //we enter here if the current user got a friend request from somebody
        //and he wants to accept it
        //initial status 3 for current user->1
        //initial status 3 for sender->1
        User friend=friendsTable.getSelectionModel().getSelectedItem();
        User userWithId=service.valid(friend.getFirst_name()+friend.getLast_name(),friend.getPasswd());
        Friendship fromUserToFriend=service.findFriendshipByIds(currentUser.getId(), userWithId.getId());
        Friendship fromFriendToUser=service.findFriendshipByIds(userWithId.getId(), currentUser.getId());
        service.deteteFriendship(fromUserToFriend.getId());
        service.addFriendship(fromUserToFriend.getId_1(), fromUserToFriend.getId_2(),1L );

        service.deteteFriendship(fromFriendToUser.getId());
        service.addFriendship(fromFriendToUser.getId_1(), fromFriendToUser.getId_2(), 1L);
        /*        for(Friendship friendship:service.getAllFriens().values()){
            if(currentUser.getId()==friendship.getId_1()&&userWithId.getId()== friendship.getId_2()){
                service.deteteFriendship(friendship.getId());
                //delete the rèverse one
                service.addFriendship(friendship.getId_1(), friendship.getId_2(), 1L);
                break;
            }
*//*            else if(currentUser.getId()==friendship.getId_2()&&userWithId.getId()== friendship.getId_1()){
                service.deteteFriendship(friendship.getId());
                service.addFriendship(friendship.getId_1(), friendship.getId_2(), 1L);
                break;
            }*//*
            onFriendClick();
        }*/
        addBtn.setDisable(true);
        delBtn.setDisable(false);
        friensSiceLabel.setText("Friens since "+fromFriendToUser.getFriendsFrom());
        sendRequestLabel.setVisible(false);
        denyRequestBtn.setVisible(false);
        confirmRequestBtn.setVisible(false);
        addBtn.setText("Friends");


    }

    public void onClickRejectFriend(){
        //we enter here if the current user got a friend request from somebody
        //and he wants to reject it
        //initial status 3 for current user->0
        //initial status 3 for sender->0
        User friend=friendsTable.getSelectionModel().getSelectedItem();
        User userWithId=service.valid(friend.getFirst_name()+friend.getLast_name(),friend.getPasswd());

        /*        for(Friendship friendship:service.getAllFriens().values()){
            if(currentUser.getId()==friendship.getId_1()&&userWithId.getId()== friendship.getId_2()){
                service.deteteFriendship(friendship.getId());
                //delete the rèverse one
                friendship.setStatus(0L);
                break;
            }
*//*            else if(currentUser.getId()==friendship.getId_2()&&userWithId.getId()== friendship.getId_1()){
                service.deteteFriendship(friendship.getId());
                friendship.setStatus(0L);
                break;
            }*//*
        }*/
        //reset the status of the friendship
        Friendship fromUserToFriend=service.findFriendshipByIds(currentUser.getId(), userWithId.getId());
        Friendship fromFriendToUser=service.findFriendshipByIds(userWithId.getId(), currentUser.getId());
        service.deteteFriendship(fromUserToFriend.getId());
        service.addFriendship(fromUserToFriend.getId_1(), fromUserToFriend.getId_2(),0L );

        service.deteteFriendship(fromFriendToUser.getId());
        service.addFriendship(fromFriendToUser.getId_1(), fromFriendToUser.getId_2(), 0L);
        addBtn.setDisable(false);
        addBtn.setText("Add");
        delBtn.setDisable(true);
        sendRequestLabel.setVisible(false);
        denyRequestBtn.setVisible(false);
        confirmRequestBtn.setVisible(false);
    }
    public void onClickAdd(){
        User friend=friendsTable.getSelectionModel().getSelectedItem();
        User userWithId=service.valid(friend.getFirst_name()+friend.getLast_name(),friend.getPasswd());
        service.addFriendship(getCurrentUser().getId(),userWithId.getId(),2L);
        service.addFriendship(userWithId.getId(),getCurrentUser().getId(),3L);
        addBtn.setDisable(true);
        addBtn.setText("Requested");
        delBtn.setText("Cancel");
    }

    public void onClickShowFriends(){
        userObservableList.clear();
        for(Friendship friendship:service.getAllFriens().values()){
            if(friendship.getId_1()== currentUser.getId()){
                if(friendship.getStatus()==1L){
                    userObservableList.add(service.findUser(friendship.getId_2()));
                }

            }
        }
    }
    
    public void onClickShowAll(){
        userObservableList.setAll(getUserList());
    }

    public void onClickShowRequests(){
        userObservableList.clear();
        for(Friendship friendship:service.getAllFriens().values()){
            if(friendship.getId_1()== currentUser.getId()){
               if (friendship.getStatus()==3L){
                   userObservableList.add(service.findUser(friendship.getId_2()));
               }

            }
        }
    }

    public void onClickOpenMessage() throws IOException {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(HelloApplication.class.getResource("views/mesaje.fxml"));
        HBox layout = loader.load();
        MessageController messageController=loader.getController();
    //    messageController.setService(new Service<>());
        messageController.setCurrenUser(currentUser);
        messageController.poulate();
        Stage stage=new Stage();
        Scene scene=new Scene(layout,400,400);
        stage.setScene(scene);
        stage.show();
    }

}
