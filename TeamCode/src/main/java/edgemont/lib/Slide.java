package edgemont.lib;

import static androidx.core.math.MathUtils.clamp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Slide {

    final double RATIO = 0.83;
    final double POWER = 1;
    final int MAX_SLIDE = (int) (11235), MAX_REEL = 0, MIN_SLIDE = 0, MIN_REEL = (int) (-11235);//(-10540);
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

        if(power > 0){
            slide.setPower(POWER / 2);
            reel.setPower(POWER / 2);
        }else{
            slide.setPower(POWER);
            reel.setPower(POWER * RATIO);
        }
    }

    public void setPos(int slidePos, int reelPos, boolean wait, double power) throws InterruptedException {
        if(slide.getCurrentPosition() > slidePos){
            reel.setPower(power);
            slide.setPower(power * RATIO);
        }else{
            slide.setPower(power);
            reel.setPower(power);
        }

        slide.setTargetPosition(slidePos);
        reel.setTargetPosition(reelPos);

        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        reel.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if(wait) {
            while (slide.isBusy() || reel.isBusy()) {
                Thread.sleep(10);
            }
        }
    }

    public void waitForFinish() throws InterruptedException {
        while (slide.isBusy() || reel.isBusy()) {
            Thread.sleep(10);
        }
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
