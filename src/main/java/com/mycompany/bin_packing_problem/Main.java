
package com.mycompany.bin_packing_problem;

import Utility.Data;
import ilog.concert.IloException;
import java.io.FileNotFoundException;
import java.io.PrintStream;


public class Main {
public static void main(String[] args) throws IloException, FileNotFoundException{
     System.setOut(new PrintStream("Bin_Packing_Problem.log"));
     double[] weights = Data.Weights();
   int numItems = Data.numItems(weights);
   double [] capacities= Data.Capacities();
   int numBins = Data.numBins(weights);
   
   Bin_Packing_Model model = new Bin_Packing_Model (numItems,numBins,weights,capacities);
   model.solveModel();
   
} 
}
