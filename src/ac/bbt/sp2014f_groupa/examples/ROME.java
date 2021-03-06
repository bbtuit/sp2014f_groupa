package ac.bbt.sp2014f_groupa.examples;

import java.io.Reader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;

import android.annotation.SuppressLint;

/**
 * ROME 1.0.0 - RSS取得ライブラリの使用サンプル
 * http://rometools.github.io/rome/ROMEReleases/ROME1.0Release.html
 * 
 * ※シンプルなJavaアプリケーション（≠Androidアプリケーション）を実行する場合は、
 * 　以下のサイトの記述を参考に実行方式を変更してください。
 * 　http://stackoverflow.com/questions/13030111/fatal-error-invalid-layout-of-java-lang-string-at-value
 *	・Project->Properties->Run/Debug Settings;
 *	・Select your Class and click "Edit";
 *	・Open the tab "Classpath" and remove Android Lib from "Bootstrap Entries";
 *	・Apply everything and Run the class again.
 */
@SuppressLint("SimpleDateFormat")
public class ROME {
	public static void main(String args[]) {
		ROME app = new ROME();

        app.testSyndFeedInput();
	}
	
	/**
	 * RSS Feedの取得テスト
	 */
	public void testSyndFeedInput() {
		// 日付フォーマット用のクラス
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分");
		
		// SyndFeedInputの生成
		SyndFeedInput input = new SyndFeedInput();
		Reader reader = null;
		
		try {
			// フィードの取得
            //URL url = new URL("http://rss.asahi.com/rss/asahi/newsheadlines.rdf");
            URL url = new URL("http://www.engadget.com/rss.xml");
            reader = new XmlReader(url.openStream());
            SyndFeed feed = input.build(reader);
            
            // タイトルを表示
            System.out.println(feed.getTitle());
            
            for (SyndEntry entry : (List<SyndEntry>) feed.getEntries()) {
            	System.out.println("==========");
            	
            	// エントリのタイトル
            	System.out.println(entry.getTitle());
            	// エントリの日付
            	System.out.println(sdf.format(entry.getPublishedDate()));
            	System.out.println(entry.getLink());
            	System.out.println(entry.getUri());
            	System.out.println("");
            	
            	// 概要（最初の５０文字だけ表示）
            	System.out.println(entry.getDescription().getValue());
            }
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
