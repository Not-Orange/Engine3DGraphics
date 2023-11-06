package Geometry;

import java.util.ArrayList;

public class Mesh {
    public ArrayList<Triangle> tris = new ArrayList<>();

    public Mesh() {}
    public Mesh(Mesh mesh) {
        for (Triangle triangle : mesh.tris) {
            this.tris.add(new Triangle(triangle));
        }
    }

    public void printMesh() {
        for (Triangle triangle : tris) {
            triangle.printTriangle();
            System.out.println();
        }
    }
}
