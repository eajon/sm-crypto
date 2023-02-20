package cn.csfz.crypto;

import cn.csfz.core.util.StrUtil;

/**
 * 加密异常
 * @author Looly
 *
 */
public class CryptoException extends RuntimeException {
	private static final long serialVersionUID = 8068509879445395353L;

	public CryptoException(Throwable e) {
		super(getMessage(e), e);
	}

	public CryptoException(String message) {
		super(message);
	}

	public CryptoException(String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params));
	}

	public CryptoException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public CryptoException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
	}

	public CryptoException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrUtil.format(messageTemplate, params), throwable);
	}

	/**
	 * 获得完整消息，包括异常名，消息格式为：{SimpleClassName}: {ThrowableMessage}
	 *
	 * @param e 异常
	 * @return 完整消息
	 */
	public static String getMessage(Throwable e) {
		if (null == e) {
			return "";
		}
		return StrUtil.format("{}: {}", e.getClass().getSimpleName(), e.getMessage());
	}
}
