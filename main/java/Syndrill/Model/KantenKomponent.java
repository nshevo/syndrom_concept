package Syndrill.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.LinkedList;

/**
 * Klasse bzw. UI-Komponent zur Darstellung der Relationen (UI-Komponente der Kanten)
 */
public class KantenKomponent extends JComponent {

    /**
     * Liste aller Relationen
     */
    private final LinkedList<Relation> relations = new LinkedList<>();

    /**
     * Default-Konstruktor, Durchsichtiges UI-Komponent
     */
    public KantenKomponent(){
        setOpaque(false);
    }

    /**
     * Methode gibt an, ob Relationen vorhanden sind
     * @return boolean true -> keine Kanten | false -> Kanten vorhanden
     */
    public boolean empty(){
        return relations.isEmpty();
    }

    /**
     * Methode gibt eine Relation anhand der Parameter zurück
     * @param startPoint Point2D.Double
     * @param endPoint Point2D.Double
     * @param color Color
     * @param kantenArt KantenArt
     * @return Relation
     */
    public Relation addLine(Point2D.Double startPoint, Point2D.Double endPoint, Color color, KantenArt kantenArt) {
        Relation currentRelation = null;

        if(kantenArt.equals(KantenArt.VERSTAERKEND)){
            currentRelation = new Relation(startPoint, endPoint, color,true);
            relations.add(currentRelation);
        } else if(kantenArt.equals(KantenArt.ABSCHWAECHEND)){
            currentRelation = new Relation(startPoint, endPoint, color,false);
            relations.add(currentRelation);
        }

        repaint();

        return currentRelation;
    }

    /**
     * Methode zum Löschen der existierenden Relationen anhand der start- und endPunkte der Relation
     * @param startPunkt
     * @param endPunkt
     */
    public void deleteLines(Point2D.Double startPunkt, Point2D.Double endPunkt){
        LinkedList<Relation> allRelations = new LinkedList<>();

        for(Relation r : relations){
            if(r.startPoint.equals(startPunkt) && r.endPoint.equals(endPunkt)){
                allRelations.add(r);
            }
        }
        for (Relation removeRelation : allRelations){
            relations.remove(removeRelation);
        }

        refresh();
    }

    /**
     * Führt ein Repaint der Komponente durch
     */
    public void refresh(){
        if(relations.size() > 0 ) repaint();
    }

    /**
     * Methode zur visuellen Darstellung der Relationen
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        for (Relation relation : relations) {
            g2.setColor(relation.color);
            g2.setStroke(new BasicStroke(1));

            double ctrlx1 = 0;
            double ctrly1 = 0;
            double ctrlx2 = 0;
            double ctrly2 = 0;

            double offsetX = 300.0;
            double offsetY = 150.0;

            if(relation.startPoint.getY() > relation.endPoint.getY()){
                if(relation.startPoint.getX() > relation.endPoint.getX()){
                    ctrlx1 = relation.endPoint.getX() - offsetX;
                    ctrly1 = relation.endPoint.getY() + offsetY;

                    ctrlx2 = relation.startPoint.getX() + offsetX;
                    ctrly2 = relation.startPoint.getY() - offsetY;
                }else{
                    ctrlx1 = relation.endPoint.getX() - offsetX;
                    ctrly1 = relation.endPoint.getY() - offsetY;

                    ctrlx2 = relation.startPoint.getX() + offsetX;
                    ctrly2 = relation.startPoint.getY() + offsetY;
                }
            }else if(relation.startPoint.getY() < relation.endPoint.getY()){
                if(relation.startPoint.getX() > relation.endPoint.getX()){
                    ctrlx1 = relation.endPoint.getX() - offsetX;
                    ctrly1 = relation.endPoint.getY() - offsetY;

                    ctrlx2 = relation.startPoint.getX() + offsetX;
                    ctrly2 = relation.startPoint.getY() + offsetY;
                }else{
                    ctrlx1 = relation.endPoint.getX() - offsetX;
                    ctrly1 = relation.endPoint.getY() + offsetY;

                    ctrlx2 = relation.startPoint.getX() + offsetX;
                    ctrly2 = relation.startPoint.getY() - offsetY;
                }
            }

            CubicCurve2D.Double curve = new CubicCurve2D.Double(relation.startPoint.getX(),relation.startPoint.getY(),
                     ctrlx2, ctrly2, ctrlx1, ctrly1, relation.endPoint.getX(), relation.endPoint.getY());

            g2.setStroke(new BasicStroke(Kante.STAERKE));
            g2.draw(curve);

            if(relation.withArrow){
                drawArrowHead(g2, relation, curve);
            }else{
                drawCircleHead(g2, relation);
            }

        }
    }

    /**
     * Methode zum Zeichnen des Kreises am Ende der Linie bzw. Relation
     * @param g2 Graphics2D
     * @param relation Relation
     */
    private void drawCircleHead(Graphics2D g2, Relation relation){
        AffineTransform oldTx = g2.getTransform();
        AffineTransform tx = g2.getTransform();
        tx.translate(relation.endPoint.getX(), relation.endPoint.getY());
        tx.rotate(Math.toDegrees(0));
        g2.setTransform(tx);

        g2.fillOval(-5 , -5,
                relation.circle.diameter, relation.circle.diameter);

        g2.setTransform(oldTx);
    }

    /**
     * Methode zum Zeichnen der Pfeilspitze am Ende der Linie bzw. Relation
     * @param g2 Graphics2D
     * @param relation Relation
     * @param curve CubicCurve2D.Double
     */
    private void drawArrowHead(Graphics2D g2, Relation relation, CubicCurve2D.Double curve) {
        double angle = Math.atan2(relation.endPoint.getY() - curve.getCtrlY2(), relation.endPoint.getX() - curve.getCtrlX2() );
        AffineTransform oldTx = g2.getTransform();
        AffineTransform tx = g2.getTransform();
        tx.translate(relation.endPoint.getX(), relation.endPoint.getY());
        tx.rotate(angle - Math.PI / 2d);
        g2.setTransform(tx);

        g2.drawLine(0,5,-5,-5);
        g2.drawLine(0,5,5,-5);

        g2.setTransform(oldTx);
    }

}
