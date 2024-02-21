package Syndrill.Model;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;

/**
 * Klasse zur Darstellung von Kanten
 */
public class Kante extends GraphElemente {
    public static int STAERKE = 1;
    private Knoten startKnoten;
    private Knoten endKnoten;
    private KantenArt kantenArt;
    private Relation relation;

    /**
     * Konstruktor
     * @param startKnoten Knoten
     * @param endKnoten Knoten
     */
    public Kante(Knoten startKnoten, Knoten endKnoten){
        setStartKnoten(startKnoten);
        setEndKnoten(endKnoten);
    }

    /**
     * Methode setzt die KantenArt fest
     * @param kantenArt
     */
    public void setKantenArt(KantenArt kantenArt) {
        if(kantenArt == null){
            throw new NullPointerException("Kantenart ist null");
        }
        this.kantenArt = kantenArt;
    }

    /**
     * Methode setzt StartKnoten der Kante fest
     * @param startKnoten Knoten
     */
    public void setStartKnoten(Knoten startKnoten) {
        if(startKnoten == null){
            throw new NullPointerException("Anfangsknoten ist null");
        }
        this.startKnoten = startKnoten;
    }

    /**
     * Methode gibt Endknoten zurück
     * @return Knoten
     */
    public Knoten getEndKnoten() {
        return endKnoten;
    }

    /**
     * Methode legt Endknoten fest
     * @param endKnoten
     */
    public void setEndKnoten(Knoten endKnoten) {
        if(endKnoten == null){
            throw new NullPointerException("Endknoten ist null");
        }
        this.endKnoten = endKnoten;
    }

    /**
     * Methode zum Zeichnen der Kante im KantenKomponent
     * @param kantenKomponent
     */
    public void drawKante(KantenKomponent kantenKomponent) {
        // left top corner of each knoten
        Point2D.Double startP = startKnoten.getAnker(kantenArt,true);
        Point2D.Double endP = endKnoten.getAnker(kantenArt,false);

        this.relation = kantenKomponent.addLine(startP, endP, farbe, kantenArt);

        startKnoten.getKnotenKomponent().getParent().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                System.out.println("STARTK: " + startKnoten.getInternalPosition());

                // correct relations when sphere layout changes
                startKnoten.setPosition(startKnoten.getKnotenKomponent().getLocationOnScreen());
                startKnoten.getKnotenKomponent().revalidate();
                startKnoten.getKnotenKomponent().repaint();

                relation.setStartPoint(startKnoten.getAnker(kantenArt, true));
                kantenKomponent.refresh();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                // correct relations when sphere layout changes
                startKnoten.setPosition(startKnoten.getKnotenKomponent().getLocationOnScreen());
                startKnoten.getKnotenKomponent().revalidate();
                startKnoten.getKnotenKomponent().repaint();

                relation.setStartPoint(startKnoten.getAnker(kantenArt, true));

                kantenKomponent.refresh();
            }
        });

        startKnoten.getKnotenKomponent().addComponentListener(new ComponentAdapter() {

            @Override
            public void componentMoved(ComponentEvent e) {
                relation.setStartPoint(startKnoten.getAnker(kantenArt, true));
                kantenKomponent.refresh();
            }
        });

        endKnoten.getKnotenKomponent().getParent().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                System.out.println("ENDK: " + endKnoten.getInternalPosition());



                // correct relations when sphere layout changes
                endKnoten.setPosition(endKnoten.getKnotenKomponent().getLocationOnScreen());
                endKnoten.getKnotenKomponent().revalidate();
                endKnoten.getKnotenKomponent().repaint();


                relation.setEndPoint(endKnoten.getAnker(kantenArt,false));
                kantenKomponent.refresh();
            }

            @Override
            public void componentMoved(ComponentEvent e) {

                // correct relations when sphere layout changes
                endKnoten.setPosition(endKnoten.getKnotenKomponent().getLocationOnScreen());
                endKnoten.getKnotenKomponent().revalidate();
                endKnoten.getKnotenKomponent().repaint();


                relation.setEndPoint(endKnoten.getAnker(kantenArt,false));
                kantenKomponent.refresh();
            }
        });

        endKnoten.getKnotenKomponent().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                relation.setEndPoint(endKnoten.getAnker(kantenArt,false));
                kantenKomponent.refresh();
            }
        });

    }

    /**
     * Gibt die Relation der Kante zurück
     * @return
     */
    public Relation getRelation(){
        return relation;
    }


}
