package com.example.lab_gui.repository.dataBase;

import com.example.lab_gui.entities.User;
import com.example.lab_gui.validators.Validator;
import com.example.lab_gui.repository.Repository0;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
public class UserRepoDb implements Repository0<Long, User> {
    private String url;
    private String username;
    private String passwdDb;
    private Validator<User> validator;

    public UserRepoDb(String url, String username, String passwdDb, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.passwdDb = passwdDb;
        this.validator = validator;

    }

    @Override
    public User findOne(Long aLong) {
        String sql="SELECT  * FROM users WHERE id="+aLong.toString();
        try(Connection connection= DriverManager.getConnection(url,username,passwdDb);
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            // preparedStatement.setString(1,aLong.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Long id=resultSet.getLong("id");
            String firstName=resultSet.getString("first_name");
            String lastName=resultSet.getString("last_name");
            String passwd=resultSet.getString("password");
            User user=new User(passwd,firstName,lastName);
            user.setId(id);
            return user;
        }

        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> userSet=new HashSet<>();
        try(Connection connection= DriverManager.getConnection(url,username,passwdDb)){
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT  * FROM users");
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                Long id=resultSet.getLong("id");
                String firstName=resultSet.getString("first_name");
                String lastName=resultSet.getString("last_name");
                String passwd=resultSet.getString("password");
                User user=new User(firstName,lastName,passwd);
                user.setId(id);
                userSet.add(user);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return userSet;
    }


    public Map<Long,User> getALl() {
        Map<Long,User> userSet=new HashMap<>();
        try(Connection connection= DriverManager.getConnection(url,username,passwdDb)){
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT  * FROM users");
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                Long id=resultSet.getLong("id");
                String firstName=resultSet.getString("first_name");
                String lastName=resultSet.getString("last_name");
                String paswd=resultSet.getString("password");
              //  MessageDigest md= MessageDigest.getInstance("MD5");
                User user=new User(paswd,firstName,lastName);
                user.setId(id);
                userSet.put(id,user);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return userSet;
    }
    @Override
    public User save(User entity) {


        String sql="INSERT INTO users(first_name,last_name,password) VALUES(?,?,?)";
        try(Connection connection=DriverManager.getConnection(url,username,passwdDb);
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            preparedStatement.setString(1,entity.getFirst_name());
            preparedStatement.setString(2,entity.getLast_name());
            MessageDigest md= MessageDigest.getInstance("MD5");
            String passw=md.digest(entity.getPasswd().getBytes(StandardCharsets.UTF_8)).toString();
            preparedStatement.setString(3,entity.getPasswd());
            preparedStatement.executeUpdate();
        }

        catch (SQLException e){
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public User delete(Long aLong){
        String sql="DELETE FROM users WHERE id="+aLong.toString();
        User user=this.findOne(aLong);
        try(Connection connection=DriverManager.getConnection(url,username,passwdDb);
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            // preparedStatement.setString(1,aLong.toString());
            preparedStatement.executeUpdate();
        }

        catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User update(User entity) {
        return null;
    }

 /*     @Override
  public User update(User entity) {
        String sql="UPDATE users SET first_name='"+entity.getFirstName().toString()+
                "', last_name='"+entity.getLastName().toString()+" 'WHERE id="+entity.getId().toString();
        User user=this.findOne(entity.getId());
        try(Connection connection=DriverManager.getConnection(url,username,passwdDb)){
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            // return user;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }*/
}
