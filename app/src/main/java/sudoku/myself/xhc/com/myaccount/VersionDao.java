package sudoku.myself.xhc.com.myaccount;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.io.File;
import java.sql.SQLException;

/**
 * Created by xhc on 2017/6/4.
 */

public class VersionDao {

    private Context context;
    private Dao<VersionBean, Integer> dao;
    private DatabaseHelper helper;


    public VersionDao(Context context) {
        this.context = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            dao = helper.getDao(VersionBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public VersionDao(){

    }

    //导入备份数据库,需要让用户选择
    public boolean   getBackUpDataBase(String path){
        File file = new File(path);

        if(file.exists()){
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file , null);
            AndroidConnectionSource connectionSource = new AndroidConnectionSource(
                    db);

            try{
                dao = DaoManager.createDao(
                        connectionSource, VersionBean.class);
            }catch(Exception e){
                Log.e("xhc" ," exception "+e.getMessage());
                return false;
            }
        }
        return true;
    }



    public void updateVersion(long time){
        VersionBean vb = new VersionBean();
        vb.setVersion(time);
        vb.setId(1);
        try {
            dao.createOrUpdate(vb);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //获取当前库的版本号
    public long getVersion(){
        try {
            VersionBean vb = dao.queryForId(1);
            if(vb != null){
                return vb.getVersion();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

}
