package net.mormolhs.facebook.fbanalytics;


import net.mormolhs.facebook.fbanalytics.ui.LandingPage;

import javax.swing.*;

/**
 * Created by toikonomakos on 3/16/14.
 */
public class Main {


    public static void main(String[] args) {
        System.out.println("STARTING...");
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        LandingPage landingPage = new LandingPage();
        landingPage.run();

        System.out.println("ENDING...");
    }

}