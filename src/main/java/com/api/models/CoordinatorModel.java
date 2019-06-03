package com.api.models;

import javax.persistence.*;

@Entity
public class CoordinatorModel extends UserModel {
    
    public CoordinatorModel() {
    }

    public CoordinatorModel(String name, String surname, String email, String password) {
        super(name, surname, email, password);
    }
}
