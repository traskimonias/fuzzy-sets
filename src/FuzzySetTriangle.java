public class FuzzySetTriangle extends FuzzySet {
    //Constructor
    public FuzzySetTriangle(double min, double max, double baseStart, double top, double baseEnd){
        super(min, max);
        add(new Point2D(min, 0));
        add(new Point2D(baseStart, 0));
        add(new Point2D(top, 1));
        add(new Point2D(baseEnd, 0));
        add(new Point2D(max, 0));
    }
}