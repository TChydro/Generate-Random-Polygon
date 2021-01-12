import java.util.Scanner ;
/**
 * @author Nathan Moder
 * The Driver program, generates random simple polygons and outputs them to .ggb files
 * for use with geogebra.
 */

public class generateRandomPolygon
{    
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in) ;
        int choice ;
        int amount = 2;
        char choiceChar ;
        
        //Variables for the advanced generation options.
        boolean bulk = false ;
        int verticies = 3 ;
        int variance = 0 ;
        double var ;
        int scaleX = 1 ;
        int scaleY = 1 ;
        int algorithmChoice ;
        
        Polygon randomPolygon ;
        
        System.out.println("*** Random Polygon Generation ***") ;
        System.out.println("(1) Quick Generation (2) Greedy Generation (3) Approximate Generation (4) Star Generation (5) Bulk Generation (6) Advanced Generation (0) Quit") ;
        choice = scanner.nextInt() ;
        
        while(choice < 0 || choice > 6)
        {
            System.out.println("Invalid option.") ;
            System.out.println("(1) Quick Generation (2) Greedy Generation (3) Approximate Generation (4) Star Generation (5) Bulk Generation (6) Advanced Generation (0) Quit") ;
            choice = scanner.nextInt() ;
        }
        switch(choice)
        {
            //Quick generation makes a polygon with a user defined number of vertices 
            //using the GRP_CH heuristic with the x and y scale equal to the number of 
            //vertices. Outputs one file with labels and one without.
            case 1 :
                System.out.println("How many vertices?") ;
                choice = scanner.nextInt() ;
                choice = Math.max(3 , choice) ;
                
                do
                {
                    randomPolygon = Polygon.generatePolygon(choice , choice , choice) ;
                }while(randomPolygon == null) ;

                OutputFile.polygonToGBBFile(randomPolygon , true , 1);
                OutputFile.polygonToGBBFile(randomPolygon , false , 1);
                
                System.out.println("Finished") ;
                break ;
            //Greedy generation uses a modified version of the GRP_CH heuristic to
            //generate a polygon with a user defined number of vertices. It should run
            //faster, but some amount of randomness is lost.
            case 2 :
                System.out.println("How many vertices?") ;
                choice = scanner.nextInt() ;
                choice = Math.max(3 , choice) ;
                
                do
                {
                    randomPolygon = Polygon.generatePolygonGreedy(choice , choice , choice) ;
                }while(randomPolygon == null) ;

                OutputFile.polygonToGBBFile(randomPolygon , true , 1);
                OutputFile.polygonToGBBFile(randomPolygon , false , 1);
                
                System.out.println("Finished") ;
                break ;
            //Approximate generation uses a modified version of the GRP_CH heuristic to
            //generate a polygon with a number of vertices close to the number defined
            //by the user. Typically the final polygon has between 1% and 3% fewer
            //vertices. However, it does run much faster than the previous two methods.
            case 3 :
                System.out.println("How many vertices?") ;
                choice = scanner.nextInt() ;
                choice = Math.max(3 , choice) ;
                
                randomPolygon = Polygon.generatePolygonApproximate(choice , choice , choice) ;

                OutputFile.polygonToGBBFile(randomPolygon , true , 1);
                OutputFile.polygonToGBBFile(randomPolygon , false , 1);
                
                System.out.println("Finished") ;
                break ;
            //Star generation generates star-shaped polygons with a user defined number of vertices. 
            //They are not very interesting, but the algorithm is extremely fast and 
            //capable of handling very large polygons easily.
            case 4 :
                System.out.println("How many vertices?") ;
                choice = scanner.nextInt() ;
                choice = Math.max(3 , choice) ;
                
                randomPolygon = Polygon.generatePolygonStar(choice , choice , choice) ;

                OutputFile.polygonToGBBFile(randomPolygon , true , 1);
                OutputFile.polygonToGBBFile(randomPolygon , false , 1);
                
                System.out.println("Finished") ;
                break ;      
            //Bulk generation makes a user defined number of polygons using a user defined
            //algorithm with x and y scale equal to the number of vertices. Outputs
            //files based on the user's selection.
            case 5 : 
                System.out.println("How many to generate?") ;
                choice = scanner.nextInt() ;
                amount = Math.max(2 , choice) ;
                
                System.out.println("How many verticies?") ;
                choice = scanner.nextInt() ;
                choice = Math.max(3 , choice) ;
                
                System.out.println("Display Labels? (y) Yes (n) No (b) Both") ;
                choiceChar = scanner.next().charAt(0) ;
                
                System.out.println("Which algorithm? (1) Standard (2) Greedy (3) Approximate (4) Star (5) Line") ;
                algorithmChoice = scanner.nextInt() ;
                if(algorithmChoice < 1 || algorithmChoice > 5)
                {
                    algorithmChoice = 1 ;
                }
                
                for(int i = 1 ; i <= amount ; i++)
                {
                    do
                    {
                        switch(algorithmChoice)
                        {
                            case 1:
                                randomPolygon = Polygon.generatePolygon(choice , choice , choice) ;
                                break ;
                            case 2:
                                randomPolygon = Polygon.generatePolygonGreedy(choice , choice , choice) ;
                                break ;
                            case 3:
                                randomPolygon = Polygon.generatePolygonApproximate(choice , choice , choice) ;
                                break ;
                            case 4:
                                randomPolygon = Polygon.generatePolygonStar(choice , choice , choice) ;
                                break ;
                            case 5 :
                                randomPolygon = Polygon.generatePolygonAlternate(choice , choice) ;
                                break ;
                            default :
                                System.out.println("Something may have gone wrong picking an algorithm. Using the default") ;
                                randomPolygon = Polygon.generatePolygon(choice , choice , choice) ;
                                break ;
                        }  
                    }while(randomPolygon == null) ;

                    switch (choiceChar)
                    {
                        case 'y' :
                            OutputFile.polygonToGBBFile(randomPolygon , true , i);
                            break ;
                        case 'n' :
                            OutputFile.polygonToGBBFile(randomPolygon , false , i);
                            break ;
                        default :
                            OutputFile.polygonToGBBFile(randomPolygon , true , i);
                            OutputFile.polygonToGBBFile(randomPolygon , false , i);
                            break ;
                    }
                }
                System.out.println("Finished") ;
                break ;
            //Advanced generation allows the user to choose how many polygons to create,
            //the method of generation, x and y scale, variation in the number of vertices
            //(bulk only) and what kinds of files to output.
            case 6 :
                System.out.println("Bulk Generate? (y)/(n)") ;
                choiceChar = scanner.next().charAt(0) ;
                switch (choiceChar)
                {
                    case 'y' :
                        bulk = true ;
                        break ;
                    default :
                        bulk = false ;
                        break ;
                }
                
                System.out.println("How many verticies?") ;
                choice = scanner.nextInt() ;
                verticies = Math.max(3 , choice) ;
                if(bulk)
                {
                    System.out.println("How many to generate?") ;
                    choice = scanner.nextInt() ;
                    amount = Math.max(2 , choice) ;
                    System.out.println("How much variance in the number of verticies?") ;
                    choice = scanner.nextInt() ;
                    variance = Math.max(0 , choice) ;
                }
                
                System.out.println("X scale?") ;
                choice = scanner.nextInt() ;
                scaleX = Math.max(1 , choice) ;
                System.out.println("Y scale?") ;
                choice = scanner.nextInt() ;
                scaleY = Math.max(1 , choice) ;
                
                System.out.println("Which algorithm? (1) Standard (2) Greedy (3) Approximate (4) Star (5) Line") ;
                algorithmChoice = scanner.nextInt() ;
                if(algorithmChoice < 1 || algorithmChoice > 5)
                {
                    algorithmChoice = 1 ;
                }
                
                System.out.println("Display labels? (y) Yes (n) No (b) Both") ;
                choiceChar = scanner.next().charAt(0) ;
                
                if(bulk)
                {
                    for(int i = 1 ; i <= amount ; i++)
                    {
                        var = Math.random() ;
                        if(var >= .5)
                        {
                            var = Math.random() * variance ;
                        }
                        else
                        {
                            var = Math.random() * variance * -1 ;
                        }
                        
                        do
                        {
                            switch(algorithmChoice)
                            {
                                case 1:
                                    randomPolygon = Polygon.generatePolygon(Math.max((int)(verticies + var) , 3) , scaleX , scaleY) ;
                                    break ;
                                case 2:
                                    randomPolygon = Polygon.generatePolygonGreedy(Math.max((int)(verticies + var) , 3) , scaleX , scaleY) ;
                                    break ;
                                case 3:
                                    randomPolygon = Polygon.generatePolygonApproximate(Math.max((int)(verticies + var) , 3) , scaleX , scaleY) ;
                                    break ;
                                case 4:
                                    randomPolygon = Polygon.generatePolygonStar(Math.max((int)(verticies + var) , 3) , scaleX , scaleY) ;
                                    break ;
                                case 5 :
                                    randomPolygon = Polygon.generatePolygonAlternate(Math.max((int)(verticies + var) , 3) , Math.max(scaleX , scaleY)) ;
                                    break ;
                                default :
                                    System.out.println("Something may have gone wrong picking an algorithm. Using the default") ;
                                    randomPolygon = Polygon.generatePolygon(Math.max((int)(verticies + var) , 3) , scaleX , scaleY) ;
                                    break ;
                            }  
                        }while(randomPolygon == null) ;

                        switch (choiceChar)
                            {
                                case 'y' :
                                    OutputFile.polygonToGBBFile(randomPolygon , true , i);
                                    break ;
                                case 'n' :
                                    OutputFile.polygonToGBBFile(randomPolygon , false , i);
                                    break ;
                                default :
                                    OutputFile.polygonToGBBFile(randomPolygon , true , i);
                                    OutputFile.polygonToGBBFile(randomPolygon , false , i);
                                    break ;
                            }
                    }//end for
                }//end if
                else
                {
                    do
                    {
                        switch(algorithmChoice)
                        {
                            case 1:
                                randomPolygon = Polygon.generatePolygon(verticies , scaleX , scaleY) ;
                                break ;
                            case 2:
                                randomPolygon = Polygon.generatePolygonGreedy(verticies , scaleX , scaleY) ;
                                break ;
                            case 3:
                                randomPolygon = Polygon.generatePolygonApproximate(verticies , scaleX , scaleY) ;
                                break ;
                            case 4:
                                randomPolygon = Polygon.generatePolygonStar(verticies , scaleX , scaleY) ;
                                break ;
                            case 5 :
                                randomPolygon = Polygon.generatePolygonAlternate(verticies , Math.max(scaleX , scaleY)) ;
                                break ;
                            default :
                                System.out.println("Something may have gone wrong picking an algorithm. Using the default") ;
                                randomPolygon = Polygon.generatePolygon(verticies , scaleX , scaleY) ;
                                break ;
                        }  
                    }while(randomPolygon == null) ;
                    
                    switch (choiceChar)
                    {
                        case 'y' :
                            OutputFile.polygonToGBBFile(randomPolygon , true , 1);
                            break ;
                        case 'n' :
                            OutputFile.polygonToGBBFile(randomPolygon , false , 1);
                            break ;
                        default :
                            OutputFile.polygonToGBBFile(randomPolygon , true , 1);
                            OutputFile.polygonToGBBFile(randomPolygon , false , 1);
                            break ;
                    }
                }
                System.out.println("Finished") ;
                break ;
            default :
                break ;
        }//end switch
    }//end main
}
