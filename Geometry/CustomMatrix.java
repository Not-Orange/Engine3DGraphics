package geometry;

import java.lang.Math;
import main.MainPanel;

public class CustomMatrix {
    MainPanel panel;

    public float matProj[][];

    public float matRotZ[][];
    public float matRotX[][];
    public float matRotY[][];

    float fNear = 0.1f;
    float fFar = 1000.0f;
    float fFov = 90.0f;
    float fAspectRatio = (float)MainPanel.SCREEN_HEIGHT / (float)MainPanel.SCREEN_WIDTH;
    double angle = fFov * 0.05 / 180.0 * Math.PI;
    float fFovRad =  1.0f / (float)(Math.tan(angle));

    // Constructor - generates default projection matrix
    public CustomMatrix(MainPanel mainPanel) {
        this.panel = mainPanel;


        matProj = new float[][] {
            {fAspectRatio * fFovRad, 0,       0,                                  0   },
            {0,                      fFovRad, 0,                                  0   },
            {0,                      0,       (fFar / (fFar - fNear)),            1.0f},
            {0,                      0,       (-(fFar * fNear) / (fFar - fNear)), 0   }
        };
    }



    // Updates rotation matrices with new fTheta angle
    public void updateRotMat(double fTheta, float rotMulti) {

        matRotX = new float[][] {
            {1.0f, 0, 0, 0},
            {0, (float)Math.cos(fTheta * rotMulti), (float)Math.sin(fTheta * rotMulti), 0},
            {0, -(float)Math.sin(fTheta * rotMulti), (float)Math.cos(fTheta * rotMulti), 0},
            {0, 0, 0, 1.0f},
        };

        matRotZ = new float[][] {
            {(float)Math.cos(fTheta * rotMulti),  (float)Math.sin(fTheta * rotMulti),     0,        0},
            {-(float)Math.sin(fTheta * rotMulti), (float)Math.cos(fTheta * rotMulti),     0,        0},
            {0,                                0,                                1.0f,        0},
            {0,                                0,                                   0,     1.0f}
        };

        matRotY = new float[][] {
            {(float)Math.cos(fTheta * rotMulti), 0, (float)Math.sin(fTheta * rotMulti), 0},
            {0, 1.0f, 0, 0},
            {-(float)Math.sin(fTheta * rotMulti), 0, (float)Math.cos(fTheta * rotMulti), 0},
            {0, 0, 0, 1.0f},
        };
    }


    // Handles multiplication of matrices with 3D vectors
    public Vec3 MultiplyVectorByMatrix(Vec3 input, float[][] mat) {
        Vec3 v = new Vec3();

        v.x = input.x*mat[0][0] + input.y*mat[1][0] + input.z*mat[2][0] + mat[3][0];
        v.y = input.x*mat[0][1] + input.y*mat[1][1] + input.z*mat[2][1] + mat[3][1];
        v.z = input.x*mat[0][2] + input.y*mat[1][2] + input.z*mat[2][2] + mat[3][2];
        float w  = input.x*mat[0][3] + input.y*mat[1][3] + input.z*mat[2][3] + mat[3][3];

        if(w != 0.0f) {
            v.x /= w;
            v.y /= w;
            v.z /= w;
        }

        return v;
    }


}
