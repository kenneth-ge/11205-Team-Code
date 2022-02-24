package edgemont.auto.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import edgemont.auto.Drive;
import edgemont.auto.Intake;
import edgemont.lib.Grabber;

@Autonomous(name = "Red No Camera Long Depot 26pts")
public class RedNoCamera26 extends LinearOpMode {
  Drive drive;

  DcMotor motor;
  Intake intake;

  Grabber grabber;

  double motorpwr;

  public void runOpMode() throws InterruptedException {
    motor = (DcMotor)hardwareMap.dcMotor.get("carousel");
    drive = new Drive(this);
    intake = new Intake(hardwareMap);
    grabber = new Grabber(hardwareMap);

    //grabber.grab();

    waitForStart(); //where the program starts

    this.motorpwr = -1D;

    drive.drive(0.2);

    drive.strafe(-1, 3000, false);
    drive.turnToZero();
    drive.strafe(-0.85, 3000, false);

    drive.drive(-0.08);

    this.motor.setPower(this.motorpwr);
    Thread.sleep(3000);
    this.motor.setPower(0);

    drive.turnToZero();

    drive.drive(0.25);

    intake.intake.setPower(-intake.POWER * 0.7);
    Thread.sleep(2500);
    intake.stop();

    drive.turn(-0.25);

    drive.drive(5.5);

    drive.strafe(1, 5000, false);

    drive.strafe(0.65, 3000, false);
    drive.turnToAngle(-0.25);

    drive.drive(3.75);
  }

}
