package cn.csfz.crypto.symmetric;

import cn.csfz.core.io.IORuntimeException;
import cn.csfz.core.io.IoUtil;
import cn.csfz.core.util.StrUtil;
import cn.csfz.crypto.SecureUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 对称解密器接口，提供：
 * <ul>
 *     <li>从bytes解密</li>
 *     <li>从Hex(16进制)解密</li>
 *     <li>从Base64解密</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.12
 */
public interface SymmetricDecryptor {
	/**
	 * 解密
	 *
	 * @param bytes 被解密的bytes
	 * @return 解密后的bytes
	 */
	byte[] decrypt(byte[] bytes);

	/**
	 * 解密，针对大数据量，结束后不关闭流
	 *
	 * @param data    加密的字符串
	 * @param out     输出流，可以是文件或网络位置
	 * @param isClose 是否关闭流，包括输入和输出流
	 * @throws IORuntimeException IO异常
	 */
	void decrypt(InputStream data, OutputStream out, boolean isClose);


}
