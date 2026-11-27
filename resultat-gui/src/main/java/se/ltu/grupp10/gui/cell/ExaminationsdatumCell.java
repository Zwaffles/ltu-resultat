package se.ltu.grupp10.gui.cell;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;

import se.ltu.grupp10.gui.model.StudentRad;

public class ExaminationsdatumCell extends TableCell<StudentRad, LocalDate> {

    private final DatePicker datumval = new DatePicker();
    private ObjectProperty<LocalDate> radensDatum;

    public ExaminationsdatumCell() {
        datumval.setEditable(false);
        datumval.setPromptText("Välj datum");
    }

    @Override
    protected void updateItem(LocalDate datum, boolean tom) {
        super.updateItem(datum, tom);

        if (radensDatum != null) {
            datumval.valueProperty().unbindBidirectional(radensDatum);
            radensDatum = null;
        }

        if (tom) {
            setGraphic(null);
            return;
        }

        StudentRad rad = getTableView().getItems().get(getIndex());
        radensDatum = rad.examinationsdatumProperty();
        datumval.valueProperty().bindBidirectional(radensDatum);
        setGraphic(datumval);
    }
}
