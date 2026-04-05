/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.admin;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leiyam
 *
 * Represents a company participating in the fair.
 */
public class Organization {

    private String name;
    private List<Booth> booths = new ArrayList<>();

    public Organization(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addBooth(Booth booth) {
        booths.add(booth);
    }

    public List<Booth> getBooths() {
        return booths;
    }
}