package main;

import Geometry.CustomMatrix;
import Geometry.Mesh;
import Geometry.Triangle;
import Geometry.Vec3;

public class Renderer {

    MainPanel mainPanel;
    CustomMatrix mat4x4;
    Vec3 vec3 = new Vec3();
    
    double fTheta = 0.0f;
    float offSetX = 0.0f;
    float offSetY = 0.0f;
    float offSetZ = 20.0f;

    public Renderer(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.mat4x4 = new CustomMatrix(mainPanel);
    }
    

    public Mesh renderMesh(Mesh mesh) {

        // Update viewing angle
        fTheta += Math.PI / 3.0f / MainPanel.FPS;

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

            // Rotate around Y
            triangleRotated.points[0] = mat4x4.MultiplyMatrixVector(triangleRotated.points[0], mat4x4.matRotY);
            triangleRotated.points[1] = mat4x4.MultiplyMatrixVector(triangleRotated.points[1], mat4x4.matRotY);
            triangleRotated.points[2] = mat4x4.MultiplyMatrixVector(triangleRotated.points[2], mat4x4.matRotY);


            // Set offset
            Triangle triangleTranslated = new Triangle(triangleRotated);
            triangleTranslated.offSet(offSetX, offSetY, offSetZ);

            // Calculating normal vector
            Vec3 normal = new Vec3(); 
            Vec3 l1 = new Vec3(); 
            Vec3 l2 = new Vec3();

            l1.x = triangleTranslated.points[1].x - triangleTranslated.points[0].x;
            l1.y = triangleTranslated.points[1].y - triangleTranslated.points[0].y;
            l1.z = triangleTranslated.points[1].z - triangleTranslated.points[0].z;

            l2.x = triangleTranslated.points[2].x - triangleTranslated.points[0].x;
            l2.y = triangleTranslated.points[2].y - triangleTranslated.points[0].y;
            l2.z = triangleTranslated.points[2].z - triangleTranslated.points[0].z;

            normal = normal.getCrossProduct(l1, l2);


            // Assign projected values to the triangle vertices (project 3D into 2D)
            triangleTranslated.points[0] = mat4x4.MultiplyMatrixVector(triangleTranslated.points[0], mat4x4.matProj);
            triangleTranslated.points[1] = mat4x4.MultiplyMatrixVector(triangleTranslated.points[1], mat4x4.matProj);
            triangleTranslated.points[2] = mat4x4.MultiplyMatrixVector(triangleTranslated.points[2], mat4x4.matProj);
    

            // Display only if normal to a triangle is not pointing away from the viewer on z axis
            if (vec3.getDotProduct(normal, vec3.subtractVecs3(triangleTranslated.points[0], mainPanel.cameraPos)) < 0.0f) {

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
        }
        return mesh;
    }
}
