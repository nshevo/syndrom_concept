package Syndrill.Model;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Klasse zur visuellen Darstellung der Kanten
 */
public class Relation implements Serializable {

    Point2D.Double startPoint;
    Point2D.Double endPoint;
    final Color color;
    final boolean withArrow;
    Circle circle;

    /**
     * Default-Konstruktor
     * @param startPoint
     * @param endPoint
     * @param farbe Color
     * @param withArrow boolean
     */
    public Relation(Point2D.Double startPoint, Point2D.Double endPoint, Color farbe, boolean withArrow) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.color = farbe;
        this.withArrow = withArrow;

        relationType();
    }

    /**
     * Legt den Relation-End-Zeichen fest
     */
    private void relationType(){
        if(!withArrow){
            circle = new Circle(endPoint.getX(), endPoint.getY(), 10);
        }
    }

    /*
     * Getter und Setter - Methoden
     */

    public Point2D.Double getStartPoint(){
        return startPoint;
    }
    public Point2D.Double getEndPoint(){
        return  endPoint;
    }

    public void setStartPoint(Point2D.Double startPoint){
        this.startPoint = startPoint;
    }
    public void setEndPoint(Point2D.Double endPoint){
        this.endPoint = endPoint;
        relationType();
    }

}

