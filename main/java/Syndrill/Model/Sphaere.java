package Syndrill.Model;

import Syndrill.Model.Exceptions.KnotenNichtGefunden;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasse zur Darstellung der Sphäre
 */
public class Sphaere extends GraphElemente {
    private List<Knoten> knotenList;
    private JPanel sphaereKomponent;

    /**
     * Default-Konstruktor
     * @param bezeichnung String
     * @param farbe Color
     */
    public Sphaere(String bezeichnung, Color farbe){
        super(bezeichnung,farbe);
        knotenList = new LinkedList<>();
    }

    /**
     * Methode legt den Sphärenkomponent fest
     * @param komponent
     */
    public void setSphaereKomponent(JPanel komponent){
        if(komponent == null){
            throw new NullPointerException("Sphärenkomponent ist null!");
        }
        sphaereKomponent = komponent;
    }

    /**
     * Methode gibt den Sphärenkomponent zurück
     * @return JPanel
     */
    public JPanel getSphaereKomponent(){
        if (sphaereKomponent == null){
            throw new  NullPointerException("Sphärenkomponent ist null!");
        }
        return sphaereKomponent;
    }

    /**
     * Methode gibt eine Liste aller Knoten zurück
     * @return
     */
    public List<Knoten> getKnotenList(){
        return knotenList;
    }

    /**
     * Methode zum Hinzufügen der Knoten
     * @param knoten
     */
    public void addKnoten(Knoten knoten){
        if(knoten == null){
            throw new NullPointerException("Knoten ist null");
        }
        knotenList.add(knoten);
    }

    /**
     * Methode zum Löschen der Knoten
     * @param knoten
     * @throws KnotenNichtGefunden
     */
    public void deleteKnoten(Knoten knoten) throws KnotenNichtGefunden {
        if(knoten == null){
            throw new NullPointerException("Knoten ist null");
        }

        if(!knotenList.contains(knoten)){
            throw new KnotenNichtGefunden("Knoten existiert nicht");
        }

        knotenList.remove(knoten);

    }

}
