package edgemont.lib;

import static androidx.core.math.MathUtils.clamp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Slide {

    final double RATIO = 0.83;
    final double POWER = 0.3;
    public static final int MAX_SLIDE = (int) (4350), MIN_SLIDE = 0;
    public DcMotor slide;
    Grabber grabber;

    public Slide(HardwareMap hardwareMap, Grabber grabber){
        this.grabber = grabber;

        slide = hardwareMap.get(DcMotor.class, "slide");

        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void up(){
        setPower(POWER);
    }

    public void down(){
        setPower(-POWER);
    }

    public void setPower(final double power){
        if(power > 0 && slide.getCurrentPosition() < 1200 && slide.getCurrentPosition() > 250){
            if(!grabber.grabbing)
                grabber.looseGrab();
        }

        int posSlide = (int) (-power * MAX_SLIDE / 10.);

        slide.setTargetPosition(clamp(slide.getCurrentPosition() + posSlide, MIN_SLIDE, MAX_SLIDE));

        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if(power > 0){
            slide.setPower(power / 2);
        }else{
            slide.setPower(power);
        }
    }

    public void setPos(int slidePos, boolean wait, double power) throws InterruptedException {
        if(slide.getCurrentPosition() > slidePos){
            slide.setPower(power * RATIO);
        }else{
            slide.setPower(power);
        }

        slide.setTargetPosition(slidePos);

        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if(wait) {
            while (slide.isBusy()) {
                Thread.sleep(10);
            }
        }
    }

    public void waitForFinish() throws InterruptedException {
        while (slide.isBusy()) {
            Thread.sleep(10);
        }
    }

    public void stop(){
        slide.setPower(0);
    }

    public int slidePos(){
        return slide.getCurrentPosition();
    }

}
