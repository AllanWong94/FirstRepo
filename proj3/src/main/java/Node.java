import java.util.HashMap;

/**
 * Created by Allan Wong on 2017/5/23.
 */
public class Node {
    private long id;
    private double lon;
    private double lat;
    private HashMap<String, String> attributes;
    public boolean marked;
    public Node prev;
    public double priority;

    public void setPrev(Node prev,double p) {
        this.prev = prev;
        priority=p;
    }

    public Node(long ID, double Lon, double Lat, HashMap<String, String> attb){
        id=ID;
        lon=Lon;
        lat=Lat;
        attributes=attb;
        marked=false;
        prev=null;
    }

    public boolean equals(Node n){
        return id==n.getId();
    }

    public long getId() {
        return id;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public HashMap<String, String> getAttributes() {
        return attributes;
    }


}
