package sudoku.myself.xhc.com.myaccount;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by xhc on 2017/2/5.
 */

public class MyRecyleAdapter extends MyBaseAdapter<Account, MyRecyleAdapter.ViewHolder> {

    public MyRecyleAdapter(List list, Context context) {
        super(list, context);
    }
    private SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm" , Locale.getDefault());


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup container, int type) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.account_item , container, false) );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Account account = list.get(position);
        if(account.getType() == Account.INCOME){
            holder.rlRoot.setBackgroundResource(R.drawable.income_selector);
        }
        else{
            holder.rlRoot.setBackgroundResource(R.drawable.expend_selector);
        }
        holder.tvAccount.setText(account.getMoney() + context.getString(R.string.rmb));
        holder.tvDate.setText(sdf.format(new Date(account.getDate())));
        holder.tvRemark.setText(account.getRemark());
        holder.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lis != null){
                    lis.onItemClick(view , account , position);
                }
            }
        });

        holder.rlRoot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(longLis != null){
                    longLis.onLongClick(account);
                }

                return true;
            }
        });

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvAccount;
        TextView tvDate;
        RelativeLayout rlRoot;
        TextView tvRemark;
        public ViewHolder(View itemView) {
            super(itemView);
            rlRoot = (RelativeLayout) itemView.findViewById(R.id.root);
            tvAccount = (TextView)itemView.findViewById(R.id.tv_account);
            tvDate = (TextView)itemView.findViewById(R.id.tv_date);
            tvRemark = (TextView) itemView.findViewById(R.id.tv_remark);
        }
    }

}
