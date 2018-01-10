// Copyright (C), Razvan Radoi, first of his name
import java.awt.image.BufferedImage;
// geometric shape implements visitable and shape interfaces
// all geometric shapes extend this class
class GeometricShape implements Visitable, Shape {
    protected String shapeDescriptor;
    @Override
    public void setShapeDescriptor(final String newDescriptor) {
        this.shapeDescriptor = newDescriptor;
    }

    @Override
    public String getShapeDescriptor() {
        return this.shapeDescriptor;
    }

    @Override
    public void accept(final Visitor v, final BufferedImage image) {
        v.visit(this, image);
    }
}
