import java.awt.*; //* to import all
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

	// Everything we need for this program
	static final int SCREEN_WIDTH = 600; //screen width
	static final int SCREEN_HEIGHT = 600; //screen height
	static final int UNIT_SIZE = 25; // how big to set objects in game
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;// calculate how many objects we can fit on screen																			
	static final int DELAY = 75;// delay timer (higher, the slower the game is)

	// creation of ARRAYS to hold body components of snake (like head)
	final int x[] = new int[GAME_UNITS]; // use GAME_UNITS because snake isn't going to bigger than game is itself
	final int y[] = new int[GAME_UNITS]; // x holds all x coordinate like head s, y all y coordinates
	int bodyParts = 6; // initial snake begins with 6 body parts
	int applesEaten; // starts with 0
	int appleX; // x coordinate of where apple appears (going to be random each time)
	int appleY; // y coordinate of where apple appears
	char direction = 'R'; // snake begins by going right
	boolean running = false; // snake begins by not running
	Timer timer; // instance of timer class
	Random random; // instance of Random class

	GamePanel() { // Constructor for GamePanel
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // Dimensions
		this.setBackground(Color.black); // background colour
		this.setFocusable(true); // focusable
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() { // start snake game function
		newApple(); // call newApple method to create new apple on screen for snake
		running = true; // false to begin with, now set to true
		timer = new Timer(DELAY, this); // this = using KeyListener interface, create new timer instance
		timer.start(); // start function
	}

	public void paintComponent(Graphics g) { // paint function
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) { // drawing function
		// creation of GRID for better visualization
		g.setColor(Color.DARK_GRAY);
		if (running) {
			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}
			g.setColor(Color.red); // set colour of g to red (apple)
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // set shape of apple to oval

			for (int i = 0; i < bodyParts; i++) { // for loop to iterate through bodyparts of snake to draw snake itself
				if (i == 0) { // dealing with head of snake
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else { // dealing with body of snake
					g.setColor(new Color(45, 180, 0)); // different shade of green
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			} // display score
			g.setColor(Color.red); // text colour
			g.setFont(new Font("Arial", Font.BOLD, 40)); // text font
			FontMetrics metrics = getFontMetrics(g.getFont()); // to centralize text
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
					g.getFont().getSize());
		} else {
			gameOver(g);
		}
	}

	public void newApple() { // new apples for snake to eat function
		// generate coordinates of newApple method
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE; // appear somewhere random along X
																				// domains
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
	}

	public void move() { // moving snake method
		for (int i = bodyParts; i > 0; i--) { // iterate through bodyparts of the snake
			x[i] = x[i - 1]; // shifting body part of snake around by 1 spot
			y[i] = y[i - 1];
		}

		switch (direction) { // change direction of where snake is headed
		case 'U': // up
			y[0] = y[0] - UNIT_SIZE; // y coordinate (head of snake)
			break;
		case 'D': // down
			y[0] = y[0] + UNIT_SIZE; 
			break;
		case 'L': // left
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R': // right
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}

	public void checkApple() { // checking if snake eats apple function
		if ((x[0] == appleX) && (y[0] == appleY)) { // if snake touches apple
			bodyParts++; // increase snake body parts
			applesEaten++; // increase apple eaten
			newApple(); // generate new apple
		}
	}

	public void checkCollisions() { // check if snake collides function
		// check if snake head collides with snake body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false; // trigger gameOver method
			}
		}

		// check if snake head collides left border
		if (x[0] < 0) {
			running = false;
		}
		// check if snake head collides right border
		if (x[0] > SCREEN_WIDTH) {
			running = false;
		}
		// check if snake head collides top border
		if (y[0] < 0) {
			running = false;
		}
		// check if snake head collides bottom border
		if (y[0] > SCREEN_HEIGHT) {
			running = false;
		}

		if (!running) { // if running is false stop timer
			timer.stop();
		}
	}

	public void gameOver(Graphics g) { // game over function
		// Score shown when game ended
		g.setColor(Color.red); // text colour
		g.setFont(new Font("Arial", Font.BOLD, 40)); // text font
		FontMetrics metricsScore = getFontMetrics(g.getFont()); // to centralize text
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metricsScore.stringWidth("Score: " + applesEaten)) / 2,
				g.getFont().getSize());
		// Game over text
		g.setColor(Color.red); // text colour
		g.setFont(new Font("Arial", Font.BOLD, 75)); // text font
		FontMetrics metricsEnd = getFontMetrics(g.getFont()); // to centralize text
		g.drawString("Game Over", (SCREEN_WIDTH - metricsEnd.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
	}

	@Override
	public void actionPerformed(ActionEvent e) { // actionPerformed

		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();

	}

	public class MyKeyAdapter extends KeyAdapter { // inter - class
		@Override
		public void keyPressed(KeyEvent e) { // keyPressed method for arrow keys
			switch (e.getKeyCode()) { // arrow keys movement switch for controlling snake
			case KeyEvent.VK_LEFT: // left arrow key
				if (direction != 'R') { // restricting movements to 90 degrees, so snake doesn't crash into itself
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT: // right arrow key
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP: // up arrow key
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN: // down arrow key
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

}
