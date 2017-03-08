// package collinear;

import java.util.*;

/**
 * Created by oderor on 3/4/2017.
 */
public class FastCollinearPoints {
    private int numberOfSegments = 0;
    private LineSegment[] segments;
    private int minNumOfPointsOnLine = 4;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Points must not repeat!");
                }
            }
        }
        // get a copy of points due to immutability, and another copy to keep slopeOrder
        int numberOfPoints = points.length;
        if (numberOfPoints < minNumOfPointsOnLine){
            segments = new LineSegment[numberOfSegments];
            return;
        }
        Point[] points1 = points.clone();
        Arrays.sort(points1);
        Point[] pointsInSlopeOrder = points.clone();
        List<LineSegment> lineSegmentList = new ArrayList<>();

        // for each point P sort other points in slopeOrder and get 4-sets which have the same slope

        for (int i = 0; i < numberOfPoints; i++) {
            Point p = points1[i];

//            Point[] pointsInSlopeOrder = Arrays.copyOfRange(points1, i + 1, numberOfPoints);
            sort(pointsInSlopeOrder, p.slopeOrder()); // sort all the points due to 'p' slopeOrder
            List<Point> collinearPoints = new ArrayList<>();
            int numCollinearPoints = 0;
            double slope = p.slopeTo(pointsInSlopeOrder[1]);
            double prevSlope;
            for (int j = 1; j < pointsInSlopeOrder.length; j++) { // find points with the same slope
                if (pointsInSlopeOrder[j] == p) {
                    continue;
                }
                Point q = pointsInSlopeOrder[j];
                prevSlope = slope;
                slope = p.slopeTo(q);

                if (Double.compare(prevSlope,slope) == 0) {
                    collinearPoints.add(q);
                    numCollinearPoints++;
                } else {
                    if (numCollinearPoints >= minNumOfPointsOnLine - 1) {
                        collinearPoints.add(p);
                        addLineSegment(lineSegmentList, collinearPoints);
                    }
                    collinearPoints.clear();
                    collinearPoints.add(q);
                    numCollinearPoints = 1;
                }
            }
        }

        segments = new LineSegment[numberOfSegments];
        lineSegmentList.toArray(segments);
        lineSegmentList.clear();
    }

    private void addLineSegment(List<LineSegment> lineSegments, List<Point> collinearPoints) {
//        Collections.sort(collinearPoints);
        lineSegments.add(new LineSegment(collinearPoints.get(0),
                collinearPoints.get(collinearPoints.size()-1)));
    }

    private void sort(Point[] points, Comparator<Point> comparator) {
//        Arrays.sort(points, comparator);
        mergeSort(points, comparator);
    }

    // the number of line segments
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.clone();
    }

    // ==================================================
    // MERGE BU SORT
    // ==================================================
    // stably merge a[lo..mid] with a[mid+1..hi] using aux[lo..hi]
    private static void merge(Point[] a, Point[] aux, int lo, int mid, int hi, Comparator<Point> comparator) {

        // copy to aux[]
        System.arraycopy(a, lo, aux, lo, hi + 1 - lo);

        // merge back to a[]
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];  // this copying is unneccessary
            else if (j > hi) a[k] = aux[i++];
            else if (less(aux[j], aux[i], comparator)) a[k] = aux[j++];
            else a[k] = aux[i++];
        }

    }

    /**
     * Rearranges the array in ascending order, using the natural order.
     *
     * @param a the array to be sorted
     */
    private static void mergeSort(Point[] a, Comparator<Point> comparator) {
        int n = a.length;
        Point[] aux = new Point[n];
        for (int len = 1; len < n; len *= 2) {
            for (int lo = 0; lo < n - len; lo += len + len) {
                int mid = lo + len - 1;
                int hi = Math.min(lo + len + len - 1, n - 1);
                merge(a, aux, lo, mid, hi, comparator);
            }
        }
    }

    /***********************************************************************
     * Helper sorting functions.
     ***************************************************************************/

    // is v < w ?
    private static boolean less(Point v, Point w, Comparator<Point> comparator) {
        return comparator.compare(v, w) < 0;
    }

}