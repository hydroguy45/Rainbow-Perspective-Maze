

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game {
	public enum Mode{
		INITIAL,
		LEVELCHOICE,
		DIFFICULTYCHOICE,
		INFORMATION,
		GAME,
		CONGRATS
	}
	public static Mode requestedMode;
	public static int levelRequest;
	public static int difficultyRequest;
	public static void main(String[] args) {
		//Needs to be anything but Mode.INITIAL to enter the if statement in the while loop
		Mode currentMode = Mode.LEVELCHOICE;
		JFrame frame = new JFrame("Loading");
		requestedMode = Mode.INITIAL;
		levelRequest = 1;
		difficultyRequest = 3;
		while(true){
			System.out.print(".");
			if(!currentMode.equals(requestedMode)){
				System.out.println(requestedMode);
				frame.setVisible(false);
				JPanel newScreen = new JPanel();
				int width = 100;
				int height = 100;
				String name = "Loading";
				switch(requestedMode){
					case INITIAL:
						name = "Main";
						newScreen = new Menu();
						width = 400;
						height = 200;
						break;
					case LEVELCHOICE:
						name = "Level Choice";
						newScreen = new LevelSelection();
						width = 400;
						height = 300;
						break;
					case DIFFICULTYCHOICE:
						name = "Difficulty";
						newScreen = new DifficultySelection();
						width = 400;
						height = 300;
						break;
					case INFORMATION:
						name = "Info";
						newScreen = new Information();
						width = 600;
						height = 700;
						break;
					case GAME:
						name = "The Maze Game";
						newScreen= Level.getLevel(levelRequest, difficultyRequest);
						width = 1000;
						height = 1000;
						break;
					case CONGRATS:
						name = "The Maze Game";
						newScreen = new CongratsScreen();
						width = 400;
						height = 400;
						break;
					default:
						name = "Error";
						newScreen = new JPanel();
						width = 1000;
						height = 1000;
						System.out.println("Faulty mode swap");
				}
				frame = new JFrame(name);
				frame.add(newScreen);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setSize(width, height);
				frame.setVisible(true);
				currentMode = requestedMode;
			}
		}
	}
}
