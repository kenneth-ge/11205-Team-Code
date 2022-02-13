package edgemont.lib;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slide {

    final double RATIO = (7./4.) * 0.6;
    final double POWER = 0.25;
    final int MAX_SLIDE = 0, MAX_REEL = 0, MIN_SLIDE = -3595, MIN_REEL = -1000;
    final double SLIDE_OVER_REEL = ((double) MIN_SLIDE) / ((double) MIN_REEL);
    DcMotor slide, reel;
    Grabber grabber;

    public Slide(HardwareMap hardwareMap, Grabber grabber){
        this.grabber = grabber;

        slide = hardwareMap.get(DcMotor.class, "slide");
        reel = hardwareMap.get(DcMotor.class, "reel");

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        reel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void up(){
        setPower(POWER);
    }

    public void down(){
        setPower(-POWER);
    }

    public void setPower(double power){
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        reel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if(power > 0){
            if(slide.getCurrentPosition() > MAX_SLIDE || reel.getCurrentPosition() > MAX_REEL){
                return;
            }
        }else{
            /*if(slide.getCurrentPosition() < MIN_SLIDE + 1000 || reel.getCurrentPosition() < MIN_REEL + 300){
                grabber.grabNotTight();
            }*/
            if(slide.getCurrentPosition() < MIN_SLIDE || reel.getCurrentPosition() < MIN_REEL){
                return;
            }
        }

        //slide.setTargetPosition(power * slide);
        slide.setPower(power);
        reel.setPower(power * RATIO);//7 over 4
    }

    public void stop(){
        slide.setPower(0);
        reel.setPower(0);//7 over 4
    }

    public int slidePos(){
        return slide.getCurrentPosition();
    }

    public int reelPos(){
        return reel.getCurrentPosition();
    }

}
