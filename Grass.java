import java.awt.Color;
import java.awt.Graphics;

public class Grass
{
    private int x;
    private int y;
    private int width;
    private int height;

    public Grass(int x, int y)
    {
        this.x = x;
        this.y = y;
        width = (int)(Math.random()*300+300);
        height = (int)(Math.random()*200+200);
    }

    public void drawMe(Graphics g, int xDiff, int yDiff)
    {
        g.setColor(new Color(105, 153, 93));
		g.fillRoundRect(x+xDiff, y+yDiff, width, height, 100, 100);
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
}
