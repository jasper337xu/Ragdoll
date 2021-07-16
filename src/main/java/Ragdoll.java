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
import javafx.scene.paint.Color;
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
        Image torsoImg = new Image("torso.png", 80, 200, true, true);
        ImageView torsoIv = new ImageView(torsoImg);
        torsoIv.setFitWidth(100);
        torsoIv.setFitHeight(200);
        Sprite torso = new Sprite(torsoIv);

        Image headImg = new Image("head.png", 50, 80, true, true);
        ImageView headIv = new ImageView(headImg);
        headIv.setFitWidth(50);
        headIv.setFitHeight(80);
        Sprite head = new Sprite(headIv);

        Image lUpperArmImg = new Image("upper_arm.png", 30, 100, true, true);
        ImageView lUpperArmIv = new ImageView(lUpperArmImg);
        lUpperArmIv.setFitWidth(30);
        lUpperArmIv.setFitHeight(100);
        Sprite lUpperArm = new Sprite(lUpperArmIv);

        // build scene graph aka tree from them
        torso.addChild(head);
        torso.addChild(lUpperArm);

        // translate them to a starting position
        torso.translate(450, 200);
        head.translate(15, -80);
        try {
            lUpperArm.rotate(30);
        } catch (NonInvertibleTransformException e) {}
        lUpperArm.translate(-15, -5);

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
