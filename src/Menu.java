

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Menu extends JPanel{
	public Menu(){
		GridLayout collumn = new GridLayout(2,1);
		setLayout(collumn);
		JButton start= new JButton("Start Game");
		add(start);
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.requestedMode = Game.Mode.LEVELCHOICE;
			}
		});
		JButton info = new JButton("Information");
		add(info);
		info.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game.requestedMode = Game.Mode.INFORMATION;
			}
		});
	}
}
