import java.io.File ;
import java.io.FileOutputStream ;
import java.io.FileWriter ;
import java.io.IOException ;
import java.nio.file.Files ;
import java.text.SimpleDateFormat ;
import java.util.ArrayList ;
import java.util.Date ;
import java.util.zip.ZipEntry ;
import java.util.zip.ZipOutputStream ;

/**
 * @author Nathan Moder
 * A class for converting a polygon into a geogebra compatable file. Should be in
 * the polygon class, but the code is incredibly messy, so I left it as its own
 * thing.
 */

public class OutputFile 
{
    public static void polygonToGBBFile(Polygon polygon , Boolean showLabels , int count)
    {
        //Header content for the xml file. It's the same every time.
        String contentHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                                "<geogebra format=\"5.0\" version=\"5.0.613.0\" app=\"classic\" platform=\"w\" id=\"3985EFA7-A3CE-4224-97C0-BE2EB71D1FF3\"  xsi:noNamespaceSchemaLocation=\"http://www.geogebra.org/apps/xsd/ggb.xsd\" xmlns=\"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" >\n" +
                                "<gui>\n" +
                                "	<window width=\"2190\" height=\"937\" />\n" +
                                "	<perspectives>\n" +
                                "<perspective id=\"tmp\">\n" +
                                "	<panes>\n" +
                                "		<pane location=\"\" divider=\"0.1735159817351598\" orientation=\"1\" />\n" +
                                "	</panes>\n" +
                                "	<views>\n" +
                                "		<view id=\"4097\" visible=\"false\" inframe=\"false\" stylebar=\"true\" location=\"1,1,1,1\" size=\"400\" window=\"100,100,700,550\" />\n" +
                                "		<view id=\"512\" toolbar=\"0 | 1 501 5 19 , 67 | 2 15 45 18 , 7 37 | 514 3 9 , 13 44 , 47 | 16 51 | 551 550 11 ,  20 22 21 23 , 55 56 57 , 12 | 69 | 510 511 , 512 513 | 533 531 , 534 532 , 522 523 , 537 536 , 535 , 538 | 521 520 | 36 , 38 49 560 | 571 30 29 570 31 33 | 17 | 540 40 41 42 , 27 28 35 , 6 , 502\" visible=\"false\" inframe=\"false\" stylebar=\"false\" location=\"1,1,1\" size=\"500\" window=\"100,100,600,400\" />\n" +
                                "		<view id=\"4\" toolbar=\"0 || 2020 , 2021 , 2022 || 2001 , 2003 , 2002 , 2004 , 2005 || 2040 , 2041 , 2042 , 2044 , 2043\" visible=\"false\" inframe=\"false\" stylebar=\"false\" location=\"1,1\" size=\"300\" window=\"100,100,600,400\" />\n" +
                                "		<view id=\"8\" toolbar=\"1001 | 1002 | 1003  || 1005 | 1004 || 1006 | 1007 | 1010 || 1008 | 1009 || 6\" visible=\"false\" inframe=\"false\" stylebar=\"false\" location=\"1,3\" size=\"300\" window=\"100,100,600,400\" />\n" +
                                "		<view id=\"1\" visible=\"true\" inframe=\"false\" stylebar=\"false\" location=\"1\" size=\"1262\" window=\"100,100,600,400\" />\n" +
                                "		<view id=\"2\" visible=\"true\" inframe=\"false\" stylebar=\"false\" location=\"3\" size=\"380\" tab=\"ALGEBRA\" window=\"100,100,600,400\" />\n" +
                                "		<view id=\"16\" visible=\"false\" inframe=\"false\" stylebar=\"false\" location=\"1\" size=\"300\" window=\"50,50,500,500\" />\n" +
                                "		<view id=\"32\" visible=\"false\" inframe=\"false\" stylebar=\"true\" location=\"1\" size=\"300\" window=\"50,50,500,500\" />\n" +
                                "		<view id=\"64\" toolbar=\"0\" visible=\"false\" inframe=\"false\" stylebar=\"false\" location=\"1\" size=\"480\" window=\"50,50,500,500\" />\n" +
                                "		<view id=\"128\" visible=\"false\" inframe=\"false\" stylebar=\"false\" location=\"1\" size=\"480\" window=\"50,50,500,500\" />\n" +
                                "		<view id=\"70\" toolbar=\"0 || 2020 || 2021 || 2022\" visible=\"false\" inframe=\"false\" stylebar=\"true\" location=\"1\" size=\"900\" window=\"50,50,500,500\" />\n" +
                                "	</views>\n" +
                                "	<toolbar show=\"true\" items=\"0 73 62 | 1 501 67 , 5 19 , 72 75 76 | 2 15 45 , 18 65 , 7 37 | 4 3 8 9 , 13 44 , 58 , 47 | 16 51 64 , 70 | 10 34 53 11 , 24  20 22 , 21 23 | 55 56 57 , 12 | 36 46 , 38 49  50 , 71  14  68 | 30 29 54 32 31 33 | 25 17 26 60 52 61 | 40 41 42 , 27 28 35 , 6\" position=\"1\" help=\"false\" />\n" +
                                "	<input show=\"true\" cmd=\"true\" top=\"algebra\" />\n" +
                                "	<dockBar show=\"false\" east=\"false\" />\n" +
                                "</perspective>\n" +
                                "	</perspectives>\n" +
                                "	<labelingStyle  val=\"0\"/>\n" +
                                "	<font  size=\"16\"/>\n" +
                                "</gui>\n" +
                                "<euclidianView>\n" +
                                "	<viewNumber viewNo=\"1\"/>\n" +
                                "	<size  width=\"1532\" height=\"884\"/>\n" +
                                "	<coordSystem xZero=\"765.9999999999998\" yZero=\"328\" scale=\"49.999999999999986\" yscale=\"49.99999999999999\"/>\n" +
                                "	<evSettings axes=\"true\" grid=\"true\" gridIsBold=\"false\" pointCapturing=\"3\" rightAngleStyle=\"1\" checkboxSize=\"26\" gridType=\"3\"/>\n" +
                                "	<bgColor r=\"255\" g=\"255\" b=\"255\"/>\n" +
                                "	<axesColor r=\"0\" g=\"0\" b=\"0\"/>\n" +
                                "	<gridColor r=\"192\" g=\"192\" b=\"192\"/>\n" +
                                "	<lineStyle axes=\"1\" grid=\"0\"/>\n" +
                                "	<axis id=\"0\" show=\"true\" label=\"\" unitLabel=\"\" tickStyle=\"1\" showNumbers=\"true\"/>\n" +
                                "	<axis id=\"1\" show=\"true\" label=\"\" unitLabel=\"\" tickStyle=\"1\" showNumbers=\"true\"/>\n" +
                                "</euclidianView>\n" +
                                "<algebraView>\n" +
                                "	<mode val=\"3\"/>\n" +
                                "</algebraView>\n" +
                                "<kernel>\n" +
                                "	<continuous val=\"false\"/>\n" +
                                "	<usePathAndRegionParameters val=\"true\"/>\n" +
                                "	<decimals val=\"2\"/>\n" +
                                "	<angleUnit val=\"degree\"/>\n" +
                                "	<algebraStyle val=\"3\" spreadsheet=\"0\"/>\n" +
                                "	<coordStyle val=\"0\"/>\n" +
                                "</kernel>\n" +
                                "<tableview min=\"-2\" max=\"2\" step=\"1\"/>\n" +
                                "<scripting blocked=\"false\" disabled=\"false\"/>\n" ;
        String constructionStart = "<construction title = \"test\" author=\"sans\" date =\"\">\n" ;
        
        String elements = new String() ;
        String content ;
        Point currentPoint ;
        
        ArrayList<Point> points = polygon.getVerticies() ;
        
        //Adding all the points to the file.
        for(int i = 0 ; i < points.size() ; i++)
        {
            currentPoint = points.get(i) ;
            content = "<element type=\"point\" label=\"p" + i +"\">\n" +
                        "	<show object=\"true\" label=\"" + showLabels + "\"/>\n" +
                        "	<objColor r=\"77\" g=\"77\" b=\"255\" alpha=\"0\"/>\n" +
                        "	<layer val=\"0\"/>\n" +
                        "	<labelMode val=\"0\"/>\n" +
                        "	<animation step=\"0.1\" speed=\"1\" type=\"1\" playing=\"false\"/>\n" +
                        "	<coords x=\"" + currentPoint.x + "\" y=\"" + currentPoint.y + "\" z=\"1\"/>\n" +
                        "	<pointSize val=\"2\"/>\n" +
                        "	<pointStyle val=\"0\"/>\n" +
                        "</element>\n" ;
            elements = elements.concat(content) ;
        }
    
        //Now we need to put all the edges together into a polygon.
        content = "<command name=\"Polygon\">\n" +
                    "	<input " ;
        //first add the input points.
        elements = elements.concat(content) ;
        
        for(int i = 0 ; i < points.size() ; i++)
        {
            content = "a" + i + "=\"p" + i + "\" " ;
            elements = elements.concat(content) ;
        }
        elements = elements.concat("/>\n    <output a0 = \"poly1\" ") ;
        elements = elements.concat("a1 = \"e" + (points.size() - 1) + "\" ") ;
        
        //Then add the edges in the output
        for(int i = 0 ; i < points.size() - 1 ; i++)
        {
            content = "a" + (i + 2) + "=\"e" + i + "\" " ;
            elements = elements.concat(content) ;
        }
        elements = elements.concat("/> \n </command>\n") ;
        
        
        String constructionEnd = "</construction>\n" + "</geogebra>" ;
        
        //This is where everything gets written to the correct files and compiled
        //into a .ggb file for use with geogebra.
        try
        {
            File polygonData = new File("geogebra.xml") ;
            FileWriter writer = new FileWriter(polygonData) ;
            writer.write(contentHeader + constructionStart + elements + constructionEnd) ;
            writer.close() ;
            
            String fileName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) ;
            fileName = fileName.concat("_" + count) ;
            fileName = fileName.concat("_" + showLabels) ;
            
            
            File f = new File(fileName + ".ggb") ;
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f)) ;
            ZipEntry e = new ZipEntry("geogebra.xml") ;
            out.putNextEntry(e) ;
            
            byte[] data = Files.readAllBytes(polygonData.toPath()) ;
            out.write(data , 0 , data.length) ;
            out.closeEntry();
            
            File defaults2d = new File("geogebra_defaults2d.xml") ;
            e = new ZipEntry("geogebra_defaults2d.xml") ;
            out.putNextEntry(e) ;
            
            data = Files.readAllBytes(defaults2d.toPath()) ;
            out.write(data , 0 , data.length) ;
            out.closeEntry();
            
            File defaults3d = new File("geogebra_defaults3d.xml") ;
            e = new ZipEntry("geogebra_defaults3d.xml") ;
            out.putNextEntry(e) ;
            
            data = Files.readAllBytes(defaults3d.toPath()) ;
            out.write(data , 0 , data.length) ;
            out.closeEntry();
            
            /*
            When you make an object with XML using "command," it automatically
            generates the code similar to how we make the points, and the label
            visibility is set to true. They do provide some javascript integration
            that lets us disable them, so everything should work out in the end.
            */
            File geogebraJS ;
            if(showLabels)
            {
                geogebraJS = new File("showLabelsTrue\\geogebra_javascript.js") ;
            }
            else
            {
                geogebraJS = new File("showLabelsFalse\\geogebra_javascript.js") ;
            }
            e = new ZipEntry("geogebra_javascript.js") ;
            out.putNextEntry(e) ;
            
            data = Files.readAllBytes(geogebraJS.toPath()) ;
            out.write(data , 0 , data.length) ;
            out.closeEntry();
            
            File geogebraTN = new File("geogebra_thumbnail.png") ;
            e = new ZipEntry("geogebra_thumbnail.png") ;
            out.putNextEntry(e) ;
            
            data = Files.readAllBytes(geogebraTN.toPath()) ;
            out.write(data , 0 , data.length) ;
            out.closeEntry();
            
            out.close() ;
        }//end Try
        catch(IOException ioe)
        {
            System.out.println("Something went wrong. " + ioe) ;
        }
    }//End method
}//End class
