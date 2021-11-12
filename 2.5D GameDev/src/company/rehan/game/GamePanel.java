package company.rehan.game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable{

    public static int width;
    public static int height;

    public Thread thread;
    private boolean running = false;

    private BufferedImage img;
    private  Graphics2D g;

    public GamePanel(int width, int height){
        this.width = width;
        this.height = height;

        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        requestFocus();

    }

    public void addNotify(){
        super.addNotify();

        if(thread == null){
            thread = new Thread(this, "GameThread");
            thread.start();
        }
    }

    public void init(){
        running = true;

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) img.getGraphics();
    }


    public void run(){

        init();

        final double GAME_HERTZ = 60.0;
        final double TBU = 1000000000 / GAME_HERTZ; // Time Before Update

        final int MUBR = 5; // Most Updates Before Render

        double lastUpdateTime = System.nanoTime();
        // 1* 10^-9
        double lastRenderTime;

        final double TARGET_FPS = 60;
        final double TTBR = 1000000000 / TARGET_FPS; // Total Time Before Render

        int frameCount = 0;
        int lastSecondTime = (int) (lastUpdateTime / 1000000000);
        int oldFrameCount = 0;

        while(running){
            double now = System.nanoTime();
            int updateCount = 0;
            while(((now - lastUpdateTime) > TBU) && (updateCount < MUBR)){
                update();
                input();
                lastUpdateTime += TBU;
                updateCount ++;
            }

            if(now - lastUpdateTime > TBU) {
                lastUpdateTime = now - TBU;
            }

            input();
            render();
            draw();
            lastRenderTime = now;
            frameCount++;

            int thisSecond = (int) (lastUpdateTime / 1000000000);
            if(thisSecond > lastSecondTime) {
                if(frameCount != oldFrameCount) {
                    System.out.println("NEW SECOND " + thisSecond + " " + frameCount);
                    oldFrameCount = frameCount;
                }
                frameCount = 0;
                lastSecondTime = thisSecond;
            }

            while(now - lastRenderTime < TTBR && now - lastUpdateTime < TBU){
                Thread.yield();

                try{
                    Thread.sleep(1);
                } catch(Exception e){
                    System.out.println("ERROR: yielding thread");
                }

                now = System.nanoTime();

            }

        }

    }


    public void update(){

    }

    public void input(){

    }

    public void render(){
        if(g != null){
            g.setColor(new Color(66,134,244));
            g.fillRect(0,0,width,height);
        }
    }

    public void draw(){
        Graphics g2 = (Graphics) this.getGraphics();
        g2.drawImage(img, 0,0,width,height, null);
        g2.dispose();

    }


}
