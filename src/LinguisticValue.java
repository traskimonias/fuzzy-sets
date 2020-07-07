public class LinguisticValue {
    protected FuzzySet fuzzySet;
    protected String name;
    //Constructor
    public LinguisticValue(String _name, FuzzySet _fs){
        fuzzySet=_fs;
        name=_name;
    }
    double membershipValue(double value){
        return fuzzySet.membershipValue(value);
    }
}