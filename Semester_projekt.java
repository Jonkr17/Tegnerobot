package semester_projekt;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import javax.imageio.ImageIO;

public class Semester_projekt {

    public static void main(String[] args) throws IOException {
       
    
        // Her indlæses billede og gøres black/white
        BufferedImage binary = null;
        try {
            BufferedImage img = ImageIO.read(new File("C:\\Users\\Bruger\\Desktop\\SDU\\Semester projekt\\Rainbow.png"));

            binary = new BufferedImage(img.getWidth(), img.getHeight(),
                    BufferedImage.TYPE_BYTE_BINARY);

            Graphics2D g = binary.createGraphics();
            g.drawImage(img, 0, 0, null);

            HashSet<Integer> colors = new HashSet<>();
            int color = 0;
            for (int y = 0; y < binary.getHeight(); y++) {
                for (int x = 0; x < binary.getWidth(); x++) {
                    color = binary.getRGB(x, y);

                    if (color == -1) {
                        binary.setRGB(x, y, -16777216);
                    } else {
                        binary.setRGB(x, y, -1);
                    }

                    System.out.println(color);
                    colors.add(color);

                }
            }

            System.out.println(colors.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File black_and_white_output = new File("C:\\Users\\Bruger\\Desktop\\SDU\\Semester projekt\\black_and_white.jpg");
            ImageIO.write(binary, "jpg", black_and_white_output);
            System.out.println("File writing completed ");
        } catch (IOException e) {
            System.out.println("Error: " + e);
    }
        
        // Here we scale the picture down
        File scale_input = new File("C:\\Users\\Bruger\\Desktop\\SDU\\Semester projekt\\black_and_white.jpg");
        BufferedImage image = ImageIO.read(scale_input);
        BufferedImage resized = resize(image, 12, 24);
        File scale_output = new File("C:\\Users\\Bruger\\Desktop\\SDU\\Semester projekt\\scale_output.jpg");
        ImageIO.write(resized, "jpg", scale_output);
        //Picture p1 = new Picture(scale_output); 
        //Here we find the value of the pixels in the picture and print them to a text file
        File outputtest = new File("C:\\Users\\Bruger\\Desktop\\SDU\\Semester projekt\\scale_output.jpg");
    int[][] compute = compute(outputtest);

}
     private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
    public static int[][] compute(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            Raster raster = image.getData();
            int w = raster.getWidth(), h = raster.getHeight();
        
            int pixels[][] = new int[w][h];
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    
                    pixels[x][y] = raster.getSample(x, y, 0);
                }
            }
            String[] position = {"X", "Y"};
            String stringx;
            String stringy;
            try (
                    PrintStream output = new PrintStream(new File("C:\\Users\\Bruger\\Desktop\\SDU\\Semester projekt\\output_text.txt"));) {
                 for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                   // output.print(pixels[x][y] + ", ");
                   if (pixels[x][y] < 125){
                       if (x < 10){
                          stringx = "x 0" + x;
                       }
                       else{
                          stringx = "x " + x;
                       }
                       if (y < 10){
                           stringy = " y 0" + y;
                       }
                       else{
                           stringy = " y " + y;
                       }
                       output.println(stringx + stringy);
                   }
                }
                output.println("");
                 }
                 
                    
               /* for (int i = 0; i < position.length; i++) {
                    String sc = "";
                    for (int j = 0; j < pixels[i].length; j++) {
                        sc += pixels[i][j] + " ";
                    }
                    output.println("Placering  " + position[i] + " værdi af pixels " + sc);
                }
                */output.close();

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
            return pixels;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
     public static int[][] convertToArray(BufferedImage image) {

        if (image == null || image.getWidth() == 0 || image.getHeight() == 0) {
            return null;
        }
        // This returns bytes of data starting from the top left of the bitmap
        // image and goes down.
        // Top to bottom. Left to right.
        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        final int width = image.getWidth();
        final int height = image.getHeight();

        int[][] result = new int[height][width];

        boolean done = false;
        boolean alreadyWentToNextByte = false;
        int byteIndex = 0;
        int row = 0;
        int col = 0;
        int numBits = 0;
        byte currentByte = pixels[byteIndex];
        while (!done) {
            alreadyWentToNextByte = false;

            result[row][col] = (currentByte & 0x80) >> 7;
            currentByte = (byte) (((int) currentByte) << 1);
            numBits++;

            if ((row == height - 1) && (col == width - 1)) {
                done = true;
            } else {
                col++;

                if (numBits == 8) {
                    currentByte = pixels[++byteIndex];
                    numBits = 0;
                    alreadyWentToNextByte = true;
                }

                if (col == width) {
                    row++;
                    col = 0;

                    if (!alreadyWentToNextByte) {
                        currentByte = pixels[++byteIndex];
                        numBits = 0;
                    }
                }
            }
        }

        return result;
    }
      
}
