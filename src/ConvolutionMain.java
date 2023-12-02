import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
public class ConvolutionMain {
    public static void main(String[] args) {
        JFrame frame = new JFrame("ImageFilter Test");
        frame.setSize(1050,650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(50,50);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        ImageIcon icon =new ImageIcon("images/jiraya.png");
        ConvolutionImageFilter filter = new ConvolutionImageFilter(ConvolutionImageFilter.EDGEDETECTION3);
        if ( icon == null)
            System.out.println("icon is null");
        ImageRendererComponent comp = new ImageRendererComponent( icon.getImage(), filter);
        comp.setSize(1000,600);
        comp.setLocation(0,0);
        comp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(comp);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
