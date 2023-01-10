package com.example.lab_gui.repository.dataBase;

import com.example.lab_gui.entities.Friendship;
import com.example.lab_gui.entities.Mesaj;
import com.example.lab_gui.repository.Repository0;

import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MesajeRepoDb implements Repository0<Long, Mesaj> {

    private String url;
    private String username;
    private String passwdDb;

    public MesajeRepoDb(String url, String username, String passwdDb) {
        this.url = url;
        this.username = username;
        this.passwdDb = passwdDb;
    }

    @Override
    public Mesaj findOne(Long aLong) {
        String sql="SELECT  * FROM mesaje WHERE id="+aLong.toString();
        try(Connection connection= DriverManager.getConnection(url,username,passwdDb);
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            // preparedStatement.setString(1,aLong.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Long id=resultSet.getLong("id");
            Long id_1=resultSet.getLong("from");
            Long id_2=resultSet.getLong("to");
            String mesaj1=resultSet.getString("mesaj");

            Mesaj mesaj=new Mesaj(mesaj1,id_1,id_2);
            mesaj.setId(id);
            return mesaj;
        }

        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Mesaj> findAll() {
        String sql="SELECT* FROM mesaje";
        Set<Mesaj> friendshipSet=new HashSet<>();
        try(Connection connection= DriverManager.getConnection(url,username,passwdDb);
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                Long id=resultSet.getLong("id");
                Long id_1=resultSet.getLong("id_1");
                Long id_2=resultSet.getLong("id_2");
                String msg=resultSet.getString("mesaj");
                Mesaj mesaj=new Mesaj(msg,id_1,id_2);
                mesaj.setId(id);
                friendshipSet.add(mesaj);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return friendshipSet;
    }

    public Map<Long,Mesaj> getAll() {
        String sql="SELECT* FROM mesaje";
        Map<Long,Mesaj> mesajMap=new HashMap<>();
        try(Connection connection= DriverManager.getConnection(url,username,passwdDb);
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                Long id=resultSet.getLong("id");
                Long id_1=resultSet.getLong("id_from");
                Long id_2=resultSet.getLong("id_to");
                String msg=resultSet.getString("mesaj");
                Mesaj mesaj=new Mesaj(msg,id_1,id_2);
                mesaj.setId(id);
               mesajMap.put(id,mesaj);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return mesajMap;
    }
    @Override
    public Mesaj save(Mesaj entity) {
        String sql="INSERT INTO mesaje(mesaj,id_from,id_to) VALUES(?,?,?)";
        try (Connection connection=DriverManager.getConnection(url,username,passwdDb)){
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setLong(2,entity.getFrom());
            preparedStatement.setLong(3,entity.getTo());

            preparedStatement.setString(1,entity.getMesaj());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Mesaj delete(Long aLong) throws FileNotFoundException {
        return null;
    }

    @Override
    public Mesaj update(Mesaj entity) {
        return null;
    }
}
