# Convolution Image Fiter Tool
Welcome to the Convolution Image Filter Tool, a **Java Swing** application designed to apply filters on the provided image using Convolution matrices. 

## Key Features
- Select from a range of pre-built convolution matrices including Edge Detection, Emboss, and Blur, each giving your images a unique touch.
- You can load any image from your device and watch the magic happen! Apply your chosen filter to see stunning transformations.
- Before applying the selected convolution matrix as a filter to the image, you can view as well as change its values
- Craft your own convolution matrix to achieve custom effects and experiment with unique image transformations.

### Following convolution Matrices are available
- Edge Detection
- Identity
- Box Blur
- Guassian Blur radius 3
- Guassian Blur radius 5
- Emboss: Left, Right, Top and Bottom
- Emboss General

### How to add your own Convolution Matrix
You can find data about all convolution matrices in **resources/convolutionMatrixData.txt" file. If you want to add a new convolution matrix, add the matrix details in the following format at the end of the file.

```
Matrix Name 
r c
r rows each containinig c values
```

Example
```
Box Blur
3 3
0.111111 0.111111 0.111111
0.111111 0.111111 0.111111
0.111111 0.111111 0.111111
```

Once you have added the value, increment the count of the total number of convolution matrices in the beginning of the file by 1.  

### Demonstration 
Original Image: 
![TajMahal](https://github.com/VishalRana2015/ConvolutionImageFilter/assets/69715143/b496c149-61d0-4032-a544-2c6ac41b3262)

Image after applying 
- **Sharpen Convolution Matrix**  
![SharpenTajMahal](https://github.com/VishalRana2015/ConvolutionImageFilter/assets/69715143/8d3cb967-25ec-44a5-8233-7ab8ef27a024)
- **Emboss**  
![EmbossTajMahal](https://github.com/VishalRana2015/ConvolutionImageFilter/assets/69715143/08445bd0-3347-44a9-9714-c0f07451cf14)
- **Edge Detection**  
![EdgeTajMahal jpg](https://github.com/VishalRana2015/ConvolutionImageFilter/assets/69715143/6f8d1933-7399-4e37-b4ee-4776ff69b87d)
- **Guassian Blur**  
![GuassianBlurTajMahal](https://github.com/VishalRana2015/ConvolutionImageFilter/assets/69715143/d6916cf8-af2b-474e-82a0-8b22f6ffb056)
- **Top Sobel**  
![TopSobelTajMahal](https://github.com/VishalRana2015/ConvolutionImageFilter/assets/69715143/1d1adc83-9dbe-4297-9b74-1eae4ab6bd55)


## Contribution and Support
Have suggestions or found a bug? Feel free to share in the Issues section. Contributions via pull requests are highly appreciated!
