package sudoku.myself.xhc.com.myaccount;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by xhc on 2017/2/5.
 */

public class AccountDao {

    private Context context;
    private Dao<Account, Integer> dao;
    private DatabaseHelper helper;

    public AccountDao(Context context)
    {
        this.context = context;
        try
        {
            helper = DatabaseHelper.getHelper(context);
            dao = helper.getDao(Account.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    public void add(Account account){
        try{
            dao.createOrUpdate(account);
        }catch (Exception e){

        }

    }

    public List<Account> getAllByTime(){
        QueryBuilder<Account, Integer>  builder = dao.queryBuilder();
        builder.orderBy("date" , false);
        try{
            return builder.query();
        }catch (Exception e){

        }
        return null;
    }

    public List<Account> getAll(){
        try{
            return dao.queryForAll();
        }catch (Exception e){

        }
        return null;
    }

}
