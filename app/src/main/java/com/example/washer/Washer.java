package com.example.washer;

import android.view.View;
import android.widget.Button;

public class Washer {
    private boolean busy = false;
    private int id = 0;
    private long destiny_millis_time = 0;
    private final Button button;

    public Washer(int id, Button button) {
        this.id = id;
        this.button = button;

    }

    public boolean getImformFromDatabase(){

        //**업데이트 필요!!  this.id를 통해 DB에서 데이터 받아온다
        boolean getImformSuccess = true;
        this.destiny_millis_time = 0;
        this.setBusy(true);

        return getImformSuccess;
    }

    public boolean updateImformToDatabase( boolean busy, long destiny_millis_time) {
        long destiny_millis_time_server = 0;//DB에서 불러온다
        boolean busy_server = false;//DB에서 불러온다
        if(this.getDestiny_millis_time() != destiny_millis_time_server || this.isBusy() != busy_server){
            //현재 destiny_millis_time destiny_millis_time_server와 다르다면 실패 ex) 업데이트 하려는데 DB에서 이미 정보가 바뀌었을때
            //따로 업데이트는 해줄 필요 없다. 주기적으로 쓰레드에서 업데이트를 해줄거기 때문에
            return false;
        }
        //**업데이트 필요!!  server에 destiny_millis_time 업데이트
        this.setDestiny_millis_time(destiny_millis_time);
        //**업데이트 필요!!  server에 busy를 업데이트
        this.setBusy(busy);
        return true;
    }
//    public boolean updateImformToDatabase(){
//        //this.id를 통해 DB에 데이터 업데이트
//        //서버랑 지금이랑 비교해서 다르면 서버껄 쓴다
//
//        this.destiny_millis_time = 0;
//        this.setBusy(true);
//
//        return updateImformSuccess;
//    }
    //=================================
    //========getter and setter========
    //=================================

    public void setButtonListener(View.OnClickListener listener){
        this.button.setOnClickListener(listener);
    }

    public long getDestiny_millis_time() {
        return destiny_millis_time;
    }

    private void setDestiny_millis_time(long destiny_millis_time) {
        this.destiny_millis_time = destiny_millis_time;
    }

    public boolean isBusy() {
        return busy;
    }

    private void setBusy(boolean busy) {
        this.busy = busy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
