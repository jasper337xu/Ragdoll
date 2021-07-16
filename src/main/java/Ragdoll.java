import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Ragdoll extends Application {
    final float SCREEN_WIDTH = 1024;
    final float SCREEN_HEIGHT = 768;

    @Override
    public void start(Stage stage) {
        MenuBar menuBar = createFileMenu(stage);

        Group root = new Group(menuBar);
        stage.setResizable(false);
        stage.setWidth(SCREEN_WIDTH);
        stage.setHeight(SCREEN_HEIGHT);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private MenuBar createFileMenu(Stage stage) {
        //Creating a menu
        Menu fileMenu = new Menu("File");

        //Creating menu Items and setting keyboard accelerators
        fileMenu.setMnemonicParsing(true);
        MenuItem item1 = new MenuItem("Reset");
        item1.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        item1.setOnAction(event -> { reset(); });
        MenuItem item2 = new MenuItem("Quit");
        item2.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        item2.setOnAction(event -> { exit(stage); });
        fileMenu.getItems().addAll(item1, item2);

        //Creating separator menu items and adding to menu
        SeparatorMenuItem sep = new SeparatorMenuItem();
        fileMenu.getItems().add(1, sep);

        //Creating a menu bar and adding menu to it
        MenuBar menuBar = new MenuBar(fileMenu);
        return menuBar;
    }

    private void reset() {

    }

    private void exit(Stage stage) {
        stage.close();
        System.out.println("Quitting");
        System.exit(0);
    }
}
