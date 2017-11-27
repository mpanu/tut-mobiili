package fi.tut.matti.laskukone;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FileUtils {
    private static final String TAG = FileUtils.class.getName();

    public static void kirjoitaTiedostoon(Context context, String string) {
        //https://developer.android.com/training/basics/data-storage/files.html#WriteInternalStorage
        Log.d(TAG, "kirjoitus");
        String filename = "myfile";
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(string.getBytes());
            outputStream.close();
            Log.d(TAG, "kirjoitus onnistui");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "kirjoitus ei onnistunut");
        }
    }

    public static String lueTiedostosta(Context context) {
        //http://stackoverflow.com/questions/14768191/how-do-i-read-the-file-content-from-the-internal-storage-android-app
        String filename = "myfile";
        FileInputStream fis;
        StringBuilder sb=new StringBuilder("moi");
        try {
            fis=context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            sb = new StringBuilder();
            String line;
            while((line=bufferedReader.readLine())!=null)
            {
                sb.append(line+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Luku ei onnistunut");
        }
        String string=sb.toString();
        Log.d(TAG, string);
        return string;
    }

    //
    public static void clearLog(Context context) {
        String filename = "myfile";
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write("".getBytes());
            outputStream.close();
            Log.d(TAG, "tyhjäys onnistui");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "tyhjäys ei onnistunut");
        }
    }

}
