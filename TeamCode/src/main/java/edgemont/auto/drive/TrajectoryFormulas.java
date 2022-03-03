package edgemont.auto.drive;

public class TrajectoryFormulas {

    public static double minJerkTrajectory(double posMoved, double posTotal) {
        //this min jerk curve does accel and deceleration -- just use the first half of the curve
        double halfProp = posMoved / (2 * posTotal);
        //put our proportion into the curve
        double curve = 30 * Math.pow(halfProp, 2) - 60 * Math.pow(halfProp, 3) + 30 * Math.pow(halfProp, 4);
        //normalize the curve so the maximum is at 1
        curve = curve / 1.875;
        //clamp curve so it's not 0
        curve = Math.max(curve, 0.05);
        return curve;
    }

    public static double expGrowth(double posMoved, double posTotal) {
        double prop = posMoved / posTotal;

        return Math.exp((prop - 1.) * 1);
    }

    public static double linear(double posMoved, double posTotal){
        double minPower = 0.3;
        double prop = posMoved / posTotal;

        return minPower + 0.7 * prop;
    }

}
