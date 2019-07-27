package tank;

import java.awt.*;

public class Explode {
    private int x,y;
    private TankClient tc;
    private boolean live = true;

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public Explode(int x, int y, TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    int [] diameter = {4,7,12,18,26,32,49,30,14,6};//爆炸圆的半径

    int step = 0;
    public void paint(Graphics g){
        if(step>=diameter.length){
            live = false;
            step=0;
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.ORANGE);
        g.fillOval(x,y,diameter[step],diameter[step]);
        g.setColor(c);
        step++;
    }
}
