package BackgroundTasks;

import app.item.AssetType;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class ChangeSceneService extends Service<Integer> {
    private static final String FXML = ".fxml";
    private MouseEvent mouseEvent;
    private Scene scene;
    private String fxmlFileName;
    public void setFxmlFileName(String fxmlFileName){this.fxmlFileName=fxmlFileName;}
    public void setMouseEvent(MouseEvent mouseEvent){this.mouseEvent=mouseEvent;}
    public void setScene(Scene scene){this.scene=scene;}
    @Override
    protected Task<Integer> createTask() {
        ChangeSceneTask changeSceneTask=new ChangeSceneTask();
        changeSceneTask.setFxmlFileName(fxmlFileName);
//        changeSceneTask.setMouseEvent(mouseEvent);
//        changeSceneTask.setScene(scene);
        try {
            changeSceneTask.call();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return changeSceneTask;
    }
}
