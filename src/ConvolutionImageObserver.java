import java.awt.*;
import java.awt.image.ImageObserver;

public class ConvolutionImageObserver implements ImageObserver {
    ImageRendererComponent comp;
    ConvolutionImageObserver(ImageRendererComponent comp){
        this.comp = comp;
    }
    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        String s  = "";
        if ( (infoflags &ImageObserver.ALLBITS) != 0) {
            s = s + "Done";
            comp.repaint();
        }
        if ( (infoflags & ImageObserver.ABORT) != 0)
            s = s+ "Abort";
        if ( (infoflags & ImageObserver.ERROR) != 0)
            s = s + "Error";
        if ( (infoflags & ImageObserver.FRAMEBITS) != 0)
            s = s + "FrameBits";
        if ( (infoflags & ImageObserver.HEIGHT) != 0)
            s = s + "height";
        if ( (infoflags & ImageObserver.PROPERTIES) != 0)
            s = s+ "Properties";
        if ( (infoflags & ImageObserver.WIDTH ) != 0)
            s = s+ "Width";
        if ( (infoflags & ImageObserver.SOMEBITS ) != 0)
            s = s+ "SomeBits";
        return true;
    }
}