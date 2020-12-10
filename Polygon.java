import java.util.ArrayList ;

/**
 * @author Nathan Moder
 * A class for dealing with polygons in 2D space, also has methods for generating
 * them randomly.
 */
public class Polygon 
{
    private ArrayList<Point> vertices ;
    
    //A polygon is a list of points.
    Polygon(ArrayList<Point> vertices)
    {
        this.vertices = vertices ;
    }
    
    public ArrayList getVerticies()
    {
        return this.vertices ;
    }
    
    //generates a random simple polygon with n vertices.
    //Uses the GRP_CH heuristic.
    public static Polygon generatePolygon(int n , int scaleX , int scaleY)
    {
        Point[] points = Point.generatePoints(n , scaleX , scaleY) ;
        Point[] hull = Point.convexHull(points) ;
        ArrayList<Edge> hullEdges ;
        ArrayList<Point> pointList ;
        Edge e ;
        
        Point currentPoint ;
        Boolean polygonCreated ;
        int failCount = 0 ;
        do
        {
            polygonCreated = true ;
            
            //The first step is to remove all the points that are on the hull from our
            //list of points.
            pointList = new ArrayList<>() ;
            hullEdges = Edge.makeEdgeList(hull) ;
            for(int i = 0 ; i < points.length ; i++)
            {
                pointList.add(points[i]) ;
            }

            for(int i = 0 ; i < hull.length ; i++)
            {
                pointList.remove(hull[i]) ;
            }
            
            while(!pointList.isEmpty())
            {
                currentPoint = pointList.get((int)(Math.random() * pointList.size())) ;
                Edge[] FVE = currentPoint.fullyVisibleEdges(hullEdges) ;
                if(FVE.length == 0)
                {
                    //If there are no fully visible edges, a polygon is not possible.
                    System.out.println("Failed to find a fully visible edge. There are " + pointList.size() + " points not in the hull. " + failCount) ;
                    polygonCreated = false ;
                    failCount ++ ;
                    break ;
                }

                e = FVE[(int)(Math.random() * FVE.length)] ;
                hullEdges.remove(e) ;
                hullEdges.add(new Edge(e.a , currentPoint)) ;
                hullEdges.add(new Edge(currentPoint , e.b)) ;
                pointList.remove(currentPoint) ;
            }
            if(failCount > 10 * n)
            {
                return null ;
            }
        }while(!polygonCreated) ;
        return new Polygon(Point.makePointList(hullEdges)) ;
    }
    
    //Generates a random polygon using the "human" method.
    public static Polygon generatePolygonAlternate(int n , int scale)
    {
        double angle ;
        double length ;
        boolean intersects ;
        Point p ;
        
        Point[] points = new Point[n] ;
        points[0] = new Point(0 , 0) ;
        
        for(int i = 1 ; i < n ; i++)
        {
            //System.out.println("Inside For Loop");
            intersects = false ;
            angle = Math.random() * 360 ;

            length = Math.random() * scale ;
            p = new Point(points[i-1] , length , angle) ;
            do
            {
                //System.out.println("Inside Do While Loop");
                //System.out.println(Arrays.toString(points)) ;
                for(int j = 2 ; j < i ; j++)
                {
                    Point inter = (new Edge(p , points[i - 1])).intersection(new Edge(points[j - 2] , points[j - 1])) ;
                    //System.out.println(inter);
                    if(inter != null)
                    {
                        intersects = true ;
                        length = Math.random() * scale ;
                        angle = Math.random() * 360 ;
                        p = new Point(points[i - 1] , length , angle) ;
                        break ;
                    }
                    
                    intersects = false ;
                }
            }while(intersects) ;
            
            points[i] = p ;
        }
        
        //Now we need to see if the final edge will intersect anything
        for(int i = 3 ; i < points.length ; i ++)
        {
            Point inter = (new Edge(points[n - 1] , points[0])).intersection(new Edge(points[i - 2], points[i - 1])) ;
            if(inter != null)
            {
                return null ;
            }
        }
        
        ArrayList<Point> vertices = new ArrayList<>() ;
            
        for(int i = 0 ; i < points.length ; i++)
        {
            vertices.add(points[i]) ;
        }
        
        return new Polygon(vertices) ;
    }    
}
