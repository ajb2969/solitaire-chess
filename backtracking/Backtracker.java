package backtracking;/*
 * backtracking.Backtracker.java
 *
 * This file comes from the backtracking lab. It should be useful
 * in this project. A second method has been added that you should
 * implement.
 */

import model.SoltrChessModel;

import java.lang.management.OperatingSystemMXBean;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

/**
 * This class represents the classic recursive backtracking algorithm.
 * It has a solver that can take a valid configuration and return a
 * solution, if one exists.
 * 
 * @author sps (Sean Strout @ RIT CS)
 * @author jeh (James Heliotis @ RIT CS)
 */
public class Backtracker {
    
    /**
     * Try find a solution, if one exists, for a given configuration.
     * 
     * @param config A valid configuration
     * @return A solution config, or null if no solution
     */
    public Stack<Configuration> solve(Configuration config) {
        Stack<Configuration> solutionStack = new Stack<Configuration>();
        if (config.isGoal()) {
            solutionStack.push(config);
            return solutionStack;
        } else {
            for (Configuration child : config.getSuccessors()) {
                System.out.println(config.getSuccessors().size());
                ((SoltrChessModel)child).printBoard();
                if (child.isValid()) {
                    Stack<Configuration> k  = solve(child);
                    if (k!=null) {
                        k.push(child);
                        return k;
                    }
                }
            }
            // implicit backtracking happens here
        }
        return null;
    }

    /**
     * Find a goal configuration if it exists, and how to get there.
     * @param current the starting configuration
     * @return a list of configurations to get to a goal configuration.
     *         If there are none, return null.
     */
    public Stack< Configuration > solveWithPath( Configuration current ) {
        Stack<Configuration> solutionStack = new Stack<Configuration>();
        if (current.isGoal()) {
            solutionStack.push(current);
            return solutionStack;
        } else {
            for (Configuration child : current.getSuccessors()) {
                if (child.isValid()) {
                    Stack<Configuration> k  = solve(child);
                    if (k!=null) {
                        k.push(child);
                        return k;
                    }
                }
            }
            // implicit backtracking happens here
        }
        return null;
    }
}
