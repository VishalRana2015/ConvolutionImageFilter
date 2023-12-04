import javax.swing.*;
import java.awt.*;
import java.awt.image.FilteredImageSource;

public class ImageRendererComponent extends JComponent {
    Image image;
    ImageIcon icon;
    MediaTracker tracker;
    ConvolutionImageObserver observer ;
    ConvolutionImageFilter filter ;
    public ImageRendererComponent(ImageIcon icon , ConvolutionImageFilter filter){
        this.icon = icon;
        this.filter  = filter;
        this.image = icon.getImage();
        FilteredImageSource fis  = new FilteredImageSource(image.getSource(), filter);
        this.image = createImage(fis);
        tracker = new MediaTracker(this);
        tracker.addImage(this.image,1);
        observer = new ConvolutionImageObserver(this);
    }

    public void setFilter(ConvolutionImageFilter filter) {
        this.filter = filter;
        FilteredImageSource fis  = new FilteredImageSource(this.icon.getImage().getSource(), filter);
        tracker.removeImage(this.image);
        this.image = createImage(fis);
        tracker.addImage(this.image,1);
        this.revalidate();
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x, y , w, h;
        x = (int)g.getClipBounds().getX();
        y = (int)g.getClipBounds().getY();
        w = this.getWidth() - (this.getInsets().left + this.getInsets().right);
        h = this.getHeight()  - (this.getInsets().top + this.getInsets().bottom);
        Graphics2D gg = (Graphics2D)g.create();
        //if ( tracker.statusID(1,false) == MediaTracker.COMPLETE)
        if ( image != null)
            gg.drawImage(image, x,y, w, h, observer);
        else{
            int mx, my ;
            mx = x + w/2;
            my = y + h/2;
            gg.setColor(Color.BLACK);
            gg.drawString("Image is null for now ", mx,my);
        }
        gg.dispose();
    }

}