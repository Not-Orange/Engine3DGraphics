package geometry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Mesh {

    public ArrayList<Triangle> tris = new ArrayList<>();
    public ArrayList<Triangle> trisRendered = new ArrayList<>();

    public Vec3 origin = new Vec3(0.0f, 0.0f, 0.0f);


    public Mesh() {
    }
    public Mesh(Mesh mesh) {
        this.origin = mesh.origin;
        for (Triangle triangle : mesh.tris) {
            this.tris.add(new Triangle(triangle));
        }
        for (Triangle triangle : mesh.trisRendered) {
            this.trisRendered.add(new Triangle(triangle));
        }
    }



    // Makes a copy of a trisRendered
    @SuppressWarnings("unchecked")
    public ArrayList<Triangle> copyTriangles(ArrayList<Triangle> original) {

        return (ArrayList<Triangle>)original.clone();
    }

    // Prints all of the vertices of all of the triangles of this mesh
    public void print() {
        for (Triangle triangle : tris) {
            triangle.print();
            System.out.println();
        }
    }



    // Used for reading file and producing a new mesh.
    // Returns true if opened file correctly, return false otherwise
    public boolean readFormFile(String filePath) {

        // Vertices stored in a file
        ArrayList<Vec3> vertices = new ArrayList<>();
        // Triangles stored in a file
        ArrayList<Triangle> triangles = new ArrayList<>();

        //Read a file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = reader.readLine()) != null) {
                
                // Reads all possible points for this file
                if(line.charAt(0) == 'v') {
                    line = line + " ";
                    int index = 1;
                    int newIndex = 1;
                    int counter = 0;
                    float points[] = new float[3];

                    // Iterate through all of the values stored in one line
                    while (counter < 3 && line.indexOf(' ', index) != -1) {

                        newIndex = line.indexOf(' ', index + 1);
                        String s = line.substring(index + 1, newIndex);
                        points[counter] = Float.parseFloat(s);
                        index = newIndex;
                        counter++;
                    }
                    // Construct a Vec3 out of these points and add them to vertices Arraylist
                    vertices.add(new Vec3(points[0], points[1], points[2]));
                
                // Reads all triangles for this file
                } else if(line.charAt(0) == 'f') {
                    line = line + " .";
                    int index = 1;
                    int newIndex = 1;
                    int counter = 0;
                    int points[] = new int[3];

                    // Iterate through all of the values stored in one line
                    // Each value is a index of a point stored in this file
                    while (counter < 3 && line.indexOf(' ', index) != -1) {

                        newIndex = line.indexOf(' ', index + 1);
                        String s = line.substring(index + 1, newIndex);
                        points[counter] = Integer.parseInt(s) - 1;
                        index = newIndex;
                        counter++;
                    }
                    // Based on these values and the points that they reference construct a triangle
                    // and add it to the triangles ArrayList
                    triangles.add(new Triangle(vertices.get(points[0]), vertices.get(points[1]), vertices.get(points[2])));
                }
            }
        } catch (IOException e) {
            // If could not open return false;
            return false;
        }

        // Clear anything that could already be stored on this mesh
        tris.clear();
        // Copy read information to the tris ArrayList
        for(Triangle t : triangles) {
            tris.add(new Triangle(t));
        }
        // Clear triangles - it is no longer needed as this file will be read just once
        triangles.clear();

        return true;
    }
}
