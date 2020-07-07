import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.StringJoiner;

public class FuzzySet {
    protected ArrayList<Point2D> points;
    protected double min;
    protected double max;
    //Constructor
    public FuzzySet(double _min, double _max){
        points= new ArrayList<Point2D>();
        min= _min;
        max= _max;
    }
    //Adds a point
    public void add(Point2D point){
        points.add(point);
        Collections.sort(points);
    }
    public void add(double x, double y){
        Point2D pt= new Point2D(x, y);
        add(pt);
    }

    @Override
    public String toString(){
        StringJoiner sj= new StringJoiner(" ");
        sj.add("[" + min + "-" + max + "]:");
        for(Point2D pt :points){
            sj.add(pt.toString());
        }
        return sj.toString();
    }

    //OPERATORS

    //Comparer based on the string of each point
    @Override
    public boolean equals(Object pt2){
        return toString().equals(((Point2D)pt2).toString());
    }
    //Multiplier
    public FuzzySet multiplyBy(double value){
        FuzzySet set = new FuzzySet(min, max);
        for(Point2D pt : points){
            set.add(pt.x, pt.y*value);
        }
        return set;
    }
    //NOT negation
    public FuzzySet not(){
        FuzzySet set= new FuzzySet(min, max);
        for( Point2D pt: points){
            set.add(pt.x,1-pt.y);
        }
        return set;
    }
    //Calculate membership value of a point
    public double membershipValue(double value){
        //Case 1: outside the interval of the fuzzy set
        if(value<min || value>max || points.size()<2){
            return 0;
        }
        Point2D ptBefore= points.get(0);
        Point2D ptNext= points.get(1);
        int index=0;
        while(value> ptNext.x){
            index++;
            ptBefore=ptNext;
            ptNext= points.get(index);
        }
        //If there is a point with that value
        if(ptBefore.x==value){
            return ptBefore.y;
        }else{
            //Else aply interpolation
            return ((ptBefore.y-ptNext.y) * (ptNext.x-value)/(ptNext.x-ptBefore.x)+ ptNext.y);
        }
    }
    //Min or max method
    private static double optimum(double value1, double value2, String method){
        if(method.equals("Min")){
            return Math.min(value1, value2);
        }else{
            return Math.max(value1, value2);
        }
    }
    //Y operator
    public FuzzySet Y(FuzzySet s2){
        return fusion(this, s2, "Min");
    }
    //O operator
    public FuzzySet O(FuzzySet s2){
        return fusion(this, s2, "Max");
    }
    //Fusion
    private static FuzzySet fusion(FuzzySet s1, FuzzySet s2, String method){
        //Create result
        FuzzySet result = new FuzzySet(Math.min(s1.min, s2.min), Math.max(s1.max,s2.max));
        //Iterate through the list
        Iterator<Point2D> iterator1= s1.points.iterator();
        Point2D ptSet1= iterator1.next();
        Point2D oldPtSet1= ptSet1;
        Iterator<Point2D> iterator2= s2.points.iterator();
        Point2D ptSet2= iterator2.next();

        //Calculate relative position of both curves
        int oldRelativePosition;
        int newRelativePosition= (int) Math.signum(ptSet1.y-ptSet2.y);
        boolean list1Finished= false;
        boolean list2Finished= false;

        //Loop through all the points in both collections
        while(!list1Finished && !list2Finished){
            //Get current x
            double x1= ptSet1.x;
            double x2= ptSet2.x;
            //Calculate current relative position
            oldRelativePosition= newRelativePosition;
            newRelativePosition=(int) Math.signum(ptSet1.y-ptSet2.y);

            //Are the curves inverted? if not, how many points are into account
            if(oldRelativePosition!=newRelativePosition && oldRelativePosition!=0 && newRelativePosition!=0){
                //Calculate intersection point
                double x = (x1==x2 ? oldPtSet1.x : Math.min(x1, x2));
                double xPrime = Math.max(x1, x2);
                //Calculate incline
                double p1= s1.membershipValue(xPrime) - s1.membershipValue(x)/(xPrime-x);
                double p2= s2.membershipValue(xPrime) - s2.membershipValue(x)/(xPrime-x);
                //Calculate delta
                double delta=0;
                if((p2-p1) !=0){
                    delta = (s2.membershipValue(x)-s1.membershipValue(x))/(p1-p2);
                }
                //Add intersection point to the result
                result.add(x+delta,s1.membershipValue(x+delta));
                //Go to next point
                if(x1<x2){
                    oldPtSet1= ptSet1;
                    if(iterator1.hasNext()){
                        ptSet1= iterator1.next();
                    }else{
                        list1Finished=true;
                        ptSet1=null;
                    }
                }else if(x1>x2){
                    if(iterator2.hasNext()){
                        ptSet2=iterator2.next();
                    }else{
                        list2Finished=true;
                        ptSet2=null;
                    }
                }//Unsure about this else
            }else if(x1==x2){
                //2 points with the same x value. Save the best option
                result.add(x1,optimum(ptSet1.y, ptSet2.y, method));
                //To the next point
                if( iterator1.hasNext()){
                    oldPtSet1=ptSet1;
                    ptSet1=iterator1.next();
                }else{
                    ptSet1=null;
                    list1Finished=true;
                }
                if(iterator2.hasNext()){
                    ptSet2= iterator2.next();
                }else{
                    ptSet2= null;
                    list2Finished=true;
                }
            }else if(x1<x2){
                //Set 1 has a point before. Calculate membership of 2 and save the optimum
                result.add(x1,optimum(ptSet1.y, s2.membershipValue(x1), method));
                //To the next point
                if(iterator1.hasNext()){
                    oldPtSet1= ptSet1;
                    ptSet1=iterator1.next();
                }else{
                    ptSet1=null;
                    list1Finished=true;
                }
            }else {
                //Set 2 has a point before. Calculate membership of 1 and save the optimum
                result.add(x2,optimum(s1.membershipValue(x1), ptSet2.y, method));
                //To the next point
                if(iterator2.hasNext()){
                    ptSet2=iterator2.next();
                }else{
                    ptSet2=null;
                    list2Finished=true;
                }
            }
        }
        //At least one of the list has finished
        //Add the rest of points
        if(!list1Finished){
            while(iterator1.hasNext()){
                ptSet1= iterator1.next();
                result.add(ptSet1.x,optimum(ptSet1.y, 0, method));
            }
        }else if(!list2Finished){
            while(iterator2.hasNext()){
                ptSet2= iterator2.next();
                result.add(ptSet2.x,optimum(ptSet2.y, 0, method));
            }
        }
        return result;
    }
    public double baricenter(){
        //It needs at least 2 points to have a baricenter
        if(points.size()<=2){
            return 0;
        }else{
            //Initialize surfaces
            double weightedSurface= 0;
            double totalSurface= 0;
            double localSurface= 0;
            //Loop through the list conserving 2 points
            Point2D oldPoint=null;
            for(Point2D pt : points){
                if(oldPoint!=null){
                    //Calculate local baricenter
                    if(oldPoint.y==pt.y){
                        //We have a rectangle whose baricenter is in the center
                        localSurface=pt.y * (pt.x-oldPoint.x);
                        totalSurface+= localSurface;
                        weightedSurface+= localSurface * ((pt.x - oldPoint.x) / 2.0 + oldPoint.x);
                    }
                    else{
                        //We have a trapezoid that we need to descompose in a rectangle and a triangle
                        //First the rectangle
                        localSurface= Math.min(pt.y, oldPoint.y) *(pt.x-oldPoint.x);
                        totalSurface+= localSurface;
                        weightedSurface+= localSurface * ((pt.x-oldPoint.x)/2.0 +oldPoint.x);
                        //Second the triangle
                        localSurface=(pt.x-oldPoint.x) * Math.abs(pt.y-oldPoint.y)/2.0;
                        totalSurface+=localSurface;
                        if(pt.y>oldPoint.y){
                            //Baricenter is on the pt side
                            weightedSurface += localSurface *(2/3 * (pt.x-oldPoint.x) + oldPoint.x);
                        }else{
                            //Baricenter is on the oldPoint side
                            weightedSurface += localSurface *(1/3 * (pt.x-oldPoint.x) + oldPoint.x);
                        }
                    }
                }
                oldPoint=pt;
            }
            //Return baricenter coordinates
            return weightedSurface/totalSurface;
        }
    }
}
