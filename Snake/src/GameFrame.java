import javax.swing.JFrame;

public class GameFrame extends JFrame{
	GameFrame() {
		
		this.add(new GamePanel()); //shortcut
		this.setTitle("Snake"); //title
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setResizable(false);
		this.pack(); //will take JFrame & fit smoothly around all components added to the frame
		this.setVisible(true); 
		this.setLocationRelativeTo(null); //set window to middle of screen
		
	}
}
