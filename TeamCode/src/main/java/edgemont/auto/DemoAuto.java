package edgemont.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Demo for Control Award")
public class DemoAuto extends LinearOpMode {
    Drive drive;

    Camera camera;

    DcMotor motor;
    Intake intake;

    double motorpwr;

    public void runOpMode() throws InterruptedException {
        camera = new Camera(hardwareMap);
        motor = (DcMotor)hardwareMap.dcMotor.get("carousel");
        drive = new Drive(this);
        intake = new Intake(hardwareMap);

        waitForStart(); //where the program starts

        long INFINITY = Long.MAX_VALUE / 3;

        int scanResult = scan(camera);
        if(scanResult == 1){
            drive.strafe(-.25);
            drive.drive(.5);
        }
        else if (scanResult == 2){
            drive.drive(.5);
        }
        else{
            drive.strafe(.25);
            drive.drive(.5);
        }


        drive.drive(3);
        Thread.sleep(1000);
        drive.drive(-3);
        Thread.sleep(1000);
        drive.turn(1);
        Thread.sleep(1000);
        drive.turn(-1);
        Thread.sleep(1000);
        drive.strafe(3, INFINITY, true);
        Thread.sleep(1000);
        drive.strafe(-3, INFINITY, true);
        Thread.sleep(1000);
        //spinCarousel();


    }

    public void spinCarousel(CRServo servo, int time, double servopwr) throws InterruptedException {
        servopwr = 0.2D;
        servo.setPower(servopwr);
        Thread.sleep((time * 1000));
        servo.setPower(0.0D);
    }

    public int scan(Camera camera) throws InterruptedException {
        Pixel[][] image = camera.picture();
        telemetry.addData("dims", "(" + image.length + ", " + image[0].length + ")");

        Pixel topPixel = image[0][0];
        int left = 0;
        int right = 0;
        int middle = 0;
        double width = (image[0]).length;
        for (int j = 0; j < (image[0]).length; j++) {
            for (int i = 0; i < image.length; i++) {
                Pixel current = image[i][j];
                if (current.h >= 70.0D && current.h <= 170.0D && current.s >= 0.49D)
                    if (j <= width / 3.0D) {
                        left++;
                    } else if (j <= 2.0D * width / 3.0D) {
                        middle++;
                    } else {
                        right++;
                    }
            }
        }

        telemetry.addData("left", left);
        telemetry.addData("middle", middle);
        telemetry.addData("right", right);

        if (left > middle && left > right)
            return 1;
        if (middle > left && middle > right)
            return 2;
        return 3;
    }
}
