// package collinear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oderor on 3/4/2017.
 */
public class BruteCollinearPoints {
    private int numberOfSegments = 0;
    private LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        numberOfSegments = 0;

        // check for null reference
        if (points == null) {
            throw new NullPointerException("Points array must not be NULL!");
        }
        // no null points allowed
        for (Point p : points) {
            if (p == null) {
                throw new NullPointerException("A point must not be NULL!");
            }
        }
        // no repeated points allowed
        for (int i = 0; i < points.length; i++) {
            for (int j = i+1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Points must not repeat!");
                }
            }
        }

        // get a copy of points array and sort it
        // Sorting allows to write down lineSegments start and end points in order,
        // thus independently of the order they were discovered.
        Point[] points1 = points.clone();
        Arrays.sort(points1);

        List<LineSegment> lineSegmentList = new ArrayList<>();

        int numberOfPoints = points1.length;
        for (int i = 0; i < numberOfPoints; i++) {
            Point p1 = points1[i];
            for (int j = i + 1; j < numberOfPoints; j++) {
                Point p2 = points1[j];
                for (int k = j + 1; k < numberOfPoints; k++) {
                    Point p3 = points1[k];
                    for (int l = k + 1; l < numberOfPoints; l++) {
                        Point p4 = points1[l];
                        double slope12 = p1.slopeTo(p2);
                        double slope13 = p1.slopeTo(p3);
                        double slope14 = p1.slopeTo(p4);
                        if (slope12 == slope13
                                && slope12 == slope14
                                && slope13 == slope14) {
                            lineSegmentList.add(new LineSegment(p1, p4));
                            numberOfSegments++;
                        }
                    }
                }
            }
        }
        segments = new LineSegment[numberOfSegments];
        lineSegmentList.toArray(segments);
        lineSegmentList.clear();
    }

    // the number of line segments
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.clone();
    }
}
