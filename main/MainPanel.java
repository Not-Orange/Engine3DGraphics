package main;

import javax.swing.JPanel;

import Geometry.Mat4x4;
import Geometry.Mesh;
import Geometry.Triangle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;
import static java.util.Map.entry;    

public class MainPanel extends JPanel implements Runnable {

    Thread thread;

    Mat4x4 mat4x4 = new Mat4x4();
    Mesh meshCube = new Mesh();

    float offSetX = -0.3f;
    float offSetY = -0.6f;
    float offSetZ = 15.0f;

    Map<Integer, Color> colors = Map.ofEntries(
        entry(0, Color.BLUE),
        entry(1, Color.CYAN),
        entry(2, Color.GRAY),
        entry(3, Color.BLUE),
        entry(4, Color.GREEN),
        entry(5, Color.DARK_GRAY),
        entry(6, Color.MAGENTA),
        entry(7, Color.ORANGE),
        entry(8, Color.PINK),
        entry(9, Color.YELLOW),
        entry(10, Color.WHITE),
        entry(11, Color.YELLOW)
        
    );

    public final static int FPS = 60;
    public final static int SCREEN_WIDTH = 960;
    public final static int SCREEN_HEIGHT = 540;
    public final static int DRAW_SIZE = 5;

    boolean shown = false;

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

                onUserUpdate();
                if(!shown) {

                    repaint();
                }
                shown = true;
                
            }
        }
    }

    private void onCreate() {

        //South
        meshCube.tris.add(new Triangle(0.0f, 0.0f, 0.0f,    0.0f, 1.0f, 0.0f,    1.0f, 1.0f, 0.0f));
        meshCube.tris.add(new Triangle(0.0f, 0.0f, 0.0f,    1.0f, 1.0f, 0.0f,    1.0f, 0.0f, 0.0f));

        //East
        meshCube.tris.add(new Triangle(1.0f, 0.0f, 0.0f,    1.0f, 1.0f, 0.0f,    1.0f, 1.0f, 1.0f));
        meshCube.tris.add(new Triangle(1.0f, 0.0f, 0.0f,    1.0f, 1.0f, 1.0f,    1.0f, 0.0f, 1.0f));

        //North
        meshCube.tris.add(new Triangle(1.0f, 0.0f, 1.0f,    1.0f, 1.0f, 1.0f,    0.0f, 1.0f, 1.0f));
        meshCube.tris.add(new Triangle(1.0f, 0.0f, 1.0f,    0.0f, 1.0f, 1.0f,    0.0f, 0.0f, 1.0f));

        //West
        meshCube.tris.add(new Triangle(0.0f, 0.0f, 1.0f,    0.0f, 1.0f, 1.0f,    0.0f, 1.0f, 0.0f));
        meshCube.tris.add(new Triangle(0.0f, 0.0f, 1.0f,    0.0f, 1.0f, 0.0f,    0.0f, 0.0f, 0.0f));

        //Top
        meshCube.tris.add(new Triangle(0.0f, 1.0f, 0.0f,    0.0f, 1.0f, 1.0f,    1.0f, 1.0f, 1.0f));
        meshCube.tris.add(new Triangle(0.0f, 1.0f, 0.0f,    1.0f, 1.0f, 1.0f,    1.0f, 1.0f, 0.0f));

        //Bottom
        meshCube.tris.add(new Triangle(1.0f, 0.0f, 1.0f,    0.0f, 0.0f, 1.0f,    0.0f, 0.0f, 0.0f));
        meshCube.tris.add(new Triangle(1.0f, 0.0f, 1.0f,    0.0f, 0.0f, 0.0f,    1.0f, 0.0f, 0.0f));
    }



    private void onUserUpdate() {
    }

    private void drawTriangle(Graphics2D g2, Triangle triangle) {
        
        g2.drawLine((int)triangle.points[0].x, (int)triangle.points[0].y, (int)triangle.points[1].x, (int)triangle.points[1].y);
        g2.drawLine((int)triangle.points[1].x, (int)triangle.points[1].y, (int)triangle.points[2].x, (int)triangle.points[2].y);
        g2.drawLine((int)triangle.points[0].x, (int)triangle.points[0].y, (int)triangle.points[2].x, (int)triangle.points[2].y);
    }



    public void paintComponent(Graphics g) {

        System.out.println(shown);

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        for(int i = 0; i < meshCube.tris.size(); i++) {

            Triangle triangle = new Triangle(meshCube.tris.get(i));
            Triangle triangleProjected = new Triangle();
            Triangle triangleTranslated = new Triangle(triangle);

            triangleTranslated.offSet(offSetX, offSetY, offSetZ);
    
            //Assign projected values
            triangleProjected.points[0] = mat4x4.MultiplyMatrixVector(triangleTranslated.points[0]);
            triangleProjected.points[1] = mat4x4.MultiplyMatrixVector(triangleTranslated.points[1]);
            triangleProjected.points[2] = mat4x4.MultiplyMatrixVector(triangleTranslated.points[2]);
    
            // Scale into view
            triangleProjected.points[0].x += 1.0f; triangleProjected.points[0].y += 1.0f;
            triangleProjected.points[1].x += 1.0f; triangleProjected.points[1].y += 1.0f;
            triangleProjected.points[2].x += 1.0f; triangleProjected.points[2].y += 1.0f;
            
            triangleProjected.points[0].x *= (0.5f * (float)SCREEN_WIDTH); 
            triangleProjected.points[0].y *= (0.5f * (float)SCREEN_HEIGHT);
            triangleProjected.points[1].x *= (0.5f * (float)SCREEN_WIDTH); 
            triangleProjected.points[1].y *= (0.5f * (float)SCREEN_HEIGHT);
            triangleProjected.points[2].x *= (0.5f * (float)SCREEN_WIDTH); 
            triangleProjected.points[2].y *= (0.5f * (float)SCREEN_HEIGHT);
            
            //Set next color
            g2.setColor(colors.get(i));

            //Draw triangles
            drawTriangle(g2, triangleProjected);
        }
    }
}
