package edgemont.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

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

            Thread.sleep(2000);
        }
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
