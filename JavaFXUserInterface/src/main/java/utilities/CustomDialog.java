package utilities;


import app.item.Asset;
import external.AssetDAOImpl;
import external.AssetTypeDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Comparator;

import static utilities.TextConstants.*;

public class CustomDialog extends Stage {

    private static final String SAVE_LABEL = "Save";
    private static final String TIME_CYCLE_LABEL = "Time cycle:";
    private static final UIUtilities uiUtilities = new UIUtilities();
    private final Button okBtn;
    private final Pane rootPane;
    private final Button cancelBtn;

    public CustomDialog(String header, String content) {
        rootPane = new Pane();

        initStyle(StageStyle.TRANSPARENT);
        initModality(Modality.APPLICATION_MODAL);

        Rectangle bg = new Rectangle(500, 200, Color.WHITESMOKE);
        bg.setStroke(Color.BLACK);
        bg.setStrokeWidth(1.5);

        Text headerText = new Text(header);
        headerText.setFont(Font.font(20));

        Text contentText = new Text(content);
        contentText.setFont(Font.font(16));

        VBox box = new VBox(10, headerText, contentText);
        box.setPadding(new Insets(15));

        okBtn = new Button("Ok");
        okBtn.setTranslateX(bg.getWidth() - 120);
        okBtn.setTranslateY(bg.getHeight() - 50);

        cancelBtn = new Button("Cancel");
        cancelBtn.setTranslateX(bg.getWidth() - 70);
        cancelBtn.setTranslateY(bg.getHeight() - 50);
        cancelBtn.setOnAction(e -> closeDialog());

        rootPane.getChildren().addAll(bg, box, okBtn, cancelBtn);
        setScene(new Scene(rootPane, null));
    }

    public static void deleteAssetTypeConfirmationDialogShowAndWait(String assetTypeID, Scene scene) {
        CustomDialog dialog = new CustomDialog(DELETE_ASSET_TYPE_DIALOG_HEADER, DELETE_ASSET_TYPE_DIALOG_CONTENT);
        AssetTypeDAOImpl assetTypeDAO = new AssetTypeDAOImpl();

        dialog.getOkButton().setOnAction(e -> {
            assetTypeDAO.deleteAssetTypeByID(assetTypeID);
            uiUtilities.changeScene(ASSET_TYPE_LIST_SCENE, scene);
            dialog.closeDialog();
        });

        dialog.openDialog();
    }

    public static void nullModelAlertDialogShowAndWait() {
        CustomDialog dialog = new CustomDialog(NO_MODEL_ALERT_DIALOG_HEADER, NO_MODEL_ALERT_DIALOG_CONTENT);

        dialog.getRootPane().getChildren().remove(dialog.getCancelBtn());
        dialog.getOkButton().setOnAction(e -> {
            uiUtilities.changeScene(ASSET_TYPE_INFO_SCENE, dialog.getScene());
            dialog.closeDialog();
        });

        dialog.openDialog();
    }

    public static void deleteAssetConfirmationDialogShowAndWait(int assetID, Scene scene) {
        CustomDialog dialog = new CustomDialog(DELETE_ASSET_DIALOG_HEADER, DELETE_ASSET_DIALOG_CONTENT);
        AssetDAOImpl assetDAOImpl = new AssetDAOImpl();

        dialog.getOkButton().setOnAction(e -> {
            assetDAOImpl.deleteAssetByID(assetID);
            uiUtilities.changeScene(ASSETS_SCENE, scene);
            dialog.closeDialog();
        });

        dialog.openDialog();
    }

    public static void saveNewAssetInformationDialogShowAndWait() {
        CustomDialog dialog = new CustomDialog(NEW_ASSET_SAVED_DIALOG_HEADER, NEW_ASSET_SAVED_DIALOG_CONTENT);

        dialog.getRootPane().getChildren().remove(dialog.getCancelBtn());
        dialog.getOkButton().setOnAction(e -> {
            uiUtilities.changeScene(ASSETS_SCENE, dialog.getScene());
            dialog.closeDialog();
        });

        dialog.openDialog();
    }

    /**
     * This static functions creates and shows a custom dialog meant for archiving assets. It gets shown whenever
     * the archive button is pressed. The user will be able to determine the last RUL time cycle for the asset
     * specified. Saving the selection will delete all time cycles after the selected one and will archive the
     * asset.
     *
     * @param asset is the asset to be archived
     * @param scene is a button from the parent scene of the custom dialog window
     * @author Jeremie
     */
    public static void archiveAssetConfirmationDialogShowAndWait(Asset asset, Scene scene) {
        CustomDialog dialog = new CustomDialog(ARCHIVE_DIALOG_HEADER, ARCHIVE_DIALOG_CONTENT);
        final Integer[] selectedCycle = new Integer[1];
        AssetDAOImpl assetDAO = new AssetDAOImpl();

        generateTimeCycleSelection(asset, dialog, selectedCycle);

        dialog.getOkButton().setText(SAVE_LABEL);
        dialog.getOkButton().setOnAction(e -> {
            assetDAO.deleteAssetMeasurementsAfterTimeCycle(asset.getId(), selectedCycle[0]);
            assetDAO.setAssetToBeArchived(asset.getId());
            uiUtilities.changeScene(ASSETS_SCENE, scene);
            dialog.closeDialog();
        });

        dialog.openDialog();
    }

    /**
     * This static functions generates the ComboBox and Label needed for the time cycle selection. It will use
     * the given asset to return the list of time cycles with attribute values available for that asset. This
     * functions allows to keep track of the selected time cycle.
     *
     * @param asset         is the asset to be archived
     * @param dialog        is the parent custom dialog holding the label and ComboBox
     * @param selectedCycle is the selected option in the ComboBox list
     * @author Jeremie
     */
    private static void generateTimeCycleSelection(Asset asset, CustomDialog dialog, Integer[] selectedCycle) {
        // Time Cycle Label Creation and Configuration
        Label timeCycleLabel = new Label(TIME_CYCLE_LABEL);
        timeCycleLabel.setLayoutX(50);
        timeCycleLabel.setLayoutY(105);

        // Time Cycle ComboBox Creation and Configuration
        ComboBox<Integer> timeCycleComboBox = new ComboBox<>();
        timeCycleComboBox.setLayoutX(150);
        timeCycleComboBox.setLayoutY(100);
        timeCycleComboBox.valueProperty().addListener((observableValue, integer, selectedInt) -> selectedCycle[0] = selectedInt);
        ObservableList<Integer> timeCycles;
        timeCycles = FXCollections.observableArrayList(asset.getAssetInfo().getAssetAttributes().get(0).getTimeCyclesList());
        timeCycleComboBox.setItems(timeCycles.sorted(Comparator.reverseOrder()));
        timeCycleComboBox.setValue(timeCycleComboBox.getItems().get(0));

        // Adding the ComboBox and Label to the dialog stage
        dialog.getRootPane().getChildren().addAll(timeCycleLabel, timeCycleComboBox);
    }

    public Pane getRootPane() {
        return rootPane;
    }

    public Button getCancelBtn() {
        return cancelBtn;
    }

    public Button getOkButton() {
        return okBtn;
    }

    public void openDialog() {
        show();
    }

    public void closeDialog() {
        close();
    }
}
