package it.polito.justorder_framework;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Utils {

    public static String encodeImage(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    public static Bitmap getBitmapOfImage(String base64){
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static Bitmap resizeBitmap(Bitmap image, int width){
        float aspectRatio = image.getWidth() /
                (float) image.getHeight();
        int height = Math.round(width / aspectRatio);

        return Bitmap.createScaledBitmap(image, width, height, false);
    }

    public static String beautifyTime(Integer hour, Integer minute){
        String hourTime = Integer.toString(hour);
        if(hourTime.length() == 1){
            hourTime = "0" + hourTime;
        }
        String minuteTime = Integer.toString(minute);
        if(minuteTime.length() == 1){
            minuteTime = "0" + minuteTime;
        }

        return hourTime + ":" + minuteTime;
    }
}
