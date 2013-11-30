/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.c3ma.noisetest;

import de.c3ma.animation.noise.PerlinNoise;
import de.c3ma.fullcircle.dyn.Dynamic;
import de.c3ma.fullcircle.dyn.OnFullcirclePaint;
import de.c3ma.fullcircle.io.Sequence;
import de.c3ma.proto.fctypes.Frame;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.texgen.gui.TextureImage;
import org.texgen.textures.Texture;

/**
 *
 * @author tobi
 */
public class NoiseTest extends JFrame implements Runnable, OnFullcirclePaint {

    private BufferedImage image;
    private int width = 20;
    private int height = 20;
    private float dimm = 1f;
    //private float step = 0.01f;
    private float offsetX = 42f;
    private float offsetY = 23f;
    private float osX = 0.00001f;
    private float osY = 0.00001f;
    private long sleep = 83; 
    private TextureImage texture;
    private Texture texturePattern;
    private long timeToRun;
    private File output;
    private int fps;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException, IOException, CmdLineException {
        CommandLineValues values = new CommandLineValues(args);
        CmdLineParser parser = new CmdLineParser(values);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.exit(1);
        }

        JFrame frame = new NoiseTest(values);
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        if (values.hasGui()) {
            frame.setVisible(true);
        }

    }

    public NoiseTest(CommandLineValues values) throws UnknownHostException, IOException {
        width = values.getWidth();
        height = values.getHeight();
        timeToRun = values.getTime();
        dimm = (float) values.getDimm();
        output = values.getOutput();
        offsetX = values.getOffsetX();
        offsetY = values.getOffsetY();
        osX = values.getOsX();
        osY = values.getOsY();
        sleep = values.getSleep(); 
        fps = values.getFps();

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        texture = new TextureImage(width, height);
        texturePattern = new TestTexture();
        ((TestTexture) texturePattern).dimm = dimm;
        
        ((TestTexture) texturePattern).offsetU = offsetX;
        ((TestTexture) texturePattern).offsetV = offsetY;

        if (values.getRemoteAddress() != null) {
            Dynamic d = new Dynamic(values.getRemoteAddress());
            d.setOnPaint(this);
        }

        new Thread(this).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image,
                10, 30, 480, 400,
                0, 0, width, height,
                this);
    }

    @Override
    public void run() {
        double fooX = 0, fooY = 0;
        long starttime = new Date().getTime();
        Sequence s = null;
        if (output != null) {
            s = new Sequence(fps, width, height);
        }

        while (true) {
            System.gc();
            texture.renderAndWait(texturePattern);

            image = texture.getImage();


            ((TestTexture) texturePattern).offsetU += 0.003 * Math.sin(fooX + PerlinNoise.noise(fooX, fooX / 2, 3.0));
            ((TestTexture) texturePattern).offsetV += 0.003 * Math.cos( fooY + PerlinNoise.noise(fooY / 2, fooY, 5));
            fooX += osX;
            fooX += osX;
            //System.out.println("x: " + ((TestTexture) texturePattern).offsetU + "; y: " + ((TestTexture) texturePattern).offsetV );
            

            this.repaint();
            if (output != null && s != null) {
                Frame f = new Frame(width, height);
                for(int y = 0; y < height; y++) {
                    for(int x = 0; x < width; x++) { 
                        Color c = new Color(image.getRGB(x, y));
                        f.updatePixel(c.getRed(), c.getGreen(), c.getBlue(), x, y);
                    }
                }
                s.addFrame(f);
            }
            try {
                if (sleep > 0)
                {
                    Thread.sleep(sleep);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(NoiseTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (timeToRun > 0) {
                if ((starttime + timeToRun) < new Date().getTime()) {
                    break;
                }
            }
        }

        if (output != null && s != null) {
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(output);
                fos.write(s.serialize());
                fos.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(NoiseTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(NoiseTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.exit(0);
    }

    @Override
    public void paint(Graphics g, int width, int height) {
        if ((this.width != width) || (this.height != height)) {
            image = new BufferedImage(
                    (width % 2 != 0) ? width + 1 : width,
                    (height % 2 != 0) ? height + 1 : height,
                    BufferedImage.TYPE_INT_RGB);
            this.width = width;
            this.height = height;

            texture.resize(
                    (width % 2 != 0) ? width + 1 : width,
                    (height % 2 != 0) ? height + 1 : height);
        }

        g.drawImage(image,
                0, 0, width, height,
                0, 0, width, height, null);
    }
}
