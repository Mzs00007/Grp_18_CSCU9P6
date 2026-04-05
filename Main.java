/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Recruitment;

import javax.swing.*;

/**
 *
 * @author Taha
 */
public class Main {

    public static void main(String[] args) {
    for (int i = 0; i < 3; i++) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}

}

