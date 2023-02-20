package cn.csfz.core.io;

import cn.csfz.core.io.copy.ReaderWriterCopier;
import cn.csfz.core.util.HexUtil;
import cn.csfz.core.util.StrUtil;


import java.io.*;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * IO工具类<br>
 * IO工具类只是辅助流的读写，并不负责关闭流。原因是流可能被多次读写，读写关闭后容易造成问题。
 *
 * @author xiaoleilu
 */
public class IoUtil extends NioUtil {

	// -------------------------------------------------------------------------------------- Copy start

	/**
	 * 将Reader中的内容复制到Writer中 使用默认缓存大小，拷贝后不关闭Reader
	 *
	 * @param reader Reader
	 * @param writer Writer
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer) throws IORuntimeException {
		return copy(reader, writer, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
	 *
	 * @param reader     Reader
	 * @param writer     Writer
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize) throws IORuntimeException {
		return copy(reader, writer, bufferSize, null);
	}

	/**
	 * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
	 *
	 * @param reader         Reader
	 * @param writer         Writer
	 * @param bufferSize     缓存大小
	 * @param streamProgress 进度处理器
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
		return copy(reader, writer, bufferSize, -1, streamProgress);
	}

	/**
	 * 将Reader中的内容复制到Writer中，拷贝后不关闭Reader
	 *
	 * @param reader         Reader
	 * @param writer         Writer
	 * @param bufferSize     缓存大小
	 * @param count          最大长度
	 * @param streamProgress 进度处理器
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize, long count, StreamProgress streamProgress) throws IORuntimeException {
		return new ReaderWriterCopier(bufferSize, count, streamProgress).copy(reader, writer);
	}

	/**
	 * 拷贝流，使用默认Buffer大小，拷贝后不关闭流
	 *
	 * @param in  输入流
	 * @param out 输出流
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out) throws IORuntimeException {
		return copy(in, out, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 拷贝流，拷贝后不关闭流
	 *
	 * @param in         输入流
	 * @param out        输出流
	 * @param bufferSize 缓存大小
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize) throws IORuntimeException {
		return copy(in, out, bufferSize, null);
	}

	/**
	 * 拷贝流，拷贝后不关闭流
	 *
	 * @param in             输入流
	 * @param out            输出流
	 * @param bufferSize     缓存大小
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
		return copy(in, out, bufferSize, -1, streamProgress);
	}

	/**
	 * 拷贝流，拷贝后不关闭流
	 *
	 * @param in             输入流
	 * @param out            输出流
	 * @param bufferSize     缓存大小
	 * @param count          总拷贝长度
	 * @param streamProgress 进度条
	 * @return 传输的byte数
	 * @throws IORuntimeException IO异常
	 * @since 5.7.8
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize, long count, StreamProgress streamProgress) throws IORuntimeException {
		return new StreamCopier(bufferSize, count, streamProgress).copy(in, out);
	}

	/**
	 * 拷贝文件流，使用NIO
	 *
	 * @param in  输入
	 * @param out 输出
	 * @return 拷贝的字节数
	 * @throws IORuntimeException IO异常
	 */
	public static long copy(FileInputStream in, FileOutputStream out) throws IORuntimeException {
		

		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			inChannel = in.getChannel();
			outChannel = out.getChannel();
			return copy(inChannel, outChannel);
		} finally {
			close(outChannel);
			close(inChannel);
		}
	}

	// -------------------------------------------------------------------------------------- Copy end

	// -------------------------------------------------------------------------------------- getReader and getWriter start

	/**
	 * 获得一个文件读取器，默认使用UTF-8编码
	 *
	 * @param in 输入流
	 * @return BufferedReader对象
	 * @since 5.1.6
	 */
	public static BufferedReader getUtf8Reader(InputStream in) {
		return getReader(in, Charset.forName("UTF-8"));
	}

	/**
	 * 获得一个文件读取器
	 *
	 * @param in          输入流
	 * @param charsetName 字符集名称
	 * @return BufferedReader对象
	 * @deprecated 请使用 {@link #getReader(InputStream, Charset)}
	 */
	@Deprecated
	public static BufferedReader getReader(InputStream in, String charsetName) {
		return getReader(in, Charset.forName(charsetName));
	}

	/**
	 * 从{@link BOMInputStream}中获取Reader
	 *
	 * @param in {@link BOMInputStream}
	 * @return {@link BufferedReader}
	 * @since 5.5.8
	 */
	public static BufferedReader getReader(BOMInputStream in) {
		return getReader(in, in.getCharset());
	}

	/**
	 * 从{@link InputStream}中获取{@link BomReader}
	 *
	 * @param in {@link InputStream}
	 * @return {@link BomReader}
	 * @since 5.7.14
	 */
	public static BomReader getBomReader(InputStream in) {
		return new BomReader(in);
	}

	/**
	 * 获得一个Reader
	 *
	 * @param in      输入流
	 * @param charset 字符集
	 * @return BufferedReader对象
	 */
	public static BufferedReader getReader(InputStream in, Charset charset) {
		if (null == in) {
			return null;
		}

		InputStreamReader reader;
		if (null == charset) {
			reader = new InputStreamReader(in);
		} else {
			reader = new InputStreamReader(in, charset);
		}

		return new BufferedReader(reader);
	}

	/**
	 * 获得{@link BufferedReader}<br>
	 * 如果是{@link BufferedReader}强转返回，否则新建。如果提供的Reader为null返回null
	 *
	 * @param reader 普通Reader，如果为null返回null
	 * @return {@link BufferedReader} or null
	 * @since 3.0.9
	 */
	public static BufferedReader getReader(Reader reader) {
		if (null == reader) {
			return null;
		}

		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
	}

	/**
	 * 获得{@link PushbackReader}<br>
	 * 如果是{@link PushbackReader}强转返回，否则新建
	 *
	 * @param reader       普通Reader
	 * @param pushBackSize 推后的byte数
	 * @return {@link PushbackReader}
	 * @since 3.1.0
	 */
	public static PushbackReader getPushBackReader(Reader reader, int pushBackSize) {
		return (reader instanceof PushbackReader) ? (PushbackReader) reader : new PushbackReader(reader, pushBackSize);
	}

	/**
	 * 获得一个Writer，默认编码UTF-8
	 *
	 * @param out 输入流
	 * @return OutputStreamWriter对象
	 * @since 5.1.6
	 */
	public static OutputStreamWriter getUtf8Writer(OutputStream out) {
		return getWriter(out, Charset.forName("UTF-8"));
	}

	/**
	 * 获得一个Writer
	 *
	 * @param out         输入流
	 * @param charsetName 字符集
	 * @return OutputStreamWriter对象
	 * @deprecated 请使用 {@link #getWriter(OutputStream, Charset)}
	 */
	@Deprecated
	public static OutputStreamWriter getWriter(OutputStream out, String charsetName) {
		return getWriter(out, Charset.forName(charsetName));
	}

	/**
	 * 获得一个Writer
	 *
	 * @param out     输入流
	 * @param charset 字符集
	 * @return OutputStreamWriter对象
	 */
	public static OutputStreamWriter getWriter(OutputStream out, Charset charset) {
		if (null == out) {
			return null;
		}

		if (null == charset) {
			return new OutputStreamWriter(out);
		} else {
			return new OutputStreamWriter(out, charset);
		}
	}
	// -------------------------------------------------------------------------------------- getReader and getWriter end

	// -------------------------------------------------------------------------------------- read start

	/**
	 * 从流中读取UTF8编码的内容
	 *
	 * @param in 输入流
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 * @since 5.4.4
	 */
	public static String readUtf8(InputStream in) throws IORuntimeException {
		return read(in, Charset.forName("UTF-8"));
	}

	/**
	 * 从流中读取内容，读取完成后关闭流
	 *
	 * @param in          输入流
	 * @param charsetName 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 * @deprecated 请使用 {@link #read(InputStream, Charset)}
	 */
	@Deprecated
	public static String read(InputStream in, String charsetName) throws IORuntimeException {
		final FastByteArrayOutputStream out = read(in);
		return StrUtil.isBlank(charsetName) ? out.toString() : out.toString(charsetName);
	}

	/**
	 * 从流中读取内容，读取完毕后关闭流
	 *
	 * @param in      输入流，读取完毕后关闭流
	 * @param charset 字符集
	 * @return 内容
	 * @throws IORuntimeException IO异常
	 */
	public static String read(InputStream in, Charset charset) throws IORuntimeException {
		return StrUtil.str(readBytes(in), charset);
	}

	/**
	 * 从流中读取内容，读到输出流中，读取完毕后关闭流
	 *
	 * @param in 输入流
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 */
	public static FastByteArrayOutputStream read(InputStream in) throws IORuntimeException {
		return read(in, true);
	}

	/**
	 * 从流中读取内容，读到输出流中，读取完毕后可选是否关闭流
	 *
	 * @param in      输入流
	 * @param isClose 读取完毕后是否关闭流
	 * @return 输出流
	 * @throws IORuntimeException IO异常
	 * @since 5.5.3
	 */
	public static FastByteArrayOutputStream read(InputStream in, boolean isClose) throws IORuntimeException {
		final FastByteArrayOutputStream out;
		if (in instanceof FileInputStream) {
			// 文件流的长度是可预见的，此时直接读取效率更高
			try {
				out = new FastByteArrayOutputStream(in.available());
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		} else {
			out = new FastByteArrayOutputStream();
		}
		try {
			copy(in, out);
		} finally {
			if (isClose) {
				close(in);
			}
		}
		return out;
	}

	/**
	 * 从Reader中读取String，读取完毕后关闭Reader
	 *
	 * @param reader Reader
	 * @return String
	 * @throws IORuntimeException IO异常
	 */
	public static String read(Reader reader) throws IORuntimeException {
		return read(reader, true);
	}

	/**
	 * 从{@link Reader}中读取String
	 *
	 * @param reader  {@link Reader}
	 * @param isClose 是否关闭{@link Reader}
	 * @return String
	 * @throws IORuntimeException IO异常
	 */
	public static String read(Reader reader, boolean isClose) throws IORuntimeException {
		final StringBuilder builder = StrUtil.builder();
		final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
		try {
			while (-1 != reader.read(buffer)) {
				builder.append(buffer.flip());
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isClose) {
				IoUtil.close(reader);
			}
		}
		return builder.toString();
	}

	/**
	 * 从流中读取bytes，读取完毕后关闭流
	 *
	 * @param in {@link InputStream}
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 */
	public static byte[] readBytes(InputStream in) throws IORuntimeException {
		return readBytes(in, true);
	}

	/**
	 * 从流中读取bytes
	 *
	 * @param in      {@link InputStream}
	 * @param isClose 是否关闭输入流
	 * @return bytes
	 * @throws IORuntimeException IO异常
	 * @since 5.0.4
	 */
	public static byte[] readBytes(InputStream in, boolean isClose) throws IORuntimeException {
		return read(in, isClose).toByteArray();
	}














	// -------------------------------------------------------------------------------------- read end

	/**
	 * String 转为流
	 *
	 * @param content     内容
	 * @param charsetName 编码
	 * @return 字节流
	 * @deprecated 请使用 {@link #toStream(String, Charset)}
	 */
	@Deprecated
	public static ByteArrayInputStream toStream(String content, String charsetName) {
		return toStream(content, Charset.forName(charsetName));
	}

	/**
	 * String 转为流
	 *
	 * @param content 内容
	 * @param charset 编码
	 * @return 字节流
	 */
	public static ByteArrayInputStream toStream(String content, Charset charset) {
		if (content == null) {
			return null;
		}
		return toStream(StrUtil.bytes(content, charset));
	}

	/**
	 * String 转为UTF-8编码的字节流流
	 *
	 * @param content 内容
	 * @return 字节流
	 * @since 4.5.1
	 */
	public static ByteArrayInputStream toUtf8Stream(String content) {
		return toStream(content, Charset.forName("UTF-8"));
	}

	/**
	 * 文件转为{@link FileInputStream}
	 *
	 * @param file 文件
	 * @return {@link FileInputStream}
	 */
	public static FileInputStream toStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * byte[] 转为{@link ByteArrayInputStream}
	 *
	 * @param content 内容bytes
	 * @return 字节流
	 * @since 4.1.8
	 */
	public static ByteArrayInputStream toStream(byte[] content) {
		if (content == null) {
			return null;
		}
		return new ByteArrayInputStream(content);
	}

	/**
	 * {@link ByteArrayOutputStream}转为{@link ByteArrayInputStream}
	 *
	 * @param out {@link ByteArrayOutputStream}
	 * @return 字节流
	 * @since 5.3.6
	 */
	public static ByteArrayInputStream toStream(ByteArrayOutputStream out) {
		if (out == null) {
			return null;
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	/**
	 * 转换为{@link BufferedInputStream}
	 *
	 * @param in {@link InputStream}
	 * @return {@link BufferedInputStream}
	 * @since 4.0.10
	 */
	public static BufferedInputStream toBuffered(InputStream in) {
		return (in instanceof BufferedInputStream) ? (BufferedInputStream) in : new BufferedInputStream(in);
	}


	/**
	 * 转换为{@link PushbackInputStream}<br>
	 * 如果传入的输入流已经是{@link PushbackInputStream}，强转返回，否则新建一个
	 *
	 * @param in           {@link InputStream}
	 * @param pushBackSize 推后的byte数
	 * @return {@link PushbackInputStream}
	 * @since 3.1.0
	 */
	public static PushbackInputStream toPushbackStream(InputStream in, int pushBackSize) {
		return (in instanceof PushbackInputStream) ? (PushbackInputStream) in : new PushbackInputStream(in, pushBackSize);
	}

	/**
	 * 将byte[]写到流中
	 *
	 * @param out        输出流
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param content    写入的内容
	 * @throws IORuntimeException IO异常
	 */
	public static void write(OutputStream out, boolean isCloseOut, byte[] content) throws IORuntimeException {
		try {
			out.write(content);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isCloseOut) {
				close(out);
			}
		}
	}



	/**
	 * 将多部分内容写到流中，自动转换为字符串
	 *
	 * @param out         输出流
	 * @param charsetName 写出的内容的字符集
	 * @param isCloseOut  写入完毕是否关闭输出流
	 * @param contents    写入的内容，调用toString()方法，不包括不会自动换行
	 * @throws IORuntimeException IO异常
	 * @deprecated 请使用 {@link #write(OutputStream, Charset, boolean, Object...)}
	 */
	@Deprecated
	public static void write(OutputStream out, String charsetName, boolean isCloseOut, Object... contents) throws IORuntimeException {
		write(out, Charset.forName(charsetName), isCloseOut, contents);
	}

	/**
	 * 将多部分内容写到流中，自动转换为字符串
	 *
	 * @param out        输出流
	 * @param charset    写出的内容的字符集
	 * @param isCloseOut 写入完毕是否关闭输出流
	 * @param contents   写入的内容，调用toString()方法，不包括不会自动换行
	 * @throws IORuntimeException IO异常
	 * @since 3.0.9
	 */
	public static void write(OutputStream out, Charset charset, boolean isCloseOut, Object... contents) throws IORuntimeException {
		OutputStreamWriter osw = null;
		try {
			osw = getWriter(out, charset);
			for (Object content : contents) {
				if (content != null) {
					osw.write(content.toString()!=null?content.toString():StrUtil.EMPTY);
				}
			}
			osw.flush();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isCloseOut) {
				close(osw);
			}
		}
	}

	/**
	 * 从缓存中刷出数据
	 *
	 * @param flushable {@link Flushable}
	 * @since 4.2.2
	 */
	public static void flush(Flushable flushable) {
		if (null != flushable) {
			try {
				flushable.flush();
			} catch (Exception e) {
				// 静默刷出
			}
		}
	}

	/**
	 * 关闭<br>
	 * 关闭失败不会抛出异常
	 *
	 * @param closeable 被关闭的对象
	 */
	public static void close(Closeable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			} catch (Exception e) {
				// 静默关闭
			}
		}
	}


}
