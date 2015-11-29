
package org.fbb.balkna.awt.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.fbb.balkna.model.ImagesSaver;

/**
 *
 * @author jvanek
 */
public class ImagesSaverImpl implements ImagesSaver {

    @Override
    public void writeExercisesImagesToDir(File imgDir, List<String> i) throws IOException {
        ImgUtils.writeExercisesImagesToDir(imgDir, i);
    }

    @Override
    public void writeTrainingsImagesToDir(File imgDir, List<String> is) throws IOException {
        ImgUtils.writeTrainingsImagesToDir(imgDir, is);
    }
    
}
