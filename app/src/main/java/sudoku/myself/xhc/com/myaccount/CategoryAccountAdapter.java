package sudoku.myself.xhc.com.myaccount;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by xhc on 2017/2/7.
 */

public class CategoryAccountAdapter extends MyBaseAdapter<List<Account> , CategoryAccountAdapter.ViewHolder> {

    private int category ;
    private SimpleDateFormat sdf = null;
    public CategoryAccountAdapter(List list, Context context) {
        super(list, context);
    }

    public void setCategory(int category){
        this.category = category;
        String pattern = null;
        switch (category) {
            case Constant.Config.DAY:
                pattern = "yyyy-MM-dd";
                break;
            case Constant.Config.WEEK:
                pattern = "yyyy-MM-dd";
                break;
            case Constant.Config.MONTH:
                pattern = "yyyy-MM";
                break;
            case Constant.Config.YEAR:
                pattern = "yyyy";
                break;
        }
        sdf =  new SimpleDateFormat(pattern,Locale.getDefault());

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup container, int type) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item_layout , container , false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<Account> listT = list.get(position);
        float result = getAllMoney(listT);
        holder.tvAccount.setText(context.getString(R.string.all)+result+"");
        holder.tvSize.setText("("+(listT != null ? listT.size() :  0)+")");
        if(sdf != null){
            holder.tvDate.setText(sdf.format(listT.get(0).getDate()));
        }


    }

    private float getAllMoney(List<Account> list){
        float result = 0;
        for(Account a : list){
            if(a.getType() == Account.EXPEND){
                result += a.getMoney();
            }
        }
        return result;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvSize ;
        TextView tvAccount;
        TextView tvDate;
        RelativeLayout rlRoot;
        public ViewHolder(View itemView) {
            super(itemView);
            rlRoot = (RelativeLayout) itemView.findViewById(R.id.root);
            tvAccount = (TextView)itemView.findViewById(R.id.tv_account);
            tvDate = (TextView)itemView.findViewById(R.id.tv_date);
            tvSize = (TextView)itemView.findViewById(R.id.tv_size);
        }
    }

}
