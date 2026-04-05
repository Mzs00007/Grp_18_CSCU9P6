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
 * Observable class that allows observers to subscribe and receive updates.
 * 
 * Your central Model should either EXTEND this class OR copy this logic.
 */
public class Observable {

    protected List<Observer> observers = new ArrayList<>();

    /**
     * Register an observer (e.g., AdministratorScreen).
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Notify all observers of an event.
     */
    public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(message);
        }
    }
}