/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author ThienDinh
 */
public class Audio implements Runnable {

    private File audioFile;

    public Audio() {
        audioFile = new File("kin.wav");
    }

    public void setVoiceLink(URL url) {
        try {
            boolean done = false;
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(audioFile);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
        } catch (Exception ex) {
            System.out.println("Can not create kin.wav:" + ex.getMessage());
        }
    }

    @Override
    public void run() {
        Clip clip = null;
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = stream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
            while (true) {
                Thread.sleep(50);
            }
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("Clip ERROR!!!" + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Clip ERROR!!!" + ex.getMessage());
        } catch (LineUnavailableException ex) {
            System.out.println("Clip ERROR!!!" + ex.getMessage());
        } catch (InterruptedException ex) {
            clip.close();
        }
    }
}
