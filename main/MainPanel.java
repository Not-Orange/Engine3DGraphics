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
import java.awt.Point;


public class MainPanel extends JPanel implements Runnable {

    public final static int FPS = 60;
    public final static int SCREEN_WIDTH = 960;
    public final static int SCREEN_HEIGHT = 540;


    CustomMatrix cMatrix = new CustomMatrix(this);
    Renderer renderer = new Renderer(this);
    
    Mesh mesh = new Mesh();
    
    Thread thread;

    Vec3 cameraPos = new Vec3(0.0f, 0.0f, 0.0f);
    Vec3 lightDir = new Vec3(0.0f, 0.0f, -1.0f);

    Point[][] depthBuffer;


    public MainPanel() {
        
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        onCreate();
        startThread();

        depthBuffer = createDepthBuffer();
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

        if(mesh.readFormFile("res\\VideoShip.obj")) {
            System.err.println("file read");
        } else {
            System.out.println("file not found");
        }

        mesh = renderer.offsetMesh(mesh, 0.0f, 0.0f, 10.0f);
        mesh = renderer.rotateMesh(mesh, 109999.0f, 'x');
        mesh = renderer.scaleMesh(mesh, 0.2f);
    }



    private Point[][] createDepthBuffer() {
        Point[][] depthBuffer = new Point[SCREEN_HEIGHT][SCREEN_WIDTH];

        for(int y = 0; y < SCREEN_HEIGHT; y++) {
            for(int x = 0; x < SCREEN_WIDTH; x++) {
                depthBuffer[y][x] = new Point(x, y);
            }
        }

        return depthBuffer;
    }



    private void onUpdate() {

        // Render mesh
        mesh.trisRendered = renderer.renderMesh(mesh.tris);
        mesh = renderer.rotateMesh(mesh, 2.0f, 'y');
    }



    public void paintComponent(Graphics g) {

        // Get 2D brush
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        paintMesh(g2, mesh);
    }



    // Draws the entire mesh
    private void paintMesh(Graphics2D g2, Mesh m) {

        // Draw all triangles in a mesh
        for(Triangle renTriangle : m.trisRendered) {
            
            // Catch anomalies TODO find out why those even happen
            if(renTriangle.lightIntensity > 1 || renTriangle.lightIntensity < 0) {
                renTriangle.lightIntensity = 0;
            }

            // Set a color based on light intensity
            int red = 0; int green = 180; int blue = 150;
            Color color = new Color(
                (int)(red * renTriangle.lightIntensity), 
                (int)(green * renTriangle.lightIntensity), 
                (int)(blue * renTriangle.lightIntensity));
            g2.setColor(color);
            g2.fill(renTriangle.constructPolygon(renTriangle));
        }
    }
}
