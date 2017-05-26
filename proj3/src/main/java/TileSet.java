import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Allan Wong on 2017/5/21.
 */
public class TileSet {
    public static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625, Lv3WDDP=0.00004291534423828125;
    private Tile root;
    private ArrayList<Tile> tileCollection;
    private int tcWidth;
    private int tcHeight;
    private double rasteredImageWidth;
    private double rasteredImageHeight;
    private double rasteredImageUlLon;
    private double rasteredImageUlLat;
    private double rasteredImageLrLon;
    private double rasteredImageLrLat;
    private boolean tcInitialized;
    private double tcDepth;
    private double lonDPP;
    private double latDPP;
    private BufferedImage bi;
    private OutputStream os;
    private LinkedList<Node> nodes;

    public void setNodes(LinkedList<Node> Nodes) {
        nodes=Nodes;
        drawJoinedBufferedImage(os);
    }

    private static final int IMAGE_SIZE=256;

    //Input tile: should be the root
    public TileSet(){
        root=new Tile(ROOT_ULLON, ROOT_ULLAT, ROOT_LRLON, ROOT_LRLAT, 0, 0, 0);
        tcInitialized=false;
        tileCollection=new ArrayList<>();
        for (int i=0;i<4;i++){
            tileCollection.add(root.getChild(i));
        }
        OutputStream OS=new ByteArrayOutputStream();
        tcInitialized=true;
        tcWidth=2;
        tcHeight=2;
        rasteredImageUlLon=ROOT_ULLON;
        rasteredImageUlLat=ROOT_ULLAT;
        rasteredImageLrLon=ROOT_LRLON;
        rasteredImageLrLat=ROOT_LRLAT;
        drawJoinedBufferedImage(OS);
    }

    public ArrayList<Tile> findTiles(double ullonQ, double ullatQ, double lrlonQ, double lrlatQ, double width, double height){
        ullonQ=Math.max(ullonQ, ROOT_ULLON);
        ullatQ=Math.min(ullatQ, ROOT_ULLAT);
        lrlonQ=Math.min(lrlonQ, ROOT_LRLON);
        lrlatQ=Math.max(lrlatQ, ROOT_LRLAT);
        double lonDPPQ=(lrlonQ-ullonQ)/width;
        return findTiles(ullonQ,ullatQ,lrlonQ,lrlatQ,lonDPPQ);
    }

    //Returns an arraylist of tiles that will be used to construct the image shown to the user.
    public ArrayList<Tile> findTiles(double ullonQ, double ullatQ, double lrlonQ, double lrlatQ, double lonDPPQ){
        tileCollection.clear();
        Tile TopLeft=find(ullonQ,ullatQ,lonDPPQ,0);
        Tile TopRight=find(lrlonQ,ullatQ,lonDPPQ,1);
        Tile BottomLeft=find(ullonQ,lrlatQ,lonDPPQ,2);
        Tile BottomRight=find(lrlonQ,lrlatQ,lonDPPQ,3);
        rasteredImageUlLon=TopLeft.getUllon();
        rasteredImageUlLat=TopLeft.getUllat();
        rasteredImageLrLon=BottomRight.getLrlon();
        rasteredImageLrLat=BottomRight.getLrlat();
        Tile Beginning=TopLeft;
        Tile End=TopRight;
        Tile next;
        tcHeight=0;
        tcWidth=0;
        while(Beginning.getIndex()!=BottomLeft.Down()){
            tcHeight++;
            next=Beginning;
            while(next.getIndex()!=End.Right()){
                tileCollection.add(next);
                next=get(next.Right());
            }
            Beginning=get(Beginning.Down());
            End=get(End.Down());
        }
        tcInitialized=true;
        if(tileCollection.size()>0){
            tcWidth=tileCollection.size()/tcHeight;
            tcDepth=tileCollection.get(0).getDepth();
            lonDPP=tileCollection.get(0).getLonDPP();
            latDPP=tileCollection.get(0).getLatDPP();
        }else{
            for(int i=0;i<4;i++)
                tileCollection.add(root.getChild(i));
            tcDepth=1;
            lonDPP=tileCollection.get(0).getLonDPP();
            latDPP=tileCollection.get(0).getLatDPP();
            tcWidth=2;
            tcHeight=2;
        }
        return tileCollection;
    }

    //Find the suitable tile that is at the corner of the raster. Index indicates which corner,
    //i.e. upper left=>0, upper right=>1, lower left=>2, lower right=>3
    public Tile find(double ullonQ, double ullatQ, double lonDPPQ, int index){
        Tile t=root.getChild(childIndex(ullonQ,ullatQ,root.getCenterlon(),root.getCenterlat(),index));
        while(t.getLonDPP()>Math.min(lonDPPQ, Lv3WDDP)&&t.getDepth()<7){
            t=t.getChild(childIndex(ullonQ,ullatQ,t.getCenterlon(),t.getCenterlat(),index));
        }
        return t;
    }

    public Tile get(int index){
        LinkedList<Integer> l=new LinkedList<>();
        char[] c=Integer.toString(index).toCharArray();
        for (int i=c.length-1;i>=0;i--){
            int num=Integer.parseInt(String.valueOf(c[i]));
            l.addFirst(num-1);
        }
        Tile t=root;
        int size=l.size();
        for (int j=0;j<size;j++){
            t=t.getChild(l.remove());
        }
        return t;
    }

    //Given a center coordinate, return the index of the child tile that the location lies in.
    public int childIndex(double ullon, double ullat, double centerLon, double centerLat, int index){
        boolean UP;
        boolean LEFT;
        LEFT=ullon<centerLon;
        UP=ullat>centerLat;
        if(ullon==centerLon){
            if(index==1||index==3){
                LEFT=true;
            }
        }
        if(ullat==centerLat){
            if(index==2||index==3){
                UP=true;
            }
        }
        int res=0;
        if(!UP){
            res+=2;
        }
        if(!LEFT){
            res+=1;
        }
        return res;
    }

    public int getTcWidth() {
        return tcWidth;
    }

    public int getTcHeight() {
        return tcHeight;
    }

    public void drawJoinedBufferedImage(OutputStream os) {
        this.os=os;
        if (!tcInitialized){
            System.out.println("Error! TileCollection not initialized!");
            return;
        }
        rasteredImageWidth = IMAGE_SIZE*tcWidth;
        rasteredImageHeight = IMAGE_SIZE*tcHeight;
        //System.out.println(rasteredImageWidth);
        //System.out.println(rasteredImageHeight);
        int TopLeftX;
        int TopLeftY;
        BufferedImage img;
        String imageName;
        String inputImagePath = ".\\img\\";
        //create a new buffer and draw two image into the new image
        BufferedImage newImage = new BufferedImage((int)rasteredImageWidth,(int)rasteredImageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, (int)rasteredImageWidth, (int)rasteredImageHeight);
        //draw image
        g2.setColor(oldColor);
        try {
            for(int i=0;i<tcHeight;i++){
                for(int j=0;j<tcWidth;j++){
                    TopLeftX=j*IMAGE_SIZE;
                    TopLeftY=i*IMAGE_SIZE;
                    imageName=tileCollection.get(i*tcWidth+j).getFilename();

                    img = ImageIO.read(new File(inputImagePath+imageName));
                    g2.drawImage(img, null, TopLeftX,TopLeftY);

                }
            }
            if(nodes!=null){
                g2.setColor(Color.blue);
                g2.setStroke(new BasicStroke(MapServer.ROUTE_STROKE_WIDTH_PX,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int[] pos1,pos2;
                Long id1,id2;
                Node node1,node2;
                for (int i=0;i<nodes.size()-1;i++){
                    node1=nodes.get(i);
                    node2=nodes.get(i+1);
                    pos1=calPos(node1.getLon(),node1.getLat());
                    pos2=calPos(node2.getLon(),node2.getLat());
                    g2.drawLine(pos1[0],pos1[1],pos2[0],pos2[1]);
                }
            }

            g2.dispose();
            bi=newImage;
            ImageIO.write(newImage, "png", os);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public double getRasteredImageWidth() {
        if (!tcInitialized){
            return 0;
        }
        return rasteredImageWidth;
    }

    public double getRasteredImageHeight() {
        if (!tcInitialized){
            return 0;
        }
        return rasteredImageHeight;
    }

    public double getRasteredImageUlLon() {
        if (!tcInitialized){
            return 0;
        }
        return rasteredImageUlLon;
    }

    public double getRasteredImageUlLat() {
        if (!tcInitialized){
            return 0;
        }
        return rasteredImageUlLat;
    }

    public double getRasteredImageLrLon() {
        if (!tcInitialized){
            return 0;
        }
        return rasteredImageLrLon;
    }

    public double getRasteredImageLrLat() {
        if (!tcInitialized){
            return 0;
        }
        return rasteredImageLrLat;
    }

    public double getTcDepth() {
        return tcDepth;
    }

    public boolean isTcInitialized() {
        return tcInitialized;
    }

    public static void main(String[] args) {
        Tile t=new Tile(ROOT_ULLON, ROOT_ULLAT, ROOT_LRLON, ROOT_LRLAT, 0, 0, 0);
        TileSet ts=new TileSet();
        Tile temp;
        ArrayList<Tile> tiles=ts.findTiles(-122.27783203125,37.88352140802976,-122.23388671875,37.83147657274216,0.00004291534423828125);
        //ts.drawJoinedBufferedImage(tiles, ts.getTcWidth(), ts.getTcHeight());

    }

    public BufferedImage getBufferedImage() {
        return bi;
    }

    public int[] calPos(double lon,double lat){
        int[] pos=new int[2];
        pos[0]=new Double((lon-rasteredImageUlLon)/lonDPP).intValue();
        pos[1]=new Double((rasteredImageUlLat-lat)/latDPP).intValue();
        return pos;
    }

    public void writeImage(){
        try{
            ImageIO.write(bi, "png", os);
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
