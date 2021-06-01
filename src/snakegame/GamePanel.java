package snakegame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
	
	
	private static final long serialVersionUID = 1L;
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75; //dictates how fast the game will run
	final int x[] = new int[GAME_UNITS]; //hold all of the x coord. of the body parts including the head of our snake
	final int y[] = new int[GAME_UNITS]; //hold the y coordinates
	int bodyParts = 6; //initial amount of body parts the snake has
	int applesEaten; //this will initially be 0
	int appleX; //X coordinate of where the apple is located, it will appear randomly
	int appleY; //for the y positioning of the apple
	char direction = 'R'; //which direction the snake will begin the game moving to
	boolean running = false; // to end or start the game
	static boolean gameOn = false; // to pause or resume the game
	Timer timer;
	Random random;
	GameFrame frame = null;
		
	GamePanel(GameFrame frame) {
		this.frame = frame;
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter(this));
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this); //we pass in 'this' because we are using the action listener interface
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if(running) {
			
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			//draw the snake, draw the head and draw the body
			
			//iterate through all of the body parts of the snake
			for(int i = 0; i< bodyParts; i++) {
				//draw the head
				if(i == 0) {
					g.setColor(Color.green); //sanke's head color
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); //paint a rectangle
				}
				else {
					g.setColor(new Color(45,180,0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); //paint a rectangle
				}			
			}
			// add the score in the top of the panel
			g.setColor(Color.red);
			g.setFont( new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		} else {
			gameOver(g);
		}
		
	
	}
	//generates the coordinates of a new apple
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		
	}
	
	public void move() {
		//iterate through all the body parts of the snake
		for(int i = bodyParts;i>0;i--) {
			//shift all the coordinates in this array over by one spot
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		//change the direction of where the snake is headed
		switch(direction) {
		case 'U': //UP
			y[0] = y[0] - UNIT_SIZE; //the y[0] is the y coordinate for the head of the snake
			break;
		case 'D': //DOWN
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L': //LEFT
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R': //RIGHT
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}
	
	public void checkApple() {
		//examine the coordinates of the snake and the coordinates of the apple
		//if head of the snake x coordinate is equal to x coordinate of the apple AND
		//y coordinate of the head of the snake is also equal to apple y coordinate
		if((x[0] == appleX) && (y[0] == appleY)) {
			//increase snake size by one, increase score and create new apple in a random spot
			bodyParts++;
			applesEaten++;
			newApple();
		}
		
	}
	
	public void checkCollisions() {
		// check if head collides with body
		//iterate through all of the body parts of the snakes
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				//if this is true, it means the head collided with the body
				//running = false will trigger a end game
				running = false;
			}
		}
		// check if head touches left border
		if (x[0] < 0) {
			running = false;
		}
		// check if head touches right border
		if (x[0] > SCREEN_WIDTH) {
			running = false;
		}
		// check if head touches top border
		if (y[0] < 0) {
			running = false;
		}
		// check if head touches bottom border
		if (y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		//stop the timer 
		if (!running) {
			timer.stop();
		}

	}
	
	public void pause() {
		GamePanel.gameOn = true;
		timer.stop();
	}

	public void resume() {
		GamePanel.gameOn = false;
		timer.start();
	}

	
	public void gameOver(Graphics g) {
		// Display the score on the game over page
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont()); //FontMetrics are useful for aligning up text in the center of the screen
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2,
				g.getFont().getSize());

		// Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
		
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString("Press enter to play again", (SCREEN_WIDTH - metrics3.stringWidth("Press enter to play again\"")) / 2, SCREEN_HEIGHT / 2 + 100);
		
		// Play again button
				
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
		
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		
		GamePanel parent = null;
		public MyKeyAdapter (GamePanel parent) {
			this.parent = parent;
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			//Examine the e key event and get key code
			switch (e.getKeyCode()) {
			//We have one case for each of the arrow keys
			//we don't want the user to be able to turn 180 degrees in the opposite direction
			//otherwise would get a game over since they're going directly into themselves
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			//pause the game or restart it when space is pressed	
			case KeyEvent.VK_SPACE:
				if(GamePanel.gameOn) {
					resume();
				} else {
					pause();
				}
				break;
			//recreate the game if enter is pressed (if on game over page)	
			case KeyEvent.VK_ENTER:
				if(!running) {
				
					frame.getContentPane().remove(parent);
					GamePanel newPanel = new GamePanel(parent.frame);
					frame.add(newPanel);
					newPanel.requestFocusInWindow();
					frame.getContentPane().invalidate();
					frame.getContentPane().validate();
				}
			}
		}
	}	

}
