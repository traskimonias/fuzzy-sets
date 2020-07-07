public class App {
    public static void main(String[] args) throws Exception {
        //Creates the system

        FuzzyController controller = new FuzzyController("GPS Zoom manager");

        System.out.println("Adding input variables");
        //Adding linguistic variable:
        //distance (in m, from 0 to 500 000)

        LinguisticVariable distance= new LinguisticVariable("Distance",0,500000);
        distance.addLinguisticValue(new LinguisticValue("Small", new FuzzySetLeftTrapezoid(0, 500000, 30, 50)));
        distance.addLinguisticValue(new LinguisticValue("Middle", new FuzzySetTrapezoid(0, 500000, 40, 50, 100, 150)));
        distance.addLinguisticValue(new LinguisticValue("Big", new FuzzySetRightTrapezoid(0, 500000, 100, 150)));
        controller.addInputVariable(distance);
        //Adding linguistic variable:
        //speed (in km/h, from 0 to 200)
        LinguisticVariable velocity= new LinguisticVariable("Speed", 0, 200);
        velocity.addLinguisticValue(new LinguisticValue("Slow", new FuzzySetLeftTrapezoid(0, 200, 20, 30)));
        velocity.addLinguisticValue(new LinguisticValue("Middle", new FuzzySetTrapezoid(0, 200, 20, 30, 70, 80)));
        velocity.addLinguisticValue(new LinguisticValue("Fast", new FuzzySetTrapezoid(0, 200, 70, 80, 90, 110)));
        velocity.addLinguisticValue(new LinguisticValue("VeryFast", new FuzzySetRightTrapezoid(0, 200, 90, 110)));
        controller.addInputVariable(velocity);
        System.out.println("Adding output variables");
        //Adding output:
        //Zoom level (from 1 to 5)
        LinguisticVariable zoom= new LinguisticVariable("Zoom", 1, 5);
        zoom.addLinguisticValue(new LinguisticValue("Low", new FuzzySetLeftTrapezoid(0, 5, 1, 2)));
        zoom.addLinguisticValue(new LinguisticValue("Normal", new FuzzySetTrapezoid(0, 5, 1, 2, 3, 4)));
        zoom.addLinguisticValue(new LinguisticValue("Big", new FuzzySetRightTrapezoid(0, 5, 3, 4)));
        controller.addOutputVariable(zoom);
        System.out.println("Adding rules");
        //Adding the rules
        controller.addRule("IF Distance IS Big THEN Zoom IS Low");
        controller.addRule("IF Distance IS Small AND Speed IS Slow THEN Zoom IS Normal");
        controller.addRule("IF Distance IS Small AND Speed IS Middle THEN Zoom IS Normal");
        controller.addRule("IF Distance IS Small AND Speed IS Fast THEN Zoom IS Big");
        controller.addRule("IF Distance IS Small AND Speed IS VeryFast THEN Zoom IS Big");
        controller.addRule("IF Distance IS Middle AND Speed IS Slow THEN Zoom IS Low");
        controller.addRule("IF Distance IS Middle AND Speed IS Middle THEN Zoom IS Normal");
        controller.addRule("IF Distance IS Middle AND Speed IS Fast THEN Zoom IS Normal");
        controller.addRule("IF Distance IS Middle AND Speed IS VeryFast THEN Zoom IS Big");

        System.out.println("Solving user case");
        System.out.println("insert velocity:");
        double v = Double.parseDouble(System.console().readLine());
        System.out.println("insert distance");
        double d = Double.parseDouble(System.console().readLine());
        controller.clearNumericValues();
        controller.addNumericValue(velocity, v);
        controller.addNumericValue(distance, d);
        System.out.println("Result: "+ controller.resolve()+ "\n");
    }
}
