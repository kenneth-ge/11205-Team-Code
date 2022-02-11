package edgemont.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import edgemont.lib.Grabber;

@TeleOp(name="Grabber Pos Test")
public class ServoPosTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Grabber grabber = new Grabber(hardwareMap);
        boolean wasGrabbing = false;

        telemetry.addData("Controls", "Press A to toggle grabber");
        telemetry.update();

        waitForStart();

        long counter = System.currentTimeMillis();
        while(opModeIsActive()){
            if(gamepad1.a && !wasGrabbing){
                grabber.toggle();
            }
            wasGrabbing = gamepad1.a;

            telemetry.addData("pos", grabber.pos());
            telemetry.addData("time", (System.currentTimeMillis() - counter) / 1000);
            telemetry.update();
            Thread.sleep(10);
        }
    }

}
