package main;

import Geometry.CustomMatrix;
import Geometry.Mesh;
import Geometry.Triangle;
import Geometry.Vec3;

public class Renderer {

    MainPanel mainPanel;

    CustomMatrix cMatrix;
    Vec3 vec3;
    Triangle trig;

    
    double fTheta = 0.0f;
    float offSetX = 0.0f;
    float offSetY = 0.0f;
    float offSetZ = 20.0f;

    public Renderer(MainPanel mainPanel) {
        this.mainPanel = mainPanel;

        this.cMatrix = new CustomMatrix(mainPanel);
        this.vec3 = new Vec3();
        this.trig = new Triangle();
    }
    

    public Mesh renderMesh(Mesh mesh) {

        // Update viewing angle
        fTheta += Math.PI / 3.0f / MainPanel.FPS;

        // Wipe all rendered data before starting new render
        mesh.trisRendered.clear();


        // Goes through all of the triangles in the mesh
        for(int i = 0; i < mesh.tris.size(); i++) {

            // Update values of rotation matrices with new fTheta
            cMatrix.updateRotMat(fTheta);


            //Extract a triangle from mesh as a copy of the original
            Triangle triangleRotated = new Triangle(mesh.tris.get(i));
            

            // Rotate around X
            triangleRotated.points[0] = cMatrix.MultiplyVectorByMatrix(triangleRotated.points[0], cMatrix.matRotX);
            triangleRotated.points[1] = cMatrix.MultiplyVectorByMatrix(triangleRotated.points[1], cMatrix.matRotX);
            triangleRotated.points[2] = cMatrix.MultiplyVectorByMatrix(triangleRotated.points[2], cMatrix.matRotX);

            // Rotate around Z
            triangleRotated.points[0] = cMatrix.MultiplyVectorByMatrix(triangleRotated.points[0], cMatrix.matRotZ);
            triangleRotated.points[1] = cMatrix.MultiplyVectorByMatrix(triangleRotated.points[1], cMatrix.matRotZ);
            triangleRotated.points[2] = cMatrix.MultiplyVectorByMatrix(triangleRotated.points[2], cMatrix.matRotZ);

            // Rotate around Y
            triangleRotated.points[0] = cMatrix.MultiplyVectorByMatrix(triangleRotated.points[0], cMatrix.matRotY);
            triangleRotated.points[1] = cMatrix.MultiplyVectorByMatrix(triangleRotated.points[1], cMatrix.matRotY);
            triangleRotated.points[2] = cMatrix.MultiplyVectorByMatrix(triangleRotated.points[2], cMatrix.matRotY);


            // Set offset
            Triangle triangleTranslated = new Triangle(triangleRotated);
            triangleTranslated.offSet(offSetX, offSetY, offSetZ);


            // Assign projected values to the triangle vertices (project 3D into 2D)
            Triangle triangleProjected = new Triangle(triangleTranslated);
            triangleProjected.points[0] = cMatrix.MultiplyVectorByMatrix(triangleProjected.points[0], cMatrix.matProj);
            triangleProjected.points[1] = cMatrix.MultiplyVectorByMatrix(triangleProjected.points[1], cMatrix.matProj);
            triangleProjected.points[2] = cMatrix.MultiplyVectorByMatrix(triangleProjected.points[2], cMatrix.matProj);


            // Calculating normal vector
            Vec3 normal = new Vec3(); 
            Vec3 l1 = new Vec3(); 
            Vec3 l2 = new Vec3();

            l1 = vec3.subtract(triangleProjected.points[1], triangleProjected.points[0]);
            l2 = vec3.subtract(triangleProjected.points[2], triangleProjected.points[0]);
            normal = vec3.getCrossProduct(l1, l2);
    

            // Display only if normal to a triangle is between being parallel and at a right angle to 
            // Vector from a camera to this triangle
            if (vec3.getDotProduct(normal, vec3.subtract(triangleProjected.points[0], mainPanel.cameraPos)) < 0.0f) {

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
    
    
                // Save rendered triangle on trisRendered
                mesh.trisRendered.add(triangleNormalized);
            }
            // mesh.trisRendered.add(normalTrig);
        }
        return mesh;
    }
}
