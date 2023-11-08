package main;

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
    float offSetZ = 15.0f;

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
        }
        return mesh;
    }
}
