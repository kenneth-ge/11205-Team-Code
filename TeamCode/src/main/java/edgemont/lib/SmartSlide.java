package edgemont.lib;

public class SmartSlide {

    Slide slide;
    Ramp ramp;
    Grabber grabber;

    final double power;

    public SmartSlide(Slide slide, Ramp ramp, Grabber grabber, double standardPower){
        this.slide = slide;
        this.ramp = ramp;
        this.grabber = grabber;
        this.power = standardPower;
    }

    public synchronized void lower() throws InterruptedException {
        //slide.setPos();
        ramp.lift();
    }

    public synchronized void middle(boolean wait) throws InterruptedException {
        slide.setPos(2033, wait, power);
    }

    public synchronized void high() throws InterruptedException {

    }

    public synchronized void reach(boolean wait) throws InterruptedException {
        slide.setPos((int)(slide.slidePos() * 1.15), wait, power);
    }

}
