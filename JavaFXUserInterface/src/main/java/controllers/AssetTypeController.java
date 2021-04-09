/*
  This Controller is responsible for generating the list of asset types.
  @author Najim, Shirwa, Paul
  @last_edit 02/7/2020
 */
package controllers;

import app.item.Asset;
import app.item.AssetType;
import external.AssetTypeDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import utilities.AssetTypeList;
import utilities.TextConstants;
import utilities.UIUtilities;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AssetTypeController extends Controller implements Initializable {

    //Configure the table and columns
    @FXML
    private TableView<AssetTypeList> tableView;
    @FXML
    private TableColumn<AssetTypeList, String> columnName;
    @FXML
    private TableColumn<AssetTypeList, Integer> columnLiveAssets;
    @FXML
    private TableColumn<AssetTypeList, Integer> columnArchivedAssets;
    @FXML
    private TableColumn<AssetTypeList, Double> columnOk;
    @FXML
    private TableColumn<AssetTypeList, Double> columnAdvisory;
    @FXML
    private TableColumn<AssetTypeList, Double> columnCaution;
    @FXML
    private TableColumn<AssetTypeList, Double> columnWarning;
    @FXML
    private TableColumn<AssetTypeList, Double> columnFailed;

    @FXML
    private FlowPane assetsTypeThumbPane;

    //Configure buttons
    @FXML
    private Button addTypeBtn;

    private UIUtilities uiUtilities;
    private ArrayList<AssetType> assetTypes;
    private AssetTypeDAOImpl assetTypeDOA;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uiUtilities = new UIUtilities();
        assetTypeDOA = new AssetTypeDAOImpl();
        assetTypes = assetTypeDOA.getAssetTypeList();
        attachEvents();
        ObservableList<AssetTypeList> assetTypeList = getAssetList();
        generateThumbnails(assetTypeList);
        fillAssetTypeTable(assetTypeList);
    }

    /**
     * Fill table with database information
     *
     * @author Shirwa
     */
    public void fillAssetTypeTable(ObservableList<AssetTypeList> list) {
        tableView.setItems(list);
        // When TableRow is clicked, send data to AssetTypeInfo scene.
        tableView.setRowFactory(tv -> {
            TableRow<AssetTypeList> row = new TableRow<>();
            row.setOnMouseClicked(event -> uiUtilities.changeScene(row, "/AssetTypeInfo", row.getItem(), row.getScene()));
            return row;
        });
        UIUtilities.autoResizeColumns(tableView);
    }

    /**
     * This method will return an ObservableList of AssetList objects
     *
     * @author Shirwa, Paul
     * edit: There was an issue where the getAssetTypeIdCount() would not match in size to the assetTypeList()
     * and it would crash the asset if there was an asset type with no assets associated to it
     * so this methode was rewrote
     */
    private ObservableList<AssetTypeList> getAssetList() {
        ObservableList<AssetTypeList> assetTypeList = FXCollections.observableArrayList();

        for (AssetType assetType : assetTypes) {
            assetTypeList.add(new AssetTypeList(
                    assetType,
                    assetTypeDOA.getAssetTypeIdCount(assetType.getId(), true),
                    assetTypeDOA.getAssetTypeIdCount(assetType.getId(), false),
                    assetTypeDOA.getAssetTypeThreshold(assetType.getId(), TextConstants.OK_THRESHOLD),
                    assetTypeDOA.getAssetTypeBoundaryCount(assetType.getId(), TextConstants.OK_THRESHOLD),
                    assetTypeDOA.getAssetTypeThreshold(assetType.getId(), TextConstants.CAUTION_THRESHOLD),
                    assetTypeDOA.getAssetTypeBoundaryCount(assetType.getId(), TextConstants.CAUTION_THRESHOLD),
                    assetTypeDOA.getAssetTypeThreshold(assetType.getId(), TextConstants.ADVISORY_THRESHOLD),
                    assetTypeDOA.getAssetTypeBoundaryCount(assetType.getId(), TextConstants.ADVISORY_THRESHOLD),
                    assetTypeDOA.getAssetTypeThreshold(assetType.getId(), TextConstants.WARNING_THRESHOLD),
                    assetTypeDOA.getAssetTypeBoundaryCount(assetType.getId(), TextConstants.WARNING_THRESHOLD),
                    assetTypeDOA.getAssetTypeThreshold(assetType.getId(), TextConstants.FAILED_THRESHOLD),
                    assetTypeDOA.getAssetTypeBoundaryCount(assetType.getId(), TextConstants.FAILED_THRESHOLD)

            ));
        }
        return assetTypeList;
    }

    /**
     *
     */
    public void generateThumbnails(ObservableList<AssetTypeList> assetTypeList) {
        if (assetTypes != null) {
            for (AssetTypeList assetType : assetTypeList) {
                Pane pane = new Pane();
                pane.setOnMouseClicked(event -> uiUtilities.changeScene("/AssetTypeInfo", assetType, pane.getScene()));
                pane.getStyleClass().add("assetTypePane");

                Text assetTypeName = new Text(assetType.getName());
                assetTypeName.getStyleClass().add("assetTypeName");
                Label okLabel = new Label("OK");
                Label advisoryLabel = new Label("Advisory");
                Label cautionLabel = new Label("Caution");
                Label warningLabel = new Label("Warning");
                Label failedLabel = new Label("Failed");

                HBox okBox = new HBox();
                okBox.getStyleClass().add("okBox");
                okBox.getStyleClass().add("ok");
                okBox.setAlignment(Pos.CENTER);
                HBox advisoryBox = new HBox();
                advisoryBox.getStyleClass().add("advisoryBox");
                advisoryBox.getStyleClass().add("advisory");
                advisoryBox.setAlignment(Pos.CENTER);
                HBox cautionBox = new HBox();
                cautionBox.getStyleClass().add("cautionBox");
                cautionBox.getStyleClass().add("caution");
                cautionBox.setAlignment(Pos.CENTER);
                HBox warningBox = new HBox();
                warningBox.getStyleClass().add("warningBox");
                warningBox.getStyleClass().add("warning");
                warningBox.setAlignment(Pos.CENTER);
                HBox failedBox = new HBox();
                failedBox.getStyleClass().add("failedBox");
                failedBox.getStyleClass().add("failed");
                failedBox.setAlignment(Pos.CENTER);
                Text okAssets = new Text(String.valueOf(assetType.getCountOk()));
                Text advisoryAssets = new Text(String.valueOf(assetType.getCountAdvisory()));
                Text cautionAssets = new Text(String.valueOf(assetType.getCountCaution()));
                Text warningAssets = new Text(String.valueOf(assetType.getCountWarning()));
                Text failedAssets = new Text(String.valueOf(assetType.getCountFailed()));

                assetTypeName.setLayoutX(15.0);
                assetTypeName.setLayoutY(35.0);
                okLabel.setLayoutX(15.0);
                okLabel.setLayoutY(76.0);
                advisoryLabel.setLayoutX(15.0);
                advisoryLabel.setLayoutY(119.0);
                cautionLabel.setLayoutX(15.0);
                cautionLabel.setLayoutY(162.0);
                warningLabel.setLayoutX(15.0);
                warningLabel.setLayoutY(205.0);
                failedLabel.setLayoutX(15.0);
                failedLabel.setLayoutY(248.0);

                okBox.setLayoutX(131.0);
                okBox.setLayoutY(68.0);
                advisoryBox.setLayoutX(131.0);
                advisoryBox.setLayoutY(111.0);
                cautionBox.setLayoutX(131.0);
                cautionBox.setLayoutY(154.0);
                warningBox.setLayoutX(131.0);
                warningBox.setLayoutY(197.0);
                failedBox.setLayoutX(131.0);
                failedBox.setLayoutY(240.0);

                okBox.getChildren().add(okAssets);
                advisoryBox.getChildren().add(advisoryAssets);
                cautionBox.getChildren().add(cautionAssets);
                warningBox.getChildren().add(warningAssets);
                failedBox.getChildren().add(failedAssets);

                pane.getChildren().addAll(assetTypeName, okLabel, advisoryLabel, warningLabel, cautionLabel, failedLabel, okBox, advisoryBox,
                        warningBox, cautionBox, failedBox);
                assetsTypeThumbPane.getChildren().add(pane);
            }
        }
    }

    /**
     * Adds mouse events to all the buttons and allows columns to be filled with
     * give information
     *
     * @author Shirwa
     */
    public void attachEvents() {
        // As the window expands or shrinks, asset panes will adjust to the window size accordingly
        assetsTypeThumbPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            double assetTypeFlowWidth = (double) newVal - 54;
            if(assetsTypeThumbPane.getChildren().size() < 6) {
                int nbOfPanes = assetsTypeThumbPane.getChildren().size();
                assetTypeFlowWidth = assetTypeFlowWidth - (247 * nbOfPanes);
                assetTypeFlowWidth = assetTypeFlowWidth / (nbOfPanes - 1);
                assetsTypeThumbPane.setHgap(assetTypeFlowWidth);
            } else {
                int nbOfPanes = (int) (assetTypeFlowWidth / 247);
                assetTypeFlowWidth = (assetTypeFlowWidth % 247);
                assetTypeFlowWidth = assetTypeFlowWidth / (nbOfPanes - 1);
                assetsTypeThumbPane.setHgap(assetTypeFlowWidth);
            }
        });

        //set up the columns in the table
        attachColumnEvents();

        //Attach link to addTypeBtn to go to AddAssetType.fxml
        addTypeBtn.setOnMouseClicked(mouseEvent -> uiUtilities.changeScene("/AddAssetType", addTypeBtn.getScene()));
    }

    /**
     * add the column events so they get filled with the correct information
     *
     * @author Paul
     */
    public void attachColumnEvents() {
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnLiveAssets.setCellValueFactory(new PropertyValueFactory<>("liveAssets"));
        columnArchivedAssets.setCellValueFactory(new PropertyValueFactory<>("archivedAssets"));
        columnOk.setCellValueFactory(new PropertyValueFactory<>("countOk"));
        columnAdvisory.setCellValueFactory(new PropertyValueFactory<>("countAdvisory"));
        columnCaution.setCellValueFactory(new PropertyValueFactory<>("countCaution"));
        columnWarning.setCellValueFactory(new PropertyValueFactory<>("countWarning"));
        columnFailed.setCellValueFactory(new PropertyValueFactory<>("countFailed"));
    }
}
