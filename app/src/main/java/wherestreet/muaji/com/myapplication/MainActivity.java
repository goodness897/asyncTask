package wherestreet.muaji.com.myapplication;
        /*
         * 
         * MainActivity.java
         * @author Santosh Shinde
         * Date: 30/01/2015 12:28:16 PM
         * 
         */
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import wherestreet.muaji.com.myapplication.R;

public class MainActivity extends Activity {
    //public static final String URL ="https://googledrive.com/host/0B_DiX4MiMa3HTHdiYVRmUHBMcW8/image1.jpg";
    public static final String URL="https://s3.ap-northeast-2.amazonaws.com/wherestreet/food/ic_pin_02.png";
    public static final String URL1="https://s3.ap-northeast-2.amazonaws.com/wherestreet/food/KakaoTalk_20160119_213803117.jpg";
    ImageView imageView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);

        // Create an object for subclass of AsyncTask
        GetXMLTask task = new GetXMLTask();
        // Execute the task
        task.execute(new String[] { URL, URL1 });
    }

    private class GetXMLTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }
        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
            System.out.println("finished");
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpsURLConnection httpConnection = (HttpsURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpsURLConnection.HTTP_OK ||
                        httpConnection.getResponseCode() == HttpsURLConnection.HTTP_NOT_MODIFIED ) {

                    stream = httpConnection.getInputStream();
                } else { // just in case..

                    //log.d("Surprize HTTP status was: " ,httpConnection.getResponseCode());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }



    }
}