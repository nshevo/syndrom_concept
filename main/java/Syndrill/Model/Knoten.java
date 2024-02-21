package Syndrill.Model;

import Syndrill.Model.Exceptions.KanteNichtGefunden;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasse zur Darstellung von Knoten
 */
public class Knoten extends GraphElemente {
    JPanel knotenKomponent;
    private List<Kante> kantenList;
    private Point internalPosition;

    /**
     * Default-Konstruktor
     * @param bezeichnung String
     * @param farbe Color
     */
    public Knoten(String bezeichnung, Color farbe){
        super(bezeichnung,farbe);
        kantenList = new LinkedList<>();
    }

    /**
     * Methode setzt innere Positionierung des Knoten innerhalb einer Sphäre fest
     * @param internalPosition
     */
    public void setInternalPosition(Point internalPosition){
        this.internalPosition = internalPosition;
    }

    /**
     * Methode gibt die innere Positionierung des Knoten innerhalb einer Sphäre zurück
     * @return
     */
    public Point getInternalPosition(){
        return internalPosition;
    }

    /**
     * Methode legt den Knotenkomponent fest
     * @param knotenKomponent JPanel
     */
    public void setKnotenKomponent(JPanel knotenKomponent) {
        this.knotenKomponent = knotenKomponent;

        this.setGroesse(knotenKomponent.getSize());
        this.setPosition(knotenKomponent.getLocationOnScreen());
        this.setInternalPosition(knotenKomponent.getBounds().getLocation());

        knotenKomponent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                Point position = knotenKomponent.getLocationOnScreen();
                Point internalPosition = knotenKomponent.getBounds().getLocation();
                setPosition(position);
                setInternalPosition(internalPosition);
            }
        });


    }

    /**
     * Methode gibt den Knotenkomponent zurück
     * @return JPanel
     */
    public JPanel getKnotenKomponent(){
        return knotenKomponent;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                // Jedes Knoten hat 2 Eingänge (eingangA und eingangV) und 2 Ausgänge (ausgangA und AusgangV)
                // A steht für Kantenart Abschwächend und V steht für Kantenart Verstärkend

                // (endknoten)  KnotenKomponent   (startKnoten)
                //           -- -- -- -- -- --
                //EingangA  |                 |
                //          |                 |
                //          |                 | AusgangAV
                //          |                 |
                // EingangV |                 |
                //           -- -- -- -- -- --

                // getX() -> Punkt ganz links unten im Komponent

                // boolean startKnoten true Anker von Eingang
                // boolean startKnoten false Anker von Ausgang
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Methode gibt ein AnkerPunkt für Relation anhand der Parameter
     * @param kantenart KantenArt
     * @param startKnoten boolean
     * @return Point2D.Double
     */
    public Point2D.Double getAnker(KantenArt kantenart, boolean startKnoten){

        String OS = System.getProperty("os.name", "generic").toLowerCase();

        Point2D.Double ankerEingangA = new Point2D.Double(getPosition().getX(),
                getPosition().getY() - getGroesse().getHeight() + 10);

        Point2D.Double ankerEingangV = new Point2D.Double(getPosition().getX(),
                getPosition().getY());

        Point2D.Double ankerAusgangAV = new Point2D.Double(getPosition().getX() + getGroesse().getWidth(),
                getPosition().getY() - getGroesse().getHeight() / 2   );

        if(OS.split(" ").length > 1){
            if(OS.split(" ")[0].equals("windows")){
                ankerEingangA = new Point2D.Double(getPosition().getX(),
                        getPosition().getY());

                ankerEingangV = new Point2D.Double(getPosition().getX(),
                        getPosition().getY() + getGroesse().getHeight());

                ankerAusgangAV = new Point2D.Double(getPosition().getX() + getGroesse().getWidth(),
                        getPosition().getY() + getGroesse().getHeight() / 2 );
            }

        }



        if(kantenart.equals(KantenArt.ABSCHWAECHEND)){
            if(startKnoten){
                return ankerAusgangAV;
            }else{
                return ankerEingangA;
            }

        }else if(kantenart.equals(KantenArt.VERSTAERKEND)){
            if(startKnoten){
                return ankerAusgangAV;
            }else{
                return ankerEingangV;
            }

        }

        return null;
    }

    /**
     * Methode fügt eine Kante dem Knoten hinzu
     * @param kante Kante
     */
    public void addKante(Kante kante){
        if(kante == null){
            throw new NullPointerException("Kante ist null");
        }
        kantenList.add(kante);
    }

    /**
     * Methode löscht eine Kante aus dem Knoten
     * @param kante Kante
     * @throws NullPointerException passiert wenn Kante null ist
     * @throws KanteNichtGefunden Kante existiert nicht
     */
    public void deleteKante(Kante kante) throws NullPointerException, KanteNichtGefunden {
        if(kante == null){
            throw new NullPointerException("Kante ist null");
        }
        if(!kantenList.contains(kante)){
            throw new KanteNichtGefunden("Diese Kante existiert nicht");
        }
        kantenList.remove(kante);
    }

    /**
     * Methode gibt die Liste aller Kanten zurück
     * @return
     */
    public List<Kante> getKantenList(){
        return kantenList;
    }

}
