package ac.bbt.sp2014f_groupa;

import java.net.URL;

import ac.bbt.sp2014f_groupa.models.RssModel;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 非同期でRSS Feedを読み込むクラス
 * @author Uehara Masato
 */
public class AsyncInsertRssRequest extends AsyncTask<URL, Void, Long> {
    private AddRss mainActivity;

    /**
     * コンストラクタ
     *  
     * @param activity	呼び出し元のActivity
     */
    public AsyncInsertRssRequest(AddRss activity) {
        // 呼び出し元のアクティビティ
        this.mainActivity = activity;
    }
    
    /**
     * 非同期で実行される処理を実装している
     * 
     * @param urls	URLの配列、、なのですが複数指定されることは想定すると実装が難しいので、
     * 				１つしか指定されない前提で実装しています
     * @return 登録に成功した RSS Feed のid
     */
    @Override
    protected Long doInBackground(URL... urls) {
        Log.d("APP", "RSSの取得・登録の非同期処理を開始します。");

        Log.d("APP", "URL: " + urls[0] + " を処理します");

        SQLiteHelper helper = SQLiteHelper.getInstance();
        Long id = Long.valueOf(0L);
        URL url = urls[0];
        id = helper.insertRss(url);
            
        Log.d("APP", "RSSの取得・登録の非同期処理が終了しました。");

    	return id;
    }


    /**
     * このメソッドは非同期処理の終わった後に呼び出されるメソッド
     * 終了後処理を記述したり、処理が終わったことをアプリケーションに通知するために使用する
     * 
     * ここでは登録完了時に、Viewのラベルに登録成功メッセージを表示している
     * 
     * @param id doInBackgroundの戻り値が渡されます
     * @see doInBackground(URL... urls)
     */
    @Override
    protected void onPostExecute(Long id) {
    	Log.d("APP", "Rss登録処理の後処理を開始します");
        TextView label = (TextView) mainActivity.findViewById(R.id.tv_message);
        EditText etTitle = (EditText) mainActivity.findViewById(R.id.et_title);
        EditText etCreatedAt = (EditText) mainActivity.findViewById(R.id.et_created_at);

    	try {
    		// idから登録されたデータを取得する
            RssModel rss = RssModel.findById(id);

            // 更新結果をラベルに表示
            label.setText("「" + rss.getTitle() + "」" + "を追加しました");
            etTitle.setText(rss.getTitle());
            etCreatedAt.setText(rss.getCreatedAt());
            
            // アクティビティのモデルを更新
            mainActivity.setRss(rss);
    	} catch (Exception e) {
    		// 該当データがないか、想定していないエラーが発生した
            label.setText("想定しないエラーが発生しました");
    	}

    	Log.d("APP", "Rss登録処理の後処理を終了します");
    }
}
