package whataday.oneweek;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by jaehun on 16. 3. 18..
 */
public class Utils {

    String Dir_path = Environment.getExternalStorageDirectory().toString()+"/OneWeek";

    public void saveBitmap(Bitmap bitmap){

        File path = new File(Dir_path);
        if(!path.exists()){
            path.mkdir();
            Log.i("DIR", "CREATE");
            Log.i("DIR PATH", Dir_path);
            //저장경로 폴더가 존재하지 않으면 생성.
        }

        File image_file = new File(Dir_path+String.format(
                "/%d.jpg", System.currentTimeMillis()));

        if(!image_file.exists()){
            try {
                FileOutputStream outStream = new FileOutputStream(image_file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                Log.d("Image path : ", image_file.getPath());

                outStream.close();
                Log.d("Log", "onPictureTaken - count bytes: " + bitmap.getByteCount());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            Log.d("Log", "onPictureTaken - jpeg");
        }

    }
}
