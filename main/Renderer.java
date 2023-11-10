package main;

import java.util.ArrayList;
import java.util.Comparator;
import geometry.CustomMatrix;
import geometry.Mesh;
import geometry.Triangle;
import geometry.Vec3;

public class Renderer {

    MainPanel mainPanel;

    CustomMatrix cMatrix;
    Vec3 vec3;
    Triangle trig;


    public Renderer(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        this.cMatrix = new CustomMatrix(mainPanel);
        this.vec3 = new Vec3();
        this.trig = new Triangle();
    }

    

    public ArrayList<Triangle> renderMesh(ArrayList<Triangle> tris) {

        ArrayList<Triangle> trisRendered = new ArrayList<>();

        // Illumination
        Vec3 lightDir = vec3.normalize(mainPanel.lightDir);

        // Go through all of the triangles in the mesh
        for(int i = 0; i < tris.size(); i++) {

            //Extract a triangle from mesh as a copy of the original
            Triangle triangle = new Triangle(tris.get(i));
            
            // Calculate normal vector
            Vec3 l1 = vec3.subtract(triangle.points[1], triangle.points[0]);
            Vec3 l2 = vec3.subtract(triangle.points[2], triangle.points[0]);
            Vec3 normal = vec3.getCrossProduct(l1, l2);
            
            // Proceed with projection only if:
            // normal is between being parallel and at a right angle to Vector from a camera to this triangle
            if (vec3.getDotProduct(normal, vec3.subtract(triangle.points[0], mainPanel.cameraPos)) < 0.0f) {

                // 3D projected into 2D space
                // Assign projected values to the triangle vertices
                triangle = cMatrix.multiTrigByMatrix(triangle, cMatrix.matProj);

                // Normalize and scale the triangle into view
                triangle.points[0].x += 1.0f; triangle.points[0].y += 1.0f;
                triangle.points[1].x += 1.0f; triangle.points[1].y += 1.0f;
                triangle.points[2].x += 1.0f; triangle.points[2].y += 1.0f;
                
                triangle.points[0].x *= (0.5f * (float)MainPanel.SCREEN_WIDTH); 
                triangle.points[0].y *= (0.5f * (float)MainPanel.SCREEN_HEIGHT);
                triangle.points[1].x *= (0.5f * (float)MainPanel.SCREEN_WIDTH); 
                triangle.points[1].y *= (0.5f * (float)MainPanel.SCREEN_HEIGHT);
                triangle.points[2].x *= (0.5f * (float)MainPanel.SCREEN_WIDTH); 
                triangle.points[2].y *= (0.5f * (float)MainPanel.SCREEN_HEIGHT);

                // Add illumination information
                // Compare how similar normal of a triangle is to the direction of the light
                triangle.lightIntensity = vec3.getDotProduct(lightDir, normal);  

                // Save rendered triangle on trisRendered
                trisRendered.add(triangle);
            }

            // Sort all triangles in mesh such that the ones with the 
            // largest values of Z (the furthest from observer) are first.
            // When drawing triangles that are closer than others will then be
            // drawn on top of them.
            trisRendered.sort(new trigComparator());
        }

        System.out.println("done");
        return trisRendered;
    }



    // Offsets whole mesh, saves a new origin point for that mesh
    public Mesh offsetMesh(Mesh mesh, float offSetX, float offSetY, float offSetZ) {

        mesh.origin.offSet(offSetX, offSetY, offSetZ);

        for(int i = 0; i < mesh.tris.size(); i++) {
            Triangle triangle = mesh.tris.get(i);
            triangle.offSet(offSetX, offSetY, offSetZ);
        }  

        return mesh;
    }
    // Scales entire mesh uniformly by a scale factor
    public Mesh scaleMesh(Mesh mesh, float scale) {

        float[][] scaleMat = cMatrix.getScalingMatrix(scale);

        for(int i = 0; i < mesh.tris.size(); i++) {
            Triangle triangle = mesh.tris.get(i);

            triangle.offSet(-mesh.origin.x, -mesh.origin.y, -mesh.origin.z);
            
            triangle = cMatrix.multiTrigByMatrix(triangle, scaleMat);

            triangle.offSet(mesh.origin.x, mesh.origin.y, mesh.origin.z);
        }  

        return mesh;
    }



    // Rotates a mesh
    public Mesh rotateMesh(Mesh mesh, float angularSpeed, char axis) {

        for(int i = 0; i < mesh.tris.size(); i++) {
            mesh.tris.set(i, rotateTriangle(mesh.tris.get(i), angularSpeed, axis, mesh.origin));
        } 

        return mesh;
    }
    // Rotates a triangle
    private Triangle rotateTriangle(Triangle t, float angularSpeed, char axis, Vec3 origin) {

        float[][] rotMat;
        switch (axis) {
            case 'x':
                rotMat = cMatrix.getRotMatX(angularSpeed);
                break;
            case 'y':
                rotMat = cMatrix.getRotMatY(angularSpeed);
                break;
            case 'z':
                rotMat = cMatrix.getRotMatZ(angularSpeed);
                break;
            default:
                return t;
        }

        // Set to the origin
        // Rotations are performed around (1, 1, 1) or (0, 0, 0) (I don't rally know)
        t.offSet(-origin.x, -origin.y, -origin.z);

        t = cMatrix.multiTrigByMatrix(t, rotMat);

        // Set back old coordinates
        t.offSet(origin.x, origin.y, origin.z);

        return t;
    }



    // Comparator used for comparing 2 triangles based on which one has 
    // a larger mean Z value
    class trigComparator implements Comparator<Triangle> {
        @Override
        public int compare(Triangle a, Triangle b) {
            float aZ = a.points[0].z + a.points[2].z + a.points[1].z;
            float bZ = b.points[0].z + b.points[2].z + b.points[1].z;

            if(bZ > aZ) { return 1; } 
            else if (bZ < aZ) { return -1; }
            else { return 0; }
        }
    }
}
