package sudoku.myself.xhc.com.myaccount;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by xhc on 2017/6/4.
 */
@DatabaseTable(tableName = "tb_version")
public class VersionBean {

    @DatabaseField(generatedId = true)
    private int id;

    //利用时间锉标志版本
    @DatabaseField(columnName = "version")
    private long version;


    public void setId(int id) {
        this.id = id;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }



}
