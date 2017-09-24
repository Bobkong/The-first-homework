package com.example.bob.mynote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DB{
    private SQLiteDatabase db;
    List<Data> all = new ArrayList<>();
    private static final String dbname = "Note";
    private static final String tbname = "note";
    private static final String pictbname = "pic";
    public DB(Context context){
        db = DatabaseHelper.getInstance(context);
    }
    public void insert(int id,String mNote,String mTime,String mAccount){
        db.execSQL("insert into note values(?,?,?,?)",new String[] { id+"",mNote,mTime,mAccount});
    }
    public int getId(){
        return  DatabaseHelper.id;
    }
    public Cursor getAllUsers(){
        Cursor cr = db.rawQuery("select * from user",null);
        return cr;
    }
    public long ifExisted(String account){
        Cursor cr;
        cr = db.rawQuery("select count(*) from user where account = ?" ,new String[]{account});
        cr.moveToFirst();
        long count = cr.getLong(0);
        cr.close();
        return count;
    }
    public void insertUser(String account,String password){
        db.execSQL("insert into user values(?,?)",new String[] { account,password});
    }

    public Cursor getAll(String mAccount){
        Cursor cr = db.rawQuery("select * from note where user = ?",new String[]{mAccount});
        return cr;
    }

    public void delete(int id){
        db.execSQL("delete from note where noteid=?", new String[] { id + "" });
    }
    public void update(int id,String note,String time){
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("noteid",id);
        updatedValues.put("note",note);
        updatedValues.put("time",time);
        String where = "noteid=" +id;
        db.update(tbname,updatedValues,where,null);
    }
    public Cursor query(String note){
        Cursor cr = db.rawQuery("select * from note where note like ?",new String[]{"%" + note + "%"});
        return cr;
    }
    public Cursor getPic(int id){
        Cursor cr = db.rawQuery("select picture from pic where picid = ?",new String[]{id + ""});
        return cr;
    }
    public void insertPic(int id, int pos,String path){
        db.execSQL("insert into pic values(?,?,?)",new String[] { id+"",pos+"",path});
    }
    public void deletePic(int id, int pos){
        db.execSQL("delete from pic where picid = ? and pos = ?",new String[]{id+"",pos+""});
    }
    public void close(){
        db.close();
    }
    public void dropTable(SQLiteDatabase db){
        db.execSQL("drop from note");
    }
}