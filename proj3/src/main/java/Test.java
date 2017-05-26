
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
public class Test {
    //public static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
    //       ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;
    /*public static void main(String[] args) {
        //  Tile t=new Tile(ROOT_ULLON, ROOT_ULLAT, ROOT_LRLON, ROOT_LRLAT, 0, 0, 0);
         TileSet ts=new TileSet(t);

    }*/
    public static void main(String args[]) {
        String path="G:\\Workspace\\proj3\\berkeley.osm";
        GraphDB g=new GraphDB(path);
        Route route=new Route(g,g.maphandler);
        route.setStartEnd(-122.23958303651945, 37.85552621806486,-122.23952939233915,37.85347627500828);
      //  route.findCrossroads();
        route.route();
        route.displayRoute();
        System.out.println("done");
    }
}

