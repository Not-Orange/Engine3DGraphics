package Geometry;

public class Vec3 {
    public float x, y, z;

    public Vec3() {
        this.x = 0; this.y = 0; this.z = 0;
    }
    public Vec3(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
    }
    public Vec3(Vec3 vec3) {
        this.x = vec3.x; this.y = vec3.y; this.z = vec3.z;
    }

    // Adds offset
    public void offSet(float offSetX, float offSetY, float offSetZ) {
        this.x += offSetX;
        this.y += offSetY;
        this.z += offSetZ;
    }

    // Prints coords of the Vector
    public void printV3() {
        System.out.print(x + ";" + y + ";" + z);
    }
}
