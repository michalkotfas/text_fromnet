package pl.kotfas.michal.nettext;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import java.io.IOException;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;

public class MainActivity extends Activity
{
    Activity context;
    TextView txtview;
    ProgressDialog pd;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
    }

    public void onStart()
    {
        super.onStart();

        BackTask bt=new BackTask();
        bt.execute("http://192.168.1.1/");

    }

    private class BackTask extends AsyncTask<String,Integer,Void>
    {
        String text="";
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setTitle("Reading");
            pd.setMessage("Downloading");
            pd.setCancelable(true);
            pd.setIndeterminate(false);
            pd.show();
        }

        protected Void doInBackground(String...params){
            URL url;
            try {
                url = new URL(params[0]);
                HttpURLConnection con=(HttpURLConnection)url.openConnection();
                InputStream is=con.getInputStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                String line;
                while((line=br.readLine())!=null)
                {
                    text+=line;
                }
                br.close();
            }catch (Exception e)
            {
                e.printStackTrace();
                if(pd!=null) pd.dismiss();
            }
            return null;
        }
        protected void onPostExecute(Void result)
        {
            if(pd!=null)
                pd.dismiss();
            txtview=(TextView)findViewById(R.id.text_view);
            txtview.setText(text);
        }
    }
}