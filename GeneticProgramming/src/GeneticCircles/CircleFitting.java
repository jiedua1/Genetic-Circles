package GeneticCircles;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CircleFitting extends JPanel
		implements ActionListener, MouseListener, MouseWheelListener, MouseMotionListener {
	// genome size variable, right now 16. Genome is 0000000000
	public static int genomeSize = 36;
	public int generation;
	public int maxGen;
	// keep this even for tournament selection
	public int populationSize = 40;
	public Individual[] individuals;
	private Timer timer;
	// number of randomized circles
	private int nCircles = 40;

	// size of current circle to be added
	private double tempCircleX = 0;
	private double tempCircleY = 0;
	private double tempCircleRadius = 30;

	public static int width = 1024;
	public static int height = 1024;
	public static ArrayList<Ellipse2D.Double> circles;

	//is left mouse button down?
	private boolean mouseDown = false;

	public synchronized static void main(String[] args) {

		CircleFitting main = new CircleFitting();

		JFrame frame = new JFrame();
		frame.getContentPane().add(main, BorderLayout.CENTER);
		frame.setSize(1200, 1200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println(frame.getSize());
		frame.setVisible(true);

		frame.getContentPane().addMouseListener(main);
		frame.getContentPane().addMouseMotionListener(main);
		frame.getContentPane().addMouseWheelListener(main);

		Graphics g = main.getGraphics();

		for (Individual i : main.individuals) {
			// i.printGenome();
		}
		main.printBestIndividual();
		if (main.generation > 100) {
			main.stopTimer();
		}
	}

	private void stopTimer() {
		timer.stop();
	}

	public CircleFitting() {
		generation = 0;
		maxGen = 10;
		init();
		// addMouseListener(new MouseAdapter() {
		// @Override
		// public void mousePressed(MouseEvent arg0) {
		// generation = 0;
		// circles.add(new Ellipse2D.Double(arg0.getX()-25, arg0.getY()-25, 50,
		// 50));
		// }
		// @Override
		// public void mouseReleased(MouseEvent arg0) {
		// generation = 0;
		// System.out.println("New Circle of radius " + newCircleRadius + " at
		// (" + tempCircleX+ "," + tempCircleY+")");
		// circles.add(new Ellipse2D.Double(arg0.getX()-25, arg0.getY()-25, 50,
		// 50));
		//
		// }
		// });

	}

	// Initializes the individuals
	public void init() {
		Random random = new Random(5);
		//remove this seed for standardized testing
		//Seed 5 gives good results
		generation = 0;
		timer = new Timer(1, this);
		timer.start();
		circles = new ArrayList<Ellipse2D.Double>();
		individuals = new Individual[populationSize];
		for (int i = 0; i < individuals.length; i++) {
			individuals[i] = new Individual();
		}
		for (int i = 0; i < nCircles; i++) {
			double r = 20 + random.nextDouble() * 100;
			circles.add(new Ellipse2D.Double(random.nextDouble() * width, random.nextDouble() * height, r, r));
		}
	}

	public void iterateGeneration() {
		Individual[] temp = new Individual[populationSize / 2];
		Collections.shuffle(Arrays.asList(temp));
		// does comparing between values of individual, half of individuals with
		// worst fitness is eliminated
		for (int i = 0; i < temp.length; i++) {
			if (individuals[2 * i].getFitness() > individuals[2 * i + 1].getFitness()) {
				temp[i] = new Individual(individuals[2 * i]);
			} else {
				temp[i] = new Individual(individuals[2 * i + 1]);
			}
		}
		individuals[0] = new Individual(getBestIndividual());
		for (int i = 1; i < individuals.length; i++) {
			individuals[i] = new Individual(temp[(int) (temp.length * Math.random())],
					temp[(int) (temp.length * Math.random())]);
		}
		generation += 1;
	}

	public void printBestIndividual() {
		double fitness = individuals[0].getFitness();
		int index = 0;
		for (int i = 1; i < individuals.length; i++) {
			if (individuals[i].getFitness() > fitness) {
				fitness = individuals[i].getFitness();
				index = i;
			}
		}
		individuals[index].printGenome();
		printArray(individuals[index].readBinary());
		System.out.print("Generation: " + generation + "  ");
		System.out.println("Best Fitness: " + fitness + "  Index: " + index);

	}

	public Individual getBestIndividual() {
		double fitness = individuals[0].getFitness();
		int index = 0;
		for (int i = 1; i < individuals.length; i++) {
			if (individuals[i].getFitness() > fitness) {
				fitness = individuals[i].getFitness();
				index = i;
			}
		}
		return individuals[index];
	}

	public void drawBestIndividual(Graphics2D g) {
		double fitness = individuals[0].getFitness();
		int index = 0;
		for (int i = 1; i < individuals.length; i++) {
			if (individuals[i].getFitness() > fitness) {
				fitness = individuals[i].getFitness();
				index = i;
			}
		}
		individuals[index].draw(g);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g.drawRect(0, 0, width, height);
		for(Ellipse2D.Double circle: circles) {
			g.setColor(Color.blue);
			g2d.fill(circle);
		}
		drawBestIndividual((Graphics2D)g);
		
		g.setColor(Color.black);
		g2d.setStroke(new BasicStroke(4));
		if(mouseDown ==false) {
			g2d.draw(new Ellipse2D.Double(tempCircleX-tempCircleRadius, tempCircleY-tempCircleRadius,
					2*tempCircleRadius,2*tempCircleRadius));
		} else {
			circles.remove(circles.size()-1);
			g2d.fill(new Ellipse2D.Double(tempCircleX-tempCircleRadius, tempCircleY-tempCircleRadius,
					2*tempCircleRadius,2*tempCircleRadius));
			circles.add(new Ellipse2D.Double(tempCircleX-tempCircleRadius, tempCircleY-tempCircleRadius,
					2*tempCircleRadius,2*tempCircleRadius));
		}
		//}
	}

	public void printArray(double[] ds) {
		for (double i : ds) {
			System.out.print(i + ",");
		}
		System.out.println();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		iterateGeneration();
		if(generation%5 == 0) repaint();
		if (generation % 200 == 0)
			printBestIndividual();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		tempCircleX = e.getX();
		tempCircleY = e.getY();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton()==1) {
			circles.add(new Ellipse2D.Double(tempCircleX-tempCircleRadius, tempCircleY-tempCircleRadius,
					2*tempCircleRadius,2*tempCircleRadius));
			mouseDown = true;
		}
		if (e.getButton() == 3) {
			tempCircleX = e.getX();
			tempCircleY = e.getY();
			
//			circles.add(new Ellipse2D.Double(e.getX() - 25, e.getY() - 25, 50, 50));
		} else if (e.getButton() == 2) {
			nCircles = 0;
			init();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//left click only
		if(e.getButton()==1) {
			mouseDown = false;
			circles.remove(circles.size()-1);
		}
		if(tempCircleRadius>0 && e.getButton() ==3) {
			mouseDown = false;
			System.out.println(
					"New Circle of radius " + tempCircleRadius + " at (" + tempCircleX + "," + tempCircleY + ")");
			System.out.println();
			generation = 0;
			circles.add(new Ellipse2D.Double(tempCircleX-tempCircleRadius, tempCircleY-tempCircleRadius,
					2*tempCircleRadius,2*tempCircleRadius));
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		tempCircleRadius -= 10*e.getPreciseWheelRotation();
		if(tempCircleRadius<0) tempCircleRadius= 0;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		tempCircleX = e.getX();
		tempCircleY = e.getY();
	}

}
