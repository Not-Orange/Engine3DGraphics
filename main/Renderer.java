package main;

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

    

    public Mesh renderMesh(Mesh mesh) {

        // Wipe all rendered data before starting new render
        mesh.trisRendered.clear();

        // Go through all of the triangles in the mesh
        for(int i = 0; i < mesh.tris.size(); i++) {

            //Extract a triangle from mesh as a copy of the original
            Triangle triangle = new Triangle(mesh.tris.get(i));
            
            // Calculate normal vector
            Vec3 normal = new Vec3(); 
            Vec3 l1 = new Vec3(); 
            Vec3 l2 = new Vec3();

            l1 = vec3.subtract(triangle.points[1], triangle.points[0]);
            l2 = vec3.subtract(triangle.points[2], triangle.points[0]);
            normal = vec3.getCrossProduct(l1, l2);
            
            // Proceed with projection only if:
            // normal is between being parallel and at a right angle to Vector from a camera to this triangle
            if (vec3.getDotProduct(normal, vec3.subtract(triangle.points[0], mainPanel.cameraPos)) < 0.0f) {
                
                // Illumination
                Vec3 lightDir = vec3.normalize(mainPanel.lightDir);
                // Compare how similar normal of a triangle is to the direction of the light
                float lightInt = vec3.getDotProduct(lightDir, normal); 

                // 3D projected into 2D space
                // Assign projected values to the triangle vertices
                Triangle triangleProjected = new Triangle(triangle);
                triangleProjected = cMatrix.multiTrigByMatrix(triangleProjected, cMatrix.matProj);

                // Normalize and scale the triangle into view
                Triangle triangleNormalized = new Triangle(triangleProjected);
                triangleNormalized.points[0].x += 1.0f; triangleNormalized.points[0].y += 1.0f;
                triangleNormalized.points[1].x += 1.0f; triangleNormalized.points[1].y += 1.0f;
                triangleNormalized.points[2].x += 1.0f; triangleNormalized.points[2].y += 1.0f;
                
                triangleNormalized.points[0].x *= (0.5f * (float)MainPanel.SCREEN_WIDTH); 
                triangleNormalized.points[0].y *= (0.5f * (float)MainPanel.SCREEN_HEIGHT);
                triangleNormalized.points[1].x *= (0.5f * (float)MainPanel.SCREEN_WIDTH); 
                triangleNormalized.points[1].y *= (0.5f * (float)MainPanel.SCREEN_HEIGHT);
                triangleNormalized.points[2].x *= (0.5f * (float)MainPanel.SCREEN_WIDTH); 
                triangleNormalized.points[2].y *= (0.5f * (float)MainPanel.SCREEN_HEIGHT);
                //Add illumination information
                triangleNormalized.lightIntensity = lightInt;  

                // Save rendered triangle on trisRendered
                mesh.trisRendered.add(triangleNormalized);
            }

            // Sort all triangles in mesh such that the ones with the 
            // largest values of Z (the furthest from observer) are first.
            // When drawing triangles that are closer than others will then be
            // drawn on top of them.
            mesh.trisRendered.sort(new trigComparator());
        }
        return mesh;
    }



    // Offsets whole mesh, saves a new origin point for that mesh
    public Mesh offSetMesh(Mesh mesh, float offSetX, float offSetY, float offSetZ) {

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
            Triangle triangleRotated = rotateTriangle(mesh.tris.get(i), angularSpeed, axis, mesh.origin);
            mesh.tris.set(i, triangleRotated);
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
