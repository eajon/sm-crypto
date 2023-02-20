package cn.csfz.crypto.digest.mac;

import cn.csfz.crypto.SmUtil;
import cn.csfz.crypto.digest.HmacAlgorithm;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * {@link MacEngine} 实现工厂类
 *
 * @author Looly
 * @since 4.5.13
 */
public class MacEngineFactory {

	/**
	 * 根据给定算法和密钥生成对应的{@link MacEngine}
	 *
	 * @param algorithm 算法，见{@link HmacAlgorithm}
	 * @param key       密钥
	 * @return {@link MacEngine}
	 */
	public static MacEngine createEngine(String algorithm, Key key) {
		return createEngine(algorithm, key, null);
	}

	/**
	 * 根据给定算法和密钥生成对应的{@link MacEngine}
	 *
	 * @param algorithm 算法，见{@link HmacAlgorithm}
	 * @param key       密钥
	 * @param spec      spec
	 * @return {@link MacEngine}
	 * @since 5.7.12
	 */
	public static MacEngine createEngine(String algorithm, Key key, AlgorithmParameterSpec spec) {
		return new DefaultHMacEngine(algorithm, key, spec);
	}
}
