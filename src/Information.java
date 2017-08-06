

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Information extends JPanel {
	public Information() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		JPanel infoPanel = new JPanel();
		//thankfully this can be written in HTML, so this part should be super simple
		String informationText = "<html><u>In Game Controls</u><br>"
							   + "Rotate Left: left arrow<br>"
							   + "Rotate Right: right arrow<br>"
							   + "Forwards: up arrow<br>"
							   + "Backwards: down arrow<br>"
							   + "Back To Start: escape<br><br>"
							   + "<u>Game Description</u><br>"
							   + "Every wall of the maze is colored along<br>"
							   + "a spectrum.<br> More north --> more blue<br>"
							   + "More west --> more red<br>"
							   + "More east --> more green<br>"
							   + "The goal is given a target color, to <br>"
							   + "navigate to that wall's location in <br>"
							   + "the maze. This game can be played in <br>"
							   + "three very different difficulties <ul>"
							   + "<li>Easy: play from a bird's eye view perspective</li>"
							   + "<li>Medium: play from first person with a map</li>"
							   + "<li>Hard: play from first person without a map</li></ul><br>"
							   + "<u>Engine Description</u><br>"
							   + "I wrote the 3d graphics engine from scratch. While the 2d mode is quite<br>"
							   + "intuitive, the 3d modes are a little more complex. Firstly I rotate all walls to the player's<br>"
							   + "bearing and translate the walls over to the player. Subsequently I remove all walls<br>"
							   + "that are not in the player's field of vision. I then crop any walls that are seen but not fully.<br>"
							   + "Now that the set of walls only contains wall in the player's field of vision, I need to <br>"
							   + "construct a set of walls that are seen (i.e. no wall that are covered by other walls). This<br>"
							   + "is done by iterating through the current set at pheta intervals and finding that nearest wall for<br>"
							   + "that pheta interval. Then I add a wall of size pheta interval (~9/50 of a degree), minimum distance, <br>"
							   + "and color of the closest wall to a new wall set. Finally I have a set of every wall seen. To check this out<br>"
							   + "press semi-colon in either medium or hard difficulty. After this, I just apply a basic 3d transformation <br> "
							   + "where height of each wall is proportional to 1/(1+r), so the closest wall will take up the whole screen,<br>"
							   + " and any wall in the distance will be visible."
							   + "</html>";
		JLabel text = new JLabel(informationText, JLabel.CENTER);
		infoPanel.add(text);
		add(infoPanel, BorderLayout.CENTER);
		JPanel backPanel = new JPanel();
		JButton back = new JButton("Back To Game");
		backPanel.add(back);
		add(backPanel, BorderLayout.SOUTH);
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.requestedMode = Game.Mode.INITIAL;
			}
		});
	}
}
