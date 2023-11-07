package main;

import Geometry.CustomMatrix;
import Geometry.Mesh;
import Geometry.Triangle;

public class Renderer {

    MainPanel mainPanel;
    CustomMatrix mat4x4;

    
    double fTheta = 0.0f;
    float offSetX = -1.0f;
    float offSetY = 0.0f;
    float offSetZ = 15.0f;

    public Renderer(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.mat4x4 = new CustomMatrix(mainPanel);
    }
    

    public Mesh renderMesh(Mesh mesh) {

        // Update viewing angle
        fTheta += Math.PI / 1.5f / MainPanel.FPS;

        // Wipe all rendered data before starting new render
        mesh.trisRendered.clear();


        // Goes through all of the triangles in the mesh
        for(int i = 0; i < mesh.tris.size(); i++) {

            // Update values of rotation matrices with new fTheta
            mat4x4.updateMatRot(fTheta);


            //Extract a triangle from mesh as a copy of the original
            Triangle triangleRotated = new Triangle(mesh.tris.get(i));
            

            // Rotate around X
            triangleRotated.points[0] = mat4x4.MultiplyMatrixVector(triangleRotated.points[0], mat4x4.matRotX);
            triangleRotated.points[1] = mat4x4.MultiplyMatrixVector(triangleRotated.points[1], mat4x4.matRotX);
            triangleRotated.points[2] = mat4x4.MultiplyMatrixVector(triangleRotated.points[2], mat4x4.matRotX);

            // Rotate around Z
            triangleRotated.points[0] = mat4x4.MultiplyMatrixVector(triangleRotated.points[0], mat4x4.matRotZ);
            triangleRotated.points[1] = mat4x4.MultiplyMatrixVector(triangleRotated.points[1], mat4x4.matRotZ);
            triangleRotated.points[2] = mat4x4.MultiplyMatrixVector(triangleRotated.points[2], mat4x4.matRotZ);


            // Set offset
            Triangle triangleTranslated = new Triangle(triangleRotated);
            triangleTranslated.offSet(offSetX, offSetY, offSetZ);
    

            // Assign projected values to the triangle vertices
            triangleTranslated.points[0] = mat4x4.MultiplyMatrixVector(triangleTranslated.points[0], mat4x4.matProj);
            triangleTranslated.points[1] = mat4x4.MultiplyMatrixVector(triangleTranslated.points[1], mat4x4.matProj);
            triangleTranslated.points[2] = mat4x4.MultiplyMatrixVector(triangleTranslated.points[2], mat4x4.matProj);
    

            // Normalize and scale the triangle into view
            Triangle triangleProjected = new Triangle(triangleTranslated);
            triangleProjected.points[0].x += 1.0f; triangleProjected.points[0].y += 1.0f;
            triangleProjected.points[1].x += 1.0f; triangleProjected.points[1].y += 1.0f;
            triangleProjected.points[2].x += 1.0f; triangleProjected.points[2].y += 1.0f;
            
            triangleProjected.points[0].x *= (0.5f * (float)MainPanel.SCREEN_WIDTH); 
            triangleProjected.points[0].y *= (0.5f * (float)MainPanel.SCREEN_HEIGHT);
            triangleProjected.points[1].x *= (0.5f * (float)MainPanel.SCREEN_WIDTH); 
            triangleProjected.points[1].y *= (0.5f * (float)MainPanel.SCREEN_HEIGHT);
            triangleProjected.points[2].x *= (0.5f * (float)MainPanel.SCREEN_WIDTH); 
            triangleProjected.points[2].y *= (0.5f * (float)MainPanel.SCREEN_HEIGHT);


            // Save rendered triangle on trisRendered
            mesh.trisRendered.add(triangleProjected);
        }
        return mesh;
    }
}
