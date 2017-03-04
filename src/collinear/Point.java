package collinear;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Comparator;

/**
 * Created by oderor on 3/4/2017.
 */
public class Point implements Comparable<Point> {
    // constructs the point (x, y)
    public Point(int x, int y) {
        throw new NotImplementedException();
    }

    // draws this point
    public void draw() {
        throw new NotImplementedException();
    }

    // draws the line segment from this point to that point
    public void drawTo(Point that) {
        throw new NotImplementedException();
    }

    // string representation
    public String toString() {
        throw new NotImplementedException();
    }

    // compare two points by y-coordinates, breaking ties by x-coordinates
    public int compareTo(Point that) {
        throw new NotImplementedException();
    }

    // the slope between this point and that point
    public double slopeTo(Point that) {
        throw new NotImplementedException();
    }

    // compare two points by slopes they make with this point
    public Comparator<Point> slopeOrder() {
        throw new NotImplementedException();
    }
}