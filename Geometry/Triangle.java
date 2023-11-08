package Geometry;

import java.awt.Polygon;

public class Triangle {
    
    public Vec3[] points = new Vec3[3];

    public Triangle() {
        Vec3 p1 = new Vec3(0, 0, 0);
        Vec3 p2 = new Vec3(0, 0, 0);
        Vec3 p3 = new Vec3(0, 0, 0);

        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
    }
    public Triangle(float p1X, float p1Y, float p1Z, float p2X, float p2Y, float p2Z, float p3X, float p3Y, float p3Z) {
        Vec3 p1 = new Vec3(p1X, p1Y, p1Z);
        Vec3 p2 = new Vec3(p2X, p2Y, p2Z);
        Vec3 p3 = new Vec3(p3X, p3Y, p3Z);

        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
    }
    public Triangle(Triangle triangle) {
        this.points[0] = new Vec3(triangle.points[0]);
        this.points[1] = new Vec3(triangle.points[1]);
        this.points[2] = new Vec3(triangle.points[2]);
    }


    // Adds offset
    public void offSet(float offSetX, float offSetY, float offSetZ) {
        points[0].offSet(offSetX, offSetY, offSetZ);
        points[1].offSet(offSetX, offSetY, offSetZ);
        points[2].offSet(offSetX, offSetY, offSetZ);
    }

    // Prints all of the coords of the vertices of the triangle
    public void print() {
        points[0].print();
        System.out.print(", ");
        points[1].print();
        System.out.print(", ");
        points[2].print();
        System.out.println();
    }

    // Constructs polygon representation of a triangle
    // Used for accessing drawing methods of g2 object
    public Polygon constructPolygon(Triangle triangle) {
        Polygon p;

        int[] xPoints = new int[]{
            (int)triangle.points[0].x,
            (int)triangle.points[1].x,
            (int)triangle.points[2].x
        };
        int[] yPoints = new int[]{
            (int)triangle.points[0].y,
            (int)triangle.points[1].y,
            (int)triangle.points[2].y
        };

        p = new Polygon(xPoints, yPoints, 3);
        return p;
    }
}
