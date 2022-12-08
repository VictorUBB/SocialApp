package com.example.lab_gui.repository.dataBase;

import com.example.lab_gui.entities.Friendship;
import com.example.lab_gui.repository.Repository0;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FriendRepoDb implements Repository0<Long, Friendship> {
    private String url;
    private String username;
    private String passwdDb;

    public FriendRepoDb(String url, String username, String passwdDb) {
        this.url = url;
        this.username = username;
        this.passwdDb = passwdDb;
    }

    @Override
    public Friendship findOne(Long aLong) {
        String sql="SELECT  * FROM friendship WHERE id="+aLong.toString();
        try(Connection connection= DriverManager.getConnection(url,username,passwdDb);
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            // preparedStatement.setString(1,aLong.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Long id=resultSet.getLong("id");
            Long id_1=resultSet.getLong("id_1");
            Long id_2=resultSet.getLong("id_2");
            String time=resultSet.getString("time");
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Friendship friend= new Friendship(id_1,id_2, LocalDateTime.parse(time,formatter) );
            friend.setId(id);
            return friend;
        }

        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        String sql="SELECT* FROM friend";
        Set<Friendship> friendshipSet=new HashSet<>();
        try(Connection connection= DriverManager.getConnection(url,username,passwdDb);
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                Long id=resultSet.getLong("id");
                Long id_1=resultSet.getLong("id_1");
                Long id_2=resultSet.getLong("id_2");
                String time=resultSet.getString("time");
                DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                Friendship friend= new Friendship(id_1,id_2, LocalDateTime.parse(time,formatter) );
                friend.setId(id);
                friendshipSet.add(friend);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return friendshipSet;
    }

    public Map<Long,Friendship> getALl(){
        String sql="SELECT* FROM friendship";
        // Set<Friendship> friendshipSet=new HashSet<>();
        Map<Long,Friendship> friendshipMap=new HashMap<>();
        try(Connection connection= DriverManager.getConnection(url,username,passwdDb);
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                Long id=resultSet.getLong("id");
                Long id_1=resultSet.getLong("id_1");
                Long id_2=resultSet.getLong("id_2");
                String time=resultSet.getString("time");
                Long status=resultSet.getLong("status");
                DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                Friendship friend= new Friendship(id_1,id_2, LocalDateTime.parse(time,formatter) );
                friend.setId(id);
                friend.setStatus(status);
                friendshipMap.put(id,friend);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return friendshipMap;
    }
    @Override
    public Friendship save(Friendship entity) {
        String sql="INSERT INTO friendship(id_1,id_2,time,status)  VALUES(?,?,?,?)";
        try (Connection connection=DriverManager.getConnection(url,username,passwdDb)){
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setLong(1,entity.getId_1());
            preparedStatement.setLong(2,entity.getId_2());
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            preparedStatement.setString(3,entity.getFriendsFrom().format(formatter));
            preparedStatement.setLong(4,entity.getStatus());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Friendship delete(Long aLong)  {
        String sql="DELETE FROM friendship WHERE id="+aLong.toString();
        Friendship friend=this.findOne(aLong);
        try(Connection connection=DriverManager.getConnection(url,username,passwdDb);
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return friend;

    }

    @Override
    public Friendship update(Friendship entity) {
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String sql="UPDATE friend SET time='"+entity.getFriendsFrom().format(formatter)+"' WHERE id="+entity.getId().toString();
        Friendship friendship=this.findOne(entity.getId());
        try(Connection connection=DriverManager.getConnection(url,username,passwdDb)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return friendship;
    }

}
