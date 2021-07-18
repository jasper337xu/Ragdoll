import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.Stage;

public class Ragdoll extends Application {
    final float SCREEN_WIDTH = 1024;
    final float SCREEN_HEIGHT = 768;

    @Override
    public void start(Stage stage) {
        MenuBar menuBar = createFileMenu(stage);
        // setup a canvas to use as a drawing surface
        Canvas canvas = createCanvas();

        Sprite torso = createSprites();
        draw(canvas, torso);
//        gc.drawImage(torso.iv.getImage(), 0, 0);


        Group root = new Group();
        root.getChildren().add(menuBar);
        root.getChildren().add(canvas);

        stage.setResizable(false);
        stage.setWidth(SCREEN_WIDTH);
        stage.setHeight(SCREEN_HEIGHT);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void draw(Canvas canvas, Sprite root) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.draw(gc);
    }

    private Sprite createSprites() {
        Image torsoImg = new Image("torso.png", 80, 150, true, true);
        ImageView torsoIv = new ImageView(torsoImg);
        torsoIv.setFitWidth(80);
        torsoIv.setFitHeight(150);
        Sprite torso = new Sprite(torsoIv);

        Image headImg = new Image("head.png", 50, 80, true, true);
        ImageView headIv = new ImageView(headImg);
        headIv.setFitWidth(50);
        headIv.setFitHeight(80);
        Sprite head = new Sprite(headIv, 40, 0, 25, 80);

        Image lUpperArmImg = new Image("upper_arm.png", 30, 100, true, true);
        ImageView lUpperArmIv = new ImageView(lUpperArmImg);
        lUpperArmIv.setFitWidth(30);
        lUpperArmIv.setFitHeight(100);
        Sprite lUpperArm = new Sprite(lUpperArmIv, 0, 0, 15, 0);

        Image rUpperArmImg = new Image("upper_arm.png", 30, 100, true, true);
        ImageView rUpperArmIv = new ImageView(rUpperArmImg);
        rUpperArmIv.setFitWidth(30);
        rUpperArmIv.setFitHeight(100);
        Sprite rUpperArm = new Sprite(rUpperArmIv, 80, 0, 15, 0);

        Image lLowerArmImg = new Image("lower_arm.png", 20, 80, true, true);
        ImageView lLowerArmIv = new ImageView(lLowerArmImg);
        lLowerArmIv.setFitWidth(20);
        lLowerArmIv.setFitHeight(80);
        Sprite lLowerArm = new Sprite(lLowerArmIv, 15, 75, 10, 0);

        Image rLowerArmImg = new Image("lower_arm.png", 20, 80, true, true);
        ImageView rLowerArmIv = new ImageView(rLowerArmImg);
        rLowerArmIv.setFitWidth(20);
        rLowerArmIv.setFitHeight(80);
        Sprite rLowerArm = new Sprite(rLowerArmIv, 15, 75, 10, 0);

        Image lHandImg = new Image("hand.png", 30, 30, true, true);
        ImageView lHandIv = new ImageView(lHandImg);
        lHandIv.setFitWidth(30);
        lHandIv.setFitHeight(30);
        Sprite lHand = new Sprite(lHandIv, 10, 60, 15, 0);

        Image rHandImg = new Image("hand.png", 30, 30, true, true);
        ImageView rHandIv = new ImageView(rHandImg);
        rHandIv.setFitWidth(30);
        rHandIv.setFitHeight(30);
        Sprite rHand = new Sprite(rHandIv, 10, 60, 15, 0);

        Image lUpperLegImg = new Image("leg.png", 20, 120, true, true);
        ImageView lUpperLegIv = new ImageView(lUpperLegImg);
        lUpperLegIv.setFitWidth(20);
        lUpperLegIv.setFitHeight(120);
        Sprite lUpperLeg = new Sprite(lUpperLegIv, 10, 150, 10, 0);

        Image rUpperLegImg = new Image("leg.png", 20, 120, true, true);
        ImageView rUpperLegIv = new ImageView(rUpperLegImg);
        rUpperLegIv.setFitWidth(20);
        rUpperLegIv.setFitHeight(120);
        Sprite rUpperLeg = new Sprite(rUpperLegIv, 70, 150, 10, 0);

        Image lLowerLegImg = new Image("leg.png", 15, 90, true, true);
        ImageView lLowerLegIv = new ImageView(lLowerLegImg);
        lLowerLegIv.setFitWidth(15);
        lLowerLegIv.setFitHeight(90);
        Sprite lLowerLeg = new Sprite(lLowerLegIv, 10, 90, 7.5, 0);

        Image rLowerLegImg = new Image("leg.png", 15, 90, true, true);
        ImageView rLowerLegIv = new ImageView(rLowerLegImg);
        rLowerLegIv.setFitWidth(15);
        rLowerLegIv.setFitHeight(90);
        Sprite rLowerLeg = new Sprite(rLowerLegIv, 10, 90, 7.5, 0);

        Image lFootImg = new Image("foot.png", 50, 20, true, true);
        ImageView lFootIv = new ImageView(lFootImg);
        lFootIv.setFitWidth(50);
        lFootIv.setFitHeight(20);
        Sprite lFoot = new Sprite(lFootIv, 7.5, 67.5, 50, 10);

        Image rFootImg = new Image("foot.png", 50, 20, true, true);
        ImageView rFootIv = new ImageView(rFootImg);
        rFootIv.setFitWidth(50);
        rFootIv.setFitHeight(20);
        Sprite rFoot = new Sprite(rFootIv, 7.5, 67.5, 0, 10);

        // build scene graph aka tree from them
        torso.addChild(head);
        torso.addChild(lUpperArm);
        torso.addChild(rUpperArm);
        lUpperArm.addChild(lLowerArm);
        rUpperArm.addChild(rLowerArm);
        lLowerArm.addChild(lHand);
        rLowerArm.addChild(rHand);
        torso.addChild(lUpperLeg);
        torso.addChild(rUpperLeg);
        lUpperLeg.addChild(lLowerLeg);
        rUpperLeg.addChild(rLowerLeg);
        lLowerLeg.addChild(lFoot);
        rLowerLeg.addChild(rFoot);

        // translate them to a starting position
        torso.translate(450, 200);
        try {
            head.rotate(0);
        } catch (NonInvertibleTransformException e) {}
//        head.translate(15, -80);
        try {
            lUpperArm.rotate(30);
        } catch (NonInvertibleTransformException e) {}
        //lUpperArm.translate(-15, -5);

        try {
            rUpperArm.rotate(-30);
        } catch (NonInvertibleTransformException e) {}
//        rUpperArm.translate(70, 10);

        try {
            lLowerArm.rotate(-30);
        } catch (NonInvertibleTransformException e) {}
        try {
            lHand.rotate(0);
        } catch (NonInvertibleTransformException e) {}
        //lLowerArm.translate(-30, 70);
        try {
            rLowerArm.rotate(30);
        } catch (NonInvertibleTransformException e) {}
        try {
            rHand.rotate(0);
        } catch (NonInvertibleTransformException e) {}
        try {
            lUpperLeg.rotate(0);
        } catch (NonInvertibleTransformException e) {}
        try {
            rUpperLeg.rotate(0);
        } catch (NonInvertibleTransformException e) {}
        try {
            lLowerLeg.rotate(0);
        } catch (NonInvertibleTransformException e) {}
        try {
            rLowerLeg.rotate(0);
        } catch (NonInvertibleTransformException e) {}
        try {
            lFoot.rotate(0);
        } catch (NonInvertibleTransformException e) {}
        try {
            rFoot.rotate(0);
        } catch (NonInvertibleTransformException e) {}
//        rLowerArm.translate(40, 60);
//        lHand.translate(-20, 55);
//        rHand.translate(15, 60);
//        lUpperLeg.translate(0, 150);
//        rUpperLeg.translate(60, 150);
//        lLowerLeg.translate(0, 95);
//        rLowerLeg.translate(0, 95);
//        lFoot.translate(-40, 60);
//        rFoot.translate(10, 60);

//        try {
//            lUpperArm.rotate(60);
//        } catch (NonInvertibleTransformException e) {}
        torso.translate(200, 100);

        return torso;
    }

    private MenuBar createFileMenu(Stage stage) {
        // Creating a menu
        Menu fileMenu = new Menu("File");

        // Creating menu Items and setting keyboard accelerators
        fileMenu.setMnemonicParsing(true);
        MenuItem item1 = new MenuItem("Reset");
        item1.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        item1.setOnAction(event -> { reset(); });
        MenuItem item2 = new MenuItem("Quit");
        item2.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        item2.setOnAction(event -> { exit(stage); });
        fileMenu.getItems().addAll(item1, item2);

        // Creating separator menu items and adding to menu
        SeparatorMenuItem sep = new SeparatorMenuItem();
        fileMenu.getItems().add(1, sep);

        // Creating a menu bar and adding menu to it
        MenuBar menuBar = new MenuBar(fileMenu);
        return menuBar;
    }

    private Canvas createCanvas() {
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT - 30);
        canvas.setLayoutX(0);
        canvas.setLayoutY(30);
        return canvas;
    }

    private void reset() {

    }

    private void exit(Stage stage) {
        stage.close();
        System.out.println("Quitting");
        System.exit(0);
    }
}
