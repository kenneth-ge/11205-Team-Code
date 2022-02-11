package edgemont.lib;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Grabber {

    boolean grabbing = false;
    Servo grabber;

    public Grabber(HardwareMap hardwareMap){
        grabber = hardwareMap.get(Servo.class, "grabber");
        grabber.setPosition(0);
    }

    public void release(){
        grabbing = false;
        grabber.setPosition(0);
    }

    public void grab(){
        grabbing = true;
        grabber.setPosition(0.2);
    }

    public void toggle(){
        if(grabbing)
            release();
        else
            grab();
    }

    public double pos(){
        return grabber.getPosition();
    }

}
