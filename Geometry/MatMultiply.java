package Geometry;

public class MatMultiply {
    

    public Vec3 MultiplyMatrixVector(Vec3 input, float[][] mat) {
        Vec3 output = new Vec3();

        output.x = input.x*mat[0][0] + input.y*mat[1][0] + input.z*mat[2][0] + mat[3][0];
        output.y = input.x*mat[0][1] + input.y*mat[1][1] + input.z*mat[2][1] + mat[3][1];
        output.z = input.x*mat[0][2] + input.y*mat[1][2] + input.z*mat[2][2] + mat[3][2];
        float w  = input.x*mat[0][3] + input.y*mat[1][3] + input.z*mat[2][3] + mat[3][3];

        if(w != 0.0f) {
            output.x /= w;
            output.y /= w;
            output.z /= w;
        }

        return output;
    }
}
