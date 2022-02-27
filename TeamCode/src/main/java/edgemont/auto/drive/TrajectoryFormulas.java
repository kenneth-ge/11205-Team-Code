package edgemont.auto.drive;

public class TrajectoryFormulas {

    public static double minJerkTrajectory(double posMoved, double posTotal){
        //this min jerk curve does accel and deceleration -- just use the first half of the curve
        double halfProp = posMoved / (2 * posTotal);
        //put our proportion into the curve
        double curve = 30 * Math.pow(halfProp, 2) - 60 * Math.pow(halfProp, 3) + 30 * Math.pow(halfProp, 4);
        //normalize the curve so the maximum is at 1
        return curve / 1.875;
    }

}
