import javax.swing.*;
import java.awt.*;
import java.awt.image.FilteredImageSource;

public class ImageRendererComponent extends JPanel {
    Image image;
    ImageIcon icon;
    MediaTracker tracker;
    ConvolutionImageObserver observer ;
    ConvolutionImageFilter filter ;
    public ImageRendererComponent(ImageIcon icon , ConvolutionImageFilter convolutionImageFilter){
        this.icon = icon;
        this.tracker = new MediaTracker(this);
        this.setFilter(convolutionImageFilter);
        observer = new ConvolutionImageObserver(this);
    }

    public void setFilter(ConvolutionImageFilter filter) {
        this.filter = filter;
        FilteredImageSource fis  = new FilteredImageSource(this.icon.getImage().getSource(), filter);
        tracker.removeImage(this.image);
        this.image = createImage(fis);
        tracker.addImage(this.image,1);
        setSizeToImage();
        this.revalidate();
        this.repaint();
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
        this.setFilter(this.filter);
        setSizeToImage();
        this.revalidate();
        this.repaint();
    }

    public void setSizeToImage(){
        Dimension dimension = new Dimension(this.icon.getIconWidth(), this.icon.getIconHeight());
        System.out.println("Dimension : "+ dimension);
        this.setMinimumSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
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