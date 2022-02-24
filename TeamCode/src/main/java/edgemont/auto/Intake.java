package edgemont.auto;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    public final double POWER = -0.8;
    public DcMotor intake;

    public Intake(HardwareMap hardwareMap){
        intake = hardwareMap.dcMotor.get("intake");
        intake.setDirection(DcMotor.Direction.FORWARD);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void in(){
        intake.setPower(POWER);
    }

    public void out(){
        intake.setPower(-POWER * 0.7);
    }

    public void outSlow(){
        intake.setPower(-POWER / 2.75);
    }

    public void stop(){
        intake.setPower(0);
    }

}
