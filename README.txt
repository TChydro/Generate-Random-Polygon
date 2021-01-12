https://github.com/TChydro/Generate-Random-Polygon
A compiled JAR file can be found here, along with the netbeans package if that is more useful.

https://link.springer.com/chapter/10.1007%2F978-3-642-35926-2_32
This project uses the GRP_CH heuristic for generating random polygons found here.

https://www.geogebra.org/classic
This project creates files that are compatable with geogebra in order to view the polygons easily.
For larger polygons ~500 edges you should download and use the desktop version (it's free) because I've
found that they tend to crash the browser when you get that large.

Generate Random Polygon

Simply run the file through the command line to produce geogebra compatable files for randomly generated polygons.
Upon running the user will be prompted with 7 options: Quick Generation, Greedy Generation, Approximate Generation,
Star Generation, Bulk Generation, Advanced Generation, and Quit.

Quick generation makes a polygon with a user defined number of vertices using the GRP_CH heuristic with the x and y scale 
equal to the number of vertices. Outputs one file with labels and one without.

Greedy and Approximate generation use modified versions of the GRP_CH heuristic and are intended to improve the speed
of polygon generation, at the loss of a small amount of randomness. Instead of searching for all possible edges
to add to the polygon and selecting one at random, these versions simply use first possible edge they find. Additionally,
approximate generation will only attempt to place each point at most twice, and remove any points that were not placed at the end.
This typically ends up reducing the size of the polygon by between 1 and 3 percent. Outputs one file with labels and one without.

Star generation generates simple star-shaped polygons. They are not particularly interesting, but the algorithm is incredibly fast
compared to the others. Outputs one file with labels and one without.

Bulk generation makes a user defined number of polygons with a user defined number of vertices using a selected algorithm listed above, 
with x and y scale equal to the number of vertices. Outputs files based on the user's selection.

Advanced generation allows the user to choose how many polygons to create, the method of generation, x and y scale, variation 
in the number of vertices (bulk only) and what kinds of files to output. In this section there is an option for "line" generation,
which generates polygons by drawing lines with random lengths at random angles starting from (0,0). It is not recommended for use, as
it is quite slow in most cases.

For using the code to make polygons and do other things with them, you can define them manually by making an array list of points
ordered such that there is an edge between the next and previous points, and the last point has an edge to the first. 
IE 1 -> 2 -> 3 -> 4 -> 1

There are five main methods to generate polygons randomly:
generatePolygon(int n , int scaleX , int scaleY)
generatePolygonGreedy(int n , int scaleX , int scaleY)
generatePolygonApproximate(int n , int scaleX , int scaleY)
generatePolygonStar(int n , int scaleX , int scaleY)
generatePolygonAlternate(int n , int scale)

where n is the number of vertices and scaleX and scaleY are the maximum x and y values of a point in the polygon. For alternate, scale is
the maximum distance between points.

The generatePolygon, generatePolygonGreedy, and generatePolygonAlternate methods can all fail to create polygons in certain situations 
caused by their use of random choices, so I suggest putting them in a do while loop if you need one generated.
