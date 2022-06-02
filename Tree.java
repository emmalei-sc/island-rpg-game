import java.awt.Color;
import java.awt.Graphics;

public class Tree
{
	private int x;
	private int y;
	private Color lightGreen;
	private Color darkGreen;
	private boolean containsWood;

	public Tree(int x, int y)
	{
		this.x = x;
		this.y = y;

		lightGreen = new Color(12, 117, 24);
		darkGreen = new Color(7, 74, 15);
		containsWood = true;
	}


	public void drawMe(Graphics g, int xDiff, int yDiff)
	{
        // trunk
        g.setColor(new Color(51, 35, 2));
        g.fillRoundRect(x+xDiff, y+yDiff, 10, 17, 5, 5);

		if (containsWood)
		{
			// leaves
			g.setColor(darkGreen);
	        g.fillOval(x-17+xDiff, y-25+yDiff, 45, 30);
	        g.fillOval(x-12+xDiff, y-35+yDiff, 35, 25);
	        g.fillOval(x-7+xDiff, y-42+yDiff, 25, 20);
	        g.fillOval(x-2+xDiff, y-45+yDiff, 15,15);
		}
	}

	public void drawShadow(Graphics g, int xDiff, int yDiff)
	{
		// shadow
        g.setColor(new Color(0.2f,0.2f,0.2f, 0.3f));
        g.fillOval(x-6+xDiff, y+11+yDiff, 30, 15);
	}

	public void updateWoodStatus(boolean cw)
	{
		containsWood = cw;
	}

	public boolean getWoodStatus()
	{
		return containsWood;
	}

	public int getX()
	{
		return x-17; // widest part of tree
	}

	public int getY()
	{
		return y-45; // topmost part of tree 
	}

	public int getWidth()
	{
		return 45;
	}

	public int getHeight()
	{
		return 62;
	}

}
