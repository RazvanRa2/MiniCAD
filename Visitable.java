// Copyright (C), Razvan Radoi, first of his name

import java.awt.image.BufferedImage;
public interface Visitable {
    // interface is implemented by the GeometricShape class and then extended by
    // all geometric shapes
    void accept(Visitor v, BufferedImage image);
}
