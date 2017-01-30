import java.util.Arrays;
import java.util.Random;
public class Individual {
	private double mutationP = 0.005;
	private double crossoverP = 0.5;
	private int[] genome;
	public Individual() {
		genome = new int[Main.genomeSize];
		Random random = new Random();
		for(int i = 0; i<Main.genomeSize; i++) {
			genome[i] = random.nextInt(2);
		}
		mutate();
	}
	public Individual(Individual toBeCopied) {
		genome = Arrays.copyOf(toBeCopied.getGenome(), toBeCopied.getGenome().length);
	}
	public Individual(Individual a, Individual b) {
		genome = new int[Main.genomeSize];
		Random random = new Random();
		for(int i = 0; i<Main.genomeSize; i++) {
			if(Math.random()<crossoverP) {
				genome[i] = a.getGenome()[i];
			} else {
				genome[i] = b.getGenome()[i];
			}
		}
		mutate();
	}
	public void printGenome() {
		for(int i = 0; i<genome.length; i++) {
			System.out.print(genome[i]);
		}
		System.out.print("  Fitness: "+this.getFitness());
		System.out.println();
	}
	public int[] getGenome() {
		return genome;
	}
	public void mutate() {
		for(int i = 0; i<genome.length; i++) {
			if(Math.random()<mutationP) {
				genome[i] = 1-genome[i];
			}
		}
	}
	public int getFitness() {
		return FitnessTester.getFitness(genome);
	}
}