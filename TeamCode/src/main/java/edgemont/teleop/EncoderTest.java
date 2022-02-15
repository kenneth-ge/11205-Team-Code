package edgemont.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.lang.reflect.Field;

@TeleOp(name="Encoder Test")
public class EncoderTest extends LinearOpMode {

    DcMotor intake, reel, carousel, slide;
    DcMotor wheelLF, wheelLB, wheelRF, wheelRB;

    @Override
    public void runOpMode() throws InterruptedException {
        intake = hardwareMap.get(DcMotor.class, "intake");
        reel = hardwareMap.get(DcMotor.class, "reel");
        carousel = hardwareMap.get(DcMotor.class, "carousel");
        slide = hardwareMap.get(DcMotor.class, "slide");
        wheelLF = hardwareMap.get(DcMotor.class, "wheelLF");
        wheelLB = hardwareMap.get(DcMotor.class, "wheelLB");
        wheelRF = hardwareMap.get(DcMotor.class, "wheelRF");
        wheelRB = hardwareMap.get(DcMotor.class, "wheelRB");

        waitForStart();

        while(opModeIsActive()){
            telemetry.addData("intake", intake.getCurrentPosition());
            telemetry.addData("reel", reel.getCurrentPosition());
            telemetry.addData("carousel", carousel.getCurrentPosition());
            telemetry.addData("slide", slide.getCurrentPosition());
            telemetry.addData("wheelLF", wheelLF.getCurrentPosition());
            telemetry.addData("wheelLB", wheelLB.getCurrentPosition());
            telemetry.addData("wheelRF", wheelRF.getCurrentPosition());
            telemetry.addData("wheelRB", wheelRB.getCurrentPosition());
            telemetry.update();

            Thread.sleep(25);
        }
    }
}
