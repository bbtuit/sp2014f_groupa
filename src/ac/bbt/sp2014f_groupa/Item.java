package ac.bbt.sp2014f_groupa;

public class Item {
	// 記事のタイトル
		private CharSequence mTitle;
		// 記事の本文
		private CharSequence mURL;

		public Item() {
			mTitle = "";
			mURL = "";
		}

		public CharSequence getTitle() {
			return mTitle;
		}

		public void setTitle(CharSequence title) {
			mTitle = title;
		}

		public CharSequence getURL() {
			return mURL;
		}

		public void setURL(CharSequence URL) {
			mURL = URL;
		}

}
