package ac.bbt.sp2014f_groupa;

import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class AsyncGetRSSRequest extends AsyncTask<URL, Void, String> {
    private Activity mainActivity;

    /**
     * コンストラクタ
     *  
     * @param activity	呼び出し元のActivity
     */
    public AsyncGetRSSRequest(Activity activity) {
        // 呼び出し元のアクティビティ
        this.mainActivity = activity;
    }
    
    // このメソッドは必ずオーバーライドする必要があるよ
    // ここが非同期で処理される部分みたいたぶん。
    @Override
    protected String doInBackground(URL... urls) {
        Log.d("APP", "RSSの取得・登録処理を開始");

        long id = 0;
        String titles = "";
        SQLiteHelper helper = SQLiteHelper.getInstance();
        SQLiteDatabase db = helper.getReadableDatabase();
        
        try {
        	for (URL url: urls) {
                id = helper.insertRSS(url);
                Cursor cursor = db.query(
                		"rsses"
                		, new String[] {"title"}
                		, "id=?"
                		, new String[] {String.valueOf(id)}
                		, null
                		, null
                		, null
                    );
                cursor.moveToFirst();
                
                if (titles.length()>0) {
                	titles += ", " + cursor.getString(0);
                } else {
                	titles = cursor.getString(0);
                }
        	}
        } catch (Exception e) {
            Log.e("APP", "RSSの登録に失敗しました");
            Log.e("APP", e.getMessage());
        }

        Log.d("APP", "RSSの取得・登録処理を終了する");
    	return titles;
    }


    // このメソッドは非同期処理の終わった後に呼び出されます
    @Override
    protected void onPostExecute(String result) {
        // 取得した結果をテキストビューに入れちゃったり
        // TextView tv = (TextView) mainActivity.findViewById(R.id.name);
        // tv.setText(result)
        TextView label = (TextView) mainActivity.findViewById(R.id.tv_message);
        label.setText("「" + result + "」" + "を追加しました");
    }
}
