
package Utility;


public class Data {
    public static int numBins(double[]weights) {
        int numBins = weights.length;
        return numBins;
    }
    public static int numItems(double[]weights) {
        int numItems = weights.length;
        return numItems;
    }
    public static double[] Weights() {
        double[] weights = {48, 30, 19, 36, 36, 27, 42, 42, 36, 24, 30};
        return weights;
    }
    
   public static double[] Capacities() {
        double[] binCapacities = {100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100};
        return binCapacities;
    }
}
