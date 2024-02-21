package Syndrill.Model.Exceptions;

/**
 * Klasse der Exception, die passiert, wenn eine Sphäre nicht gefunden wurde
 */
public class SphaereNichtGefunden extends Exception {
    public SphaereNichtGefunden(String nachricht){
        super(nachricht);
    }
}
