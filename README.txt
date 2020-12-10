Generate Random Polygon

Simply run the file through the command line to produce geogebra compatable files for randomly generated polygons.
Upon running the user will be prompted with 4 options: Quick Generation, Bulk Generation, Advanced Generation, and Quit

Quick generation makes a polygon with a user defined number of vertices using the GRP_CH heuristic with the x and y scale 
equal to the number of vertices. Outputs one file with labels and one without.

Bulk generation makes a user defined number of polygons with a user defined number of vertices using the GRP_CH heuristic 
with x and y scale equal to the number of vertices. Outputs files based on the user's selection.

Advanced generation allows the user to choose how many polygons to create, the method of generation, x and y scale, variation 
in the number of vertices (bulk only) and what kinds of files to output.

For using the code to make polygons and do other things with them, you can define them manually by making an array list of points
ordered such that there is an edge between the next and previous points, and the last point has an edge to the first. 
IE 1 -> 2 -> 3 -> 4 -> 1

There are two methods to generate polygons randomly:
generatePolygon(int n , int scaleX , int scaleY)
generatePolygonAlternate(int n , int scale)

where n is the number of vertices and scaleX and scaleY are the maximum x and y values of a point in the polygon. For alternate, scale is
the maximum distance between points.

Both of the methods can fail to create polygons in certain situations caused by their use of random choices, so I suggest putting them in
a do while loop if you need one generated. The regular generatePolygon method can be adjusted to always return a polygon, but it may not have
the specified number of vertices.

For general use, don't use generatePolygonAlternate, it is much slower and can potentially loop infinitely.
