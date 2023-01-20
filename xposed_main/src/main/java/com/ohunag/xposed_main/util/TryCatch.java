package com.ohunag.xposed_main.util;

import android.util.Log;

public class TryCatch {

    public static final String TAG="TryCatch";
    public static void  run(Block block){
        try {
            block.invoke();
        }catch (Exception ignored){

        }
    }

    public static void runLog(Block block){
        try {
            block.invoke();
        }catch (Exception e){
            Log.e(TAG, "runLog: "+e.toString() );
        }
    }
    public static void runPrintStackTrace(Block block){
        try {
            block.invoke();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void run(Block block,ExceptionCallBack exceptionCallBack){
        try {
            block.invoke();
        }catch (Exception e){
           exceptionCallBack.onException(e);
        }
    }



    public interface Block{

        void invoke() throws Exception;
    }

    public interface ExceptionCallBack{
        void onException(Exception e);
    }

}
