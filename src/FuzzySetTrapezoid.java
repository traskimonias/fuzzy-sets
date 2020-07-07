public class FuzzySetTrapezoid extends FuzzySet{
    //Constructor
    public FuzzySetTrapezoid(double min,double max,double baseStart,double plateauStart, double plateauEnd, double baseEnd){
        super(min, max);
        add(new Point2D(min, 0));
        add(new Point2D(baseStart, 0));
        add(new Point2D(plateauStart, 1));
        add(new Point2D(plateauEnd, 1));
        add(new Point2D(baseEnd, 0));
        add(new Point2D(max, 0));
    }
}