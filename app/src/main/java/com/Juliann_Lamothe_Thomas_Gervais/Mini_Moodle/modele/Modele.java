package com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele;

import com.Juliann_Lamothe_Thomas_Gervais.Mini_Moodle.modele.entite.Users;

import java.util.ArrayList;
import java.util.List;

public class Modele {

    private List<Users> listUsers = new ArrayList<>();

    public List<Users> getListUsers() {
        return listUsers;
    }
    public void setListUsers(List<Users> listUsers) {
        this.listUsers = listUsers;
    }

    public Users getusers(String id){
        for(Users users : listUsers){
            if(users.getId().equals(id)) {
                return users;
            }
        }
        return null;
    }



}
