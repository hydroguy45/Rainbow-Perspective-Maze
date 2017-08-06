

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class LevelSelection extends JPanel {
	public LevelSelection() {
		class LevelRequester implements ActionListener {
			final int level;
			public LevelRequester(int l) {
				level = l;
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("ACTION"+level);
				Game.levelRequest = level;
				Game.requestedMode = Game.Mode.DIFFICULTYCHOICE;
				System.out.println(Game.requestedMode);
			}
		}
		GridLayout collumn = new GridLayout(Level.levels.length, 1);
		setLayout(collumn);
		for (int i = 1; i <= Level.levels.length; i++) {
			JButton levelButton = new JButton("Level "+i);
			add(levelButton);
			levelButton.addActionListener(new LevelRequester(i));
		}
	}
}
