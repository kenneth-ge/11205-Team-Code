package edgemont.auto.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import edgemont.auto.Camera;
import edgemont.auto.Drive;
import edgemont.auto.IfUtil;
import edgemont.auto.Pixel;
import edgemont.lib.Carousel;
import edgemont.lib.Grabber;
import edgemont.lib.Slide;

@Autonomous(name="Red Auto Hub Duck Shipping-hub-park 42pts")
public class TestCameraLevel extends LinearOpMode {

    Drive drive;
    Grabber grabber;
    Camera camera;
    Slide slide;
    DcMotor motor;
    double motorpwr = -1;

    public void runOpMode() throws InterruptedException {
        grabber = new Grabber(hardwareMap);
        grabber.grab();

        camera = new Camera(hardwareMap);
        slide = new Slide(hardwareMap, grabber);
        drive = new Drive(this);
        motor = (DcMotor)hardwareMap.dcMotor.get("carousel");

        waitForStart(); //where the program starts

        drive.drive(-0.1);
        drive.strafe(0.85);

        int region = scan(camera);

        switch(region){
            case 2: // middle -> middle
                drive.strafe(-2);
                drive.drive(-0.5);
                slide.setPos(4966, -5186, false, 1);
                drive.turn(-0.100);
                drive.drive(-1);
                drive.turn(0.08);
                grabber.release();
                slide.setPos((int) (5214 * 1.1), (int) (-5445 * 1.1), false, 1);
                drive.turn(-0.08);
                slide.waitForFinish();
                grabber.looseGrab();

                slide.setPos(0, 0, false, 0.4);

                drive.drive(0.8);
                Thread.sleep(500);
                drive.turnToAngle(0.5);
                drive.strafe(-4.35);
                drive.drive(-1.05);

                carousel();

                drive.turnToAngle(0.5);

                drive.drive(1.9);

                break;
            case 3: //right -> bottom
                drive.strafe(-26./12. - 0.85, Drive.INFINITY, true, 0.25);
                slide.setPos((int) (0.90 * 5333 * 0.5 * 0.90), (int) (-5160 * 0.90 * 0.5 * 90), false, 0.5);
                drive.drive(-18./12. + 0.1);
                drive.turn(-5/360.);
                slide.setPos((int) (0.90 * 5333 * 0.90), (int) (-5160 * 0.90 * 0.90), true, 0.5);
                drive.turn(5/360.);
                grabber.release();
                Thread.sleep(500);
                grabber.looseGrab();
                slide.setPos(0, 0, false, 0.3);
                drive.drive(0.1);
                drive.turn(-0.5);
                drive.turnToAngle(-0.5);
                drive.drive(-0.85);
                drive.strafe(-5, Drive.INFINITY, true, 0.5);
                drive.drive(-0.60);

                carousel();

                drive.turnToAngle(0.25);
                /*drive.drive(6, 1);
                drive.strafe(0.8, Drive.INFINITY, true, 0.5);
                drive.drive(2.5, 0.5);*/

                /*drive.drive(1.8);
                drive.turnToAngle(0.5);*/
                break;
            case 1:
            default: //left -> top
                /*drive.strafe(-2);
                drive.drive(-0.5);
                drive.turn(-0.100);
                slide.setPos((int) (4966 * 1.3), (int) (-5186 * 1.3));
                drive.turn(0.075);
                grabber.release();
                Thread.sleep(500);
                grabber.looseGrab();
                drive.turn(-0.075);
                slide.setPos(0, 0);*/
                break;
        }
    }

    public void carousel() throws InterruptedException {
        motor.setPower(motorpwr);
        Thread.sleep(3000);
        motor.setPower(0);
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
