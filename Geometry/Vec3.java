package Geometry;

public class Vec3 {
    public float x, y, z, w;

    // Constructors
    public Vec3() {
        this.x = 0; this.y = 0; this.z = 0; this.w = 1;
    }
    public Vec3(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z; this.w = 1; 
    }
    public Vec3(Vec3 vec3) {
        this.x = vec3.x; this.y = vec3.y; this.z = vec3.z; this.w = vec3.w;
    }



    // Adds offset to a point in space
    public void offSet(float offSetX, float offSetY, float offSetZ) {
        this.x += offSetX;
        this.y += offSetY;
        this.z += offSetZ;
    }

    // Prints coords of the Vector
    public void print() {
        System.out.print(x + ";" + y + ";" + z);
    }


    // Normalizes the vector
    public Vec3 normalize(Vec3 v) {
        float length = (float)Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
        v.x /= length;
        v.y /= length;
        v.z /= length;

        return v;
    }

    // Calculates cross product between to vectors
    public Vec3 getCrossProduct(Vec3 v1, Vec3 v2) {
        Vec3 normal = new Vec3();

        normal.x = v1.y * v2.z - v1.z * v2.y;
        normal.y = v1.z * v2.x - v1.x * v2.z;
        normal.z = v1.x * v2.y - v1.y * v2.x;

        normal = normalize(normal);

        return normal;
    }

    // Calculates dot product between two vectors
    public float getDotProduct(Vec3 v1, Vec3 v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public Vec3 subtract(Vec3 v1, Vec3 v2) {
        return new Vec3(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }
    public Vec3 add(Vec3 v1, Vec3 v2) {
        return new Vec3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }
}
