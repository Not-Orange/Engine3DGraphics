package Geometry;

import java.lang.Math;
import main.MainPanel;

public class Mat4x4 {
    MainPanel panel;

    public float matProj[][];
    public float matRotZ[][];
    public float matRotX[][];

    float fNear = 0.1f;
    float fFar = 1000.0f;
    float fFov = 90.0f;
    float fAspectRatio = (float)MainPanel.SCREEN_HEIGHT / (float)MainPanel.SCREEN_WIDTH;
    double angle = fFov * 0.05 / 180.0 * Math.PI;
    float fFovRad =  1.0f / (float)(Math.tan(angle));

    public Mat4x4(MainPanel mainPanel) {
        this.panel = mainPanel;


        matProj = new float[][] {
            {fAspectRatio * fFovRad, 0,       0,                                  0   },
            {0,                      fFovRad, 0,                                  0   },
            {0,                      0,       (fFar / (fFar - fNear)),            1.0f},
            {0,                      0,       (-(fFar * fNear) / (fFar - fNear)), 0   }
        };

        updateMatRot(panel.fTheta);
    }



    
    public void updateMatRot(double fTheta) {

        matRotX = new float[][] {
            {1.0f,  0,                              0,                                 0},
            {0,     (float)Math.cos(panel.fTheta),  (float)Math.sin(panel.fTheta),     0},
            {0,     -(float)Math.sin(panel.fTheta), (float)Math.cos(panel.fTheta),     0},
            {0,     0,                              0,                              1.0f},
        };

        matRotZ = new float[][] {
            {(float)Math.cos(panel.fTheta * 0.05f),  (float)Math.sin(panel.fTheta * 0.05f),     0,        0},
            {-(float)Math.sin(panel.fTheta * 0.05f), (float)Math.cos(panel.fTheta * 0.05f),     0,        0},
            {0,                                      0,                                         1.0f,     0},
            {0,                                      0,                                         0,     1.0f}
        };
    }
}
