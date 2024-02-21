package Syndrill.Controller;

import Syndrill.Logic.Logic;
import Syndrill.Model.*;
import Syndrill.Model.Exceptions.GleicheKnoten;
import Syndrill.Model.Exceptions.KanteNichtGefunden;
import Syndrill.View.MainWindow;


import javax.imageio.ImageIO;
import javax.naming.NamingException;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;

/**
 * Klasse des Application-Controllers
 */
public class Controller {
    private Logic logic;
    private MainWindow mainWindow;

    /**
     * Controller Konstruktor
     * @param logic Logic
     */
    public Controller(Logic logic){
        this.logic = logic;
    }

    /**
     * Methode zum Kreieren des Hauptfensters
     * @throws IOException
     */
    private void createMainApplication() throws IOException {
        mainWindow = new MainWindow(this);
        mainWindow.setVisible(true);
    }

    /**
     * Methode zum Angeizegen der Nachricht
     * @param nachricht
     * @param titel
     * @param nachrichtTyp
     */
    private void showMessage(String nachricht, String titel, int nachrichtTyp){
        JOptionPane.showMessageDialog(mainWindow, nachricht, titel,
                nachrichtTyp);
    }

    /**
     * Methode zum Starten der Application
     * @throws IOException
     */
    public void startApplication() throws IOException {
        createMainApplication();
    }

    /**
     * Methode zum Schließen der Application
     */
    public void applicationExit() {
        System.exit(0);
    }

    /**
     * Methode ruft Relayout-Methode des View
     */
    private void refreshGraph(){
        mainWindow.refreshUI();
    }

    /**
     * Methode gibt aktuelle Sphärenpanel zurück
     * @return JPanel
     */
    private JPanel getSpherePanel(){
        return mainWindow.getSpherePanel();
    }

    /**
     * Methode zum Exportieren des Graphen als PDF
     * @param panel JPanel zuexportierende Panel
     * @param glassPane zuexportierende GlassPane
     * @param fileName Dateiname für Speicherung
     */
    public void saveToPDF(JPanel panel, Component glassPane, String fileName) {

        File imgFile = new File("glassPane.png");

        BufferedImage img = new BufferedImage(glassPane.getWidth(), glassPane.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        glassPane.paint(g);
        g.dispose();

        try{
            ImageIO.write(img, "png", imgFile);
        }catch(IOException e){
            e.printStackTrace();
        }

        PdfDocument pdf = null;
        try {
            pdf = new PdfDocument(new PdfWriter(fileName));
            pdf.addNewPage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        PdfPage page = pdf.getFirstPage();

        int rotate = page.getRotation();
        if(rotate == 0){
            page.setRotation(90);
        }

        BufferedImage jImg = new BufferedImage(panel.getWidth(),
                panel.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        panel.paint(jImg.createGraphics());

        com.itextpdf.layout.element.Image itextImg = null;
        try {
            itextImg = new com.itextpdf.layout.element.Image(
                    ImageDataFactory.create(jImg, null));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageData imageData = null;
        try {
            imageData = ImageDataFactory.create("glassPane.png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Document document = new Document(pdf);

        itextImg.setRotationAngle(Math.toRadians(90));
        itextImg.setFixedPosition(0,0);
        itextImg.scaleToFit(page.getPageSize().getHeight(), page.getPageSize().getWidth());
        document.add(itextImg);

        com.itextpdf.layout.element.Image pdfImg = new com.itextpdf.layout.element.Image(imageData);

        pdfImg.setRotationAngle(Math.toRadians(90));
        // Y Koordinaten Anpassung
        // - 17 mac
        // - 30 win

        String OS = System.getProperty("os.name", "generic").toLowerCase();
        int left = -17;

        if(OS.split(" ").length > 1){
            if( OS.split(" ")[0].equals("windows")){
                left = -15;
            }

        }

        pdfImg.setFixedPosition(left,0);
        pdfImg.scaleToFit(page.getPageSize().getHeight(), page.getPageSize().getWidth());

        document.add(pdfImg);

        document.close();
    }

    /**
     * Methode liefert das Pfad der ausgewählten Ordner sowie Dateinamen mit Addition des Formates zurück
     * @param format String
     * @return String
     */
    public String chooseDirectory(String format){
        File fileToSave = null;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Graph speichern unter...");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        int result = fileChooser.showSaveDialog(mainWindow);

        if (result == JFileChooser.APPROVE_OPTION) {
            fileToSave = fileChooser.getSelectedFile();
        }

        return fileToSave.getAbsolutePath()+"."+format;

    }
    /**
     * Methode liefert das Pfad der zuladender Graphdatei zurück
     * @return String
     */
    public String chooseFile() {
        File fileToSave = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Graphdatei auswählen");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        int result = fileChooser.showOpenDialog(mainWindow);

        if (result == JFileChooser.APPROVE_OPTION) {
            fileToSave = fileChooser.getSelectedFile();
        }

        return fileToSave.getAbsolutePath();
    }

    /**
     * Methode zum Anliegen eines neuen Graphes
     */
    public void createNewGraph() {
        allKanten = new LinkedList<>();
        logic.createNewGraph();
        getSpherePanel().removeAll();
        refreshGraph();
    }

    /**
     * Methode zum Speichern des gesamten Graphes unter einer Datei
     * @param filePath String
     */
    public void saveGraph(String filePath) {
        try {
            logic.saveGraph(filePath);
            showMessage("Graph wurde erfolgreich abgespeichert", "Aktion erfolgreich", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) {
            showMessage(e.getMessage(),"Graphspeicherung fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Methode zum Setzen des aktuellen Sphärenkomponentes
     * @param sphereKomponent JPanel
     */
    public void setCurrentSphereKomponent(JPanel sphereKomponent){
        logic.setCurrentSphaereKomponent(sphereKomponent);
    }
    /**
     * Methode gibt den aktuellen Sphärenkomponent zurück
     * @return JPanel
     */
    public JPanel getCurrentSphereKomponent(){
        return logic.getCurrentSphaereKomponent();
    }
    /**
     * Methode setzt die aktuelle Sphäre
     * @param sphaere Sphaere
     */
    public void setCurrentSphere(Sphaere sphaere) {
        try{
            logic.setCurrentSphere(sphaere);
        }catch (NullPointerException e){
            JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "Sphäre wurde nicht gefunden", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Methode zum Löschen der aktuellen Sphäre
     */
    public void deleteCurrentSphere() {
        deleteSphere(logic.getCurrentSphere());
    }

    /**
     * Methode zeigt ein Dialog zum Erstellen der Sphäre
     */
    public void createSphere(){
        if(mainWindow == null){
            throw new NullPointerException("MainApplication ist null");
        }

        mainWindow.displayCreateSphereDialog();
    }
    /**
     * Methode zum Erstellen der Sphäre und dessen UI-Komponente
     * @param sphereName String
     * @param farbe Color
     */
    public void createSphere(String sphereName, Color farbe){
        try {
            Sphaere sp = logic.createSphere(sphereName, farbe);
            logic.checkNames(sp);
            JPanel spKomponent = createSphereKomponent(sp);
            displaySphereKomponent(spKomponent);
            logic.addSphere(sp);
            logic.addSphereKomponent(sp,spKomponent);
            repaintKanten();
            JOptionPane.showMessageDialog(mainWindow, "Sphäre wurde erfolgreich hinzugefügt", "Aktion erfolgreich", JOptionPane.INFORMATION_MESSAGE);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "Sphäre wurde nicht erstellt", JOptionPane.ERROR_MESSAGE);
        }catch (IllegalArgumentException | NamingException e) {
            JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "Sphäre wurde nicht erstellt", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Methode zum Bearbeiten der Sphäre
     * @param sphaere Sphaere
     * @param farbe Color
     */
    public void editSphere(Sphaere sphaere, Color farbe){
        try{
            logic.editSphere(sphaere, farbe);
            sphaere.getSphaereKomponent().setBackground(farbe);
            refreshGraph();
            JOptionPane.showMessageDialog(mainWindow, "Sphäre wurde erfolgreich bearbeitet", "Aktion erfolgreich", JOptionPane.INFORMATION_MESSAGE);
        }catch (NullPointerException e){
            JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "Sphäre wurde nicht bearbeitet", JOptionPane.ERROR_MESSAGE);
        }catch (RuntimeException e){
            JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "Sphäre wurde nicht bearbeitet", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Methode zum Löschen der Sphäre
     * @param sphaere Sphaere
     */
    private void deleteSphere(Sphaere sphaere){
        try{
            logic.deleteSphere(sphaere);
            deleteSphereRelations(sphaere);
            deleteSphereKomponent();
            refreshGraph();
        }catch (NoSuchElementException e){
            JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "Sphäre wurde nicht gelöscht", JOptionPane.ERROR_MESSAGE);
        }catch (NullPointerException e){
            JOptionPane.showMessageDialog(mainWindow, "Keine zugehörige Sphärekomponente wurde gefunden", "Sphäre wurde nicht gelöscht", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void deleteSphereRelations(Sphaere sphaere) {
        for(Knoten k : sphaere.getKnotenList()){
            logic.deleteKantenOfKnoten(k);
            deleteRelationen(k);
        }
    }

    /**
     * Methode gibt einen erstellten Sphärenkomponent zurück
     * @param sphaere Sphaere
     * @return JPanel
     */
    public JPanel createSphereKomponent(Sphaere sphaere){
        if(sphaere == null){
            throw new NullPointerException("Sphäre ist null");
        }
        return mainWindow.createSphereKomponent(sphaere);
    }
    /**
     * Methode zum Anzeigen der Sphärenkomponente
     * @param sphaerenKomponent JPanel
     */
    public void displaySphereKomponent(JPanel sphaerenKomponent){
        if(sphaerenKomponent == null){
            throw new NullPointerException("Sphärenkomponent ist null");
        }
        mainWindow.displaySphereKomponent(sphaerenKomponent);

    }
    /**
     * Methode zum Löschen des aktuellen Sphärenkomponentes
     */
    private void deleteSphereKomponent() {
        JPanel spherePanel = getSpherePanel();
        JPanel sphereKomponent = logic.getCurrentSphaereKomponent();

        spherePanel.remove(sphereKomponent);
    }


    /**
     * Methode zeigt ein Dialog zum Erstellen des Knoten
     */
    public void createKnoten(){
        if(mainWindow != null){
            mainWindow.displayCreateKnotenDialog();
        }
    }
    /**
     * Methode zum Erstellen des Knoten anhand der zwei Parameter
     * @param bezeichnung String
     * @param farbe Color
     */
    public void createKnoten(String bezeichnung, Color farbe) {
        try{
            Knoten knoten = logic.createKnoten(bezeichnung, farbe);
            JPanel knotenKomponent = createKnotenKomponent(knoten);
            displayKnotenKomponent(knotenKomponent);
            Sphaere currentSp = logic.getCurrentSphere();
            knoten.setKnotenKomponent(knotenKomponent);
            logic.addKnoten(currentSp,knoten);
            JOptionPane.showMessageDialog(mainWindow, "Knoten wurde erfolgreich erstellt", "Aktion erfolgreich", JOptionPane.INFORMATION_MESSAGE);
        }catch (NullPointerException e){
            JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "Knoten konnte nicht erstellt werden", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Methode zum Bearbeiten des Knotens
     * @param knoten Knoten
     * @param farbeNeu Color
     */
    public void editKnoten(Knoten knoten, Color farbeNeu) {
        try{
            logic.editKnoten(knoten, farbeNeu);
            JOptionPane.showMessageDialog(mainWindow, "Knoten wurde erfolgreich bearbeitet", "Aktion erfolgreich", JOptionPane.INFORMATION_MESSAGE);
        }catch (NullPointerException e){
            JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "Knoten wurde nicht bearbeitet", JOptionPane.ERROR_MESSAGE);
        }catch (RuntimeException e){
            JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "Knoten wurde nicht bearbeitet", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Methode zum Löschen des Knotens
     * @param knoten Knoten
     */
    public void deleteKnoten(Knoten knoten){
        try{
            if(logic.deleteKnoten(knoten)){
                deleteRelationen(knoten);
                deleteKnotenKomponent(knoten);
                mainWindow.refreshUI();
                JOptionPane.showMessageDialog(mainWindow, "Knoten wurde erfolgreich gelöscht", "Aktion erfolgreich", JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(mainWindow, "Knoten wurde nicht gelöscht", "Fehler", JOptionPane.INFORMATION_MESSAGE);
            }
        }catch (NullPointerException e){
            JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "Knoten wurde nicht bearbeitet", JOptionPane.ERROR_MESSAGE);
        }catch (RuntimeException e){
            JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "Knoten wurde nicht bearbeitet", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Methode löscht alle Kanten-Relationen eines Knoten
     * @param knoten Knoten
     */
    private void deleteRelationen(Knoten knoten) {
        for(Kante k : knoten.getKantenList()){
            logic.getAlleKantenKomponente().deleteLines(k.getRelation().getStartPoint(), k.getRelation().getEndPoint());
        }
        logic.getAlleKantenKomponente().refresh();
    }

    /**
     * Methode liefert ein Knotenkomponent zum Knoten zurück
     * @param knoten Knoten
     * @return JPanel
     */
    private JPanel createKnotenKomponent(Knoten knoten){
        return mainWindow.createKnotenKomponent(knoten);
    }
    /**
     * Methode zum Anzeigen des Knotenkomponentes
     * @param knotenKomponent JPanel
     */
    private void displayKnotenKomponent(JPanel knotenKomponent) {
        mainWindow.displayKnotenKomponent(knotenKomponent);
    }
    /**
     * Methode zum Löschen des Knotenkomponentes
     * @param knoten Knoten
     */
    private void deleteKnotenKomponent(Knoten knoten){
        int index = 0;
        for(Component c : getCurrentSphereKomponent().getComponents()){
            if(c.equals(knoten.getKnotenKomponent())){
                break;
            }
            index += 1;
        }

        getCurrentSphereKomponent().remove(index);
    }


    /**
     * Methode zum Repaint aller existierender  Kanten
     */
    private void repaintKanten() {
        logic.getAlleKantenKomponente().refresh();
    }
    /**
     * Hilfsmethode zum Erstellen der Kante
     * @param kantenArt Kantenart
     */
    public void newKante(KantenArt kantenArt){
        setNewKantenArt(kantenArt);
        enableKantenZeichnung();
    }
    /**
     * Hilfsmethode zum Anlegen der Kante
     * @param kantenArt KantenArt
     */
    private void setNewKantenArt(KantenArt kantenArt){
        logic.setCurrentKantenArt(kantenArt);
    }
    /**
     * Hilfsmethode, die erlaubt, eine Kante zwischen zwei Knoten zu zeichnen
     */
    private void enableKantenZeichnung(){
        logic.setKantenZeichnung(true);
    }
    /**
     * Hilfsmethode gibt den Status der Kantenzeichnung zurück
     * @return boolean
     */
    public boolean getKantenZeichnung() {
        return logic.getKantenZeichnung();
    }
    /**
     * Methode zum Erstellen der Kante anhand der zwei ausgewählten bzw. angeclickten UI-Knotenkomponente
     * @param knoten Knoten
     */
    public void setKantenKnoten(Knoten knoten) {
        try {
            boolean kanteReadyToBeDrawn = logic.setKantenKnoten(knoten);
            if(kanteReadyToBeDrawn && !logic.getCurrentKantenArt().equals(KantenArt.UNBEKANNT)){
                Color farbe = mainWindow.displaySetColorKanteDialog();
                Kante newKante = logic.createKante(farbe);
                logic.addKanteToSelectedKnoten(newKante);
                logic.createKantenKomponent(newKante);
            }else if(kanteReadyToBeDrawn && logic.getCurrentKantenArt().equals(KantenArt.UNBEKANNT)){
                logic.deleteKante();
                loadAllKantenKomponente();
            }
            logic.getAlleKantenKomponente().refresh();
            updateGlassPlane();
        } catch (KanteNichtGefunden | GleicheKnoten e){
            showMessage(e.getMessage(),"Aktion fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Methode zur Neuzeichnung der Kanten
     */
    private void updateGlassPlane() {
        if(!logic.getAlleKantenKomponente().empty()){
            loadAllKantenKomponente();
            if(!mainWindow.getGlassPane().isVisible()){
                mainWindow.getGlassPane().setVisible(true);
            }
        }
    }

    private void loadAllKantenKomponente() {
        mainWindow.setGlassPane(logic.getAlleKantenKomponente());
    }

    /**
     * Hilfsliste zur Elimination von gleichen Kanten
     */
    private java.util.List<Kante> allKanten = new LinkedList<>();
    /**
     * Methode zum Laden des Graphen aus einer Datei
     * @param filePath String
     */
    public void loadGraph(String filePath){

        createNewGraph();
        
        try {
            logic.loadGraph(filePath);

            Graph g = logic.getCurrentGraph();

            for(Sphaere s : g.getSphaerenList()){
                loadSphaere(s);
            }

            loadAllKanten();
            refreshGraph();
            updateGlassPlane();
            logic.deleteRelationLink();

        } catch (IOException e) {
            showMessage(e.getMessage(),"Graphladen fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Hilfsmethode zum Laden der Sphären des zuladenden Graphes
     * @param sphaere Sphaere
     */
    private void loadSphaere(Sphaere sphaere) {
        JPanel sphaereKomponent = createSphereKomponent(sphaere);
        displaySphereKomponent(sphaereKomponent);

        for(Knoten k : sphaere.getKnotenList()){
            loadKnoten(k, sphaereKomponent);
        }

    }
    /**
     * Hilfsmethode zum Laden der Knoten des zuladenden Graphes
     * @param knoten Knoten
     * @param sphaereKomponent JPanel
     */
    private void loadKnoten(Knoten knoten, JPanel sphaereKomponent) {
        JPanel knotenKomponent = createKnotenKomponent(knoten);
        sphaereKomponent.add(knotenKomponent);
        knotenKomponent.setLocation(knoten.getInternalPosition());
        knotenKomponent.setSize(knoten.getGroesse());
        knotenKomponent.setVisible(true);

        knotenKomponent.validate();
        knotenKomponent.repaint();

        knoten.setKnotenKomponent(knotenKomponent);

        for(Kante kante : knoten.getKantenList()){
            allKanten.add(kante);
        }
    }
    /**
     * Hilfsmethode zum Laden aller Kanten
     */
    private void loadAllKanten() {
        java.util.List<Kante> kantenToLoad = allKanten.stream().distinct().collect(Collectors.toList());

        for(Kante kante : kantenToLoad){
            loadKante(kante);
        }

    }
    /**
     * Hilfsmethode zum Laden einzelner Kante
     * @param kante
     */
    private void loadKante(Kante kante) {
        logic.createKantenKomponent(kante);
    }

    // Für Tests
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
}
