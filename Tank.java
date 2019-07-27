package tank;

import java.awt.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {
    private int x, y;   //坦克有坐标
    public static final int Tank_WIDTH = 30;//坦克的大小
    public static final int Tank_HEIGTH = 30;
    public static final int XSPEED = 10;
    public static final int YSPEED = 10;
    private Direction dir = Direction.STOP;
    //添加记录按键状态的布尔量
    private boolean bL = false, bU = false, bR = false, bD = false;
    Bullet bullet = null;
    TankClient tc;
    private Direction ptdir = Direction.D;
    private boolean good = true;
    private boolean live = true;
    public static Random r = new Random();
    int step = 0;
    int oldX = 0, oldY = 0;
    private int life = 100;

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Tank(int x, int y, boolean good, Direction dir, TankClient tc) {
        this(x, y);
        this.dir = dir;
        this.tc = tc;
        this.good = good;
    }

    public void paint(Graphics g) {
        if (!live) return;
        Color c = g.getColor();
        if (good) {
            g.setColor(Color.MAGENTA);
            g.fillRect(x, y, 30, 30);
            g.setColor(Color.RED);
            g.fillRect(x, y - Tank_HEIGTH / 2, Tank_WIDTH * life / 100, 10);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, 30, 30);
        }
        g.fillOval(x, y, Tank_WIDTH, Tank_HEIGTH);
        g.setColor(c);

        switch (ptdir) {
            case L://炮筒方向，随着我们tank方法移动而发生变化
                g.drawLine(x + Tank.Tank_HEIGTH / 2, y + Tank.Tank_HEIGTH / 2, x, y + Tank.Tank_HEIGTH / 2);
                break;
            case LU:
                g.drawLine(x + Tank.Tank_WIDTH / 2, y + Tank.Tank_HEIGTH / 2, x, y);
                break;
            case U:
                g.drawLine(x + Tank.Tank_WIDTH / 2, y + Tank.Tank_HEIGTH / 2, x + Tank.Tank_WIDTH / 2, y);
                break;
            case RU:
                g.drawLine(x + Tank.Tank_WIDTH / 2, y + Tank.Tank_HEIGTH / 2, x + Tank.Tank_WIDTH, y);
                break;
            case R:
                g.drawLine(x + Tank.Tank_WIDTH / 2, y + Tank.Tank_HEIGTH / 2, x + Tank.Tank_WIDTH, y + Tank.Tank_HEIGTH / 2);
                break;
            case RD:
                g.drawLine(x + Tank.Tank_WIDTH / 2, y + Tank.Tank_HEIGTH / 2, x + Tank.Tank_WIDTH, y + Tank.Tank_HEIGTH);
                break;
            case D:
                g.drawLine(x + Tank.Tank_WIDTH / 2, y + Tank.Tank_HEIGTH / 2, x + Tank.Tank_WIDTH / 2, y + Tank.Tank_HEIGTH);
                break;
            case LD:
                g.drawLine(x + Tank.Tank_WIDTH / 2, y + Tank.Tank_HEIGTH / 2, x, y + Tank.Tank_HEIGTH);
                break;
        }
        move();
    }


    public void move() {
        oldX = x;
        oldY = y;
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
            case STOP:
                break;
        }
        if (dir != Direction.STOP) {
            ptdir = dir;
        }
        if (x < 0) x = 0;
        if (y < 30) y = 30;
        if (x + Tank_WIDTH > TankClient.WIDTH) {
            x = TankClient.WIDTH - Tank.Tank_WIDTH;
            if (!good) {
                Direction[] dirs = Direction.values();
                dir = dirs[r.nextInt(dirs.length)];
            }
        }
        if (y + Tank_HEIGTH > TankClient.HEIGTH) {
            y = TankClient.HEIGTH - Tank.Tank_HEIGTH;
            if (!good) {
                Direction[] dirs = Direction.values();
                dir = dirs[r.nextInt(dirs.length)];
            }
        }
        if (!good) {
            Direction[] dirs = Direction.values();

            if (step == 0) {
                step = r.nextInt(12) + 3;
                dir = dirs[r.nextInt(dirs.length)];
            }
            if (r.nextInt(45) > 43) this.fire();
            step--;
        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, Tank_WIDTH, Tank_HEIGTH);
    }

    public Bullet fire() {
        if (!live) return null;
        int x = this.x + Tank_WIDTH / 2 - bullet.Bullet_WIDTH / 2;
        int y = this.y + Tank_HEIGTH / 2 - bullet.Bullet_HEIGTH / 2;
        bullet = new Bullet(x, y, this.good, ptdir, tc);
        tc.b.add(bullet);
        return bullet;
    }

    public Bullet fire(Direction dir) {
        if (!live) return null;
        int x = this.x + Tank_WIDTH / 2 - bullet.Bullet_WIDTH / 2;
        int y = this.y + Tank_HEIGTH / 2 - bullet.Bullet_HEIGTH / 2;
        bullet = new Bullet(x, y, this.good, dir, tc);
        tc.b.add(bullet);
        return bullet;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_F3:
                if(!isGood()){
                    if(!isLive()){

                    }
                }
            case KeyEvent.VK_F2:
                if (isGood()) {
                    if (!isLive()) {
                        live = true;
                        life = 100;
                    }
                }
                break;
            case KeyEvent.VK_A:
                superFire();
                break;
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
        }
        locaDirection();
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_CONTROL:
                fire();
                break;
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
        }
        locaDirection();
    }

    public void locaDirection() {
        if (bL && !bU && !bR && !bD) dir = Direction.L;
        else if (bL && bU && !bR && !bD) dir = Direction.LU;
        else if (bL && !bU && !bR && bD) dir = Direction.LD;
        else if (!bL && bU && !bR && !bD) dir = Direction.U;
        else if (!bL && bU && bR && !bD) dir = Direction.RU;
        else if (!bL && !bU && bR && !bD) dir = Direction.R;
        else if (!bL && !bU && bR && bD) dir = Direction.RD;
        else if (!bL && !bU && !bR && bD) dir = Direction.D;
        else if (!bL && !bU && !bR && !bD) dir = Direction.STOP;
    }

    public void stay() {
        x = oldX;
        y = oldY;
    }

    public boolean hitWall(Wall w) {
        if (this.live && this.getRect().intersects(w.getRect()) && this.isLive()) {
            stay();
            return true;
        }
        return false;
    }

    public boolean withTank(List<Tank> tanks) {
        for (int i = 0; i < tanks.size(); i++) {//拿出每一辆tank
            Tank t = tanks.get(i);
            if (this != t) {//如果不是我们自身
                if (this.live && t.isLive() && this.getRect().intersects(t.getRect())) {//如果相撞了
                    this.stay();//停止
                    return true;
                }
            }
        }
        return false;
    }

    public void superFire() {
        Direction[] dirs = Direction.values();
        for (int i = 0; i < 8; i++) {
            fire(dirs[i]);
        }
    }
}

