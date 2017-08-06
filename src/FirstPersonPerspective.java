

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JPanel;
import javax.swing.Timer;

public class FirstPersonPerspective extends JPanel {
	private boolean debugMode = false;
	public float playerX;
	public float playerY;
	public float bearing;
	public boolean mapInclusion;
	private Set<Line> map;
	private BirdsEyePerspective seeder;
	private float degreeCutOff = 45;
	private Boolean[][] booleanArray;
	public final float resolution = 500;
	public final float degreeBounds = 45;
	private final float[] goalPoint;
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
	public FirstPersonPerspective(float x, float y, float degrees, Boolean[][] seed, boolean includeMap, float[] goal) {
		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			private Set<Integer> keysHeldDown = new TreeSet<Integer>();
			public void keyReleased(KeyEvent e) {
				keysHeldDown.remove(e.getKeyCode());
			};
			public void keyPressed(KeyEvent e) {
				System.out.println("("+playerX+","+playerY+") to ("+goalPoint[0]+","+goalPoint[1]+")");
				if(inGoalZone()){
					System.out.println("Congrats!");
					Game.requestedMode = Game.Mode.CONGRATS;
				}
	//checking the pressed key only gets ride of multiple key presses...
	//so I create a set that only removes keys after a release... but
	//it creates a pause whenever a new press is added
				keysHeldDown.add(e.getKeyCode());
				if (keysHeldDown.contains(KeyEvent.VK_ESCAPE)){
					Game.requestedMode = Game.Mode.INITIAL;
				}
				if (keysHeldDown.contains(KeyEvent.VK_LEFT)) {
					bearing -= 3;
				}
				if (keysHeldDown.contains(KeyEvent.VK_RIGHT)) {
					bearing += 3;
				}
				if (keysHeldDown.contains(KeyEvent.VK_SEMICOLON)) {
					debugMode = !debugMode;
				}
				if (keysHeldDown.contains(KeyEvent.VK_DOWN)) {

					float projectedY = (float) (playerY - Math.cos(Math.toRadians(bearing)) * 0.05);
					float projectedX = (float) (playerX - Math.sin(Math.toRadians(bearing)) * 0.05);
					float saftyY = (float) (playerY - Math.cos(Math.toRadians(bearing)) * 0.15);
					float saftyX = (float) (playerX - Math.sin(Math.toRadians(bearing)) * 0.15);
					if (!collides(projectedX, projectedY) && !(collides(saftyX, saftyY))) {
						playerY = projectedY;
						playerX = projectedX;
					}
				}
				if (keysHeldDown.contains(KeyEvent.VK_UP)) {
					float projectedY = (float) (playerY + Math.cos(Math.toRadians(bearing)) * 0.05);
					float projectedX = (float) (playerX + Math.sin(Math.toRadians(bearing)) * 0.05);
					float saftyY = (float) (playerY + Math.cos(Math.toRadians(bearing)) * 0.15);
					float saftyX = (float) (playerX + Math.sin(Math.toRadians(bearing)) * 0.15);
					if (!collides(projectedX, projectedY) && !(collides(saftyX, saftyY))) {
						playerY = projectedY;
						playerX = projectedX;
					}
				}
			}
		});
		Timer timer = new Timer(1, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		timer.start();
		goalPoint = goal;
		playerX = x;
		playerY = y;
		bearing = degrees;
		booleanArray = seed;
		mapInclusion = includeMap;
		seeder = new BirdsEyePerspective(playerX, playerY, bearing, seed, false, goalPoint);
		map = seeder.getMap();
	}
	private Line lineAtDegree (Set<Line> set, float degree, float degreeSep){
		float minR = 1000;
		Color c = Color.lightGray;
		for(Line l : set){	
			if (((Math.toDegrees(l.pheta1) >= degree)&&(Math.toDegrees(l.pheta2)<= degree))||((Math.toDegrees(l.pheta2) >= degree)&&(Math.toDegrees(l.pheta1) <= degree))){
				float r = l.distanceAt(degree, false);
				if(r < minR){
					minR = r;
					c =  l.c;
				}
			}
		}
		return new Line((float) (minR*Math.sin(Math.toRadians(degree))), (float) (minR*Math.cos(Math.toRadians(degree))), 
				(float) (minR*Math.sin(Math.toRadians(degree+2*degreeSep))), (float) (minR*Math.cos(Math.toRadians(degree+2*degreeSep))), c );
	}
	private Set<Line> seenLines(Set<Line> linesInFieldOfVision, float degreeBounds, float resolution){
		float degreeSeperation = degreeBounds*2/resolution;
		Set<Line> visibleLines = new TreeSet<Line>();
		for( float d = -degreeBounds; d < degreeBounds; d += degreeSeperation){
			visibleLines.add(lineAtDegree(linesInFieldOfVision, d, degreeSeperation));
		}
		return visibleLines;
	}
	private void relativeBirdsEyePerspective() {
		Set<Line> newMap = new TreeSet<Line>();
		for (Line l : map) {
			Line a = l.translateOver(playerX, playerY);
			Line b = a.rotate(bearing);
			if (b.ifWithinDegrees(degreeCutOff)) {
				newMap.add(b.withinDegrees(degreeCutOff));
			}
		}
		//map = newMap;
		map = seenLines(newMap, degreeBounds, resolution);
	}

	private boolean collides(float x, float y) {
		// make this compare to the map
		int i = (int) Math.floor(x);
		int j = (int) Math.floor(y);
		return seeder.initMap[i][j];
	}

	public void paintComponent(Graphics g) {
		seeder = new BirdsEyePerspective(playerX, playerY, bearing, booleanArray, false, goalPoint);
		map = seeder.getMap();
		relativeBirdsEyePerspective();
		int width = getWidth();
		int height = getHeight();
		g.setColor(Color.gray);
		g.clearRect(0, 0, width, height);
		if (debugMode) {
			for (Line l : map) {
				if (l != null) {
					Line l2 = l.translateOver(-3, -3);
					int displayX1 = (int) ((l2.x1 / 6) * width);
					int displayY1 = (int) ((l2.y1 / 6) * height);
					int displayX2 = (int) ((l2.x2 / 6) * width);
					int displayY2 = (int) ((l2.y2 / 6) * height);
					g.setColor(l.c);
					g.drawLine(displayX1, displayY1, displayX2, displayY2);
					int midX = (displayX1 + displayX2) / 2;
					int midY = (displayY1 + displayY2) / 2;
		//COMMENT OUT IF YOU DON'T WANT TOO MUCH DATA/or if you are producing only visible lines
					/*g.drawString(
							Double.toString(Math.round(Math.toDegrees(l.pheta1))) + "("
									+ Float.toString(((float) Math.round((double) l.x1 * 100)) / 100) + ","
									+ Float.toString(((float) Math.round((double) l.y1 * 100)) / 100) + ")",
							displayX1, displayY1 + 12);
					g.drawString(
							Double.toString(Math.round(Math.toDegrees(l.pheta2))) + "("
									+ Float.toString(((float) Math.round((double) l.x2 * 100)) / 100) + ","
									+ Float.toString(((float) Math.round((double) l.y2 * 100)) / 100) + ")",
							displayX2, displayY2 + 12);*/
				}
			}
			g.setColor(Color.BLACK);
			int radius = 10;
			g.fillOval((int) (width / 2 - radius), (int) (height / 2 - radius), radius * 2, radius * 2);
			g.drawLine((int) (width / 2), (int) (height / 2), (int) (width / 2), (int) (height / 2 + 20));
			g.drawLine((int) (width / 2), (int) (height / 2), (int) (width), (int) (height));
			g.drawLine((int) (width / 2), (int) (height / 2), (int) 0, (int) (height));
			g.drawLine((int) (width / 2), (int) (height / 2), (int) (width / 2), height);
			g.drawLine((int) (width / 2), (int) (height / 2), (int) (width / 4), height);
			g.drawLine((int) (width / 2), (int) (height / 2), (int) ((width *3 )/ 4), height);
			g.drawLine((int) ((3-0.71)/6 * width), (int) ((3+1.41)/6 * height), (int) (width/2),(int)((3+0.71)/6 * height));
			g.drawLine((int) ((3+0.71)/6 * width), (int) ((3+1.41)/6 * height), (int) (width/2),(int)((3+0.71)/6 * height));
		} else {
			//Draw 3d rectangles
			g.clearRect(0, 0, width, height);
			g.setColor(Color.lightGray);
			g.fillRect(0, 0, width, height/2);
			g.setColor(Color.darkGray);
			g.fillRect(0, height/2, width, height/2);
			for (Line l : map) {
				float displayX = (0.5f + ((float) Math.toDegrees(l.pheta1)) / ((float) 90)) * (float) width;
				int deltaY = (int) (1 / (1 + l.r1) * height / 2);
				int displayY = (int) height / 2 - deltaY;
				int widthOfPixle = width / 90 +1;
				int heightOfPixle = deltaY * 2;
				g.setColor(l.c);
				g.fillRect((int) displayX, displayY, widthOfPixle, heightOfPixle);
			}
			//Draw target color
			g.setColor(Line.appropriateColorValue(goalPoint[1], goalPoint[0], seeder.maxX, seeder.maxY));
			g.fillRect(5*width/6, 0, width/6, height/6);
			//Draw Mini-map
			if(mapInclusion){
				System.out.println("Map Included");
				g.setColor(Color.darkGray);
				for (int i = 0; i < booleanArray.length; i++){
					for (int u = 0; u < booleanArray[0].length; u++){
						if(booleanArray[i][u]){
							g.fillRect(i*width/60, u*height/60, (width+120)/60, (height+120)/60);
						}
					}
				}
			}
		}
	}
}
