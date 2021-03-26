package BackgroundTasks;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import utilities.TextConstants;
import utilities.UIUtilities;

import java.io.IOException;

public class ChangeSceneTask extends Task<Integer> {
    private static final String FXML = ".fxml";
    private MouseEvent mouseEvent;
    private Scene scene;
    private String fxmlFileName;
    public void setFxmlFileName(String fxmlFileName){this.fxmlFileName=fxmlFileName;}
    public void setMouseEvent(MouseEvent mouseEvent){this.mouseEvent=mouseEvent;}
    public void setScene(Scene scene){this.scene=scene;}

    @Override
    protected Integer call() throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlFileName + FXML));
            Parent parent = loader.load();
            scene.setRoot(parent);
        } catch (IOException e) {
//            logger.error("Exception in changeScene(): ", e);
        }
        return null;
    }
}
