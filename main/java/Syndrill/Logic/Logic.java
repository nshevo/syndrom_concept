package Syndrill.Logic;

import Syndrill.Model.*;
import Syndrill.Model.Exceptions.GleicheKnoten;
import Syndrill.Model.Exceptions.KanteNichtGefunden;
import Syndrill.Model.Persistence.Loader;
import Syndrill.Model.Persistence.Saver;

import javax.naming.NamingException;
import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Klasse der Application-Logic
 */
public class Logic {
    private Loader loader;
    private Saver saver;
    private Graph currentGraph;
    private JPanel currentSphaereKomponent;
    private Sphaere currentSphere;

    private boolean kantenZeichnung = false;
    private Knoten startKnoten = null;
    private Knoten endKnoten = null;
    private KantenArt currentKantenArt = null;
    private KantenKomponent alleKantenKomponente;

    /**
     * Default-Konstruktor
     */
    public Logic(){
        loader = new Loader();
        saver = new Saver();
        currentGraph = new Graph();
        alleKantenKomponente = new KantenKomponent();
    }

    /**
     * Methode setzt den aktuellen Sphärenkomponent fest
     * @param currentSphaereKomponent JPanel
     */
    public void setCurrentSphaereKomponent(JPanel currentSphaereKomponent) {
        this.currentSphaereKomponent = currentSphaereKomponent;
    }

    /**
     * Methode gibt den aktuellen Sphärenkomponent zurück
     * @return JPanel
     */
    public JPanel getCurrentSphaereKomponent(){
        return currentSphaereKomponent;
    }

    /**
     * Methode setzt die aktuelle Sphäre fest
     * @param sphaere Sphaere
     */
    public void setCurrentSphere(Sphaere sphaere) {
        if(sphaere == null){
            throw new NullPointerException("Aktuelle Sphäre ist null");
        }
        currentSphere = sphaere;
    }

    /**
     * Methode gibt aktuelle Sphäre zurück
     * @return Sphaere
     */
    public Sphaere getCurrentSphere(){
        return currentSphere;
    }

    /**
     * Methode zum Erstellen einer Sphaere
     * @param sphaereName String
     * @param farbe Color
     * @return Sphaere
     */
    public Sphaere createSphere(String sphaereName, Color farbe){
        Sphaere sphaere = new Sphaere(sphaereName, farbe);
        return sphaere;
    }

    /**
     * Methode zum Bearbeiten der Sphäre
     * @param sphaere Sphaere
     * @param farbe Color
     */
    public void editSphere(Sphaere sphaere, Color farbe){
        currentGraph.editSphaere(sphaere,farbe);
    }

    /**
     * Methode fügt eine Sphäre zum aktuellen Graphen hinzu
     * @param sphaere Sphaere
     */
    public void addSphere(Sphaere sphaere) {
        currentGraph.addSphaere(sphaere);
    }

    /**
     * Hilfsmethode zur Namenbesetzung innerhalb eines Graphen
     * @param sphaere
     * @throws NamingException Sphärenname bereits besetzt
     * @throws IllegalArgumentException Sphäre existiert bereits
     */
    public void checkNames(Sphaere sphaere) throws NamingException, IllegalArgumentException {
        currentGraph.checkNames(sphaere);
    }

    /**
     * Methode zum Löschen der Sphäre aus dem aktuellen Graphen
     * @param sphaere Sphaere
     */
    public void deleteSphere(Sphaere sphaere) {
        if(!currentGraph.getSphaerenList().contains(sphaere)){
            throw new NoSuchElementException("Sphäre nicht gefunden!");
        }

        currentGraph.getSphaerenList().remove(sphaere);
    }

    /**
     * Methode fügt ein Sphärenkomponent zur zugehörigen Sphäre hinzu
     * @param sphaere1 Sphaere
     * @param spKomponent JPanel
     */
    public void addSphereKomponent(Sphaere sphaere1, JPanel spKomponent){
        for(Sphaere sphaere: currentGraph.getSphaerenList()){
            if(sphaere == sphaere1){
                sphaere.setSphaereKomponent(spKomponent);
                sphaere.setGroesse(spKomponent.getSize());
                sphaere.setPosition(spKomponent.getLocation());
                break;
            }
        }
    }

    /**
     * Methode gibt einen neuangelegten Knoten zurück
     * @param bezeichnung String
     * @param farbe Color
     * @return Knoten
     */
    public Knoten createKnoten(String bezeichnung, Color farbe) {
        return new Knoten(bezeichnung, farbe);
    }

    /**
     * Methode zum Bearbeiten des Knotens im aktuellen Graphen
     * @param knoten Knoten
     * @param farbeNeu Color
     */
    public void editKnoten(Knoten knoten, Color farbeNeu) {
        Sphaere s = getCurrentSphere();
        if(currentGraph.getSphaerenList().contains(s)){
            int sphereIndex = currentGraph.getSphaerenList().indexOf(s);
            for (Knoten kn : currentGraph.getSphaerenList().get(sphereIndex).getKnotenList()){
                if(kn.equals(knoten)){
                    kn.setFarbe(farbeNeu);
                    kn.getKnotenKomponent().setBackground(farbeNeu);
                }
            }
        }
    }

    /**
     * Methode fügt ein Knoten zur einer Sphäre hinzu
     * @param sphaere Sphaere
     * @param knoten Knoten
     */
    public void addKnoten(Sphaere sphaere, Knoten knoten) {
        if(currentGraph.getSphaerenList().contains(sphaere)){
            int sphereIndex = currentGraph.getSphaerenList().indexOf(sphaere);
            currentGraph.getSphaerenList().get(sphereIndex).addKnoten(knoten);

        }
    }

    /**
     * Methode setzt den K
     * @param status
     */
    public void setKantenZeichnung(boolean status)
    {
        kantenZeichnung = status;
    }

    /**
     * Methode gibt ein Status der Kantenzeichnung zurück
     * @return boolean true - kantenZeichnung aktiv / false - aus
     */
    public boolean getKantenZeichnung(){
        return kantenZeichnung;
    }

    /**
     * Hilfsmethode zum Setzen der Start- und Endknoten der Kante
     * @param knoten Knoten
     * @return true -> beide Knoten gesetzt | false -> nur ein Knoten wurde gesetzt
     * @throws GleicheKnoten passiert wenn Start- und Endknoten gleich sind
     */
    public boolean setKantenKnoten(Knoten knoten) throws GleicheKnoten {

        if(startKnoten == null){
            startKnoten = knoten;
            return false;
        }else if(endKnoten == null){
            endKnoten = knoten;
            if(endKnoten.equals(startKnoten)){
                setCurrentKantenArt(null);
                throw new GleicheKnoten("Start- und Endknoten müssen unterschiedlich sein");
            }
            return true;
        }

        if(startKnoten != null && endKnoten != null){
            startKnoten = null;
            endKnoten = null;
            return setKantenKnoten(knoten);
        }

        return false;
    }

    /**
     * Hilfsmethode liefert die KantenArt der zukreierenden Kanten
     * @return
     */
    public KantenArt getCurrentKantenArt(){
        return currentKantenArt;
    }

    /**
     * Methode gibt eine erstellte Kante zurück
     * @param farbe Color
     * @return Kante
     */
    public Kante createKante(Color farbe) {

        if(startKnoten != null && endKnoten != null){
            setKantenZeichnung(false);

            Kante k = new Kante(startKnoten, endKnoten);
            k.setKantenArt(currentKantenArt);
            k.setFarbe(farbe);
            return k;
        }

        return null;
    }

    /**
     * Methode zum Löschen der Kante zwischen zwei ausgewählten Knoten
     * @throws KanteNichtGefunden
     */
    public void deleteKante() throws KanteNichtGefunden {
        if(startKnoten != null && endKnoten != null){
            setKantenZeichnung(false);

            for(Kante kante : anyMutualKanten()){
                deleteKantenKomponent(kante);
                startKnoten.deleteKante(kante);
                endKnoten.deleteKante(kante);
            }
        }
    }

    /**
     * Methode gibt eine Liste der Kanten zurück, welche zwischen zwei ausgewählten Knoten existieren
     * @return
     */
    private LinkedList<Kante> anyMutualKanten() {
        LinkedList<Kante> allMutualKanten = new LinkedList<>();
        for(Kante kante : startKnoten.getKantenList())
        {
            if(kante.getEndKnoten().equals(endKnoten)){
                allMutualKanten.add(kante);
            }
        }
        return allMutualKanten;
    }

    /**
     * Hilfsmethode zum Hinzufügen der Kante den Start- und Endknoten
     * @param kante
     */
    public void addKanteToSelectedKnoten(Kante kante){
        startKnoten.addKante(kante);
        endKnoten.addKante(kante);
    }

    /**
     * Methode zum Anzeigen des Kantenkomponentes
     * @param kante Kante
     */
    public void createKantenKomponent(Kante kante){
        kante.drawKante(alleKantenKomponente);
    }

    /**
     * Methode gibt alle gezeichnete Kanten in einem Element zurück
     * @return KantenKomponent
     */
    public KantenKomponent getAlleKantenKomponente(){
        return alleKantenKomponente;
    }

    /**
     * Methode zum Löschen des Knotens aus aktuellem Graph
     * @param knoten Knoten
     * @return boolean true -> Löschen erfolgreich | false -> Löschen fehlgeschlagen
     */
    public boolean deleteKnoten(Knoten knoten) {
        Sphaere s = getCurrentSphere();
        int indexSphere = currentGraph.getSphaerenList().indexOf(s);

        if(currentGraph.getSphaerenList().get(indexSphere).getKnotenList().remove(knoten)){
            return true;
        }

        return false;
    }

    /**
     * Methode zum Setzen des aktuellen KantenArtes des zukreierenden Kanten fest.
     * @param kantenArt KantenArt
     */
    public void setCurrentKantenArt(KantenArt kantenArt){
        currentKantenArt = kantenArt;
    }

    /**
     * Methode zum Löschen des Kantenkomponentes der zugehörigen Kante
     * @param kante Kante
     */
    public void deleteKantenKomponent(Kante kante) {
        alleKantenKomponente.deleteLines(kante.getRelation().getStartPoint(),kante.getRelation().getEndPoint());
    }

    /**
     * Hilfsmethode zum Zurücksetzen der Logic-Variablen
     */
    public void createNewGraph() {
        currentSphere = null;
        currentSphaereKomponent = null;
        currentGraph = new Graph();
        alleKantenKomponente = new KantenKomponent();
    }

    /**
     * Hilfsmethode zum Zurücksetzen der Knoten für Kantenzeichnung
     */
    public void deleteRelationLink(){
        startKnoten = null;
        endKnoten = null;
    }

    /**
     * Methode zum Speichern des gesamten Graphen
     * @param filePath String
     * @throws IOException
     */
    public void saveGraph(String filePath) throws IOException {
        saver.save(currentGraph, filePath);
    }

    /**
     * Methode zum Laden des gesamten Graphen
     * @param filePath
     * @throws IOException
     */
    public void loadGraph(String filePath) throws IOException {
        Graph g = loader.loadGraph(filePath);
        currentGraph = g;
    }

    /**
     * Methode gibt aktuellen Graph zurück
     * @return Graph
     */
    public Graph getCurrentGraph(){
        return currentGraph;
    }

    /**
     * Hilfsmethode löscht Kanten eines Knoten von allen anderen Knoten
     * @param knoten
     */
    public void deleteKantenOfKnoten(Knoten knoten) {
        for(Kante kanteSphaere : knoten.getKantenList()){
            for(Sphaere s : currentGraph.getSphaerenList()){
                for(Knoten k : s.getKnotenList()){
                    if(k.getKantenList().contains(kanteSphaere)){
                        k.getKantenList().remove(kanteSphaere);
                    }
                }
            }
        }
    }
}
