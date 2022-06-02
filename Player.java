import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player
{
	private int x;
	private int y;
	private int width;
	private int height;

	private BufferedImage playerImg;

	public Player(int x, int y)
	{
		this.x = x;
		this.y = y;

		this.width = 30;	//total width of the player
		this.height = 30;	//total height of the player

		try
		{
			playerImg = ImageIO.read(new File("images/mushroomPlayer.png"));
		} catch(IOException e)
		{
			System.out.println(e);
		}
	}

	public void drawMe(Graphics g)
	{
		// shadow
		g.setColor(new Color(0.2f,0.2f,0.2f, 0.2f));
		g.fillOval(x+(int)(.15*width), y+height-2, (int)(width*.75), 7);
		// player img
		g.drawImage(playerImg,x,y,width,height,null);
	}

	public void setXY(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public boolean checkCollision(Item item, int xDiff, int yDiff)
	{
		int pX = x;
		int pY = y;
		int pWidth = width;
		int pHeight = height;

		int tX = item.getX()+xDiff;
		int tY = item.getY()+yDiff;
		int tWidth = item.getWidth();
		int tHeight = item.getHeight();

		if( pX+pWidth >= tX && pX <= tX + tWidth  &&
			pY+pHeight >= tY && pY <= tY + tHeight )
		{
			//System.out.println("Collision w/ item: "+item.getName());
			return true;

		}

		return false;
	}

	public boolean checkCollision(Tree tr, int xDiff, int yDiff)
	{
		int pX = x;
		int pY = y;
		int pWidth = width;
		int pHeight = height;

		int tX = tr.getX()+xDiff;
		int tY = tr.getY()+yDiff;
		int tWidth = tr.getWidth();
		int tHeight = tr.getHeight();

		if( pX+pWidth >= tX && pX <= tX + tWidth  &&
			pY+pHeight >= tY && pY <= tY + tHeight )
		{
			//System.out.println("Collision w/ tree");
			return true;
		}

		return false;
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
