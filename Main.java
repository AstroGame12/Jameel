import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jameel on 09/01/15.
 */

public class Main implements MouseListener, MouseMotionListener, KeyListener {

    public static void main(String[] arg) {
        new Main();

    }

    // Important Website for Transparency  http://zetcode.com/gfx/java2d/transparency/
    // Make sure to fix bullet problem (Bullets dont delete after moving past y-10)
    /****************/
    /* Variables
     * ************/
    //Font font1 = new Font("Arial", Font.BOLD, 14);

    //window sizes
    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    int winWidth = gd.getDisplayMode().getHeight();
    int winHeight = gd.getDisplayMode().getWidth();


    JPanel mainPanel;  // the main JPanel

    //timer
    Timer timerEnemy, timerBullet;
    int timerESpeed = 500; //speed of timer repeats (ms)
    int timerEpause = 1000;  //initial delay (ms)
    int timerBSpeed = 10;
    int timerBpause = 1;  //initial delay (ms)

    boolean onOff = false;

    //x and y position of the mouse
    // int mx =50;
    // int my =50;
    // int mxClick =50;
    // int myClick =50;

    Ship ship = new Ship();

    ArrayList<Enemy> hoardList = new ArrayList<Enemy>();


    //list of all the bullets
    ArrayList<Shoot> bulletList = new ArrayList<Shoot>();


    /**
     * **************************************
     * Set up the window (JFrame)           *
     * and initialize whatever you need to  *
     * ***************************************
     */
    Main() {
        Object[] options = { "Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6" };
        int level = (JOptionPane.showOptionDialog(null, "What level do you want to start on?", "AstroShooter!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2])) + 1;
        //Setup timers
        ETimerListener etl = new ETimerListener();
        BTimerListener btl = new BTimerListener();
        timerEnemy = new Timer(timerESpeed, etl);
        timerEnemy.setInitialDelay(timerEpause);
        timerBullet = new Timer(timerBSpeed, btl);
        //don't set initial delay for bullets

        int enemyx =100;
        int enemyy = 50;
        //	if (level == 1) {
        // alien = new Enemy(Enemyx, Enemyy, 80, 20);
        // HoardList.add(alien);
        // alien = new Enemy(Enemyx, Enemyy, 80, 20);
        // HoardList.add(alien);
        for (int i=0; i < (level*10); i ++) {
            hoardList.add(new Enemy(enemyx * i + 10 , enemyy, 80, 20));
        }

        //	}

        // timer.start(); //moved to after frame is drawn

        JFrame window = new JFrame("Animation Template");
        mainPanel = new GrPanel();
        window.add(mainPanel);

        //add all listeners
        mainPanel.addMouseMotionListener(this);
        mainPanel.addMouseListener(this);
        mainPanel.addKeyListener(this);

        //window setup
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //set window size to full screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        winWidth = ((int) tk.getScreenSize().getWidth());
        winHeight = ((int) tk.getScreenSize().getHeight());

        window.setSize(winWidth, winHeight);
        window.setLocationRelativeTo(null);

        ship.y = (int) (winHeight * 0.8);
        ship.x = (int) (winWidth / 1.5);
        ship.width = 50;
        ship.height = 10;

        window.setResizable(false);
        window.setVisible(true);
        timerEnemy.start(); //put this after all of the drawing and setup has been done
        timerBullet.start();
    }

    //JPanel inner class
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

            /***************************/
			/* Draw all objects here */
            /***************************/

            //draw where the mouse is moved
            //g2.setColor(Color.YELLOW);
            //g2.fillOval(mx-4, my-4, 8, 8);

            //draw where the mouse is clicked
            //g2.setColor(Color.RED);
            //g2.drawRect(mxClick-10,myClick-10,20,20);

            //draw Ship
            ship.paintIcon(g2, ship.x, ship.y, ship.width, ship.height);

            //draw enemy
            for(Enemy en : hoardList) {
                g2.setColor(en.clr);
                g2.fillRect(en.x, en.y, en.width, en.height);
            }

            //draw Shoot
            g2.setColor(Shoot.clr);
            for (Shoot b : bulletList)
                g2.fillRect(b.x, b.y, b.width, b.height);
        }


        //draw something that flashes on and off each second
		/*
			if (onOff) {
				g.setFont(font1);
				g.setColor(Color.WHITE);
				g.drawString("Move mouse,     Click mouse,   or press arrow keys",180,20);
			}
			*/
    }

    /****************************/
	/* All event listeners here */

    /**
     * ************************
     */
    public void mouseMoved(MouseEvent e) {
        // mx = e.getX();
        // my = e.getY();
        // mainPanel.repaint();
    }

    public void mouseClicked(MouseEvent e) {
        // mxClick = e.getX();
        // myClick = e.getY();
        // mainPanel.repaint();
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        // mainPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR)); //change the mouse cursor
        mainPanel.requestFocus();
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        //if (e.getKeyCode() == 16) SHIFTON = true;

        System.out.println(key); //DEBUG to find keycodes

        if (key == 37) {
            if (ship.x > 0) {
                ship.x -= 20;  //left arrow
            }
        }
        if (key == 39) {
            if (ship.x < (winWidth - ship.width)) {
                ship.x += 20;  //right arrow
            }
        }
        if (key == 32) {    //spacebar
            //add another bullet to the screen.
            //position the bullet at the top of the ship
            Shoot bullet = new Shoot(ship.x + 24, ship.y - 12, 2, 12);
            bulletList.add(bullet);


        }
        /*
        if (key == ) {
            joptionpane
                    timerEpause
        }
        */
        mainPanel.repaint();

    }

    public void keyReleased(KeyEvent e) {
        //if (e.getKeyCode() == 16) SHIFTON = false; //just releasing the shift key
        //else key=0;
        //mainPanel.repaint();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    //###############################################
    //inner class for Timer Listener
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
    }  //end of inner class

    /**********************************/
	/* put and special functions here */
    /**
     * ******************************
     */
    void moveEnemies() throws IOException {
        //move all of the enemies
        for(Enemy en : hoardList) {
            en.x += en.xSpeed;
            if (en.x > winWidth ) {    //TODO use a variable for screenwidth
                en.y += 25; //move it down
                en.xSpeed = -en.xSpeed;
            }
            if (en.x <= 0) {    //use a variable for screenwidth
                en.y += 25; //move it down
                en.xSpeed = -en.xSpeed;
            }
        }
    } //end of moveStuff

    private class BTimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
			/* All timer tasks here    */
            //move bullet.
            //the timer moves the bullets up the screen
            for (Shoot s : bulletList) {
                s.y -= 10;
                for(Enemy en : hoardList) {
                    if (s.intersects(en)) {
                        en.health--;
                        if (en.health <= 0) {
                            hoardList.remove(en);
                            break;
                        } else {
                            en.update();
                            s.y = -100;
                        }
                    }
                }
            }




            //	and if it moves off the screen, delete the bullet
            for (int i=0; i < bulletList.size(); i++) {
                if (bulletList.get(i).y < 0) bulletList.remove(i);  //tested using < 200
            }

            mainPanel.repaint();
        }
    }  //end of inner class
}

