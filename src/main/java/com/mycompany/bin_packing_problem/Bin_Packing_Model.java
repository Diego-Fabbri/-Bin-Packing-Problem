/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bin_packing_problem;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVarType;
import ilog.concert.IloObjective;
import ilog.concert.IloObjectiveSense;
import ilog.cplex.IloCplex;

/**
 *
 * @author diego
 */
public class Bin_Packing_Model {
     protected IloCplex model;

    protected int n;
    protected int m;
    protected double[] weights;
    protected double[] capacities;
 
    protected IloIntVar[][] x;
    protected IloIntVar[] y;

    Bin_Packing_Model(int numItems, int numBins, double[] weights, double[] capacities) throws IloException {
         this.n = numItems;
        this.m = numBins;
        this.weights = weights;
        this.capacities = capacities;
        this.model = new IloCplex();
        this.x = new IloIntVar[numItems][numBins];
        
        this.y= new IloIntVar[numBins];
    }
    
    //The following code creates the variables
    protected void addVariables() throws IloException {
//        As in the multiple knapsack example, you define an array of variables x[(i, j)], 
//        whose value is 1 if item i is placed in bin j, and 0 otherwise.
        for (int i = 0; i < n; i++) {

            for (int j = 0; j < m; j++) {

                x[i][j] = (IloIntVar) model.numVar(0, 1, IloNumVarType.Int, "x[" + i + "][" + j + "]");
            }
        }
//For bin packing, you also define an array of variables, y[j], 
//whose value is 1 if bin j is used—that is, if any items are packed in it—and 0 otherwise. The sum of the y[j] will be the number of bins used.
        for (int j = 0; j < m; ++j) {
            y[j] = (IloIntVar) model.numVar(0, 1, IloNumVarType.Int, "y[" + j + "]");
        }

    }
    //The following code defines the constraints for the problem
     protected void addConstraints() throws IloException {
//Each item must be placed in exactly one bin.
//This constraint is set by requiring that the sum of x[i][j] over all bins j is equal to 1.
//Note that this differs from the multiple knapsack problem, in which the sum is only required to be less than or equal to 1,
//because not all items have to be packed.

        for (int i = 0; i < n; i++) {
            IloLinearNumExpr expr_1 = model.linearNumExpr();
            for (int j = 0; j < m; j++) {
                expr_1.addTerm(x[i][j], 1);
            }
            model.addEq(expr_1, 1);
        }
//The total weight packed in each bin can't exceed its capacity.
// This is the same constraint as in the multiple knapsack problem,
// but in this case you add the bin capacity on the left side of the inequalities by -y[j]
        

        for (int j = 0; j < m; j++) {
            IloLinearNumExpr expr_2 = model.linearNumExpr();
            expr_2.addTerm(y[j],- capacities[j]);
            for (int i = 0; i < n; i++) {
                expr_2.addTerm(x[i][j], weights[i]);
                 
            }
            
            model.addLe(expr_2, 0);
        }
    }
//The following code creates the objective function for the problem.
    protected void addObjective() throws IloException {
        IloLinearNumExpr objective = model.linearNumExpr();

       
            for (int j = 0; j < m; j++) {
                objective.addTerm(y[j], 1);

           
        }

        IloObjective Obj = model.addObjective(IloObjectiveSense.Minimize, objective);
    }
    public void solveModel() throws IloException {
        addVariables();
        addObjective();
        addConstraints();
        model.exportModel("Bin_Packing_Problem.lp");

        model.solve();

        if (model.getStatus() == IloCplex.Status.Feasible
                | model.getStatus() == IloCplex.Status.Optimal) {
            System.out.println();
            System.out.println("Solution status = " + model.getStatus());
            System.out.println();
            System.out.println("Number of bins used: " + model.getObjValue());
            double totalWeight = 0;
            for (int j = 0; j < m; ++j) {
                if (model.getValue(y[j]) == 1) {
                    System.out.println("\nBin " + j + "\n");
                    double binWeight = 0;
                    for (int i = 0; i < n; ++i) {
                        if (model.getValue(x[i][j]) == 1) {
                            System.out.println("Item " + i + " - weight: " + weights[i]);
                            binWeight += weights[i];
                        }
                    }
                    System.out.println("Packed bin weight: " + binWeight);
                    totalWeight += binWeight;
                }
            }
            System.out.println("\nTotal packed weight: " + totalWeight);
        } else {
            System.out.println("The problem status is: " + model.getStatus());
        }
    }

}
