package collinear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FastCollinearPoints_hashmap {
    // finds all line segments containing 4 or more points
    private ArrayList<LineSegment> segments = new ArrayList<>();
    private HashMap<Double, ArrayList<Point>> slopeEndPoints = new HashMap<>();

    public FastCollinearPoints_hashmap(Point[] points) {
        if (points == null)
            throw new java.lang.NullPointerException("Null Pointer");

        // Arrays.sort(points);
        Point[] ptsClone = points.clone();
        // the array is firstly sorted along the y-axis,
        // this is for eliminating unnecessary "look back", such as p->r (p.y < r.y),
        Arrays.sort(ptsClone);
        check(ptsClone);
        // there's no need to look back r->p.

        for (int i = 0; i < points.length - 3; i++) {
            Point[] pts = Arrays.copyOfRange(ptsClone, i, points.length);
            Arrays.sort(pts, pts[0].slopeOrder());
            int first = 1, last = 2;
            for (; last < pts.length; last++) {
                double slope = pts[0].slopeTo(pts[first]);
                while (last < pts.length &&
                        Double.compare(slope, pts[0].slopeTo(pts[last])) == 0) last++;
                if (last - first >= 3) {
                    if (endPointAvailable(slope, pts, last - 1))
                        segments.add(new LineSegment(pts[0], pts[last - 1]));
                }
                first = last;
            }
        }

    }

    private boolean endPointAvailable(double slope, Point[] pts, int end) {
        ArrayList<Point> ends = this.slopeEndPoints.get(slope);
        if (ends == null) {
            ends = new ArrayList<Point>();
            ends.add(pts[end]);
            this.slopeEndPoints.put(slope, ends);
            return true;
        } else if (ends.contains(pts[end])) return false;
        else {
            ends.add(ends.size(), pts[end]);
            this.slopeEndPoints.put(slope, ends);
            return true;
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    private void check(Point[] points) {
        // the following only applies to already sorted array of points, say sorted by y-axis
        for (int i = 0; i < points.length - 1; i++)
            if (points[i] == null)
                throw new java.lang.NullPointerException("Null Pointer inside points at index " + i);
            else if (points[i].compareTo(points[i + 1]) == 0)
                throw new IllegalArgumentException("Array has duplicate points");
    }

    public static void main(String[] args) {

    }
}

