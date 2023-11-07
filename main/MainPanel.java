package main;

import javax.swing.JPanel;

import Geometry.CustomMatrix;
import Geometry.Mesh;
import Geometry.Triangle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;
import static java.util.Map.entry;    

public class MainPanel extends JPanel implements Runnable {

    public final static int FPS = 60;
    public final static int SCREEN_WIDTH = 960;
    public final static int SCREEN_HEIGHT = 540;
    public final static int DRAW_SIZE = 5;


    CustomMatrix mat4x4 = new CustomMatrix(this);
    Renderer renderer = new Renderer(this);
    
    Mesh meshCube = new Mesh();
    
    Thread thread;


    Map<Integer, Color> colors = Map.ofEntries( entry(0, Color.BLUE), entry(1, Color.CYAN), entry(2, Color.DARK_GRAY), 
        entry(3, Color.GRAY), entry(4, Color.GREEN), entry(5, Color.LIGHT_GRAY), entry(6, Color.MAGENTA), 
        entry(7, Color.ORANGE), entry(8, Color.PINK), entry(9, Color.RED), entry(10, Color.WHITE), entry(11, Color.YELLOW)
    );

    public MainPanel() {
        
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        onCreate();
        startThread();
    }
    
    public void startThread() {

        thread = new Thread(this);
        thread.start();
    }

    private void drawTriangle(Graphics2D g2, Triangle triangle) {
        
        g2.drawLine((int)triangle.points[0].x, (int)triangle.points[0].y, (int)triangle.points[1].x, (int)triangle.points[1].y);
        g2.drawLine((int)triangle.points[1].x, (int)triangle.points[1].y, (int)triangle.points[2].x, (int)triangle.points[2].y);
        g2.drawLine((int)triangle.points[0].x, (int)triangle.points[0].y, (int)triangle.points[2].x, (int)triangle.points[2].y);
    }



    @Override
    public void run() {
        double drawingInterval = 1_000_000_000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currTime;
    
        while (thread != null) {

            currTime = System.nanoTime();
            delta += (currTime - lastTime) / drawingInterval;
            lastTime = currTime;

            
            if (delta >= 1) {
                
                onUpdate();

                repaint();
                
                delta--;
            }
        }
    }



    private void onCreate() {

        // Initialize starting cube 

        // South
        meshCube.tris.add(new Triangle(0.0f, 0.0f, 0.0f,    0.0f, 1.0f, 0.0f,    1.0f, 1.0f, 0.0f));
        meshCube.tris.add(new Triangle(0.0f, 0.0f, 0.0f,    1.0f, 1.0f, 0.0f,    1.0f, 0.0f, 0.0f));

        // East
        meshCube.tris.add(new Triangle(1.0f, 0.0f, 0.0f,    1.0f, 1.0f, 0.0f,    1.0f, 1.0f, 1.0f));
        meshCube.tris.add(new Triangle(1.0f, 0.0f, 0.0f,    1.0f, 1.0f, 1.0f,    1.0f, 0.0f, 1.0f));

        // North
        meshCube.tris.add(new Triangle(1.0f, 0.0f, 1.0f,    1.0f, 1.0f, 1.0f,    0.0f, 1.0f, 1.0f));
        meshCube.tris.add(new Triangle(1.0f, 0.0f, 1.0f,    0.0f, 1.0f, 1.0f,    0.0f, 0.0f, 1.0f));

        // West
        meshCube.tris.add(new Triangle(0.0f, 0.0f, 1.0f,    0.0f, 1.0f, 1.0f,    0.0f, 1.0f, 0.0f));
        meshCube.tris.add(new Triangle(0.0f, 0.0f, 1.0f,    0.0f, 1.0f, 0.0f,    0.0f, 0.0f, 0.0f));

        // Top
        meshCube.tris.add(new Triangle(0.0f, 1.0f, 0.0f,    0.0f, 1.0f, 1.0f,    1.0f, 1.0f, 1.0f));
        meshCube.tris.add(new Triangle(0.0f, 1.0f, 0.0f,    1.0f, 1.0f, 1.0f,    1.0f, 1.0f, 0.0f));

        // Bottom
        meshCube.tris.add(new Triangle(1.0f, 0.0f, 1.0f,    0.0f, 0.0f, 1.0f,    0.0f, 0.0f, 0.0f));
        meshCube.tris.add(new Triangle(1.0f, 0.0f, 1.0f,    0.0f, 0.0f, 0.0f,    1.0f, 0.0f, 0.0f));
    }

    private void onUpdate() {

        // Render meshCube
        meshCube = renderer.renderMesh(meshCube);
    }


    public void paintComponent(Graphics g) {

        // Get 2D brush
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        // Draw meshCube
        for(int i = 0; i < meshCube.trisRendered.size(); i++) {

            // Set next color
            g2.setColor(colors.get(i));

            // Draw triangles
            drawTriangle(g2, meshCube.trisRendered.get(i));
        }
    }
}
