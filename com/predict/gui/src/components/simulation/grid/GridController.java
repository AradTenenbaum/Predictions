package components.simulation.grid;

import data.dto.GridDto;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import utils.object.Grid;

public class GridController {

    @FXML
    private Label rowLabel;

    @FXML
    private Label colLabel;

    private GridDto grid;

    public void setGrid(GridDto grid) {
        this.grid = grid;
        setDisplay();
    }

    public void setDisplay() {
        rowLabel.setText("Rows: " + grid.getRows());
        colLabel.setText("Columns: " + grid.getCols());
    }
}
