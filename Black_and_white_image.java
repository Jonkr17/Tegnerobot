/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package black_and_white_image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import javax.imageio.ImageIO;

public class Black_and_white_image {

    public static void main(String[] args) {
        File s = null;
        BufferedImage binary = null;
        try {
            BufferedImage img = ImageIO.read(new File("C:\\Users\\frede\\Videos\\SDU Robot Diplom\\Semester projekt 1\\Rainbow.PNG"));
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
            s = new File("C:\\Users\\frede\\Videos\\SDU Robot Diplom\\Semester projekt 1\\new picture.jpg");
            ImageIO.write(binary, "jpg", s);
            System.out.println("File writing completed ");
        } catch (IOException e) {
            System.out.println("Error: " + e);

        }
        BufferedImage binary2 = null;
        try {
            BufferedImage image = ImageIO.read(new File("C:\\Users\\frede\\Videos\\SDU Robot Diplom\\Semester projekt 1\\new picture.jpg"));
//            binary2 = new BufferedImage(image.getWidth(), image.getHeight(), image.getRGB(0, 0));
        }catch (IOException e) {
            System.out.println(" " + e.getMessage());
        }
        File file = new File("C:\\Users\\frede\\Videos\\SDU Robot Diplom\\Semester projekt 1\\new picture.jpg");
        int [][]compute = compute(file);
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
    public static int[][] compute(File file)
{
try 
{
    BufferedImage image= ImageIO.read(file);
    Raster raster=image.getData();
    int w=raster.getWidth(),h=raster.getHeight();
    int pixels[][]=new int[w][h];
    for (int x=0;x<w;x++)
    {
        for(int y=0;y<h;y++)
        {
            pixels[x][y]=raster.getSample(x,y,0);
        }
    }
            String[] position = {"X","Y"};
   try (
                PrintStream output = new PrintStream(new File("C:\\Users\\frede\\Videos\\SDU Robot Diplom\\output.txt"));
            ){

            for(int i =0;i<position.length;i++){
                String sc ="";
                for (int j=0;j<pixels[i].length;j++){
                        sc+=pixels[i][j]+" ";
                }
                output.println("Placering  " + position[i] + " værdi af pixels " + sc);
            }
            output.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
    return pixels;

}
catch (Exception e)
{
    e.printStackTrace();
}
return null;
}
    }

    
