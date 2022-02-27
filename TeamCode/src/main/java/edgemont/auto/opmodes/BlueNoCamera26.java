package edgemont.auto.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import edgemont.auto.cameravision.Camera;
import edgemont.auto.drive.Drive;
import edgemont.auto.Intake;
import edgemont.auto.cameravision.Pixel;

@Autonomous(name = "Blue No Camera Long Depot 26pts")
public class BlueNoCamera26 extends LinearOpMode {
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

    this.motorpwr = 1D;
    this.motor.setPower(this.motorpwr);

    drive.strafe(0.5, 5000, false);
    this.drive.drive(-1.75D);

    Thread.sleep(2500);

    this.motor.setPower(0);

    this.drive.drive(0.5);
    telemetry.addData("start angle", this.drive.getAngle());
    telemetry.update();
    this.drive.turnToZero();

    this.drive.turn(-0.25);
    this.drive.drive(0.75);

    this.drive.strafe(0.75, 3000, false);
    drive.turnToAngle(-0.25);

    intake.out();
    Thread.sleep(2500);
    intake.stop();

    this.drive.turn(0.25);

    //this.drive.strafe(-1.5);

    this.drive.drive(5);
    this.drive.turn(0.25);
    this.drive.drive(1);
    this.drive.turn(-0.25);
    this.drive.strafe(-0.53, 3000, false);
    this.drive.drive(4);
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
