package cn.csfz.crypto.asymmetric;

import cn.csfz.core.io.IORuntimeException;
import cn.csfz.core.io.IoUtil;
import cn.csfz.core.util.StrUtil;
import cn.csfz.crypto.SecureUtil;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 非对称解密器接口，提供：
 * <ul>
 *     <li>从bytes解密</li>
 *     <li>从Hex(16进制)解密</li>
 *     <li>从Base64解密</li>
 *     <li>从BCD解密</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.12
 */
public interface AsymmetricDecryptor {

	/**
	 * 解密
	 *
	 * @param bytes   被解密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 解密后的bytes
	 */
	byte[] decrypt(byte[] bytes, KeyType keyType);




}
