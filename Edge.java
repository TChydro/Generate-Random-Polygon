import java.util.ArrayList ;

/**
 * @author Nathan Moder
 * A class for dealing with edges in 2D space, also helper methods relating to the
 * generation of random polygons that involve the edges.
 */
public class Edge
{
    public Point a ;
    public Point b ;
    
    //Basic constructor
    Edge(Point a , Point b)
    {
        this.a = a ;
        this.b = b ;
    }
    
    //4 coordinates constructor
    Edge(double ax , double ay , double bx , double by)
    {
        this.a = new Point(ax , ay) ;
        this.b = new Point(bx , by) ;
    }
    
    //2 coordinates to a point constructor
    Edge(double ax , double ay , Point b)
    {
        this.a = new Point(ax , ay) ;
        this.b = b ;
    }
    
    //Point to 2 coordinates constructor
    Edge(Point a , double bx , double by)
    {
        this.a = a ;
        this.b = new Point(by , by) ;
    }
    
    /**
     * @param points an array of points.
     * @return a list of edges such that there is an edge connecting each consecutive
     * point in the original array. There is also an edge connecting the last and first
     * entry of the array.
     */
    public static ArrayList makeEdgeList(Point[] points)
    {
        ArrayList<Edge> edges = new ArrayList<>() ;
        Edge currentEdge ;
        for(int i = 0 ; i < points.length ; i++)
        {
            //This makes our edges, and allows them to wrap around to zero for the last one.
            currentEdge = new Edge(points[i] , points[(i + 1) % points.length]) ;
            edges.add(currentEdge) ;
        }
        
        return edges ;
    }
        
    /**
     * @param hull a set of edges that makes up a polygon.
     * @param n the index of the edge in the hull list whose visibility is being checked.
     * @return True if the edge it is called on does not intersect any edges in the
     * hull, false otherwise.
     */
    public boolean isVisible(ArrayList<Edge> hull , int n)
    {
        Edge currentEdge ;
        Point intersect ;
        
        Point connectorA = hull.get(n).a ;
        Point connectorB = hull.get(n).b ;
        
        for(int i = 0 ; i < hull.size() ; i++)
        {
            currentEdge = hull.get(i) ;
            if(i != n)
            {
                intersect = this.intersection(currentEdge) ;
                if(intersect != null && !(intersect.equals(connectorA) || intersect.equals(connectorB)) )
                {
                    //System.out.println("Edge " + hull.get(n) + " is not visible, intersect at " + intersect + " with edge " + currentEdge) ; ;
                    return false ;
                }
            }
        }
        return true ;
    }
    
    /**
     * @param e an edge
     * @return The point of intersection between the edge the method is called on and the
     * input edge. If there is no intersect, then it returns null.
     */
    public Point intersection(Edge e)
    {
        Point a = this.a ;
        Point b = this.b ;
        Point c = e.a ;
        Point d = e.b ;
        
        double div = determ(a.x - b.x , a.y - b.y , c.x - d.x , c.y - d.y) ;
        if(Math.abs(div) == 0)
        {
            return null ;
        }
        
        double d1 = determ(a.x , a.y , b.x , b.y) ;
        double d2 = determ(c.x , c.y , d.x , d.y) ;
        double x = determ(d1 , a.x - b.x , d2 , c.x - d.x) / div ;
        double y = determ(d1 , a.y - b.y , d2 , c.y - d.y) / div ;
        
        if(x < Math.min(a.x , b.x) || x > Math.max(a.x , b.x))
        {
            return null ;
        }
        if(y < Math.min(a.y , b.y) || y > Math.max(a.y , b.y))
        {
            return null ;
        }
        if(x < Math.min(c.x , d.x) || x > Math.max(c.x , d.x))
        {
            return null ;
        }
        if(y < Math.min(c.y , d.y) || y > Math.max(c.y , d.y))
        {
            return null ;
        }
        
        return new Point(x , y) ;
    }
    
    /**
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @return The determinant of the 2x2 matrix with rows x1 x2 and y1 y2. Used as a
     * helper function for finding the intersection.
     */
    public static double determ(double x1 , double x2 , double y1 , double y2)
    {
        return (x1 * y2 - y1 * x2) ;
    }
    
    /**
     * @return The edge formated ((xa,ya),(xb,yb)).
     */
    @Override
    public String toString()
    {
        return new String("(" + this.a.toString() + " , " + this.b.toString() + ")") ;
    }
}
