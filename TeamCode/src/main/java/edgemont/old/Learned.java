package edgemont.old;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import edgemont.auto.Camera;

@Autonomous(name="Learned", group="Test")
public class Learned extends LinearOpMode {
    
    @Override
    public void runOpMode() throws InterruptedException{
    
        Camera camera = new Camera(hardwareMap);
        waitForStart();
    }
    
    /*public int scan() throws InterruptedException {
        Pixel[][] image = camera.picture();
        
        Pixel topPixel = image[0][0];
        
        int left = 0;
        int right = 0;
        int middle = 0;
        
        double width = image[0].length;
        
        telemetry.addData("width", image[0].length);
        telemetry.addData("height", image.length);
        
        for(int j = 0; j<image[0].length; j++) {
            for (int i=0; i<image.length; i++) {
                Pixel current = image[i][j];
                if (current.h >= 10 && current.h <= 37 && current.s >= 0.60) {
                    if(j<=width / 3.0){
                            left+=1;
                    }else if(j <= 2.0 * width / 3.0){
                            middle+=1;
                    }else{
                            right+=1;
                    }
                }
            }
        }
        telemetry.addData("left", left);
        telemetry.addData("middle", middle);
        telemetry.addData("right", right);
        if(left > middle && left > right){
            return 1;
        }
        else if(middle > left && middle > right){
            return 2;
        }
        else{
            return 3;
        }
        
        /*double percentage = ((double) count)/((double) image.length*image[0].length);
        
        
        if(percentage<0.05){
            telemetry.addData("Rings", 0);
        }else if (percentage<=0.2){
            telemetry.addData("Rings", 1);
        } else{
            telemetry.addData("Rings", 4);
        }
        telemetry.addData("Percentage", percentage);
/*        double avgR = sumR/((double) image.length*image[0].length);
        double avgG = sumG/((double) image.length*image[0].length);
        double avgB = sumB/((double) image.length*image[0].length);
        
        Pixel avg = new Pixel(avgR, avgG, avgB);
        if (avg.h >= 10 && avg.h <= 37 && avg.s >= 0.60) {
            telemetry.addData("Color is orange",avg.h);
            telemetry.addData("Saturation", avg.s);

        }
        else{
            telemetry.addData("Color is not orange",avg.h);
            telemetry.addData("Saturation", avg.s);
        }
        //telemetry.update();*/
        
        /*for(Pixel[] p: image){
            telemetry.addData("", Arrays.deepToString(p));
        }
    
        telemetry.addData("Telemetry data", "New data");
        telemetry.update();
        
        Thread.sleep(30000);
    }*/

}