package com.devlawal.user;

import java.util.Objects;
import java.util.UUID;

public class User {
    private String name;
    private UUID id;
    private String email;
    private Integer age;

    public User(String name, String email, Integer age, UUID id) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.id = id;
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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(age, user.age) && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, age, id);
    }
}