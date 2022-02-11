package edgemont.auto;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

public class Camera {
    
    final int WIDTH = 16, HEIGHT = 9;
    VuforiaLocalizer vuforia;
    int[] pixels = new int[WIDTH * HEIGHT];
    
    public Camera(HardwareMap hardwareMap){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AU85pjz/////AAABmRxaLmh4PE+xqfOXRBJO1At7Buw6VZVlgNerT6X+Vi/Ki9a8Dx4nm8p/GvcvDP+s4H3BP7/f1mTzMqIlGL79XMNq/o6joaur+45x/s67VLt/gG3e9s82CrQW+f63c48G95LeoqYKd7uGS3zSaD3fuTeFYILg7U3MF7LgPH0dQqJD/9taBrSgZpreNROWcb2ZIoxN9icgTk28CSq+SjQieYwOBwl85ZSUzCDug05UnazHXJXj21K1RIsi34dLFhhG/r34Nup6+6Te799BVQKty6kjyqxDqfvPz3zkCToniTqzUEUALvmANgplzjhhNBmNong3zS/AyvEWFHv7T5D2+8Xl6dMcGxm0APuDPnA5lXMI";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        vuforia.setFrameQueueCapacity(5);
        vuforia.enableConvertFrameToBitmap();
    }
    
    public Pixel[][] picture() throws InterruptedException {
        Pixel[][] pix = new Pixel[HEIGHT][WIDTH];
        
        VuforiaLocalizer.CloseableFrame frame = vuforia.getFrameQueue().take();

        Bitmap bm = vuforia.convertFrameToBitmap(frame);
        bm = Bitmap.createScaledBitmap(bm, WIDTH, HEIGHT, false);

        bm.getPixels(pixels, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm.getHeight());

        for(int i = 0; i < WIDTH * HEIGHT; i++){
            int row = i / bm.getWidth();
            int col = i % bm.getWidth();
            
            int color = pixels[i];

            float r = div255((color >> 16) & 0xff);
            float g = div255((color >>  8) & 0xff);
            float b = div255((color      ) & 0xff);

            pix[row][col] = new Pixel(r, g, b);
        }
        
        return pix;
    }
    
    float div255(float x){
        return x / 255f;
    }
}