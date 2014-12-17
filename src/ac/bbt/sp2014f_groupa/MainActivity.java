package ac.bbt.sp2014f_groupa;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	// onCreateメソッド(画面初期表示イベントハンドラ)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		// スーパークラスのonCreateメソッド呼び出し
    	super.onCreate(savedInstanceState);
        // レイアウト設定ファイルの指定
    	setContentView(R.layout.activity_main);
        // フラグメントの設定
    	if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
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
        getMenuInflater().inflate(R.menu.main, menu);
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
    	private TextView mTitle;
		private Activity view;
    	
        public PlaceholderFragment() { 	
        }

		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            //ここからなべさん

            // ボタンオブジェクト取得
            Button addRss = (Button)rootView.findViewById(R.id.bt_addRSS);
            Button readLater = (Button)rootView.findViewById(R.id.bt_read_later);

            // ボタンオブジェクトにクリックリスナー設定
            addRss.setOnClickListener((android.view.View.OnClickListener) new ButtonClickListener());
            readLater.setOnClickListener((android.view.View.OnClickListener) new ButtonClickListener());

            //ここまでなべさん            
            helper = SQLiteHelper.getInstance();
        
            // DB(読出用)のオブジェクト生成
        	db = helper.getWritableDatabase(); 	

			/* DBデータ一覧表示 */
			if (db != null) {
				try {
					// SQL文の実行
					Cursor cursor = db.rawQuery("select * from rsses", null);

					// ListView初期化
					ListView list = (ListView) rootView
							.findViewById(android.R.id.list);
					ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
							getActivity(), android.R.layout.simple_list_item_1);

					// カーソル開始位置を先頭にする
					cursor.moveToFirst();

					// DBデータ取得
					for (int i = 0; i < cursor.getCount(); i++) {
						// mAdapterにDBから文字列を追加
						mAdapter.add(cursor.getString(2));
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

        //ここからなべさん
        // ボタンクリックリスナー定義
        class ButtonClickListener implements OnClickListener {
            // onClickメソッド(ボタンクリック時イベントハンドラ)
            public void onClick(View v) {
                switch(v.getId()){
                case R.id.bt_addRSS:
                    // インテントの生成(呼び出すクラスの指定)
                    Intent intent = new Intent(getActivity(), AddRssActivity.class);
                    // 次のアクティビティの起動
                    startActivity(intent);
                    break;
                case R.id.bt_read_later:
                    Intent intent1 = new Intent(getActivity(), ReadLater.class);
                    // 次のアクティビティの起動
                    startActivity(intent1);
                    break;
                }
           }
        }
    }
}


