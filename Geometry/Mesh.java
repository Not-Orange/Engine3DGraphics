package Geometry;

import java.util.ArrayList;

public class Mesh {
    public ArrayList<Triangle> tris = new ArrayList<>();

    public void printMesh() {
        for (Triangle triangle : tris) {
            triangle.printTriangle();
            System.out.println();
        }
    }
}
