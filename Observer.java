/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.admin;

import com.mycompany.admin.*; 
/**
 *
 * @author leiyam
 *
 * Observer interface for the Audit Logging system.
 * 
 * Any class that wants to "listen" to system events (e.g., AdministratorScreen)
 * must implement this interface.
 */
public interface Observer {
    
    /**
     * This method is called whenever the Model broadcasts an event.
     * 
     * @param message A description of the event (e.g., "Candidate booked a slot")
     */
    void update(String message);
}
