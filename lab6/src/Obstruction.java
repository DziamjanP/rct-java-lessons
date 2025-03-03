package src;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

public class Obstruction implements Runnable {
    private double x;
    private double y;

    private double sizeX = 160;
    private double sizeY = 80;

    private Color color;

    private boolean enabled = false;

    public Obstruction() {
        color = new Color((float) Math.random(), (float) Math.random(),
                (float) Math.random());
        
            }
            
    public void enable() {
        if (!enabled){
            // Запускаем поток
            Thread thisThread = new Thread(this);
            thisThread.start();
            enabled = true;
        }
    }
    
    public void disable() {
        if (enabled) {
            enabled = false;
        }
    }

    public class MouseMotionHandler implements MouseMotionListener {
        @Override
        public void mouseDragged(java.awt.event.MouseEvent e) {
            //Do nothing
        }
        
        @Override
        public void mouseMoved(java.awt.event.MouseEvent e) {
            x = e.getX();
            y = e.getY();
        }
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getSizeX(){
        return sizeX;
    }

    public double getSizeY(){
        return sizeY;
    }

    @Override
    public void run() {
        while(enabled) {
            
        }
    }

    public void paint(Graphics2D canvas) {
        if (!enabled) return;
        canvas.setColor(color);
        canvas.setPaint(color);
        Rectangle2D.Double rect = new Rectangle2D.Double(x - sizeX / 2, y - sizeY / 2, sizeX, sizeY);
        canvas.draw(rect);
        canvas.fill(rect);
    }
    
}
