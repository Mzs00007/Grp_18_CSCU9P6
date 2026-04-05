/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.admin;

import java.time.LocalDateTime;

/**
 *
 * @author leiyam
 *
 * Controller for AdministratorScreen.
 * 
 * Handles all admin actions and communicates with the Model.
 * 
 * IMPORTANT:
 * - This class should NOT contain UI code.
 * - It acts as a bridge between GUI and Model.
 */
public class AdminController {

    private CentralModel model;

    public AdminController(CentralModel model) {
        this.model = model;
    }

    /**
     * Creates a new Organization and adds it to the system.
     */
    public void createOrganization(String name) {
        Organization org = new Organization(name);
        model.addOrganization(org);

        model.notifyObservers("Organization created: " + name);
    }

    /**
     * Creates a Booth under a specific Organization.
     */
    public void createBooth(String boothName, String orgName) {
        Organization org = model.findOrganization(orgName);

        if (org != null) {
            Booth booth = new Booth(boothName);
            org.addBooth(booth);

            model.notifyObservers("Booth " + boothName + " created under " + orgName);
        }
    }

    /**
     * Assigns a Recruiter to a Booth.
     */
    public void assignRecruiter(String recruiterName, String boothName) {
        Booth booth = model.findBooth(boothName);

        if (booth != null) {
            Recruiter recruiter = new Recruiter(recruiterName);
            booth.assignRecruiter(recruiter);

            model.notifyObservers("Recruiter " + recruiterName + " assigned to booth " + boothName);
        }
    }

    /**
     * Sets the system timeline.
     * 
     * Expected format: yyyy-MM-ddTHH:mm
     */
    public void setTimeline(String open, String close, String start, String end) {
        LocalDateTime openTime = LocalDateTime.parse(open);
        LocalDateTime closeTime = LocalDateTime.parse(close);
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);

        model.setTimeline(openTime, closeTime, startTime, endTime);

        model.notifyObservers("System timeline configured.");
    }

    /**
     * Provides access to the Model (used to register observers).
     */
    public CentralModel getModel() {
        return model;
    }
}