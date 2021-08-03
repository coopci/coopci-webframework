package springless.string.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.stream.IntStream;

// 之所以要费劲巴拉地弄这个一个 delegate 是因为 java.lang.String 是 final的类。
public class DelegatedString implements java.io.Serializable, Comparable<DelegatedString>, CharSequence {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String delegate;

	public DelegatedString(String s) {
		this.delegate = s;
	}
	@Override
	public IntStream chars() {
		return delegate.chars();
	}

	@Override
	public IntStream codePoints() {
		return delegate.codePoints();
	}

	@Override
	public int length() {
		return delegate.length();
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public char charAt(int index) {
		return delegate.charAt(index);
	}

	public int codePointAt(int index) {
		return delegate.codePointAt(index);
	}

	public int codePointBefore(int index) {
		return delegate.codePointBefore(index);
	}

	public int codePointCount(int beginIndex, int endIndex) {
		return delegate.codePointCount(beginIndex, endIndex);
	}

	public int offsetByCodePoints(int index, int codePointOffset) {
		return delegate.offsetByCodePoints(index, codePointOffset);
	}

	public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
		delegate.getChars(srcBegin, srcEnd, dst, dstBegin);
	}

	@SuppressWarnings("deprecation")
	public void getBytes(int srcBegin, int srcEnd, byte[] dst, int dstBegin) {
		delegate.getBytes(srcBegin, srcEnd, dst, dstBegin);
	}

	public byte[] getBytes(String charsetName)
			throws UnsupportedEncodingException {
		return delegate.getBytes(charsetName);
	}

	public byte[] getBytes(Charset charset) {
		return delegate.getBytes(charset);
	}

	public byte[] getBytes() {
		return delegate.getBytes();
	}

	@Override
	public boolean equals(Object anObject) {
		return delegate.equals(anObject);
	}

	public boolean contentEquals(StringBuffer sb) {
		return delegate.contentEquals(sb);
	}

	public boolean contentEquals(CharSequence cs) {
		return delegate.contentEquals(cs);
	}

	public boolean equalsIgnoreCase(String anotherString) {
		return delegate.equalsIgnoreCase(anotherString);
	}

	@Override
	public int compareTo(DelegatedString anotherString) {
		return delegate.compareTo(anotherString.delegate);
	}

	public int compareToIgnoreCase(String str) {
		return delegate.compareToIgnoreCase(str);
	}

	public boolean regionMatches(int toffset, String other, int ooffset, int len) {
		return delegate.regionMatches(toffset, other, ooffset, len);
	}

	public boolean regionMatches(boolean ignoreCase, int toffset, String other,
			int ooffset, int len) {
		return delegate.regionMatches(ignoreCase, toffset, other, ooffset, len);
	}

	public boolean startsWith(String prefix, int toffset) {
		return delegate.startsWith(prefix, toffset);
	}

	public boolean startsWith(String prefix) {
		return delegate.startsWith(prefix);
	}

	public boolean endsWith(String suffix) {
		return delegate.endsWith(suffix);
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	public int indexOf(int ch) {
		return delegate.indexOf(ch);
	}

	public int indexOf(int ch, int fromIndex) {
		return delegate.indexOf(ch, fromIndex);
	}

	public int lastIndexOf(int ch) {
		return delegate.lastIndexOf(ch);
	}

	public int lastIndexOf(int ch, int fromIndex) {
		return delegate.lastIndexOf(ch, fromIndex);
	}

	public int indexOf(String str) {
		return delegate.indexOf(str);
	}

	public int indexOf(String str, int fromIndex) {
		return delegate.indexOf(str, fromIndex);
	}

	public int lastIndexOf(String str) {
		return delegate.lastIndexOf(str);
	}

	public int lastIndexOf(String str, int fromIndex) {
		return delegate.lastIndexOf(str, fromIndex);
	}

	public String substring(int beginIndex) {
		return delegate.substring(beginIndex);
	}

	public String substring(int beginIndex, int endIndex) {
		return delegate.substring(beginIndex, endIndex);
	}

	@Override
	public CharSequence subSequence(int beginIndex, int endIndex) {
		return delegate.subSequence(beginIndex, endIndex);
	}

	public String concat(String str) {
		return delegate.concat(str);
	}

	public String replace(char oldChar, char newChar) {
		return delegate.replace(oldChar, newChar);
	}

	public boolean matches(String regex) {
		return delegate.matches(regex);
	}

	public boolean contains(CharSequence s) {
		return delegate.contains(s);
	}

	public String replaceFirst(String regex, String replacement) {
		return delegate.replaceFirst(regex, replacement);
	}

	public String replaceAll(String regex, String replacement) {
		return delegate.replaceAll(regex, replacement);
	}

	public String replace(CharSequence target, CharSequence replacement) {
		return delegate.replace(target, replacement);
	}

	public String[] split(String regex, int limit) {
		return delegate.split(regex, limit);
	}

	public String[] split(String regex) {
		return delegate.split(regex);
	}

	public String toLowerCase(Locale locale) {
		return delegate.toLowerCase(locale);
	}

	public String toLowerCase() {
		return delegate.toLowerCase();
	}

	public String toUpperCase(Locale locale) {
		return delegate.toUpperCase(locale);
	}

	public String toUpperCase() {
		return delegate.toUpperCase();
	}

	public String trim() {
		return delegate.trim();
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

	public char[] toCharArray() {
		return delegate.toCharArray();
	}

	public String intern() {
		return delegate.intern();
	}
	
}
