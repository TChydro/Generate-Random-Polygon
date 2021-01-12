import java.math.BigDecimal;
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
    
    /**
     * @param n The number of vertices/edges
     * @param scaleX The maximum x value of any vertex
     * @param scaleY The maximum y value of any vertex
     * @return A polygon with n vertices randomly generated using the GRP_CH heuristic.
     * 
     * Read about the algorithm here: https://www.researchgate.net/publication/266942388_GRP_CH_Heuristic_for_Generating_Random_Simple_Polygon
     */
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
    
    //***** Maybe try using the closest visible edge? *****//
    
    /**
     * @param n The number of vertices/edges
     * @param scale The maximum length of all but the final edge.
     * @return A polygon with n vertices generated using a nieve "human" algorithm
     * by drawing line segments with random length and angle in sequence.
     */
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

    /**
     * @param n The number vertices/edges
     * @param scaleX The maximum x value of a vertex
     * @param scaleY The maximum y value of a vertex
     * @return A polygon generated using a greedier version of the GRP_CH heuristic.
     * A slight amount of randomness is lost with this algorithm, but it should not be
     * noticeable. It runs faster overall.
     */
    public static Polygon generatePolygonGreedy(int n , int scaleX , int scaleY)
    {
        Point[] points = Point.generatePoints(n , scaleX , scaleY) ;
        Point[] hull = Point.convexHull(points) ;
        ArrayList<Edge> hullEdges ;
        ArrayList<Point> pointList ;
        Edge e ;
        
        Point currentPoint ;
        Boolean polygonCreated ;
        int failCount = 0 ;
        int notInHull = 0 ;
            
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
            Edge FVE = currentPoint.firstVisibleEdge(hullEdges) ;

            if(FVE != null)
            {
                hullEdges.remove(FVE) ;
                hullEdges.add(new Edge(FVE.a , currentPoint)) ;
                hullEdges.add(new Edge(currentPoint , FVE.b)) ;
                pointList.remove(currentPoint) ;
            }
            else
            {
                if(notInHull == pointList.size())
                {
                    failCount ++ ;
                }
                else
                {
                    failCount = 0 ;
                }
                notInHull = pointList.size() ;
                System.out.println("Failed to find a fully visible edge. There are " + notInHull + " points not in the hull. " + failCount) ;
            }
            if(failCount > 2 * pointList.size())
            {
                return null ;
            }
        }
        return new Polygon(Point.makePointList(hullEdges)) ;
    }

    /**
     * @param n The maximum number of vertices/edges in the polygon.
     * @param scaleX The maximum x value of a vertex.
     * @param scaleY The maximum y value of a vertex.
     * @return A polygon with roughly n vertices generated using the greedy version
     * of the GRP_CH heuristic. Typically the polygons produced by this algorithm have
     * 1% to 3% fewer points than the input.
     */
    public static Polygon generatePolygonApproximate(int n , int scaleX , int scaleY)
    {
        Point[] points = Point.generatePoints(n , scaleX , scaleY) ;
        Point[] hull = Point.convexHull(points) ;
        ArrayList<Edge> hullEdges ;
        ArrayList<Point> pointList ;
        Edge e ;
        
        Point currentPoint ;
        Boolean polygonCreated ;
        int failCount = 0 ;
        int notInHull = 0 ;
            
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

        boolean[] inHull = new boolean[pointList.size()] ;
        for(int i = 0 ; i < pointList.size() ; i++)
        {
            currentPoint = pointList.get(i) ;
            Edge FVE = currentPoint.firstVisibleEdge(hullEdges) ;

            if(FVE != null)
            {
                hullEdges.remove(FVE) ;
                hullEdges.add(new Edge(FVE.a , currentPoint)) ;
                hullEdges.add(new Edge(currentPoint , FVE.b)) ;
                inHull[i] = true ;
            }
            else
            {
                System.out.println("Failed to find a fully visible edge. This point may be removed " + currentPoint) ;
                inHull[i] = false ;
                failCount ++ ;
            }
        }
        
        for(int i = 0 ; i < inHull.length ; i++)
        {
            if(!inHull[i])
            {
                currentPoint = pointList.get(i) ;
                Edge FVE = currentPoint.firstVisibleEdge(hullEdges) ;
                if(FVE != null)
                {
                    hullEdges.remove(FVE) ;
                    hullEdges.add(new Edge(FVE.a , currentPoint)) ;
                    hullEdges.add(new Edge(currentPoint , FVE.b)) ;
                    failCount -- ;
                }
            }
        }
        System.out.println(failCount + " Points were not placed. The polygon has " + (n - failCount) + " vertices." ) ;
        return new Polygon(Point.makePointList(hullEdges)) ;
    }
    
    /**
     * @param n The number of vertices/edges.
     * @param scaleX The maximum x value of a vertex.
     * @param scaleY The maximum y value of a vertex.
     * @return A star-shaped polygon with n vertices.
     */
    public static Polygon generatePolygonStar(int n , int scaleX , int scaleY)
    {
        double scale = (scaleX + scaleY) / 2 ;
        BigDecimal totalAngle = new BigDecimal("360") ;
        BigDecimal polygonSize = new BigDecimal(n) ;
        BigDecimal angle = totalAngle.divide(polygonSize) ;
        System.out.println(angle.doubleValue()) ;
        ArrayList<Point> pointList = new ArrayList<>() ;
        Point currentPoint ;
        Point origin = new Point(0 , 0) ;
        for(int i = 0 ; i < n ; i++)
        {
            currentPoint = new Point(origin , scale * Math.random() , Math.toRadians(angle.doubleValue() * i)) ;
            pointList.add(currentPoint) ;
            if(i % (n / 10) == 0)
            {
                System.out.println("Working...") ;
            }
        }
        return new Polygon(pointList) ;
    }
}
