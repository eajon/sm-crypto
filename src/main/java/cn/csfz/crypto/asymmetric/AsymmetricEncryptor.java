package cn.csfz.crypto.asymmetric;

import cn.csfz.core.io.IORuntimeException;
import cn.csfz.core.io.IoUtil;
import cn.csfz.core.util.HexUtil;
import cn.csfz.core.util.StrUtil;
import org.bouncycastle.util.encoders.Base64;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 非对称加密器接口，提供：
 * <ul>
 *     <li>加密为bytes</li>
 *     <li>加密为Hex(16进制)</li>
 *     <li>加密为Base64</li>
 *     <li>加密为BCD</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.12
 */
public interface AsymmetricEncryptor {

	/**
	 * 加密
	 *
	 * @param data    被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	byte[] encrypt(byte[] data, KeyType keyType);



}
