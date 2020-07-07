public class FuzzySetLeftTrapezoid extends FuzzySet{
    //Constructor
    public FuzzySetLeftTrapezoid(double min,double max,double highPlaneEnd,double lowPlaneStart){
        super(min, max);
        add(new Point2D(min, 1));
        add(new Point2D(highPlaneEnd, 1));
        add(new Point2D(lowPlaneStart, 0));
        add(new Point2D(max, 0));
    }
}