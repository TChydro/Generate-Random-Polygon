function ggbOnInit() 
{
    console.log("Is Running?") ;
    console.log(ggbApplet.getAllObjectNames) ;
    var objectNames = ggbApplet.getAllObjectNames() ;
    console.log(objectNames) ;
    for(var i = 0 ; i < objectNames.length ; i++)
    {
        ggbApplet.setLabelVisible(objectNames[i] , false) ;
    }
}