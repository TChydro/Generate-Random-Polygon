import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.Comparator ;

/**
 * @author Nathan Moder
 * A class for dealing with points in 2D space. Also some helper methods for random
 * polygon generation that use the points/vertices.
 */
public class Point
{
    public double x ;
    public double y ;
    
    //Compare based on Y value. Break ties with smallest X.
    public static Comparator<Point> yPosition = new Comparator<Point>()
    {
        public int compare(Point a , Point b)
        {
            if(a.y < b.y)
            {
                return -1 ;
            }
            else if(a.y > b.y)
            {
                return 1 ;
            }
            else if(a.x < b.x)
            {
                return -1 ;
            }
            else if(a.x > b.x)
            {
                return 1 ;
            }
            else
            {
                return 0 ;
            }
        }
    } ;
    
    //Compare points based on x value, break ties with the y value.
    public static Comparator<Point> xPosition = new Comparator<Point>()
    {
        public int compare(Point a , Point b)
        {
            if(a.x < b.x)
            {
                return 1 ;
            }
            else if(a.x > b.x)
            {
                return -1 ;
            }
            else if(a.y < b.y)
            {
                return -1 ;
            }
            else if(a.y > b.y)
            {
                return 1 ;
            }
            else
            {
                return 0 ;
            }
        }
    } ;
    
    //Basic constructor
    Point(double x , double y)
    {
        this.x = x ;
        this.y = y ;
    }
    
    //Polar constructor, makes a point "r" distance from "a" at the given angle.
    Point(Point a , double r , double angle)
    {
        this.x = a.x + (r * Math.cos(angle)) ;
        this.y = a.y + (r * Math.sin(angle)) ;
    }
    
    //returns the slope between two points.
    public static double slope(Point a , Point b)
    {
        return (b.y - a.y) / (b.x - a.x) ;
    }
    
    //returns the orientation of a set of three points.
    //-1 = clockwise, 1 = counter clockwise, 0 = collinear
    public static int orientation(Point a , Point b , Point c)
    {
        double area = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x) ;
        if(area < 0)
        {
            return -1 ;
        }
        else if(area > 0)
        {
            return 1 ;
        }
        else
        {
            return 0 ;
        }
    }
    
    //generates a list of n random points on a 2d plane.
    public static Point[] generatePoints(int n , int scaleX , int scaleY)
    {
        Point[] pointSet = new Point[n] ;
        double x ;
        double y ;
        Point p ;
        
        for(int i = 0 ; i < n ; i++)
        {
            x = Math.random() * scaleX ;
            y = Math.random() * scaleY ;
            p = new Point(x , y) ;
            pointSet[i] = p ;
        }
        
        return pointSet ;
    }
    
    //Takes a list of points and returns the convex hull.
    public static Point[] convexHull(Point[] points)
    {
        int n = points.length ;
        Point[] pointsCopy = new Point[n] ;
        ArrayList<Point> hullList = new ArrayList<>() ;
        System.arraycopy(points, 0, pointsCopy, 0, n);
        
        //Sort the array by increasing x position. 
        Arrays.sort(pointsCopy , xPosition) ;
        
        //This should be the leftmost point now.
        Point pointOnHull = pointsCopy[0] ;
        hullList.add(pointOnHull) ;
        Point endpoint ;
        
        int currentIndex = 0 ;
        int nextIndex = 0 ;
        do
        {
            endpoint = pointsCopy[0] ;
            nextIndex = 0 ;
            for(int i = 0 ; i < pointsCopy.length ; i++)
            {
                if(i == currentIndex)
                {
                    continue ;
                }
                double crossProd = crossProductLength(pointOnHull , pointsCopy[i] , endpoint) ;
                if(nextIndex == currentIndex || crossProd > 0)
                {
                    endpoint = pointsCopy[i] ;
                    nextIndex = i ;
                }
            }
            
            hullList.add(endpoint) ;
            pointOnHull = endpoint ;
            currentIndex = nextIndex ;
        }while(currentIndex != 0) ;
        
        //Moving the hull out of the list and into an array.
        int hullVerticies = hullList.size() ;
        
        //We do the -1 here because the algorithm adds the initial point to the end of the array.
        //The list of points is assumed to loop back on itself when the polygon is constructed,
        //so it is not needed.
        Point[] hull = new Point[hullVerticies - 1] ;
        
        for(int i = 0 ; i < hullVerticies - 1 ; i++)
        {
            hull[i] = hullList.get(i) ;
        }
        
        return hull ;
    }
    
    //Helper function used for finding the convex hull.
    private static double crossProductLength(Point a , Point b , Point c)
    {
        double baX = a.x - b.x ;
        double baY = a.y - b.y ;
        double bcX = c.x - b.x ;
        double bcY = c.y - b.y ;
        
        return(baX * bcY - baY * bcX) ;
    }
    
    //Converts a list of edges into a list of points such that there is an edge 
    //connecting each point to the next and previous point in the list.
    public static ArrayList makePointList(ArrayList<Edge> edges)
    {
        ArrayList<Point> points = new ArrayList<>() ;
        System.out.println("There are: " + edges.size() + " edges.") ;
        points.add(edges.get(0).b) ;
        Point target = edges.get(0).b ;
        System.out.println(edges.get(0).a) ;
        Point initialPoint = edges.get(0).a ;
        Edge currentEdge ;
        edges.remove(0) ;
        while(!edges.isEmpty())
        {
            for(int i = 0 ; i < edges.size() ; i++)
            {
                currentEdge = edges.get(i) ;
                if(currentEdge.a.equals(target))
                {
                    points.add(currentEdge.b) ;
                    target = currentEdge.b ;
                    edges.remove(i) ;
                    break ;
                }
            }
            if(target.equals(initialPoint))
            {
                break ;
            }
        }
        
        return points ;
    }
    
    //Returns a list of edges that are fully visible to the point.
    public Edge[] fullyVisibleEdges(ArrayList<Edge> hull)
    {
        int n = hull.size() ;
        ArrayList<Edge> edges = new ArrayList<>() ;
        Edge first , second ;
        for(int i = 0 ; i < n ; i++)
        {
            first = new Edge(this , hull.get(i).a) ;
            second = new Edge(this , hull.get(i).b) ;
            
            if(first.isVisible(hull , i) && second.isVisible(hull , i))
            {
                edges.add(hull.get(i)) ;
            }
        }
        
        Edge[] FVE = new Edge[edges.size()] ;
        for(int i = 0 ; i < edges.size() ; i++)
        {
            FVE[i] = edges.get(i) ;
        }
        return FVE ;
    }
        
    //Formats points (x,y)
    @Override
    public String toString()
    {
        return new String("(" + this.x + " , " + this.y + ")") ;
    }
    
    //Returns x1 == x2 && y1 == y2
    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Point)
        {
            Point point = (Point)obj ;
            return ((this.x == point.x) && (this.y == point.y)) ; 
        }
        else
        {
            return false ;
        }
    }
}
