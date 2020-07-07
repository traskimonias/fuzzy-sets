import java.util.ArrayList;

/**
 * Class that controls the fuzzy system
 */
public class FuzzyController {
    protected String name;
    protected ArrayList<LinguisticVariable> inputs;
    protected LinguisticVariable output;
    protected ArrayList<FuzzyRule> rules;
    protected ArrayList<NumericValue> problem;
    //Contructor
    public FuzzyController(String _name){
        name= _name;
        inputs=new ArrayList<>();
        rules=new ArrayList<>();
        problem=new ArrayList<>();
    }
    //Adds a linguistic variable as an input
    public void addInputVariable(LinguisticVariable lv){
        inputs.add(lv);
    }
    //Adds a linguistic variable as an output, there can only be one
    public void addOutputVariable(LinguisticVariable lv){
        output=lv;
    }
    //Adds a new rule from a Fuzzy rule
    public void addRule(FuzzyRule rule){
        rules.add(rule);
    }
    //Adds a new rule from a text
    public void addRule(String ruleStr){
        FuzzyRule rule= new FuzzyRule(ruleStr, this);
        rules.add(rule);
    }
    //Adds a new numeric value as a problem
    public void addNumericValue(LinguisticVariable lv,double value){
        problem.add(new NumericValue(lv, value));
    }
    //Clears the problem so it can move to the next case
    public void clearNumericValues(){
        problem.clear();
    }
    //Finds a linguistic variable by its name
    public LinguisticVariable linguisticVariableByName(String _name){
        for(LinguisticVariable var : inputs){
            if(var.name.equalsIgnoreCase(_name)){
                return var;
            }
        }
        if(output.name.equalsIgnoreCase(_name)){
            return output;
        }
        return null;
    }
    public double resolve(){
        //Initialize the resulting fuzzy set
        FuzzySet result= new FuzzySet(output.minValue, output.maxValue);
        result.add(output.minValue ,0);
        result.add(output.maxValue, 0);
        //Aply the rules and modify the the resulting fuzzy set
        for(FuzzyRule rule: rules){
            result=result.O(rule.aply(problem));
        }
        //Defuzzification
        return result.baricenter();
    }
}