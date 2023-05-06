package com.example.demo;


public record Entity(Long id, String name, Integer age, String text) {

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", text='" + text + '\'' +
                '}';
    }
}
