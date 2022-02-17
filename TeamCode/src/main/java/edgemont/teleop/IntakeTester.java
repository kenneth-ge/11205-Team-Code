package edgemont.teleop;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import edgemont.lib.Carousel;
import edgemont.lib.Grabber;
import edgemont.lib.Slide;

@TeleOp(name="Full Final TeleOp 2-16")

public class IntakeTester extends LinearOpMode {

    DcMotor intake;
    DcMotor wheelLF; //init
    DcMotor wheelRF;
    DcMotor wheelRB;
    DcMotor wheelLB;
    Grabber grabber;
    Slide slide;
    Carousel carousel;
    RevColorSensorV3 color;

    @Override
    public void runOpMode() throws InterruptedException {
        color = hardwareMap.get(RevColorSensorV3.class, "color");
        grabber = new Grabber(hardwareMap);
        slide = new Slide(hardwareMap, grabber);
        carousel = new Carousel(hardwareMap, this);

        intake = hardwareMap.dcMotor.get("intake");
        intake.setDirection(DcMotor.Direction.FORWARD);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        
        wheelLF = hardwareMap.dcMotor.get("wheelLF");
        wheelRF = hardwareMap.dcMotor.get("wheelRF");
        wheelLB = hardwareMap.dcMotor.get("wheelLB");
        wheelRB = hardwareMap.dcMotor.get("wheelRB");
        
        wheelLF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        wheelRF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        wheelLB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        wheelRB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        
        wheelLF.setDirection(DcMotor.Direction.FORWARD);
        wheelRF.setDirection(DcMotor.Direction.FORWARD);
        wheelLB.setDirection(DcMotor.Direction.FORWARD);
        wheelRB.setDirection(DcMotor.Direction.FORWARD);
        
        printControls();
        
        waitForStart();

        carousel.threadStart();
        
        boolean yWasPressed = false;
        boolean leftBumperWasPressed = false;
        boolean toggleIntake = false;
        boolean xWasPressed = false;
        boolean toggleSlow = false;
        boolean aWasDown = false;
        boolean a2WasDown = false;
        boolean b2WasDown = false;
        boolean x2WasDown = false;
        boolean upWasDown = false;
        boolean y2WasDown = false;

        boolean wasRumbling = false;
        boolean shouldRumble = false;

        boolean consideredGrabbingAfterRumble = false;

        boolean toggleRandom = false;

        long startTime = System.currentTimeMillis();

        long rumbleTime = System.currentTimeMillis();

        //TODO: Add toggle "Random" mode in controller B
        while(opModeIsActive()){
            shouldRumble = /*color.getDistance(DistanceUnit.CM) < 4 && */(color.green() > 800 && color.red() > 800);

            if(!wasRumbling && shouldRumble){
                gamepad2.rumble(500);
                gamepad1.rumble(500);
                rumbleTime = System.currentTimeMillis();
                consideredGrabbingAfterRumble = false;
                wasRumbling = true;
            }

            if(!shouldRumble){
                wasRumbling = false;
            }

            if(System.currentTimeMillis() > rumbleTime + 300 && !grabber.grabbing && !consideredGrabbingAfterRumble){
                //grabber.grab();
                consideredGrabbingAfterRumble = true;
            }

            telemetry.addData("Time", (System.currentTimeMillis() - startTime) / 1000);
            telemetry.addData("pos slide", slide.slidePos());
            telemetry.addData("pos reel", slide.reelPos());
            printControls();

            if(gamepad1.a && !aWasDown){
                grabber.toggle();
            }
            aWasDown = gamepad1.a;

            if(gamepad2.y && !y2WasDown){
                if(!grabber.grabbing)
                    grabber.looseGrab();
                else
                    grabber.release();
            }
            y2WasDown = gamepad2.y;

            boolean yReleased = (!gamepad1.y && yWasPressed) || (!gamepad2.left_bumper && leftBumperWasPressed);
            yWasPressed = gamepad1.y; leftBumperWasPressed = gamepad2.left_bumper;
            
            if(yReleased){
                toggleIntake = !toggleIntake;
            }
            
            if(toggleIntake){
                intake.setPower(-1);
            }else{
                double power = (gamepad1.right_trigger - gamepad1.left_trigger) + (gamepad2.right_trigger - gamepad2.left_trigger);

                if(power > 0)
                    intake.setPower(power * 0.7);
                else
                    intake.setPower(power);
            }

            if(!gamepad2.dpad_up && upWasDown){
                toggleRandom = !toggleRandom;

                if(toggleRandom){
                    slide.slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    slide.reel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }else{
                    slide.slide.setTargetPosition(slide.slide.getCurrentPosition());
                    slide.reel.setTargetPosition(slide.reel.getCurrentPosition());

                    slide.slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    slide.reel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                }
            }
            upWasDown = gamepad2.dpad_up;

            if(toggleRandom){
                slide.slide.setPower(-gamepad2.left_stick_y);
                slide.reel.setPower(gamepad2.right_stick_y);
            }else{
                if (gamepad1.dpad_up) {
                    slide.up();
                } else if (gamepad1.dpad_down) {
                    slide.down();
                } else {
                    slide.setPower(gamepad2.left_stick_y);

                    if (Math.abs(gamepad2.left_stick_y) < 0.05) {
                        slide.stop();
                    }
                }
            }

            if(gamepad2.a && !a2WasDown){
                grabber.toggle();
            }else if(gamepad2.b && !b2WasDown){
                grabber.toggleLooseGrab();
            }
            a2WasDown = gamepad2.a;
            b2WasDown = gamepad2.b;

            if(gamepad2.dpad_left){
                carousel.leftDuck();
            }else if(gamepad2.dpad_right){
                carousel.rightDuck();
            }

            if(gamepad2.x && !x2WasDown){
                grabber.toggle();
            }
            x2WasDown = gamepad2.x;
            
            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            double robotAngle = -Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            double rightX = -gamepad1.right_stick_x;
            
            double v1 = -r * Math.cos(robotAngle) + rightX * 0.6;
            double v2 = r * Math.sin(robotAngle) + rightX * 0.6;
            double v3 = -r * Math.sin(robotAngle) + rightX * 0.6;
            double v4 = r * Math.cos(robotAngle) + rightX * 0.6;
            
            boolean xReleased =!gamepad1.x && xWasPressed;
            xWasPressed = gamepad1.x;
            
            if(xReleased){
                toggleSlow = !toggleSlow;
            }
            
            if(toggleSlow){
                v1 = -r * Math.cos(robotAngle) * 0.6 + rightX * 0.3;
                v2 = r * Math.sin(robotAngle) * 0.6 + rightX * 0.3;
                v3 = -r * Math.sin(robotAngle) * 0.6 + rightX * 0.3;
                v4 = r * Math.cos(robotAngle) * 0.6 + rightX * 0.3;
            }
            
            wheelLF.setPower(v1);
            wheelRF.setPower(v2);
            wheelLB.setPower(v3);
            wheelRB.setPower(v4);
        }
        //Cleanup
    }

    public void printControls(){
        telemetry.addData("Controls", "");
        telemetry.addData("C2 dpad Up (upon release)", "Switch to \"Random\" Mode");
        telemetry.addData("C2 Y", "Loose close, for moving the slide down");
        telemetry.addData("Left and right joysticks", "C1 Mecanum driving, C2 rail");
        telemetry.addData("Left trigger", "Take object in with intake");
        telemetry.addData("Right trigger", "Spit object out with intake");
        telemetry.addData("Y on C1 & C2", "Toggle intake");
        telemetry.addData("X", "Toggle slow mode");
        telemetry.addData("A", "Toggle grabber");
        telemetry.addData("Dpad up and down", "Move slide up and down");
        telemetry.addData("Controller 2 left joystick", "Up moves slide up");
        telemetry.addData("Controller 2 A button", "Red duck");
        telemetry.addData("Controller 2 B button", "Blue duck");
        telemetry.update();
    }
}