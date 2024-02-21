package Syndrill.Model.Exceptions;

/**
 * Klasse der Exception, die passiert, wenn eine Kante nicht gefunden wurde
 */
public class KanteNichtGefunden extends Exception {
    public  KanteNichtGefunden(String nachricht){
        super(nachricht);
    }
}
