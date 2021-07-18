import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.Stage;

public class Ragdoll extends Application {
    final float SCREEN_WIDTH = 1024;
    final float SCREEN_HEIGHT = 768;
    double previous_x, previous_y;
    double previous_d = 0;
    Sprite selectedSprite;

    @Override
    public void start(Stage stage) {
        MenuBar menuBar = createFileMenu(stage);
        // setup a canvas to use as a drawing surface
        Canvas canvas = createCanvas();

        Sprite torso = createSprites();

        // add listeners, click selects the shape under the cursor
        canvas.setOnMousePressed(mouseEvent -> {
            Sprite hit = torso.getSpriteHit(mouseEvent.getX(), mouseEvent.getY());
            if (hit != null) {
                selectedSprite = hit;
                previous_x = mouseEvent.getX();
                previous_y = mouseEvent.getY();
            }
        });

        // un-selects any selected shape
        canvas.setOnMouseReleased( mouseEvent -> {
            selectedSprite = null;
            //System.out.println("Unselected");
        });

        canvas.setOnMouseDragged(mouseEvent -> {
            if (selectedSprite != null) {
                if (selectedSprite.id.equals("torso")) {
                    double dx = mouseEvent.getX() - previous_x;
                    double dy = mouseEvent.getY() - previous_y;
                    selectedSprite.translate(dx, dy);
                }
                else if (selectedSprite.id.equals("head")) {
                    double distance = Math.sqrt(Math.pow(mouseEvent.getX() - previous_x, 2) + Math.pow(mouseEvent.getY() - previous_y, 2));
                    double theta = Math.atan(distance);
                    if (mouseEvent.getX() - previous_x < 0) {
                        theta = -theta;
                    }
                    selectedSprite.totalRotation += theta;
                    if (selectedSprite.totalRotation >= 50) {
                        selectedSprite.totalRotation = 50;
                    }
                    if (selectedSprite.totalRotation <= -50) {
                        selectedSprite.totalRotation = -50;
                    }
                    if (selectedSprite.totalRotation > -50 && selectedSprite.totalRotation < 50) {
                        try {
                            selectedSprite.rotate(theta);
                            selectedSprite.totalRotation += theta;
                        } catch (NonInvertibleTransformException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (selectedSprite.id.equals("lUpperArm") || selectedSprite.id.equals("rUpperArm")) {
                    double distance = Math.sqrt(Math.pow(mouseEvent.getX() - previous_x, 2) + Math.pow(mouseEvent.getY() - previous_y, 2));
                    double theta = Math.atan(distance);
                    try {
                        selectedSprite.rotate(theta);
                    } catch (NonInvertibleTransformException e) {
                        e.printStackTrace();
                    }
                }
                else if (selectedSprite.id.equals("lLowerArm") || selectedSprite.id.equals("rLowerArm")) {
                    double distance = Math.sqrt(Math.pow(mouseEvent.getX() - previous_x, 2) + Math.pow(mouseEvent.getY() - previous_y, 2));
                    double theta = Math.atan(distance);
                    if (mouseEvent.getX() - previous_x > 0) {
                        theta = -theta;
                    }
                    selectedSprite.totalRotation += theta;
                    if (selectedSprite.totalRotation >= 270) {
                        selectedSprite.totalRotation = 270;
                    }
                    if (selectedSprite.totalRotation <= -270) {
                        selectedSprite.totalRotation = -270;
                    }
                    if (selectedSprite.totalRotation > -270 && selectedSprite.totalRotation < 270) {
                        try {
                            selectedSprite.rotate(theta);
                            selectedSprite.totalRotation += theta;
                        } catch (NonInvertibleTransformException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (selectedSprite.id.equals("lHand") || selectedSprite.id.equals("rHand")) {
                    double distance = Math.sqrt(Math.pow(mouseEvent.getX() - previous_x, 2) + Math.pow(mouseEvent.getY() - previous_y, 2));
                    double theta = Math.atan(distance);
                    if (mouseEvent.getX() - previous_x > 0) {
                        theta = -theta;
                    }
                    selectedSprite.totalRotation += theta;
                    if (selectedSprite.totalRotation >= 70) {
                        selectedSprite.totalRotation = 70;
                    }
                    if (selectedSprite.totalRotation <= -70) {
                        selectedSprite.totalRotation = -70;
                    }
                    if (selectedSprite.totalRotation > -70 && selectedSprite.totalRotation < 70) {
                        try {
                            selectedSprite.rotate(theta);
                            selectedSprite.totalRotation += theta;
                        } catch (NonInvertibleTransformException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (selectedSprite.id.equals("lUpperLeg") || selectedSprite.id.equals("rUpperLeg")) {
                    double distance = Math.sqrt(Math.pow(mouseEvent.getX() - previous_x, 2) + Math.pow(mouseEvent.getY() - previous_y, 2));
                    double theta = Math.atan(distance);
                    if (mouseEvent.getX() - previous_x > 0) {
                        theta = -theta;
                    }
                    selectedSprite.totalRotation += theta;
                    if (selectedSprite.totalRotation >= 180) {
                        selectedSprite.totalRotation = 180;
                    }
                    if (selectedSprite.totalRotation <= -180) {
                        selectedSprite.totalRotation = -180;
                    }
                    if (selectedSprite.totalRotation > -180 && selectedSprite.totalRotation < 180) {
                        try {
                            selectedSprite.rotate(theta);
                            selectedSprite.totalRotation += theta;
                        } catch (NonInvertibleTransformException e) {
                            e.printStackTrace();
                        }
                    }
                    // handle scaling
                    double d = Math.sqrt(Math.pow(mouseEvent.getX() - selectedSprite.selfPivot.getX(), 2)
                            + Math.pow(mouseEvent.getY() - selectedSprite.selfPivot.getY(), 2));
                    if (previous_d == 0) { previous_d = d; }
                    if (d >= previous_d) { // scale up
                        selectedSprite.totalScale *= 1.008;
                        if (selectedSprite.totalScale >= 2) {
                            selectedSprite.totalScale = 2;
                        }
                        if (selectedSprite.totalScale < 2) {
                            try {
                                selectedSprite.scale(1.008, 1.008);
                                selectedSprite.totalScale *= 1.008;
                            } catch (NonInvertibleTransformException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else { // scale down
                        selectedSprite.totalScale *= 0.99;
                        if (selectedSprite.totalScale <= 0.5) {
                            selectedSprite.totalScale = 0.5;
                        }
                        if (selectedSprite.totalScale > 0.5) {
                            try {
                                selectedSprite.scale(0.99, 0.99);
                                selectedSprite.totalScale *= 0.99;
                            } catch (NonInvertibleTransformException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    previous_d = d;
                }
                else if (selectedSprite.id.equals("lLowerLeg") || selectedSprite.id.equals("rLowerLeg")) {
                    double distance = Math.sqrt(Math.pow(mouseEvent.getX() - previous_x, 2) + Math.pow(mouseEvent.getY() - previous_y, 2));
                    double theta = Math.atan(distance);
                    if (mouseEvent.getX() - previous_x > 0) {
                        theta = -theta;
                    }
                    selectedSprite.totalRotation += theta;
                    if (selectedSprite.totalRotation >= 180) {
                        selectedSprite.totalRotation = 180;
                    }
                    if (selectedSprite.totalRotation <= -180) {
                        selectedSprite.totalRotation = -180;
                    }
                    if (selectedSprite.totalRotation > -180 && selectedSprite.totalRotation < 180) {
                        try {
                            selectedSprite.rotate(theta);
                            selectedSprite.totalRotation += theta;
                        } catch (NonInvertibleTransformException e) {
                            e.printStackTrace();
                        }
                    }
                    // handle scaling
                    double d = Math.sqrt(Math.pow(mouseEvent.getX() - selectedSprite.selfPivot.getX(), 2)
                            + Math.pow(mouseEvent.getY() - selectedSprite.selfPivot.getY(), 2));
                    if (previous_d == 0) { previous_d = d; }
                    if (d >= previous_d) { // scale up
                        selectedSprite.totalScale *= 1.008;
                        if (selectedSprite.totalScale >= 2) {
                            selectedSprite.totalScale = 2;
                        }
                        if (selectedSprite.totalScale < 2) {
                            try {
                                selectedSprite.scale(1.008, 1.008);
                                selectedSprite.totalScale *= 1.008;
                            } catch (NonInvertibleTransformException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else { // scale down
                        selectedSprite.totalScale *= 0.99;
                        if (selectedSprite.totalScale <= 0.5) {
                            selectedSprite.totalScale = 0.5;
                        }
                        if (selectedSprite.totalScale > 0.5) {
                            try {
                                selectedSprite.scale(0.99, 0.99);
                                selectedSprite.totalScale *= 0.99;
                            } catch (NonInvertibleTransformException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    previous_d = d;
                }
                else if (selectedSprite.id.equals("lFoot") || selectedSprite.id.equals("rFoot")) {
                    double distance = Math.sqrt(Math.pow(mouseEvent.getX() - previous_x, 2) + Math.pow(mouseEvent.getY() - previous_y, 2));
                    double theta = Math.atan(distance);
                    if (mouseEvent.getX() - previous_x > 0) {
                        theta = -theta;
                    }
                    selectedSprite.totalRotation += theta;
                    if (selectedSprite.totalRotation >= 70) {
                        selectedSprite.totalRotation = 70;
                    }
                    if (selectedSprite.totalRotation <= -70) {
                        selectedSprite.totalRotation = -70;
                    }
                    if (selectedSprite.totalRotation > -70 && selectedSprite.totalRotation < 70) {
                        try {
                            selectedSprite.rotate(theta);
                            selectedSprite.totalRotation += theta;
                        } catch (NonInvertibleTransformException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // draw tree in new position
                draw(canvas, torso);

                // save coordinates for next event
                previous_x = mouseEvent.getX();
                previous_y = mouseEvent.getY();
            }
        });

        draw(canvas, torso);

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
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        root.draw(gc);
    }

    private Sprite createSprites() {
        Image torsoImg = new Image("torso.png", 80, 150, true, true);
        ImageView torsoIv = new ImageView(torsoImg);
        torsoIv.setFitWidth(80);
        torsoIv.setFitHeight(150);
        Sprite torso = new Sprite(torsoIv, "torso");

        Image headImg = new Image("head.png", 50, 80, true, true);
        ImageView headIv = new ImageView(headImg);
        headIv.setFitWidth(50);
        headIv.setFitHeight(80);
        Sprite head = new Sprite(headIv, 40, 0, 25, 80, "head");

        Image lUpperArmImg = new Image("upper_arm.png", 30, 100, true, true);
        ImageView lUpperArmIv = new ImageView(lUpperArmImg);
        lUpperArmIv.setFitWidth(30);
        lUpperArmIv.setFitHeight(100);
        Sprite lUpperArm = new Sprite(lUpperArmIv, 0, 0, 15, 0, "lUpperArm");

        Image rUpperArmImg = new Image("upper_arm.png", 30, 100, true, true);
        ImageView rUpperArmIv = new ImageView(rUpperArmImg);
        rUpperArmIv.setFitWidth(30);
        rUpperArmIv.setFitHeight(100);
        Sprite rUpperArm = new Sprite(rUpperArmIv, 80, 0, 15, 0, "rUpperArm");

        Image lLowerArmImg = new Image("lower_arm.png", 20, 80, true, true);
        ImageView lLowerArmIv = new ImageView(lLowerArmImg);
        lLowerArmIv.setFitWidth(20);
        lLowerArmIv.setFitHeight(80);
        Sprite lLowerArm = new Sprite(lLowerArmIv, 15, 75, 10, 0, "lLowerArm");

        Image rLowerArmImg = new Image("lower_arm.png", 20, 80, true, true);
        ImageView rLowerArmIv = new ImageView(rLowerArmImg);
        rLowerArmIv.setFitWidth(20);
        rLowerArmIv.setFitHeight(80);
        Sprite rLowerArm = new Sprite(rLowerArmIv, 15, 75, 10, 0, "rLowerArm");

        Image lHandImg = new Image("hand.png", 30, 30, true, true);
        ImageView lHandIv = new ImageView(lHandImg);
        lHandIv.setFitWidth(30);
        lHandIv.setFitHeight(30);
        Sprite lHand = new Sprite(lHandIv, 10, 60, 15, 0, "lHand");

        Image rHandImg = new Image("hand.png", 30, 30, true, true);
        ImageView rHandIv = new ImageView(rHandImg);
        rHandIv.setFitWidth(30);
        rHandIv.setFitHeight(30);
        Sprite rHand = new Sprite(rHandIv, 10, 60, 15, 0, "rHand");

        Image lUpperLegImg = new Image("leg.png", 20, 120, true, true);
        ImageView lUpperLegIv = new ImageView(lUpperLegImg);
        lUpperLegIv.setFitWidth(20);
        lUpperLegIv.setFitHeight(120);
        Sprite lUpperLeg = new Sprite(lUpperLegIv, 10, 150, 10, 0, "lUpperLeg");

        Image rUpperLegImg = new Image("leg.png", 20, 120, true, true);
        ImageView rUpperLegIv = new ImageView(rUpperLegImg);
        rUpperLegIv.setFitWidth(20);
        rUpperLegIv.setFitHeight(120);
        Sprite rUpperLeg = new Sprite(rUpperLegIv, 70, 150, 10, 0, "rUpperLeg");

        Image lLowerLegImg = new Image("leg.png", 15, 90, true, true);
        ImageView lLowerLegIv = new ImageView(lLowerLegImg);
        lLowerLegIv.setFitWidth(15);
        lLowerLegIv.setFitHeight(90);
        Sprite lLowerLeg = new Sprite(lLowerLegIv, 10, 90, 7.5, 0, "lLowerLeg");

        Image rLowerLegImg = new Image("leg.png", 15, 90, true, true);
        ImageView rLowerLegIv = new ImageView(rLowerLegImg);
        rLowerLegIv.setFitWidth(15);
        rLowerLegIv.setFitHeight(90);
        Sprite rLowerLeg = new Sprite(rLowerLegIv, 10, 90, 7.5, 0, "rLowerLeg");

        Image lFootImg = new Image("foot.png", 50, 20, true, true);
        ImageView lFootIv = new ImageView(lFootImg);
        lFootIv.setFitWidth(50);
        lFootIv.setFitHeight(20);
        Sprite lFoot = new Sprite(lFootIv, 7.5, 67.5, 50, 10, "lFoot");

        Image rFootImg = new Image("foot.png", 50, 20, true, true);
        ImageView rFootIv = new ImageView(rFootImg);
        rFootIv.setFitWidth(50);
        rFootIv.setFitHeight(20);
        Sprite rFoot = new Sprite(rFootIv, 7.5, 67.5, 0, 10, "rFoot");

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
        try {
            lUpperArm.rotate(30);
        } catch (NonInvertibleTransformException e) {}
        try {
            rUpperArm.rotate(-30);
        } catch (NonInvertibleTransformException e) {}
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

        return torso;
    }

    private MenuBar createFileMenu(Stage stage) {
        // Creating a menu
        Menu fileMenu = new Menu("File");

        // Creating menu Items and setting keyboard accelerators
        fileMenu.setMnemonicParsing(true);
        MenuItem item1 = new MenuItem("Reset");
        item1.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        item1.setOnAction(event -> { reset(stage); });
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

    private void reset(Stage stage) {
        start(stage);
    }

    private void exit(Stage stage) {
        stage.close();
        System.out.println("Quitting");
        System.exit(0);
    }
}
