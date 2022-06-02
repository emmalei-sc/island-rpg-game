import java.awt.Color;
import java.awt.Graphics;

public class Item
{
	private int x;
	private int y;
	private int width;
	private int height;
	private String name;

	public Item(int x, int y, int width, int height, String name)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
	}

	public void drawMe(Graphics g, int xDiff, int yDiff)
	{
		g.setColor(Color.black);
		g.fillRect(x+xDiff, y+yDiff, width, height);
	}

	public void drawInventoryItem(Graphics g, int x, int y)
	{
		// draw border of an inventory item
		g.setColor(new Color(230,230,230));
		g.drawRect(x, y, 50, 50);
	}


	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public String getName()
	{
		return name;
	}


}
