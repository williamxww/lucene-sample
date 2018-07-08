package com.bow.lucene.sample.analyzer;

import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.util.CharTokenizer;

/**
 * 根据空格标点符号等，将letter转换为word
 * 
 * @see LetterTokenizer
 * @see LowerCaseTokenizer
 * @author vv
 * @since 2018/7/8.
 */
public class MyTokenizer extends CharTokenizer {
	@Override
	protected boolean isTokenChar(int c) {
		// 只要不是字母就不是token
		return Character.isLetter(c);
	}
}
