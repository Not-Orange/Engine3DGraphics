package geometry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Mesh {

    public ArrayList<Triangle> tris = new ArrayList<>();
    public ArrayList<Triangle> trisRendered = new ArrayList<>();
    public ArrayList<Triangle> temporaryTrisRendered = new ArrayList<>();

    public Mesh() {}
    public Mesh(Mesh mesh) {
        for (Triangle triangle : mesh.tris) {
            this.tris.add(new Triangle(triangle));
        }
        for (Triangle triangle : mesh.trisRendered) {
            this.trisRendered.add(new Triangle(triangle));
        }
    }
    
    public void createTemporaryTrisRendered() {
        temporaryTrisRendered.clear();
        for(Triangle triangle : trisRendered) {

            temporaryTrisRendered.add(new Triangle(triangle));
        }
    }

    // Prints all of the vertices of all of the triangles of this mesh
    public void print() {
        for (Triangle triangle : tris) {
            triangle.print();
            System.out.println();
        }
    }


    public boolean readFormFile(String filePath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            System.out.println(reader.readLine());
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
