package Syndrill.Model.Exceptions;

/**
 * Klasse der Exception, die passiert, wenn zwei gleiche Knoten interagieren
 */
public class GleicheKnoten extends Exception {
    public GleicheKnoten(String nachricht){
        super(nachricht);
    }
}
