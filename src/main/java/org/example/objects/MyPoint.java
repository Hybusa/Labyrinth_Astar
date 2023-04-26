package org.example.objects;

import java.util.List;

public class MyPoint {
    public int x;
    public int y;

    public MyPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static MyPoint calculateCenterPoint(List<MyPoint> points){
        MyPoint center = new MyPoint(0,0);
        for(MyPoint p:points){
            center.x += p.x;
            center.y += p.y;
        }
        center.x /= Math.max(1, points.size());
        center.y /= Math.max(1,points.size());
        return center;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.getClass() != obj.getClass())
            return false;
        return this.x == ((MyPoint) obj).x && this.y == ((MyPoint) obj).y;
    }

    @Override
    public String toString() {
        return "MyPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
