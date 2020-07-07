import java.util.ArrayList;

/** 
 * Class representing a Fuzzy rule with various with various premises and a conclusion
 */
public class FuzzyRule {
    protected ArrayList<FuzzyExpression> premises;
    protected FuzzyExpression conclusion;
    //Constructor
    public FuzzyRule(ArrayList<FuzzyExpression> _premises, FuzzyExpression _conclusion){
        premises=_premises;
        conclusion=_conclusion;
    }
    //Constructor from a string
    public FuzzyRule(String rules, FuzzyController controller){
        rules=rules.toUpperCase();
        String[] rule= rules.split("THEN");
        if(rule.length==2){
            rule[0]=rule[0].replaceFirst("IF ", "").trim();
            String[] premisesStr = rule[0].split(" AND ");
            premises= new ArrayList<>();
            for( String exp : premisesStr){
                String[] parts= exp.trim().split(" IS ");
                if(parts.length==2){
                    FuzzyExpression dExpression= new FuzzyExpression(controller.linguisticVariableByName(parts[0]), parts[1]);
                    premises.add(dExpression);
                }
            }
            String[] conclusionStr = rule[1].trim().split(" IS ");
            if(conclusionStr.length==2){
                conclusion= new FuzzyExpression(controller.linguisticVariableByName(conclusionStr[0]), conclusionStr[1]);
            }
        }
    }
    //Aply the rule to a concrete problem creates a fuzzy set
    FuzzySet aply(ArrayList<NumericValue> problem){
        double grade=1;
        for(FuzzyExpression premise : premises){
            double localGrade=0;
            LinguisticValue lv=null;
            for(NumericValue nv : problem){
                if(premise.linguisticVariable.equals(nv.linguisticVariable)){
                    lv=premise.linguisticVariable.linguisticValueByName(premise.linguisticValueName);
                    if(lv != null){
                        localGrade= lv.membershipValue(nv.value);
                        break;
                    } 
                }
            }
            if(lv== null){
                return null;
            }
            grade= Math.min(grade, localGrade);
        }
        return conclusion.linguisticVariable.linguisticValueByName(conclusion.linguisticValueName).fuzzySet.multiplyBy(grade);
    }
}