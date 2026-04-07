/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.admin;

import com.mycompany.admin.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leiyam
 * 
 * Central Model of the system.
 * 
 * NOTE:
 * - This class stores ALL system data.
 * - Extend this with Candidate, Booking, Session logic.
 * - Do NOT duplicate data elsewhere.
 */
public class CentralModel extends Observable {

    // ===== Core Data =====
    private List<Organization> organizations = new ArrayList<>();

    // ===== Timeline =====
    private LocalDateTime bookingsOpen;
    private LocalDateTime bookingsClose;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // ===== Data to PURGE =====
    private List<String> reservations = new ArrayList<>();
    private List<String> sessions = new ArrayList<>();
    private List<String> notifications = new ArrayList<>();

    // ===== Organization Methods =====

    public void addOrganization(Organization org) {
        organizations.add(org);
    }

    public Organization findOrganization(String name) {
        for (Organization o : organizations) {
            if (o.getName().equals(name)) {
                return o;
            }
        }
        return null;
    }

    public Booth findBooth(String boothName) {
        for (Organization org : organizations) {
            for (Booth booth : org.getBooths()) {
                if (booth.getName().equals(boothName)) {
                    return booth;
                }
            }
        }
        return null;
    }

    // ===== Timeline Methods =====

    public void setTimeline(LocalDateTime open, LocalDateTime close,
                            LocalDateTime start, LocalDateTime end) {
        this.bookingsOpen = open;
        this.bookingsClose = close;
        this.startTime = start;
        this.endTime = end;
    }

    // ===== PURGE LOGIC =====

    /**
     * Clears all runtime data when leaving Dormant state.
     * 
     * IMPORTANT:
     * - Must be triggered during state transition.
     */
    public void purgeData() {
        reservations.clear();
        sessions.clear();
        notifications.clear();

        notifyObservers("System data purged.");
    }
}
