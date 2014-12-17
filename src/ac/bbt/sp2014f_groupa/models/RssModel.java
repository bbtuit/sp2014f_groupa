package ac.bbt.sp2014f_groupa.models;

import ac.bbt.sp2014f_groupa.SQLiteHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * データベースのRssesテーブルの一行をモデル化したクラス
 * 
 * @author ueharamasato
 */
public class RssModel {
	private long id;
	private String url;
	private String title;
	private String created_at;

	/**
	 * コンストラクタ
	 */
	public RssModel() {}
	
	/**
	 * コンストラクタ
	 */
	public RssModel(
			long id
			, String url
			, String title
			, String created_at
			) {
		this.setId(id);
		this.setUrl(url);
		this.setTitle(title);
		this.setCreatedAt(created_at);
	}
	
	/**
	 * 検索条件を元に DB からオブジェクトを生成する
	 * 
	 * @param	selection		検索条件
	 * @param	selectionArgs	検索条件で使用する値
	 * @return	該当データ
	 */
	public static RssModel findBy(String selection, String[] selectionArgs) throws NotFoundException {
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
                , selection
                , selectionArgs
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

        cursor.moveToFirst();
        RssModel rss = new RssModel(
        		cursor.getLong(0)
        		, cursor.getString(1)
        		, cursor.getString(2)
        		, cursor.getString(3)
        		);
        
        return rss;
	}

	/**
	 * id を元に DB からオブジェクトを生成する
	 * 
	 * @param	id	RSSのID
	 * @return
	 */
	public static RssModel findById(long id) throws NotFoundException {
		RssModel rss = RssModel.findBy("id=?", new String[] {String.valueOf(id)});
        
        return rss;
	}

	/**
	 * url を元に DB からオブジェクトを生成する
	 * 
	 * @param	id	RSSのURL
	 * @return
	 */
	public static RssModel findByUrl(String url) throws NotFoundException {
		RssModel rss = RssModel.findBy("url=?", new String[] {url});
        
        return rss;
	}
	
	/**
	 * id を元に該当データが存在するかをチェックする
	 * @param id	対象のID
	 * @return	存在する時は true, それ以外は false を返す
	 */
	public static boolean isIdExists(long id) {
		boolean result = true;
		try {
			RssModel.findById(id);
		} catch (NotFoundException e) {
			result = false;
		}
		
		return result;
	}

	/**
	 * url を元に該当データが存在するかをチェックする
	 * @param url	対象URL
	 * @return	存在する時は true, それ以外は false を返す
	 */
	public static boolean isUrlExists(String url) {
		boolean result = true;
		try {
			RssModel.findByUrl(url);
		} catch (NotFoundException e) {
			result = false;
		}
		
		return result;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreatedAt() {
		return created_at;
	}

	public void setCreatedAt(String created_at) {
		this.created_at = created_at;
	}
}
