package cn.csfz.core.util;

/**
 * 原始类型数组工具类
 *
 * @author looly
 * @since 5.5.2
 */
public class PrimitiveArrayUtil {
	/**
	 * 数组中元素未找到的下标，值为-1
	 */
	public static final int INDEX_NOT_FOUND = -1;

	// ---------------------------------------------------------------------- isEmpty

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(long[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(int[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(short[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(char[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(byte[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(double[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(float[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(boolean[] array) {
		return array == null || array.length == 0;
	}

	// ---------------------------------------------------------------------- isNotEmpty

	/**
	 * 数组是否为非空
	 *
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(byte[] array) {
		return false == isEmpty(array);
	}



	// ---------------------------------------------------------------------- resize

	/**
	 * 生成一个新的重新设置大小的数组<br>
	 * 调整大小后拷贝原数组到新数组下。扩大则占位前N个位置，其它位置补充0，缩小则截断
	 *
	 * @param bytes   原数组
	 * @param newSize 新的数组大小
	 * @return 调整后的新数组
	 * @since 4.6.7
	 */
	public static byte[] resize(byte[] bytes, int newSize) {
		if (newSize < 0) {
			return bytes;
		}
		final byte[] newArray = new byte[newSize];
		if (newSize > 0 && isNotEmpty(bytes)) {
			System.arraycopy(bytes, 0, newArray, 0, Math.min(bytes.length, newSize));
		}
		return newArray;
	}

	// ---------------------------------------------------------------------- addAll

	/**
	 * 将多个数组合并在一起<br>
	 * 忽略null的数组
	 *
	 * @param arrays 数组集合
	 * @return 合并后的数组
	 * @since 4.6.9
	 */
	public static byte[] addAll(byte[]... arrays) {
		if (arrays.length == 1) {
			return arrays[0];
		}

		// 计算总长度
		int length = 0;
		for (byte[] array : arrays) {
			if (null != array) {
				length += array.length;
			}
		}

		final byte[] result = new byte[length];
		length = 0;
		for (byte[] array : arrays) {
			if (null != array) {
				System.arraycopy(array, 0, result, length, array.length);
				length += array.length;
			}
		}
		return result;
	}


	/**
	 * 返回数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.0.7
	 */
	public static int indexOf(char[] array, char value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}


	/**
	 * 数组中是否包含元素
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 * @since 3.0.7
	 */
	public static boolean contains(char[] array, char value) {
		return indexOf(array, value) > INDEX_NOT_FOUND;
	}
}
