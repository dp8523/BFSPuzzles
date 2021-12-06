package puzzles.lunarlanding.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.lunarlanding.model.LunarLandingModel;
import util.Coordinates;
import util.Observer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * The GUI view of the Lunar Landing puzzle.
 * @author Dara Prak
 * November 2021
 */
public class LunarLandingGUI extends Application
        implements Observer< LunarLandingModel, Object > {
    private LunarLandingModel model;

    private Image E = new Image(LunarLandingGUI.class.getResourceAsStream("resources/explorer.png"));
    private Image lander = new Image(LunarLandingGUI.class.getResourceAsStream("resources/lander.png"));
    private Image B = new Image(LunarLandingGUI.class.getResourceAsStream("resources/robot-blue.png"));
    private Image G = new Image(LunarLandingGUI.class.getResourceAsStream("resources/robot-green.png"));
    private Image O = new Image(LunarLandingGUI.class.getResourceAsStream("resources/robot-orange.png"));
    private Image P = new Image(LunarLandingGUI.class.getResourceAsStream("resources/robot-purple.png"));
    private Image Y = new Image(LunarLandingGUI.class.getResourceAsStream("resources/robot-yellow.png"));
    private Image other = new Image(LunarLandingGUI.class.getResourceAsStream("resources/robot-white.png"));

    private Text message;
    private Stage currentStage;

    /**
     * Creates a LunarLandingModel using a file and sets itself as an observer.
     */
    @Override
    public void init()
    {
        List<String> parameters = getParameters().getRaw();
        this.model = new LunarLandingModel(parameters.get(0));
        this.model.addObserver(this);
    }

    /**
     * Creates grid component of the GUI that represents the game board. It appears to be blank except for the
     * FigureButtons and lander, if there is no figure on top of it.
     * @return a GridPane of the game board.
     */
    private GridPane makeGameGrid()
    {
        GridPane gridPane = new GridPane();

        for (int col=0; col<this.model.getCurrentConfig().getCurrentGrid().getNCols(); col++) {
            for (int row=0; row<this.model.getCurrentConfig().getCurrentGrid().getNRows(); row++) {
                Rectangle r = new Rectangle();
                r.setWidth(this.E.getWidth());
                r.setHeight(this.E.getHeight());
                r.setFill(Color.TRANSPARENT);
                gridPane.add(r, col, row);
            }
        }

        ImageView landerSpot = new ImageView(lander);
        gridPane.add(landerSpot, this.model.getCurrentConfig().getLanderCords().col(),
                this.model.getCurrentConfig().getLanderCords().row());

        Map<Coordinates, String> figures = new HashMap<>();
        figures.putAll(this.model.getCurrentConfig().getFigureCords());
        for(Map.Entry<Coordinates, String> entry: figures.entrySet())
        {
            FigureButton figureButton = new FigureButton(entry.getValue());
            figureButton.setOnAction(event -> this.model.choose(new Coordinates(entry.getKey().row(), entry.getKey().col())));
            gridPane.add(figureButton, entry.getKey().col(), entry.getKey().row());
        }
        return gridPane;
    }

    /**
     * Creates a window with the entire GUI allowing a view and controls for the model
     * @param stage not used, required as it implements Application method
     */
    @Override
    public void start(Stage stage)
    {
        this.currentStage = new Stage();

        BorderPane borderPane = new BorderPane();

        this.message = new Text("File loaded");
        borderPane.setTop(this.message);

        GridPane gameGrid = makeGameGrid();
        borderPane.setCenter(gameGrid);

        VBox controls = new VBox();
        GridPane directions = new GridPane();
        Button north = new Button("↑");
        north.setOnAction(event -> this.model.go("north"));
        Button south = new Button("↓");
        south.setOnAction(event -> this.model.go("south"));
        Button east = new Button("→");
        east.setOnAction(event -> this.model.go("east"));
        Button west = new Button("←");
        west.setOnAction(event -> this.model.go("west"));
        directions.add(north, 1, 0);
        directions.add(south, 1, 2);
        directions.add(west, 0, 1);
        directions.add(east, 2, 1);
        Button load = new Button("LOAD");
        load.setOnAction(event -> {
            fileSelect();
                });
        Button reload = new Button("RELOAD");
        reload.setOnAction(event -> {
            this.model.reload();
        });
        Button hint = new Button("HINT");
        hint.setOnAction(event -> this.model.hint());
        controls.getChildren().addAll(directions, load, reload, hint);
        borderPane.setRight(controls);

        Scene scene = new Scene(borderPane);
        this.currentStage.setScene(scene);
        this.currentStage.setTitle("Lunar Landing");
        this.currentStage.setResizable(false);
        this.currentStage.show();
    }

    /**
     * Uses a fileChooser so the user can open the starting file
     */
    private void fileSelect(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select another starting configuration");
        try {
            File file = fileChooser.showOpenDialog(new Stage());
            String filename = file.getAbsolutePath();
            model.load(filename);
        }
        catch (NullPointerException | NoSuchElementException e){
            this.message.setText("File not found");
        }
    }

    /**
     * A private inner class that extends the behavior of a JavaFX
     * button to have an image.
     */
    private class FigureButton extends Button {
        /**
         * Create the button.
         */
        public FigureButton(String figureString)
        {
            switch(figureString)
            {
                case "E":
                    this.setGraphic(new ImageView(E));
                    break;
                case "B":
                    this.setGraphic(new ImageView(B));
                    break;
                case "G":
                    this.setGraphic(new ImageView(G));
                    break;
                case "O":
                    this.setGraphic(new ImageView(O));
                    break;
                case "P":
                    this.setGraphic(new ImageView(P));
                    break;
                case "Y":
                    this.setGraphic(new ImageView(Y));
                    break;
                default:
                    this.setGraphic(new ImageView(other));
            }
        }
    }

    /**
     * Updates the GUI with the model's new information and message. It replaces the stage with the updated view.
     * @param lunarLandingModel this LunarLandingModel
     * @param announcement the message being sent for the PTUI to display
     */
    @Override
    public void update(LunarLandingModel lunarLandingModel, Object announcement)
    {
        this.currentStage.hide();
        start(this.currentStage);
        this.message.setText(announcement.toString());
    }

    /**
     * The main method to start the GUI by loading a file.
     * @param args the file name
     */
    public static void main( String[] args ) {
        if(args.length != 1){
            System.out.println("Usage: java LunarLandingGUI file debug");
        }else{
            Application.launch(args);
        }
    }
}
