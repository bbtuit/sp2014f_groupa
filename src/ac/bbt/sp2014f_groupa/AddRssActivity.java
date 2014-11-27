package ac.bbt.sp2014f_groupa;

import ac.bbt.sp2014f_groupa.MainActivity.PlaceholderFragment;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
    
        // onCreateViewメソッド(フラグメント初期表示イベントハンドラ)
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	// フラグメント設定情報取得
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            
            // 登録ボタンのクリックリスナー設定
            Button insertBtn = (Button)rootView.findViewById(R.id.bt_insert);
            insertBtn.setTag("insert");
            insertBtn.setOnClickListener(new ButtonClickListener());
            
    }
        
           SQLiteDatabase db = helper.getWritableDatabase();
           ContentValues val = new ContentValues();
           val.put("url", "...");
           val.put("title", "...");
           val.put("created_at", "2014-11-23 09:00:00");
           db.update("rssies", val, "id = ?", new String[] {Integer.toString(rss.getId())});

               }


 }
