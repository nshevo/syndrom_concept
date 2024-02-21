package Syndrill.Model;

import Syndrill.Model.Exceptions.SphaereNichtGefunden;

import javax.naming.NamingException;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasse zur Darstellung eines Graphen
 */
public class Graph extends GraphElemente {
    private List<Sphaere> sphaerenList;

    /**
     * Default-Konstruktor
     */
    public Graph(){
        super();
        sphaerenList = new LinkedList<>();
    }

    /**
     * Methode zum Überprüfen der Einmaligkeit der Sphärennamen
     * @param sphaere Sphaere
     * @throws NamingException Sphäre mit gleichem Namen existiert bereits
     * @throws IllegalArgumentException Sphäre existiert bereits
     */
    public void checkNames(Sphaere sphaere) throws NamingException, IllegalArgumentException {
        if(!sphaerenList.contains(sphaere)){
            for (Sphaere s : sphaerenList){
                if(s.getBezeichnung().equals(sphaere.getBezeichnung())){
                    throw new NamingException("Sphäre mit gleichem Namen existiert bereits");
                }
            }
        }else {
            throw new IllegalArgumentException("Sphäre existiert bereits");
        }
    }

    /**
     * Methode zum Hinzufügen der Sphäre
     * @param sphaere Sphaere
     * @throws NullPointerException
     */
    public void addSphaere(Sphaere sphaere) throws NullPointerException {

        if(sphaere == null){
            throw new NullPointerException("Sphäre ist null");
        }

        sphaerenList.add(sphaere);
    }

    /**
     * Methode zum Bearbeiten der Sphäre
     * @param sphaere
     * @param farbe
     */
    public void editSphaere(Sphaere sphaere, Color farbe) {
        if(sphaere == null){
            throw new NullPointerException("Sphäre ist null");
        }

        if(sphaerenList.contains(sphaere)){
            for (Sphaere s : sphaerenList){
                if(s == sphaere){
                    s.setFarbe(farbe);
                }
            }
        }else{
            throw new RuntimeException("Sphäre wurde nicht gefunden");
        }
    }

    /**
     * Methode zum Löschen der Sphäre
     * @param sphaere
     * @throws SphaereNichtGefunden
     */
    public void deleteSphaere(Sphaere sphaere) throws SphaereNichtGefunden {

        if(sphaere == null){
            throw new NullPointerException("Sphäre ist null");
        }

        if(!sphaerenList.contains(sphaere)){
            throw new SphaereNichtGefunden("Sphäre existiert nicht");
        }

        sphaerenList.remove(sphaere);

    }

    /**
     * Methode gibt eine Liste aller Sphären zurück
     * @return
     */
    public List<Sphaere> getSphaerenList() {
        return sphaerenList;
    }

}
