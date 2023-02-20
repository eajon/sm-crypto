package cn.csfz.crypto.symmetric;


import cn.csfz.core.io.IORuntimeException;
import cn.csfz.core.io.IoUtil;
import cn.csfz.core.util.HexUtil;
import cn.csfz.core.util.StrUtil;
import org.bouncycastle.util.encoders.Base64;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 对称加密器接口，提供：
 * <ul>
 *     <li>加密为bytes</li>
 *     <li>加密为Hex(16进制)</li>
 *     <li>加密为Base64</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.12
 */
public interface SymmetricEncryptor {

	/**
	 * 加密
	 *
	 * @param data 被加密的bytes
	 * @return 加密后的bytes
	 */
	byte[] encrypt(byte[] data);

	/**
	 * 加密，针对大数据量，可选结束后是否关闭流
	 *
	 * @param data    被加密的字符串
	 * @param out     输出流，可以是文件或网络位置
	 * @param isClose 是否关闭流
	 * @throws IORuntimeException IO异常
	 */
	void encrypt(InputStream data, OutputStream out, boolean isClose);


}
