package GeneticCircles;

import java.awt.geom.Ellipse2D;

public class FitnessTester{
	//For now, solution is 11111111...
	public static int[] solution = new int[CircleFitting.genomeSize];
	public static int getFitness(int[] genome, Ellipse2D.Double body) {
		int fitness = 0;
		for(int i = 0; i<solution.length; i++) {
			fitness -= Math.abs(solution[i]-genome[i]);
		}
		return fitness;
	}
}