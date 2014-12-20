package ac.bbt.sp2014f_groupa;

import java.net.URL;

import ac.bbt.sp2014f_groupa.models.NotFoundException;
import ac.bbt.sp2014f_groupa.models.RssModel;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 非同期でRSS Feedを読み込むクラス
 * @author Uehara Masato
 */
public class AsyncUpdateAllRssRequest extends AsyncTask<Void, Void, Long> {
    private ArrayAdapter<String> mAdapter;

    /**
     * コンストラクタ
     *  
     * @param activity	呼び出し元のActivity
     */
    public AsyncUpdateAllRssRequest(ArrayAdapter<String> mAdapter) {
        this.mAdapter = mAdapter;
    }
    
    /**
     * 非同期で実行される処理を実装している
     * 
     * @param voids	値は使用されません
     * @return 更新した件数
     */
    @Override
    protected Long doInBackground(Void... voids) {
        Log.d("APP", "RSSの更新の非同期処理を開始します。");

		SQLiteHelper helper = SQLiteHelper.getInstance();
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                "rsses"
                , new String[] {
                		"id"
                		, "url"
                		, "title"
                		, "created_at"
                		}
                , null
                , null
                , null
                , null
                , null
            );
        
        // 該当データがない時は例外を射出
        if (cursor.getCount() == 0) {
        	throw new NotFoundException(
        			"該当するデータがありません。"
        			);
        }

        long cnt = 0;
        while (cursor.moveToNext()) {
        	cnt += helper.updateRss(cursor.getLong(0));
        }

    	return cnt;
    }


    /**
     * このメソッドは非同期処理の終わった後に呼び出されるメソッド
     * 終了後処理を記述したり、処理が終わったことをアプリケーションに通知するために使用する
     * 
     * ここでは更新完了時に、Viewのラベルに登録成功メッセージを表示している
     * 
     * @param cnt	登録された記事件数
     * @see doInBackground(URL... urls)
     */
    @Override
    protected void onPostExecute(Long cnt) {
    	Log.d("APP", "Rss更新処理の後処理を開始します");

    	// まずはアダプターをクリアする
    	mAdapter.clear();

    	mAdapter.notifyDataSetChanged();

        // DB(読出用)のオブジェクト生成
        SQLiteHelper helper = SQLiteHelper.getInstance();

        /* DBデータ一覧表示 */
        // 一覧の取得
        Cursor cursor = helper.findAllArticlesForMainActivity();

        while (cursor.moveToNext()) {
            mAdapter.add(cursor.getString(3));
        }

        // カーソルのクローズ
        cursor.close();
        
    	Log.d("APP", "Rss更新処理の後処理を終了します");
    }
}
