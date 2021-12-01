package puzzles.tipover.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.tipover.model.TipOverModel;
import util.Coordinates;
import util.Grid;
import util.Observer;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The GUI to display the Tip Over game.
 * @author Eli Lurie
 * November 2021
 */
public class TipOverGUI extends Application implements Observer< TipOverModel, Object > {
    private TipOverModel model;
    private Label[][] labels;
    private Label messages;
    private GridPane theGridPane;
    private Stage thisStage;

    /**
     * Creates a new model and adds itself as an observer.
     */
    @Override
    public void init() {
        List<String> parameters = getParameters().getRaw();
        this.model = new TipOverModel(parameters.get(0));
        this.model.addObserver(this);
    }

    /**
     * Starts a new tip over game.
     * @param stage stage to display
     */
    @Override
    public void start( Stage stage ) {
        thisStage = new Stage();
        BorderPane borderPane = new BorderPane();

        theGridPane = createBoard();
        borderPane.setCenter(theGridPane);

        messages = new Label();
        messages.setText("");
        borderPane.setTop(messages);

        VBox vbox = createButtons();
        borderPane.setRight(vbox);

        Scene scene = new Scene(borderPane);
        thisStage.setScene(scene);
        thisStage.setTitle("Tip Over");
        thisStage.show();
    }

    /**
     * Creates a new grid of labels.
     * @return grid
     */
    public GridPane createBoard(){
        GridPane gridPane = new GridPane();
        labels = new Label[model.getBoard().getNCols()][model.getBoard().getNRows()];
        for(int i=0; i<labels.length; i++){
            for(int j=0; j<labels[0].length; j++){
                labels[i][j] = new Label();
            }
        }
        setLabels();
        for(int i=0; i<labels.length; i++){
            for(int j=0; j<labels[i].length; j++){
                gridPane.add(labels[i][j], i, j);
            }
        }
        return gridPane;
    }

    /**
     * Uses the model's board to set each label to its correct number.
     */
    public void setLabels(){
        Grid<Integer> grid = model.getBoard();
        for(int i=0; i<grid.getNCols(); i++){
            for(int j=0; j< grid.getNRows(); j++){
                String num = Integer.toString(grid.get(j, i));
                createLabel(labels[i][j], num);
            }
        }
        Coordinates cords = model.getCords();
        labels[cords.col()][cords.row()].setBackground(new Background(new BackgroundFill(Color.RED,
                CornerRadii.EMPTY, Insets.EMPTY)));
        Coordinates goal = model.getGoal();
        labels[goal.col()][goal.row()].setPadding(new Insets(7));
        labels[goal.col()][goal.row()].setBorder(new Border(new BorderStroke(Color.RED,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4))));

    }

    /**
     * Creates a new label.
     * @param label label to set
     * @param text to display
     */
    public void createLabel(Label label, String text){
        if(text.equals("0")){
            label.setText("  ");
        }
        else{
            label.setText(text);
        }
        label.setFont(Font.font("Courier", 20));
        label.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,
                Insets.EMPTY)));
        label.setPadding(new Insets(10));
        label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

    /**
     * Creats a VBox containing the buttons to move, load, reload, and get a hint.
     * @return VBox of buttons
     */
    public VBox createButtons(){
        VBox vBox = new VBox();
        GridPane gridPane = new GridPane();
        Button north = new Button("↑");
        north.setOnAction(event -> model.move("NORTH"));
        Button south = new Button("↓");
        south.setOnAction(event -> model.move("SOUTH"));
        Button west = new Button("←");
        west.setOnAction(event -> model.move("WEST"));
        Button east = new Button("→");
        east.setOnAction(event -> model.move("EAST"));
        gridPane.add(north, 1, 0);
        gridPane.add(west, 0, 1);
        gridPane.add(east, 2, 1);
        gridPane.add(south, 1, 2);
        vBox.getChildren().add(gridPane);

        Button hint = new Button("Hint");
        hint.setOnAction(event -> model.getHint());
        Button load = new Button("Load");
        load.setOnAction(event -> {
            chooseFile();
            thisStage.hide();
            thisStage = new Stage();
            start(thisStage);
        });
        Button reload = new Button("Reload");
        reload.setOnAction(event -> {
            model.reloadFile();
            thisStage.hide();
            thisStage = new Stage();
            start(thisStage);
        });
        vBox.getChildren().addAll(hint, load, reload);
        return vBox;
    }

    /**
     * Uses a fileChooser to let user load a new file.
     */
    public void chooseFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose New Puzzle");
        try {
            File file = fileChooser.showOpenDialog(new Stage());
            String filename = file.getAbsolutePath();
            model.loadFile(filename);
        }
        catch (NullPointerException | NoSuchElementException ex){
            messages.setText("File not found");
        }
    }

    /**
     * Updates the current GUI.
     * @param tipOverModel the model
     * @param announce message to display
     */
    @Override
    public void update(TipOverModel tipOverModel, Object announce) {
        if(announce != null){
            messages.setText(announce.toString());
        }
        else if(model.isSolution()){
            messages.setText("You Win!");
        }
        else{
            messages.setText("Make a move, get a hint, or load a file");
        }
        if(model.getBoard().getNCols() == labels.length && model.getBoard().getNRows() == labels[0].length) {
            setLabels();
        }
    }

    /**
     * Used to initiate the GUI
     * @param args filename
     */
    public static void main( String[] args ) {
        if(args.length != 1){
            System.out.println("Invalid arguments");
        }
        else {
            Application.launch(args);
        }
    }
}
