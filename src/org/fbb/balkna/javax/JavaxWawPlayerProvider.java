package org.fbb.balkna.javax;

import java.net.URL;
import org.fbb.balkna.model.WavPlayer;
import org.fbb.balkna.model.WavPlayerProvider;

/**
 *
 * @author jvanek
 */
public class JavaxWawPlayerProvider implements  WavPlayerProvider{

    public JavaxWawPlayerProvider() {
    }

    @Override
    public WavPlayer createPlayer(URL u) {
        return new WavPlayerImpl(u);
    }
    
}
