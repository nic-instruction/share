package main.java;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
//import com.google.common.collect.Range; 


public class Board extends JPanel implements ActionListener {

    protected final int B_WIDTH = 900;  // was 300, changing to 900
    protected  final int B_HEIGHT = 900; // was 300, changing to 900
    protected  final int DOT_SIZE = 30;  // was 10, changing to 30 for snake resizing
    protected  final int ALL_DOTS = 900; // docs say this is (b_width * b_height)/(dot_size * dot_size)
    protected  final int RAND_POS = 29;   
    protected  final int DELAY = 140;

    protected  final int x[] = new int[ALL_DOTS];
    protected  final int y[] = new int[ALL_DOTS];

    protected  int dots;
    protected  int apple_x;
    protected  int apple_y;

    protected  boolean leftDirection = false;
    protected  boolean rightDirection = true;
    protected  boolean upDirection = false;
    protected  boolean downDirection = false;
    protected  boolean inGame = true;

    protected  Timer timer;
    protected  Image ball;
    protected  Image apple;
    protected  Image head;
    protected  Image tail;       // added
    protected  Image body;       // added

    public Board() {
        
        initBoard();
    }
    
    protected void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    protected void loadImages() {

        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple_item.PNG");   // altered to take new graphics
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/blue_snakeHead.PNG");   // altered to take new graphics
        head = iih.getImage();
        
        ImageIcon iib = new ImageIcon("src/resources/blue_snakeBody.PNG");   // altered to take new graphics
        body = iib.getImage();
        
        ImageIcon iit = new ImageIcon("src/resources/blue_snakeTail.PNG");   // altered to take new graphics
        tail = iit.getImage();
        
        // NOTE: rename the above variables to something more human readable
        
    }

    protected void initGame() {

        dots = 3;  

        for (int z = 0; z < dots; z++) {  
            x[z] = 50 - z * 10;  
            y[z] = 50;           
        }
        
        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    protected void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);
            System.out.println("apple_x " + apple_x + " apple_y " + apple_y);
            System.out.println("x[0] " + x[0] + " y[0] " + y[0]);

            for (int z = 0; z < dots + 1; z++) {
                if (z == dots) {
                	// at the end, put a tail!
                    g.drawImage(tail, x[z], y[z], this);
                } else if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(body, x[z], y[z], this);  // was ball, changing to body
                }
                
                }
            

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }        
    }

    protected void gameOver(Graphics g) {
        
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    public void checkApple() {	// changed 
    	/*
        //the first line is the origonal, we have to scale up since we scaled size
    	for (int xVal = (x[0] - 30); xVal == (x[0] + 30); xVal++ ) {
    		for (int yVal = (y[0] - 30); yVal == (y[0] + 30); yVal++) {
    		   	if (((xVal) == apple_x) && ((yVal) == apple_y)) {
    		   		System.out.println("xVal and yVal " + xVal + " " + yVal);

    	            dots++;
    	            locateApple();
    	            
    	          
    	        }
    			
    		}
    		
    	}*/
    	
    	// origonal just made sure x and y were the same pixel
        
    	if ((apple_x + 30) < x[0] && x[0] > (apple_x - 30) && (apple_y + 30) < y[0]  && (y[0]> (apple_y - 30))) {
            System.out.println("this is where we hit the apple...");
            dots++;
            locateApple();
        } 
    }

    protected void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    protected void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
        }
    }

    protected void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    protected class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}
