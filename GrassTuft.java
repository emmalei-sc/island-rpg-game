import java.awt.Color;
import java.awt.Graphics;

public class GrassTuft
{
    private int x;
    private int y;
    private int gX;
    private int gY;
    private int gWidth;
    private int gHeight;

    public GrassTuft(Grass gr)
    {
        gX = gr.getX();
        gY = gr.getY();
        gWidth = gr.getWidth();
        gHeight = gr.getHeight();

        x = (int)(Math.random()*(.9*gWidth)+gX);
        y = (int)(Math.random()*(.9*gHeight)+gY);
    }

    public void drawMe(Graphics g, int xDiff, int yDiff)
    {
        g.setColor(new Color(80, 114, 85));
        g.fillRoundRect(x+xDiff, y+yDiff+2, 2, 5, 2, 2);
        g.fillRoundRect(x+3+xDiff, y+yDiff, 2, 6, 2, 2);
        g.fillRoundRect(x+6+xDiff, y+yDiff+2, 2, 5, 2, 2);
    }
}
