package utilities;

import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ProgressIndicator;
public class ProgresIndicator {
    public static AnchorPane addIndicator(double x, double y){
        AnchorPane subPane=new AnchorPane();
        ProgressIndicator pi=new ProgressIndicator();
        pi.setVisible(true);
        subPane.getChildren().add(pi);
        return subPane;

    }
}
