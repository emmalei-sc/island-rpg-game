import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Wood extends Item
{
    private BufferedImage woodImg;

    public Wood(int x, int y)
    {
        super(x,y,30,15,"wood");
        try
        {
            woodImg = ImageIO.read(new File("images/wood.png"));
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }

    @Override
    public void drawMe(Graphics g, int xDiff, int yDiff)
    {
        g.drawImage(woodImg, getX()+xDiff, getY()+yDiff, getWidth(), getHeight(), null);
    }

    @Override
    public void drawInventoryItem(Graphics g, int x, int y)
    {
        super.drawInventoryItem(g,x,y);
        g.drawImage(woodImg,x+(50-getWidth())/2, y+(50-getHeight())/2, getWidth(), getHeight(), null);
    }
}
