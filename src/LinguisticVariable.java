import java.util.ArrayList;

public class LinguisticVariable {
    protected String name;
    protected ArrayList<LinguisticValue> values;
    protected double minValue;
    protected double maxValue;
    //Constructor
    public LinguisticVariable(String _name,double _min, double _max){
        name=_name;
        minValue=_min;
        maxValue=_max;
        values= new ArrayList<LinguisticValue>();
    }
    //Adds new values
    public void addLinguisticValue(LinguisticValue lv){
        values.add(lv);
    }
    public void addLinguisticValue(String name, FuzzySet set){
        values.add(new LinguisticValue(name, set));
    }
    //Clears values
    public void clearValues(){
        values.clear();
    }
    LinguisticValue linguisticValueByName(String name){
        for(LinguisticValue lv: values){
            if(lv.name.equalsIgnoreCase(name)){
                return lv;
            }
        }
        return null;
    }
}