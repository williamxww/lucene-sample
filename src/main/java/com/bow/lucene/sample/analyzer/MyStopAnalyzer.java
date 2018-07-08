package com.bow.lucene.sample.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.util.Arrays;

/**
 * @see StopAnalyzer
 * @see SimpleAnalyzer
 * @author vv
 * @since 2018/7/8.
 */
public class MyStopAnalyzer extends Analyzer {

	public static final CharArraySet ENGLISH_STOP_WORDS_SET = StandardAnalyzer.ENGLISH_STOP_WORDS_SET;
	// 把you当做STOP WORD
	public static final CharArraySet MY_STOP_WORDS_SET = new CharArraySet( Arrays.asList("you"), false);

	@Override
	protected TokenStreamComponents createComponents(final String fieldName) {
		final Tokenizer source = new MyTokenizer();
		return new TokenStreamComponents(source, new MyStopFilter(source, MY_STOP_WORDS_SET));
	}

	@Override
	protected TokenStream normalize(String fieldName, TokenStream in) {
		// return new LowerCaseFilter(in);
		// 不做任何处理
        System.out.println("enter normalize");
        return in;
	}
}
