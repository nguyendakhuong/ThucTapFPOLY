package khuonndph14998.fpoly.thuctapfpoly.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.net.PortUnreachableException;

public class FavDB extends SQLiteOpenHelper {
    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "favoriteProduct";
    private static String TABLE_NAME = "favorite";
    private static String KEY_ID = "id";
    private static String ITEM_NAME = "itemName";
    private static String ITEM_CODE = "itemCode";
    private static String ITEM_IMAGE = "itemImage";
    private static String ITEM_DESCRIBE = "itemDescribe";
    private static String ITEM_PRICE = "itemPrice";
    private static String ITEM_QUANTITY = "itemQuantity";
    private static String ITEM_NOTE = "itemNote";
    private static String ITEM_CATEGORY = "itemCategory";
    private static String ITEM_STATUS = "itemStatus";

    private static String CREATE_TABLE = " CREATE TABLE " + TABLE_NAME +
            "(" +KEY_ID+ "TEXT,"+ ITEM_NAME+"TEXT,"+ITEM_CODE+"TEXT,"+ITEM_IMAGE+"TEXT,"+
            ITEM_PRICE+ "INTEGER,"+ITEM_DESCRIBE+"TEXT,"+ITEM_QUANTITY+"INTEGER,"+
            ITEM_NOTE+ "TEXT,"+ITEM_CATEGORY+"TEXT,"+ITEM_STATUS+"TEXT)";

    public FavDB(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void insertEmpty(){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i=1;i<20;i++){
            values.put(KEY_ID,i);
            values.put(ITEM_STATUS,"0");
            database.insert(TABLE_NAME,null,values);
        }
    }
    public void insertIntroTheDatabase(String id,String name,String code,String image,int price
    ,String describe,int quantity,String note,String category,String status){
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID,id);
        cv.put(ITEM_NAME,name);
        cv.put(ITEM_CODE,code);
        cv.put(ITEM_IMAGE,image);
        cv.put(ITEM_PRICE,price);
        cv.put(ITEM_DESCRIBE,describe);
        cv.put(ITEM_QUANTITY,quantity);
        cv.put(ITEM_NOTE,note);
        cv.put(ITEM_CATEGORY,category);
        cv.put(ITEM_STATUS,status);
        db.insert(TABLE_NAME,null,cv);
        Log.d("favoriteProduct",id);
    }
    public Cursor read_all_data(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " +TABLE_NAME+" WHERE "+KEY_ID+"="+id+"";
        return db.rawQuery(sql,null,null);
    }
    public void remove_fav(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = " UPDATE " + TABLE_NAME + " SET "+ ITEM_STATUS+ "='0' WHERE "+KEY_ID+"="+id+"";
        db.execSQL(sql);
    }
    public Cursor select_all_favorite_list(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = " SELECT * FROM " +TABLE_NAME+" WHERE "+ITEM_STATUS+"='1'";
        return db.rawQuery(sql,null,null);
    }
}
