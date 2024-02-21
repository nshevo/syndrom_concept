package Syndrill.Model;

import java.awt.*;
import java.io.Serializable;

/**
 * Oberklasse der GraphElemente
 */
public class GraphElemente implements Serializable {
    String bezeichnung;
    Color farbe;
    Dimension groesse;
    Point position;

    /**
     * Default-Konstruktor
     */
    public GraphElemente(){
        // Leeres Konstruktor
    }

    /**
     * Konstruktor
     * @param bezeichnung String
     * @param farbe Color
     */
    public GraphElemente(String bezeichnung, Color farbe) {
        if (bezeichnung.isEmpty() || bezeichnung.length() == 0) {
            throw new RuntimeException("Bezeichnung ist leer");
        }
        if (farbe == null) {
            throw new NullPointerException("Farbe ist null");
        }

        this.bezeichnung = bezeichnung;
        this.farbe = farbe;
    }

    /**
     * Konstruktor
     * @param bezeichnung String
     * @param farbe Color
     * @param groesse Dimension
     * @param position Point
     */
    public GraphElemente(String bezeichnung, Color farbe, Dimension groesse, Point position)
    {
        if(bezeichnung.isEmpty() || bezeichnung.length() == 0){
            throw new RuntimeException("Bezeichnung ist leer");
        }
        if(farbe == null){
            throw new NullPointerException("Farbe ist null");
        }
        if(groesse == null){
            throw new NullPointerException("Groesse ist null");
        }
        if(position == null){
            throw new NullPointerException("Position ist null");
        }

        this.bezeichnung = bezeichnung.trim();
        this.farbe = farbe;
        this.groesse = groesse;
        this.position = position;

    }

    /**
     * Methode gibt die Bezeichnung des Elementes zurück
     * @return String
     */
    public String getBezeichnung() {
        return bezeichnung;
    }

    /**
     * Methode legt die Bezeichnung des Elementes fest
     * @param bezeichnung String
     */
    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    /**
     * Methode gibt die Größe des Elementes zurück
     * @return Dimension
     */
    public Dimension getGroesse() {
        return groesse;
    }

    /**
     * Methode legt die Größe des Elementes fest
     * @param groesse
     */
    public void setGroesse(Dimension groesse) {
        this.groesse = groesse;
    }

    /**
     * Methode gibt die Farbe des Elementes zurück
     * @return Color
     */
    public Color getFarbe() {
        return farbe;
    }

    /**
     * Methode setzt die Farbe des Elementen fest
     * @param farbe Color
     */
    public void setFarbe(Color farbe) {
        this.farbe = farbe;
    }

    /**
     * Methode gibt die Position des Elementen zurück
     * @return
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Methode setzt die Position des Elementen fest
     * @param position Point
     */
    public void setPosition(Point position) {
        this.position = position;
    }
}
