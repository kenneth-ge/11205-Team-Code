package edgemont.lib;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Carousel {

    DcMotor carousel;

    public Carousel(HardwareMap hardwareMap){
        carousel = hardwareMap.get(DcMotor.class, "carousel");
    }

    /** Spin leftward to remove one duck */
    public void leftDuck() throws InterruptedException {
        carousel.setPower(-1);
        Thread.sleep(3000);
    }

    /** Spin rightward to remove one duck */
    public void rightDuck() throws InterruptedException {
        carousel.setPower(1);
        Thread.sleep(3000);
    }

}
