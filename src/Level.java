

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Level {
	static Boolean[][] map1 = { { true, true, true }, 
								{ true, false, true }, 
								{ true, true, true } };
	static Boolean[][] map2 = { { true, true, true, true, true }, 
								{ true, false, false, true, true },
								{ true, false, true, true, true }, 
								{ true, false, false, true, true },
								{ true, true, true, true, true } };
	static Boolean[][] map3 = {
			{true, true, true, true, true, true, true},
			{true, false, false,false,false,false,true},
			{true, false, true, true, true, false,true},
			{true, false, false,false,false,false,true},
			{true, false, true, false,true, false,true},
			{true, false, false,false,false,false,true},
			{true, true, true, true, true, true, true}
	};
	static Boolean[][] map4 = {
			{true, true, true, true, true, true, true},
			{true,false,false,false,false,false, true},
			{true,false, true,false, true,false, true},
			{true, true, true,false, true, true, true},
			{true,false, true,false, true,false, true},
			{true,false,false,false,false,false, true},
			{true, true, true, true, true, true, true}
	};
	static Boolean[][] map5 = { 
			{ true, true,  true,  true,  true,  true,  true,  true,  true,  true },
			{ true, false, false, false, false, true,  true,  true,  false, true },
			{ true, false, true,  true,  false, true,  true,  true,  false, true },
			{ true, false, true,  false, false, false, false, false, false, true },
			{ true, false, true,  true,  true,  true,  false, true,  false, true },
			{ true, false, false, false, false, true,  false, true,  false, true },
			{ true, false, true,  false, true,  true,  false, true,  false, true },
			{ true, false, true,  false, true,  true,  false, true,  false, true },
			{ true, false, false, false, false, false, false, true,  false, true },
			{ true, true,  true,  true,  true,  true,  true,  true,  true,  true } };
	static Boolean[][][] levels = {map1, map2, map3, map4, map5};
	static float[][] goals = {{2, 1.5f},{1.5f, 4}, {5f,2.5f}, {1, 4.5f}, {5.5f, 8}};
	public static JPanel getLevel(int levelNum, int difficulty){
		switch(difficulty){
			case 1:
				return new BirdsEyePerspective(1.5f, 1.5f, 45, levels[levelNum-1], true, goals[levelNum-1]);
		case 2:
				return new FirstPersonPerspective(1.5f, 1.5f, 45, levels[levelNum-1], true, goals[levelNum-1]);
			case 3:
				return new FirstPersonPerspective(1.5f, 1.5f, 45, levels[levelNum-1], false, goals[levelNum-1]);
			default:
				return new JPanel();
		}
		
	}
}
