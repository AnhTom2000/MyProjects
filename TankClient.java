package tank;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankClient extends Frame {
    public static final int WIDTH = 800;
    public static final int HEIGTH = 600;
    Tank mytank = new Tank(700, 450, true, Direction.STOP, this);
    List<Tank> enemy = new ArrayList<Tank>();
    Bullet bullet = null;
    List<Bullet> b = new ArrayList<Bullet>();
    Image offScreenImage = null;
    List<Explode> ex = new ArrayList<Explode>();
    Wall w1 = new Wall(200, 220, 30, 300);
    Wall w2 = new Wall(0, 220, 200, 30);
    Wall w3 = new Wall(220, 220, 70, 30);
    Wall w4 = new Wall(400, 220, 30, 30);
    Wall w5 = new Wall(550, 220, 30, 300);
    Wall w6 = new Wall(770, 220, 30, 30);
    Wall w7 = new Wall(580, 220, 50, 30);

    @Override
    public void paint(Graphics g) {
        g.drawString("bullet count；" + b.size(), 10, 50);
        g.drawString("Life  count:"+mytank.getLife(),10,70);
        if (enemy.size() <= 0) {
            for (int i = 0; i < 5; i++) {
                enemy.add(new Tank(50 + 70 * (i + 1), 50, false, Direction.D, this));
            }
        }
        if (mytank.isLive()) {
            mytank.hitWall(w1);
            mytank.hitWall(w2);
            mytank.hitWall(w3);
            mytank.hitWall(w4);
            mytank.hitWall(w5);
            mytank.hitWall(w6);
            mytank.hitWall(w7);
            mytank.paint(g);
        }
        for (int i = 0; i < b.size(); i++) {
            bullet = b.get(i);
            bullet.hitTank(mytank);
            bullet.hitTanks(enemy);
            bullet.hitWall(w1);
            bullet.hitWall(w2);
            bullet.hitWall(w3);
            bullet.hitWall(w4);
            bullet.hitWall(w5);
            bullet.hitWall(w6);
            bullet.hitWall(w7);
            bullet.paint(g);
        }

        for (int i = 0; i < enemy.size(); i++) {
            Tank t = enemy.get(i);
            if (t.isLive()) {
                t.hitWall(w1);
                t.hitWall(w2);
                t.hitWall(w3);
                t.hitWall(w4);
                t.hitWall(w5);
                t.hitWall(w6);
                t.hitWall(w7);
                t.withTank(enemy);
                t.paint(g);
            }
        }
        for (int i = 0; i < ex.size(); i++) {
            Explode e = ex.get(i);
            if (e.isLive()) {
                e.paint(g);
            }
        }
        w1.paint(g);
        w2.paint(g);
        w3.paint(g);
        w4.paint(g);
        w5.paint(g);
        w6.paint(g);
        w7.paint(g);
    }

    /*为什么会闪烁，刷新重画频率太快，paint方法还没有完成*/
    /*解决闪烁现象，将所有东西花在虚拟图片上，使用双缓冲解决*/
    @Override
    public void update(Graphics g) {//g画笔是paint里的画笔
        if (offScreenImage == null) {//初始化图片容器
            offScreenImage = this.createImage(WIDTH, HEIGTH);//将图片传进去
        }
        //背后图片画笔
        Graphics gOffScreen = offScreenImage.getGraphics();//获取图片
        gOffScreen.fillRect(0, 0, WIDTH, HEIGTH);
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.GREEN);
        c = gOffScreen.getColor();
        paint(gOffScreen);//将图片传入paint
        g.drawImage(offScreenImage, 0, 0, null);//画出图片

    }

    public void lunchFrame() {
        for (int i = 0; i < enemy.size(); i++) {
            enemy.add(new Tank(40, 50, false, Direction.D, this));
        }
        this.setLocation(400, 300);
        this.setSize(WIDTH, HEIGTH);
        this.setTitle("TankCombat");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setResizable(false);
        setVisible(true);
        this.addKeyListener(new KeyMonitor());//键盘事件监听器
        new Thread(new PaintThread()).start();
    }

    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.lunchFrame();
    }

    //内部类直接调用重画方法。但是要考虑到一直重画，就开启一个线程去监听
    private class PaintThread implements Runnable {//内部类  可以直接调用方法外部类封装的方法

        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class KeyMonitor extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            mytank.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            mytank.keyReleased(e);
        }
    }
}
