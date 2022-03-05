package edgemont.lib;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Ramp {

    Servo ramp;

    public Ramp(HardwareMap hardwareMap){
        this.ramp = hardwareMap.get(Servo.class, "ramp");
        retract();
    }

    public void retract(){
        ramp.setPosition(0.04);
    }

    public void lift(){
        ramp.setPosition(0.4);
    }

}
