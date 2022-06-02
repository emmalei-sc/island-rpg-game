import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Wave
{
    private BufferedImage waveImg;
    private double x;
    private int y;

    public Wave(int x, int y)
    {
        try
		{
			waveImg = ImageIO.read(new File("images/waves.png"));
		} catch (IOException e)
		{
			System.out.println(e);
		}

        this.x = x;
        this.y = y;
    }

    public void drawMe(Graphics g)
    {
        g.drawImage(waveImg, (int)x, y, 90, 30, null);
    }

    public void move()
    {
        x -= .2;
        if (x < -100)
        {
            x = 810;
        }
    }
}
