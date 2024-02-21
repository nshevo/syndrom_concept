package Syndrill.Model;

import java.io.Serializable;

/**
 * Hilfsklasse einer Relation zur Darstellung von Kreisen
 */
public class Circle implements Serializable
{
    public double x, y;
    public int diameter;

    /**
     * Default-Konstruktor
     * @param x Koordinate
     * @param y Koordinate
     * @param diameter Durchmesser
     */
    public Circle(double x, double y, int diameter){
        this.x = x;
        this.y = y;
        this.diameter = diameter;
    }
}