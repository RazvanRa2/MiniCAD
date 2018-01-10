// Copyright (C), Razvan Radoi, first of his name
import java.awt.image.BufferedImage;
public final class Canvas extends GeometricShape {
    // canvas specifically accepts visitor
    @Override
    public void accept(final Visitor v, final BufferedImage image) {
        v.visit(this, image);
    }
}
