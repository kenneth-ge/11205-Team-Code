package edgemont.auto;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Util;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Drive {

    final static long INFINITY = Long.MAX_VALUE / 3;

    DcMotor wheelLF; 
    DcMotor wheelRF;
    DcMotor wheelRB; 
    DcMotor wheelLB;
    
    BNO055IMU imu;
    Orientation lastAngles = new Orientation();
    double globalAngle;
    
    LinearOpMode opMode;
    Telemetry telemetry;
    HardwareMap hardwareMap;
    
    public Drive(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        this.telemetry = opMode.telemetry;
        this.hardwareMap = opMode.hardwareMap;
        
        BNO055IMU.Parameters params = new BNO055IMU.Parameters();
        params.mode = BNO055IMU.SensorMode.IMU;
        params.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        params.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        params.loggingEnabled = false;
        
        imu = opMode.hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(params);
        
        wheelLF = hardwareMap.dcMotor.get("wheelLF");
        wheelRF = hardwareMap.dcMotor.get("wheelRF");
        wheelLB = hardwareMap.dcMotor.get("wheelLB");
        wheelRB = hardwareMap.dcMotor.get("wheelRB");
        
        wheelLF.setDirection(DcMotor.Direction.FORWARD);
        wheelRF.setDirection(DcMotor.Direction.REVERSE);
        wheelLB.setDirection(DcMotor.Direction.FORWARD);
        wheelRB.setDirection(DcMotor.Direction.REVERSE);
        
        wheelLF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        wheelRF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        wheelLB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        wheelRB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
       
       while(!imu.isGyroCalibrated()){
           Thread.sleep(20);
       }

       getAngle();
       globalAngle = 0;
    }

    public void turnToAngle(double toTurn) throws InterruptedException {
        double angleTurn = toTurn * 360.;
        double angle = getAngle();

        globalAngle = globalAngle % 360;

        while(globalAngle < 0){
            globalAngle += 360;
        }

        telemetry.addData("angle", globalAngle);
        telemetry.addData("rotations", (angleTurn-globalAngle) / 360.);
        telemetry.update();

        double rotOneWay = (angleTurn-globalAngle) / 360.;
        double rotOtherWay = rotOneWay - 1;

        if(rotOneWay < 0){
            rotOtherWay = rotOneWay + 1;
        }

        if(Math.abs(rotOneWay) <= Math.abs(rotOtherWay)){
            turn(rotOneWay);
        }else{
            turn(rotOtherWay);
        }
    }

    public void turnToZero() throws InterruptedException {
        turnToAngle(0);
        /*double angle = getAngle();

        while(globalAngle < 0){
            globalAngle += 360;
        }

        globalAngle = globalAngle % 360;

        telemetry.addData("angle", globalAngle);
        telemetry.addData("rotations", -globalAngle / 360.);
        telemetry.update();
        turn(-globalAngle / 360.);*/
    }
    
    public double getAngle(){
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }
    
    
    final double TURN_SPEED     = 0.4;
    final double DRIVE_SPEED    = 0.4;
    
    /** @param rotation - turns left by this number of full rotations */
    public void turn(double rotation) throws InterruptedException{
        wheelLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            
        wheelLB.setPower(TURN_SPEED * Math.signum(rotation));
        wheelRF.setPower(-TURN_SPEED * Math.signum(rotation));
        wheelLF.setPower(TURN_SPEED * Math.signum(rotation));
        wheelRB.setPower(-TURN_SPEED * Math.signum(rotation));
        
        final double initialAngle = getAngle();
        final double targetAngle = initialAngle+rotation*360.;
        
        while(true){
            double angleLeft = targetAngle - getAngle();
            
            if(Math.abs(angleLeft) < 30.){
                //change speed
                double proportionFinished = 1 - (Math.abs(angleLeft) / 30.0);
                double percentPower = Math.exp(proportionFinished * -2.5);
                
                wheelLB.setPower(percentPower*TURN_SPEED * Math.signum(rotation));
                wheelRF.setPower(percentPower*-TURN_SPEED * Math.signum(rotation));
                wheelLF.setPower(percentPower*TURN_SPEED * Math.signum(rotation));
                wheelRB.setPower(percentPower*-TURN_SPEED * Math.signum(rotation));
            }
            
            if(targetAngle - initialAngle > 0){
                if(angleLeft < 0) {
                    wheelLB.setPower(0);
                    wheelRF.setPower(0);
                    wheelLF.setPower(0);
                    wheelRB.setPower(0);
                    break;
                }
                
            }
            else {
                if(angleLeft > 0){
                    wheelLB.setPower(0);
                    wheelRF.setPower(0);
                    wheelLF.setPower(0);
                    wheelRB.setPower(0);
                    break;
                }
            }
        }
    }

    void strafeRight(double feet, long maxTime, boolean angleCorrect) throws InterruptedException {
        final double initialAngle = getAngle();
        final int target = (int) (feet*(595));
        final int startPosition = wheelLF.getCurrentPosition();

        wheelLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //double initialAngle = getAngle();
        double powers = -0.25;

        wheelLB.setPower(0.25);
        wheelRF.setPower(0.25);
        wheelLF.setPower(-0.25);
        wheelRB.setPower(-0.25);

        long start = System.currentTimeMillis();

        while (true){
            if(System.currentTimeMillis() - start >= maxTime){
                break;
            }

            Thread.sleep(10);
            int average = wheelLF.getCurrentPosition();

            int positionsLeft = Math.abs(target - Math.abs(average - startPosition));

            if (wheelLF.getCurrentPosition() < startPosition - target){
                break;
            }

            if (positionsLeft < 250){
                double proportionLeft = 1 - positionsLeft / 250.0;
                double percentPower = Math.exp(proportionLeft * -2);
                powers = -0.25 * percentPower;
            }
            else{
                powers = -0.25;
            }

            if(angleCorrect) {
                double correctionFactor = (initialAngle - getAngle()) * 0.03;

                wheelLB.setPower(-powers + correctionFactor);
                wheelRF.setPower(-powers + correctionFactor);
                wheelLF.setPower(powers - correctionFactor);
                wheelRB.setPower(powers - correctionFactor);
            }else{
                wheelLB.setPower(-powers);
                wheelRF.setPower(-powers);
                wheelLF.setPower(powers);
                wheelRB.setPower(powers);
            }
        }

        wheelLB.setPower(0);
        wheelRF.setPower(0);
        wheelLF.setPower(0);
        wheelRB.setPower(0);
    }

    /** Values should still be negative */
    void strafeLeft(double feet, long maxTime, boolean angleCorrect) throws InterruptedException {
        final double initialAngle = getAngle();
        final int posToMove = (int) Math.abs(-feet*(595));
        final int startPosition = wheelLF.getCurrentPosition();

        wheelLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double powers = 0.25;

        wheelLB.setPower(-powers);
        wheelRF.setPower(-powers);
        wheelLF.setPower(powers);
        wheelRB.setPower(powers);

        final long start = System.currentTimeMillis();
        while (true){
            if(System.currentTimeMillis() - start >= maxTime){
                break;
            }

            Thread.sleep(10);

            final int average = wheelLF.getCurrentPosition();
            final int posAway = posToMove - Math.abs(average - startPosition);
            final int positionsLeft = Math.abs(posAway);

            if (average >= posToMove+startPosition || posAway < 5){
                break;
            }

            if (positionsLeft < 250){
                double proportionLeft = 1.0 - positionsLeft / 250.0;
                double percentPower = Math.exp(proportionLeft * -2);
                powers = 0.25 * percentPower;
            }
            else{
                powers = 0.25;
            }

            if(angleCorrect) {
                double correctionFactor = (initialAngle - getAngle()) * 0.03;

                wheelLB.setPower(-powers + correctionFactor);
                wheelRF.setPower(-powers + correctionFactor);
                wheelLF.setPower(powers - correctionFactor);
                wheelRB.setPower(powers - correctionFactor);
            }else{
                wheelLB.setPower(-powers);
                wheelRF.setPower(-powers);
                wheelLF.setPower(powers);
                wheelRB.setPower(powers);
            }
        }

        wheelLB.setPower(0);
        wheelRF.setPower(0);
        wheelLF.setPower(0);
        wheelRB.setPower(0);
    }

    public void strafe(double feet) throws InterruptedException {
        strafe(feet, INFINITY);
    }

    public void strafe(double feet, long maxTime) throws InterruptedException {
        strafe(feet, maxTime, true);
    }

    //Right by default
    //Weight distribution issue may be solved by placing weights on the other side of the robot
    public void strafe(double feet, long maxTime, boolean angleCorrect) throws InterruptedException {
        if(feet > 0){
            strafeRight(feet, maxTime, angleCorrect);
        }else{
            strafeLeft(feet, maxTime, angleCorrect);
        }
    }

    public static final int POS_PER_FOOT = 530;
    
    public void drive(double feet) throws InterruptedException {
        int positionLF = wheelLF.getCurrentPosition();
        int positionLB = wheelLB.getCurrentPosition();
        int positionRF = wheelRF.getCurrentPosition();
        int positionRB = wheelRB.getCurrentPosition();
    
        int target = (int) (feet*(-POS_PER_FOOT));
        int startPosition = (positionLF+positionRF+positionLB+positionRB)/4;

        wheelLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wheelRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        double initialAngle = getAngle();
        double[] powers = new double[4];

        if(target > 0){
            wheelLB.setPower(DRIVE_SPEED);
            wheelRF.setPower(DRIVE_SPEED);
            wheelLF.setPower(DRIVE_SPEED);
            wheelRB.setPower(DRIVE_SPEED);
            powers[0] = powers[1] = powers[2] = powers[3] = DRIVE_SPEED;
        }
        else{
            wheelLB.setPower(-DRIVE_SPEED);
            wheelRF.setPower(-DRIVE_SPEED);
            wheelLF.setPower(-DRIVE_SPEED);
            wheelRB.setPower(-DRIVE_SPEED);
            powers[0] = powers[1] = powers[2] = powers[3] = -DRIVE_SPEED;
        }
        
        while (true){
            Thread.sleep(10);
            positionLF = wheelLF.getCurrentPosition();
            positionLB = wheelLB.getCurrentPosition();
            positionRF = wheelRF.getCurrentPosition();
            positionRB = wheelRB.getCurrentPosition();
            
            int average = (positionLF+positionRF+positionLB+positionRB)/4;
            int positionsLeft = Math.abs(target+startPosition-average);
            if(target > 0){
                if (average >= target+startPosition - 0){
                    break;
                }
            }else{
                if (average <= target+startPosition + 0){
                    break;
                }
            }
            
            if (positionsLeft < 250){
                double proportionLeft = 1 - positionsLeft / 250.0;
                double percentPower = Math.exp(proportionLeft * -2);
                if(target > 0){
                    powers[0] = powers[1] = powers[2] = powers[3] = DRIVE_SPEED * percentPower;
                }
                else{
                    powers[0] = powers[1] = powers[2] = powers[3] = -DRIVE_SPEED * percentPower;
                }
            }
            else{
                if(target > 0)
                    powers[0] = powers[1] = powers[2] = powers[3] = DRIVE_SPEED;
                else
                    powers[0] = powers[1] = powers[2] = powers[3] = -DRIVE_SPEED;
            }
            
            double correctionFactor = (initialAngle - getAngle()) * 0.03;
            powers[0] += correctionFactor;
            powers[2] += correctionFactor;
            powers[1] -= correctionFactor;
            powers[3] -= correctionFactor;
            
            wheelLB.setPower(powers[0]);
            wheelRF.setPower(powers[1]);
            wheelLF.setPower(powers[2]);
            wheelRB.setPower(powers[3]);
        }  
        
        
        wheelLB.setPower(0);
        wheelRF.setPower(0);
        wheelLF.setPower(0);
        wheelRB.setPower(0);
    }
    
}