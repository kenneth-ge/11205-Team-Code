package edgemont.lib;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slide {

    final double RATIO = (7./4.) * 0.6;
    final double POWER = 0.1;
    final int MAX_SLIDE = Integer.MAX_VALUE, MAX_REEL = Integer.MAX_VALUE, MIN_SLIDE = 0, MIN_REEL = 0;
    DcMotor slide, reel;

    public Slide(HardwareMap hardwareMap){
        slide = hardwareMap.get(DcMotor.class, "slide");
        reel = hardwareMap.get(DcMotor.class, "reel");
    }

    public void up(){
        /*if(slide.getCurrentPosition() > MAX_SLIDE || reel.getCurrentPosition() > MAX_REEL){
            return;
        }*/
        slide.setPower(POWER);
        reel.setPower(POWER * RATIO);//7 over 4
    }

    public void down(){
        /*if(slide.getCurrentPosition() < MIN_SLIDE || reel.getCurrentPosition() < MIN_REEL){
            return;
        }*/

        slide.setPower(-POWER);
        reel.setPower(-POWER * RATIO);//7 over 4
    }

    public void setPower(double power){
        /*if(power > 0){
            if(slide.getCurrentPosition() > MAX_SLIDE || reel.getCurrentPosition() > MAX_REEL){
                return;
            }
        }else{
            if(slide.getCurrentPosition() < MIN_SLIDE || reel.getCurrentPosition() < MIN_REEL){
                return;
            }
        }*/

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
