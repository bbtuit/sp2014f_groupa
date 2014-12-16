package ac.bbt.sp2014f_groupa;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AddRssActivity extends Activity {
	// onCreateメソッド(画面初期表示イベントハンドラ)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		// スーパークラスのonCreateメソッド呼び出し
    	super.onCreate(savedInstanceState);
        // レイアウト設定ファイルの指定
    	setContentView(R.layout.activity_rss);
        // フラグメントの設定
    	if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
     }

	// onOptionsItemSelectedメソッド(オプションメニュー選択)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// オプションメニューID取得
        int id = item.getItemId();
        // オプションメニュー判定
        if (id == R.id.action_settings) {
            return true;
        }
        // スーパークラスのonOptionsItemSelectedメソッド呼び出し
        return super.onOptionsItemSelected(item);
    }    
    
    //ここからDBサンプルから転記・改造
    // フラグメントクラス定義
    public static class PlaceholderFragment extends Fragment {
    	
    	SQLiteHelper helper = null;
    	SQLiteDatabase db = null;

        public PlaceholderFragment() {
        }

        // onCreateViewメソッド(フラグメント初期表示イベントハンドラ)
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	// フラグメント設定情報取得
            View rootView = inflater.inflate(R.layout.fragment_rss, container, false);
            
            // 登録ボタンのクリックリスナー設定
            Button insertBtn = (Button)rootView.findViewById(R.id.bt_insert);
            insertBtn.setTag("insert");
            insertBtn.setOnClickListener(new ButtonInsertClickListener());

            // 更新ボタンのクリックリスナー設定
            Button updatetBtn = (Button)rootView.findViewById(R.id.bt_update);
            updatetBtn.setTag("update");
            updatetBtn.setOnClickListener(new ButtonClickListener());

            // 削除ボタンのクリックリスナー設定
            Button delBtn = (Button)rootView.findViewById(R.id.bt_delete);
            delBtn.setTag("delete");
            delBtn.setOnClickListener(new ButtonClickListener());

            // 表示ボタンのクリックリスナー設定
            Button selectBtn = (Button)rootView.findViewById(R.id.bt_display);
            selectBtn.setTag("display");
            selectBtn.setOnClickListener(new ButtonClickListener());

            return rootView;
        }

        // クリックリスナー定義
        class ButtonClickListener implements OnClickListener {
            // onClickメソッド(ボタンクリック時イベントハンドラ)
            public void onClick(View v){
                // タsグの取得
                String tag = (String)v.getTag();

                // メッセージ表示用
                String message  = "";
                TextView label = (TextView)getActivity().findViewById(R.id.tv_message);
                // 入力情報取得
                EditText url = (EditText)getActivity().findViewById(R.id.et_url);
                EditText title = (EditText)getActivity().findViewById(R.id.et_title);
                EditText created_at = (EditText)getActivity().findViewById(R.id.et_created_at);

                // テーブルレイアウトオブジェクト取得
                TableLayout tablelayout = (TableLayout)getActivity().findViewById(R.id.tl_list);

                // テーブルレイアウトのクリア
                tablelayout.removeAllViews();

                // 該当DBオブジェクト取得
                SQLiteHelper helper = SQLiteHelper.getInstance();
                SQLiteDatabase db = helper.getWritableDatabase();

                // 登録ボタンが押された場合
                if(tag.equals("insert")){

                // 更新ボタンが押された場合
                }else if(tag.endsWith("update")){
                    // ファイルのデータ削除
                    try{
                        // 更新条件
                        String condition = null;
                        if(url != null && !url.equals("")){
                            condition = "url = '" + url.getText().toString() + "'";
                        }

                        // トランザクション制御開始
                        db.beginTransaction();

                        // 更新データ設定
                        ContentValues val = new ContentValues();
                        val.put("title", title.getText().toString());
                        val.put("created_at", created_at.getText().toString());
                        // データ更新
                        db.update("id", val, condition, null);

                        // コミット
                        db.setTransactionSuccessful();

                        // トランザクション制御終了
                        db.endTransaction();

                        // メッセージ設定
                        message = "データを更新しました！";
                    }catch(Exception e){
                        // メッセージ設定
                        message = "データ更新に失敗しました！";
                        Log.e("ERROR",e.toString());
                    }

                // 削除ボタンが押された場合
                }else if(tag.endsWith("delete")){
                    // ファイルのデータ削除
                    try{
                        // 削除条件
                        String condition = null;
                        if(url != null && !url.equals("")){

                            condition = "url = '" + url.getText().toString() + "'";
                        }

                        // トランザクション制御開始
                        db.beginTransaction();

                        // データ削除
                        db.delete("id", condition, null);

                        // コミット
                        db.setTransactionSuccessful();

                        // トランザクション制御終了
                        db.endTransaction();

                        // メッセージ設定
                        message = "データを削除しました！";
                    }catch(Exception e){
                        // メッセージ設定
                        message = "データ削除に失敗しました！";
                        Log.e("ERROR",e.toString());
                    }

                // 表示ボタンが押された場合
                }else if(tag.equals("display")){
                    // データ取得
                    try{
                        // 該当DBオブジェクト取得
                        db = helper.getReadableDatabase();

                        // 列名定義
                        String columns[] = {"url","title","created_at"};

                        // データ取得
                        Cursor cursor = db.query(
                                "id", columns, null, null, null, null, "url");

                        // テーブルレイアウトの表示範囲を設定
                        tablelayout.setStretchAllColumns(true);

                        // テーブル一覧のヘッダ部設定
                        TableRow headrow = new TableRow(getActivity());

                        TextView headtxt1 = new TextView(getActivity());
                        headtxt1.setText("URL");
                        headtxt1.setGravity(Gravity.CENTER_HORIZONTAL);
                        headtxt1.setWidth(60);
                        TextView headtxt2 = new TextView(getActivity());
                        headtxt2.setText("RSS名");
                        headtxt2.setGravity(Gravity.CENTER_HORIZONTAL);
                        headtxt2.setWidth(100);
                        TextView headtxt3 = new TextView(getActivity());
                        headtxt3.setText("登録日");
                        headtxt3.setGravity(Gravity.CENTER_HORIZONTAL);
                        headtxt3.setWidth(60);
                        headrow.addView(headtxt1);
                        headrow.addView(headtxt2);
                        headrow.addView(headtxt3);
                        tablelayout.addView(headrow);

                        // 取得したデータをテーブル明細部に設定
                        while(cursor.moveToNext()){

                            TableRow row = new TableRow(getActivity());
                            TextView urltxt
                                    = new TextView(getActivity());
                            urltxt.setGravity(Gravity.CENTER_HORIZONTAL);
                            urltxt.setText(cursor.getString(0));
                            TextView titletxt = new TextView(getActivity());
                            titletxt.setGravity(Gravity.CENTER_HORIZONTAL);
                            titletxt.setText(cursor.getString(1));
                            TextView created_attxt = new TextView(getActivity());
                            created_attxt.setGravity(Gravity.CENTER_HORIZONTAL);
                            created_attxt.setText(cursor.getString(2));
                            row.addView(urltxt);
                            row.addView(titletxt);
                            row.addView(created_attxt);
                            tablelayout.addView(row);

                            // メッセージ設定
                            message = "データを取得しました！";
                        }

                    }catch(Exception e){
                        // メッセージ設定
                        message = "データ取得に失敗しました！";
                        Log.e("ERROR",e.toString());
                    }
                }

                // DBオブジェクトクローズ
                db.close();

                // メッセージ表示
                label.setText(message);
            }
        }
        //ここまでDBサンプルから転記・改造

        /**
         * 登録ボタンが押下された時の処理
         * 
         * @author ueharamasato
         */
        class ButtonInsertClickListener implements OnClickListener {
            // onClickメソッド(ボタンクリック時イベントハンドラ)
            public void onClick(View v) {
            	Log.d("APP", "登録ボタンがクリックされた時の処理を実行します");

                // メッセージ表示用
                TextView label = (TextView)getActivity().findViewById(R.id.tv_message);

                // 入力情報取得
                EditText etURL = (EditText)getActivity().findViewById(R.id.et_url);
                String url_string = etURL.getText().toString();
                
                // RSS URLが既に登録されていないかをチェック
                SQLiteHelper helper = SQLiteHelper.getInstance();
                boolean isDup = helper.isRssUrlDuplicated(url_string);
                
                if (isDup) {
                	// 既に登録されていたので処理をキャンセルします。
					label.setText("そのURLは既に登録されています");
					return;
                }

                try {
                	
                	// RSS Feedを取得します。
					URL url = new URL(url_string);
					
					// ネットへのリクエストは非同期で実行する必要があるので、
					// 非同期処理をしてくれるクラスに処理を行ってもらいます。
					AsyncInsertRssRequest task = new AsyncInsertRssRequest(getActivity());
					task.execute(url);
					
				} catch (MalformedURLException e) {
					Log.e("APP", "URL の記述書式が間違っています");
					Log.e("APP", e.getMessage());

					label.setText("正しい URL を指定してください");
				} catch (Exception e) {
					Log.e("APP", "予期しないエラーにより、RSSの登録に失敗しました。エラーメッセージを確認して不具合を修正してください。");
					Log.e("APP", e.getMessage());
					label.setText("登録に失敗しました");
				}

            	Log.d("APP", "登録ボタンがクリックされた時の処理を実行が完了しました");
            }
        }
    }
}

    