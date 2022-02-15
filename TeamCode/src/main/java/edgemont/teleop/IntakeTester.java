package edgemont.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import edgemont.lib.Carousel;
import edgemont.lib.Grabber;
import edgemont.lib.Slide;

@TeleOp(name="Full Final TeleOp 2-12")

public class IntakeTester extends LinearOpMode {

    DcMotor intake;
    DcMotor wheelLF; //init
    DcMotor wheelRF;
    DcMotor wheelRB;
    DcMotor wheelLB;
    Grabber grabber;
    Slide slide;
    Carousel carousel;

    @Override
    public void runOpMode() throws InterruptedException {
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
        boolean toggleIntake = false;
        boolean xWasPressed = false;
        boolean toggleSlow = false;
        boolean aWasDown = false;
        boolean a2WasDown = false;
        boolean b2WasDown = false;
        boolean x2WasDown = false;
        boolean startWasDown = false;
        boolean backWasDown = false;

        boolean toggleRandom = false;

        long startTime = System.currentTimeMillis();

        //TODO: Add toggle "Random" mode in controller B
        while(opModeIsActive()){
            telemetry.addData("Time", (System.currentTimeMillis() - startTime) / 1000);
            telemetry.addData("pos slide", slide.slidePos());
            telemetry.addData("pos reel", slide.reelPos());
            printControls();

            if(gamepad1.a && !aWasDown){
                grabber.toggle();
            }
            aWasDown = gamepad1.a;

            if(gamepad2.x && !x2WasDown){
                grabber.toggle();
            }
            x2WasDown = gamepad2.x;

            if(gamepad2.back && !backWasDown){
                grabber.looseGrab();
            }
            backWasDown = gamepad2.back;

            boolean yReleased = !(gamepad1.y || gamepad2.y) && yWasPressed;
            yWasPressed = (gamepad1.y || gamepad2.y);
            
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

            if(!gamepad2.start && startWasDown){
                toggleRandom = !toggleRandom;
            }
            startWasDown = gamepad2.start;

            if(toggleRandom){
                slide.slide.setPower(gamepad2.left_stick_y);
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
                carousel.leftDuck();
            }else if(gamepad2.b && !b2WasDown){
                carousel.rightDuck();
            }

            a2WasDown = gamepad2.a;
            b2WasDown = gamepad2.b;
            
            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            double robotAngle = -Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            double rightX = -gamepad1.right_stick_x;
            
            double v1 = -r * Math.cos(robotAngle) + rightX;
            double v2 = r * Math.sin(robotAngle) + rightX;
            double v3 = -r * Math.sin(robotAngle) + rightX;
            double v4 = r * Math.cos(robotAngle) + rightX;
            
            boolean xReleased =!gamepad1.x && xWasPressed;
            xWasPressed = gamepad1.x;
            
            if(xReleased){
                toggleSlow = !toggleSlow;
            }
            
            if(toggleSlow){
                v1 = v1 * 0.6;
                v2 = v2 * 0.6;
                v3 = v3 * 0.6;
                v4 = v4 * 0.6;
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
        telemetry.addData("C2 Start (upon release)", "Switch to \"Random\" Mode");
        telemetry.addData("C2 Back", "Loose close, for moving the slide down");
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