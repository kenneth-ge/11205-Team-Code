package edgemont.auto.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import edgemont.auto.drive.Drive;
import edgemont.auto.Intake;

@Autonomous(name = "Red No Camera Short Storage 19pts")
public class RedNoCamera19 extends LinearOpMode {

    Drive drive;

    DcMotor motor;
    Intake intake;

    double motorpwr;

    public void runOpMode() throws InterruptedException {
        motor = (DcMotor)hardwareMap.dcMotor.get("carousel");
        drive = new Drive(this);
        intake = new Intake(hardwareMap);

        waitForStart(); //where the program starts

        this.motorpwr = -1D;

        drive.drive(0.2);

        drive.strafe(-0.8, 3000, false);
        drive.turnToZero();
        drive.strafe(-0.80, 3000, false);

        drive.drive(-0.08);

        this.motor.setPower(this.motorpwr);
        Thread.sleep(3000);
        this.motor.setPower(0);

        drive.turnToZero();

        drive.drive(1.3);

        intake.out();
        Thread.sleep(2500);
        intake.stop();

        drive.drive(-0.6);

        drive.turn(-0.25);
        drive.strafe(-0.8, 3000, false);
        drive.turnToAngle(-0.25);
        drive.drive(-0.2);
    }

}
