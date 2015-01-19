import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.KeyAdapter;

public class Main {
	public static void main(String[] arg) {
		new Main();
	}

	// Graphics stuff
	private final int winWidth;
	private final int winHeight;

	// Swing stuff
	private final JPanel mainPanel;
	private final JFrame window;

	// Timer stuff
	private final Timer timerEnemy, timerBullet;
	private final int timerESpeed;
	private final int timerEpause = 1000;  //initial delay (ms)
	private final int timerBSpeed = 10;

	// Allows X and Z to alternate firing 
	private boolean enableFire = true;
	
	// Ship stuff
	private final int numberOfPlayers;
	private final Ship ship1 = new Ship(Color.GREEN);
	private final Ship ship2 = new Ship(Color.PINK);

	// Keep track of enemies and bullets
	private final List<Enemy> hoardList = new ArrayList<Enemy>();
	private final List<Shoot> bulletList = new ArrayList<Shoot>();

	Main() {
		// Set window size to fill the screen
		Toolkit tk = Toolkit.getDefaultToolkit();
		winWidth = ((int) tk.getScreenSize().getWidth());
		winHeight = ((int) tk.getScreenSize().getHeight());
		
		// Get the number of players that are playing
		numberOfPlayers = getNumberOfPlayers();
		
		// Set the position of our first ship
		ship1.y = (int) (winHeight * 0.8);
		ship1.x = (int) (winWidth / 1.5);
		
		// If there are two players playing, set the position of the second ship
		if (numberOfPlayers == 2) {
			ship2.y = (int) (winHeight * 0.8);
			ship2.x = (int) (winWidth / 3);
		}
		
		// Level (difficulty) select
		String[] options = {"Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6", "Level 7", "Level 8", "Level 9", "Level 10", "Level 11", "Level 12" };
		int level = (JOptionPane.showOptionDialog(null, "What level do you want to start on?", "AstroShooter!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2])) + 1;
		
		//Setup timers
		timerESpeed = 40 / level;
		ETimerListener etl = new ETimerListener();
		BTimerListener btl = new BTimerListener();
		timerEnemy = new Timer(timerESpeed, etl);
		timerEnemy.setInitialDelay(timerEpause);
		timerBullet = new Timer(timerBSpeed, btl);
		//don't set initial delay for bullets

		int enemyx = 100;
		int enemyy = 50;
		for (int j = 0; j < level; j++) {
			for (int i = 0; i < 10; i ++) {	
				hoardList.add(new Enemy(enemyx * i + 10 % 2 , enemyy + j * 30, 80, 20));
			}
		}

		window = new JFrame("Animation Template");
		mainPanel = new GrPanel();
		window.add(mainPanel);

		// Register event listeners
		mainPanel.addMouseMotionListener(new MouseInputAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mainPanel.requestFocus();
			}
			
			@Override
			public void mouseMoved(MouseEvent e) {
				ship1.x = e.getX() - ship1.width/2;
			}
		});
		mainPanel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				System.out.println("Key Pressed: " + key);
				
				// Handle movement for ship #1
				if (key == 37) {
					if (ship1.x > 0) {
						ship1.x -= 20;  //left arrow
					}
				} else if (key == 39) {
					if (ship1.x < (winWidth - ship1.width)) {
						ship1.x += 20;  //right arrow
					}
				} else if (key == 88 || key == 90) {    //x & z button
					if (!enableFire) return; 
					enableFire = false; //disable firing
					//add another bullet to the screen.
					//position the bullet at the top of the ship1
					Shoot bullet = new Shoot(ship1.x + 24, ship1.y - 12, 2, 12);
					bulletList.add(bullet);
				}
				
				// If there is 2 players playing, handle movement for ship #2
				if (numberOfPlayers == 2) {
					
				}

				mainPanel.repaint();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == 88 || key == 90) enableFire = true; 
			}
		});

		//window setup
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		window.setSize(winWidth, winHeight);
		window.setLocationRelativeTo(null);

		ship1.y = (int) (winHeight * 0.8);
		ship1.x = (int) (winWidth / 1.5);
		ship1.width = 50;
		ship1.height = 10; 
		
		window.setResizable(false);
		window.setVisible(true);
		timerEnemy.start(); //put this after all of the drawing and setup has been done
		timerBullet.start();
	}
	
	/**
	 * Show an option pane to get the number of people playing
	 * @return The number of players playing
	 */
	private int getNumberOfPlayers() {
		String[] options = {"One", "Two"};
		return JOptionPane.showOptionDialog(null, "Player Selection", "Please choose the number of players that will be playing",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options, options[0]);
	}

	private class GrPanel extends JPanel {
		GrPanel() {
			this.setBackground(Color.BLACK);
		}
		
		//paint components
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			//draw Ship
			ship1.paintIcon(g2, ship1.x, ship1.y, ship1.width, ship1.height);

			//draw enemy
			for(Enemy en : hoardList) {
				g2.setColor(en.clr);
				g2.fillRect(en.x, en.y, en.width, en.height);
				if (en.y >= ship1.y) {
					window.dispose();
					loser();
					System.out.print("! ");
					return;
				}
			}

			//draw Shoot
			g2.setColor(Shoot.clr);
			for (Shoot b : bulletList) {
				g2.fillRect(b.x, b.y, b.width, b.height);
			}
			g.setFont(new Font("Arial", Font.BOLD, 14));
			g.setColor(Color.WHITE);
			if (hoardList.size() > 0 ) {
				g.drawString("Enemies left = " + hoardList.size(),20,20);
			} else {
				timerEnemy.stop(); //put this after all of the drawing and setup has been done
				timerBullet.stop();
				winner();
			}
		}	
	}
	

	private class ETimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			/***************************/
			/* All timer tasks here    */
			/***************************/
			//do something
			//onOff = !onOff;
			try {
				moveEnemies();
			} catch (IOException e1) {
				System.out.print("IOException was caught");
			}
			mainPanel.repaint();
		}
	}
	

	void moveEnemies() throws IOException {
		//move allxxof the enemies
		for(Enemy en : hoardList) {
			en.x += en.xSpeed;
			if ((en.x + en.width) > winWidth ) {    //TODO use a variable for screenwidth
				en.y += 25; //move it down
				en.xSpeed = -en.xSpeed;
			}
			if (en.x <= 0) {    //use a variable for screenwidth
				en.y += 25; //move it down
				en.xSpeed = -en.xSpeed;
			}
			
		}
	}
	
	
	public void winner() {
		Object[] options = { "PLAY AGAIN", "EXIT" };
		int win = JOptionPane.showOptionDialog(null, "Congratulations", "Winner!",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
				null, options, options[0]);
				
				if (win == 0) {
					window.dispose();
					new Main();
				} else System.exit(0);
	}
	
	public void loser() {

		Object[] options = { "PLAY AGAIN", "EXIT" };
		int loss = JOptionPane.showOptionDialog(null, "VAC banned", "You suck!",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
				null, options, options[0]);
				
				if (loss == 0) {
					window.dispose();
					new Main();
				}
				if (loss == 1) System.exit(0);
				
		
	}

	private class BTimerListener implements ActionListener {
		// All timer tasks here
		public void actionPerformed(ActionEvent e) {
			// This moves the bullets up the screen
			for (Shoot s : bulletList) {
				s.y -= 3;
				for(Enemy en : hoardList) {
					if (s.intersects(en)) {
						en.health--;
						if (en.health <= 0) {
							hoardList.remove(en);
							if (hoardList.size() == 0) {
								//either here or in paintComponent
							}
							break;
						} else {
							en.update();
							s.y = -100;
						}
					}
				}
			}

			// and if it moves off the screen, delete the bullet
			for (int i=0; i < bulletList.size(); i++) {
				if (bulletList.get(i).y < 0) bulletList.remove(i);
			}

			mainPanel.repaint();
		}
	}  //end of inner class
}
