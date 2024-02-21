package Syndrill.Model.Exceptions;

/**
 * Klasse der Exception, die passiert, wenn ein Knoten nicht gefunden wurde
 */
public class KnotenNichtGefunden extends Exception {

    public KnotenNichtGefunden(String nachricht){
        super(nachricht);
    }

}
