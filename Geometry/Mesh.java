package Geometry;

import java.util.ArrayList;

public class Mesh {

    public ArrayList<Triangle> tris = new ArrayList<>();
    public ArrayList<Triangle> trisRendered = new ArrayList<>();

    public Mesh() {}
    public Mesh(Mesh mesh) {
        for (Triangle triangle : mesh.tris) {
            this.tris.add(new Triangle(triangle));
        }
        for (Triangle triangle : mesh.trisRendered) {
            this.trisRendered.add(new Triangle(triangle));
        }
    }

    public void cloneOriginalOntoRendered() {
        trisRendered.clear();
        for (Triangle triangle : tris) {
            trisRendered.add(triangle);
        }
    }

    public void printMesh() {
        for (Triangle triangle : tris) {
            triangle.printTriangle();
            System.out.println();
        }
    }
}
