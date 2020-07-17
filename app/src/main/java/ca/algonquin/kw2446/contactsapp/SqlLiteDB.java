package ca.algonquin.kw2446.contactsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SqlLiteDB {
    private final String Contact_Table="Contacts";
    public static final String KEY_ROWID="_id";
    public static final String KEY_NAME="name";
    public static final String KEY_CELL="phone";
    public static final String KEY_EMAIL="email";
    public static final String KEY_REGDATE="regDate";
    public static final String KEY_UEMAIL="uEmail";

    String[] contactsColumns = new String[]{KEY_ROWID, KEY_NAME, KEY_EMAIL , KEY_CELL, KEY_UEMAIL,KEY_REGDATE};

    private final String User_TABLE="Users";

    public static final String KEY_USERNAME="userName";
    public static final String KEY_USEREMAIL="userEmail";
    public static final String KEY_USERPWD="userPwd";
    String[] userColumns = new String[]{KEY_ROWID, KEY_USERNAME, KEY_USEREMAIL , KEY_USERPWD,KEY_REGDATE};

    private final String DATABASE_NAME="ContactDB";


    private final int DATABASE_VERSION=1;

    private SqlLiteDB.DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    public SqlLiteDB (Context context){
        this.ourContext=context;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            /*
            Create Table contactsTable(_id Interger Privatry key Autoincreament,
             person_name Text not null, _celll text not null);
            */

            String sqlCode="Create Table "+ Contact_Table +"("+
                    KEY_ROWID +" INTEGER Primary Key AUTOINCREMENT, " +
                    KEY_NAME +" TEXT not null,"+
                    KEY_EMAIL +" TEXT not null,"+
                    KEY_CELL +" TEXT not null," +
                    KEY_UEMAIL+" TEXT not null," +
                    KEY_REGDATE+" DATETIME DEFAULT (datetime('now','localtime')));";
            db.execSQL(sqlCode);

            String sqlCode2="Create Table "+ User_TABLE +"("+
                    KEY_ROWID +" INTEGER Primary Key AUTOINCREMENT, " +
                    KEY_USERNAME +" TEXT not null,"+
                    KEY_USEREMAIL +" TEXT not null,"+
                    KEY_USERPWD +" TEXT not null,"+
                    KEY_REGDATE+" datetime default current_timestamp);";

            db.execSQL(sqlCode2);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop table IF Exists "+ Contact_Table);
            db.execSQL("Drop table IF Exists "+ User_TABLE);
            onCreate(db);
        }
    }

    public  SqlLiteDB open() throws SQLException {
        ourHelper=new SqlLiteDB.DBHelper(ourContext);
        ourDatabase=ourHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        ourHelper.close();
    }

    public long createEntry(String tableName, HashMap<String, String> columns){
        ContentValues cv=new ContentValues();

        for (Iterator<Map.Entry<String, String>> it = columns.entrySet().iterator();
             it.hasNext();) {

            Map.Entry<String, String> pair = it.next();
            cv.put(pair.getKey(),pair.getValue());
        }

        return ourDatabase.insert(tableName,null, cv);
    }
    public String getDataString(String tableName) {
        String[] columns = tableName.equals("Users")?this.userColumns:this.contactsColumns;
        Cursor cursor = ourDatabase.query(tableName, columns, null, null, null, null, null);

        String result = "";


        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
           for(int i=0;i<cursor.getColumnCount();i++){
               result += cursor.getString(i)+":";
            }
            result += "\n";
        }
        cursor.close();
        String temp=result;
        return result;
    }
    public JSONArray getDataJSon(String tableName) {
        String[] columns=tableName.equals("Contacts")?contactsColumns:userColumns;

        Cursor cursor = ourDatabase.query(tableName, columns, null, null, null, null, null);

        String result = "";
        JSONArray resultSet = new JSONArray();
        JSONObject returnObj = new JSONObject();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {

                    try {

                        if (cursor.getString(i) != null) {

                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {

                    }
                }

            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        //returnObj.put("rows", resultSet);
        return resultSet;

    }
    public ArrayList<Contact> getContacts() {

        Cursor cursor = ourDatabase.query("Contacts", contactsColumns, null, null, null, null, null);


        ArrayList<Contact> resultSet=new ArrayList<Contact>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int totalColumn = cursor.getColumnCount();
              Contact contact=new Contact(cursor.getInt(0),cursor.getString(1),
                      cursor.getString(2),cursor.getString(3),cursor.getString(4),
                      cursor.getString(5));

            resultSet.add(contact);

        }

        cursor.close();
        //returnObj.put("rows", resultSet);
        return resultSet;

    }

    public long deleteEntry(String tableName,int rowId){
        return  ourDatabase.delete(tableName, KEY_ROWID+"=?", new String[]{String.valueOf(rowId)});
    }

    public  long updateEntry(String tabelName, int rowId, String name, String cell, String email){
        ContentValues cv=new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_CELL, cell);
        cv.put(KEY_EMAIL, email);

        return ourDatabase.update(tabelName,cv,KEY_ROWID+"=?",new String[]{String.valueOf(rowId)});
    }

    public Users IsValidUser(String email, String pwd){
        //Cursor c = db.rawQuery("SELECT * FROM Users WHERE userEmail='email' AND userPwd='pwd'", null);


        Cursor cursor = ourDatabase.query(true,"Users", userColumns,"userEmail=?",new String[]{email},null,null,null,null);
        cursor.moveToFirst();
        Users loginUser=null;
        if(cursor.getCount()>0){
            String userPwd=cursor.getString(cursor.getColumnIndex(KEY_USERPWD));

            if( userPwd.equals(pwd)){
                loginUser=new Users(cursor.getInt(0),cursor.getString(1)
                        ,cursor.getString(2),cursor.getString(3), cursor.getString(4));
              //  ApplicationClass.user=loginUser;
            }


           return loginUser;
        }
        else{
            return loginUser;
        }
    }
}
