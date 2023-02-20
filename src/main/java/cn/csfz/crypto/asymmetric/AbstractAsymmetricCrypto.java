package cn.csfz.crypto.asymmetric;

import cn.csfz.core.io.IORuntimeException;
import cn.csfz.core.io.IoUtil;
import cn.csfz.core.util.HexUtil;
import cn.csfz.core.util.StrUtil;
import cn.csfz.crypto.SecureUtil;
import org.bouncycastle.util.encoders.Base64;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 抽象的非对称加密对象，包装了加密和解密为Hex和Base64的封装
 *
 * @param <T> 返回自身类型
 * @author Looly
 */
public abstract class AbstractAsymmetricCrypto<T extends AbstractAsymmetricCrypto<T>>
		extends BaseAsymmetric<T>
		implements AsymmetricEncryptor, AsymmetricDecryptor{
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------ Constructor start
	/**
	 * 构造
	 * <p>
	 * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
	 * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
	 *
	 * @param algorithm  算法
	 * @param privateKey 私钥
	 * @param publicKey  公钥
	 * @since 3.1.1
	 */
	public AbstractAsymmetricCrypto(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
		super(algorithm, privateKey, publicKey);
	}
	// ------------------------------------------------------------------ Constructor end

	/**
	 * 解密
	 *
	 * @param data    被解密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 解密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	public byte[] decrypt(InputStream data, KeyType keyType) throws IORuntimeException {
		return decrypt(IoUtil.readBytes(data), keyType);
	}

	/**
	 * 从Hex或Base64字符串解密，编码为UTF-8格式
	 *
	 * @param data    Hex（16进制）或Base64字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 解密后的bytes
	 * @since 4.5.2
	 */
	public byte[] decrypt(String data, KeyType keyType) {
		return decrypt(SecureUtil.decode(data), keyType);
	}

	/**
	 * 解密为字符串，密文需为Hex（16进制）或Base64字符串
	 *
	 * @param data    数据，Hex（16进制）或Base64字符串
	 * @param keyType 密钥类型
	 * @param charset 加密前编码
	 * @return 解密后的密文
	 * @since 4.5.2
	 */
	public String decryptStr(String data, KeyType keyType, Charset charset) {
		return StrUtil.str(decrypt(data, keyType), charset);
	}

	/**
	 * 解密为字符串，密文需为Hex（16进制）或Base64字符串
	 *
	 * @param data    数据，Hex（16进制）或Base64字符串
	 * @param keyType 密钥类型
	 * @return 解密后的密文
	 * @since 4.5.2
	 */
	public String decryptStr(String data, KeyType keyType) {
		return decryptStr(data, keyType, Charset.forName("UTF-8"));
	}

	/**
	 * 编码为Hex字符串
	 *
	 * @param data    被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Hex字符串
	 */
	public String encryptHex(byte[] data, KeyType keyType) {
		return HexUtil.encodeHexStr(encrypt(data, keyType));
	}

	/**
	 * 编码为Base64字符串
	 *
	 * @param data    被加密的bytes
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Base64字符串
	 * @since 4.0.1
	 */
	public String encryptBase64(byte[] data, KeyType keyType) {
		return new String(Base64.encode(encrypt(data, keyType)));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(String data, String charset, KeyType keyType) {
		return encrypt(StrUtil.bytes(data, charset), keyType);
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(String data, Charset charset, KeyType keyType) {
		return encrypt(StrUtil.bytes(data, charset), keyType);
	}

	/**
	 * 加密，使用UTF-8编码
	 *
	 * @param data    被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 */
	public byte[] encrypt(String data, KeyType keyType) {
		return encrypt(StrUtil.utf8Bytes(data), keyType);
	}

	/**
	 * 编码为Hex字符串
	 *
	 * @param data    被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Hex字符串
	 * @since 4.0.1
	 */
	public String encryptHex(String data, KeyType keyType) {
		return HexUtil.encodeHexStr(encrypt(data, keyType));
	}

	/**
	 * 编码为Hex字符串
	 *
	 * @param data    被加密的bytes
	 * @param charset 编码
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Hex字符串
	 * @since 4.0.1
	 */
	public String encryptHex(String data, Charset charset, KeyType keyType) {
		return HexUtil.encodeHexStr(encrypt(data, charset, keyType));
	}

	/**
	 * 编码为Base64字符串，使用UTF-8编码
	 *
	 * @param data    被加密的字符串
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Base64字符串
	 * @since 4.0.1
	 */
	public String encryptBase64(String data, KeyType keyType) {
		return new String(Base64.encode(encrypt(data, keyType)));
	}

	/**
	 * 编码为Base64字符串
	 *
	 * @param data    被加密的字符串
	 * @param charset 编码
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Base64字符串
	 * @since 4.0.1
	 */
	public String encryptBase64(String data, Charset charset, KeyType keyType) {
		return new String(Base64.encode(encrypt(data, charset, keyType)));
	}

	/**
	 * 加密
	 *
	 * @param data    被加密的数据流
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return 加密后的bytes
	 * @throws IORuntimeException IO异常
	 */
	public byte[] encrypt(InputStream data, KeyType keyType) throws IORuntimeException {
		return encrypt(IoUtil.readBytes(data), keyType);
	}

	/**
	 * 编码为Hex字符串
	 *
	 * @param data    被加密的数据流
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Hex字符串
	 * @since 4.0.1
	 */
	public String encryptHex(InputStream data, KeyType keyType) {
		return HexUtil.encodeHexStr(encrypt(data, keyType));
	}

	/**
	 * 编码为Base64字符串
	 *
	 * @param data    被加密的数据流
	 * @param keyType 私钥或公钥 {@link KeyType}
	 * @return Base64字符串
	 * @since 4.0.1
	 */
	public String encryptBase64(InputStream data, KeyType keyType) {
		return new String(Base64.encode(encrypt(data, keyType)));
	}
}
