public class Point2D implements Comparable {
    //Coordinates
    public double x;
    public double y;
    //Constructor
    public Point2D(double _x,double _y){
        x=_x;
        y=_y;
    }
    @Override
    public int compareTo(Object t){
        return (int) (x-((Point2D) t).x);
    }
    @Override
    public String toString(){
        return "("+x+";"+y+")";
    }
}