/*
  This class contains methods which are often reused withing the UI.

  @author
  @last_edit 02/7/2020
 */
package utilities;

import app.item.Asset;
import controllers.AssetInfoController;
import controllers.AssetTypeInfoController;
import controllers.Controller;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static utilities.TextConstants.FXML;

public class UIUtilities {

    Logger logger = LoggerFactory.getLogger(UIUtilities.class);

    /**
     * Given a tableView this function will set the width to fit the largest content
     *
     * @param table is the table view object that will be resized
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
     * Changes scenes once an element is clicked.
     *
     * @param fxmlFileName is the name of the resource file for the scene
     * @param scene        is the screen that will app will be changed to
     * @author Jeff
     */
    public void changeScene(String fxmlFileName, Scene scene) {
        AnchorPane rootPane = (AnchorPane) scene.getRoot().lookup("root");
        FadeTransition ft = fadeOut(rootPane);
        try {
            ((Controller) ((FXMLLoader) scene.getUserData()).getController()).getTimelines().forEach(Timeline::stop);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlFileName + FXML));
            Parent parent = loader.load();
            scene.setRoot(parent);
            scene.setUserData(loader);
        } catch (IOException e) {
            logger.error("Exception in changeScene(): ", e);
        }
        ft.play();
    }

    /**
     * Changes scenes once an element is clicked, and
     * sends an asset to the new scene's controller.
     *
     * @param fxmlFileName the name of the fxml file that will be loaded for the scene
     * @author Jeff
     */
    public void changeScene(TableRow<Asset> row, String fxmlFileName, Asset asset, Scene scene) {
        row.getScene().getWindow();
        if (!row.isEmpty()) {
            changeScene(fxmlFileName, asset, scene);
        }
    }

    /**
     * Changes scenes once an element is clicked, and
     * sends an asset type to the new scene's controller.
     *
     * @param fxmlFileName the name of the fxml file that will be loaded for the scene
     * @param row          is the table row of an asset type
     * @param assetType    is the asset type being observed
     * @author Najim, Jeff
     */
    public void changeScene(TableRow<AssetTypeList> row, String fxmlFileName, AssetTypeList assetType, Scene scene) {
        AnchorPane rootPane = (AnchorPane) scene.getRoot().lookup("root");
        FadeTransition ft = fadeOut(rootPane);
        row.getScene().getWindow();
        try {
            if (!row.isEmpty()) {
                ((Controller) ((FXMLLoader) scene.getUserData()).getController()).getTimelines().forEach(Timeline::stop);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(fxmlFileName + FXML));
                Parent parent = loader.load();
                scene.setRoot(parent);
                scene.setUserData(loader);
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
     * @param fxmlFileName the name of the fxml file that will be loaded for the scene
     * @param asset        the asset we want to sent to the other page
     * @author Paul, Jeff
     */
    public void changeScene(String fxmlFileName, Asset asset, Scene scene) {
        AnchorPane rootPane = (AnchorPane) scene.getRoot().lookup("root");
        FadeTransition ft = fadeOut(rootPane);
        try {
            ((Controller) ((FXMLLoader) scene.getUserData()).getController()).getTimelines().forEach(Timeline::stop);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlFileName + FXML));
            Parent parent = loader.load();
            scene.setRoot(parent);
            scene.setUserData(loader);
            AssetInfoController controller = loader.getController();
            controller.initData(asset);

        } catch (IOException e) {
            logger.error("Exception in changeScene 3: ", e);
        }
        ft.play();
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
        ft.setDuration(Duration.millis(700));
        ft.setNode(rootPane);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }
}
