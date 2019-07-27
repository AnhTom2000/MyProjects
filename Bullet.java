package tank;

import java.awt.*;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Bullet {
    private int x, y;
    public static final int Bullet_WIDTH = 10;
    public static final int Bullet_HEIGTH = 10;
    public static final int XSPEED = 13;
    public static final int YSPEED = 13;
    private boolean bL = false, bU = false, bR = false, bD = false;
    private boolean live = true;
    private boolean good = true;

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    private Direction dir = Direction.STOP;
    TankClient tc;
    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public Bullet(int x, int y, boolean good, Direction dir, TankClient tc) {
        this(x, y);
        this.dir = dir;
        this.tc = tc;
        this.good  = good;

    }

    public void move() {
        switch (dir) {
            case L:
                this.x -= XSPEED;
                break;
            case LU:
                this.x -= XSPEED;
                this.y -= YSPEED;
                break;
            case U:
                this.y -= YSPEED;
                break;
            case RU:
                this.x += XSPEED;
                this.y -= YSPEED;
                break;
            case R:
                this.x += XSPEED;
                break;
            case RD:
                this.x += XSPEED;
                this.y += YSPEED;
            case D:
                this.y += YSPEED;
                break;
            case LD:
                this.x -= XSPEED;
                this.y += YSPEED;
                break;
        }
        if (x < 0 || y < 0 || x > TankClient.WIDTH || y > TankClient.HEIGTH) {
            live = false;
        }
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void paint(Graphics g) {
        if (!live) {
            tc.b.remove(this);
            return;
        }
        Color c = g.getColor();
        if(good){
            g.setColor(Color.RED);
        }else {
            g.setColor(Color.WHITE);
        }
        g.fillOval(x, y, Bullet_WIDTH, Bullet_HEIGTH);
        g.setColor(c);
        move();
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, Bullet_WIDTH, Bullet_HEIGTH);
    }

    public boolean hitTank(Tank t) {
        if (this.live && this.getRect().intersects(t.getRect()) && this.good != t.isGood() && t.isLive()) {
            if(t.isGood()){
                t.setLife(t.getLife()-20);
                if(t.getLife()<=0){
                    t.setLive(false);
                }
            }else {
                t.setLive(false);
            }
            tc.b.remove(this);

            this.live = false;
            Explode e = new Explode(x, y, tc);
            tc.ex.add(e);
            return true;
        }
        return false;
    }

    public boolean hitTanks(List<Tank> tanks) {
        for (int i = 0; i < tanks.size(); i++) {
            if (hitTank(tanks.get(i))) {//判断是否打中敌人
                return true;
            }
        }
        return false;
    }
    public boolean hitWall(Wall w){
        if (this.live && this.getRect().intersects(w.getRect()) && this.isLive()){
            this.live =false;
            tc.b.remove(this);
            return true;
        }
        return false;
    }
}
