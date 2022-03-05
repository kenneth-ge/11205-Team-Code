package edgemont.auto.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import edgemont.auto.Intake;
import edgemont.auto.cameravision.Camera;
import edgemont.auto.cameravision.Pixel;
import edgemont.auto.drive.Drive;
import edgemont.lib.Grabber;
import edgemont.lib.Slide;

@Autonomous(name = "Test")
public class Test extends LinearOpMode {

    Drive drive;
    Slide slide;
    Grabber grabber;

    public void runOpMode() throws InterruptedException {
        drive = new Drive(this);
        grabber = new Grabber(hardwareMap);
        slide = new Slide(hardwareMap, grabber);

        grabber.grab();

        waitForStart(); //where the program starts

        slide.setPos(753, true, 0.15);
        grabber.release();
        Thread.sleep(500);
        slide.setPos(0, true, 0.15);
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
                if (current.h >= 70.0D && current.h <= 170.0D && current.s >= 0.5D) //sat > 0.5 uses our perceptron classification algorithm
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
