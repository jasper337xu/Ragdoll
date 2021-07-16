import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

import java.util.Vector;

public class Sprite {
    Sprite parent = null;
    Affine matrix = new Affine();
    Vector<Sprite> children = new Vector<Sprite>();
    ImageView iv = null;
    private double x, y;

    public Sprite(ImageView iv) {
        this.iv = iv;
        x = 0;
        y = 0;
    }

    public Sprite(ImageView iv, Sprite parent) {
        this(iv);
        if (parent != null) {
            parent.addChild(this);
        }
    }

    // maintain hierarchy
    public void addChild(Sprite child) {
        this.children.add(child);
        child.setParent(this);
    }

    void translate(double dx, double dy) {
        matrix.prependTranslation(dx, dy);
    }

    void rotate(double theta) throws NonInvertibleTransformException {
        Affine fullMatrix = getFullMatrix();
        Affine inverse = fullMatrix.createInverse();

        // move to the origin, rotate and move back
        matrix.prepend(inverse);
        matrix.prependRotation(theta);
        matrix.prepend(fullMatrix);
    }

    void scale(double sx, double sy) throws NonInvertibleTransformException {
        Affine fullMatrix = getFullMatrix();
        Affine inverse = fullMatrix.createInverse();

        // move to the origin, rotate and move back
        matrix.prepend(inverse);
        matrix.prependScale(sx, sy);
        matrix.prepend(fullMatrix);
    }

    Affine getLocalMatrix() {
        return matrix;
    }

    Affine getFullMatrix() {
        Affine fullMatrix = getLocalMatrix().clone();
        if (parent != null) {
            fullMatrix.append(parent.getFullMatrix());
        }
        return fullMatrix;
    }

    // Draw on the supplied canvas
    public void draw(GraphicsContext gc) {
        // save the current graphics context so that we can restore later
        Affine oldMatrix = gc.getTransform();

        // make sure we have the correct transformations for this shape
        gc.setTransform(getFullMatrix());
        gc.drawImage(iv.getImage(), x, y);

        // draw children
        for (Sprite child : children) {
            child.draw(gc);
        }

        // set back to original value since we're done with this branch of the scene graph
        gc.setTransform(oldMatrix);
    }

    private void setParent(Sprite parent) {
        this.parent = parent;
    }
}
