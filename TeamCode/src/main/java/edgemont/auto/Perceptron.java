package edgemont.auto;

public class Perceptron {

    final double w1, w2;

    public Perceptron(double w1, double w2){
        this.w1 = w1;
        this.w2 = w2;
    }

    public double getOutput(double x1, double x2){
        double wx, y1;

        wx = (x1 * w1) + (x2 * w2);

        y1 = Math.tanh(wx);

        return y1;
    }

}
