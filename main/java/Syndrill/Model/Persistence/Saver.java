package Syndrill.Model.Persistence;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import Syndrill.Model.*;

/**
 * Klasse zur Speicherung des gesamten Graphes
 */
public class Saver {
    /**
     * Methode zur Speicherung des gesamten Graphes in eine Datei
     * @param graph Graph
     * @param filePath String
     * @throws IOException
     */
    public void save(Graph graph, String filePath) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath, false)))
        {
            oos.writeObject(graph);
        } catch(Exception ex) {
            throw new IOException("Graph konnte nicht gespeichert werden.");
        }

    }
}
