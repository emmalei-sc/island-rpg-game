import java.awt.Color;
import java.awt.Graphics;

public class Raindrop
{
	private int x;
	private int y;
	private Color blue = new Color(69, 101, 128);

	public Raindrop(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public void drawMe(Graphics g)
	{
		g.setColor(blue);
		g.fillRoundRect(x,y,2,8,5,5);
	}

	public void moveDown()
	{
		y += 5;
		if (y > 575)
		{
			y = -10;
			x = (int)(Math.random()*799);
		}
	}

}
