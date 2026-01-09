package com.devlawal.user;

import java.util.Objects;
import java.util.UUID;

public class User {
    private String name;
    private UUID id;
    private String email;
    private Integer age;
    private boolean isAvailable;

//    public User(String name, String email, Integer age, UUID id) {
//        this.name = name;
//        this.email = email;
//        this.age = age;
//        this.id = id;
//        this.isAvailable = true;
//    }

    public User(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.isAvailable = true;
        this.id = UUID.randomUUID();
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return this.email;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", isAvailable=" + isAvailable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isAvailable == user.isAvailable && Objects.equals(name, user.name) && Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(age, user.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, email, age, isAvailable);
    }
}