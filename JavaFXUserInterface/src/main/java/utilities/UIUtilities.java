/*
  This class contains methods which are often reused withing the UI.

  @author
  @last_edit 02/7/2020
 */
package utilities;

import BackgroundTasks.ChangeSceneService;
import app.item.Asset;
import app.item.Model;
import controllers.AssetInfoController;
import controllers.AssetTypeInfoController;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParsePosition;

public class UIUtilities {

    private static final String FXML = ".fxml";
    private static final String ERROR_MESSAGE = "error-message";
    private static final String INPUT_ERROR = "input-error";
    Logger logger = LoggerFactory.getLogger(UIUtilities.class);

    /**
     * Given a tableView this function will set the width to fit the largest content
     *
     * @author Paul
     */
    public static void autoResizeColumns(TableView<?> table) {
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().forEach(column ->
        {
            Text t = new Text(column.getText());
            double max = t.getLayoutBounds().getWidth();
            for (int i = 0; i < table.getItems().size(); i++) {
                if (column.getCellData(i) != null) {
                    t = new Text(column.getCellData(i).toString());
                    double calcWidth = t.getLayoutBounds().getWidth();
                    if (calcWidth > max) {
                        max = calcWidth;
                    }
                }
            }
            column.setPrefWidth(max + 35.0d);
        });
    }

    /**
     * This function validates an input of a change on a text field to only allow the change if it fits the DecimalFormat
     *
     * @author Paul
     */
    public static TextFormatter.Change checkFormat(DecimalFormat format, TextFormatter.Change c) {
        if (c.getControlNewText().isEmpty())
            return c;
        ParsePosition parsePosition = new ParsePosition(0);
        if (format.parse(c.getControlNewText(), parsePosition) == null || parsePosition.getIndex() < c.getControlNewText().length())
            return null;
        return c;
    }

    /**
     * Creates an error message to be displayed next to the TextField or TextArea
     * and makes the border of the TextField red
     *
     * @param inputError         The AnchorPane where error messages will be displayed in
     * @param errorMessages      An array keeping track of error messages
     * @param field              The field being validated
     * @param msg                The error message
     * @param verticalPosition   The vertical position of the error message
     * @param horizontalPosition The horizontal position of the message
     * @param i                  The field number/position (starting from 0)
     * @author Najim
     */
    public static void createInputError(AnchorPane inputError, Text[] errorMessages, TextInputControl field, String msg, double verticalPosition, double horizontalPosition, int i) {
        if (errorMessages[i] == null) {
            errorMessages[i] = new Text(msg);
            errorMessages[i].setLayoutY(verticalPosition);
            errorMessages[i].setLayoutX(horizontalPosition);
            errorMessages[i].getStyleClass().add(ERROR_MESSAGE);

            inputError.getChildren().add(errorMessages[i]);
            field.getStyleClass().add(INPUT_ERROR);
        } else if (errorMessages[i].getText().equals("")) {
            errorMessages[i].getStyleClass().remove(ERROR_MESSAGE);
            field.getStyleClass().remove(INPUT_ERROR);

            errorMessages[i] = new Text(msg);
            errorMessages[i].setLayoutY(verticalPosition);
            errorMessages[i].setLayoutX(horizontalPosition);
            errorMessages[i].getStyleClass().add(ERROR_MESSAGE);

            inputError.getChildren().add(errorMessages[i]);
            field.getStyleClass().add(INPUT_ERROR);
        }
    }

    /**
     * Removes the error message from the AnchorPane and the styling added on the field being validated.
     *
     * @param inputError    The AnchorPane where error messages will be displayed in
     * @param errorMessages An array keeping track of error messages
     * @param validInput    An array keeping track of fields which are valid or invalid
     * @param field         The field being validated
     * @param i             The field number/position (starting from 0)
     * @author Najim
     */
    public static void removeInputError(AnchorPane inputError, Text[] errorMessages, boolean[] validInput, TextInputControl field, int i) {
        if (errorMessages[i] != null && validInput[i]) {
            field.getStyleClass().remove(INPUT_ERROR);
            inputError.getChildren().remove(errorMessages[i]);
            errorMessages[i] = null;
        }
    }

    /**
     * Compares two thresholds and determines if the previous threshold is larger than the next.
     *
     * @param previousThreshold The Threshold preceding
     * @param nextThreshold     The Threshold succeeding
     * @author Najim
     */
    public static boolean compareThresholds(TextField previousThreshold, TextField nextThreshold) {
        boolean valid = true;
        if (!previousThreshold.getText().isEmpty() && !nextThreshold.getText().isEmpty()) {
            double previousThresholdValue = Double.parseDouble(previousThreshold.getText());
            double nextThresholdValue = Double.parseDouble(nextThreshold.getText());
            if (previousThresholdValue <= nextThresholdValue) {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * Changes scenes once an element is clicked.
     *
     * @param mouseEvent
     * @param fxmlFileName
     * @param scene
     * @author Jeff
     */
    public void changeScene(MouseEvent mouseEvent, String fxmlFileName, Scene scene) {
        AnchorPane rootPane = (AnchorPane) scene.getRoot().lookup("root");
        AnchorPane anchorPane=new AnchorPane();
        ProgressIndicator pi=new ProgressIndicator();
        anchorPane.getChildren().add(pi);
        rootPane.getChildren().add(anchorPane);
        FadeTransition ft = fadeOut(rootPane);
        ChangeSceneService changeSceneService=new ChangeSceneService();
        changeSceneService.setFxmlFileName(fxmlFileName);
        pi.visibleProperty().bind(changeSceneService.runningProperty());
        changeSceneService.start();
//        try {
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource(fxmlFileName + FXML));
//            Parent parent = loader.load();
//            scene.setRoot(parent);
//        } catch (IOException e) {
//            logger.error("Exception in changeScene(): ", e);
//        }
        ft.play();
    }

    /**
     * Changes scenes once an element is clicked, and
     * sends an asset to the new scene's controller.
     *
     * @param mouseEvent
     * @param fxmlFileName
     * @author Jeff
     */
    public void changeScene(MouseEvent mouseEvent, TableRow<Asset> row, String fxmlFileName, Asset asset, Scene scene) {
        row.getScene().getWindow();
        if (!row.isEmpty()) {
            changeScene(mouseEvent, fxmlFileName, asset, scene);
        }
    }

    /**
     * Changes scenes once an element is clicked, and
     * sends an asset type to the new scene's controller.
     *
     * @param mouseEvent
     * @param fxmlFileName
     * @param row
     * @param assetType
     * @author Najim, Jeff
     */
    public void changeScene(MouseEvent mouseEvent, TableRow<AssetTypeList> row, String fxmlFileName, AssetTypeList assetType, Scene scene) {
        AnchorPane rootPane = (AnchorPane) scene.getRoot().lookup("root");
        FadeTransition ft = fadeOut(rootPane);
        row.getScene().getWindow();
        try {
            if (!row.isEmpty()) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(fxmlFileName + FXML));
                Parent parent = loader.load();
                scene.setRoot(parent);
                AssetTypeInfoController controller = loader.getController();
                controller.initData(assetType);
            }
        } catch (IOException e) {
            logger.error("Exception in changeScene 2: ", e);
        }
        ft.play();
    }

    /**
     * Changes scenes once an element is clicked, and
     * sends an asset to the new scene's controller.
     *
     * @param mouseEvent   the mouse click event
     * @param fxmlFileName the name of the fxml file we want to navigate to
     * @param asset        the asset we want to sent to the other page
     * @author Paul, Jeff
     */
    public void changeScene(MouseEvent mouseEvent, String fxmlFileName, Asset asset, Scene scene) {
        AnchorPane rootPane = (AnchorPane) scene.getRoot().lookup("root");
        Timeline rmseTimeline = new Timeline(new KeyFrame(Duration.millis(3000), e ->
        {
        }));
        rootPane.getChildren()
                .stream()
                .filter(ProgressIndicator.class::isInstance)
                .map(ProgressIndicator.class::cast)
                .findFirst().get().setVisible(false);
        FadeTransition ft = fadeOut(rootPane);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlFileName + FXML));
            Parent parent = loader.load();
            scene.setRoot(parent);
            AssetInfoController controller = loader.getController();
            controller.initData(asset);

        } catch (IOException e) {
            logger.error("Exception in changeScene 3: ", e);
        }
        ft.play();
    }

    /**
     * Stop the Timeline, and changes the scene.
     *
     * @param timeline
     * @param mouseEvent
     * @param s
     * @param scene
     * @author Jeff
     */
    public void changeScene(Timeline timeline, MouseEvent mouseEvent, String s, Scene scene) {
        timeline.stop();
        changeScene(mouseEvent, s, scene);
    }

    /**
     * Creates a fade out animation
     *
     * @param rootPane Root AnchorPane of the view/page
     * @return FadeTransition object
     * @author Najim
     */
    public FadeTransition fadeOut(AnchorPane rootPane) {
        FadeTransition ft = new FadeTransition();
        ft.setDuration(Duration.millis(700));
        ft.setNode(rootPane);
        ft.setFromValue(1);
        ft.setToValue(0);

        return ft;
    }

    /**
     * Creates a fade in animation
     *
     * @param rootPane Root AnchorPane of the view/page
     * @author Najim
     */
    public void fadeInTransition(AnchorPane rootPane) {
        FadeTransition ft = new FadeTransition();
//        ProgressIndicator pi=new ProgressIndicator();
//        rootPane.getChildren().add(pi);
//        pi.setVisible(true);
        ft.setDuration(Duration.millis(700));
        ft.setNode(rootPane);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }
}
