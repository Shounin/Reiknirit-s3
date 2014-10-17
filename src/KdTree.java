
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
        Node parent;
        boolean alignment;

        Node(Point2D p, Node parent, RectHV rect){
            this.point = p;
            this.left = null;
            this.right = null;
            this.parent = parent;
            this.rect = rect;

            if(parent == null){ alignment = true; } //True = Horizontal search (x), false = Vertical search (y)
            else{
                this.alignment = setAlignment(this, parent);
            }
        }

        private boolean setAlignment(Node n, Node parent){
            if(parent.left == n){
                return true;
            }
            else{
                return false;
            }
        }
    }

    private Node root;
    private static int size;

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    //Make a rectangle
    private RectHV makeRect(Node parent, boolean level){
        if(parent == null){
            return new RectHV(0,0,1,1);
        }
        if(level){
            if(parent.alignment){
                return new RectHV(parent.rect.xmin(), parent.rect.ymin(),parent.rect.xmax(), parent.point.y());
            }
            else{
                return new RectHV(parent.rect.xmin(), parent.point.y(),parent.rect.xmax(), parent.rect.ymax());
            }
        }
        else{
            if(parent.alignment){
                return new RectHV(parent.rect.xmin(), parent.rect.ymin(),parent.point.x(), parent.rect.ymax());
            }
            else{
                return new RectHV(parent.point.x(), parent.rect.ymin(),parent.rect.xmax(), parent.rect.ymax());
            }
        }
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
            root = new Node(p, null, makeRect(null, true));
            size++;
        }
        else if(!contains(p)){
            root = insert(p, null, root, true);
        }
    }

    private Node insert(Point2D p, Node parent, Node n, boolean xy){
       if(n == null){
           size++;
           return new Node(p, parent, makeRect(parent, xy));
       }
        if(p.equals(n.point)){
            return n;
        }

        //Horizontal search
        if(xy){
            if(p.x() < n.point.x()){
                n.left = insert(p, n, n.left, !xy);
            }
            if(p.x() >= n.point.x()){
                n.right = insert(p, n, n.right, !xy);
            }
        }

        //Vertical search
        else{
            if(p.y() < n.point.y()){
                n.left = insert(p, n, n.left, !xy);
            }
            if(p.y() >= n.point.y()){
                n.right = insert(p, n, n.right, !xy);
            }
        }
        return n;
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return contains(p, null, root, true);
    }

    public boolean contains(Point2D p, Node parent, Node n, boolean xy){
        if(n == null){
            return false;
        }

        //Horizontal search
        if(xy){
            if(p.x() < n.point.x()){
                return contains(p, n, n.left, !xy);
            }
            if(p.x() > n.point.x()){
                return contains(p, n, n.right, !xy);
            }
        }

        //Vertical search
        else if(!xy){
            if(p.y() < n.point.y()){
                return contains(p, n, n.left, !xy);
            }
            if(p.y() > n.point.y()){
                return contains(p, n, n.right, !xy);
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
