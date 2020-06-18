// Name: Alisa Uthikamporn
// Student ID: 6188025
// Section: 1

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class KMovieGUI extends JFrame{

	public static void main(String[] args) 
	{
		KMovieGUI movGui = new KMovieGUI();
		movGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		movGui.setSize(500,500);
		movGui.setVisible(true);

	}
	
	private JLabel text1;
	private JTextField textboxSearch;
	private JButton SearchButton;
	
	public KMovieGUI()
	{
		super("Kimmy's Moogle GUI Application");
		setLayout(new FlowLayout());
		
		text1 = new JLabel("Search movie");
		textboxSearch = new JTextField(30);
		SearchButton = new JButton("Search");
		SearchButton.setPreferredSize(new Dimension(180,30));
		
		add(text1);
		add(textboxSearch);
		add(SearchButton);
		
		theHandler handler = new theHandler();
		textboxSearch.addActionListener(handler); 	//may not use it
		SearchButton.addActionListener(handler);
	}

	private class theHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String str = "";
			if(event.getSource() == textboxSearch)
			{
				str = event.getActionCommand();
				JOptionPane.showMessageDialog(null, str);	
			}
			else if(event.getSource() == SearchButton)
			{
				JOptionPane.showMessageDialog(null, "Searching for the movies...");
			}
		}	
	}	
		
}
