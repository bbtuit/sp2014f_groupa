package ac.bbt.sp2014f_groupa;

import java.net.URL;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

/**
 * 非同期でRSS Feedを読み込むクラス
 * @author Uehara Masato
 */
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
    
    /**
     * 非同期で実行される処理を実装している
     * 
     * @param urls URLの配列
     * @return 登録に成功した RSS Feed のタイトルをカンマ区切りで繋げた文字列
     */
    @Override
    protected String doInBackground(URL... urls) {
        Log.d("APP", "RSSの取得・登録の非同期処理を開始します。");

        long id = 0;
        String titles = "";
        Cursor cursor = null;
        SQLiteHelper helper = SQLiteHelper.getInstance();
        SQLiteDatabase db = helper.getReadableDatabase();
        
        // URLが配列で来るのでループで処理する
        for (URL url: urls) {
            
        	Log.d("APP", "URL: " + url + " を処理します");
            // URLを登録
            id = helper.insertRSS(url);
            
            // idから登録されたデータを取得する
            cursor = db.query(
                    "rsses"
                    , new String[] {"title"}
                    , "id=?"
                    , new String[] {String.valueOf(id)}
                    , null
                    , null
                    , null
                );
            cursor.moveToFirst();
            String title = cursor.getString(0);
            
            // 取得したデータからタイトルを連結する
            // a, b, cといった形にしたいので、最初のひとつ目以降はタイトルの前にカンマを付けて連結する
            if (titles.length()>0) {
                // 文字列の長さ0より大きいので、２つ目以降のタイトル
                titles += ", " + title;
            } else {
                // 文字列の長さ0なので、最初のタイトル
                titles = title;
            }

        	Log.d("APP", "URL: " + url + " の処理が完了しました（title: " + title + "）");
        }

        Log.d("APP", "RSSの取得・登録の非同期処理が終了しました。");

    	return titles;
    }


    /**
     * このメソッドは非同期処理の終わった後に呼び出されるメソッド
     * 終了後処理を記述したり、処理が終わったことをアプリケーションに通知するために使用する
     * 
     * ここでは登録完了時に、Viewのラベルに登録成功メッセージを表示している
     * 
     * @param result doInBackgroundの戻り値が渡されます
     * @see doInBackground(URL... urls)
     */
    @Override
    protected void onPostExecute(String result) {
    	// 更新結果をラベルに表示
        TextView label = (TextView) mainActivity.findViewById(R.id.tv_message);
        label.setText("「" + result + "」" + "を追加しました");
    }
}
