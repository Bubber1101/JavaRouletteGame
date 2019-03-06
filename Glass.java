import javax.swing.*;
import java.awt.*;

public class Glass extends JPanel {
    Baloon[] bArr;
    Timer bT;

    public Glass()
    {

        setVisible(true);
        bArr = new Baloon[10];



        for(int i=0; i<bArr.length; i++)
        {
            bArr[i] = new Baloon(0 + i*(60), 0);
        }

        bT = new Timer(50,
                e -> {
                    for(int i=0; i<bArr.length; i++)
                    {
                        bArr[i].goUp();

                    }
                    repaint();
                });
        bT.start();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for(int i=0; i<bArr.length; i++)
        {
            g.drawImage(bArr[i].img, bArr[i].x, bArr[i].y, this);
        }

    }
}
 class Baloon extends JLabel{
    int x, y;
    Image img;

    public Baloon(int pos_x, int pos_y)
    {
        x = pos_x;
        y = pos_y;
        img = new ImageIcon("src//balon.png").getImage();
    }
    public void goUp()
    {
        x += 10;
        y += 30;
    }

}
