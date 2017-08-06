

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class DifficultySelection extends JPanel {
	public DifficultySelection() {
		GridLayout collumn = new GridLayout(3,1);
		setLayout(collumn);
		JButton easy = new JButton("Easy (Bird's Eye)");
		add(easy);
		easy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Easy Mode");
				Game.difficultyRequest = 1;
				Game.requestedMode = Game.Mode.GAME;
			}
		});
		JButton medium = new JButton("Medium (First Person + Map)");
		add(medium);
		medium.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Medium Mode");
				Game.difficultyRequest = 2;
				Game.requestedMode = Game.Mode.GAME;
			}
		});
		JButton hard = new JButton("Hard (First Person w/o Map)");
		add(hard);
		hard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Hard Mode");
				Game.difficultyRequest = 3;
				Game.requestedMode = Game.Mode.GAME;
			}
		});
	}
}
