package ac.bbt.sp2014f_groupa;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class SQLiteHelper extends SQLiteOpenHelper {
    private static SQLiteHelper self;
    public static final String DB_NAME = "dbrss";

    // コンストラクタ定義
    public SQLiteHelper(Context con){
        // SQLiteOpenHelperのコンストラクタ呼び出し
        super(con, DB_NAME,null,1);
        
        SQLiteHelper.self = this;

        Log.d("APP", "初期化しました。");
    }
    
    /**
     * RSSの登録処理
     */
    public long insertRSS(URL url) {
    	Log.d("APP", "Feedの登録処理を開始します");
    	
    	long id = 0;
    	try {
            SyndFeed feed = this.getRSS(url);

            // トランザクション制御開始
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            
            // 日付の取得
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // 登録データ設定
            ContentValues val = new ContentValues();
            val.put("url", url.toString());
            val.put("title", feed.getTitle());
            val.put("created_at", sdf.format(now));

            // データ登録
            id = db.insert("rsses", null, val);

            // コミット
            db.setTransactionSuccessful();

            // トランザクション制御終了
            db.endTransaction();

            Log.d("APP", "Feedの登録に成功しました（URL:" + url + "）");
    	} catch (Exception e) {
    		Log.e(
    				"APP"
    				, "フィードの登録に失敗しました(URL: "
    					+ url
    					+ ")"
    				);
    		Log.e("APP", e.getClass().getName());
    		Log.e("APP", e.getMessage());
    	}
    	
    	return id;
    }

    /**
     * RSSの取得処理
     * 指定されたURLのRSS Feedを取得して返します。
     * 
     * @param	url	RSSのURL
     * @return	値取得済みのSyndFeed
     * @throws IOException 
     * @throws FeedException 
     * @throws IllegalArgumentException 
     */
    public SyndFeed getRSS(URL url)
    		throws
    			IOException
    			, IllegalArgumentException
    			, FeedException
    {
    	Log.d("APP", "Feedの取得処理を開始します");

		// SyndFeedInputの生成
		SyndFeedInput input = new SyndFeedInput();
		Reader reader = null;
        SyndFeed feed = null;
		
        try {
            // フィードの取得
            reader = new XmlReader(url.openStream());
            feed = input.build(reader);
        } catch (IOException e) {
        	Log.e("APP", "URLからデータの読み込みに失敗しました");
        	Log.e("APP", e.getMessage());
        	throw e;
        } catch (IllegalArgumentException e) {
        	Log.e("APP", "IllegalArgumentExceptionが発生しました");
        	Log.e("APP", e.getMessage());
        	throw e;
        } catch (FeedException e) {
        	Log.e("APP", "FeedExceptionが発生しました");
        	Log.e("APP", e.getMessage());
        	throw e;
        }
        
        Log.d("APP", "Feedの取得に成功しました");
		
		return feed;
    }
    
    /**
     * RSS URLの重複をチェックする
     * 
     * @param	url	チェックするURL
     * @return	重複している true, それ以外は false を返す。
     */
    public boolean isRssUrlDuplicated(String url) {
    	Log.d("APP", "RSSの重複チェックを開始します（URL:" + url + "）");
    	boolean result = false;
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	Cursor cursor = db.rawQuery("SELECT COUNT(id) FROM rsses WHERE url=? GROUP BY url", new String[] {url});

    	if (cursor.getCount() > 0) {
            cursor.moveToLast();
            int cnt = cursor.getInt(0);
            
            if (cnt > 0) {
                result = true;
                Log.d("APP", "RSSは重複しています");
            } else {
                Log.d("APP", "RSSは重複してません");
            }
    	} else {
    		Log.d("APP", "RSSは重複してません");
    	}

    	return result;
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    /**
     * SQLiteHelperの取得メソッド。
     * 
     * 使い方：
     * // 予めActivetyのonCreateで初期化を行っておき、
     * // その後、使いたいところで下記のように呼び出して使用する
     * SQLiteHelper helper = SQLiteHelper.getInstance();
     * 
     * SQLiteDatabase db = helper.getWritableDatabase();
     * ContentValues val = new ContentValues();
     * val.put("url", "...");
     * val.put("title", "...");
     * val.put("created_at", "2014-11-23 09:00:00");
     * db.update("rssies", val, "id = ?", new String[] {Integer.toString(rss.getId())});
     * 
     * @return SQLiteHelperのインスタンス
     */
    public static SQLiteHelper getInstance()
    {
        if (SQLiteHelper.self == null) {
            Log.d("APP", "初期化前に呼び出されました。");
        }
        return SQLiteHelper.self;
    }

    /**
     * テーブルの存在をチェックする。
     * 
     * @param table チェック対象のテーブル名
     * @return テーブルが存在する時は true, それ以外は false を返す。
     */
    public boolean isTableExists(String table) {
        Log.d("APP", table + " テーブルの存在をチェック・・・");
        boolean is_table_exists = false;

        SQLiteDatabase db = getReadableDatabase();
    	
        try {
            String sql = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name=?";
            String selectionArgs[] = {table};
            Log.d("APP", "SQL：" + sql);
            Cursor c = db.rawQuery(sql, selectionArgs);
            c.moveToFirst();
            int cnt = c.getInt(0);
            Log.d("APP", "テーブルの数を取得 => " + cnt);
            is_table_exists = (cnt != 0);
            
            if (is_table_exists) {
                Log.d("APP", table + " テーブルは既に存在しています。");
            }
        } catch (Exception e) {
            // XXX: 存在チェックに失敗する場合、アプリケーションを停止するなりの処理が必要
            Log.e("APP", "テーブルの存在チェックに失敗しました。");
            Log.e("APP", e.getMessage());
        }
    	
        return is_table_exists;
    }
    
    /**
     * テーブルの初期化メソッド
     * 
     * @return none
     */
    public void initTables() {
        String sql;
        SQLiteDatabase db = getWritableDatabase();
    	
        try {
            sql = "CREATE TABLE rsses"
            + "("
            + "    id INTEGER PRIMARY KEY ASC AUTOINCREMENT"
            + "    , url TEXT"
            + "    , title TEXT"
            + "    , created_at TEXT"
            + ");";
            db.execSQL(sql);

            sql = "CREATE TABLE articles"
            + "("
            + "    id INTEGER PRIMARY KEY ASC AUTOINCREMENT"
            + "    , rss_id INTEGER"
            + "    , url TEXT"
            + "    , title TEXT"
            + "    , published_at TEXT"
            + "    , created_at TEXT"
            + ");";
            db.execSQL(sql);

            sql = "CREATE TABLE to_reads"
            + "("
            + "    id INTEGER PRIMARY KEY ASC AUTOINCREMENT"
            + "    , article_id INTEGER"
            + "    , created_at TEXT"
            + ");";
            db.execSQL(sql);

        } catch (Exception e) {
            // FixMe: テーブル作成に失敗する場合、アプリケーションを停止するなりの処理が必要
            Log.e("APP", "テーブルの初期化に失敗しました。");
            Log.e("APP", e.getMessage());
        }
    }

    /**
     * ダミーデータの初期化メソッド
     * 
     * @return none
     */
    public void initDummyData() {
        SQLiteDatabase db = getWritableDatabase();
    	
        try {
            db.execSQL("INSERT INTO rsses (url, title, created_at) VALUES ('http://rss.asahi.com/rss/asahi/newsheadlines.rdf', '朝日新聞デジタル', '2014-11-23 09:00:00');");
            db.execSQL("INSERT INTO rsses (url, title, created_at) VALUES ('http://rss.rssad.jp/rss/mainichi/flash.rss', '毎日新聞 - ニュース速報(総合)', '2014-11-23 10:00:00');");
            db.execSQL("INSERT INTO rsses (url, title, created_at) VALUES ('http://www.nikkan.co.jp/rss/nksrdf.rdf', '日刊工業新聞社　BusinessLine', '2014-11-23 11:00:00');");

            db.execSQL("INSERT INTO articles (rss_id, url, title, published_at, created_at) VALUES (1, 'http://www.asahi.com/articles/ASGCR2JZWGCRUTIL003.html?ref=rss', '長野県北部の地震、けが人５７人に　骨折した重傷者も','2014-11-23 08:22:09','2014-11-23 08:22:09');");
            db.execSQL("INSERT INTO articles (rss_id, url, title, published_at, created_at) VALUES (1, 'http://www.asahi.com/articles/ASGCQ759CGCQPFIB00W.html?ref=rss', '入所者の髪の毛引き抜いた疑い　介護施設職員を逮捕','2014-11-23 03:55:32','2014-11-23 03:55:32');");
            db.execSQL("INSERT INTO articles (rss_id, url, title, published_at, created_at) VALUES (1, 'http://www.asahi.com/articles/ASGCQ7F9YGCQUTIL02J.html?ref=rss', '長野北部で震度６弱、けが３０人超か　倒壊民家で救助も','2014-11-23 02:28:22','2014-11-23 02:28:22');");
            db.execSQL("INSERT INTO articles (rss_id, url, title, published_at, created_at) VALUES (1, 'http://www.asahi.com/articles/ASGCR03VMGCQTNAB00Q.html?ref=rss', 'マイクロバスと乗用車が衝突、１４人けが　宮崎の国道','2014-11-23 02:10:07','2014-11-23 02:10:07');");
            db.execSQL("INSERT INTO articles (rss_id, url, title, published_at, created_at) VALUES (2, 'http://www.asahi.com/articles/ASGCR02YQGCQULBJ00K.html?ref=rss', '震度５強程度の余震の恐れ　長野北部、１週間は注意を','2014-11-23 01:24:19','2014-11-23 01:24:19');");
            db.execSQL("INSERT INTO articles (rss_id, url, title, published_at, created_at) VALUES (2, 'http://www.asahi.com/articles/ASGCR04CZGCQUTIL047.html?ref=rss', '青森・田子で住宅火災　３人の遺体見つかる','2014-11-23 01:05:31','2014-11-23 01:05:31');");
            db.execSQL("INSERT INTO articles (rss_id, url, title, published_at, created_at) VALUES (2, 'http://www.asahi.com/articles/ASGCQ7HXJGCQUTIL02W.html?ref=rss', '「ドン」と突き上げる揺れが…　震度５強の白馬村','2014-11-23 00:58:38','2014-11-23 00:58:38');");
            db.execSQL("INSERT INTO articles (rss_id, url, title, published_at, created_at) VALUES (3, 'http://www.asahi.com/articles/ASGCQ4GBFGCQUQIP010.html?ref=rss', '目黒川、青くきらめく　４０万個のＬＥＤ','2014-11-23 00:03:46','2014-11-23 00:03:46');");
            db.execSQL("INSERT INTO articles (rss_id, url, title, published_at, created_at) VALUES (3, 'http://www.asahi.com/articles/ASGCQ44N8GCQUGTB00B.html?ref=rss', '福島・川内村で原発事故後初の訓練　「恐怖思い出した」','2014-11-23 23:46:39','2014-11-23 23:46:39');");

            db.execSQL("INSERT INTO to_reads (article_id, created_at) VALUES (1, '2014-11-23 09:00:00');");
            db.execSQL("INSERT INTO to_reads (article_id, created_at) VALUES (2, '2014-11-23 10:00:00');");
            db.execSQL("INSERT INTO to_reads (article_id, created_at) VALUES (3, '2014-11-23 11:00:00');");
            db.execSQL("INSERT INTO to_reads (article_id, created_at) VALUES (4, '2014-11-23 12:00:00');");
            db.execSQL("INSERT INTO to_reads (article_id, created_at) VALUES (5, '2014-11-23 13:00:00');");

        } catch (Exception e) {
            // FixMe: ダミーデータの登録に失敗する場合、アプリケーションを停止するなりの処理が必要
            Log.e("APP", "ダミーデータの初期化に失敗しました。");
            Log.e("APP", e.getMessage());
        }
    }
}
