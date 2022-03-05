package edgemont.auto.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import edgemont.auto.cameravision.Camera;
import edgemont.auto.drive.Drive;
import edgemont.auto.cameravision.Pixel;
import edgemont.lib.Grabber;
import edgemont.lib.Ramp;
import edgemont.lib.Slide;

@Autonomous(name="Auto Hub Duck Shipping-hub-park 42pts")
public class Auto42 extends LinearOpMode {

    Drive drive;
    Grabber grabber;
    Camera camera;
    Slide slide;
    DcMotor motor, motor2;
    Ramp ramp;
    double motorpwr = -1;

    public void runOpMode() throws InterruptedException {
        grabber = new Grabber(hardwareMap);
        grabber.grab();

        camera = new Camera(hardwareMap);
        slide = new Slide(hardwareMap, grabber);
        drive = new Drive(this);
        motor = (DcMotor)hardwareMap.dcMotor.get("carousel");
        motor2 = hardwareMap.dcMotor.get("reel");

        ramp = new Ramp(hardwareMap);

        waitForStart(); //where the program starts

        drive.drive(-0.1);
        drive.strafe(0.2);

        int region = scan(camera);

        switch(region){
            case 2: // middle -> middle
                drive.strafe(-1.1);
                drive.drive(-0.5);
                slide.setPos((int) (4966. / 11235. * Slide.MAX_SLIDE), false, 0.5);
                drive.turn(-0.100);
                drive.drive(-1);
                drive.turn(0.06);
                grabber.release();
                slide.setPos((int) (5214 * 1.1 / 11235. * Slide.MAX_SLIDE), false, 0.5);
                drive.turn(-0.06);
                slide.waitForFinish();
                grabber.looseGrab();

                slide.setPos(0, false, 0.4);

                drive.drive(0.8);
                Thread.sleep(500);
                drive.turnToAngle(0.25);
                drive.drive(-3.75);
                drive.turnToAngle(0.5);
                drive.drive(-0.4);

                carousel();

                drive.turnToAngle(0.5);

                drive.drive(1.9);
                break;
            case 1: //right -> bottom
                slide.setPos(753, false, 0.15);
                drive.strafe(-18.5/12. - 0.2, Drive.INFINITY, true, 0.25);
                slide.waitForFinish();
                grabber.release();
                Thread.sleep(500);
                grabber.looseGrab();
                slide.setPos(0, false, 0.15);
                drive.drive(-12./12. + 0.1);
                slide.waitForFinish();
                drive.drive(-0.55);

                ramp.lift();
                Thread.sleep(500);
                ramp.retract();

                drive.drive(-0.05);
                drive.drive(0.1);
                drive.drive(0.85);
                drive.turnToAngle(0.25);
                drive.drive(-4);
                drive.strafe(0.225, 2500);

                carousel();

                drive.turnToAngle(0.25);
                drive.drive(5, 0.7);
                drive.strafe(0.8);
                drive.drive(4, 0.7);
                break;
            case 3:
            default: //left -> top
                slide.setPos(Slide.MAX_SLIDE, false, 0.5);
                drive.strafe(0.4);
                drive.turn(0.13);
                slide.waitForFinish();
                grabber.release();
                Thread.sleep(500);
                slide.setPos(Slide.MAX_SLIDE / 2, true, 0.5);
                drive.turnToAngle(0);
                slide.setPos(0, false, 0.5);
                break;
        }
    }

    public void carousel() throws InterruptedException {
        motor.setPower(motorpwr);
        motor2.setPower(motorpwr);
        Thread.sleep(3000);
        motor.setPower(0);
        motor2.setPower(0);
    }

    public int scan(Camera camera) throws InterruptedException {
        Pixel[][] image = camera.picture();
        telemetry.addData("dims", "(" + image.length + ", " + image[0].length + ")");

        Pixel topPixel = image[0][0];
        int left = 0;
        int right = 0;
        int middle = 0;

        double leftSat = 0;
        double rightSat = 0;
        double middleSat = 0;

        double width = (image[0]).length;
        for (int j = 0; j < (image[0]).length; j++) {
            for (int i = 0; i < image.length; i++) {
                Pixel current = image[i][j];
                if (current.h >= 70.0D && current.h <= 170.0D && current.s >= 0.5D) //sat > 0.5 uses our perceptron classification algorithm
                    if (j <= width / 3.0D) {
                        left++;
                        leftSat += current.s;
                    } else if (j <= 2.0D * width / 3.0D) {
                        middle++;
                        middleSat += current.s;
                    } else {
                        right++;
                        rightSat += current.s;
                    }
            }
        }

        telemetry.addData("left", left);
        telemetry.addData("middle", middle);
        telemetry.addData("right", right);

        telemetry.addData("leftSat", leftSat);
        telemetry.addData("middleSat", middleSat);
        telemetry.addData("rightSat", rightSat);

        if (leftSat > middleSat && leftSat > rightSat)
            return 1;
        if (middleSat > leftSat && middleSat > rightSat)
            return 2;
        return 3;
    }

}
