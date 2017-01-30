package GeneticCircles;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;

import java.util.Random;
public class Individual {
	private double mutationP = 0.03;
	private double crossoverP = 0.5;
	private int[] genome;
	public Individual() {
		genome = new int[CircleFitting.genomeSize];
		Random random = new Random();
		for(int i = 0; i<CircleFitting.genomeSize; i++) {
			genome[i] = random.nextInt(2);
		}
		mutate();
	}
	public Individual(Individual toBeCopied) {
		genome = Arrays.copyOf(toBeCopied.getGenome(), toBeCopied.getGenome().length);
	}
	public Individual(Individual a, Individual b) {
		genome = new int[CircleFitting.genomeSize];
		Random random = new Random();
		for(int i = 0; i<CircleFitting.genomeSize; i++) {
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
//		for(int i = 0; i<24; i++) {
//			if(Math.random()<mutationP) {
//				genome[i] = 1-genome[i];
//			}
//		}
		for(int i = 11; i>=0; i--) {
			if(Math.random() < mutationP) {
				while(i>=0) {
					genome[i] = 1-genome[i];
					i--;
				}
			}
		}
		for(int i = 23; i>=12; i--) {
			if(Math.random() < mutationP) {
				while(i>=12) {
					genome[i] = 1-genome[i];
					i--;
				}
			}
		}
		for(int i = genome.length-1; i>23; i--) {
			if(Math.random() < mutationP) {
				while(i>=24) {
					genome[i] = 1-genome[i];
					i--;
				}
			}
		}
		for(int i = 0; i<genome.length; i++) {
			if(Math.random()<mutationP/5) {
				genome[i] = 1-genome[i];
			}
		}
	}
	
	public Ellipse2D.Double getCircle() {
		double[] circleArray = readBinary();
		double d = circleArray[2];
		return new Ellipse2D.Double(circleArray[0]-d/2, circleArray[1]-d/2, d, d);
	}
	
	//returns int[] that gives x, y, radius of circle from genome
	public double[] readBinary() {
		double x = 0; double y = 0; double r = 0;
		for(int i = 0; i<12; i++) {
			x+= Math.pow(2, i-2)*genome[i];		
		}
		for(int i = 12; i<24; i++) {
			y+= Math.pow(2, i-14)*genome[i];		
		}
		for(int i = 24; i<36; i++) {
			r+= Math.pow(2, i-26)*genome[i];		
		}
		return new double[] {x,y,r};
	}
	
	public double getFitness() {
		double fitness = 0;
		boolean intersectsWall = false;
		int intersections = 0;
		
		double x = getCircle().getCenterX();
		double y = getCircle().getCenterY();
		double r= getCircle().width/2;
		for(int i = 0; i<CircleFitting.circles.size(); i++) {
			Ellipse2D.Double circle = CircleFitting.circles.get(i);
			double cx = circle.getCenterX();
			double cy = circle.getCenterY();
			double cr = circle.width/2;
//			//if squared distance between < radius
//			Area a = new Area(getCircle());
//			Area b = new Area(circle);
//			a.intersect(b);
//			if(!a.isEmpty()) intersections+=1;
			if((cx-x)*(cx-x)+(cy-y)*(cy-y) < (r+cr)*(r+cr)) {
				intersections+=1;
			}
		}
		if(getCircle().x < 0 || getCircle().y<0 
				|| getCircle().getMaxX()>1024 || getCircle().getMaxY()>1024) {
			intersectsWall = true;
		}
		if(intersectsWall) {
			fitness = 0;
		} else {
			fitness = r* Math.pow(1/3, intersections);
		}
		return fitness;
	}
	public boolean intersects(Ellipse2D.Double b) {
		return false;
	}
	public void draw(Graphics2D g) {
		g.setColor(Color.red);
		g.fill(getCircle());
	}
}