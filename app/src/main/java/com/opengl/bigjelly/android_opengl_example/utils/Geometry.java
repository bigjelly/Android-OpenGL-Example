package com.opengl.bigjelly.android_opengl_example.utils;


public class Geometry {

    public static class Point {
        public final float x, y, z;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point translateY(float distance) {
            return new Point(x, y + distance, z);
        }

        public Point translate(Vector vector) {
            return new Point(x + vector.x, y + vector.y, z + vector.z);
        }
    }


    public static class Ray {
        public final Point point;
        public final Vector vector;

        public Ray(Point point, Vector vector) {
            this.point = point;
            this.vector = vector;
        }

    }

    public static class Sphere {
        public final Point center;
        public final float radius;

        public Sphere(Point center,float radius){
            this.center = center;
            this.radius = radius;
        }
    }

    public static class Circle {
        public final Point center;
        public final float radius;

        public Circle(Point center, float radius) {
            this.center = center;
            this.radius = radius;
        }

        public Circle scale(float scale) {
            return new Circle(center, radius * scale);
        }
    }

    public static class Cylinder {
        public final Point center;
        public final float radius;
        public final float height;

        public Cylinder(Point center, float radius, float height) {
            this.center = center;
            this.radius = radius;
            this.height = height;
        }
    }

    public static class Vector {
        public final float x, y, z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        //勾股定理返回向量的长度
        public float length() {
            return (float) Math.sqrt(x * x + y * y + z * z);
        }

        //计算两个向量的交叉乘积
        public Vector crossProduct(Vector other) {
            return new Vector(
                    (y * other.z) - (z * other.y),
                    (z * other.x) - (x * other.z),
                    (x * other.y) - (y * other.x)
            );
        }

        //计算向量和平面的点积
        public float dotProduct(Vector other) {
            return x * other.x + y * other.y + z * other.z;
        }

        //取得缩放量后，均匀缩放向量的每个分量
        public Vector scale(float f) {
            return new Vector(
                    x * f, y * f, z * f
            );
        }
    }

    //线与球体相交测试
    public static boolean intersects(Sphere sphere,Ray ray){

        return distanceBetween(sphere.center,ray)< sphere.radius;
    }

    public static float distanceBetween(Point point,Ray ray){
        //第一点到球心
        Vector p1ToPoint = vectorBetween(ray.point,point);
        //第二点到球心
        Vector p2ToPoint = vectorBetween(ray.point.translate(ray.vector),point);

        //三角形面积 X 2
        float areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length();
        float lengthOfBase = ray.vector.length();

        //底乘以高 / 2
        float distanceFromPointToRay  = areaOfTriangleTimesTwo/lengthOfBase;

        return distanceFromPointToRay;
    }

    public static Vector vectorBetween(Point from,Point to){
        return new Vector(to.x - from.x,to.y - from.y,to.z - from.z);
    }
}
