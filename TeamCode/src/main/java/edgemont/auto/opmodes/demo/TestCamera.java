package edgemont.auto.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import edgemont.auto.cameravision.Camera;
import edgemont.auto.cameravision.Pixel;

@Autonomous(name = "Test Camera")
public class TestCamera extends LinearOpMode {

    Camera camera;

    public void runOpMode() throws InterruptedException {
        camera = new Camera(hardwareMap);

        waitForStart(); //where the program starts

        while(opModeIsActive()) {
            int region = scan(camera);

            telemetry.addData("region", region);
            telemetry.update();

            System.out.println("region " + region);

            Thread.sleep(500);
        }
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
