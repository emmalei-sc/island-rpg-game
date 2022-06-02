import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.FileInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.net.URL;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Screen extends JPanel implements KeyListener, MouseListener, MouseMotionListener
{
	private Player p1;
	private Tree[] trees;
	private Grass[] grasses;
	private int numTuftsEach;
	private GrassTuft[] tufts;
	private Wave[] waves;
	private Raindrop[] rain;
	private Character hermit;

	private int xDiff;
	private int yDiff;
	private int stepSize;

	private ArrayList<Item> groundItems;
	private ArrayList<Item> inventory;
	private ArrayList<String> questList;
	private ArrayList<String> completedQuests;
	private ArrayList<String> dialogueList;

	private Font headingFont;
	private Font questFont;
	private Font labelFont;
	private Font titleFont;
	private Font subtitleFont;

	private FontMetrics titleFm;
	private FontMetrics subtitleFm;
	private FontMetrics headingFm;
	private FontMetrics questFm;

	private int h2pX;
	private int startGameX;
	private int darknessUpY;
	private int darknessDownY;
	//private int reachedDock;

	private double boulderX;
	private double fadeOpacity;

	private Color howToPlayTextColor;
	private Color startGameTextColor;
	private Color backButtonColor;
	private Color textDefaultColor;
	private Color textHighlightColor;
	private Color panelColor;
	private Color skyColor;
	private Color waterColor;

	private BufferedImage panel;
	private BufferedImage bg;
	private BufferedImage arrowKeys;
	private BufferedImage wasdKeys;
	private BufferedImage boat;
	private double bgX;

	private int questNum;
	private int dialogueNum;

	private boolean dialogueOn;
	private boolean startPlay;
	private boolean startQuests;
	private boolean howToPlay;
	private boolean introScene;
	private boolean crash;
	private boolean wakeUp;
	private boolean hermitSpeaks;
	private boolean quest1Done;
	private boolean quest2Done;
	private boolean obtainedAxe;
	private boolean inventoryFull;
	private boolean reachedDock;
	private boolean fadeToBlack;
	private boolean endScene;
	private boolean theEnd;

	private boolean moveLeft, moveRight, moveUp, moveDown;

	private Clip rainClip;
	private Clip islandSoundClip;
	private Clip windClip;

	public Screen()
	{
		setFocusable(true);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		// instantiate objects
		p1 = new Player(385,285);
		hermit = new Character(220,125);
		trees = new Tree[30];
		for (int i=0; i<trees.length; i++)
		{
			int x = (int)(Math.random()*1200-200);
			int y = (int)(Math.random()*900-150);
			while (x > -40 && x < 270 && y > -25 && y < 190) // ensures trees don't block the house or hermit
			{
				x = (int)(Math.random()*1200-200);
				y = (int)(Math.random()*900-150);
			}
			trees[i] = new Tree(x, y);
		}
		grasses = new Grass[12];
		numTuftsEach = (int)(Math.random()*4+2); // 2-5 tufts per patch
		tufts = new GrassTuft[grasses.length*numTuftsEach];
		for (int i=0; i<grasses.length; i++)
		{
			grasses[i] = new Grass( (int)(Math.random()*600-200), (int)(Math.random()*500-150) );
		}
		for (int i=0; i<tufts.length; i++)
		{
			tufts[i] = new GrassTuft(grasses[i/numTuftsEach]);
		}
		waves = new Wave[7];
		for (int i=0; i<waves.length; i++)
		{
			waves[i] = new Wave((int)(Math.random()*710), (int)(Math.random()*245+325));
		}
		rain = new Raindrop[3500];
		for (int i=0; i<rain.length; i++)
		{
			rain[i] = new Raindrop( (int)(Math.random()*799), (int)(Math.random()*585-600) );
		}

		// load images
		try
		{
			panel = ImageIO.read(new File("images/questPanelLite.png"));
			bg = ImageIO.read(new File("images/rpgBg.jpg"));
			arrowKeys = ImageIO.read(new File("images/arrowKeys.png"));
			wasdKeys = ImageIO.read(new File("images/wasd.png"));
			boat = ImageIO.read(new File("images/boat.png"));
		} catch(IOException e)
		{
			System.out.println(e);
		}
		// load font
		try
		{
			titleFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("fonts/CocoMilk.otf"))).deriveFont(Font.PLAIN, 100);
			subtitleFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("fonts/SuperLegendBoy.ttf"))).deriveFont(Font.PLAIN, 20);
			headingFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("fonts/SuperLegendBoy.ttf"))).deriveFont(Font.PLAIN, 16);
			questFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("fonts/SuperLegendBoy.ttf"))).deriveFont(Font.PLAIN, 13);
			labelFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("fonts/SuperLegendBoy.ttf"))).deriveFont(Font.PLAIN, 9);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		// set up audio files
		try
        {
            URL url = this.getClass().getClassLoader().getResource("sounds/rainSound.wav");
            rainClip = AudioSystem.getClip();
            rainClip.open(AudioSystem.getAudioInputStream(url));
        } catch (Exception exc)
        {
            exc.printStackTrace(System.out);
        }
		try
        {
            URL url = this.getClass().getClassLoader().getResource("sounds/islandSound.wav");
            islandSoundClip = AudioSystem.getClip();
            islandSoundClip.open(AudioSystem.getAudioInputStream(url));
        } catch (Exception exc)
        {
            exc.printStackTrace(System.out);
        }
		try
        {
            URL url = this.getClass().getClassLoader().getResource("sounds/wind.wav");
            windClip = AudioSystem.getClip();
            windClip.open(AudioSystem.getAudioInputStream(url));
        } catch (Exception exc)
        {
            exc.printStackTrace(System.out);
        }
		// object lists
		groundItems = new ArrayList<Item>();
		inventory = new ArrayList<Item>();
		questList = new ArrayList<String>();
		completedQuests = new ArrayList<String>();
		dialogueList = new ArrayList<String>();

	/* set up colors, counters, ints, and booleans, add collectibles */

		xDiff = 700; // start at the edge of the island
		yDiff = 0;
		stepSize = 2;

		bgX = 0;
		boulderX = 805;
		darknessUpY = 300;
		darknessDownY = 300;
		//reachedDock = 0;
		fadeOpacity = 0.0;

		textDefaultColor = new Color(240,240,240);
		textHighlightColor = new Color(240, 201, 93); // yellow highlight
		howToPlayTextColor = textDefaultColor;
		startGameTextColor = textDefaultColor;
		panelColor = new Color(238, 224, 203); // beige
		skyColor = new Color(174, 236, 239);
		waterColor = new Color(64, 161, 179);

		// add coins to groundItem list
		for (int i=0; i<10; i++)
		{
			int x = (int)(Math.random()*1600-400);
			int y = (int)(Math.random()*1200-300);
			while (x > -40 && x < 270 && y > -25 && y < 190) // ensures coins don't block the house or hermit
			{
				x = (int)(Math.random()*1600-400);
				y = (int)(Math.random()*1200-300);
			}
			groundItems.add(new Coin(x, y));
		}
		// add axe
		int ax = (int)(Math.random()*1600-400);
		int ay = (int)(Math.random()*1200-300);
		while (ax > -40 && ax < 270 && ay > -25 && ay < 190) // ensures axe doesn't block the house or hermit
		{
			ax = (int)(Math.random()*1600-400);
			ay = (int)(Math.random()*1200-300);
		}
		groundItems.add(new Axe(ax,ay));

		questNum = 1;
		dialogueNum = 0;

		howToPlay = false; // will turn on when 'how to play' is clicked
		dialogueOn = false;
		startPlay = false; // turns on when 'start game' is clicked
		startQuests = false; // true after intro dialogue is complete
		introScene = false; // true after 'start game' is clicked
		crash = false; // only applies to when the ship crashes during the intro
		wakeUp = false; // true after the intro scene
		hermitSpeaks = false;
		quest1Done = false;
		quest2Done = false;
		obtainedAxe = false;
		inventoryFull = false;
		reachedDock = false;
		fadeToBlack = false;
		endScene = false;
		theEnd = false;

		moveLeft = false;
		moveRight = false;
		moveUp = false;
		moveDown = false;

		playWindSound();

	/* quests and dialogue set up here */

		// quests
		questList.add(""); // placeholder since questNum starts at 1
		questList.add("Collect 10 coins around the island and give them to the hermit.");
		questList.add("Find the axe, then use it to collect 5 pieces of wood.");
		questList.add("Go to the dock and build a boat. Time to set sail!");

		// storyline
/* 0 */	dialogueList.add("What a nice day!");
		dialogueList.add("... nevermind.");
		dialogueList.add("It's getting hard to see...");
/* 3 */	dialogueList.add("*CRASH*");
		dialogueList.add(" . . .");
		dialogueList.add("... everything's dark.");
/* 6 */	dialogueList.add("... you should open your eyes.");
		dialogueList.add("It looks like you've been stranded on an island. Your ship must have been destroyed.");
/* 8 */	dialogueList.add("Try to see if there's anyone around.");

	}

	public Dimension getPreferredSize()
	{
		//Sets the size of the panel
        return new Dimension(800,600);
	}

	public void paintComponent(Graphics g)
	{
        super.paintComponent(g);

		//draws background
		g.setColor(new Color(64, 161, 179)); // water
		g.fillRect(-800+xDiff, -600+yDiff, 6*400, 7*300);
		g.setColor(new Color(234,208,168)); // sand
		g.fillRoundRect(-400+xDiff, -300+yDiff, 4*400, 4*300, 150, 150);
		g.setColor(new Color(182,159,102)); // regular ground
		g.fillRoundRect(-200+xDiff, -150+yDiff, 1200, 900, 175, 175);
		for (int i=0; i<grasses.length; i++) // grass patches
		{
			grasses[i].drawMe(g, xDiff, yDiff);
		}
		for (int i=0; i<tufts.length; i++)
		{
			tufts[i].drawMe(g, xDiff, yDiff);
		}
		// dock
		g.setColor(new Color(89, 59, 7));
		g.fillRoundRect(275+xDiff, 825+yDiff, 250, 250, 20, 20);
		g.setColor(new Color(51, 35, 2));
		g.fillRoundRect(275+xDiff, 820+yDiff, 40, 260, 20, 20);
		g.fillRoundRect(485+xDiff, 820+yDiff, 40, 260, 20, 20);
		g.fillRect(325+xDiff,850+yDiff, 150, 3);
		g.fillRect(325+xDiff,900+yDiff, 150, 3);
		g.fillRect(325+xDiff,950+yDiff, 150, 3);
		g.fillRect(325+xDiff,1000+yDiff, 150, 3);
		g.fillRect(325+xDiff,1050+yDiff, 150, 3);

	/* Draw the game objects */

		// draw shadows
		for (int i=0; i<trees.length; i++) // tree shadows
		{
			trees[i].drawShadow(g, xDiff, yDiff);
		}
		int hx = 10; // hermit house's coords
		int hy = 45;
		g.setColor(new Color(0.2f,0.2f,0.2f, 0.3f)); // hermit house shadow
        g.fillRoundRect(hx-5+xDiff, hy+95+yDiff, 215, 10, 20, 20);
		hermit.drawShadow(g, xDiff, yDiff);

		hermit.drawMe(g, xDiff, yDiff);
		p1.drawMe(g); // player
		// hermit house
		g.setColor(new Color(173, 148, 104));
        g.fillRoundRect(hx+xDiff, hy+yDiff, 200, 100, 20, 20);
        g.setColor(new Color(74, 51, 18)); // roof
        int[] xRoof = {hx-20+xDiff, hx+200+20+xDiff, hx+(200/2)+xDiff};
        int[] yRoof = {hy+10+yDiff, hy+10+yDiff, hy-50+yDiff};
        g.fillPolygon(xRoof, yRoof, 3);
        g.fillRoundRect(hx+80+xDiff, hy+(100-40)+yDiff, 36, 40, 10, 10); // door
		// trees
		for (int i=0; i<trees.length; i++)
		{
			trees[i].drawMe(g, xDiff, yDiff);
		}
		// collectibles
		for (int i=0; i<groundItems.size(); i++)
		{
			groundItems.get(i).drawMe(g, xDiff, yDiff);
		}
		hermit.drawLabel(g, labelFont, xDiff, yDiff);

		//draw inventory
		int x = 150;
		int y = 549;
		for (int i=0; i<inventory.size(); i++)
		{
			inventory.get(i).drawInventoryItem(g, x, y);
			x += 50;
		}
		for (int i=inventory.size()+1; i<11; i++)
		{
			g.setColor(new Color(230,230,230));
			g.drawRect(x, y, 50, 50);
			x += 50;
		}
		if (inventoryFull)
		{
			g.setFont(labelFont);
			g.setColor(Color.black);
			g.drawString("Your inventory is full!",151,545);
		}

		// quests
		if (startQuests)
		{
			// board
			int qX = 599;
			int qY = 0;
			int qWidth = 200;
			int qHeight = 150;
			drawBoard(g, qX, qY, qWidth, qHeight);

			// board heading
			g.setFont(headingFont);
			g.setColor(new Color(230,230,230));
			g.drawString("Quest "+questNum,660,30);

			// quest
			g.setColor(Color.black);
			g.setFont(questFont);
			g.drawImage(panel,qX+10,qY+40,200-20,150-40-20, null);
	        DrawString.drawText(questList.get(questNum), questFont, g, 200-40, qX+20, qY+60);
		}

		// draw coordinates
		g.setFont(questFont);
		g.setColor(Color.black);
		g.drawString("X: "+(-xDiff) + "   Y: "+(-yDiff),15,20);

		// darkness (asleep)
		if (wakeUp || !startPlay)
		{
			g.setColor(new Color(15, 14, 14));
			g.fillRect(0,0,1600,darknessUpY);
			g.fillRect(0,darknessDownY,1600,1200);
		}

		// sailing scene
		if (introScene)
		{
			g.setColor(skyColor); // sky
			g.fillRect(-800, 0, 6*400, 600);
			g.setColor(waterColor); // water
			g.fillRect(-800, 325, 6*400, 3*300);
			for (int i=0; i<waves.length; i++) // waves
			{
				waves[i].drawMe(g);
			}
			g.drawImage(boat, 350, 265, 100,100, null); // boat
			p1.setXY(405, 310);
			p1.drawMe(g); // character
			g.setColor(new Color(64, 55, 50)); // boulder
			int[] arrX = {(int)boulderX, (int)boulderX+100,(int)boulderX+200,(int)boulderX+300,(int)boulderX+400, (int)boulderX+500};
			int[] arrY = {365,175,125,-100,-150,365};
			g.fillPolygon(arrX, arrY, 6);
			for (int i=0; i<rain.length; i++) // rain
			{
				rain[i].drawMe(g);
			}
		}
		else
		{
			p1.setXY(385,285);
		}

		if (endScene)
		{
			g.setColor(new Color(174, 236, 239)); // sky
			g.fillRect(-800, 0, 6*400, 600);
			g.setColor(new Color(64, 161, 179)); // water
			g.fillRect(-800, 325, 6*400, 3*300);
			for (int i=0; i<waves.length; i++) // waves
			{
				waves[i].drawMe(g);
			}
			g.drawImage(boat, 350, 265, 100,100, null); // boat
			p1.setXY(405, 310);
			p1.drawMe(g); // character
		}

		// dialogue
		if (dialogueOn)
		{
			if (hermitSpeaks)
			{
				g.setColor(panelColor);
				g.fillRoundRect(101,400-22,164,30,10,10);
				g.setColor(new Color(74, 54, 28));
				g.drawRoundRect(101,400-22,164,30,10,10);
				g.setColor(Color.black);
				g.setFont(questFont);
				g.drawString("Resident Hermit", 109, 395);
			}
			drawBoard(g, 100,400,600,125); // board
			g.setColor(panelColor);
			g.fillRect(100+10,400+10,600-20,125-20); // panel
			// dialogue
			g.setColor(Color.black);
			g.setFont(questFont);
			DrawString.drawText(dialogueList.get(dialogueNum), questFont, g, 600-30, 100+20,400+30);
			g.setFont(labelFont);
			g.drawString("Press Enter to continue.", 525, 510);
		}

		if (fadeToBlack)
		{
			g.setColor(new Color(0.0f, 0.0f, 0.0f, (float)fadeOpacity));
			g.fillRect(0,0,800,600);
		}

		if (theEnd)
		{
			g.setColor(Color.black);
			g.fillRect(0,0,800,600);
			g.setColor(Color.white);
			g.setFont(titleFont);
			String str = "the end.";
			g.drawString(str, (800-titleFm.stringWidth(str))/2, (600+titleFm.getAscent())/2 );
		}

	/* title screen */
		if (!startPlay)
		{
			g.drawImage(bg, (int)bgX,0, 1200, 600, null);
			g.setFont(titleFont);
			g.setColor(Color.white);
			titleFm = g.getFontMetrics();
			String title = "Stuck";
			g.drawString(title,(800-titleFm.stringWidth(title)) / 2, 295);

			g.setFont(subtitleFont);
			g.setColor(howToPlayTextColor);
			subtitleFm = g.getFontMetrics();
			h2pX = (800-subtitleFm.stringWidth("How to Play")) / 2;
			g.drawString("How to Play",h2pX, 400);
			g.setColor(startGameTextColor);
			startGameX = (800-subtitleFm.stringWidth("Start Game")) / 2;
			g.drawString("Start Game",startGameX, 450);
		}

		// how to play panel
		if (!startPlay && howToPlay)
		{
			drawBoard(g, 200,150,400,300);
			g.setColor(panelColor);
			g.fillRect(210, 160, 380, 280);
			g.setColor(Color.black);
			g.setFont(subtitleFont);
			g.drawString("How to Play",((800-subtitleFm.stringWidth("How to Play")) / 2) + 5, 240);
			g.setFont(questFont);
			questFm = g.getFontMetrics();
			String str1 = "Use the arrow keys or";
			String str2 = "WASD to move.";
			g.drawString(str1, 250 + ( (300-questFm.stringWidth(str1)) / 2 ), 295);
			g.drawString(str2, 250 + ( (300-questFm.stringWidth(str2)) / 2 ), 315);
			g.setColor(backButtonColor);
			g.drawString("< Back", 220,180);

			g.drawImage(arrowKeys, 315,355,80,40, null);
			g.drawImage(wasdKeys, 420, 353, 65,40, null);
		}

	}

	public void drawBoard(Graphics g, int x, int y, int width, int height)
	{
		g.setColor(new Color(117, 86, 47)); // base
        g.fillRect(x,y,width,height);
        g.setColor(new Color(74, 54, 28)); // dark outer outline
        g.drawRect(x,y,width,height);
        g.setColor(new Color(207, 191, 157)); // inner highlight
        g.drawRect(x+3,y+3,width-6,height-6);
	}

	public void animate()
	{
		while (true)
		{	// every .01 secs
			try
			{
                Thread.sleep(10);
            } catch (InterruptedException ex)
			{
                Thread.currentThread().interrupt();
            }

			if (!startPlay) // animates title screen bg
			{
				if (bgX > -400)
					bgX -= .2;
				else if (bgX <= -400)
					bgX = 0;
			}
			if (introScene)
			{
				if (crash) // animates boulder
				{
					boulderX -= .2;
					if (boulderX < 430)
					{
						crash = false;
						dialogueNum = 3; // ""*CRASH*""
					}
				}
				if (dialogueNum < 3) // animates waves before the crash
				{
					for (int i=0; i<waves.length; i++)
					{
						waves[i].move();
					}
				}
				if (dialogueNum > 0 && dialogueNum < 4) // rain after "what a nice day"
				{
					skyColor = new Color(125,136,152);
					waterColor = new Color(73,107,135);
					for (int i=0; i<rain.length; i++)
					{
						rain[i].moveDown();
					}
				}
			}

			if (wakeUp && !dialogueOn) // animates waking up
			{
				if (darknessUpY > -5)
				{
					darknessUpY -= 2;
				}
				if (darknessDownY < 605)
				{
					darknessDownY += 2;
				}
				if (darknessUpY == -6 && darknessDownY == 606)
				{
					wakeUp = false; // black disappears from the map
					dialogueOn = true;
				}
			}

			if (!dialogueOn && startPlay && !endScene) // movement in game
			{
				if (moveLeft)
					xDiff -= stepSize;
				if (moveRight)
					xDiff += stepSize;
				if (moveUp)
					yDiff += stepSize;
				if (moveDown)
					yDiff -= stepSize;

				if ((xDiff-p1.getWidth()) < -800)
					moveLeft = false;
				if ((xDiff+p1.getWidth()) > 800)
					moveRight = false;
				if ((yDiff+p1.getHeight()) > 600)
					moveUp = false;
				if ((yDiff-p1.getHeight()) < -600)
					moveDown = false;
			}

		// check collisions between player and items
			for (int i=0; i<groundItems.size(); i++)
			{
				if (startQuests) // only collect if questing
				{
					if ( p1.checkCollision(groundItems.get(i), xDiff, yDiff) )
					{
						if (inventory.size() >= 10)
						{
							//System.out.println("Inventory full");
							inventoryFull = true;
						}
						else
						{
							inventoryFull = false;
							if (groundItems.get(i).getName().equals("axe"))
							{
								if (questNum >= 2) // can only pick up axe during quest 2
								{
									inventory.add(groundItems.get(i)); // add to inventory
									groundItems.remove(i); // remove item from ground
									i--;
									playPickUpSound();
									System.out.println("picked up axe");
									obtainedAxe = true;
									hermitSpeaks = false;
									dialogueList.add("You found an axe! Press E near a tree to chop.");
									dialogueNum = dialogueList.size()-1;
									dialogueOn = true;
								}
							}
							else
							{
								if (groundItems.get(i).getName().equals("coin"))
								{
									playCoinSound();
									System.out.println("picked up coin");
									inventory.add(groundItems.get(i)); // add to inventory
									groundItems.remove(i); // remove item from ground
									i--;
								}
								else if (groundItems.get(i).getName().equals("wood"))
								{
									playPickUpSound();
									System.out.println("picked up wood");
									inventory.add(groundItems.get(i)); // add to inventory
									groundItems.remove(i); // remove item from ground
									i--;
								}
								if (inventory.size() >= 10)
									inventoryFull = true;
							}
						}
					}
				}
			}

		// check if player is on the dock
			if (xDiff > -85 && xDiff < 85 && (yDiff-p1.getHeight()) < -540 && questNum == 3 && quest2Done && !reachedDock)
			{
				System.out.println("Dock reached");
				reachedDock = true;
				if (reachedDock)
				{
					dialogueList.add("This is the dock. Click on it to build your boat.");
					dialogueNum = dialogueList.size()-1;
					dialogueOn = true;
				}
			}

			if (endScene)
			{
				for (int i=0; i<waves.length; i++)
				{
					waves[i].move();
				}
			}

			if (fadeToBlack)
			{
				fadeOpacity += 0.01;
				if (fadeOpacity >= 1.0)
				{
					fadeToBlack = false;
					fadeOpacity = 0.0;
					if (endScene == false)
					{
						endScene = true;
						dialogueOn = true;
					}
					else
					{
						theEnd = true;
						stopIslandSound();
					}
				}
			}

			repaint();
		}
	}

	public void playCoinSound()
	{
		try
        {
            URL url = this.getClass().getClassLoader().getResource("sounds/coinSound.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
            clip.start();
        } catch (Exception exc)
        {
            exc.printStackTrace(System.out);
        }
	}

	public void playPickUpSound()
	{
		try
        {
            URL url = this.getClass().getClassLoader().getResource("sounds/pickUpSound.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
            clip.start();
        } catch (Exception exc)
        {
            exc.printStackTrace(System.out);
        }
	}

	public void playChopSound()
	{
		try
        {
            URL url = this.getClass().getClassLoader().getResource("sounds/chopSound.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
            clip.start();
        } catch (Exception exc)
        {
            exc.printStackTrace(System.out);
        }
	}

	public void playQuestCompleteSound()
	{
		try
        {
            URL url = this.getClass().getClassLoader().getResource("sounds/questCompleteSound.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
            clip.start();
        } catch (Exception exc)
        {
            exc.printStackTrace(System.out);
        }
	}

	public void playRainSound()
	{
		rainClip.start();
		rainClip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stopRainSound()
	{
		rainClip.stop();
	}

	public void playIslandSound()
	{
		islandSoundClip.start();
		islandSoundClip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stopIslandSound()
	{
		islandSoundClip.stop();
	}

	public void playWindSound()
	{
		windClip.start();
		windClip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stopWindSound()
	{
		windClip.stop();
	}

//implement methods of the KeyListener
	public void keyPressed(KeyEvent e)
	{
		//System.out.println( "key pressed: " + e.getKeyCode()  );

		if (!dialogueOn && startPlay && !endScene) // movement
		{
			if ( (e.getKeyCode() == 39 || e.getKeyCode() == 68) ) //&& (xDiff-p1.getWidth()) > -800)
			{ // right arrow key or d
				//xDiff -= stepSize;
				moveLeft = true;
				if ((xDiff-p1.getWidth()) < -800)
					moveLeft = false;
			}
			if ( (e.getKeyCode() == 37 || e.getKeyCode() == 65) ) //&& (xDiff+p1.getWidth()) < 800)
			{ // left arrow key or a
				//xDiff += stepSize;
				moveRight = true;
				if ((xDiff+p1.getWidth()) > 800)
					moveRight = false;
			}
			if ( (e.getKeyCode() == 38 || e.getKeyCode() == 87) ) //&& (yDiff+p1.getHeight()) < 600)
			{ // up arrow key or w
				//yDiff += stepSize;
	   			moveUp = true;
				if ((yDiff+p1.getHeight()) > 600)
					moveUp = false;
			}
			if ( (e.getKeyCode() == 40 || e.getKeyCode() == 83) ) //&& (yDiff-p1.getHeight()) > -600)
			{ // down arrow key or s
				//yDiff -= stepSize;
				moveDown = true;
				if ((yDiff-p1.getHeight()) < -600)
					moveDown = false;
			}
			//System.out.println("xDiff: "+xDiff+ "\tyDiff: "+yDiff);
		}

	// enter key: advance dialogue
		if (dialogueOn && e.getKeyCode() == 10) // enter key
		{
			if (dialogueNum == 0) // after "what a nice day"
			{
				playRainSound();
			}
			else if (dialogueNum == 1) // after "...nevermind"
			{
				crash = true;
				stopWindSound();
			}
			else if (dialogueNum == 2) // after "It's getting hard to see."
			{
				boulderX = 435;
			}
			else if (dialogueNum == 3) // after "*CRASH*"
			{
				introScene = false;
				wakeUp = true; // shows dark screen
				stopRainSound();
			}
			else if (dialogueNum == 6) // after "you should open ur eyes"
			{
				dialogueOn = false;
				playIslandSound();
			}
			else if (dialogueList.get(dialogueNum).equals("Yesterday, a bird stole my sack and left 10 coins scattered around the island. If you bring them to me, I'll help you build a new boat.")) // after hermit dialogue part 1
			{
				startQuests = true;
				System.out.println("Starting quest 1");
			}
			else if (dialogueList.get(dialogueNum).equals("Once you've got it, gather 5 pieces of wood. Come back when you're done.")) // after hermit dialogue part 2
			{
				questNum = 2;
				System.out.println("Starting quest 2");
			}
			else if (dialogueList.get(dialogueNum).equals("Good luck, and happy sailing!")) // after hermit dialogue part 3
			{
				questNum = 3;
				hermitSpeaks = false;
				System.out.println("Starting quest 3");
			}
			else if (dialogueList.get(dialogueNum).equals("Building . . . Your boat is in progress . . ."))
			{
				fadeToBlack = true;
				startQuests = false;
				dialogueOn = false;
				dialogueList.add("And we're off!");
				dialogueList.add("Wow, what an adventure.");
				dialogueList.add("All this exploring has worn you out. Time to go home and take a nap.");
				dialogueList.add("Until next time . . .");
			}
			else if (dialogueList.get(dialogueNum).equals("Until next time . . ."))
			{
				fadeToBlack = true;
			}

			if (dialogueNum < dialogueList.size()-1)
			{
				dialogueNum++;
			}
			else
			{
				dialogueOn = false;
				dialogueNum = dialogueList.size();
			}
		}

	// check collisions between player and wood
		/*for (int i=0; i<groundItems.size(); i++)
		{
			if (startQuests) // only collect if questing
			{
				if ( p1.checkCollision(groundItems.get(i), xDiff, yDiff) )
				{
					if (inventory.size() >= 10)
					{
						System.out.println("Inventory full");
					}
					else
					{
						if (groundItems.get(i).getName().equals("wood"))
						{
							playPickUpSound();
							inventory.add(groundItems.get(i)); // add to inventory
							groundItems.remove(i); // remove item from ground
							i--;
						}
					}
				}
			}
		}*/

		if (e.getKeyCode() == 69) // key e, chop tree
		{
			if (!obtainedAxe)
				System.out.println("No axe available");
			else
			{
				for (int i=0; i<trees.length; i++)
				{
					if (p1.checkCollision(trees[i], xDiff, yDiff) && trees[i].getWoodStatus())
					{
						groundItems.add(new Wood(trees[i].getX()+20, trees[i].getY()+trees[i].getWidth()+10));
						trees[i].updateWoodStatus(false);
						System.out.println("Tree chopped");
						playChopSound();
					}
				}
			}
		}

		if (e.getKeyCode() == 80) // cheat key p
		{
			if (startQuests)
			{
				if (!quest1Done && questNum == 1)
				{
					for (int i=0; i<groundItems.size(); i++)
					{
						if (groundItems.get(i).getName().equals("coin"))
						{
							inventory.add(groundItems.get(i));
							groundItems.remove(i);
							i--;
						}
					}
					System.out.println("Cheat q1 activated");
				}
				else if (questNum == 2 && quest1Done && !quest2Done)
				{
					obtainedAxe = true;
					for (int i=0; i<groundItems.size(); i++)
					{
						if (groundItems.get(i).getName().equals("axe"))
						{
							inventory.add(groundItems.get(i));
							groundItems.remove(i);
							i--;
						}
					}
					for (int i=0; i<5; i++)
					{
						inventory.add(new Wood(0,0));
					}
					System.out.println("Cheat q2 activated");
				}
			}
		}

	// check if quests are complete
		int coinCount = 0;
		for (int i=0; i<inventory.size(); i++)
		{
			if (inventory.get(i).getName().equals("coin"))
				coinCount++;
		}
		if (quest1Done)
		{
			coinCount = 0;
		}
		if (coinCount == 10)
		{
			quest1Done = true;
			System.out.println("Quest 1 complete!");
			playQuestCompleteSound();
			hermitSpeaks = false;
			dialogueList.add("You finished this quest. Go talk to the hermit.");
			dialogueNum = dialogueList.size()-1;
			dialogueOn = true;
		}

		int woodCount = 0;
		for (int i=0; i<inventory.size(); i++)
		{
			if (inventory.get(i).getName().equals("wood"))
				woodCount++;
		}
		if (quest2Done)
			woodCount = 0;
		if (woodCount >= 5)
		{
			quest2Done = true;
			System.out.println("Quest 2 complete!");
			playQuestCompleteSound();
			hermitSpeaks = false;
			dialogueList.add("You finished this quest. Go talk to the hermit.");
			dialogueNum = dialogueList.size()-1;
			dialogueOn = true;
		}

		repaint();

	}

	public void keyReleased(KeyEvent e)
	{
		//System.out.println( "key released: " + e.getKeyCode()  );
		if (e.getKeyCode() == 39 || e.getKeyCode() == 68)
		{ // left arrow key or a
			moveLeft = false;
		}
		if (e.getKeyCode() == 37 || e.getKeyCode() == 65)
		{ // right arrow key or d
			moveRight = false;
		}
		if (e.getKeyCode() == 38 || e.getKeyCode() == 87)
		{ // up arrow key or w
			moveUp = false;
		}
		if (e.getKeyCode() == 40 || e.getKeyCode() == 83)
		{ // down arrow key or s
			moveDown = false;
		}
		repaint();
	}

	public void keyTyped(KeyEvent e) {}

/* clickity click stuff */
	public void mouseClicked(MouseEvent e)
	{
		if (!startPlay) // on title screen
		{
			if (howToPlay) // how to play panel
			{
				if (e.getX() > 215
					&& e.getX() < (220+questFm.stringWidth("< Back"))
					&& e.getY() > (180-questFm.getAscent()) && e.getY() < 185)
				{ // back button
					howToPlay = false;
				}
			}
			// title screen
			if (e.getX() > h2pX
				&& e.getX() < (h2pX+subtitleFm.stringWidth("How to Play"))
				&& e.getY() > 370 && e.getY() < 410)
			{ // 'how to play' text
				howToPlay = true;
			}
			if (e.getX() > startGameX
				&& e.getX() < (startGameX+subtitleFm.stringWidth("Start Game"))
				&& e.getY() > 420 && e.getY() < 460)
			{ // 'start game' text
				startPlay = true;
				dialogueOn = true;
				introScene = true;
			}
		}
		else // during gameplay
		{
			if (e.getX() > (hermit.getX()+xDiff)
				&& e.getX() < (hermit.getX()+hermit.getWidth()+xDiff)
				&& e.getY() > (hermit.getY()+yDiff)
				&& e.getY() < (hermit.getY()+hermit.getHeight()+yDiff) )
			{ // click on hermit character
				hermitSpeaks = true;
				System.out.println("Interacted with hermit");
				if (startQuests == false) // hermit dialogue part 1
				{
					hermit.getDialogue1(dialogueList);
					dialogueOn = true;
				}
				else if (startQuests && !quest1Done)
				{
					hermit.getExtraDialogue1(dialogueList);
					dialogueOn = true;
				}
				else if (quest1Done && questNum == 1)
				{
					for (int i=0; i<inventory.size(); i++) // 'give' coins to hermit
					{
						inventory.remove(i);
						i--;
					}
					inventoryFull = false;
					hermit.getDialogue2(dialogueList);
					dialogueOn = true;
				}
				else if (questNum == 2 && !quest2Done)
				{
					hermit.getExtraDialogue2(dialogueList);
					dialogueOn = true;
				}
				else if (questNum == 2 && quest2Done)
				{
					hermit.getDialogue3(dialogueList);
					dialogueOn = true;
				}
				else if (questNum == 3)
				{
					hermit.getExtraDialogue3(dialogueList);
					dialogueOn = true;
				}
			}

			if (e.getX() > (275+xDiff) && e.getX() < (485+xDiff)
				&& e.getY() > (825+yDiff) && e.getY() < (1075+yDiff)
				&& questNum == 3 && startQuests)
			{ // click on dock
				hermitSpeaks = false;
				System.out.println("Dock clicked");
				for (int i=0; i<inventory.size(); i++)
				{
					if (inventory.get(i).getName().equals("wood"))
					{
						inventory.remove(i);
						i--;
					}
				}
				playQuestCompleteSound();
				System.out.println("Quest 3 complete!");
				dialogueList.add("Building . . . Your boat is in progress . . .");
				dialogueNum = dialogueList.size()-1;
				dialogueOn = true;
				startQuests = false;
			}

		}

		repaint();
	}

	public void mouseMoved(MouseEvent e)
	{
		if (!startPlay) // on title screen
		{
			// if mouse hovers over the 'how to play' or 'start game' text areas, change the color
			if (e.getX() > h2pX && e.getX() < (h2pX+subtitleFm.stringWidth("How to Play")) && e.getY() > 370 && e.getY() < 410)
			{ // how to play text
				howToPlayTextColor = textHighlightColor;
			}
			else
			{
				howToPlayTextColor = textDefaultColor;
			}
			if (e.getX() > startGameX && e.getX() < (startGameX+subtitleFm.stringWidth("Start Game")) && e.getY() > 420 && e.getY() < 460)
			{ // start game text
				startGameTextColor = textHighlightColor;
			}
			else
			{
				startGameTextColor = textDefaultColor;
			}
			if (howToPlay && e.getX() > 215
				&& e.getX() < (220+questFm.stringWidth("< Back"))
				&& e.getY() > (180-questFm.getAscent()) && e.getY() < 185)
			{ // back button
				backButtonColor = new Color(209, 158, 19);
			}
			else
			{
				backButtonColor = new Color(0,0,0);
			}
		}

		repaint();
	}

	public void mouseDragged(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

}
