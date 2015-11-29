package org.fbb.balkna.javax;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.fbb.balkna.model.SoundProvider;
import org.fbb.balkna.model.WavPlayer;
import org.fbb.balkna.model.utils.IoUtils;

public class WavPlayerImpl implements WavPlayer{

    private static final int BUFFER_SIZE = 128000;
    private final URL u;

    public static void main(String... args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        WavPlayerImpl w2 = new WavPlayerImpl(IoUtils.getFile(SoundProvider.getDefaultSoundPack(), "endChange.wav"));
        w2.playAsync();
    }

    WavPlayerImpl(URL u) {
        this.u = u;
    }

    @Override
    public void playAsync() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                playSound();
            }
        }).start();
    }

    @Override
    public void playSound() {
        //jdk6:(
        InputStream is = null;
        try {
            is = u.openStream();
            playSound(new BufferedInputStream(is));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public static void playSound(InputStream is) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(is);

        AudioFormat audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        //jdk6
        SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
        try {
            sourceLine.open(audioFormat);

            sourceLine.start();

            int nBytesRead = 0;
            byte[] abData = new byte[BUFFER_SIZE];
            while (nBytesRead != -1) {
                nBytesRead = audioStream.read(abData, 0, abData.length);

                if (nBytesRead >= 0) {
                    int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                }
            }

            sourceLine.drain();
        } finally {
            sourceLine.close();
        }
    }
}
