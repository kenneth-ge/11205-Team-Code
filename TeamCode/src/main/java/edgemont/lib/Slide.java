package edgemont.lib;

import static androidx.core.math.MathUtils.clamp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slide {

    final double RATIO = 1;
    final double POWER = 1;
    final int MAX_SLIDE = (int) (10500 * 1.07), MAX_REEL = 0, MIN_SLIDE = 0, MIN_REEL = (int) (-10100 * 1.07);
    final double SLIDE_OVER_REEL = ((double) MIN_SLIDE) / ((double) MIN_REEL);
    public DcMotor slide, reel;
    Grabber grabber;

    public Slide(HardwareMap hardwareMap, Grabber grabber){
        this.grabber = grabber;

        slide = hardwareMap.get(DcMotor.class, "slide");
        reel = hardwareMap.get(DcMotor.class, "reel");

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        reel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        /*slide.setPower(POWER);
        slide.setTargetPosition(0);
        reel.setPower(POWER * RATIO);
        reel.setTargetPosition(0);

        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        reel.setMode(DcMotor.RunMode.RUN_TO_POSITION);*/
    }

    public void up(){
        setPower(POWER);
    }

    public void down(){
        setPower(-POWER);
    }

    public void setPower(final double power){
        if(usePos()){
            setPowerPos(power);
            return;
        }

        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        reel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if(power > 0){
            if(slide.getCurrentPosition() <= MAX_SLIDE){
                slide.setPower(-power);//7 over 4
            }else{
                slide.setPower(0);
            }

            reel.setPower(power * RATIO);//7 over 4
        }else{
            if(slide.getCurrentPosition() >= MIN_SLIDE){
                slide.setPower(-power);//7 over 4
            }else{
                slide.setPower(0);
            }

            reel.setPower(power * RATIO);//7 over 4
        }
    }

    void setPowerPos(double power){
        if(power > 0 && slide.getCurrentPosition() < 3000){
            if(!grabber.grabbing)
                grabber.looseGrab();
        }

        int posSlide = (int) (-power * MAX_SLIDE / 10.);
        int posReel = (int) (-power * MIN_REEL / 10.);

        slide.setTargetPosition(clamp(slide.getCurrentPosition() + posSlide, MIN_SLIDE, MAX_SLIDE));
        reel.setTargetPosition(clamp(reel.getCurrentPosition() + posReel, MIN_REEL, MAX_REEL));

        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        reel.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        slide.setPower(POWER);
        reel.setPower(POWER * RATIO);
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

    boolean usePos(){
        return true;
    }

}
