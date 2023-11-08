package main;

import javax.swing.JPanel;

import geometry.CustomMatrix;
import geometry.Mesh;
import geometry.Triangle;
import geometry.Vec3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Map;
import static java.util.Map.entry;    

public class MainPanel extends JPanel implements Runnable {

    public final static int FPS = 60;
    public final static int SCREEN_WIDTH = 960;
    public final static int SCREEN_HEIGHT = 540;


    CustomMatrix cMatrix = new CustomMatrix(this);
    Renderer renderer = new Renderer(this);
    
    Mesh meshCube = new Mesh();
    
    Thread thread;

    Vec3 cameraPos = new Vec3(0.0f, 0.0f, 0.0f);
    Vec3 lightDir = new Vec3(0.0f, 0.0f, -1.0f);


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

        if(meshCube.readFormFile("res\\uni.obj")) {
            System.err.println("file read");
        } else {
            System.out.println("file not found");
        }

        renderer.rotateMeshAroundX(meshCube, 0.6f, 1.0f);
    }

    private void onUpdate() {

        // Render meshCube
        meshCube = renderer.renderMesh(meshCube);
    }



    public void paintComponent(Graphics g) {

        // Get 2D brush
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw all triangles in meshCube
        ArrayList<Triangle> temporaryTrisRendered = meshCube.copyTriangles(meshCube.trisRendered);
        for(Triangle renTriangle : temporaryTrisRendered) {
            
            // Set a color based on light intensity
            if(renTriangle.lightIntensity > 1 || renTriangle.lightIntensity < 0) {
                renTriangle.lightIntensity = 0;
            }
            Color color = new Color((int)(255 * renTriangle.lightIntensity), 0, 0);
            // Color color = Color.red;
            // Draw triangle
            g2.setColor(color);
            g2.fill(renTriangle.constructPolygon(renTriangle));

            

            // Draw sides with black color
            // g2.setColor(Color.black);
            // g2.draw(renTriangle.constructPolygon(renTriangle));
        }
    }
}
