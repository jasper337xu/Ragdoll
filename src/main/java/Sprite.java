import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Sprite {
    Sprite parent = null;
    Affine matrix = new Affine();
    Vector<Sprite> children = new Vector<Sprite>();
    ImageView iv = null;
    private double x, y;

    enum OPERATION {TRANSLATE, SCALE_UP, SCALE_DOWN, ROTATE}
    OPERATION operation = null;
    List<OPERATION> localOps = new ArrayList<>();
    List<double[]> localOpParams = new ArrayList<>();

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
        operation = OPERATION.TRANSLATE;
        localOps.add(OPERATION.TRANSLATE);
        double[] param = new double[] {dx, dy};
        localOpParams.add(param);

        //matrix.prependTranslation(dx, dy);
    }

    void rotate(double theta) throws NonInvertibleTransformException {
        operation = OPERATION.ROTATE;
        localOps.add(OPERATION.ROTATE);
        double[] param = new double[] {theta};
        localOpParams.add(param);

        //Affine fullMatrix = getFullMatrix();
        //Affine inverse = fullMatrix.createInverse();

        // move to the origin, rotate and move back
        //matrix.prepend(inverse);
        //matrix.prependRotation(theta);
        //matrix.prepend(fullMatrix);
    }

    void scale(double sx, double sy) throws NonInvertibleTransformException {
        //Affine fullMatrix = getFullMatrix();
        //Affine inverse = fullMatrix.createInverse();

        // move to the origin, rotate and move back
        //matrix.prepend(inverse);
        //matrix.prependScale(sx, sy);
        //matrix.prepend(fullMatrix);
    }

    Affine getLocalMatrix() {
        matrix = new Affine();
        for (int i = 0; i < localOps.size(); i++) {
            OPERATION localOp = localOps.get(i);
            double[] localOpParam = localOpParams.get(i);
            if (localOp == OPERATION.TRANSLATE) {
                matrix.prependTranslation(localOpParam[0], localOpParam[1]);
            }
            else if (localOp == OPERATION.ROTATE) { // must have a parent
                Affine parentMatrix = parent.getFullMatrix();
                Affine parentInverse = null;
                try {
                    parentInverse = parentMatrix.createInverse();
                } catch (NonInvertibleTransformException e) { // should not get here
                    parentInverse = new Affine();
                }
                matrix.prepend(parentInverse);
                matrix.prependRotation(localOpParam[0]);
                matrix.prepend(parentMatrix);
            }
        }
        return matrix;
    }

    Affine getFullMatrix() {
        Affine fullMatrix = getLocalMatrix().clone();
/*
        if (parent == null) {
            return fullMatrix;
        }
        Affine parentMatrix = parent.getFullMatrix();
        Affine parentInverse = null;
        if (operation == OPERATION.TRANSLATE || operation == null) {
            fullMatrix.append(parentMatrix);
        } else if (operation == OPERATION.ROTATE) {
            try {
                parentInverse = parentMatrix.createInverse();
            } catch (NonInvertibleTransformException e) { // should not get here
                parentInverse = new Affine();
            }
            fullMatrix.append(parentInverse);
            fullMatrix.prepend(parentMatrix);
            fullMatrix.append(parentMatrix);
        }
*/
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
