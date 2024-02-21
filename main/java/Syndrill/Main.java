package Syndrill;

import Syndrill.Controller.Controller;
import Syndrill.Logic.Logic;

/**
 * Klasse zum Starten der Application
 */

public class Main {
    /**
     * Main-Methode der Application
     * @param args
     */
    public static void main(String[] args){
        try{
            Logic mainLogic = new Logic();
            Controller mainController = new Controller(mainLogic);
            mainController.startApplication();
        }catch(Exception e){
            System.err.println("Etwas ist schief gelaufen.");
            e.printStackTrace();
        }

    }
}
