package sudoku.myself.xhc.com.myaccount;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by xhc on 2017/2/5.
 */
@DatabaseTable(tableName = "tb_account")
public class Account implements Parcelable{

    //入账
    public static final int INCOME = 1;
    //出账
    public static final int EXPEND = 0 ;

    //category 所对应的属性
    public static final int BREAKFAST = 2;
    public static final int LAUNCH = 3;
    public static final int DINNER = 4;
    public static final int SNACK = 5;
    public static final int SHOPPING = 6;
    public static final int TRAVEL = 7;
    public static final int HAIRCUT = 8;
    public static final int TRANSPORTATION = 9;
    public static final int TREATMENT = 10;
    public static final int HOUSE = 11;
    public static final int OTHER = 12;

    @DatabaseField(generatedId = true)
    private int id;


    //入账还是出账
    @DatabaseField(columnName = "type")
    private int type ;

    //日期
    @DatabaseField(columnName = "date")
    private long date;

    //花到哪里了
    @DatabaseField(columnName = "why")
    private String why;

    //花了多少钱
    @DatabaseField(columnName = "money")
    private float money;

    //备注
    @DatabaseField(columnName = "remark")
    private String remark;

    @DatabaseField(columnName = "category")
    private int category;


    @Override
    public void writeToParcel(Parcel o, int i) {
        o.writeInt(type);
        o.writeLong(date);
        o.writeString(why);
        o.writeFloat(money);
        o.writeString(remark);
        o.writeInt(category);
        o.writeInt(id);
    }

    public Account(){}
    public Account(Parcel in){
        type = in.readInt();
        date = in.readLong();
        why = in.readString();
        money = in.readFloat();
        remark = in.readString();
        category = in.readInt();
        id = in.readInt();


    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>()
    {
        public Account createFromParcel(Parcel in)
        {
            return new Account(in);
        }

        public Account[] newArray(int size)
        {
            return new Account[size];
        }
    };

    public int getCategory() {
        return category;
    }


    public int getType() {
        return type;
    }


    public void setType(int type) {
        this.type = type;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public long getDate() {
        return date;
    }

    public String getWhy() {
        return why;
    }

    public float getMoney() {
        return money;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setWhy(String why) {
        this.why = why;
    }

    public void setMoney(float money) {
        this.money = money;
    }


    @Override
    public String toString() {
        return "Account{" +
                "date=" + date +
                ", why='" + why + '\'' +
                ", money=" + money +
                ", remark='" + remark + '\'' +
                ", category=" + category +
                '}';
    }
}
