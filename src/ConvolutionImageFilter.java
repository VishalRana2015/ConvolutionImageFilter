import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageFilter;
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public class ConvolutionImageFilter extends ImageFilter {
    int[] pixels;
    int savedWidth, savedHeight;
    double[][] cm;
    int size;
    ColorModel model;
    public static String IDENTITY_MATRIX_NAME = "Identity";
    private static String filePath = "resources/convolutionMatrixData.txt";
    private static String exitMessage = "Create this file using the given below format: \n" +
            "N: First line contains a number representing convolution matrices in the file.\n" +
            "Starting from the next line, matrices will be described one by one in the given below format\n" +
            "Convolution Matrix Name: That describes what it does when applied to an image \n" +
            "rows cols : Describing number of rows and columns in the convolution matrix\n" +
            "'rows' lines each containing 'cols' number of numbers";
    private static HashMap<String, Double[][]> convolutionMatricesMap;

    static {
        convolutionMatricesMap = new LinkedHashMap<>();
        LinkedHashMap<String, Double[][]> linkedHashMap = new LinkedHashMap<>();
        System.out.println("Static block invoked");
        File file = new File(filePath);
        if (!file.exists()) {
            // exit the program
            System.out.println("File : " + filePath + " not found.");
            System.out.println(exitMessage);
            System.exit(-1);
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            int matricesLength = Integer.parseInt(getNextToken(br));
            for (int matrixIndex = 0; matrixIndex < matricesLength; matrixIndex++) {
                String matrixName = br.readLine();
                stringTokenizer = null;
                int rows = Integer.parseInt(getNextToken(br)), cols = Integer.parseInt(getNextToken(br));
                if ( rows == 0 || cols == 0){
                    System.out.println("rows and cols of any matrix can't be null");
                    System.exit(-1);
                }
                Double[][] matrix = new Double[rows][cols];
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        matrix[i][j] = Double.parseDouble(getNextToken(br));
                    }
                }
                linkedHashMap.put(matrixName, matrix);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if ( !linkedHashMap.containsKey(IDENTITY_MATRIX_NAME)){
            Double[][] matrix = new Double[][]{ {0.0, 0.0, 0.0}, { 0.0, 1.0, 0.0}, {0.0, 0.0, 0.0}};
            convolutionMatricesMap.put(IDENTITY_MATRIX_NAME, matrix);
        }
        convolutionMatricesMap.putAll(linkedHashMap);
    }

    public static HashMap<String, Double[][]> getConvolutionMatricesMap() {
        return convolutionMatricesMap;
    }

    private static StringTokenizer stringTokenizer = null;

    private static String getNextToken(BufferedReader br) throws IOException {
        if (stringTokenizer == null || !stringTokenizer.hasMoreTokens()) {
            stringTokenizer = new StringTokenizer(br.readLine());
        }
        return stringTokenizer.nextToken();
    }

    public static double[][] sobelMatrix = {
            {-1, -2, -3, -4, -5, 5, 4, 3, 2, 1},
            {-1, -2, -3, -4, -5, 5, 4, 3, 2, 1},
            {-1, -2, -3, -4, -5, 5, 4, 3, 2, 1},
            {-1, -2, -3, -4, -5, 5, 4, 3, 2, 1},
            {-1, -2, -3, -4, -5, 5, 4, 3, 2, 1},
            {-1, -2, -3, -4, -5, 5, 4, 3, 2, 1},
            {-1, -2, -3, -4, -5, 5, 4, 3, 2, 1},
            {-1, -2, -3, -4, -5, 5, 4, 3, 2, 1},
            {-1, -2, -3, -4, -5, 5, 4, 3, 2, 1},
            {1, 2, 3, 4, 5, -5, -4, -3, -2, -1}
    };

    public ConvolutionImageFilter(double[][] mat) {
        size = mat.length;
        if ((size % 2) == 0)
            cm = new double[size + 1][size + 1];
        else
            cm = new double[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                cm[i][j] = mat[i][j];

        this.size = cm.length;
    }

    @Override
    public void setDimensions(int width, int height) {
        //super.setDimensions(width, height);
        this.savedHeight = height;
        this.savedWidth = width;
        pixels = new int[savedWidth * savedHeight];
        if (pixels == null)
            System.out.println("Unable to allocate memory to pixels");
        consumer.setDimensions(width, height);
    }

    @Override
    public void setHints(int hints) {
        String s = "";
        if ((hints & ImageConsumer.SINGLEFRAME) != 0)
            s = s + " SingleFrame";
        if ((hints & ImageConsumer.TOPDOWNLEFTRIGHT) != 0)
            s = s + " TopDownLeftRight";
        if ((hints & ImageConsumer.SINGLEPASS) != 0)
            s = s + " SinglePass";
        if ((hints & ImageConsumer.COMPLETESCANLINES) != 0)
            s = s + "CompleteScanLines";
        if ((hints & ImageConsumer.RANDOMPIXELORDER) != 0)
            s = s + "RandomPixelOrder";
        System.out.println("hints by ImageProducer :" + s);
        consumer.setHints(ImageConsumer.COMPLETESCANLINES | ImageConsumer.SINGLEPASS | ImageConsumer.SINGLEFRAME | ImageConsumer.TOPDOWNLEFTRIGHT);
    }

    @Override
    public void setColorModel(ColorModel model) {
        super.setColorModel(model);
        this.model = model;
    }

    @Override
    public void setProperties(Hashtable<?, ?> props) {
        super.setProperties(props);
    }

    @Override
    public void setPixels(int x, int y, int w, int h, ColorModel model, byte[] pixels, int off, int scansize) {
        super.setPixels(x, y, w, h, model, pixels, off, scansize);
    }

    @Override
    public void setPixels(int x, int y, int w, int h, ColorModel model, int[] pixels, int off, int scansize) {
        super.setPixels(x, y, w, h, model, pixels, off, scansize);
        int row, column;
        for (int r = 0; r < h; r++) {
            row = r + y;
            for (int c = 0; c < w; c++) {
                column = c + x;
                this.pixels[row * savedWidth + column] = pixels[r * scansize + c];
            }
        }
        consumer.setPixels(x, y, w, h, model, pixels, off, scansize);
    }

    @Override
    public void imageComplete(int status) {
        System.out.println("ImageComplete called");
        int[] temp = new int[savedWidth];
        double[][] rm = new double[size][size];
        double[][] gm = new double[size][size];
        double[][] bm = new double[size][size];
        int k = size / 2;
        int tempr, tempc;
        int red, green, blue;
        Color color;
        int colorValue;
        for (int r = 0; r < savedHeight; r++) {
            for (int c = 0; c < savedWidth; c++) {
                temp[c] = pixels[r * savedWidth + c];
                colorValue = pixels[r * savedWidth + c];
                color = new Color(colorValue);
                for (int br = 0; br < size; br++) {
                    for (int bc = 0; bc < size; bc++) {
                        tempr = r + br - k;
                        tempc = c + bc - k;
                        if ((tempr >= 0 && tempr < savedHeight) && (tempc >= 0 && tempc < savedWidth)) {
                            color = new Color(pixels[tempr * savedWidth + tempc]);
                            rm[br][bc] = color.getRed();
                            gm[br][bc] = color.getGreen();
                            bm[br][bc] = color.getBlue();
                        } else {
                            rm[br][bc] = 0;
                            gm[br][bc] = 0;
                            bm[br][bc] = 0;
                        }
                    }
                }
                red = Math.max(convolute(cm, rm) % 256, 0);
                green = Math.max(convolute(cm, gm) % 256, 0);
                blue = Math.max(convolute(cm, bm) % 256, 0);
                color = new Color(red, green, blue);
                temp[c] = color.getRGB();
                //System.out.println("pixels["+ r+ "][" + c + "] : " + (new Color(this.pixels[r*savedWidth +c])) + " color :" + color);
            }
            consumer.setPixels(0, r, savedWidth, 1, this.model, temp, 0, savedWidth);
        }
        consumer.imageComplete(status);
    }

    private int convolute(double[][] convolutionMatrix, double[][] B) {
        double ac = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ac += convolutionMatrix[i][j] * B[i][j];
            }
        }
        return (int) ac;
    }

}