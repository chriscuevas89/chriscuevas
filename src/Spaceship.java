package src;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class Spaceship
{
    Game game;
    Toolkit tk;

    public int yPos;
    public int xPos;

    public double yVel = 0.0;
    public double yAcc = 0.1;

    public int width;
    public int height;
    private int xGrace = 12;
    private int yGrace = 6;

    private BufferedImage image;
    //private int animframe = 0;
    //private int animRate = 4;
    //private int frameCount = 0;
    
    
    
    private double x, y, angle;
    private boolean rotatingLeft, rotatingRight, accelerating;
    private boolean destroyed;

    public Spaceship(Game g, Toolkit tk, double x, double y) throws IOException {
        game = g;
        this.tk = tk;
        
        
        this.x = x;
        this.y = y;
        this.angle = Math.toRadians(90.0); // Pointing upwards
        loadImage();


        //BufferedImage image = ImageIO.read(new File("starfighter.png"));
        //width = tk.getScreenSize().width / 12;
        //height = (int)(((double)width / (double)image.getWidth()) * (image.getHeight() / 4));
        //xGrace = Math.max(xGrace, width / 14);
        //yGrace = Math.max(yGrace, height / 20);

        
/*        
        BufferedImage image = ImageIO.read(new File("bird.png"));

        width = tk.getScreenSize().width / 12;
        height = (int)(((double)width / (double)image.getWidth()) * (image.getHeight() / 4));
        xGrace = Math.max(xGrace, width / 14);
        yGrace = Math.max(yGrace, height / 20);

        int fragHeight = image.getHeight() / 4;

        for (int i = 0; i < 4; i++)
        {
            Image temp = image.getSubimage(0, i * fragHeight, image.getWidth(), fragHeight).getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
            birdImage[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics x = birdImage[i].getGraphics();
            x.drawImage(temp, 0, 0, null);
            x.dispose();
        }

        reset();
        
*/        
    }
/*
    public void drawShip(Graphics2D g2d)
    {

    	double rotation = Math.tanh(yVel / 8.0 - 0.2);
        AffineTransform at = new AffineTransform();
        at.rotate(rotation, width / 2, height / 2);
        AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        frameCount++;
        animRate = Math.max((int)(4 + yVel), 1);
        if (frameCount % animRate == 0)
            animframe++;
        if(animframe >= birdImage.length)
            animframe = 0;

        BufferedImage bird = ato.filter(birdImage[animframe], null);
        g.drawImage(bird, xPos, yPos, null);

        if (game.debug)
        {
            g2d.setColor(Color.RED);
            g2d.drawRect(xPos + xGrace, yPos + yGrace, width - xGrace * 2, height - yGrace * 2);
        }

        
        
        AffineTransform old = g2d.getTransform();
        g2d.translate(x, y);
        g2d.rotate(Math.toRadians(angle));
        //g2d.setColor(Color.RED);
        //g2d.drawPolygon(new int[]{-10, 10, -10}, new int[]{-10, 0, 10}, 3);
        //BufferedImage starfighter;
        g2d.drawImage(image, xPos, yPos, null);
        g2d.setTransform(old);

        if (game.debug)
        {
            g2d.setColor(Color.RED);
            g2d.drawRect((int) (x + xGrace), (int) (y + yGrace), width - xGrace * 2, height - yGrace * 2);
        }
    }

    public boolean collide(Asteroid pipe)
    {
        if (yPos < 0 || yPos + height > tk.getScreenSize().height)
        {
            collide();
            return true;
        }
        if (xPos + width - xGrace < pipe.xPos)
            return false;
        if (xPos + xGrace > pipe.xPos + pipe.width)
            return false;
        if (yPos + height - yGrace > pipe.yPos || yPos + yGrace < pipe.yPos - pipe.gap)
        {
            collide();
            return true;
        }
        return false;
    }
*/
    public void collide()
    {
        new Thread(() ->
        {
            try
            {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File("collide.wav").getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gain.setValue(20f * (float) Math.log10(game.volume));
                clip.start();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }).start();
    }

    // Load the PNG image
    private void loadImage() {
        try {
            image = ImageIO.read(new File("starfighter2.png"));
        } catch (IOException e) {
            System.err.println("Error loading enemy ship image: " + e.getMessage());
            image = null;
        }
    }

    // Draw the spaceship
    public void drawShip(Graphics2D g2d) {
        if (image != null) {
            // Save the current transform
            var originalTransform = g2d.getTransform();

            // Rotate and draw the image
            g2d.translate(x, y);
            g2d.rotate(Math.toRadians(angle));
            // Draw the image centered at the position
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            g2d.drawImage(image, -imageWidth / 2, -imageHeight / 2, null);
            //g2d.rotate(angle);
            //g2d.drawImage(image, image.getWidth(), image.getHeight(), null);

            // Restore the original transform
            g2d.setTransform(originalTransform);
        }

        if (game.debug) {
            g2d.setColor(Color.RED);
            g2d.drawRect((int) x - image.getWidth()/2, (int) y - image.getHeight()/2, image.getWidth(), image.getHeight());
        }
    }

    public void pewpew()
    {
        //yVel -= 5.0;

        new Thread(() ->
        {
            try
            {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File("flap.wav").getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gain.setValue(20f * (float) Math.log10(game.volume));
                clip.start();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }).start();
    }

    public void reset()
    {
        x = (double) tk.getScreenSize().width / 2;
        y = (double) tk.getScreenSize().height / 2;

        //yVel = 0.0;
    }

    public void update()
    {
        //yVel += yAcc;
        //yPos += yVel;
        
        
        
        if (rotatingLeft) {
            angle -= 2;
        }
        if (rotatingRight) {
            angle += 2;
        }
        if (accelerating) {
            x += Math.cos(Math.toRadians(angle));
            y += Math.sin(Math.toRadians(angle));
        }
        // Screen wrapping
        if (x < 0) x = tk.getScreenSize().width;
        if (x > tk.getScreenSize().width) x = 0;
        if (y < 0) y = tk.getScreenSize().height;
        if (y > tk.getScreenSize().height) y = 0;

        //System.out.println("Spaceship position: x=" + x + ", y=" + y + ", angle=" + Math.toDegrees(angle));
    }
    
//    public Rectangle getBounds() {
//        return new Rectangle((int) x - 10, (int) y - 10, 20, 20);
//    }

    // Get the bounds for collision detection
    public Rectangle getBounds() {
        int width = image != null ? image.getWidth() : 0;
        int height = image != null ? image.getHeight() : 0;
        return new Rectangle((int) x - width / 2, (int) y - height / 2, width, height);
    }

    public void setRotatingLeft(boolean rotatingLeft) {
        this.rotatingLeft = rotatingLeft;
    }

    public void setRotatingRight(boolean rotatingRight) {
        this.rotatingRight = rotatingRight;
    }

    public void setAccelerating(boolean accelerating) {
        this.accelerating = accelerating;
    }

    public Bullet shoot()

    {
        pewpew();
    	return new Bullet(x, y, angle);
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
