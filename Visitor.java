// Copyright (C), Razvan Radoi, first of his name

import java.awt.image.BufferedImage;

public interface Visitor {
    // visitor interface is implemented by Drawing visitor
    void visit(GeometricShape gs, BufferedImage image);
    void visit(Canvas c, BufferedImage image);
    void visit(Circle c, BufferedImage image);
    void visit(Diamond d, BufferedImage image);
    void visit(Line l, BufferedImage image);
    void visit(Polygon p, BufferedImage image);
    void visit(Rectangle r, BufferedImage image);
    void visit(Square s, BufferedImage image);
    void visit(Triangle t, BufferedImage image);
}
