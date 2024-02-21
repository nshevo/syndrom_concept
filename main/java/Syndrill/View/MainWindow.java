package Syndrill.View;

import Syndrill.Controller.Controller;
import Syndrill.Model.KantenArt;
import Syndrill.Model.Knoten;
import Syndrill.Model.Sphaere;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Dimension2D;
import java.io.IOException;

/**
 * Klasse repräsentiert UI der Application
 */
public class MainWindow extends JFrame {

    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;
    private static final String version = "1.0.0";
    private static final String fullTitle = "Syndrill V" + version;

    private JPanel spherePanel;
    private Controller controller;
    private Point offsetKnotenDrag;

    /**
     * Konstruktor
     * @param controller Controller der Application
     * @throws IOException
     */
    public MainWindow(Controller controller) throws IOException {
        super(fullTitle);

        Image image = ImageIO.read(getClass().getResource("/icon.png"));
        super.setIconImage(new ImageIcon(image).getImage());

        this.controller = controller;

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getBounds();
        setSize(bounds.getSize());
        setPreferredSize(bounds.getSize());
        setExtendedState(MAXIMIZED_BOTH);
        setResizable(false);
        setUndecorated(true);

        createMenu();
        createToolBar();
        createSpherePanel();

        add(spherePanel);
    }

    /**
     * Methode erzeugt einen Toolbar
     */
    private void createToolBar() {
        JToolBar toolBar = new JToolBar("Toolbar");
        toolBar.setFloatable(false);
        JButton createVerstaerkendKante = new JButton("Verstärkend");
        JButton createAbschwaechendKante = new JButton("Abschwächend");
        JButton createUnbekannteKante = new JButton("Unbekannt");

        createVerstaerkendKante.addActionListener(e -> controller.newKante(KantenArt.VERSTAERKEND));
        createAbschwaechendKante.addActionListener(e -> controller.newKante(KantenArt.ABSCHWAECHEND));
        createUnbekannteKante.addActionListener(e -> controller.newKante(KantenArt.UNBEKANNT));

        toolBar.add(createVerstaerkendKante);
        toolBar.add(createAbschwaechendKante);
        toolBar.add(createUnbekannteKante);

        add(toolBar, BorderLayout.NORTH);
    }

    /**
     * Methode erzeugt ein Menü
     */
    private void createMenu(){

        final JMenuBar menuBar = new JMenuBar();

        //Graph Menu
        final JMenu graphMenu = new JMenu("Graph");

        final JMenuItem create = new JMenuItem("Erstellen");
        final JMenuItem save = new JMenuItem("Speichern");
        final JMenuItem load = new JMenuItem("Laden");

        final JMenuItem saveToPDF = new JMenuItem("Graph in PDF speichern");
        final JMenuItem exit = new JMenuItem("Beenden");


        exit.addActionListener(e -> controller.applicationExit());

        create.addActionListener(e -> {
                                setGlassPane(new JPanel(null));
                                getGlassPane().setVisible(false);
                                controller.createNewGraph();
                                });

        save.addActionListener(e -> {
                                String filePath = controller.chooseDirectory("gxl");
                                controller.saveGraph(filePath);
        });

        load.addActionListener(e -> {
                                    String filePath = controller.chooseFile();
                                    controller.loadGraph(filePath);

        } );

        saveToPDF.addActionListener(e -> {
            String filePath = controller.chooseDirectory("pdf");
            controller.saveToPDF(spherePanel, getGlassPane(), filePath);
        });

        graphMenu.add(create);
        graphMenu.add(save);
        graphMenu.add(load);
        graphMenu.add(saveToPDF);
        graphMenu.add(exit);
        menuBar.add(graphMenu);

        // Sphäre Menu
        final JMenu sphereMenu = new JMenu("Sphäre");
        final JMenuItem createSphere = new JMenuItem("Sphäre erstellen");
        createSphere.addActionListener(e -> controller.createSphere());

        sphereMenu.add(createSphere);
        sphereMenu.addSeparator();

        menuBar.add(sphereMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Methode zum Relayout der UI-Komponente
     */
    public void refreshUI(){
        getGlassPane().revalidate();
        getGlassPane().repaint();
        revalidate();
        repaint();
    }

    /**
     * Methode zum Erstellen der Panel für Sphären
     */
    private void createSpherePanel(){
        spherePanel = new JPanel(new GridLayout(3,3, 1, 1));
        spherePanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }

    /**
     * Methode gibt einen Panel mit Sphären zurück
     * @return JPanel
     */
    public JPanel getSpherePanel(){
        return  spherePanel;
    }

    /**
     * Methode erzeugt ein PopupMenü für Sphären und gibt es zurück
     * @return JPopupMenu
     */
    private JPopupMenu createSpherePopUpMenu(){
        final JPopupMenu popupmenu = new JPopupMenu("Knoten");

        JMenuItem addKnoten = new JMenuItem("Knoten erstellen");

        JMenuItem deleteSphere = new JMenuItem("Sphäre löschen");


        deleteSphere.addActionListener(e->controller.deleteCurrentSphere());
        addKnoten.addActionListener(e-> controller.createKnoten());

        popupmenu.add(addKnoten);
        popupmenu.addSeparator();
        popupmenu.add(deleteSphere);

        return popupmenu;
    }

    /**
     * Methode zeigt ein Dialog zur Erstellung der Sphäre an
     */
    public void displayCreateSphereDialog() {
        JTextField sphereName = new JTextField();
        JColorChooser colorChooser = new JColorChooser(Color.white);
        colorChooser.setPreviewPanel(new JPanel());
        final JComponent[] inputs = new JComponent[]{
                new JLabel("Sphärenname"),
                sphereName,
                new JLabel("Farbe"),
                colorChooser
        };
        int result = JOptionPane.showConfirmDialog(this, inputs,"Sphäre erstellen", JOptionPane.PLAIN_MESSAGE);
        if(result == JOptionPane.OK_OPTION){
            Color farbe = colorChooser.getColor();
            String spherenName = sphereName.getText();
            controller.createSphere(spherenName, farbe);
        }

    }

    /**
     * Methode erzeugt ein Sphärenkomponent zur Sphäre und gibt es zurück
     * @param sphaere
     * @return
     */
    public JPanel createSphereKomponent(Sphaere sphaere){

        JPanel sphereContainer = new JPanel();
        sphereContainer.setLayout(null);
        sphereContainer.setBackground(sphaere.getFarbe());
        // Formating JTextArea as JLabel for too long bezeichnung
        JTextArea bezeichnung = new JTextArea(sphaere.getBezeichnung());
        bezeichnung.setEditable(false);
        bezeichnung.setCursor(null);
        bezeichnung.setOpaque(false);
        bezeichnung.setFocusable(false);
        bezeichnung.setLineWrap(false);

        Insets insets = sphereContainer.getInsets();
        Dimension size = bezeichnung.getPreferredSize();
        bezeichnung.setBounds(25 + insets.left, 5 + insets.top,
                size.width, size.height);

        bezeichnung.setForeground(Color.white);
        sphereContainer.add(bezeichnung);
        JPopupMenu knotenMenue = createSpherePopUpMenu();
        sphereContainer.add(knotenMenue);

        sphereContainer.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                controller.setCurrentSphereKomponent(sphereContainer);
                controller.setCurrentSphere(sphaere);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // 2x Linke Maustaste für Farben bearbeitung
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2){
                    Color farbeNeu = displayEditSphereDialog(sphaere.getFarbe());
                    if(farbeNeu != null){
                        controller.editSphere(sphaere,farbeNeu);
                    }
                }
                //1x Rechte Maustaste für PopUp Menu hinzufügen
                if(SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1){
                    knotenMenue.show(sphereContainer, e.getX(),e.getY());
                }
            }
        });

        return sphereContainer;
    }

    /**
     * Methode zeigt den Sphärenkomponent an
     * @param sphereKomponent
     */
        public void displaySphereKomponent(JPanel sphereKomponent){
        spherePanel.add(sphereKomponent);
        spherePanel.revalidate();
        spherePanel.repaint();
        refreshUI();
    }

    /**
     * Methode erzeugt ein Dialog mit JColorChooser und gibt die ausgewählte Farbe zurück
     * @param farbe Color
     * @return Color
     */
    private Color displayEditSphereDialog(Color farbe){
        return displaySetColorChooser(farbe,"Sphäre bearbeiten");
    }

    /**
     * Methode zeigt den Dialog zur Erstellung von Knoten an
     */
    public void displayCreateKnotenDialog(){
        JTextField bezeichnung = new JTextField();
        JColorChooser colorChooser = new JColorChooser(Color.white);
        colorChooser.setPreviewPanel(new JPanel());
        final JComponent[] inputs = new JComponent[]{
                new JLabel("Bezeichnung"),
                bezeichnung,
                new JLabel("Farbe"),
                colorChooser
        };
        int result = JOptionPane.showConfirmDialog(null, inputs,"Knoten erstellen", JOptionPane.PLAIN_MESSAGE);
        if(result == JOptionPane.OK_OPTION){
            Color farbe = colorChooser.getColor();
            String knotenBezeichnung = bezeichnung.getText();
            controller.createKnoten(knotenBezeichnung, farbe);
        }
    }

    /**
     * Methode erzeugt ein Dialog mit JColorChooser und gibt die ausgewählte Farbe zurück
     * @param farbeNeu Color
     * @return Color
     */
    private Color displayEditKnotenDialog(Color farbeNeu){
        return displaySetColorChooser(farbeNeu, "Knoten bearbeiten");
    }

    /**
     * Methode erzeugt ein PopupMenu für Knoten und gibt es zurück
     * @param knoten
     * @return JPopupMenu
     */
    private JPopupMenu createKnotenPopUpMenu(Knoten knoten){
        final JPopupMenu popupmenu = new JPopupMenu("Knoten- und Kantenmenü");

        JMenuItem deleteKnoten = new JMenuItem("Knoten löschen");

        deleteKnoten.addActionListener(e-> controller.deleteKnoten(knoten));

        popupmenu.add(deleteKnoten);
        popupmenu.addSeparator();

        return popupmenu;
    }

    /**
     * Methode erzeugt ein Knotenkomponent und gibt es zurück
     * @param knoten
     * @return
     */
    public JPanel createKnotenKomponent(Knoten knoten){
        // display sphere
        JPanel knotenContainer = new JPanel();
        knotenContainer.setBorder(BorderFactory.createLineBorder(Color.black));
        knotenContainer.setName("knoten");
        knotenContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        knotenContainer.setBackground(knoten.getFarbe());
        JLabel bezeichnung = new JLabel(knoten.getBezeichnung());
        bezeichnung.setForeground(Color.white);
        knotenContainer.add(bezeichnung);

        JPopupMenu knotenMenu = createKnotenPopUpMenu(knoten);
        knotenContainer.add(knotenMenu);

        knotenContainer.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                offsetKnotenDrag = e.getPoint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                // 2x Linke Maustaste für Farben bearbeitung
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2){
                    Color farbeNeu = displayEditKnotenDialog(knoten.getFarbe());
                    if(farbeNeu != null){
                        controller.editKnoten(knoten,farbeNeu);
                    }
                }
                // 1x Rechte Maustaste öffnet PopUpMenu
                else if(SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1){
                    knotenMenu.show(knotenContainer, e.getX(),e.getY());
                }
                //1x Linke Maustaste für die Pfeilzeichnung
                else if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1){
                    if(controller.getKantenZeichnung()) controller.setKantenKnoten(knoten);
                }
            }
        });

        knotenContainer.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                int x = e.getPoint().x - offsetKnotenDrag.x;
                int y = e.getPoint().y - offsetKnotenDrag.y;

                Component component = e.getComponent();
                Point location = component.getLocation();
                int newLocationX = location.x += x;
                int newLocationY = location.y += y;

                int sphereMaxX = controller.getCurrentSphereKomponent().getWidth();
                int sphereMaxY = controller.getCurrentSphereKomponent().getHeight();

                sphereMaxX -= component.getWidth();
                sphereMaxY -= component.getHeight();

                if(newLocationX > 0 && newLocationY > 0 && newLocationX < sphereMaxX && newLocationY < sphereMaxY)
                {
                    component.setLocation(location);
                }
            }
        });

        return knotenContainer;
    }

    /**
     * Methode zeigt den Knotenkomponent an
     * @param knotenKomponent JPanel
     */
    public void displayKnotenKomponent(JPanel knotenKomponent){
        JPanel currentSphereKomponent = controller.getCurrentSphereKomponent();
        for(Component c : spherePanel.getComponents()){
            if(c.equals(currentSphereKomponent)){

                Insets insets = currentSphereKomponent.getInsets();
                Dimension size = knotenKomponent.getPreferredSize();
                knotenKomponent.setBounds(25 + insets.left, 5 + insets.top,
                        size.width, size.height);

                controller.getCurrentSphereKomponent().add(knotenKomponent);
                refreshUI();
                break;
            }
        }


    }

    /**
     * Methode erzeugt ein Dialog mit JColorChooser und gibt die ausgewählte Farbe zurück
     * @return
     */
    public Color displaySetColorKanteDialog(){
       return displaySetColorChooser(Color.white,"Kantenfarbe festlegen");
    }

    /**
     * Methode erzeugt ein Dialog mit JColorChooser und gibt die ausgewählte Farbe zurück
     * @return
     */
    private Color displaySetColorChooser(Color color, String title){
        JColorChooser colorChooser = new JColorChooser();
        colorChooser.setColor(color);
        colorChooser.setPreviewPanel(new JPanel());
        final JComponent[] inputs = new JComponent[]{
                new JLabel("Farbe festlegen"),
                colorChooser
        };
        int result = JOptionPane.showConfirmDialog(this, inputs,title, JOptionPane.PLAIN_MESSAGE);
        if(result == JOptionPane.OK_OPTION){
            Color farbe = colorChooser.getColor();
            return farbe;
        }
        return color;
    }

}
