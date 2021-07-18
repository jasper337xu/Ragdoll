import javafx.geometry.Point2D;
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
    Point2D parentPivot = null;
    Point2D selfPivot = null;
    //boolean hasConnected = false;

    enum OPERATION {TRANSLATE, SCALE_UP, SCALE_DOWN, ROTATE}
    // TODO: may not need operation
    OPERATION operation = null;
    List<OPERATION> localOps = new ArrayList<>();
    List<double[]> localOpParams = new ArrayList<>();

    public Sprite(ImageView iv) {
        this.iv = iv;
        x = 0;
        y = 0;
    }

    public Sprite(ImageView iv, double ppx, double ppy, double spx, double spy) {
        this(iv);
        parentPivot = new Point2D(ppx, ppy);
        selfPivot = new Point2D(spx, spy);
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
        operation = OPERATION.SCALE_UP;
        localOps.add(OPERATION.SCALE_UP);
        double[] param = new double[] {sx, sy};
        localOpParams.add(param);
        //Affine fullMatrix = getFullMatrix();
        //Affine inverse = fullMatrix.createInverse();

        // move to the origin, rotate and move back
        //matrix.prepend(inverse);
        //matrix.prependScale(sx, sy);
        //matrix.prepend(fullMatrix);
    }

    Affine getLocalMatrix() {
        matrix = new Affine();
        Affine parentMatrix = null;
        for (int i = 0; i < localOps.size(); i++) {
            OPERATION localOp = localOps.get(i);
            double[] localOpParam = localOpParams.get(i);
            if (localOp == OPERATION.TRANSLATE) {
                matrix.prependTranslation(localOpParam[0], localOpParam[1]);
            }
            else if (localOp == OPERATION.ROTATE) { // must have a parent
                //parentMatrix = parent.getFullMatrix();
                parentMatrix = parent.matrix.clone();
                Affine parentInverse;
                try {
                    parentInverse = parentMatrix.createInverse();
                } catch (NonInvertibleTransformException e) { // should not get here
                    parentInverse = new Affine();
                }
                matrix.prepend(parentInverse);
                matrix.prependRotation(localOpParam[0]);
                matrix.prepend(parentMatrix);
            }
            // TODO: Scale
        }
        //if (localOps.get(localOps.size() - 1) == OPERATION.ROTATE) {
        if (localOps.get(localOps.size() - 1) == OPERATION.ROTATE) {
        //if (localOps.get(localOps.size() - 1) == OPERATION.ROTATE && !hasConnected) {
        //if (localOps.get(localOps.size() - 1) == OPERATION.ROTATE && parentPivot.getX() == 0) {
            Point2D curParentPivot = parentMatrix.transform(parentPivot);
            System.out.println("" + curParentPivot.getX() + ", " + curParentPivot.getY());
            Affine m = matrix.clone();
            m.append(parentMatrix);
            Point2D curSelfPivot = m.transform(selfPivot);
            System.out.println("" + curSelfPivot.getX() + ", " + curSelfPivot.getY());
            //if (parentPivot.getX() == 0) {
            matrix.prependTranslation(curParentPivot.getX() - curSelfPivot.getX(), curParentPivot.getY() - curSelfPivot.getY());
            //}
            //hasConnected = true;
        }
        return matrix;
    }

    Affine getFullMatrix() {
        Affine fullMatrix = getLocalMatrix().clone();
        if (parent != null) {
            //Affine parentMatrix = parent.getFullMatrix();
            //fullMatrix.append(parentMatrix);
            //fullMatrix.append(parent.getFullMatrix());
            fullMatrix.append(parent.matrix.clone());
        }
        matrix = fullMatrix;
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
