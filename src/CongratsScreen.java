

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CongratsScreen extends JPanel {
	public CongratsScreen() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		JPanel infoPanel = new JPanel();
		String informationText = "<html>Congrats!<br> "
							   + "You just won</html>";
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
