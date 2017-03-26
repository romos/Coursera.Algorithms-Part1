package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oderor on 3/25/2017.
 */

public class KdTree {

    private static class Node {
        // the point
        private Point2D p;
        // the axis-aligned rectangle corresponding to this node
        private RectHV rect;
        // the left/bottom subtree
        private Node lb;
        // the right/top subtree
        private Node rt;
        private int size;

        public Node(Point2D point2D, int size) {
            this.p = point2D;
            this.size = size;
        }

        public Node(Point2D point2D, RectHV rectHV, int size) {
            this.p = point2D;
            this.rect = rectHV;
            this.size = size;
        }
    }

    private enum Orientation {
        HORIZONTAL, VERTICAL;

        public static Orientation next(Orientation orientation) {
            return (orientation == Orientation.HORIZONTAL ? Orientation.VERTICAL : Orientation.HORIZONTAL);
        }
    }

    private Node root;
//    private int size;

    // construct an empty set of points
    public KdTree() {
        root = null;
//        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (isEmpty()) {
            root = new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0), 1);
//            size++;
            return;
        }
        root = put(root, p, Orientation.VERTICAL);
    }

    private Node put(Node x, Point2D point2D, Orientation orientation) {
        if (x == null) {
//            size++;
            // TODO: we might add a rectangle here,
            // but need to pass it from the previous recursion call.
            // Let's go another way: we'll add the RectHV after the child node is added (see below).
            return new Node(point2D, 1);
        }

        // how we are going to split the rectangle: VERTICAL, if current level is HORIZONTAL,
        // and HORIZONTAL otherwise.
        int cmp = compare(point2D, x.p, orientation);
        if (cmp < 0) {
            x.lb = put(x.lb, point2D, Orientation.next(orientation));
            // add rectangle for the current node (only if it is not set already.
            if (x.lb.rect == null)
                switch (orientation) {
                    case HORIZONTAL:
                        x.lb.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.p.y());
                        break;
                    case VERTICAL:
                        x.lb.rect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.p.x(), x.rect.ymax());
                        break;
                }
        } else if (cmp > 0) {
            x.rt = put(x.rt, point2D, Orientation.next(orientation));
            // add rectangle for the current node (only if it is not set already.
            if (x.rt.rect == null)
                switch (orientation) {
                    case HORIZONTAL:
                        x.rt.rect = new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.rect.ymax());
                        break;
                    case VERTICAL:
                        x.rt.rect = new RectHV(x.p.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
                        break;
                }
        } else if (cmp == 0) {
            x.p = point2D;
        }
        x.size = 1 + size(x.lb) + size(x.rt);
        return x;
    }

    private int compare(Point2D p1, Point2D p2, Orientation orientation) {
        // points are identical:
        if (p1.compareTo(p2) == 0)
            return 0;

        // otherwise, compare based on the orientation
        // Note that in each branch there may be identical coordinates. We'll force them to return different result
        // by manually checking the value of compare:
        int cmp;
        switch (orientation) {
            case HORIZONTAL:
                // if y coord. is the same, we still must differentiate the points.
                // In this case, by default let's consider first to be greater.
                cmp = Double.compare(p1.y(), p2.y());
                return cmp == 0 ? 1 : cmp;

            case VERTICAL:
                // if x coord. is the same, we still must differentiate the points
                // In this case, by default let's consider first to be greater.
                cmp = Double.compare(p1.x(), p2.x());
                return cmp == 0 ? 1 : cmp;
        }
        throw new RuntimeException("Illegal comparison of Point2D's: orientation is not specified!");
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return contains(root, p, Orientation.VERTICAL);
    }

    private boolean contains(Node x, Point2D point2D, Orientation orientation) {
//        if (x == null) {
//            return false;
//        }
        Orientation currOrientation = orientation;
        while (x != null) {
            int cmp = compare(point2D, x.p, currOrientation);
            if (cmp < 0) x = x.lb;
            else if (cmp > 0) x = x.rt;
            else return true;
            currOrientation = Orientation.next(currOrientation);
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, Orientation.VERTICAL);
    }

    private void draw(Node x, Orientation orientation) {
        if (x == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();
        switch (orientation) {
            case HORIZONTAL:
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius(0.001);
                StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
                break;
            case VERTICAL:
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius(0.001);
                StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
                break;
        }
        draw(x.lb, Orientation.next(orientation));
        draw(x.rt, Orientation.next(orientation));
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> points = new ArrayList<>();
        findPointsInRect(rect, root, points);
        return points;
    }

    private void findPointsInRect(RectHV rect, Node x, List<Point2D> points) {
        if (x == null) {
            return;
        }

        if (!rect.intersects(x.rect)) {
            return;
            // Instead of checking whether the query rectangle intersects the rectangle corresponding to a node,
            // it suffices to check only whether the query rectangle intersects the splitting line segment:
            //      if it does, then recursively search both subtrees;
            //      otherwise, recursively search the one subtree where points intersecting the query rectangle could be.
            //        if (!((rect.xmin() <= x.p.x() && x.p.x() <= rect.xmax())
            //            && (x.rect.ymin() <= rect.ymin() && rect.ymin() <= x.rect.ymax())
            //            && (x.rect.ymin() <= rect.ymax() && rect.ymax() <= x.rect.ymax()))){
            //        }
        }
        if (rect.contains(x.p)) {
            points.add(x.p);
        }
        findPointsInRect(rect, x.lb, points);
        findPointsInRect(rect, x.rt, points);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        // To find closest point to given point, start at root & recursively search in both subtrees using pruning rule:
        // if the closest point discovered so far is closer than the distance between query point and the rectangle
        // corresponding to a node, there is no need to explore that node (or its subtrees).
        // That is, a node is searched only if it might contain a point that is closer than the best one found so far.
        // The effectiveness of the pruning rule depends on quickly finding a nearby point.
        // To do this, organize your recursive method so that when there are two possible subtrees to go down,
        // you always choose the subtree that is on the same side of the splitting line as the query point
        // as the first subtree to explore - the closest point found while exploring the first subtree may enable pruning
        // of the second subtree.
        if (isEmpty()) {
            return null;
        }
        return findNearest(root, p, null, Double.MAX_VALUE, Orientation.VERTICAL);
    }

    private Point2D findNearest(Node x, Point2D point, Point2D nearestPoint, double nearestDist, Orientation orientation) {
        if (x == null) {
            return nearestPoint;
        }
        // if the closest point discovered so far is closer than the distance between the query point
        // and the rectangle corresponding to a node, there is no need to explore that node (or its subtrees).
        if (nearestDist < x.rect.distanceSquaredTo(point)) {
            return nearestPoint;
        }

        // calculate distance to the current point:
        double dist = x.p.distanceSquaredTo(point);
        if (dist < nearestDist) {
            nearestDist = dist;
            nearestPoint = x.p;
        }
        // now try both subtrees

        // First, search the one on the same side of the splitting line as the query point
        Node firstToSearch = null, secondToSearch = null;
        int cmp = compare(point, x.p, orientation);
        // x.p is left/below the query point
        if (cmp < 0) {
            firstToSearch = x.lb;
            secondToSearch = x.rt;
        }
        // x.p is on the same splitting line or right/above the query point
        else {
            firstToSearch = x.rt;
            secondToSearch = x.lb;
        }
        nearestPoint = findNearest(firstToSearch, point, nearestPoint, nearestDist, Orientation.next(orientation));
        nearestDist = nearestPoint.distanceSquaredTo(point);
        nearestPoint = findNearest(secondToSearch, point, nearestPoint, nearestDist, Orientation.next(orientation));
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
