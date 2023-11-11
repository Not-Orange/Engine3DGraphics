package Geometry;

import java.lang.Math;
import main.MainPanel;

public class CustomMatrix {
    MainPanel mainPanel;

    public float matProj[][];

    public float matRotZ[][];
    public float matRotX[][];
    public float matRotY[][];

    public float matScaling[][];

    float fNear = 0.1f;
    float fFar = 1000.0f;
    float fFov = 90.0f;
    float fAspectRatio = (float)MainPanel.SCREEN_HEIGHT / (float)MainPanel.SCREEN_WIDTH;
    double angle = fFov * 0.05 / 180.0 * Math.PI;
    float fFovRad =  1.0f / (float)(Math.tan(angle));

    // Constructor - generates default projection matrix
    public CustomMatrix(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        matProj = new float[][] {
            {fAspectRatio * fFovRad, 0,       0,                                  0   },
            {0,                      fFovRad, 0,                                  0   },
            {0,                      0,       (fFar / (fFar - fNear)),            1.0f},
            {0,                      0,       (-(fFar * fNear) / (fFar - fNear)), 0   }
        };
    }



    // Updates rotation matrices with a new angle
    public float[][] getRotMatX(float angSpeed) {
        angSpeed /= MainPanel.FPS;
        matRotX = new float[][] {
            {1.0f, 0, 0, 0},
            {0, (float)Math.cos(angSpeed), (float)Math.sin(angSpeed), 0},
            {0, -(float)Math.sin(angSpeed), (float)Math.cos(angSpeed), 0},
            {0, 0, 0, 1.0f},
        };
        return matRotX;
    }
    public float[][] getRotMatY(float angSpeed) {
        angSpeed /= MainPanel.FPS;
        matRotY = new float[][] {
            {(float)Math.cos(angSpeed), 0, (float)Math.sin(angSpeed), 0},
            {0, 1.0f, 0, 0},
            {-(float)Math.sin(angSpeed), 0, (float)Math.cos(angSpeed), 0},
            {0, 0, 0, 1.0f},
        };
        return matRotY;
    }
    public float[][] getRotMatZ(float angSpeed) {
        angSpeed /= MainPanel.FPS;
        matRotZ = new float[][] {
            {(float)Math.cos(angSpeed), (float)Math.sin(angSpeed), 0, 0},
            {-(float)Math.sin(angSpeed), (float)Math.cos(angSpeed), 0, 0},
            {0, 0, 1.0f, 0},
            {0, 0, 0, 1.0f}
        };
        return matRotZ;
    }

    public float[][] getScalingMatrix(float scale) {
        matScaling = new float[][] {
            {scale, 0, 0, 0},
            {0, scale, 0, 0},
            {0, 0, scale, 0},
            {0, 0, 0, 1.0f}
        };
        return matScaling;
    }


    public Triangle multiTrigByMatrix(Triangle trig, float[][] mat) {
        trig.points[0] = multiVecByMatrix(trig.points[0], mat);
        trig.points[1] = multiVecByMatrix(trig.points[1], mat);
        trig.points[2] = multiVecByMatrix(trig.points[2], mat);
        return trig;
    }

    // Handles multiplication of matrices with 3D vectors
    public Vec3 multiVecByMatrix(Vec3 vIn, float[][] mat) {
        Vec3 vOut = new Vec3();

        vOut.x = vIn.x*mat[0][0] + vIn.y*mat[1][0] + vIn.z*mat[2][0] + vIn.w*mat[3][0];
        vOut.y = vIn.x*mat[0][1] + vIn.y*mat[1][1] + vIn.z*mat[2][1] + vIn.w*mat[3][1];
        vOut.z = vIn.x*mat[0][2] + vIn.y*mat[1][2] + vIn.z*mat[2][2] + vIn.w*mat[3][2];
        vOut.w = vIn.x*mat[0][3] + vIn.y*mat[1][3] + vIn.z*mat[2][3] + vIn.w*mat[3][3];

        if(vOut.w != 0.0f) {
            vOut.x /= vOut.w;
            vOut.y /= vOut.w;
            vOut.z /= vOut.w;
        }

        return vOut;
    }


}
