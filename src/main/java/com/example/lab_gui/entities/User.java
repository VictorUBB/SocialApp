package com.example.lab_gui.entities;

import java.util.PrimitiveIterator;

public class User extends Entity<Long>{
    private String passwd;
    private String first_name;
    private String last_name;

    public User(String passwd, String first_name, String last_name) {
        this.passwd = passwd;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Override
    public String toString() {
        return "User{" +
                "passwd='" + passwd + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                '}';
    }
}
