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

    
    double fTheta = 0.0f;
    float offSetX = 0.0f;
    float offSetY = 0.0f;
    float offSetZ = 40.0f;


    public Renderer(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        this.cMatrix = new CustomMatrix(mainPanel);
        this.vec3 = new Vec3();
        this.trig = new Triangle();
    }

    public Mesh rotateMesh(Mesh mesh, float rotMatrix[][]) {

        ArrayList<Triangle> temporary = new ArrayList<>(); 


        // Goes through all of the triangles in the mesh
        for(int i = 0; i < mesh.tris.size(); i++) {

            //Extract a triangle from mesh as a copy of the original
            Triangle triangleRotated = new Triangle(mesh.tris.get(i));
            
            // Rotate a triangle
            triangleRotated = rotateTriangle(triangleRotated, rotMatrix, 1.0f);
            temporary.add(triangleRotated);
        }  
        mesh.tris = mesh.copyTriangles(temporary);

        return mesh;
    }
    

    public Mesh renderMesh(Mesh mesh) {

        // Update viewing angle
        fTheta += Math.PI / 3.0f / MainPanel.FPS;

        // Wipe all rendered data before starting new render
        mesh.trisRendered.clear();


        // Goes through all of the triangles in the mesh
        for(int i = 0; i < mesh.tris.size(); i++) {

            //Extract a triangle from mesh as a copy of the original
            Triangle triangleRotated = new Triangle(mesh.tris.get(i));
            

            // Rotate a triangle
            triangleRotated = rotateAroundY(triangleRotated, 3.0f);
            // triangleRotated = rotateAroundX(triangleRotated, 2.0f);
            // triangleRotated = rotateAroundZ(triangleRotated, 3.0f);


            // Set offset
            Triangle triangleTranslated = new Triangle(triangleRotated);
            triangleTranslated.offSet(offSetX, offSetY, offSetZ);


            // Calculating normal vector
            Vec3 normal = new Vec3(); 
            Vec3 l1 = new Vec3(); 
            Vec3 l2 = new Vec3();

            l1 = vec3.subtract(triangleTranslated.points[1], triangleTranslated.points[0]);
            l2 = vec3.subtract(triangleTranslated.points[2], triangleTranslated.points[0]);
            normal = vec3.getCrossProduct(l1, l2);

            
            // Proceed with projection only if:
            // normal is between being parallel and at a right angle to Vector from a camera to this triangle
            if (vec3.getDotProduct(normal, vec3.subtract(triangleTranslated.points[0], mainPanel.cameraPos)) < 0.0f) {
                
                // Illumination
                Vec3 lightDir = vec3.normalize(mainPanel.lightDir);
                // Compare how similar normal of a triangle is to the direction of the light
                float lightInt = vec3.getDotProduct(lightDir, normal); 


                // 3D projected into 2D space
                // Assign projected values to the triangle vertices
                Triangle triangleProjected = new Triangle(triangleTranslated);
                triangleProjected.points[0] = cMatrix.MultiplyVectorByMatrix(triangleProjected.points[0], cMatrix.matProj);
                triangleProjected.points[1] = cMatrix.MultiplyVectorByMatrix(triangleProjected.points[1], cMatrix.matProj);
                triangleProjected.points[2] = cMatrix.MultiplyVectorByMatrix(triangleProjected.points[2], cMatrix.matProj);


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




    public Triangle rotateTriangle(Triangle t, float[][] rotMatrix, float rotMulti) {

        t.points[0] = cMatrix.MultiplyVectorByMatrix(t.points[0], rotMatrix);
        t.points[1] = cMatrix.MultiplyVectorByMatrix(t.points[1], rotMatrix);
        t.points[2] = cMatrix.MultiplyVectorByMatrix(t.points[2], rotMatrix);

        return t;
    }


    // Methods for rotating mesh around around a point of origin in X/Y/Z axis
    public Triangle rotateAroundX(Triangle t, float rotMulti) {
        // Update values of rotation matrices with new fTheta
        cMatrix.updateRotMat(fTheta, rotMulti);

        t.points[0] = cMatrix.MultiplyVectorByMatrix(t.points[0], cMatrix.matRotX);
        t.points[1] = cMatrix.MultiplyVectorByMatrix(t.points[1], cMatrix.matRotX);
        t.points[2] = cMatrix.MultiplyVectorByMatrix(t.points[2], cMatrix.matRotX);

        return t;
    }
    public Triangle rotateAroundY(Triangle t, float rotMulti) {
        // Update values of rotation matrices with new fTheta
        cMatrix.updateRotMat(fTheta, rotMulti);

        t.points[0] = cMatrix.MultiplyVectorByMatrix(t.points[0], cMatrix.matRotY);
        t.points[1] = cMatrix.MultiplyVectorByMatrix(t.points[1], cMatrix.matRotY);
        t.points[2] = cMatrix.MultiplyVectorByMatrix(t.points[2], cMatrix.matRotY);

        return t;
    }
    public Triangle rotateAroundZ(Triangle t, float rotMulti) {
        // Update values of rotation matrices with new fTheta
        cMatrix.updateRotMat(fTheta, rotMulti);

        t.points[0] = cMatrix.MultiplyVectorByMatrix(t.points[0], cMatrix.matRotZ);
        t.points[1] = cMatrix.MultiplyVectorByMatrix(t.points[1], cMatrix.matRotZ);
        t.points[2] = cMatrix.MultiplyVectorByMatrix(t.points[2], cMatrix.matRotZ);

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
