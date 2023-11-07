package Geometry;

public class Vec3 {
    public float x, y, z;

    // Constructors
    public Vec3() {
        this.x = 0; this.y = 0; this.z = 0;
    }
    public Vec3(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
    }
    public Vec3(Vec3 vec3) {
        this.x = vec3.x; this.y = vec3.y; this.z = vec3.z;
    }



    // Adds offset to a point in space
    public void offSet(float offSetX, float offSetY, float offSetZ) {
        this.x += offSetX;
        this.y += offSetY;
        this.z += offSetZ;
    }

    // Prints coords of the Vector
    public void printV3() {
        System.out.print(x + ";" + y + ";" + z);
    }



    // Calculates cross product between to vectors
    public Vec3 getCrossProduct(Vec3 v1, Vec3 v2) {
        Vec3 normal = new Vec3();

        normal.x = v1.y * v2.z - v1.z * v2.y;
        normal.y = v1.z * v2.x - v1.x * v2.z;
        normal.z = v1.x * v2.y - v1.y * v2.x;

        float length = (float)Math.sqrt(normal.x * normal.x + normal.y * normal.y + normal.z * normal.z);
        normal.x /= length;
        normal.y /= length;
        normal.z /= length;

        return normal;
    }

    // Calculates dot product between two vectors
    public float getDotProduct(Vec3 v1, Vec3 v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public Vec3 subtractVecs3(Vec3 v1, Vec3 v2) {
        return new Vec3(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }
}
