package Syndrill.Model.Persistence;

import Syndrill.Model.Graph;

import java.io.*;

/**
 * Klasse zum Laden des gesamten Graphes
 */
public class Loader {

    /**
     * Methode zum Laden des gesamten Graphes aus der Datei
     * @param filePath String
     * @return Graph
     * @throws IOException
     */
    public Graph loadGraph(String filePath) throws FileNotFoundException, IOException {

        if(filePath.isEmpty()){
            throw new FileNotFoundException("Datei wurde nicht gefunden");
        }

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Graph g = (Graph) ois.readObject();
            return g;
        } catch (Exception e) {
            throw new IOException("Graphdatei konnte nicht geöffnet werden.");
        }
    }
}
