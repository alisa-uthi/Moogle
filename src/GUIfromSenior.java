import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUIfromSenior {
	
	
   private JFrame mainFrame;//Main Frame of everything


   private JLabel headerLabel;//The top thing that says the name
   private JLabel statusLabel;//This is the thing that shows Query : Something 
   private JLabel msglabel;//Massage that is green
   private JLabel outputlabel;//the thing to show TFIDF
   
   
   private JPanel controlPanel;//A block that holds Labels and stuff
   private JPanel scorePanel;//Another block for TFIDF
   private JPanel searchBartxt;
   private JPanel Finalpanel;
    
   private JButton searchButton;
   private JButton[] Lyric;    
   
   private JTextField input;//input xD

   public GUIfromSenior()
   {
      GUI();//calls GUI
   }
   
   public static void main(String[] args)
   {
	  
	 GUIfromSenior  Show = new GUIfromSenior();//Initialize the View  
     Show.show();//calls show for stuff out of initialization
   }
   
   private void GUI()
   { 
      mainFrame = new JFrame("This is the Name of the Frame");
      mainFrame.setSize(1200,780);//Size of the Frame
      mainFrame.setLayout(new GridLayout(5, 1));//how many blocks  in the frame
      //Exit program
      mainFrame.addWindowListener(new WindowAdapter() 
      {
         public void windowClosing(WindowEvent windowEvent)
         {
            System.exit(0);
         }        
      } );    
      //Exit Program   
      
      headerLabel = new JLabel("This part can be the header of your program", JLabel.CENTER);        
      
      //TODO This text should not show after you have clicked the search button
      statusLabel = new JLabel("Just a random Label used for showing how you can change text",JLabel.CENTER);    
      
      
      statusLabel.setSize(350,100);//Size of statusLabel 

      //Every Panel should be declared properly as it will hold information for the project and other fields
      //each panel should have a layout set, In this example project the FlowLayout is used on all as an example
      //You can find many Layouts online, try read the java swing documentation for any more information
      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());
      
      scorePanel = new JPanel();
      scorePanel.setLayout(new FlowLayout());
      
      searchBartxt = new JPanel();
      searchBartxt.setLayout(new FlowLayout());
      
      Finalpanel = new JPanel();
      Finalpanel.setLayout(new FlowLayout());
     
      
      //As stated when create the label that the text will not show
      //The text will be replaced when Show.show() is called
      msglabel = new JLabel("temp text not gonna show anyways");
      
      //Just to initialize the text
      outputlabel = new JLabel("Nope");
      
      //This limit the input to only 20 characters
      input = new JTextField(20);
      
      //The Search Button
      //TODO Checkout the Listener which will tell what the button does bellow
      searchButton = new JButton("Search");
      
      
      //Just in case you need to initialize and use arrays' of buttons
      Lyric = new JButton[5];
      String[] nums = {"1", "2", "3", "4", "5"};
      for (int i = 0; i < Lyric.length; i++) {
    		   Lyric[i] = new JButton(nums[i]);
    		}
      
      
      //TODO This is what is added to the mainframe according to the blocks stated above
      mainFrame.add(headerLabel);//Head
      mainFrame.add(controlPanel);//2 block
      mainFrame.add(statusLabel);//3 block
      mainFrame.add(scorePanel);//4 block
      mainFrame.add(Finalpanel);//5 block
      
      
      

      //This is where the labels are added to the Panels 
      searchBartxt.add(msglabel);//This is a panel I just named it differently
      controlPanel.add(searchBartxt);
      controlPanel.add(input);
      controlPanel.add(searchButton);
      scorePanel.add(outputlabel);
      
      //You can add information to a panel using a loop as well :O
      for(int i=0;i<Lyric.length;i++)
      {
    	  Finalpanel.add(Lyric[i]);
      }
   
      //This function allows you to set the visibility of the panel or even the frame
      mainFrame.setVisible(true);
      scorePanel.setVisible(false);//Set false until search is done 
      Finalpanel.setVisible(true);


   }
   private void show(){
	  
      //As stated over this will change the text for the massageLabel
      msglabel.setText("Enter Song Lyric Here or Enter Path");   
      
      
      //SEARCH STARTS HERE
      ///////////////////////////
      searchButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) { 
        	//ActionListeners are the bread and butter of buttons, you can read more in the documentations :D 
            String data = "Query : " + input.getText();
            
            //TODO This is what will change the text when the search button is clicked 
            statusLabel.setText(data);  
            
        
           scorePanel.setVisible(true);//after clicking search and finishing compute score set the visibility to true
      								
         }
      }); 
      //////////////////////////////
      //SEARCH ENDS HERE
      
      mainFrame.setVisible(true);      
   }   
}