package ac.bbt.sp2014f_groupa.models;

/**
 * IDからモデルオブジェクトを生成するときに、
 * 該当のデータがないときに発行される例外
 * 
 * @author ueharamasato
 */
public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 */
	public NotFoundException() {
		super("該当データがありません");
	}

	public NotFoundException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NotFoundException(String detailMessage) {
		super(detailMessage);
	}

	public NotFoundException(Throwable throwable) {
		super(throwable);
	}
}
