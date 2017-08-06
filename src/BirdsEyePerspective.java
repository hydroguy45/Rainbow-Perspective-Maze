

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class BirdsEyePerspective extends JPanel {
	public float playerX;
	public float playerY;
	public float bearing;
	private final Set<Line> map;
	public final float maxX;
	public final float maxY;
	public final Boolean[][] initMap;
	private final float[] goalPoint;
	
	private boolean collides(float x, float y) {
		// make this compare to the map
		int i = (int) Math.floor(x);
		int j = (int) Math.floor(y);
		return initMap[i][j];
	}
	private boolean inGoalZone(){
		float goalX = goalPoint[1];
		float goalY = goalPoint[0];
		if((goalX-0.5 < playerX)&&(goalX+0.5 > playerX)){
			if((goalY-0.5 < playerY)&&(goalY+0.5 > playerY)){
				return true;
			}
		}
		return false;
	}
	public BirdsEyePerspective(float x, float y, float degrees, Boolean[][] seed, Boolean toBeDisplayed, float[] goal) {
		
		if (toBeDisplayed) {
			setFocusable(true);
			addKeyListener(new KeyAdapter() {
				private Set<Integer> keysHeldDown = new TreeSet<Integer>();
				public void keyReleased(KeyEvent e) {
					keysHeldDown.remove(e.getKeyCode());
				};
				public void keyPressed(KeyEvent e) {
					if(inGoalZone()){
						System.out.println("Congrats!");
						Game.requestedMode = Game.Mode.CONGRATS;
					}
					System.out.println("("+playerX+","+playerY+") to ("+goalPoint[0]+","+goalPoint[1]+")");
					keysHeldDown.add(e.getKeyCode());
					if (keysHeldDown.contains(KeyEvent.VK_ESCAPE)){
						Game.requestedMode = Game.Mode.INITIAL;
					}
					if (keysHeldDown.contains(KeyEvent.VK_LEFT)) {
						bearing += 5;
					}
					if (keysHeldDown.contains(KeyEvent.VK_RIGHT)) {
						bearing -= 5;
					}
					if (keysHeldDown.contains(KeyEvent.VK_DOWN)) {

						float projectedY = (float) (playerY - Math.cos(Math.toRadians(bearing)) * 0.05);
						float projectedX = (float) (playerX - Math.sin(Math.toRadians(bearing)) * 0.05);
						if (!collides(projectedX, projectedY)) {
							playerY = projectedY;
							playerX = projectedX;
						}
					}
					if (keysHeldDown.contains(KeyEvent.VK_UP)) {
						float projectedY = (float) (playerY + Math.cos(Math.toRadians(bearing)) * 0.05);
						float projectedX = (float) (playerX + Math.sin(Math.toRadians(bearing)) * 0.05);
						if (!collides(projectedX, projectedY)) {
							playerY = projectedY;
							playerX = projectedX;
						}
					}
					// System.out.println(bearing);
				}
			});
		}
		goalPoint = goal;
		initMap = seed;
		playerX = x;
		playerY = y;
		bearing = degrees;
		// To clarify seed[x][y] not the other way around... I have a feeling
		// this is gonna
		// be on of the early bugs
		maxX = seed.length;
		maxY = seed[0].length;
		// TODO:construct map and set maxX and maxY
		map = new TreeSet<Line>();
		for (float i = 0; i < seed.length; i++) {
			for (float j = 0; j < seed[0].length; j++) {
				if (seed[(int) i][(int) j]) {
					Color c = Line.appropriateColorValue(i + 0.5f, j, maxX, maxY);
					map.add(new Line(i, j, i + 1, j, c));
					c = Line.appropriateColorValue(i, j + 0.5f, maxX, maxY);
					map.add(new Line(i, j, i, j + 1, c));
					c = Line.appropriateColorValue(i + 1, j + 0.5f, maxX, maxY);
					map.add(new Line(i + 1, j, i + 1, j + 1, c));
					c = Line.appropriateColorValue(i + 0.5f, j + 1, maxX, maxY);
					map.add(new Line(i, j + 1, i + 1, j + 1, c));
				}
			}
		}
		Timer timer = new Timer(2, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		timer.start();
	}

	public Set<Line> getMap() {
		return map;
	}

	public void paintComponent(Graphics g) {
		//Display Structure
		int width = getWidth();
		int height = getHeight();
		g.clearRect(0, 0, width, height);
		for (int i = 0; i < initMap.length; i++) {
			for (int j = 0; j < initMap.length; j++) {
				if (initMap[i][j]) {
					g.setColor(Color.darkGray);
				} else {
					g.setColor(Color.lightGray);
				}
				g.fillRect((int) (width / maxX) * i + 1, (int) (height / maxY) * j + 1, (int) (width / maxX), (int) (height / maxY));
			}
		}
		//Display Walls
		Graphics2D modernG = (Graphics2D) g;
		//g lines are too thin, so Graphics2D to the rescue for thicker lines!
		modernG.setStroke(new BasicStroke(4));
		for (Line l : map) {
			int displayX1 = (int) ((l.x1 / maxX) * width);
			int displayY1 = (int) ((l.y1 / maxY) * height);
			int displayX2 = (int) ((l.x2 / maxX) * width);
			int displayY2 = (int) ((l.y2 / maxY) * height);
			modernG.setColor(l.c);
			modernG.drawLine(displayX1, displayY1, displayX2, displayY2);
		}
		modernG.setStroke(new BasicStroke(2));
		//Display player
		g.setColor(Color.BLACK);
		int radius = 10;
		g.fillOval((int) ((playerX / maxX) * width - radius), (int) ((playerY / maxY) * height - radius), radius * 2,
				radius * 2);
		int projectedX = (int) (Math.sin(Math.toRadians(bearing)) * 20.0 + (playerX / maxX) * width);
		int projectedY = (int) (Math.cos(Math.toRadians(bearing)) * 20.0 + (playerY / maxY) * height);
		g.drawLine((int) ((playerX / maxX) * width), (int) ((playerY / maxY) * height), projectedX, projectedY);
		//Display Target Color
		g.setColor(Line.appropriateColorValue(goalPoint[1], goalPoint[0], maxX, maxY));
		g.fillRect(5*width/6, 0, width/6, height/6);
	}
}
