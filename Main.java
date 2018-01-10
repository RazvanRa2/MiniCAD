// Copyright (C), Razvan Radoi, first of his name

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.StringTokenizer;

public final class Main {

    private Main() {

    }

    public static void main(final String[] args) throws Exception {
        // reading input file
        System.out.println("1");
        String inputFileName = args[0];
        Scanner fileScanner = new Scanner(new File(inputFileName));
        // reading number of geometric shapes
        int shapesNo = Integer.parseInt(fileScanner.nextLine());
        GeometricShape[] myShapes = new GeometricShape[shapesNo];
        // creating singleton shape factory
        ShapeFactory uniqueFactory = ShapeFactory.getInstance();

        int imageWidth = 0;
        int imageHeight = 0;
        System.out.println("2");
        for (int i = 0; i < shapesNo; i++) {
            // reading each shape descriptor
            String descriptionBuffer = fileScanner.nextLine();
            // if the shape is a canvas
            // initialize the image with its dimentions
            if (i == 0) {
                StringTokenizer tokenizer =
                new StringTokenizer(descriptionBuffer);
                int cnt = 0;
                final int maxCnt = 3;
                while (cnt < maxCnt) {
                    String temp = tokenizer.nextToken();
                    if (cnt == 1) {
                        // image height is second argument of canvas descriptor
                        imageHeight = Integer.parseInt(temp);
                    }
                    if (cnt == 2) {
                        // image width is third argument of canvas descriptor
                        imageWidth = Integer.parseInt(temp);
                    }
                    cnt++;
                }
            }
            // for each shape, get a shape of its type and add it to myShapes
            String shapeType =
            descriptionBuffer.substring(0, descriptionBuffer.indexOf(' '));
            System.out.println("3");
            myShapes[i] = uniqueFactory.getShape(shapeType);
            myShapes[i].setShapeDescriptor(descriptionBuffer);
        }
        fileScanner.close();
        System.out.println("4");
        BufferedImage myImage = new BufferedImage(imageWidth, imageHeight,
        BufferedImage.TYPE_INT_ARGB); // initializing output image

        // visit each geometric shape and draw it on the output file
        Visitor drawingVisitor = new DrawingVisitor();
        for (GeometricShape gs: myShapes) {
            gs.accept(drawingVisitor, myImage);
        }
        System.out.println("5");
        File outputImage = null;
        try {
            // create output
            outputImage = new File("drawing.png");
            ImageIO.write(myImage, "png", outputImage);
        } catch (IOException e) {  // me oussie, how bow dah.
            System.out.println("ERROR: " + e);
        }
    }
}
