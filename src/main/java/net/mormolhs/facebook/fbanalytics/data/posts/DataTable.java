package net.mormolhs.facebook.fbanalytics.data.posts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toikonomakos on 3/16/14.
 */
public class DataTable {

    List<DataRow> table = new ArrayList<DataRow>();

    public List<DataRow> getTable() {
        return table;
    }

    public void setTable(List<DataRow> table) {
        this.table = table;
    }
}
