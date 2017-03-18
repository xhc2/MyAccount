package sudoku.myself.xhc.com.myaccount;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by xhc on 2017/2/5.
 */

public class AccountDao {

    private Context context;
    private Dao<Account, Integer> dao;
    private DatabaseHelper helper;

    public AccountDao(Context context) {
        this.context = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            dao = helper.getDao(Account.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public AccountDao(){

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
                        connectionSource, Account.class);


            }catch(Exception e){
                Log.e("xhc" ," exception "+e.getMessage());
                return false;
            }
        }
        return true;
    }

    public void add(Account account) {
        try {
            dao.createOrUpdate(account);
        } catch (Exception e) {

        }
    }

    public void addAll(List<Account> list){
        if(list == null)return ;
        for(Account a : list){
            justAdd(a);
        }
    }

    public void justAdd(Account account){
        try {
            dao.create(account);
        } catch (Exception e) {

        }
    }

    public void delAccount(Account account){
        try {
        dao.delete(account);
        } catch (Exception e) {

        }
    }

    public List<Account> getAllByTime() {
        QueryBuilder<Account, Integer> builder = dao.queryBuilder();
        builder.orderBy("date", false);
        try {
            return builder.query();
        } catch (Exception e) {

        }
        return null;
    }

    //不同分类
    public List<List<Account>> getCategoryData(int category) {
        Map<String, List<Account>> map = new LinkedHashMap<>();
        List<Account> list = getAllByTime();
        List<List<Account>> listResult = new ArrayList<>();
        String pattern = "yyyy-MM-dd";
        switch (category) {
            case Constant.Config.DAY:
                pattern = "yyyy-MM-dd";
                break;
            case Constant.Config.WEEK:
                pattern = "yyyy-MM";
                break;
            case Constant.Config.MONTH:
                pattern = "yyyy-MM";
                break;
            case Constant.Config.YEAR:
                pattern = "yyyy";
                break;
        }


        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
        Date date = new Date();
        for (Account account : list) {
            date.setTime(account.getDate());
            String strDate = sdf.format(date);
            List<Account> listTemp = map.get(strDate);
            if (listTemp == null) {
                listTemp = new ArrayList<>();
                map.put(strDate, listTemp);
            }
            listTemp.add(account);
        }
        for (String key : map.keySet()) {
            listResult.add(map.get(key));
        }
        return listResult;
    }
//    private   TimeZone USER_TIMEZONE = TimeZone.getTimeZone("PRC");
    //先按年将数据拿到，然后再在年中将周数据分开
    public List<List<Account>> getWeekData() {
        List<List<Account>> listYear = getCategoryData(Constant.Config.YEAR);

        List<List<Account>> listResult = new ArrayList<>();
        Map<String, List<Account>> weekMap = new HashMap<>();
        Calendar cal =  Calendar.getInstance();
        for (List<Account> list : listYear) {
            for (Account account : list) {
                //判断是否一
                cal.setTimeInMillis(account.getDate());
                int week = cal.get(Calendar.WEEK_OF_YEAR);
                int year = cal.get(Calendar.YEAR);
//                int firstDay = cal.get(Calendar.DAY_OF_WEEK);
//                Log.e("xhc","year - week "+(year+"-"+week) +" date "+sdf.format(new Date(account.getDate()))+" firstday "+firstDay);
                List<Account> listTemp = weekMap.get(year+"-"+week);
                if (listTemp == null) {
                    listTemp = new ArrayList<>();
                    weekMap.put(year+"-"+week, listTemp);
                }
                listTemp.add(account);
            }
        }
        for(String yearWeek : weekMap.keySet()){
            listResult.add(weekMap.get(yearWeek));
        }
        return listResult;
    }

    public List<Account> getAll() {
        try {
            return dao.queryForAll();
        } catch (Exception e) {

        }
        return null;
    }

}
