package edgemont.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import edgemont.auto.drive.Drive;
import edgemont.lib.Grabber;

@TeleOp(name="Servo Pos Test")
public class ServoPosTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Grabber grabber = new Grabber(hardwareMap);
        Servo ramp = hardwareMap.get(Servo.class, "ramp");
        boolean wasGrabbing = false;

        telemetry.addData("Controls", "Press A to toggle grabber");
        telemetry.update();

        Drive.globalAngle = 180;

        waitForStart();

        long counter = System.currentTimeMillis();
        while(opModeIsActive()){
            ramp.setPosition(Math.max(0.03, gamepad1.left_stick_y));

            if(gamepad1.a && !wasGrabbing){
                grabber.toggle();
            }
            wasGrabbing = gamepad1.a;

            telemetry.addData("ramp pos", ramp.getPosition());
            telemetry.addData("pos", grabber.pos());
            telemetry.addData("time", (System.currentTimeMillis() - counter) / 1000);
            telemetry.update();
            Thread.sleep(10);
        }
    }

}
