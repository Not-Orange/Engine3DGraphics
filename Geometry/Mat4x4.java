package Geometry;

import java.lang.Math;
import main.MainPanel;

public class Mat4x4 {
    public float matProj[][];
    public float matRotZ[][];
    public float matRotX[][];

    float fNear = 0.1f;
    float fFar = 1000.0f;
    float fFov = 90.0f;
    float fAspectRatio = (float)MainPanel.SCREEN_HEIGHT / (float)MainPanel.SCREEN_WIDTH;
    double angle = fFov * 0.05 / 180.0 * Math.PI;
    float fFovRad =  1.0f / (float)(Math.tan(angle));

    public Mat4x4() {

        matProj = new float[][] {
            {fAspectRatio * fFovRad, 0,       0,                                  0   },
            {0,                      fFovRad, 0,                                  0   },
            {0,                      0,       (fFar / (fFar - fNear)),            1.0f},
            {0,                      0,       (-(fFar * fNear) / (fFar - fNear)), 0   }
        };
    }

    public Vec3 MultiplyMatrixVector(Vec3 input) {
        Vec3 output = new Vec3();

        output.x = input.x*matProj[0][0] + input.y*matProj[1][0] + input.z*matProj[2][0] + matProj[3][0];
        output.y = input.x*matProj[0][1] + input.y*matProj[1][1] + input.z*matProj[2][1] + matProj[3][1];
        output.z = input.x*matProj[0][2] + input.y*matProj[1][2] + input.z*matProj[2][2] + matProj[3][2];
        float w  = input.x*matProj[0][3] + input.y*matProj[1][3] + input.z*matProj[2][3] + matProj[3][3];

        if(w != 0.0f) {
            output.x /= w;
            output.y /= w;
            output.z /= w;
        }

        return output;
    }
}
