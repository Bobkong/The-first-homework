package com.example.bob.mynote;

/**
 * Created by Bob on 2017/7/4.
 */

class Data {
    private int id;
    private String note;
    private String time;
    private String mAccount;
    Data(int id,String note,String time,String mAccount){
        this.id  = id;
        this.note = note;
        this.time = time;
    }
    public int getId(){return id;}
    public String getNote(){
        return note;
    }
    public String getTime(){
        return time;
    }
    public String getmAccount(){return mAccount;}
    public void setId(int id){this.id = id;}
    public void setNote(String note){
        this.note = note;
    }
    public void setTime(String time){
        this.time = time;
    }
    public void setmAccount(String mAccount){this.mAccount = mAccount;}
}
