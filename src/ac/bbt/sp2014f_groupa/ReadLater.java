package ac.bbt.sp2014f_groupa;

import ac.bbt.sp2014f_groupa.MainActivity.PlaceholderFragment.ButtonClickListener;
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
import android.widget.Button;
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
    	private TextView mTitle;
		private Activity view;

		public PlaceholderFragment() {
		}
		
		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            
            helper = SQLiteHelper.getInstance();
        
            // DB(読出用)のオブジェクト生成
        	db = helper.getWritableDatabase();        	
        	/* DBデータ一覧表示 */
            if (db != null) 
            {
                try
                {
                	// SQL文の実行
                	Cursor cursor = db.rawQuery("select * from to_reads",null);
                	// カーソル開始位置を先頭にする
                	cursor.moveToFirst();
                	// DBデータ取得         
                	for (int i = 0; i < cursor.getCount(); i++) {
                		String title = cursor.getString(2);
                		cursor.moveToNext();
                	}
                	cursor.close();
                	db.close();        
                	// 画面表示
                	//mTitle = (TextView) rootView.findViewById(R.id.textView1); ここから2014/12/16
 
                    // テーブルレイアウトオブジェクト取得
                    TableLayout tablelayout = (TableLayout)getActivity().findViewById(R.id.tl_list);

                    // テーブルレイアウトのクリア
                    tablelayout.removeAllViews();

                	
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
                    }
                }catch(SQLException e) 
                {
                	Log.e("TAG", "SQLExcepption:"+e.toString()); 
                }               
            }
   
			return rootView;
		}
	}
}
