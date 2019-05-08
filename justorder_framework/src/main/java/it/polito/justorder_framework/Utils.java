package it.polito.justorder_framework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;

import com.google.firebase.database.DataSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;
import java.util.logging.Logger;

import it.polito.justorder_framework.model.Product;

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

    public static String createImageFromBitmap(Bitmap bitmap, Context context) {
        String fileName = "myImage";//no .png or .jpg needed
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                context.getFilesDir()/* directory */
        );

        return image;
    }

    public static <T> T convertObject(DataSnapshot dataSnapshot, Class<T> tClass) {
        T product = dataSnapshot.getValue(tClass);
        try {
            if (tClass.getMethod("setKeyId", String.class) != null) {
                tClass.getMethod("setKeyId", String.class).invoke(product, dataSnapshot.getKey());
            }
        }catch (Exception e){

        }
        return product;
    }

    public static void getBitmapFromURL(String src, Consumer<Bitmap> cb) {
        new AsyncTask<String, Void, Void>() {
            protected Void doInBackground(String... urls){
                try {
                    URL url = new URL(src);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    cb.accept(myBitmap);
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute(src);
    }

    public static Bitmap getBitmapFromURL(Uri uri) {
        try {
            URL url = new URL(uri.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static byte[] getByteArray(InputStream stream) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int read = 0;
        byte[] buff = new byte[1024];
        while ((read = stream.read(buff)) != -1) {
            bos.write(buff, 0, read);
        }
        byte[] streamData = bos.toByteArray();
        return streamData;
    }

    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    public static double distance(double startLat, double startLong,
                                  double endLat, double endLong) {

        double dLat  = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat   = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- d
    }

    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
