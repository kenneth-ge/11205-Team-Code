package edgemont.auto.cameravision;

public class Pixel {

    public double r, g, b;
    public double h, s, v;

    public Pixel(double r, double g, double b){
        this.r = r;
        this.g = g;
        this.b = b;
        
        double[] hsv = RGBtoHSB(r, g, b);
        
        this.h = hsv[0] * 360;
        this.s = hsv[1];
        this.v = hsv[2];
    }
    
    double[] RGBtoHSB(double r, double g, double b){
        return RGBtoHSB((int) (r * 255), (int) (g * 255), (int) (b * 255));
    }

    double[] RGBtoHSB(int r, int g, int b) {
        
        double hue, saturation, brightness;
        double[] hsbvals = new double[3];
        
        int cmax = (r > g) ? r : g;
        if (b > cmax) cmax = b;
        int cmin = (r < g) ? r : g;
        if (b < cmin) cmin = b;

        brightness = ((double) cmax) / 255.0;
        if (cmax != 0)
            saturation = ((double) (cmax - cmin)) / ((double) cmax);
        else
            saturation = 0;
        if (saturation == 0)
            hue = 0;
        else {
            double redc = ((double) (cmax - r)) / ((double) (cmax - cmin));
            double greenc = ((double) (cmax - g)) / ((double) (cmax - cmin));
            double bluec = ((double) (cmax - b)) / ((double) (cmax - cmin));
            if (r == cmax)
                hue = bluec - greenc;
            else if (g == cmax)
                hue = 2.0f + redc - bluec;
            else
                hue = 4.0f + greenc - redc;
            hue = hue / 6.0f;
            if (hue < 0)
                hue = hue + 1.0f;
        }
        hsbvals[0] = hue;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }
    
    @Override
    public String toString(){
        return "(" + r + ", " + g + ", " + b + ")";
    }
    
}