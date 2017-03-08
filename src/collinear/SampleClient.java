package collinear;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

/**
 * Created by oderor on 3/4/2017.
 */
public class SampleClient {
    public static void main(String[] args) {
//        testFromFile("C:\\workspace\\coursera\\algorithms_part1\\algs4-exercises\\src\\test\\collinear\\kw1260.txt");
        testFromFile("C:\\workspace\\coursera\\algorithms_part1\\algs4-exercises\\src\\test\\collinear\\horizontal5.txt");
//        testFromFile("C:\\workspace\\coursera\\algorithms_part1\\algs4-exercises\\src\\test\\collinear\\vertical5.txt");
    }

    private static void testPoints(){
        int x0 = 50;
        int y0 = 50;
        int n = 1000;

        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0, 100);
        StdDraw.setYscale(0, 100);
        StdDraw.setPenRadius(0.005);
        StdDraw.enableDoubleBuffering();

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = StdRandom.uniform(100);
            int y = StdRandom.uniform(100);
            points[i] = new Point(x, y);
            points[i].draw();
        }

        // draw p = (x0, x1) in red
        Point p = new Point(x0, y0);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.02);
        p.draw();


        // draw line segments from p to each point, one at a time, in polar order
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.BLUE);
        Arrays.sort(points, p.slopeOrder());
        for (int i = 0; i < n; i++) {
            LineSegment lineSegment = new LineSegment(p, points[i]);
            lineSegment.draw();
            StdOut.println(lineSegment.toString());
//            p.drawTo(points[i]);
            StdDraw.show();
            StdDraw.pause(10);
        }
    }

    private static void testFromFile(String file){
        // read the n points from a file
        In in = new In(file);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.005);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
//        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        FastCollinearPoints collinear = new FastCollinearPoints(points);
//        FastCollinearPoints_hashmap collinear = new FastCollinearPoints_hashmap(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
