package cn.csfz.core.text;

import cn.csfz.core.util.CharUtil;

/**
 * 字符查找器<br>
 * 查找指定字符在字符串中的位置信息
 *
 * @author looly
 * @since 5.7.14
 */
public class CharFinder extends TextFinder {
	private static final long serialVersionUID = 1L;

	private final char c;
	private final boolean caseInsensitive;

	/**
	 * 构造
	 *
	 * @param c               被查找的字符
	 * @param caseInsensitive 是否忽略大小写
	 */
	public CharFinder(char c, boolean caseInsensitive) {
		this.c = c;
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public int start(int from) {
		final int limit = getValidEndIndex();
		if(negative){
			for (int i = from; i > limit; i--) {
				if (CharUtil.equals(c, text.charAt(i), caseInsensitive)) {
					return i;
				}
			}
		} else{
			for (int i = from; i < limit; i++) {
				if (CharUtil.equals(c, text.charAt(i), caseInsensitive)) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public int end(int start) {
		if (start < 0) {
			return -1;
		}
		return start + 1;
	}
}
