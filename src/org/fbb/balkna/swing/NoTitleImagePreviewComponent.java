package org.fbb.balkna.swing;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 *
 * @author jvanek
 */
class NoTitleImagePreviewComponent extends ImagePreviewComponent {

    public NoTitleImagePreviewComponent(List<BufferedImage> srcs) {
        super(srcs);
    }

    public NoTitleImagePreviewComponent(BufferedImage src) {
        super(src);
    }

    public NoTitleImagePreviewComponent() {
        super();

    }

    protected void adjsutSelectedTo1() {
        selcted = 0;
        repaint();

    }

    @Override
    public void setSrcs(List<BufferedImage> nws) {
        boolean changed = false;
        if (nws == null && srcs == null) {
            changed = false;
        } else if ((nws != null && srcs == null) || (nws == null && srcs != null)) {
            changed = true;
        } else if (nws.size() != srcs.size()) {
            changed = true;
        } else {
            for (int i = 0; i < srcs.size(); i++) {
                if (srcs.size()>1 && i==0){
                    //ignore composed one
                    continue;
                }
                BufferedImage old = srcs.get(i);
                BufferedImage nwr = nws.get(i);
                if (old != nwr) {
                    changed = true;
                    break;
                }

            }
        }
        if (changed) {
            this.srcs = nws;
            adjsutSelectedTo1();
        }
        if (changed) {
            super.setSrcs(nws);
        }
    }

}
