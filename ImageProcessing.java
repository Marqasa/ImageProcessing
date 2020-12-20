import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Random;
import javax.imageio.ImageIO;

public class ImageProcessing {
  public static String filename = "apple.jpg";

  public static void main(String[] args) {

    int[][] imageArray = loadImage("./" + filename);

    int[][] trimmedImg = trimBorders(imageArray, 100);
    // int[][] negativeImg = negativeColor(imageArray);
    // int[][] stretchedImg = stretchHorizontally(imageArray);
    // int[][] shrunkenImg = shrinkVertically(imageArray);
    // int[][] invertedImg = invertImage(imageArray);
    // int[][] filteredImg = colorFilter(imageArray, 100, 10, 10);
    // int[][] randomImg = paintRandomImage(new int[500][500]);
    // int[][] paintedImg = paintRectangle(imageArray, 100, 250, 125, 100, 10, 50,
    // 255, 100);
    // int[][] randomRectsCanvas = generateRectangles(colorFilter(new int[500][500],
    // 255, 255, 255), 1000);
    // int[][] randomRectsImg = generateRectangles(imageArray, 10);

    saveImage(trimmedImg, "./trimmed_" + filename);
    // saveImage(negativeImg, "./negative_" + filename);
    // saveImage(stretchedImg, "./stretched_" + filename);
    // saveImage(shrunkenImg, "./shrunken_" + filename);
    // saveImage(invertedImg, "./inverted_" + filename);
    // saveImage(filteredImg, "./filtered_" + filename);
    // saveImage(randomImg, "./random_image.jpg");
    // saveImage(paintedImg, "./rectangle_" + filename);
    // saveImage(randomRectsCanvas, "./random_rects.jpg");
    // saveImage(randomRectsImg, "./random_rects_" + filename);
  }

  // Trim pixels from the top, bottom, left and right of the input image
  public static int[][] trimBorders(int[][] imageArray, int pixelCount) {

    // Get the height and width of the image
    int oldHeight = imageArray.length;
    int oldWidth = imageArray[0].length;

    // Calculate the trim amount for each axis
    int trimAmount = pixelCount * 2;

    // Check the input image is large enough to trim the requested pixels
    if (oldHeight < trimAmount || oldWidth < trimAmount) {
      System.out.println("Cannot trim that many pixels from the given image.");
      return imageArray;
    }

    // Calculate the height and width of the trimmed image
    int newHeight = oldHeight - trimAmount;
    int newWidth = oldWidth - trimAmount;

    // Create a new 2D int array to contain the trimmed image
    int[][] trimmedImg = new int[newHeight][newWidth];

    // Iterate through each pixel of the trimmed image in row-major order
    for (int i = 0; i < newHeight; i++) {
      for (int j = 0; j < newWidth; j++) {

        // Copy the trimmed pixel from the input image to the trimmed image
        trimmedImg[i][j] = imageArray[i + pixelCount][j + pixelCount];
      }
    }

    // Return the trimmed image
    return trimmedImg;
  }

  // Negate the color of each pixel in the input image
  public static int[][] negativeColor(int[][] imageArray) {

    // Get the height and width of the image
    int imageHeight = imageArray.length;
    int imageWidth = imageArray[0].length;

    // Create a new image array the same size as the input image
    int[][] negativeImg = new int[imageHeight][imageWidth];

    // Iterate through the input image in row-major order
    for (int i = 0; i < imageHeight; i++) {
      for (int j = 0; j < imageWidth; j++) {

        // Get the RGBA values from the pixel
        int[] rgba = getRGBAFromColor(imageArray[i][j]);

        // Negate the RGB values
        for (int k = 0; k < 3; k++) {
          rgba[k] = 255 - rgba[k];
        }

        // Get the hex value from RGBA array and store it in the new image
        negativeImg[i][j] = getColorFromRGBA(rgba);
      }
    }

    // Return the negative image
    return negativeImg;
  }

  // Double the width of the input image
  public static int[][] stretchHorizontally(int[][] imageArray) {

    // Get the height and width of the image
    int imageHeight = imageArray.length;
    int imageWidth = imageArray[0].length;

    // Create a new image array twice the width of the input image
    int[][] stretchedImg = new int[imageHeight][imageWidth * 2];

    // Iterate through the input image in row-major order
    for (int i = 0; i < imageHeight; i++) {
      for (int j = 0; j < imageWidth; j++) {

        // Calculate the correct column in the stretched image
        int k = j * 2;

        // Copy pixel data to the stretched image at columns k and k + 1
        stretchedImg[i][k] = imageArray[i][j];
        stretchedImg[i][k + 1] = imageArray[i][j];
      }
    }

    // Return the stretched image
    return stretchedImg;
  }

  // Halve the height of the input image
  public static int[][] shrinkVertically(int[][] imageArray) {

    // Get the height and width of the image
    int imageHeight = imageArray.length;
    int imageWidth = imageArray[0].length;

    // If the input image has an odd number of rows, ignore the last row
    if (imageHeight % 2 != 0) {
      imageHeight--;
    }

    // Create a new image array half the height of the input image
    int[][] shrunkenImg = new int[imageHeight / 2][imageWidth];

    // Iterate through the even rows of the input image in row-major order
    for (int i = 0; i < imageHeight; i += 2) {
      for (int j = 0; j < imageWidth; j++) {

        // Copy pixel data to the shrunken image at row i / 2
        shrunkenImg[i / 2][j] = imageArray[i][j];
      }
    }

    // Return the shrunken image
    return shrunkenImg;
  }

  // Flip the input image vertically and horizontally
  public static int[][] invertImage(int[][] imageArray) {

    // Get the height and width of the image
    int imageHeight = imageArray.length;
    int imageWidth = imageArray[0].length;

    // Create a new image array the same size as the input image
    int[][] invertedImg = new int[imageHeight][imageWidth];

    // Iterate through the input image in row-major order
    for (int i = 0; i < imageHeight; i++) {
      for (int j = 0; j < imageWidth; j++) {

        // Copy pixel data to the inverted image at the opposite row and column
        // positions
        invertedImg[imageHeight - 1 - i][imageWidth - 1 - j] = imageArray[i][j];
      }
    }

    // Return the inverted image
    return invertedImg;
  }

  // Filter the input image with the provided RGBA values
  public static int[][] colorFilter(int[][] imageArray, int red, int green, int blue) {

    // Get the height and width of the image
    int imageHeight = imageArray.length;
    int imageWidth = imageArray[0].length;

    // Create a new image array the same size as the input image
    int[][] filteredImg = new int[imageHeight][imageWidth];

    // Iterate through the input image in row-major order
    for (int i = 0; i < imageHeight; i++) {
      for (int j = 0; j < imageWidth; j++) {

        // Get the RGBA values from the pixel
        int[] rgba = getRGBAFromColor(imageArray[i][j]);

        // Adjust the RGB values and constrain the result between 0 and 255
        rgba[0] = Math.min(Math.max(0, rgba[0] + red), 255);
        rgba[1] = Math.min(Math.max(0, rgba[1] + green), 255);
        rgba[2] = Math.min(Math.max(0, rgba[2] + blue), 255);

        // Get the hex value from the RGBA array and store it in the filtered image
        filteredImg[i][j] = getColorFromRGBA(rgba);
      }
    }

    // Return the filtered image
    return filteredImg;
  }

  // Paint the input canvas with a random color for each pixel
  public static int[][] paintRandomImage(int[][] imageArray) {

    // Get the height and width of the image
    int imageHeight = imageArray.length;
    int imageWidth = imageArray[0].length;

    // Create a new random object
    Random rand = new Random();

    // Iterate through the input image in row-major order
    for (int i = 0; i < imageHeight; i++) {
      for (int j = 0; j < imageWidth; j++) {

        // Generate random rgb values
        int red = rand.nextInt(256);
        int green = rand.nextInt(256);
        int blue = rand.nextInt(256);

        // Create an RGBA array
        int[] rgba = { red, green, blue, 255 };

        // Get the hex value from the RGBA array and store it in the imageArray
        imageArray[i][j] = getColorFromRGBA(rgba);
      }
    }

    // Return the modified imageArray
    return imageArray;
  }

  // Draw a rectangle on the input image
  public static int[][] paintRectangle(int[][] imageArray, int width, int height, int rowPos, int colPos, int red,
      int green, int blue, int alpha) {

    // Get the height and width of the image
    int imageHeight = imageArray.length;
    int imageWidth = imageArray[0].length;

    // Create a new image array the same size as the input image
    int[][] paintedImg = new int[imageHeight][imageWidth];

    // Iterate through the input image in row-major order
    for (int i = 0; i < imageHeight; i++) {
      for (int j = 0; j < imageWidth; j++) {

        // Check if pixel is withing input rectangle bounds
        boolean paintRow = i >= rowPos && i <= rowPos + width;
        boolean paintCol = j >= colPos && j <= colPos + height;

        if (paintRow && paintCol) {

          // Get the RGBA values from the pixel
          int[] pixelRgba = getRGBAFromColor(imageArray[i][j]);

          // Get weight from alpha value
          double weight = Math.min(Math.max(0, alpha), 255) / 255.0;

          // Create new RGB values
          double redBlend = (red * weight) + (pixelRgba[0] * (1 - weight));
          double greenBlend = (green * weight) + (pixelRgba[1] * (1 - weight));
          double blueBlend = (blue * weight) + (pixelRgba[2] * (1 - weight));

          // Adjust RGB values and constrain result between 0 and 255
          pixelRgba[0] = Math.min(Math.max(0, (int) redBlend), 255);
          pixelRgba[1] = Math.min(Math.max(0, (int) greenBlend), 255);
          pixelRgba[2] = Math.min(Math.max(0, (int) blueBlend), 255);

          // Get hex value from RGBA array and store in new image
          paintedImg[i][j] = getColorFromRGBA(pixelRgba);
        } else {
          // Copy the original pixel data
          paintedImg[i][j] = imageArray[i][j];
        }
      }
    }

    // Return painted image
    return paintedImg;
  }

  // Generate randomly positioned rectangles
  public static int[][] generateRectangles(int[][] imageArray, int numRectangles) {

    // Get the height and width of the image
    int canvasHeight = imageArray.length;
    int canvasWidth = imageArray[0].length;

    // Create a new random object
    Random rand = new Random();

    // Generate random rectangles
    for (int i = 0; i < numRectangles; i++) {

      // Generate rectangle that can be up to 50% outside the canvas bounds
      int randWidth = rand.nextInt(canvasWidth);
      int randHeight = rand.nextInt(canvasHeight);
      int randRowPos = rand.nextInt(canvasHeight + (randHeight / 2)) - (randHeight / 2);
      int randColPos = rand.nextInt(canvasWidth + (randWidth / 2)) - (randWidth / 2);

      // Generate a random color
      int randRed = rand.nextInt(255);
      int randGreen = rand.nextInt(255);
      int randBlue = rand.nextInt(255);
      int randAlpha = rand.nextInt(255);

      // Paint the rectangle on the canvas
      imageArray = paintRectangle(imageArray, randWidth, randHeight, randRowPos, randColPos, randRed, randGreen,
          randBlue, randAlpha);
    }

    // Return the canvas
    return imageArray;
  }

  // Load an image from the given input string
  public static int[][] loadImage(String inputFileOrLink) {
    try {

      // Create a new image
      BufferedImage image = null;

      // Check if the input string is a URL
      if (inputFileOrLink.substring(0, 4).toLowerCase().equals("http")) {

        // Create a new URL from the input string
        URL imageUrl = new URL(inputFileOrLink);

        // Read the image from the URL
        image = ImageIO.read(imageUrl);

        // Check the image read was successful
        if (image == null) {
          System.out.println("Failed to get an image from provided URL.");
          return null;
        }
      } else {

        // Read the image from the file path
        image = ImageIO.read(new File(inputFileOrLink));
      }

      // Get the height and width of the image
      int imageHeight = image.getHeight();
      int imageWidth = image.getWidth();

      // Create a new image array from the height and width
      int[][] imageArray = new int[imageHeight][imageWidth];

      // Iterate through the image array in row-major order
      for (int i = 0; i < imageHeight; i++) {
        for (int j = 0; j < imageWidth; j++) {

          // Get the RGB values from the image and store them in the image array
          imageArray[i][j] = image.getRGB(j, i);
        }
      }

      // Return the image array
      return imageArray;
    } catch (Exception e) {

      // Catch errors when the image fails to load
      System.out.println("Failed to load image: " + e.getLocalizedMessage());
      return null;
    }
  }

  // Save the image to a new file
  public static void saveImage(int[][] imageArray, String fileName) {
    try {

      // Get the height and width of the image
      int imageHeight = imageArray.length;
      int imageWidth = imageArray[0].length;

      // Create a new image
      BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

      // Iterate through the image array in row-major order
      for (int i = 0; i < imageHeight; i++) {
        for (int j = 0; j < imageWidth; j++) {

          // Set the image RGB values from the image array
          image.setRGB(j, i, imageArray[i][j]);
        }
      }

      // Create a new file
      File file = new File(fileName);

      // Write the image to the file in jpg format
      ImageIO.write(image, "jpg", file);

    } catch (Exception e) {

      // Catch any errors
      System.out.println("Failed to save image: " + e.getLocalizedMessage());
    }
  }

  // Get RGBA values from color
  public static int[] getRGBAFromColor(int rgb) {
    Color color = new Color(rgb);
    return new int[] { color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() };
  }

  // Get color from RGBA
  public static int getColorFromRGBA(int[] rgba) {
    Color color;
    if (rgba.length == 4) {
      color = new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
    } else {
      System.out.println("Incorrect number of elements in RGBA array.");
      color = new Color(0, 0, 0, 255);
    }
    return color.getRGB();
  }
}