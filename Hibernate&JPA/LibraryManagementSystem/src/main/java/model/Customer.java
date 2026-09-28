package model;

import jakarta.persistence.Entity;

@Entity
public class Customer extends Person {

    private int age;

    public Customer() {
    }

    public Customer(String name, String email, int age) {
        super(name, email);
        this.age = age;
    }
}
