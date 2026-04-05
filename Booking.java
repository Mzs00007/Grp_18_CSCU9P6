/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Recruitment;

/**
 *
 * @author Taha
 */
public class Booking {

    private String candidateName;
    private String time;
    private String offerName;

    public Booking(String candidateName, String time, String offerName) {

        this.candidateName = candidateName;
        this.time = time;
        this.offerName = offerName;
    }

    public String toString() {

        return candidateName + " - " + time + " - " + offerName;

    }
}
