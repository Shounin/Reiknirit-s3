
/*************************************************************************
 *************************************************************************/

import java.util.Arrays;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.Out;

public class KdTree {

    private static class Node{
        Point2D point;
        Node left;
        Node right;
        RectHV rect;

        Node(Point2D p){
            this.point = p;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;
    private static int size;
    private boolean alignment = true; //True = horizontal, False = vertical

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if(isEmpty()){
            root = new Node(p);
            size++;
        }
        else {
            root = insert(p, root, alignment);
        }
    }

    private Node insert(Point2D p, Node n, boolean xy){
       if(n == null){
           size++;
           return new Node(p);
       }
        if(p.equals(n.point)){
            return n;
        }
        if(xy){
            if(p.x() < n.point.x()){
                n.left = insert(p, n.left, !xy);
            }
            if(p.x() > n.point.x()){
                n.right = insert(p, n.right, !xy);
            }
        }
        else if(!xy){
            if(p.y() < n.point.y()){
                n.left = insert(p, n.left, !xy);
            }
            if(p.y() > n.point.y()){
                n.right = insert(p, n.right, !xy);
            }
        }
        return n;
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return contains(p, root, alignment);
    }

    public boolean contains(Point2D p, Node n, boolean xy){
        if(n == null){
            return false;
        }
        if(xy){
            if(p.x() < n.point.x()){
                return contains(p, n.left, !xy);
            }
            if(p.x() > n.point.x()){
                return contains(p, n.right, !xy);
            }
        }
        else if(!xy){
            if(p.y() < n.point.y()){
                return contains(p, n.left, !xy);
            }
            if(p.y() > n.point.y()){
                return contains(p, n.right, !xy);
            }
        }
        return p.equals(n.point);
    }

    // draw all of the points to standard draw
    public void draw() {

    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        return null;
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        return p;
    }

    /*******************************************************************************
     * Test client
     ******************************************************************************/
    public static void main(String[] args) {
        In in = new In();
        Out out = new Out();
        int nrOfRectangles = in.readInt();
        int nrOfPointsCont = in.readInt();
        int nrOfPointsNear = in.readInt();
        RectHV[] rectangles = new RectHV[nrOfRectangles];
        Point2D[] pointsCont = new Point2D[nrOfPointsCont];
        Point2D[] pointsNear = new Point2D[nrOfPointsNear];
        for (int i = 0; i < nrOfRectangles; i++) {
            rectangles[i] = new RectHV(in.readDouble(), in.readDouble(),
                    in.readDouble(), in.readDouble());
        }
        for (int i = 0; i < nrOfPointsCont; i++) {
            pointsCont[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        for (int i = 0; i < nrOfPointsNear; i++) {
            pointsNear[i] = new Point2D(in.readDouble(), in.readDouble());
        }
        KdTree set = new KdTree();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble(), y = in.readDouble();
            set.insert(new Point2D(x, y));
        }
        for (int i = 0; i < nrOfRectangles; i++) {
            // Query on rectangle i, sort the result, and print
            Iterable<Point2D> ptset = set.range(rectangles[i]);
            int ptcount = 0;
            for (Point2D p : ptset)
                ptcount++;
            Point2D[] ptarr = new Point2D[ptcount];
            int j = 0;
            for (Point2D p : ptset) {
                ptarr[j] = p;
                j++;
            }
            Arrays.sort(ptarr);
            out.println("Inside rectangle " + (i + 1) + ":");
            for (j = 0; j < ptcount; j++)
                out.println(ptarr[j]);
        }
        out.println("Contain test:");
        for (int i = 0; i < nrOfPointsCont; i++) {
            out.println((i + 1) + ": " + set.contains(pointsCont[i]));
        }

        out.println("Nearest test:");
        for (int i = 0; i < nrOfPointsNear; i++) {
            out.println((i + 1) + ": " + set.nearest(pointsNear[i]));
        }
        out.println();
    }
}
