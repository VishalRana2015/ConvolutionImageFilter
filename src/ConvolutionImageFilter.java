import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageFilter;
import java.util.Hashtable;

public class ConvolutionImageFilter extends ImageFilter {
    int[] pixels;
    int savedWidth , savedHeight;
    double[][] cm;
    int size;
    ColorModel model ;
    public static final double[][] IDENTITY  = { {0,0,0}, { 0, 1 , 0}, { 0,0,0}};
    public static final double[][] EDGEDETECTION1 = { {1,0,-1},{0,0,0},{-1,0,1}};
    public static final double[][] EDGEDETECTION2 = { {0,-1,0},{-1,4,-1},{0,-1,0}};
    public static final double[][] EDGEDETECTION3= { {-1,-1,-1} , { -1,8,-1}, {-1,-1,-1}};
    public static final double[][] SHARPEN = { {0,-1,0},{-1,5,-1},{0,-1,0}};
    public static final double[][] BOXBLUR = { {1.0/9 , 1.0/9, 1.0/9}, { 1.0/9, 1.0/9 , 1.0/9}, { 1.0/9,1.0/9,1.0/9}};
    public static final double[][] GAUSSIANBLUR3 = {{1.0/16, 2.0/16, 1.0/16}, { 2.0/16, 4.0/16 , 2.0/16}, { 1.0/16, 2.0/16 , 1.0/16}};
    public static final double[][] GAUSSIANBLUR5 = { {1.0/256,4.0/256,6.0/256,4.0/256,1.0/256}, { 4.0/256 , 16.0/256,24.0/256,16.0/256,4.0/256},{6.0/256,24.0/256,36.0/256,24.0/256,6.0/256},{4.0/256,16.0/256,24.0/256,16.0/256,4.0/256},{1.0/256,4.0/256,6.0/256,4.0/256,1.0/256}};
    public static final double[][] UNSHARPMASKING = { {1.0/-256,4.0/-256,6.0/-256,4.0/-256,1.0/-256},{ 4.0/-256 , 16.0/-256,24.0/-256,16.0/-256,4.0/-256},{6.0/-256,24.0/-256,36.0/-256,24.0/-256,6.0/-256},{4.0/-256,16.0/-256,-476.0/-256,16.0/-256,4.0/-256},{1.0/-256,4.0/-256,6.0/-256,4.0/-256,1.0/-256}};
    public static final double[][] LEFTSOBEL = { {1,0,-1} , {2,0,-2}, {1,0,-1}};
    public static final double[][] RIGHTSOBEL = { {-1,0,1},{-2,0,2},{-1,0,1}};
    public static final double[][] BOTTOMSOBEl = { {-1,-2,-1},{0,0,0},{1,2,1}};
    public static final double[][] TOPSOBEL = { {1,2,1}, { 0,0,0}, { -1,-2,-1}};
    public static final double[][] EMBOSS = { {-2,-1,0}, { -1,1,1}, {0,1,2}};
    public ConvolutionImageFilter(double[][] mat){
        size = mat.length;
        if ( ( size%2)== 0)
            cm = new double[size+1][size+1];
        else
            cm= new double[size][size];
        for ( int i =0; i <size; i++)
            for (int j =0; j < size ;j++)
                cm[i][j] = mat[i][j];

        this.size = cm.length;
    }
    @Override
    public void setDimensions(int width, int height) {
        //super.setDimensions(width, height);
        this.savedHeight = height;
        this.savedWidth = width;
        pixels  = new int[savedWidth* savedHeight];
        if ( pixels == null)
            System.out.println("Unable to allocate memory to pixels");
        System.out.println("setDimensions called (width,height) :"+ width + ", "+ height);
        consumer.setDimensions(width,height);
    }

    @Override
    public void setHints(int hints) {
        //super.setHints(hints);
        String s = "";
        if (( hints & ImageConsumer.SINGLEFRAME) != 0)
            s= s+ " SingleFrame";
        if ( (hints& ImageConsumer.TOPDOWNLEFTRIGHT ) != 0)
            s =s + " TopDownLeftRight";
        if ( (hints & ImageConsumer.SINGLEPASS) != 0)
            s = s+ " SinglePass";
        if ( (hints & ImageConsumer.COMPLETESCANLINES) != 0)
            s = s + "CompleteScanLines";
        if ( (hints & ImageConsumer.RANDOMPIXELORDER) != 0)
            s = s+ "RandomPixelOrder";
        System.out.println("hints by ImageProducer :"+ s);
        consumer.setHints(ImageConsumer.COMPLETESCANLINES | ImageConsumer.SINGLEPASS | ImageConsumer.SINGLEFRAME | ImageConsumer.TOPDOWNLEFTRIGHT);
        //consumer.setHints(hints);
    }

    @Override
    public void setColorModel(ColorModel model) {
        //super.setColorModel(model);
        this.model = model;
        System.out.println("colorModel called :" + model);
        consumer.setColorModel(model);
    }

    @Override
    public void setProperties(Hashtable<?, ?> props) {
        //super.setProperties(props);
        System.out.println("setProperties called " + props);
        consumer.setProperties(props);
    }

    @Override
    public void setPixels(int x, int y, int w, int h, ColorModel model, byte[] pixels, int off, int scansize) {
        super.setPixels(x, y, w, h, model, pixels, off, scansize);
        //System.out.println("setPixels called byte version ");
        //consumer.setPixels(x,y,w,h,model,pixels,off,scansize);
    }

    @Override
    public void setPixels(int x, int y, int w, int h, ColorModel model, int[] pixels, int off, int scansize) {
        //super.setPixels(x, y, w, h, model, pixels, off, scansize);
        //System.out.println("setPixels called : x,y :"+ x+", " + y);
        int row, column;
        for ( int r = 0; r < h; r++){
            row = r + y;
            for (int c =0; c < w; c++){
                column = c + x;
                this.pixels[row*savedWidth + column] = pixels[r*scansize + c];
            }
        }
        //consumer.setPixels(x,y,w,h,model,pixels,off,scansize);
    }

    @Override
    public void imageComplete(int status) {
        //super.imageComplete(status);
        System.out.println("ImageComplete called");
        int[] temp = new int[savedWidth];
        double[][] rm = new double[size][size];
        double[][] gm = new double[size][size];
        double[][] bm = new double[size][size];
        int k = size/2;
        int tempr, tempc;
        int red, green , blue;
        Color color;
        int colorValue;
        for ( int r =0; r < savedHeight; r++){
            for ( int c = 0; c < savedWidth ; c++){
                temp[c] = pixels[r*savedWidth + c];
                colorValue = pixels[r*savedWidth + c];
                color = new Color(colorValue);
                for ( int br = 0; br < size; br++){
                    for ( int bc =0; bc < size; bc++){
                        tempr = r + br -k;
                        tempc = c + bc -k;
                        if ( ( tempr >=0  && tempr < savedHeight) && ( tempc >=0 && tempc < savedWidth)){
                            color = new Color( pixels[tempr* savedWidth  + tempc]);
                            rm[br][bc] = color.getRed();
                            gm[br][bc]= color.getGreen();
                            bm[br][bc] = color.getBlue();
                        }
                        else{
                            rm[br][bc] = 0;
                            gm[br][bc]  =0;
                            bm[br][bc]= 0;
                        }
                    }
                }
                red = Math.max(convolute(cm,rm) % 256,0);
                green = Math.max(convolute(cm,gm) % 256,0);
                blue = Math.max(convolute(cm,bm) % 256,0);
                color =new Color(red,green,blue);
                temp[c] =color.getRGB();
                //System.out.println("pixels["+ r+ "][" + c + "] : " + (new Color(this.pixels[r*savedWidth +c])) + " color :" + color);
            }
            consumer.setPixels(0,r,savedWidth,1,this.model,temp,0,savedWidth);
        }
        consumer.imageComplete(status);
    }
    private int convolute(double[][] convolutionMatrix , double[][] B){
        double ac  =0;
        for (int i =0; i < size; i++){
            for (int j =0; j < size; j++){
                ac += convolutionMatrix[i][j]*B[i][j];
            }
        }
        return (int)ac;
    }

}