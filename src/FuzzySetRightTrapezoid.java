public class FuzzySetRightTrapezoid extends FuzzySet {  
    //Constructor
    public FuzzySetRightTrapezoid(double min,double max,double lowPlaneEnd, double highPlaneStart){
        super(min, max);
        add(new Point2D(min, 0));
        add(new Point2D(lowPlaneEnd, 0));
        add(new Point2D(highPlaneStart, 1));
        add(new Point2D(max, 1));
    }
}