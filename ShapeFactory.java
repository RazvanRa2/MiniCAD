// Copyright (C), Razvan Radoi, first of his name

public final class ShapeFactory {
    // class ShapeFactory is singleton
    public static final ShapeFactory FACTORY_INSTANCE = new ShapeFactory();

    private ShapeFactory() {

    }
    // ShapeFactory returns a shape by a given string
    public GeometricShape getShape(final String shapeType) {
        if (shapeType.equals("CANVAS")) {
            return new Canvas();
        }
        if (shapeType.equals("LINE")) {
            return new Line();
        }
        if (shapeType.equals("SQUARE")) {
            return new Square();
        }
        if (shapeType.equals("RECTANGLE")) {
            return new Rectangle();
        }
        if (shapeType.equals("CIRCLE")) {
            return new Circle();
        }
        if (shapeType.equals("TRIANGLE")) {
            return new Triangle();
        }
        if (shapeType.equals("DIAMOND")) {
            return new Diamond();
        }
        if (shapeType.equals("POLYGON")) {
            return new Polygon();
        }

        return null;
    }

    // instance getter
    public static ShapeFactory getInstance() {
        return FACTORY_INSTANCE;
    }
}
