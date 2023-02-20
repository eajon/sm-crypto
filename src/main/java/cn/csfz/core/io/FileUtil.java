package cn.csfz.core.io;

import cn.csfz.core.util.ArrayUtil;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * 文件工具类
 *
 * @author looly
 */
public class FileUtil  {


	/**
	 * 通过多层目录参数创建文件<br>
	 * 此方法会检查slip漏洞，漏洞说明见http://blog.nsfocus.net/zip-slip-2/
	 *
	 * @param directory 父目录
	 * @param names     元素名（多层目录名），由外到内依次传入
	 * @return the file 文件
	 * @since 4.0.6
	 */
	public static File file(File directory, String... names) {
		if (ArrayUtil.isEmpty(names)) {
			return directory;
		}

		File file = directory;
		for (String name : names) {
			if (null != name) {
				file = file(file, name);
			}
		}
		return file;
	}

	/**
	 * 通过多层目录创建文件
	 * <p>
	 * 元素名（多层目录名）
	 *
	 * @param names 多层文件的文件名，由外到内依次传入
	 * @return the file 文件
	 * @since 4.0.6
	 */
	public static File file(String... names) {
		if (ArrayUtil.isEmpty(names)) {
			return null;
		}

		File file = null;
		for (String name : names) {
			if (file == null) {
				file = file(name);
			} else {
				file = file(file, name);
			}
		}
		return file;
	}


	/**
	 * 判断文件是否存在，如果file为null，则返回false
	 *
	 * @param file 文件
	 * @return 如果存在返回true
	 */
	public static boolean exist(File file) {
		return (null != file) && file.exists();
	}


	// -------------------------------------------------------------------------------------------- in start

	/**
	 * 获得输入流
	 *
	 * @param file 文件
	 * @return 输入流
	 * @throws IORuntimeException 文件未找到
	 */
	public static BufferedInputStream getInputStream(File file) throws IORuntimeException {
		return IoUtil.toBuffered(IoUtil.toStream(file));
	}

	/**
	 * 获得BOM输入流，用于处理带BOM头的文件
	 *
	 * @param file 文件
	 * @return 输入流
	 * @throws IORuntimeException 文件未找到
	 */
	public static BOMInputStream getBOMInputStream(File file) throws IORuntimeException {
		try {
			return new BOMInputStream(new FileInputStream(file));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得一个文件读取器
	 *
	 * @param file        文件
	 * @param charsetName 字符集
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 * @deprecated 请使用 {@link #getReader(File, Charset)}
	 */
	@Deprecated
	public static BufferedReader getReader(File file, String charsetName) throws IORuntimeException {
		return IoUtil.getReader(getInputStream(file), Charset.forName(charsetName));
	}

	/**
	 * 获得一个文件读取器
	 *
	 * @param file    文件
	 * @param charset 字符集
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedReader getReader(File file, Charset charset) throws IORuntimeException {
		return IoUtil.getReader(getInputStream(file), charset);
	}

	/**
	 * 获得一个文件读取器
	 *
	 * @param path        绝对路径
	 * @param charsetName 字符集
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 * @deprecated 请使用 {@link #getReader(String, Charset)}
	 */
	@Deprecated
	public static BufferedReader getReader(String path, String charsetName) throws IORuntimeException {
		return getReader(path, Charset.forName(charsetName));
	}

	/**
	 * 获得一个文件读取器
	 *
	 * @param path    绝对路径
	 * @param charset 字符集
	 * @return BufferedReader对象
	 * @throws IORuntimeException IO异常
	 */
	public static BufferedReader getReader(String path, Charset charset) throws IORuntimeException {
		return getReader(file(path), charset);
	}

	// -------------------------------------------------------------------------------------------- in end

	/**
	 * 读取文件内容
	 *
	 * @param file        文件
	 * @param charsetName 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 * @deprecated 请使用 {@link #readString(File, Charset)}
	 */
	@Deprecated
	public static String readString(File file, String charsetName) throws IORuntimeException {
		return readString(file, Charset.forName(charsetName));
	}

	/**
	 * 读取文件内容
	 *
	 * @param file    文件
	 * @param charset 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String readString(File file, Charset charset) throws IORuntimeException {
		return FileReader.create(file, charset).readString();
	}

	/**
	 * 读取文件内容
	 *
	 * @param path        文件路径
	 * @param charsetName 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 * @deprecated 请使用 {@link #readString(String, Charset)}
	 */
	@Deprecated
	public static String readString(String path, String charsetName) throws IORuntimeException {
		return readString(path, Charset.forName(charsetName));
	}

	/**
	 * 读取文件内容
	 *
	 * @param path    文件路径
	 * @param charset 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String readString(String path, Charset charset) throws IORuntimeException {
		return readString(file(path), charset);
	}

	/**
	 * 读取文件内容
	 *
	 * @param url         文件URL
	 * @param charsetName 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 * @deprecated 请使用 {@link #readString(URL, Charset)}
	 */
	@Deprecated
	public static String readString(URL url, String charsetName) throws IORuntimeException {
		return readString(url, Charset.forName(charsetName));
	}

	/**
	 * 读取文件内容
	 *
	 * @param url     文件URL
	 * @param charset 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 * @since 5.7.10
	 */
	public static String readString(URL url, Charset charset) throws IORuntimeException {
		if (url == null) {
			throw new NullPointerException("Empty url provided!");
		}

		InputStream in = null;
		try {
			in = url.openStream();
			return IoUtil.read(in, charset);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 从文件中读取每一行数据
	 *
	 * @param <T>        集合类型
	 * @param file       文件路径
	 * @param charset    字符集
	 * @param collection 集合
	 * @return 文件中的每行内容的集合
	 * @throws IORuntimeException IO异常
	 */
	public static <T extends Collection<String>> T readLines(File file, String charset, T collection) throws IORuntimeException {
		return FileReader.create(file,Charset.forName(charset)).readLines(collection);
	}


}
