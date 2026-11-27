package se.ltu.grupp10.gui.cell;

import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;

import se.ltu.grupp10.gui.model.StudentRad;

public final class StatusCell extends TableCell<StudentRad, String> {

    @Override
    protected void updateItem(String status, boolean tom) {
        super.updateItem(status, tom);

        if (tom || status == null || status.isBlank()) {
            setText(null);
            setTooltip(null);
            return;
        }

        setText(status);
        setTooltip(new Tooltip(status));
    }
}
