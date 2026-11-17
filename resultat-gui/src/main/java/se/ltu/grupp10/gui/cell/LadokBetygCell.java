package se.ltu.grupp10.gui.cell;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import se.ltu.grupp10.gui.model.StudentRad;

public class LadokBetygCell extends TableCell<StudentRad, String> {

    private final ComboBox<String> betygVal;
    private StringProperty radensBetyg;

    public LadokBetygCell(ObservableList<String> alternativ) {
        betygVal = new ComboBox<>(alternativ);
        betygVal.setMaxWidth(Double.MAX_VALUE);
    }

    @Override
    protected void updateItem(String betyg, boolean tom) {
        super.updateItem(betyg, tom);

        if (radensBetyg != null) {
            betygVal.valueProperty().unbindBidirectional(radensBetyg);
            radensBetyg = null;
        }

        if (tom) {
            setGraphic(null);
            return;
        }

        StudentRad rad = getTableView().getItems().get(getIndex());
        radensBetyg = rad.ladokBetygProperty();
        betygVal.valueProperty().bindBidirectional(radensBetyg);
        setGraphic(betygVal);
    }
}
