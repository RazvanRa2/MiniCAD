// Copyright (C), Razvan Radoi, first of his name
import java.util.StringTokenizer;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;
import java.awt.Point;
public final class DrawingVisitor implements Visitor {
    // One, two...
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    // ...Everybody in the car, so come on let's ride
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final int EIGHT = 8;
    private static final int NINE = 9;
    private static final int TEN = 10;
    private static final int ELEVEN = 11;

    @Override
    public void visit(final Canvas c, final BufferedImage image) {
        // visit canvas
        System.out.println("hello");
        String descriptor = c.getShapeDescriptor();  // get descriptor
        String[] canvasProprieties = new String[FIVE];
        int cnt = 0;

        StringTokenizer tokenizer = new StringTokenizer(descriptor);
        while (tokenizer.hasMoreTokens()) { // tokenize descriptor
            canvasProprieties[cnt] = tokenizer.nextToken();
            cnt++;
        }
        // set canvas parameters
        int canvasHeight = Integer.parseInt(canvasProprieties[1]);
        int canvasWidth = Integer.parseInt(canvasProprieties[2]);

        String canvasColour = canvasProprieties[THREE];
        int canvasAlpha = Integer.parseInt(canvasProprieties[FOUR]);

        int canvasARGB = getARGB(canvasColour, canvasAlpha);
        // draw whole image with the canvas colour
        for (int y = 0; y < canvasHeight; y++) {
            for (int x = 0; x < canvasWidth; x++) {
                image.setRGB(x, y, canvasARGB);
            }
        }
        System.out.println("hello");
    }

    @Override
    public void visit(final Circle c, final BufferedImage image) {
        // visit circle
        String descriptor = c.getShapeDescriptor();
        String[] circleProprieties = new String[EIGHT];
        int cnt = 0;
        StringTokenizer tokenizer = new StringTokenizer(descriptor);
        while (tokenizer.hasMoreTokens()) {  // tokenize descriptor
            circleProprieties[cnt] = tokenizer.nextToken();
            cnt++;
        }
        // set circle parameters
        int originX = Integer.parseInt(circleProprieties[1]);
        int originY = Integer.parseInt(circleProprieties[2]);
        int radius = Integer.parseInt(circleProprieties[THREE]);

        String contourColour = circleProprieties[FOUR];
        int contourAlpha = Integer.parseInt(circleProprieties[FIVE]);

        String circleColour = circleProprieties[SIX];
        int circleAlpha = Integer.parseInt(circleProprieties[SEVEN]);

        int contourARGB = getARGB(contourColour, contourAlpha);
        // draw circle outline
        circleGenerator(originX, originY, radius, contourARGB, image);
        int shapeARGB = getARGB(circleColour, circleAlpha);
        // flood fill the circle
        floodFillImage(image, originX, originY, shapeARGB, contourARGB);
    }

    @Override
    public void visit(final Diamond d, final BufferedImage image) {
        // visit diamond
        String descriptor = d.getShapeDescriptor();
        String[] diamondProprieties = new String[NINE];
        int cnt = 0;

        StringTokenizer tokenizer = new StringTokenizer(descriptor);
        while (tokenizer.hasMoreTokens()) {  // tokenize input
            diamondProprieties[cnt] = tokenizer.nextToken();
            cnt++;
        }
        // set shape parameters
        int centerX = Integer.parseInt(diamondProprieties[1]);
        int centerY = Integer.parseInt(diamondProprieties[2]);
        int horizDiagonal = Integer.parseInt(diamondProprieties[THREE]);
        int verticalDiagonal = Integer.parseInt(diamondProprieties[FOUR]);

        String contourColour = diamondProprieties[FIVE];
        int contourAlpha = Integer.parseInt(diamondProprieties[SIX]);

        String shapeColour = diamondProprieties[SEVEN];
        int shapeAlpha = Integer.parseInt(diamondProprieties[EIGHT]);

        int leftX, leftY;
        int rightX, rightY;
        int topX, topY;
        int bottomX, bottomY;
        // set corners parameters
        leftX = centerX - Math.round(horizDiagonal / 2);
        leftY = centerY;
        rightX = centerX + Math.round(horizDiagonal / 2);
        rightY = centerY;
        topX = centerX;
        topY = centerY - Math.round(verticalDiagonal / 2);
        bottomX = centerX;
        bottomY = centerY + Math.round(verticalDiagonal / 2);
        // create diamond contour
        int contourARGB = getARGB(contourColour, contourAlpha);
        drawLine(bottomX, bottomY, rightX, rightY, contourARGB, image);
        drawLine(rightX, rightY, topX, topY, contourARGB, image);
        drawLine(topX, topY, leftX, leftY,  contourARGB, image);
        drawLine(leftX, leftY, bottomX, bottomY, contourARGB, image);
        int shapeARGB = getARGB(shapeColour, shapeAlpha);
        // flood fill the diamond's contour
        floodFillImage(image, centerX, centerY, shapeARGB, contourARGB);
    }

    @Override
    public void visit(final Line l, final BufferedImage image) {
        // visit line
        String descriptor = l.getShapeDescriptor();
        String[] lineProprieties = new String[SEVEN];
        int cnt = 0;

        StringTokenizer tokenizer = new StringTokenizer(descriptor);
        while (tokenizer.hasMoreTokens()) {  // tokenize line descriptor
            lineProprieties[cnt] = tokenizer.nextToken();
            cnt++;
        }
        // set line start and end points
        int startX = Integer.parseInt(lineProprieties[1]);
        int startY = Integer.parseInt(lineProprieties[2]);
        int endX = Integer.parseInt(lineProprieties[THREE]);
        int endY = Integer.parseInt(lineProprieties[FOUR]);

        String lineColour = lineProprieties[FIVE];
        int lineAlpha = Integer.parseInt(lineProprieties[SIX]);
        int lineARGB = getARGB(lineColour, lineAlpha);
        // draw line from start point to end point
        drawLine(startX, startY, endX, endY, lineARGB, image);
    }

    @Override
    public void visit(final Polygon p, final BufferedImage image) {
        // visit polygon
        String descriptor = p.getShapeDescriptor();

        String buffer = new String(descriptor);
        buffer = buffer.substring(EIGHT);
        int bufferEnd = buffer.indexOf(' ');
        buffer = buffer.substring(0, bufferEnd);
        int cornersNo = Integer.parseInt(buffer);

        String[] polyProprieties = new String[SIX + cornersNo * 2];
        int cnt = 0;
        StringTokenizer tokenizer = new StringTokenizer(descriptor);
        while (tokenizer.hasMoreTokens()) {  // tokenize polygon descriptor
            polyProprieties[cnt] = tokenizer.nextToken();
            cnt++;
        }
        // set polygonXs and polygonYs to aknowledge corners
        System.out.println(cornersNo);
        int[] polygonXs = new int[cornersNo];
        int[] polygonYs = new int[cornersNo];
        for (int i = 0; i < cornersNo * 2; i++) {
            if (i % 2 == 0) {
                polygonXs[i / 2] = Integer.parseInt(polyProprieties[i + 2]);
            } else {
                polygonYs[i / 2] = Integer.parseInt(polyProprieties[i + 2]);
            }
        }
        // set colours
        String contourColour = polyProprieties[2 + cornersNo * 2];
        int contourAlpha = Integer.parseInt(polyProprieties[2 + cornersNo * 2 + 1]);
        System.out.println(contourColour + " " + contourAlpha);

        String shapeColour = polyProprieties[2 + cornersNo * 2 + 2];
        int shapeAlpha = Integer.parseInt(polyProprieties[2 + cornersNo * 2 + THREE]);
        System.out.println(shapeColour + " " + shapeAlpha);

        int contourARGB = getARGB(contourColour, contourAlpha);
        // draw lines for polygon
        for (int i = 0; i < cornersNo - 1; i++) {
            drawLine(polygonXs[i], polygonYs[i], polygonXs[i + 1],
            polygonYs[i + 1], contourARGB, image);
        }
        drawLine(polygonXs[cornersNo - 1],
        polygonYs[cornersNo - 1], polygonXs[0], polygonYs[0],  contourARGB, image);

        int centerX = 0;
        int centerY = 0;
        for (int i = 0; i < cornersNo; i++) {
            centerX += polygonXs[i];
            centerY += polygonYs[i];
        }
        centerY /= cornersNo;
        centerX /= cornersNo;
        int shapeARGB = getARGB(shapeColour, shapeAlpha);
        // then flood fill the polygon's interior perimeter starting from
        // its center of gravity
        floodFillImage(image, centerX, centerY, shapeARGB, contourARGB);

    }

    @Override
    public void visit(final Rectangle r, final BufferedImage image) {
        // visit rectangle
        String descriptor = r.getShapeDescriptor();
        String[] rectProprieties = new String[NINE];
        int cnt = 0;

        StringTokenizer tokenizer = new StringTokenizer(descriptor);
        while (tokenizer.hasMoreTokens()) {  // tokenize rectangle descriptor
            rectProprieties[cnt] = tokenizer.nextToken();
            cnt++;
        }
        // set rectangle parameters
        int topleftX = Integer.parseInt(rectProprieties[1]);
        int topleftY = Integer.parseInt(rectProprieties[2]);
        int rectHeight  = Integer.parseInt(rectProprieties[THREE]);
        int rectWidth  = Integer.parseInt(rectProprieties[FOUR]);

        String contourColour = rectProprieties[FIVE];
        int contourAlpha = Integer.parseInt(rectProprieties[SIX]);

        String shapeColour = rectProprieties[SEVEN];
        int shapeAlpha = Integer.parseInt(rectProprieties[EIGHT]);

        int contourARGB = getARGB(contourColour, contourAlpha);
        // draw the rectangle
        drawLine(topleftX, topleftY, topleftX, topleftY + rectHeight - 1,
        contourARGB, image);
        drawLine(topleftX, topleftY, topleftX + rectWidth - 1, topleftY,
        contourARGB, image);
        drawLine(topleftX + rectWidth - 1, topleftY, topleftX + rectWidth - 1,
        topleftY + rectHeight - 1, contourARGB, image);
        drawLine(topleftX, topleftY + rectHeight - 1, topleftX + rectWidth - 1,
        topleftY + rectHeight - 1, contourARGB, image);
        // colour the rectangle's area with its specific colour
        int centerX = topleftX + 1;
        int centerY = topleftY + 1;
        int shapeARGB = getARGB(shapeColour, shapeAlpha);
        for (int y = topleftY; y < topleftY + rectHeight - 1; y++) {
            for (int x = topleftX; x < topleftX + rectWidth - 1; x++) {
                if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
                    if (image.getRGB(x, y) != contourARGB) {
                        image.setRGB(x, y, shapeARGB);
                    }
                }
            }
        }
    }

    @Override
    public void visit(final Square s, final BufferedImage image) {
        // visit square
        System.out.println("Squre hello");
        String descriptor = s.getShapeDescriptor();
        String[] squareProprieties = new String[EIGHT];
        int cnt = 0;

        System.out.println("SqurLe hello");
        StringTokenizer tokenizer = new StringTokenizer(descriptor);
        while (tokenizer.hasMoreTokens()) {  // tokenize input
            squareProprieties[cnt] = tokenizer.nextToken();
            cnt++;
        }
        System.out.println("SqurLe hello");

        // set square parameters
        int topleftX = Integer.parseInt(squareProprieties[1]);
        int topleftY = Integer.parseInt(squareProprieties[2]);
        int squareWidth  = Integer.parseInt(squareProprieties[THREE]);

        String contourColour = squareProprieties[FOUR];
        int contourAlpha = Integer.parseInt(squareProprieties[FIVE]);

        String shapeColour = squareProprieties[SIX];
        int shapeAlpha = Integer.parseInt(squareProprieties[SEVEN]);

        int contourARGB = getARGB(contourColour, contourAlpha);
        // draw square's outline
        drawLine(topleftX, topleftY, topleftX, topleftY + squareWidth - 1,
        contourARGB, image);
        drawLine(topleftX, topleftY, topleftX + squareWidth - 1, topleftY,
        contourARGB, image);
        drawLine(topleftX + squareWidth - 1, topleftY, topleftX + squareWidth - 1,
        topleftY + squareWidth - 1, contourARGB, image);
        drawLine(topleftX, topleftY + squareWidth - 1, topleftX + squareWidth - 1,
        topleftY + squareWidth - 1, contourARGB, image);

        int shapeARGB = getARGB(shapeColour, shapeAlpha);
        // colour the square's area in its specific colour
        for (int y = topleftY; y < topleftY + squareWidth - 1; y++) {
            for (int x = topleftX; x < topleftX + squareWidth - 1; x++) {
                if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
                    if (image.getRGB(x, y) != contourARGB) {
                        image.setRGB(x, y, shapeARGB);
                    }
                }
            }
        }
        System.out.println("SqurLe hello");

    }

    @Override
    public void visit(final Triangle t, final BufferedImage image) {
        // visit triangle
        String descriptor = t.getShapeDescriptor();

        String[] triangleProprieties = new String[ELEVEN];
        int cnt = 0;

        StringTokenizer tokenizer = new StringTokenizer(descriptor);
        while (tokenizer.hasMoreTokens()) {  // tokenize triangle descriptor
            triangleProprieties[cnt] = tokenizer.nextToken();
            cnt++;
        }
        // set triangle parameters
        int trX1 = Integer.parseInt(triangleProprieties[1]);
        int trY1 = Integer.parseInt(triangleProprieties[2]);
        int trX2 = Integer.parseInt(triangleProprieties[THREE]);
        int trY2 = Integer.parseInt(triangleProprieties[FOUR]);
        int trXTHREE = Integer.parseInt(triangleProprieties[FIVE]);
        int trYTHREE = Integer.parseInt(triangleProprieties[SIX]);
        String contourColour = triangleProprieties[SEVEN];
        int contourAlpha = Integer.parseInt(triangleProprieties[EIGHT]);

        String shapeColour = triangleProprieties[NINE];
        int shapeAlpha = Integer.parseInt(triangleProprieties[TEN]);

        int contourARGB = getARGB(contourColour, contourAlpha);

        // create triangle outline
        drawLine(trX1, trY1, trX2, trY2, contourARGB, image);
        drawLine(trX2, trY2, trXTHREE, trYTHREE, contourARGB, image);
        drawLine(trXTHREE, trYTHREE, trX1, trY1, contourARGB, image);

        int shapeARGB = getARGB(shapeColour, shapeAlpha);
        int centerX = (trX1 + trX2 + trXTHREE) / THREE;
        int centerY = (trY1 + trY2 + trYTHREE) / THREE;
        // flood fill triangle starting from its center of gravity
        floodFillImage(image, centerX, centerY, shapeARGB, contourARGB);
    }

    @Override
    public void visit(final GeometricShape gs, final BufferedImage image) {
        String descriptor = gs.getShapeDescriptor();
        System.out.println("Drawing general shape");
        // move along, these are not the droids you are looking for
    }

    private int getARGB(final String rgbHexa, final int alpha) {
        // getARGB method transforms input of type #COLOUR in its argb equiv.
        final int hexaBase = 16;
        // get red, green and blue values
        int rValue = Integer.valueOf(rgbHexa.substring(1, THREE), hexaBase);
        int gValue = Integer.valueOf(rgbHexa.substring(THREE, FIVE), hexaBase);
        int bValue = Integer.valueOf(rgbHexa.substring(FIVE, SEVEN), hexaBase);
        final int alphaShift = 24;
        final int redShift = 16;
        final int greenShift = 8;
        // shift red, green, blue and alpha values to their specific positions
        // within the argb integer
        int argb = (alpha << alphaShift) | (rValue << redShift)
        | (gValue << greenShift) | bValue;

        return argb;
    }

    private void drawLine(final int x1, final int y1, final int x2, final int y2,
    final int argb, final BufferedImage image) {
        // draw line method implements the Bresenham algorithm
        // to be 100% honest, don't ask me how it works
        // i just used the algorithm
        int maxWidth = image.getWidth();
        int maxHeight = image.getHeight();

            int x = x1;
            int y = y1;
            int deltaX = Math.abs(x2 - x1);
            int deltaY = Math.abs(y2 - y1);
            int s1 = Integer.signum(x2 - x1);
            int s2 = Integer.signum(y2 - y1);

            int temp;
            Boolean interchanged = false;
            if (deltaY > deltaX) {
                temp = deltaX;
                deltaX = deltaY;
                deltaY = temp;

                interchanged = true;
            } else {
                interchanged = false;
            }

            int error = 2 * deltaY - deltaX;

            for (int i = 0; i <= deltaX; i++) {
                // only colour those pixels that are within the image
                // and ignore points outside the canvas
                if (x >= 0 && y >= 0 && x < maxWidth && y < maxHeight) {
                    image.setRGB(x, y, argb);
                }
                while (error > 0) {
                    if (interchanged) {
                        x += s1;
                    } else {
                        y += s2;
                    }

                    error -= 2 * deltaX;
                }

                if (interchanged) {
                    y += s2;
                } else {
                    x += s1;
                }

                error += 2 * deltaY;
            }
    }

    void drawCircle(final int cx, final int cy, final int x, final int y,
    final int colour, final BufferedImage image) {
        // draw circle method implements the Bresenham algorithm
        // it draws the circle only if it is within the canvas
        int maxWidth = image.getWidth();
        int maxHeight = image.getHeight();
        if (cx + x >= 0 && cx + x < maxWidth && cy + y >= 0 && cy + y < maxHeight) {
            image.setRGB(cx + x, cy + y, colour);
        }
        if (cx - x >= 0 && cx - x < maxWidth && cy + y >= 0 && cy + y < maxHeight) {
            image.setRGB(cx - x, cy + y, colour);
        }
        if (cx + x >= 0 && cx + x < maxWidth && cy - y >= 0 && cy - y < maxHeight) {
            image.setRGB(cx + x, cy - y, colour);
        }
        if (cx - x >= 0 && cx - x < maxWidth && cy - y >= 0 && cy - y < maxHeight) {
            image.setRGB(cx - x, cy - y, colour);
        }
        if (cx + y >= 0 && cx + y < maxWidth && cy + x >= 0 && cy + x < maxHeight) {
            image.setRGB(cx + y, cy + x, colour);
        }
        if (cx - y >= 0 && cx - y < maxWidth && cy + x >= 0 && cy + x < maxHeight) {
            image.setRGB(cx - y, cy + x, colour);
        }
        if (cx + y >= 0 && cx + y < maxWidth && cy - x >= 0 && cy - x < maxHeight) {
            image.setRGB(cx + y, cy - x, colour);
        }
        if (cx - y >= 0 && cx - y < maxWidth && cy - x >= 0 && cy - x < maxHeight) {
            image.setRGB(cx - y, cy - x, colour);
        }
    }

    private void circleGenerator(final int cx, final int cy, final int rad,
    final int colour, final BufferedImage image) {
        // circle generator calls drawCircle gracefully
        // this Bressenham guy was one smart person
        int x = 0;
        int y = rad;
        int d = THREE - 2 * rad;

        while (y >= x) {
            drawCircle(cx, cy, x, y, colour, image);
            x++;

            if (d > 0) {
                y--;
                d = d + FOUR * (x - y) + TEN;
            } else {
                d = d + FOUR * x + SIX;
            }

            drawCircle(cx, cy, x, y, colour, image);
        }
    }

    public static void floodFillImage(final BufferedImage image, final int x,
    final int y, final int colour, final int contourColor) {
        // hits keeps count of the pixels that were already coloured
        boolean[][] hits = new boolean[image.getHeight()][image.getWidth()];
        // use a queue because classic flood fill results in stack overflow
        Queue<Point> queue = new LinkedList<Point>();
        queue.add(new Point(x, y));
        // add given start point to the queue
        while (!queue.isEmpty()) {
            Point p = queue.remove();  // remove point from queue
            // start flood fill from that point, if point is good to go
            if (floodFillImageDo(image, hits, p.x, p.y, contourColor, colour)) {
                queue.add(new Point(p.x, p.y - 1));  // then colour all its
                queue.add(new Point(p.x, p.y + 1));  // neighbours
                queue.add(new Point(p.x - 1, p.y));
                queue.add(new Point(p.x + 1, p.y));
            }
        }
    }

    private static boolean floodFillImageDo(final BufferedImage image,
    final boolean[][] hits, final int x, final int y, final int contourColor,
    final int colour) {
        // check if given point is within canvas
        if (y < 0) {
            return false;
        }
        if (x < 0) {
            return false;
        }
        if (y > image.getHeight() - 1) {
            return false;
        }
        if (x > image.getWidth() - 1) {
            return false;
        }
        // check if point has already been coloured
        if (hits[y][x]) {
            return false;
        }
        // check if given point is not part of the shape contour
        if (image.getRGB(x, y) == contourColor) {
            return false;
        }
        // if you got here, the point is good to go, colour it
        image.setRGB(x, y, colour);
        hits[y][x] = true;  // remember you visited this point
        return true;
    }
}
// boy, is this one huge class
