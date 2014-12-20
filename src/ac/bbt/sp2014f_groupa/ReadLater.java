package ac.bbt.sp2014f_groupa;

import ac.bbt.sp2014f_groupa.MainActivity.PlaceholderFragment.ButtonClickListener;
import ac.bbt.sp2014f_groupa.MainActivity.PlaceholderFragment.ListItemClickListener;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.Build;

public class ReadLater extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// スーパークラスのonCreateメソッド呼び出し
		super.onCreate(savedInstanceState);
        // レイアウト設定ファイルの指定
		setContentView(R.layout.activity_read_later);
        // フラグメントの設定
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	    // SQLiteヘルパーを初期化する
        SQLiteHelper helper = new SQLiteHelper(this);

        // rssesの存在で初期化済みかを判断
        if (! helper.isTableExists("rsses")) {
            // テーブルが初期化されていないのなら、
            // 初期化とダミーデータの追加を行う
            helper.initTables();
            helper.initDummyData();
        }
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read_later, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
	 	SQLiteHelper helper = null;
    	SQLiteDatabase db = null;   	

		public PlaceholderFragment() {
		}
		
		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_read_later, container, false);
            
         // Homeボタンのクリックリスナー設定
            Button homeBtn = (Button)rootView.findViewById(R.id.bt_home);
            homeBtn.setTag("home");
            homeBtn.setOnClickListener(new ButtonClickListener());

            helper = SQLiteHelper.getInstance();
            
            
            // DB(読出用)のオブジェクト生成
        	db = helper.getWritableDatabase(); 	

    		/* DBデータ一覧表示 */
    		if (db != null) {
    			try {
    				// SQL文の実行
    				Cursor cursor = db.rawQuery("select * from articles", null);

    				// ListView初期化
    				ListView list = (ListView) rootView
    						.findViewById(android.R.id.list);
    				ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
    						getActivity(), android.R.layout.simple_list_item_1);

    				// カーソル開始位置を先頭にする
    				cursor.moveToFirst();

    				// DBデータ取得
    				for (int i = 0; i < 5; i++) {
    					// mAdapterにDBから文字列を追加
    					mAdapter.add(cursor.getString(3));    					
    					cursor.moveToNext();
    				}

    				// DBクローズ
    				cursor.close();
    				db.close();

    				// リストビューにアダプターをセット
    				list.setAdapter(mAdapter);

    			} catch (SQLException e) {
    				Log.e("TAG", "SQLExcepption:" + e.toString());
    			}
    		}

			return rootView;
		}
	       // クリックリスナー定義
        class ButtonClickListener implements OnClickListener {
            // onClickメソッド(ボタンクリック時イベントハンドラ)
            public void onClick(View v){
                // タsグの取得
                String tag = (String)v.getTag();

                // 登録ボタンが押された場合
                if(tag.equals("home")){
                	getActivity().finish();
                }
            }
        }
	}
}

