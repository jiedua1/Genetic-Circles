import java.util.Arrays;
import java.util.Collections;

public class Main {
	//genome size variable, right now 16. Genome is 0000000000
	public static int genomeSize = 64;
	public int generation;
	public int maxGen;
	//keep this even for tournament selection
	public int populationSize = 16;
	public Individual[] individuals;
	
	public static void main(String[] args) {
		Main main = new Main();
		main.init();
		for(Individual i: main.individuals) {
			//i.printGenome();
		}
		main.printBestIndividual();
		for(int i = 0; i<100; i++) {
			main.iterateGeneration();
			main.printBestIndividual();
		}
	}
	public Main() {
		generation = 0;
		maxGen = 10;
	}
	//Initializes the individuals
	public void init() {
		individuals = new Individual[populationSize];
		for(int i = 0; i<individuals.length; i++) {
			individuals[i] = new Individual();
		}
	}
	public void iterateGeneration() {
		Individual[] temp = new Individual[populationSize/2];
		Collections.shuffle(Arrays.asList(temp));
		//does comparing between values of individual, half of individuals with worst fitness is eliminated
		for(int i =0; i<temp.length; i++) {
			if(individuals[2*i].getFitness()>individuals[2*i+1].getFitness()) {
				temp[i] = new Individual(individuals[2*i]);
			} else {
				temp[i] = new Individual(individuals[2*i+1]);
			}
		}
		for(int i = 0; i<individuals.length; i++) {
			individuals[i] = new Individual(temp[(int)(temp.length*Math.random())],temp[(int)(temp.length*Math.random())]);
		}
		generation += 1;
	}
	public void printBestIndividual() {
		int fitness = individuals[0].getFitness();
		int index = 0;
		for(int i = 1; i<individuals.length; i++) {
			if(individuals[i].getFitness()>fitness) {
				fitness = individuals[i].getFitness();
				index = i;
			}
		}
		individuals[index].printGenome();
		System.out.print("Generation: "+generation+"  ");
		System.out.println("Best Fitness: "+fitness+"  Index: " + index);
		
	}
	
}
