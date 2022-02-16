package edgemont.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Red No Camera Long Depot 26pts")
public class RedNoCamera26 extends LinearOpMode {
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

    drive.strafe(-1, 3000);
    drive.turnToZero();
    drive.strafe(-0.85, 3000);

    drive.drive(-0.08);

    this.motor.setPower(this.motorpwr);
    Thread.sleep(3000);
    this.motor.setPower(0);

    drive.turnToZero();

    drive.drive(0.25);

    intake.out();
    Thread.sleep(2500);
    intake.stop();

    drive.turn(-0.25);

    drive.drive(5.5);

    drive.strafe(1, 5000);

    drive.strafe(0.2, 3000);
    drive.turnToAngle(-0.25);

    drive.drive(3.75);
  }

}
