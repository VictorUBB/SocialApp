package com.example.lab_gui.service;

import com.example.lab_gui.entities.Entity;
import com.example.lab_gui.entities.Friendship;
import com.example.lab_gui.entities.User;
import com.example.lab_gui.repository.dataBase.FriendRepoDb;
import com.example.lab_gui.validators.UtilizatorValidator;
import com.example.lab_gui.repository.dataBase.UserRepoDb;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

public class Service<ID,E extends Entity<ID>> {
    private UserRepoDb repository;
    private FriendRepoDb friendRepoDb;
   // private FriendshipRepoDb repoFriend;
    private Long id;
    private Long id_friendship;

    public Service()  {
        // this.com.example.lab_gui.repository = new InMemoryRepository0<Long,Utilizator>(new UtilizatorValidator());
        //   this.com.example.lab_gui.repository=new UtilizatorFile0("C:\\Users\\Victor\\Desktop\\faculta\\SEemestru III\\MAP\\completare_sem3\\com.example.lab_gui.repository\\file\\users.txt",new UtilizatorValidator());
        String url="jdbc:postgresql://localhost:5432/lab5_map";
        this.repository=new UserRepoDb("jdbc:postgresql://localhost:5432/GUIlab","postgres","snitel67",new UtilizatorValidator());
        this.friendRepoDb=new FriendRepoDb("jdbc:postgresql://localhost:5432/GUIlab","postgres","snitel67");
        // this.repoFriend=new FriendshipFile("C:\\Users\\Victor\\Desktop\\faculta\\SEemestru III\\MAP\\completare_sem3\\com.example.lab_gui.repository\\file\\friens.txt");
      //  this.repoFriend=new FriendshipRepoDb(url,"postgres","snitel67");
        //int lung=repository.getALl().size();

            //Map<Long, User> users= repository.getALl();

/*        int max=-1;
        for(Long id: users.keySet()){
            if(id.intValue()>max){
                max=id.intValue();
            }
        }*/
        //max++;
       // lung=max;
       // this.id= Long.valueOf(lung);
     //   lung=repoFriend.getALl().size();

       // this.id_friendship=Long.valueOf(lung);
        //  populate();
        this.id=0L;
    }
    public void add(String first,String last,String passwd) {
        User user=new User(passwd,first,last);
        user.setId(id);
        id++;
        repository.save(user);
    }
    public Iterable<User> findAll(){
        return repository.findAll();
    }

    public Map<Long,User> getAll(){return  repository.getALl();}

    public User readUser(){
        return null;
    }

    public User valid(String username,String passwd){

        for(User user:repository.getALl().values()){
            String check=user.getFirst_name().toString()+user.getLast_name().toString();
            if(Objects.equals(username, check))
                if(Objects.equals(passwd, user.getPasswd().toString()))
                        return user;
        }

        return null;
    }

    public Map<Long, Friendship> getAllFriens(){
        return friendRepoDb.getALl();
    }

    public void addFriendship(Long id_1,Long id_2,Long status )
    {
        Friendship friendship=new Friendship(id_1,id_2);
        friendship.setStatus(status);
        friendRepoDb.save(friendship);
    }

    public void deteteFriendship(Long id){
        friendRepoDb.delete(id);
    }
}
