/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.admin;

/**
 *
 * @author leiyam
 *
 * Represents a booth belonging to an organization.
 */
public class Booth {

    private String name;
    private Recruiter recruiter;

    public Booth(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void assignRecruiter(Recruiter recruiter) {
        this.recruiter = recruiter;
    }
}