/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Recruitment;

/**
 *
 * @author Taha
 */
import java.util.ArrayList;

public class Recruiter {

    private String name;
    private ArrayList<Offer> offers;

    public Recruiter(String name) {
        this.name = name;
        offers = new ArrayList<>();
    }

    public void addOffer(Offer offer) {
        offers.add(offer);
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }
}

