package edgemont.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Rail")

public class Rail  extends LinearOpMode {

    DcMotor slide;
    DcMotor reel;

    @Override

    public void runOpMode() throws InterruptedException {
        slide = hardwareMap.dcMotor.get("slide");
        slide.setDirection(DcMotor.Direction.FORWARD);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        reel = hardwareMap.dcMotor.get("reel");
        reel.setDirection(DcMotor.Direction.FORWARD);
        reel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        double dcMotorPwr = 0.1; //power for the motor

        waitForStart();

        while(opModeIsActive()){
            dcMotorPwr = gamepad1.left_stick_y;
            slide.setPower(dcMotorPwr);
            reel.setPower(dcMotorPwr * (7./4.));//7 over 4

            telemetry.addData("slide", dcMotorPwr);
            telemetry.addData("reel", dcMotorPwr * (7/4));
            telemetry.update();


        }

    }
    //Cleanup


}
