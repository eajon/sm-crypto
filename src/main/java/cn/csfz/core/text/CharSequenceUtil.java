package cn.csfz.core.text;

import cn.csfz.core.util.ArrayUtil;
import cn.csfz.core.util.CharUtil;
import cn.csfz.core.util.StrUtil;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link CharSequence} 相关工具类封装
 *
 * @author looly
 * @since 5.5.3
 */
public class CharSequenceUtil {

    public static final int INDEX_NOT_FOUND = Finder.INDEX_NOT_FOUND;

    /**
     * 字符串常量：{@code "null"} <br>
     * 注意：{@code "null" != null}
     */
    public static final String NULL = "null";

    /**
     * 字符串常量：空字符串 {@code ""}
     */
    public static final String EMPTY = "";

    /**
     * 字符串常量：空格符 {@code " "}
     */
    public static final String SPACE = " ";

    /**
     * <p>字符串是否为空白，空白的定义如下：</p>
     * <ol>
     *     <li>{@code null}</li>
     *     <li>空字符串：{@code ""}</li>
     *     <li>空格、全角空格、制表符、换行符，等不可见字符</li>
     * </ol>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.isBlank(null)     // true}</li>
     *     <li>{@code StrUtil.isBlank("")       // true}</li>
     *     <li>{@code StrUtil.isBlank(" \t\n")  // true}</li>
     *     <li>{@code StrUtil.isBlank("abc")    // false}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #isEmpty(CharSequence)} 的区别是：
     * 该方法会校验空白字符，且性能相对于 {@link #isEmpty(CharSequence)} 略慢。</p>
     * <br>
     *
     * <p>建议：</p>
     * <ul>
     *     <li>该方法建议仅对于客户端（或第三方接口）传入的参数使用该方法。</li>
     *     <li>需要同时校验多个字符串时，建议采用 {@link #hasBlank(CharSequence...)} 或 {@link #isAllBlank(CharSequence...)}</li>
     * </ul>
     *
     * @param str 被检测的字符串
     * @return 若为空白，则返回 true
     * @see #isEmpty(CharSequence)
     */
    public static boolean isBlank(CharSequence str) {
        final int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            if (false == CharUtil.isBlankChar(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * <p>字符串是否为非空白，非空白的定义如下： </p>
     * <ol>
     *     <li>不为 {@code null}</li>
     *     <li>不为空字符串：{@code ""}</li>
     *     <li>不为空格、全角空格、制表符、换行符，等不可见字符</li>
     * </ol>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.isNotBlank(null)     // false}</li>
     *     <li>{@code StrUtil.isNotBlank("")       // false}</li>
     *     <li>{@code StrUtil.isNotBlank(" \t\n")  // false}</li>
     *     <li>{@code StrUtil.isNotBlank("abc")    // true}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #isNotEmpty(CharSequence)} 的区别是：
     * 该方法会校验空白字符，且性能相对于 {@link #isNotEmpty(CharSequence)} 略慢。</p>
     * <p>建议：仅对于客户端（或第三方接口）传入的参数使用该方法。</p>
     *
     * @param str 被检测的字符串
     * @return 是否为非空
     * @see #isBlank(CharSequence)
     */
    public static boolean isNotBlank(CharSequence str) {
        return false == isBlank(str);
    }

    /**
     * <p>指定字符串数组中，是否包含空字符串。</p>
     * <p>如果指定的字符串数组的长度为 0，或者其中的任意一个元素是空字符串，则返回 true。</p>
     * <br>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.hasBlank()                  // true}</li>
     *     <li>{@code StrUtil.hasBlank("", null, " ")     // true}</li>
     *     <li>{@code StrUtil.hasBlank("123", " ")        // true}</li>
     *     <li>{@code StrUtil.hasBlank("123", "abc")      // false}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #isAllBlank(CharSequence...)} 的区别在于：</p>
     * <ul>
     *     <li>hasBlank(CharSequence...)            等价于 {@code isBlank(...) || isBlank(...) || ...}</li>
     *     <li>{@link #isAllBlank(CharSequence...)} 等价于 {@code isBlank(...) && isBlank(...) && ...}</li>
     * </ul>
     *
     * @param strs 字符串列表
     * @return 是否包含空字符串
     */
    public static boolean hasBlank(CharSequence... strs) {
        if (ArrayUtil.isEmpty(strs)) {
            return true;
        }

        for (CharSequence str : strs) {
            if (isBlank(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>指定字符串数组中的元素，是否全部为空字符串。</p>
     * <p>如果指定的字符串数组的长度为 0，或者所有元素都是空字符串，则返回 true。</p>
     * <br>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.isAllBlank()                  // true}</li>
     *     <li>{@code StrUtil.isAllBlank("", null, " ")     // true}</li>
     *     <li>{@code StrUtil.isAllBlank("123", " ")        // false}</li>
     *     <li>{@code StrUtil.isAllBlank("123", "abc")      // false}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #hasBlank(CharSequence...)} 的区别在于：</p>
     * <ul>
     *     <li>{@link #hasBlank(CharSequence...)}   等价于 {@code isBlank(...) || isBlank(...) || ...}</li>
     *     <li>isAllBlank(CharSequence...)          等价于 {@code isBlank(...) && isBlank(...) && ...}</li>
     * </ul>
     *
     * @param strs 字符串列表
     * @return 所有字符串是否为空白
     */
    public static boolean isAllBlank(CharSequence... strs) {
        if (ArrayUtil.isEmpty(strs)) {
            return true;
        }

        for (CharSequence str : strs) {
            if (isNotBlank(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>字符串是否为空，空的定义如下：</p>
     * <ol>
     *     <li>{@code null}</li>
     *     <li>空字符串：{@code ""}</li>
     * </ol>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.isEmpty(null)     // true}</li>
     *     <li>{@code StrUtil.isEmpty("")       // true}</li>
     *     <li>{@code StrUtil.isEmpty(" \t\n")  // false}</li>
     *     <li>{@code StrUtil.isEmpty("abc")    // false}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #isBlank(CharSequence)} 的区别是：该方法不校验空白字符。</p>
     * <p>建议：</p>
     * <ul>
     *     <li>该方法建议用于工具类或任何可以预期的方法参数的校验中。</li>
     *     <li>需要同时校验多个字符串时，建议采用 {@link #hasEmpty(CharSequence...)} 或 {@link #isAllEmpty(CharSequence...)}</li>
     * </ul>
     *
     * @param str 被检测的字符串
     * @return 是否为空
     * @see #isBlank(CharSequence)
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * <p>字符串是否为非空白，非空白的定义如下： </p>
     * <ol>
     *     <li>不为 {@code null}</li>
     *     <li>不为空字符串：{@code ""}</li>
     * </ol>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.isNotEmpty(null)     // false}</li>
     *     <li>{@code StrUtil.isNotEmpty("")       // false}</li>
     *     <li>{@code StrUtil.isNotEmpty(" \t\n")  // true}</li>
     *     <li>{@code StrUtil.isNotEmpty("abc")    // true}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #isNotBlank(CharSequence)} 的区别是：该方法不校验空白字符。</p>
     * <p>建议：该方法建议用于工具类或任何可以预期的方法参数的校验中。</p>
     *
     * @param str 被检测的字符串
     * @return 是否为非空
     * @see #isEmpty(CharSequence)
     */
    public static boolean isNotEmpty(CharSequence str) {
        return false == isEmpty(str);
    }

    /**
     * 当给定字符串为null时，转换为Empty
     *
     * @param str 被检查的字符串
     * @return 原字符串或者空串
     * @see #nullToEmpty(CharSequence)
     * @since 4.6.3
     */
    public static String emptyIfNull(CharSequence str) {
        return nullToEmpty(str);
    }

    /**
     * 当给定字符串为null时，转换为Empty
     *
     * @param str 被转换的字符串
     * @return 转换后的字符串
     */
    public static String nullToEmpty(CharSequence str) {
        return nullToDefault(str, EMPTY);
    }

    /**
     * 如果字符串是 {@code null}，则返回指定默认字符串，否则返回字符串本身。
     *
     * <pre>
     * nullToDefault(null, &quot;default&quot;)  = &quot;default&quot;
     * nullToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;&quot;
     * nullToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
     * nullToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
     * </pre>
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     */
    public static String nullToDefault(CharSequence str, String defaultStr) {
        return (str == null) ? defaultStr : str.toString();
    }

    /**
     * 如果字符串是{@code null}或者&quot;&quot;，则返回指定默认字符串，否则返回字符串本身。
     *
     * <pre>
     * emptyToDefault(null, &quot;default&quot;)  = &quot;default&quot;
     * emptyToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;default&quot;
     * emptyToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
     * emptyToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
     * </pre>
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     * @since 4.1.0
     */
    public static String emptyToDefault(CharSequence str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str.toString();
    }

    /**
     * 如果字符串是{@code null}或者&quot;&quot;或者空白，则返回指定默认字符串，否则返回字符串本身。
     *
     * <pre>
     * blankToDefault(null, &quot;default&quot;)  = &quot;default&quot;
     * blankToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;default&quot;
     * blankToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;default&quot;
     * blankToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
     * </pre>
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     * @since 4.1.0
     */
    public static String blankToDefault(CharSequence str, String defaultStr) {
        return isBlank(str) ? defaultStr : str.toString();
    }

    /**
     * 当给定字符串为空字符串时，转换为{@code null}
     *
     * @param str 被转换的字符串
     * @return 转换后的字符串
     */
    public static String emptyToNull(CharSequence str) {
        return isEmpty(str) ? null : str.toString();
    }

    /**
     * <p>是否包含空字符串。</p>
     * <p>如果指定的字符串数组的长度为 0，或者其中的任意一个元素是空字符串，则返回 true。</p>
     * <br>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.hasEmpty()                  // true}</li>
     *     <li>{@code StrUtil.hasEmpty("", null)          // true}</li>
     *     <li>{@code StrUtil.hasEmpty("123", "")         // true}</li>
     *     <li>{@code StrUtil.hasEmpty("123", "abc")      // false}</li>
     *     <li>{@code StrUtil.hasEmpty(" ", "\t", "\n")   // false}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #isAllEmpty(CharSequence...)} 的区别在于：</p>
     * <ul>
     *     <li>hasEmpty(CharSequence...)            等价于 {@code isEmpty(...) || isEmpty(...) || ...}</li>
     *     <li>{@link #isAllEmpty(CharSequence...)} 等价于 {@code isEmpty(...) && isEmpty(...) && ...}</li>
     * </ul>
     *
     * @param strs 字符串列表
     * @return 是否包含空字符串
     */
    public static boolean hasEmpty(CharSequence... strs) {
        if (ArrayUtil.isEmpty(strs)) {
            return true;
        }

        for (CharSequence str : strs) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>指定字符串数组中的元素，是否全部为空字符串。</p>
     * <p>如果指定的字符串数组的长度为 0，或者所有元素都是空字符串，则返回 true。</p>
     * <br>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.isAllEmpty()                  // true}</li>
     *     <li>{@code StrUtil.isAllEmpty("", null)          // true}</li>
     *     <li>{@code StrUtil.isAllEmpty("123", "")         // false}</li>
     *     <li>{@code StrUtil.isAllEmpty("123", "abc")      // false}</li>
     *     <li>{@code StrUtil.isAllEmpty(" ", "\t", "\n")   // false}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #hasEmpty(CharSequence...)} 的区别在于：</p>
     * <ul>
     *     <li>{@link #hasEmpty(CharSequence...)}   等价于 {@code isEmpty(...) || isEmpty(...) || ...}</li>
     *     <li>isAllEmpty(CharSequence...)          等价于 {@code isEmpty(...) && isEmpty(...) && ...}</li>
     * </ul>
     *
     * @param strs 字符串列表
     * @return 所有字符串是否为空白
     */
    public static boolean isAllEmpty(CharSequence... strs) {
        if (ArrayUtil.isEmpty(strs)) {
            return true;
        }

        for (CharSequence str : strs) {
            if (isNotEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>指定字符串数组中的元素，是否都不为空字符串。</p>
     * <p>如果指定的字符串数组的长度不为 0，或者所有元素都不是空字符串，则返回 true。</p>
     * <br>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.isAllNotEmpty()                  // false}</li>
     *     <li>{@code StrUtil.isAllNotEmpty("", null)          // false}</li>
     *     <li>{@code StrUtil.isAllNotEmpty("123", "")         // false}</li>
     *     <li>{@code StrUtil.isAllNotEmpty("123", "abc")      // true}</li>
     *     <li>{@code StrUtil.isAllNotEmpty(" ", "\t", "\n")   // true}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #isAllEmpty(CharSequence...)} 的区别在于：</p>
     * <ul>
     *     <li>{@link #isAllEmpty(CharSequence...)}    等价于 {@code isEmpty(...) && isEmpty(...) && ...}</li>
     *     <li>isAllNotEmpty(CharSequence...)          等价于 {@code !isEmpty(...) && !isEmpty(...) && ...}</li>
     * </ul>
     *
     * @param args 字符串数组
     * @return 所有字符串是否都不为为空白
     * @since 5.3.6
     */
    public static boolean isAllNotEmpty(CharSequence... args) {
        return false == hasEmpty(args);
    }

    /**
     * 是否存都不为{@code null}或空对象或空白符的对象，通过{@link #hasBlank(CharSequence...)} 判断元素
     *
     * @param args 被检查的对象,一个或者多个
     * @return 是否都不为空
     * @since 5.3.6
     */
    public static boolean isAllNotBlank(CharSequence... args) {
        return false == hasBlank(args);
    }

    /**
     * 检查字符串是否为null、“null”、“undefined”
     *
     * @param str 被检查的字符串
     * @return 是否为null、“null”、“undefined”
     * @since 4.0.10
     */
    public static boolean isNullOrUndefined(CharSequence str) {
        if (null == str) {
            return true;
        }
        return isNullOrUndefinedStr(str);
    }

    /**
     * 检查字符串是否为null、“”、“null”、“undefined”
     *
     * @param str 被检查的字符串
     * @return 是否为null、“”、“null”、“undefined”
     * @since 4.0.10
     */
    public static boolean isEmptyOrUndefined(CharSequence str) {
        if (isEmpty(str)) {
            return true;
        }
        return isNullOrUndefinedStr(str);
    }

    /**
     * 检查字符串是否为null、空白串、“null”、“undefined”
     *
     * @param str 被检查的字符串
     * @return 是否为null、空白串、“null”、“undefined”
     * @since 4.0.10
     */
    public static boolean isBlankOrUndefined(CharSequence str) {
        if (isBlank(str)) {
            return true;
        }
        return isNullOrUndefinedStr(str);
    }

    /**
     * 是否为“null”、“undefined”，不做空指针检查
     *
     * @param str 字符串
     * @return 是否为“null”、“undefined”
     */
    private static boolean isNullOrUndefinedStr(CharSequence str) {
        String strString = str.toString().trim();
        return NULL.equals(strString) || "undefined".equals(strString);
    }


    /**
     * 字符串是否以给定字符开始
     *
     * @param str 字符串
     * @param c   字符
     * @return 是否开始
     */
    public static boolean startWith(CharSequence str, char c) {
        if (isEmpty(str)) {
            return false;
        }
        return c == str.charAt(0);
    }

    /**
     * 是否以指定字符串开头<br>
     * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
     *
     * @param str        被监测字符串
     * @param prefix     开头字符串
     * @param ignoreCase 是否忽略大小写
     * @return 是否以指定字符串开头
     * @since 5.4.3
     */
    public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
        return startWith(str, prefix, ignoreCase, false);
    }

    /**
     * 是否以指定字符串开头<br>
     * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false<br>
     * <pre>
     *     CharSequenceUtil.startWith("123", "123", false, true);   -- false
     *     CharSequenceUtil.startWith("ABCDEF", "abc", true, true); -- true
     *     CharSequenceUtil.startWith("abc", "abc", true, true);    -- false
     * </pre>
     *
     * @param str          被监测字符串
     * @param prefix       开头字符串
     * @param ignoreCase   是否忽略大小写
     * @param ignoreEquals 是否忽略字符串相等的情况
     * @return 是否以指定字符串开头
     * @since 5.4.3
     */
    public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase, boolean ignoreEquals) {
        if (null == str || null == prefix) {
            if (ignoreEquals) {
                return false;
            }
            return null == str && null == prefix;
        }

        boolean isStartWith = str.toString()
                .regionMatches(ignoreCase, 0, prefix.toString(), 0, prefix.length());

        if (isStartWith) {
            return (false == ignoreEquals) || (false == equals(str, prefix, ignoreCase));
        }
        return false;
    }

    /**
     * 是否以指定字符串开头
     *
     * @param str    被监测字符串
     * @param prefix 开头字符串
     * @return 是否以指定字符串开头
     */
    public static boolean startWith(CharSequence str, CharSequence prefix) {
        return startWith(str, prefix, false);
    }

    /**
     * 是否以指定字符串开头，忽略相等字符串的情况
     *
     * @param str    被监测字符串
     * @param prefix 开头字符串
     * @return 是否以指定字符串开头并且两个字符串不相等
     */
    public static boolean startWithIgnoreEquals(CharSequence str, CharSequence prefix) {
        return startWith(str, prefix, false, true);
    }

    /**
     * 是否以指定字符串开头，忽略大小写
     *
     * @param str    被监测字符串
     * @param prefix 开头字符串
     * @return 是否以指定字符串开头
     */
    public static boolean startWithIgnoreCase(CharSequence str, CharSequence prefix) {
        return startWith(str, prefix, true);
    }

    /**
     * 给定字符串是否以任何一个字符串开始<br>
     * 给定字符串和数组为空都返回false
     *
     * @param str      给定字符串
     * @param prefixes 需要检测的开始字符串
     * @return 给定字符串是否以任何一个字符串开始
     * @since 3.0.6
     */
    public static boolean startWithAny(CharSequence str, CharSequence... prefixes) {
        if (isEmpty(str) || ArrayUtil.isEmpty(prefixes)) {
            return false;
        }

        for (CharSequence suffix : prefixes) {
            if (startWith(str, suffix, false)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 给定字符串是否以任何一个字符串结尾（忽略大小写）<br>
     * 给定字符串和数组为空都返回false
     *
     * @param str      给定字符串
     * @param suffixes 需要检测的结尾字符串
     * @return 给定字符串是否以任何一个字符串结尾
     * @since 5.8.1
     */
    public static boolean startWithAnyIgnoreCase(final CharSequence str, final CharSequence... suffixes) {
        if (isEmpty(str) || ArrayUtil.isEmpty(suffixes)) {
            return false;
        }

        for (final CharSequence suffix : suffixes) {
            if (startWith(str, suffix, true)) {
                return true;
            }
        }
        return false;
    }

    // ------------------------------------------------------------------------ endWith

    /**
     * 字符串是否以给定字符结尾
     *
     * @param str 字符串
     * @param c   字符
     * @return 是否结尾
     */
    public static boolean endWith(CharSequence str, char c) {
        if (isEmpty(str)) {
            return false;
        }
        return c == str.charAt(str.length() - 1);
    }

    /**
     * 是否以指定字符串结尾<br>
     * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
     *
     * @param str        被监测字符串
     * @param suffix     结尾字符串
     * @param ignoreCase 是否忽略大小写
     * @return 是否以指定字符串结尾
     */
    public static boolean endWith(CharSequence str, CharSequence suffix, boolean ignoreCase) {
        return endWith(str, suffix, ignoreCase, false);
    }

    /**
     * 是否以指定字符串结尾<br>
     * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
     *
     * @param str          被监测字符串
     * @param suffix       结尾字符串
     * @param ignoreCase   是否忽略大小写
     * @param ignoreEquals 是否忽略字符串相等的情况
     * @return 是否以指定字符串结尾
     * @since 5.8.0
     */
    public static boolean endWith(CharSequence str, CharSequence suffix, boolean ignoreCase, boolean ignoreEquals) {
        if (null == str || null == suffix) {
            if (ignoreEquals) {
                return false;
            }
            return null == str && null == suffix;
        }

        final int strOffset = str.length() - suffix.length();
        boolean isEndWith = str.toString()
                .regionMatches(ignoreCase, strOffset, suffix.toString(), 0, suffix.length());

        if (isEndWith) {
            return (false == ignoreEquals) || (false == equals(str, suffix, ignoreCase));
        }
        return false;
    }

    /**
     * 是否以指定字符串结尾
     *
     * @param str    被监测字符串
     * @param suffix 结尾字符串
     * @return 是否以指定字符串结尾
     */
    public static boolean endWith(CharSequence str, CharSequence suffix) {
        return endWith(str, suffix, false);
    }

    /**
     * 是否以指定字符串结尾，忽略大小写
     *
     * @param str    被监测字符串
     * @param suffix 结尾字符串
     * @return 是否以指定字符串结尾
     */
    public static boolean endWithIgnoreCase(CharSequence str, CharSequence suffix) {
        return endWith(str, suffix, true);
    }

    /**
     * 给定字符串是否以任何一个字符串结尾<br>
     * 给定字符串和数组为空都返回false
     *
     * @param str      给定字符串
     * @param suffixes 需要检测的结尾字符串
     * @return 给定字符串是否以任何一个字符串结尾
     * @since 3.0.6
     */
    public static boolean endWithAny(CharSequence str, CharSequence... suffixes) {
        if (isEmpty(str) || ArrayUtil.isEmpty(suffixes)) {
            return false;
        }

        for (CharSequence suffix : suffixes) {
            if (endWith(str, suffix, false)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 给定字符串是否以任何一个字符串结尾（忽略大小写）<br>
     * 给定字符串和数组为空都返回false
     *
     * @param str      给定字符串
     * @param suffixes 需要检测的结尾字符串
     * @return 给定字符串是否以任何一个字符串结尾
     * @since 5.5.9
     */
    public static boolean endWithAnyIgnoreCase(CharSequence str, CharSequence... suffixes) {
        if (isEmpty(str) || ArrayUtil.isEmpty(suffixes)) {
            return false;
        }

        for (CharSequence suffix : suffixes) {
            if (endWith(str, suffix, true)) {
                return true;
            }
        }
        return false;
    }

    // ------------------------------------------------------------------------ contains






    // ------------------------------------------------------------------------ indexOf





    /**
     * 指定范围内查找字符串，忽略大小写<br>
     *
     * <pre>
     * StrUtil.indexOfIgnoreCase(null, *, *)          = -1
     * StrUtil.indexOfIgnoreCase(*, null, *)          = -1
     * StrUtil.indexOfIgnoreCase("", "", 0)           = 0
     * StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
     * StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
     * StrUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
     * StrUtil.indexOfIgnoreCase("abc", "", 9)        = -1
     * </pre>
     *
     * @param str       字符串
     * @param searchStr 需要查找位置的字符串
     * @return 位置
     * @since 3.2.1
     */
    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
        return indexOfIgnoreCase(str, searchStr, 0);
    }

    /**
     * 指定范围内查找字符串
     *
     * <pre>
     * StrUtil.indexOfIgnoreCase(null, *, *)          = -1
     * StrUtil.indexOfIgnoreCase(*, null, *)          = -1
     * StrUtil.indexOfIgnoreCase("", "", 0)           = 0
     * StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
     * StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
     * StrUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
     * StrUtil.indexOfIgnoreCase("abc", "", 9)        = -1
     * </pre>
     *
     * @param str       字符串
     * @param searchStr 需要查找位置的字符串
     * @param fromIndex 起始位置
     * @return 位置
     * @since 3.2.1
     */
    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int fromIndex) {
        return indexOf(str, searchStr, fromIndex, true);
    }

    /**
     * 指定范围内查找字符串
     *
     * @param text       字符串，空则返回-1
     * @param searchStr  需要查找位置的字符串，空则返回-1
     * @param from       起始位置（包含）
     * @param ignoreCase 是否忽略大小写
     * @return 位置
     * @since 3.2.1
     */
    public static int indexOf(CharSequence text, CharSequence searchStr, int from, boolean ignoreCase) {
        if (isEmpty(text) || isEmpty(searchStr)) {
            if (StrUtil.equals(text, searchStr)) {
                return 0;
            } else {
                return INDEX_NOT_FOUND;
            }
        }
        return new StrFinder(searchStr, ignoreCase).setText(text).start(from);
    }

    /**
     * 指定范围内查找字符串，忽略大小写
     *
     * @param str       字符串
     * @param searchStr 需要查找位置的字符串
     * @return 位置
     * @since 3.2.1
     */
    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        return lastIndexOfIgnoreCase(str, searchStr, str.length());
    }

    /**
     * 指定范围内查找字符串，忽略大小写<br>
     * fromIndex 为搜索起始位置，从后往前计数
     *
     * @param str       字符串
     * @param searchStr 需要查找位置的字符串
     * @param fromIndex 起始位置，从后往前计数
     * @return 位置
     * @since 3.2.1
     */
    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int fromIndex) {
        return lastIndexOf(str, searchStr, fromIndex, true);
    }

    /**
     * 指定范围内查找字符串<br>
     * fromIndex 为搜索起始位置，从后往前计数
     *
     * @param text       字符串
     * @param searchStr  需要查找位置的字符串
     * @param from       起始位置，从后往前计数
     * @param ignoreCase 是否忽略大小写
     * @return 位置
     * @since 3.2.1
     */
    public static int lastIndexOf(CharSequence text, CharSequence searchStr, int from, boolean ignoreCase) {
        if (isEmpty(text) || isEmpty(searchStr)) {
            if (StrUtil.equals(text, searchStr)) {
                return 0;
            } else {
                return INDEX_NOT_FOUND;
            }
        }
        return new StrFinder(searchStr, ignoreCase)
                .setText(text).setNegative(true).start(from);
    }

    /**
     * 返回字符串 searchStr 在字符串 str 中第 ordinal 次出现的位置。
     *
     * <p>
     * 如果 str=null 或 searchStr=null 或 ordinal&ge;0 则返回-1<br>
     * 此方法来自：Apache-Commons-Lang
     * <p>
     * 例子（*代表任意字符）：
     *
     * <pre>
     * StrUtil.ordinalIndexOf(null, *, *)          = -1
     * StrUtil.ordinalIndexOf(*, null, *)          = -1
     * StrUtil.ordinalIndexOf("", "", *)           = 0
     * StrUtil.ordinalIndexOf("aabaabaa", "a", 1)  = 0
     * StrUtil.ordinalIndexOf("aabaabaa", "a", 2)  = 1
     * StrUtil.ordinalIndexOf("aabaabaa", "b", 1)  = 2
     * StrUtil.ordinalIndexOf("aabaabaa", "b", 2)  = 5
     * StrUtil.ordinalIndexOf("aabaabaa", "ab", 1) = 1
     * StrUtil.ordinalIndexOf("aabaabaa", "ab", 2) = 4
     * StrUtil.ordinalIndexOf("aabaabaa", "", 1)   = 0
     * StrUtil.ordinalIndexOf("aabaabaa", "", 2)   = 0
     * </pre>
     *
     * @param str       被检查的字符串，可以为null
     * @param searchStr 被查找的字符串，可以为null
     * @param ordinal   第几次出现的位置
     * @return 查找到的位置
     * @since 3.2.3
     */
    public static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return 0;
        }
        int found = 0;
        int index = INDEX_NOT_FOUND;
        do {
            index = indexOf(str, searchStr, index + 1, false);
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }

    // ------------------------------------------------------------------------ remove

    /**
     * 移除字符串中所有给定字符串<br>
     * 例：removeAll("aa-bb-cc-dd", "-") =》 aabbccdd
     *
     * @param str         字符串
     * @param strToRemove 被移除的字符串
     * @return 移除后的字符串
     */
    public static String removeAll(CharSequence str, CharSequence strToRemove) {
        // strToRemove如果为空， 也不用继续后面的逻辑
        if (isEmpty(str) || isEmpty(strToRemove)) {
            return str(str);
        }
        return str.toString().replace(strToRemove, EMPTY);
    }

    /**
     * 移除字符串中所有给定字符串，当某个字符串出现多次，则全部移除<br>
     * 例：removeAny("aa-bb-cc-dd", "a", "b") =》 --cc-dd
     *
     * @param str          字符串
     * @param strsToRemove 被移除的字符串
     * @return 移除后的字符串
     * @since 5.3.8
     */
    public static String removeAny(CharSequence str, CharSequence... strsToRemove) {
        String result = str(str);
        if (isNotEmpty(str)) {
            for (CharSequence strToRemove : strsToRemove) {
                result = removeAll(result, strToRemove);
            }
        }
        return result;
    }

    /**
     * 去除字符串中指定的多个字符，如有多个则全部去除
     *
     * @param str   字符串
     * @param chars 字符列表
     * @return 去除后的字符
     * @since 4.2.2
     */
    public static String removeAll(CharSequence str, char... chars) {
        if (null == str || ArrayUtil.isEmpty(chars)) {
            return str(str);
        }
        final int len = str.length();
        if (0 == len) {
            return str(str);
        }
        final StringBuilder builder = new StringBuilder(len);
        char c;
        for (int i = 0; i < len; i++) {
            c = str.charAt(i);
            if (false == ArrayUtil.contains(chars, c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    /**
     * 去除所有换行符，包括：
     *
     * <pre>
     * 1. \r
     * 1. \n
     * </pre>
     *
     * @param str 字符串
     * @return 处理后的字符串
     * @since 4.2.2
     */
    public static String removeAllLineBreaks(CharSequence str) {
        return removeAll(str, CharUtil.CR, CharUtil.LF);
    }

    /**
     * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br>
     * 例如：str=setName, preLength=3 =》 return name
     *
     * @param str       被处理的字符串
     * @param preLength 去掉的长度
     * @return 处理后的字符串，不符合规范返回null
     */
    public static String removePreAndLowerFirst(CharSequence str, int preLength) {
        if (str == null) {
            return null;
        }
        if (str.length() > preLength) {
            char first = Character.toLowerCase(str.charAt(preLength));
            if (str.length() > preLength + 1) {
                return first + str.toString().substring(preLength + 1);
            }
            return String.valueOf(first);
        } else {
            return str.toString();
        }
    }






    // ------------------------------------------------------------------------ strip

    /**
     * 去除两边的指定字符串
     *
     * @param str            被处理的字符串
     * @param prefixOrSuffix 前缀或后缀
     * @return 处理后的字符串
     * @since 3.1.2
     */
    public static String strip(CharSequence str, CharSequence prefixOrSuffix) {
        if (equals(str, prefixOrSuffix)) {
            // 对于去除相同字符的情况单独处理
            return EMPTY;
        }
        return strip(str, prefixOrSuffix, prefixOrSuffix);
    }

    /**
     * 去除两边的指定字符串
     *
     * @param str    被处理的字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 处理后的字符串
     * @since 3.1.2
     */
    public static String strip(CharSequence str, CharSequence prefix, CharSequence suffix) {
        if (isEmpty(str)) {
            return str(str);
        }

        int from = 0;
        int to = str.length();

        String str2 = str.toString();
        if (startWith(str2, prefix)) {
            from = prefix.length();
        }
        if (endWith(str2, suffix)) {
            to -= suffix.length();
        }

        return str2.substring(Math.min(from, to), Math.max(from, to));
    }

    /**
     * 去除两边的指定字符串，忽略大小写
     *
     * @param str            被处理的字符串
     * @param prefixOrSuffix 前缀或后缀
     * @return 处理后的字符串
     * @since 3.1.2
     */
    public static String stripIgnoreCase(CharSequence str, CharSequence prefixOrSuffix) {
        return stripIgnoreCase(str, prefixOrSuffix, prefixOrSuffix);
    }

    /**
     * 去除两边的指定字符串，忽略大小写
     *
     * @param str    被处理的字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 处理后的字符串
     * @since 3.1.2
     */
    public static String stripIgnoreCase(CharSequence str, CharSequence prefix, CharSequence suffix) {
        if (isEmpty(str)) {
            return str(str);
        }
        int from = 0;
        int to = str.length();

        String str2 = str.toString();
        if (startWithIgnoreCase(str2, prefix)) {
            from = prefix.length();
        }
        if (endWithIgnoreCase(str2, suffix)) {
            to -= suffix.length();
        }
        return str2.substring(from, to);
    }

    // ------------------------------------------------------------------------ add


    // ------------------------------------------------------------------------ split


    // ------------------------------------------------------------------------ sub

    /**
     * 改进JDK subString<br>
     * index从0开始计算，最后一个字符为-1<br>
     * 如果from和to位置一样，返回 "" <br>
     * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
     * 如果经过修正的index中from大于to，则互换from和to example: <br>
     * abcdefgh 2 3 =》 c <br>
     * abcdefgh 2 -3 =》 cde <br>
     *
     * @param str              String
     * @param fromIndexInclude 开始的index（包括）
     * @param toIndexExclude   结束的index（不包括）
     * @return 字串
     */
    public static String sub(CharSequence str, int fromIndexInclude, int toIndexExclude) {
        if (isEmpty(str)) {
            return str(str);
        }
        int len = str.length();

        if (fromIndexInclude < 0) {
            fromIndexInclude = len + fromIndexInclude;
            if (fromIndexInclude < 0) {
                fromIndexInclude = 0;
            }
        } else if (fromIndexInclude > len) {
            fromIndexInclude = len;
        }

        if (toIndexExclude < 0) {
            toIndexExclude = len + toIndexExclude;
            if (toIndexExclude < 0) {
                toIndexExclude = len;
            }
        } else if (toIndexExclude > len) {
            toIndexExclude = len;
        }

        if (toIndexExclude < fromIndexInclude) {
            int tmp = fromIndexInclude;
            fromIndexInclude = toIndexExclude;
            toIndexExclude = tmp;
        }

        if (fromIndexInclude == toIndexExclude) {
            return EMPTY;
        }

        return str.toString().substring(fromIndexInclude, toIndexExclude);
    }







    /**
     * 截取分隔字符串之后的字符串，不包括分隔字符串<br>
     * 如果给定的字符串为空串（null或""），返回原字符串<br>
     * 如果分隔字符串为空串（null或""），则返回空串，如果分隔字符串未找到，返回空串，举例如下：
     *
     * <pre>
     * StrUtil.subAfter(null, *, false)      = null
     * StrUtil.subAfter("", *, false)        = ""
     * StrUtil.subAfter(*, null, false)      = ""
     * StrUtil.subAfter("abc", "a", false)   = "bc"
     * StrUtil.subAfter("abcba", "b", false) = "cba"
     * StrUtil.subAfter("abc", "c", false)   = ""
     * StrUtil.subAfter("abc", "d", false)   = ""
     * StrUtil.subAfter("abc", "", false)    = "abc"
     * </pre>
     *
     * @param string          被查找的字符串
     * @param separator       分隔字符串（不包括）
     * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
     * @return 切割后的字符串
     * @since 3.1.1
     */
    public static String subAfter(CharSequence string, CharSequence separator, boolean isLastSeparator) {
        if (isEmpty(string)) {
            return null == string ? null : EMPTY;
        }
        if (separator == null) {
            return EMPTY;
        }
        final String str = string.toString();
        final String sep = separator.toString();
        final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
        if (INDEX_NOT_FOUND == pos || (string.length() - 1) == pos) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * 截取分隔字符串之后的字符串，不包括分隔字符串<br>
     * 如果给定的字符串为空串（null或""），返回原字符串<br>
     * 如果分隔字符串为空串（null或""），则返回空串，如果分隔字符串未找到，返回空串，举例如下：
     *
     * <pre>
     * StrUtil.subAfter(null, *, false)      = null
     * StrUtil.subAfter("", *, false)        = ""
     * StrUtil.subAfter("abc", 'a', false)   = "bc"
     * StrUtil.subAfter("abcba", 'b', false) = "cba"
     * StrUtil.subAfter("abc", 'c', false)   = ""
     * StrUtil.subAfter("abc", 'd', false)   = ""
     * </pre>
     *
     * @param string          被查找的字符串
     * @param separator       分隔字符串（不包括）
     * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
     * @return 切割后的字符串
     * @since 4.1.15
     */
    public static String subAfter(CharSequence string, char separator, boolean isLastSeparator) {
        if (isEmpty(string)) {
            return null == string ? null : EMPTY;
        }
        final String str = string.toString();
        final int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
        if (INDEX_NOT_FOUND == pos) {
            return EMPTY;
        }
        return str.substring(pos + 1);
    }

    /**
     * 截取指定字符串中间部分，不包括标识字符串<br>
     * <p>
     * 栗子：
     *
     * <pre>
     * StrUtil.subBetween("wx[b]yz", "[", "]") = "b"
     * StrUtil.subBetween(null, *, *)          = null
     * StrUtil.subBetween(*, null, *)          = null
     * StrUtil.subBetween(*, *, null)          = null
     * StrUtil.subBetween("", "", "")          = ""
     * StrUtil.subBetween("", "", "]")         = null
     * StrUtil.subBetween("", "[", "]")        = null
     * StrUtil.subBetween("yabcz", "", "")     = ""
     * StrUtil.subBetween("yabcz", "y", "z")   = "abc"
     * StrUtil.subBetween("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     *
     * @param str    被切割的字符串
     * @param before 截取开始的字符串标识
     * @param after  截取到的字符串标识
     * @return 截取后的字符串
     * @since 3.1.1
     */
    public static String subBetween(CharSequence str, CharSequence before, CharSequence after) {
        if (str == null || before == null || after == null) {
            return null;
        }

        final String str2 = str.toString();
        final String before2 = before.toString();
        final String after2 = after.toString();

        final int start = str2.indexOf(before2);
        if (start != INDEX_NOT_FOUND) {
            final int end = str2.indexOf(after2, start + before2.length());
            if (end != INDEX_NOT_FOUND) {
                return str2.substring(start + before2.length(), end);
            }
        }
        return null;
    }

    /**
     * 截取指定字符串中间部分，不包括标识字符串<br>
     * <p>
     * 栗子：
     *
     * <pre>
     * StrUtil.subBetween(null, *)            = null
     * StrUtil.subBetween("", "")             = ""
     * StrUtil.subBetween("", "tag")          = null
     * StrUtil.subBetween("tagabctag", null)  = null
     * StrUtil.subBetween("tagabctag", "")    = ""
     * StrUtil.subBetween("tagabctag", "tag") = "abc"
     * </pre>
     *
     * @param str            被切割的字符串
     * @param beforeAndAfter 截取开始和结束的字符串标识
     * @return 截取后的字符串
     * @since 3.1.1
     */
    public static String subBetween(CharSequence str, CharSequence beforeAndAfter) {
        return subBetween(str, beforeAndAfter, beforeAndAfter);
    }



    // ------------------------------------------------------------------------ repeat

    /**
     * 重复某个字符
     *
     * <pre>
     * StrUtil.repeat('e', 0)  = ""
     * StrUtil.repeat('e', 3)  = "eee"
     * StrUtil.repeat('e', -2) = ""
     * </pre>
     *
     * @param c     被重复的字符
     * @param count 重复的数目，如果小于等于0则返回""
     * @return 重复字符字符串
     */
    public static String repeat(char c, int count) {
        if (count <= 0) {
            return EMPTY;
        }

        char[] result = new char[count];
        for (int i = 0; i < count; i++) {
            result[i] = c;
        }
        return new String(result);
    }

    /**
     * 重复某个字符串
     *
     * @param str   被重复的字符
     * @param count 重复的数目
     * @return 重复字符字符串
     */
    public static String repeat(CharSequence str, int count) {
        if (null == str) {
            return null;
        }
        if (count <= 0 || str.length() == 0) {
            return EMPTY;
        }
        if (count == 1) {
            return str.toString();
        }

        // 检查
        final int len = str.length();
        final long longSize = (long) len * (long) count;
        final int size = (int) longSize;
        if (size != longSize) {
            throw new ArrayIndexOutOfBoundsException("Required String length is too large: " + longSize);
        }

        final char[] array = new char[size];
        str.toString().getChars(0, len, array, 0);
        int n;
        for (n = len; n < size - n; n <<= 1) {// n <<= 1相当于n *2
            System.arraycopy(array, 0, array, n, n);
        }
        System.arraycopy(array, 0, array, n, size - n);
        return new String(array);
    }


    // ------------------------------------------------------------------------ equals

    /**
     * 比较两个字符串（大小写敏感）。
     *
     * <pre>
     * equals(null, null)   = true
     * equals(null, &quot;abc&quot;)  = false
     * equals(&quot;abc&quot;, null)  = false
     * equals(&quot;abc&quot;, &quot;abc&quot;) = true
     * equals(&quot;abc&quot;, &quot;ABC&quot;) = false
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
     */
    public static boolean equals(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, false);
    }

    /**
     * 比较两个字符串（大小写不敏感）。
     *
     * <pre>
     * equalsIgnoreCase(null, null)   = true
     * equalsIgnoreCase(null, &quot;abc&quot;)  = false
     * equalsIgnoreCase(&quot;abc&quot;, null)  = false
     * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
     * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
     */
    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, true);
    }

    /**
     * 比较两个字符串是否相等，规则如下
     * <ul>
     *     <li>str1和str2都为{@code null}</li>
     *     <li>忽略大小写使用{@link String#equalsIgnoreCase(String)}判断相等</li>
     *     <li>不忽略大小写使用{@link String#contentEquals(CharSequence)}判断相等</li>
     * </ul>
     *
     * @param str1       要比较的字符串1
     * @param str2       要比较的字符串2
     * @param ignoreCase 是否忽略大小写
     * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
     * @since 3.2.0
     */
    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (null == str1) {
            // 只有两个都为null才判断相等
            return str2 == null;
        }
        if (null == str2) {
            // 字符串2空，字符串1非空，直接false
            return false;
        }

        if (ignoreCase) {
            return str1.toString().equalsIgnoreCase(str2.toString());
        } else {
            return str1.toString().contentEquals(str2);
        }
    }

    /**
     * 给定字符串是否与提供的中任一字符串相同（忽略大小写），相同则返回{@code true}，没有相同的返回{@code false}<br>
     * 如果参与比对的字符串列表为空，返回{@code false}
     *
     * @param str1 给定需要检查的字符串
     * @param strs 需要参与比对的字符串列表
     * @return 是否相同
     * @since 4.3.2
     */
    public static boolean equalsAnyIgnoreCase(CharSequence str1, CharSequence... strs) {
        return equalsAny(str1, true, strs);
    }

    /**
     * 给定字符串是否与提供的中任一字符串相同，相同则返回{@code true}，没有相同的返回{@code false}<br>
     * 如果参与比对的字符串列表为空，返回{@code false}
     *
     * @param str1 给定需要检查的字符串
     * @param strs 需要参与比对的字符串列表
     * @return 是否相同
     * @since 4.3.2
     */
    public static boolean equalsAny(CharSequence str1, CharSequence... strs) {
        return equalsAny(str1, false, strs);
    }

    /**
     * 给定字符串是否与提供的中任一字符串相同，相同则返回{@code true}，没有相同的返回{@code false}<br>
     * 如果参与比对的字符串列表为空，返回{@code false}
     *
     * @param str1       给定需要检查的字符串
     * @param ignoreCase 是否忽略大小写
     * @param strs       需要参与比对的字符串列表
     * @return 是否相同
     * @since 4.3.2
     */
    public static boolean equalsAny(CharSequence str1, boolean ignoreCase, CharSequence... strs) {
        if (ArrayUtil.isEmpty(strs)) {
            return false;
        }

        for (CharSequence str : strs) {
            if (equals(str1, str, ignoreCase)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 字符串指定位置的字符是否与给定字符相同<br>
     * 如果字符串为null，返回false<br>
     * 如果给定的位置大于字符串长度，返回false<br>
     * 如果给定的位置小于0，返回false
     *
     * @param str      字符串
     * @param position 位置
     * @param c        需要对比的字符
     * @return 字符串指定位置的字符是否与给定字符相同
     * @since 3.3.1
     */
    public static boolean equalsCharAt(CharSequence str, int position, char c) {
        if (null == str || position < 0) {
            return false;
        }
        return str.length() > position && c == str.charAt(position);
    }

    /**
     * 截取第一个字串的部分字符，与第二个字符串比较（长度一致），判断截取的子串是否相同<br>
     * 任意一个字符串为null返回false
     *
     * @param str1       第一个字符串
     * @param start1     第一个字符串开始的位置
     * @param str2       第二个字符串
     * @param ignoreCase 是否忽略大小写
     * @return 子串是否相同
     * @since 3.2.1
     */
    public static boolean isSubEquals(CharSequence str1, int start1, CharSequence str2, boolean ignoreCase) {
        return isSubEquals(str1, start1, str2, 0, str2.length(), ignoreCase);
    }

    /**
     * 截取两个字符串的不同部分（长度一致），判断截取的子串是否相同<br>
     * 任意一个字符串为null返回false
     *
     * @param str1       第一个字符串
     * @param start1     第一个字符串开始的位置
     * @param str2       第二个字符串
     * @param start2     第二个字符串开始的位置
     * @param length     截取长度
     * @param ignoreCase 是否忽略大小写
     * @return 子串是否相同
     * @since 3.2.1
     */
    public static boolean isSubEquals(CharSequence str1, int start1, CharSequence str2, int start2, int length, boolean ignoreCase) {
        if (null == str1 || null == str2) {
            return false;
        }

        return str1.toString().regionMatches(ignoreCase, start1, str2.toString(), start2, length);
    }

    // ------------------------------------------------------------------------ format

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is {} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示，如果模板为null，返回"null"
     * @param params   参数值
     * @return 格式化后的文本，如果模板为null，返回"null"
     */
    public static String format(CharSequence template, Object... params) {
        if (null == template) {
            return NULL;
        }
        if (ArrayUtil.isEmpty(params) || isBlank(template)) {
            return template.toString();
        }
        return StrFormatter.format(template.toString(), params);
    }

    /**
     * 有序的格式化文本，使用{number}做为占位符<br>
     * 通常使用：format("this is {0} for {1}", "a", "b") =》 this is a for b<br>
     *
     * @param pattern   文本格式
     * @param arguments 参数
     * @return 格式化后的文本
     */
    public static String indexedFormat(CharSequence pattern, Object... arguments) {
        return MessageFormat.format(pattern.toString(), arguments);
    }
    // ------------------------------------------------------------------------ bytes

    /**
     * 编码字符串，编码为UTF-8
     *
     * @param str 字符串
     * @return 编码后的字节码
     */
    public static byte[] utf8Bytes(CharSequence str) {
        return bytes(str, Charset.forName("UTF-8"));
    }

    /**
     * 编码字符串<br>
     * 使用系统默认编码
     *
     * @param str 字符串
     * @return 编码后的字节码
     */
    public static byte[] bytes(CharSequence str) {
        return bytes(str, Charset.defaultCharset());
    }

    /**
     * 编码字符串
     *
     * @param str     字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 编码后的字节码
     */
    public static byte[] bytes(CharSequence str, String charset) {
        return bytes(str, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    /**
     * 编码字符串
     *
     * @param str     字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 编码后的字节码
     */
    public static byte[] bytes(CharSequence str, Charset charset) {
        if (str == null) {
            return null;
        }

        if (null == charset) {
            return str.toString().getBytes();
        }
        return str.toString().getBytes(charset);
    }

    /**
     * 字符串转换为byteBuffer
     *
     * @param str     字符串
     * @param charset 编码
     * @return byteBuffer
     */
    public static ByteBuffer byteBuffer(CharSequence str, String charset) {
        return ByteBuffer.wrap(bytes(str, charset));
    }

    // ------------------------------------------------------------------------ wrap

    /**
     * 包装指定字符串<br>
     * 当前缀和后缀一致时使用此方法
     *
     * @param str             被包装的字符串
     * @param prefixAndSuffix 前缀和后缀
     * @return 包装后的字符串
     * @since 3.1.0
     */
    public static String wrap(CharSequence str, CharSequence prefixAndSuffix) {
        return wrap(str, prefixAndSuffix, prefixAndSuffix);
    }

    /**
     * 包装指定字符串
     *
     * @param str    被包装的字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 包装后的字符串
     */
    public static String wrap(CharSequence str, CharSequence prefix, CharSequence suffix) {
        return nullToEmpty(prefix).concat(nullToEmpty(str)).concat(nullToEmpty(suffix));
    }

    /**
     * 使用单个字符包装多个字符串
     *
     * @param prefixAndSuffix 前缀和后缀
     * @param strs            多个字符串
     * @return 包装的字符串数组
     * @since 5.4.1
     */
    public static String[] wrapAllWithPair(CharSequence prefixAndSuffix, CharSequence... strs) {
        return wrapAll(prefixAndSuffix, prefixAndSuffix, strs);
    }

    /**
     * 包装多个字符串
     *
     * @param prefix 前缀
     * @param suffix 后缀
     * @param strs   多个字符串
     * @return 包装的字符串数组
     * @since 4.0.7
     */
    public static String[] wrapAll(CharSequence prefix, CharSequence suffix, CharSequence... strs) {
        final String[] results = new String[strs.length];
        for (int i = 0; i < strs.length; i++) {
            results[i] = wrap(strs[i], prefix, suffix);
        }
        return results;
    }

    /**
     * 包装指定字符串，如果前缀或后缀已经包含对应的字符串，则不再包装
     *
     * @param str    被包装的字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 包装后的字符串
     */
    public static String wrapIfMissing(CharSequence str, CharSequence prefix, CharSequence suffix) {
        int len = 0;
        if (isNotEmpty(str)) {
            len += str.length();
        }
        if (isNotEmpty(prefix)) {
            len += prefix.length();
        }
        if (isNotEmpty(suffix)) {
            len += suffix.length();
        }
        StringBuilder sb = new StringBuilder(len);
        if (isNotEmpty(prefix) && false == startWith(str, prefix)) {
            sb.append(prefix);
        }
        if (isNotEmpty(str)) {
            sb.append(str);
        }
        if (isNotEmpty(suffix) && false == endWith(str, suffix)) {
            sb.append(suffix);
        }
        return sb.toString();
    }

    /**
     * 使用成对的字符包装多个字符串，如果已经包装，则不再包装
     *
     * @param prefixAndSuffix 前缀和后缀
     * @param strs            多个字符串
     * @return 包装的字符串数组
     * @since 5.4.1
     */
    public static String[] wrapAllWithPairIfMissing(CharSequence prefixAndSuffix, CharSequence... strs) {
        return wrapAllIfMissing(prefixAndSuffix, prefixAndSuffix, strs);
    }

    /**
     * 包装多个字符串，如果已经包装，则不再包装
     *
     * @param prefix 前缀
     * @param suffix 后缀
     * @param strs   多个字符串
     * @return 包装的字符串数组
     * @since 4.0.7
     */
    public static String[] wrapAllIfMissing(CharSequence prefix, CharSequence suffix, CharSequence... strs) {
        final String[] results = new String[strs.length];
        for (int i = 0; i < strs.length; i++) {
            results[i] = wrapIfMissing(strs[i], prefix, suffix);
        }
        return results;
    }



    /**
     * 去掉字符包装，如果未被包装则返回原字符串
     *
     * @param str    字符串
     * @param prefix 前置字符
     * @param suffix 后置字符
     * @return 去掉包装字符的字符串
     * @since 4.0.1
     */
    public static String unWrap(CharSequence str, char prefix, char suffix) {
        if (isEmpty(str)) {
            return str(str);
        }
        if (str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix) {
            return sub(str, 1, str.length() - 1);
        }
        return str.toString();
    }

    /**
     * 去掉字符包装，如果未被包装则返回原字符串
     *
     * @param str             字符串
     * @param prefixAndSuffix 前置和后置字符
     * @return 去掉包装字符的字符串
     * @since 4.0.1
     */
    public static String unWrap(CharSequence str, char prefixAndSuffix) {
        return unWrap(str, prefixAndSuffix, prefixAndSuffix);
    }




    // ------------------------------------------------------------------------ str

    /**
     * {@link CharSequence} 转为字符串，null安全
     *
     * @param cs {@link CharSequence}
     * @return 字符串
     */
    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }


    // ------------------------------------------------------------------------ replace

    /**
     * 替换字符串中的指定字符串，忽略大小写
     *
     * @param str         字符串
     * @param searchStr   被查找的字符串
     * @param replacement 被替换的字符串
     * @return 替换后的字符串
     * @since 4.0.3
     */
    public static String replaceIgnoreCase(CharSequence str, CharSequence searchStr, CharSequence replacement) {
        return replace(str, 0, searchStr, replacement, true);
    }

    /**
     * 替换字符串中的指定字符串
     *
     * @param str         字符串
     * @param searchStr   被查找的字符串
     * @param replacement 被替换的字符串
     * @return 替换后的字符串
     * @since 4.0.3
     */
    public static String replace(CharSequence str, CharSequence searchStr, CharSequence replacement) {
        return replace(str, 0, searchStr, replacement, false);
    }

    /**
     * 替换字符串中的指定字符串
     *
     * @param str         字符串
     * @param searchStr   被查找的字符串
     * @param replacement 被替换的字符串
     * @param ignoreCase  是否忽略大小写
     * @return 替换后的字符串
     * @since 4.0.3
     */
    public static String replace(CharSequence str, CharSequence searchStr, CharSequence replacement, boolean ignoreCase) {
        return replace(str, 0, searchStr, replacement, ignoreCase);
    }

    /**
     * 替换字符串中的指定字符串
     *
     * @param str         字符串
     * @param fromIndex   开始位置（包括）
     * @param searchStr   被查找的字符串
     * @param replacement 被替换的字符串
     * @param ignoreCase  是否忽略大小写
     * @return 替换后的字符串
     * @since 4.0.3
     */
    public static String replace(CharSequence str, int fromIndex, CharSequence searchStr, CharSequence replacement, boolean ignoreCase) {
        if (isEmpty(str) || isEmpty(searchStr)) {
            return str(str);
        }
        if (null == replacement) {
            replacement = EMPTY;
        }

        final int strLength = str.length();
        final int searchStrLength = searchStr.length();
        if (strLength < searchStrLength) {
            // issue#I4M16G@Gitee
            return str(str);
        }

        if (fromIndex > strLength) {
            return str(str);
        } else if (fromIndex < 0) {
            fromIndex = 0;
        }

        final StringBuilder result = new StringBuilder(strLength - searchStrLength + replacement.length());
        if (0 != fromIndex) {
            result.append(str.subSequence(0, fromIndex));
        }

        int preIndex = fromIndex;
        int index;
        while ((index = indexOf(str, searchStr, preIndex, ignoreCase)) > -1) {
            result.append(str.subSequence(preIndex, index));
            result.append(replacement);
            preIndex = index + searchStrLength;
        }

        if (preIndex < strLength) {
            // 结尾部分
            result.append(str.subSequence(preIndex, strLength));
        }
        return result.toString();
    }





    /**
     * 替换字符串中最后一个指定字符串
     *
     * @param str         字符串
     * @param searchStr   被查找的字符串
     * @param replacedStr 被替换的字符串
     * @return 替换后的字符串
     */
    public static String replaceLast(CharSequence str, CharSequence searchStr, CharSequence replacedStr) {
        return replaceLast(str, searchStr, replacedStr, false);
    }

    /**
     * 替换字符串中最后一个指定字符串
     *
     * @param str         字符串
     * @param searchStr   被查找的字符串
     * @param replacedStr 被替换的字符串
     * @param ignoreCase  是否忽略大小写
     * @return 替换后的字符串
     */
    public static String replaceLast(CharSequence str, CharSequence searchStr, CharSequence replacedStr, boolean ignoreCase) {
        if (isEmpty(str)) {
            return str(str);
        }
        int lastIndex = lastIndexOf(str, searchStr, str.length(), ignoreCase);
        if (INDEX_NOT_FOUND == lastIndex) {
            return str(str);
        }
        return replace(str, lastIndex, searchStr, replacedStr, ignoreCase);
    }


    /**
     * 替换字符字符数组中所有的字符为replacedStr<br>
     * 提供的chars为所有需要被替换的字符，例如："\r\n"，则"\r"和"\n"都会被替换，哪怕他们单独存在
     *
     * @param str         被检查的字符串
     * @param chars       需要替换的字符列表，用一个字符串表示这个字符列表
     * @param replacedStr 替换成的字符串
     * @return 新字符串
     * @since 3.2.2
     */
    public static String replaceChars(CharSequence str, String chars, CharSequence replacedStr) {
        if (isEmpty(str) || isEmpty(chars)) {
            return str(str);
        }
        return replaceChars(str, chars.toCharArray(), replacedStr);
    }

    /**
     * 替换字符字符数组中所有的字符为replacedStr
     *
     * @param str         被检查的字符串
     * @param chars       需要替换的字符列表
     * @param replacedStr 替换成的字符串
     * @return 新字符串
     * @since 3.2.2
     */
    public static String replaceChars(CharSequence str, char[] chars, CharSequence replacedStr) {
        if (isEmpty(str) || ArrayUtil.isEmpty(chars)) {
            return str(str);
        }

        final Set<Character> set = new HashSet<Character>(chars.length);
        for (char c : chars) {
            set.add(c);
        }
        int strLen = str.length();
        final StringBuilder builder = new StringBuilder();
        char c;
        for (int i = 0; i < strLen; i++) {
            c = str.charAt(i);
            builder.append(set.contains(c) ? replacedStr : c);
        }
        return builder.toString();
    }

    // ------------------------------------------------------------------------ length

    /**
     * 获取字符串的长度，如果为null返回0
     *
     * @param cs a 字符串
     * @return 字符串的长度，如果为null返回0
     * @since 4.3.2
     */
    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    /**
     * 给定字符串转为bytes后的byte数（byte长度）
     *
     * @param cs      字符串
     * @param charset 编码
     * @return byte长度
     * @since 4.5.2
     */
    public static int byteLength(CharSequence cs, Charset charset) {
        return cs == null ? 0 : cs.toString().getBytes(charset).length;
    }

    /**
     * 给定字符串数组的总长度<br>
     * null字符长度定义为0
     *
     * @param strs 字符串数组
     * @return 总长度
     * @since 4.0.1
     */
    public static int totalLength(CharSequence... strs) {
        int totalLength = 0;
        for (CharSequence str : strs) {
            totalLength += (null == str ? 0 : str.length());
        }
        return totalLength;
    }





    // ------------------------------------------------------------------------ lower and upper



    // ------------------------------------------------------------------------ filter



    // ------------------------------------------------------------------------ case

    /**
     * 给定字符串中的字母是否全部为大写，判断依据如下：
     *
     * <pre>
     * 1. 大写字母包括A-Z
     * 2. 其它非字母的Unicode符都算作大写
     * </pre>
     *
     * @param str 被检查的字符串
     * @return 是否全部为大写
     * @since 4.2.2
     */
    public static boolean isUpperCase(CharSequence str) {
        if (null == str) {
            return false;
        }
        final int len = str.length();
        for (int i = 0; i < len; i++) {
            if (Character.isLowerCase(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 给定字符串中的字母是否全部为小写，判断依据如下：
     *
     * <pre>
     * 1. 小写字母包括a-z
     * 2. 其它非字母的Unicode符都算作小写
     * </pre>
     *
     * @param str 被检查的字符串
     * @return 是否全部为小写
     * @since 4.2.2
     */
    public static boolean isLowerCase(CharSequence str) {
        if (null == str) {
            return false;
        }
        final int len = str.length();
        for (int i = 0; i < len; i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 切换给定字符串中的大小写。大写转小写，小写转大写。
     *
     * <pre>
     * StrUtil.swapCase(null)                 = null
     * StrUtil.swapCase("")                   = ""
     * StrUtil.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
     * </pre>
     *
     * @param str 字符串
     * @return 交换后的字符串
     * @since 4.3.2
     */
    public static String swapCase(final String str) {
        if (isEmpty(str)) {
            return str;
        }

        final char[] buffer = str.toCharArray();

        for (int i = 0; i < buffer.length; i++) {
            final char ch = buffer[i];
            if (Character.isUpperCase(ch)) {
                buffer[i] = Character.toLowerCase(ch);
            } else if (Character.isTitleCase(ch)) {
                buffer[i] = Character.toLowerCase(ch);
            } else if (Character.isLowerCase(ch)) {
                buffer[i] = Character.toUpperCase(ch);
            }
        }
        return new String(buffer);
    }


    // ------------------------------------------------------------------------ isSurround

    /**
     * 给定字符串是否被字符包围
     *
     * @param str    字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 是否包围，空串不包围
     */
    public static boolean isSurround(CharSequence str, CharSequence prefix, CharSequence suffix) {
        if (StrUtil.isBlank(str)) {
            return false;
        }
        if (str.length() < (prefix.length() + suffix.length())) {
            return false;
        }

        final String str2 = str.toString();
        return str2.startsWith(prefix.toString()) && str2.endsWith(suffix.toString());
    }

    /**
     * 给定字符串是否被字符包围
     *
     * @param str    字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 是否包围，空串不包围
     */
    public static boolean isSurround(CharSequence str, char prefix, char suffix) {
        if (StrUtil.isBlank(str)) {
            return false;
        }
        if (str.length() < 2) {
            return false;
        }

        return str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix;
    }

    // ------------------------------------------------------------------------ builder

    /**
     * 创建StringBuilder对象
     *
     * @param strs 初始字符串列表
     * @return StringBuilder对象
     */
    public static StringBuilder builder(CharSequence... strs) {
        final StringBuilder sb = new StringBuilder();
        for (CharSequence str : strs) {
            sb.append(str);
        }
        return sb;
    }

    /**
     * 创建StrBuilder对象
     *
     * @param strs 初始字符串列表
     * @return StrBuilder对象
     */
    public static StrBuilder strBuilder(CharSequence... strs) {
        return StrBuilder.create(strs);
    }

    // ------------------------------------------------------------------------ getter and setter

    /**
     * 获得set或get或is方法对应的标准属性名<br>
     * 例如：setName 返回 name
     *
     * <pre>
     * getName =》name
     * setName =》name
     * isName  =》name
     * </pre>
     *
     * @param getOrSetMethodName Get或Set方法名
     * @return 如果是set或get方法名，返回field， 否则null
     */
    public static String getGeneralField(CharSequence getOrSetMethodName) {
        final String getOrSetMethodNameStr = getOrSetMethodName.toString();
        if (getOrSetMethodNameStr.startsWith("get") || getOrSetMethodNameStr.startsWith("set")) {
            return removePreAndLowerFirst(getOrSetMethodName, 3);
        } else if (getOrSetMethodNameStr.startsWith("is")) {
            return removePreAndLowerFirst(getOrSetMethodName, 2);
        }
        return null;
    }


    // ------------------------------------------------------------------------ other

    /**
     * 连接多个字符串为一个
     *
     * @param isNullToEmpty 是否null转为""
     * @param strs          字符串数组
     * @return 连接后的字符串
     * @since 4.1.0
     */
    public static String concat(boolean isNullToEmpty, CharSequence... strs) {
        final StrBuilder sb = new StrBuilder();
        for (CharSequence str : strs) {
            sb.append(isNullToEmpty ? nullToEmpty(str) : str);
        }
        return sb.toString();
    }

    /**
     * 将给定字符串，变成 "xxx...xxx" 形式的字符串
     *
     * <ul>
     *     <li>abcdefgh  9 -》 abcdefgh</li>
     *     <li>abcdefgh  8 -》 abcdefgh</li>
     *     <li>abcdefgh  7 -》 ab...gh</li>
     *     <li>abcdefgh  6 -》 ab...h</li>
     *     <li>abcdefgh  5 -》 a...h</li>
     *     <li>abcdefgh  4 -》 a..h</li>
     *     <li>abcdefgh  3 -》 a.h</li>
     *     <li>abcdefgh  2 -》 a.</li>
     *     <li>abcdefgh  1 -》 a</li>
     *     <li>abcdefgh  0 -》 abcdefgh</li>
     *     <li>abcdefgh -1 -》 abcdefgh</li>
     * </ul>
     *
     * @param str       字符串
     * @param maxLength 结果的最大长度
     * @return 截取后的字符串
     */
    public static String brief(CharSequence str, int maxLength) {
        if (null == str) {
            return null;
        }
        final int strLength = str.length();
        if (maxLength <= 0 || strLength <= maxLength) {
            return str.toString();
        }

        // since 5.7.5，特殊长度
        switch (maxLength) {
            case 1:
                return String.valueOf(str.charAt(0));
            case 2:
                return str.charAt(0) + ".";
            case 3:
                return str.charAt(0) + "." + str.charAt(strLength - 1);
            case 4:
                return str.charAt(0) + ".." + str.charAt(strLength - 1);
        }

        final int suffixLength = (maxLength - 3) / 2;
        final int preLength = suffixLength + (maxLength - 3) % 2; // suffixLength 或 suffixLength + 1
        final String str2 = str.toString();
        return format("{}...{}",
                str2.substring(0, preLength),
                str2.substring(strLength - suffixLength));
    }


    /**
     * 循环位移指定位置的字符串为指定距离<br>
     * 当moveLength大于0向右位移，小于0向左位移，0不位移<br>
     * 当moveLength大于字符串长度时采取循环位移策略，即位移到头后从头（尾）位移，例如长度为10，位移13则表示位移3
     *
     * @param str          字符串
     * @param startInclude 起始位置（包括）
     * @param endExclude   结束位置（不包括）
     * @param moveLength   移动距离，负数表示左移，正数为右移
     * @return 位移后的字符串
     * @since 4.0.7
     */
    public static String move(CharSequence str, int startInclude, int endExclude, int moveLength) {
        if (isEmpty(str)) {
            return str(str);
        }
        int len = str.length();
        if (Math.abs(moveLength) > len) {
            // 循环位移，当越界时循环
            moveLength = moveLength % len;
        }
        final StringBuilder strBuilder = new StringBuilder(len);
        if (moveLength > 0) {
            int endAfterMove = Math.min(endExclude + moveLength, str.length());
            strBuilder.append(str.subSequence(0, startInclude))//
                    .append(str.subSequence(endExclude, endAfterMove))//
                    .append(str.subSequence(startInclude, endExclude))//
                    .append(str.subSequence(endAfterMove, str.length()));
        } else if (moveLength < 0) {
            int startAfterMove = Math.max(startInclude + moveLength, 0);
            strBuilder.append(str.subSequence(0, startAfterMove))//
                    .append(str.subSequence(startInclude, endExclude))//
                    .append(str.subSequence(startAfterMove, startInclude))//
                    .append(str.subSequence(endExclude, str.length()));
        } else {
            return str(str);
        }
        return strBuilder.toString();
    }



    /**
     * 对字符串归一化处理，如 "Á" 可以使用 "u00C1"或 "u0041u0301"表示，实际测试中两个字符串并不equals<br>
     * 因此使用此方法归一为一种表示形式，默认按照W3C通常建议的，在NFC中交换文本。
     *
     * @param str 归一化的字符串
     * @return 归一化后的字符串
     * @see Normalizer#normalize(CharSequence, Normalizer.Form)
     * @since 5.7.16
     */
    public static String normalize(CharSequence str) {
        return Normalizer.normalize(str, Normalizer.Form.NFC);
    }

    /**
     * 在给定字符串末尾填充指定字符，以达到给定长度<br>
     * 如果字符串本身的长度大于等于length，返回原字符串
     *
     * @param str       字符串
     * @param fixedChar 补充的字符
     * @param length    补充到的长度
     * @return 补充后的字符串
     * @since 5.8.0
     */
    public static String fixLength(CharSequence str, char fixedChar, int length) {
        final int fixedLength = length - str.length();
        if (fixedLength <= 0) {
            return str.toString();
        }
        return str + repeat(fixedChar, fixedLength);
    }

    /**
     * <p>指定字符串数组中，是否包含空字符串。</p>
     * <p>如果传入参数对象不是为空,则返回false。如果字符串包含字母,不区分大小写,则返回true</p>
     *
     * @param str 对象
     * @return 如果为字符串, 是否有字母
     */
    public static boolean hasLetter(CharSequence str) {
        if (null == str) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (CharUtil.isLetter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

}
