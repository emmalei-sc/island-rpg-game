import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Coin extends Item
{
	private BufferedImage coinImg;

	public Coin(int x, int y)
	{
		super(x, y, 20, 20, "coin");
		try
		{
			coinImg = ImageIO.read(new File("images/coin.png"));
		} catch (IOException e)
		{
			System.out.println(e);
		}
	}

	@Override
	public void drawMe(Graphics g, int xDiff, int yDiff)
	{
		g.setColor(new Color(0.2f,0.2f,0.2f, 0.2f)); // shadow
		g.fillOval(getX()+xDiff, getY()+getHeight()+yDiff+5, getWidth(), 5);
		// coin img
		g.drawImage(coinImg,getX()+xDiff, getY()+yDiff, getWidth(), getHeight(), null);
	}

	@Override
	public void drawInventoryItem(Graphics g, int x, int y)
	{
		super.drawInventoryItem(g, x, y); // black border
		g.drawImage(coinImg,x+(50-getWidth())/2, y+(50-getHeight())/2, getWidth(), getHeight(), null);
	}

}
