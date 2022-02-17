package edgemont.lib;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Carousel implements Runnable {

    boolean left = false;
    boolean right = false;
    DcMotor carousel;
    LinearOpMode opMode;

    public Carousel(HardwareMap hardwareMap, LinearOpMode opMode){
        this.opMode = opMode;

        carousel = hardwareMap.get(DcMotor.class, "carousel");
    }

    public void threadStart(){
        new Thread(this).start();
    }

    /** Spin leftward to remove one duck */
    public void leftDuck() throws InterruptedException {
        left = true;
    }

    /** Spin rightward to remove one duck */
    public void rightDuck() throws InterruptedException {
        right = true;
    }

    @Override
    public void run() {
        while(opMode.opModeIsActive()){
            if(right) {
                carousel.setPower(-1);
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                carousel.setPower(0);

                left = right = false;
            }
            if(left){
                carousel.setPower(1);
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                carousel.setPower(0);

                left = right = false;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
