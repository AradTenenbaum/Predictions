package components.pages.management.simulation.termination;

import engine.TerminationDto;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TerminationController {

    @FXML
    private Label terRules;

    private TerminationDto currentTermination;

    public void setCurrentTermination(TerminationDto currentTermination) {
        this.currentTermination = currentTermination;
        setDisplay();
    }

    private void setDisplay() {
        String rules = "";
        if(currentTermination.isStoppedByUser()) {
            rules = "Stop by user";
        } else {
            if(currentTermination.getSeconds() != -1) rules += ("Stop after " + currentTermination.getSeconds() + " seconds\n");
            if(currentTermination.getTicks() != -1) rules += ("Stop after " + currentTermination.getTicks() + " ticks");
        }
        terRules.setText(rules);
    }

}
