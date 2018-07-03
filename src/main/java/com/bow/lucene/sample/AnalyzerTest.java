package com.bow.lucene.sample;

import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttributeImpl;
import org.apache.lucene.util.Version;
import org.junit.Test;

/**
 * 英文的分词器 中文的分词器
 */
public class AnalyzerTest {

	/**
	 * 英文分词：(Lucene自带包)
	 * @throws Exception
	 */
	@Test
	public void testEN() throws Exception {
		String text = "Creates a searcher searching the index in the named directory";
		Analyzer analyzer = new StandardAnalyzer();
		this.testAnalyzer(analyzer, text);
	}


	/**
	 * 输出分词后的结果
	 * @param analyzer
	 * @param text
	 * @throws Exception
	 */
	private void testAnalyzer(Analyzer analyzer, String text) throws Exception {
		TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));
		tokenStream.addAttribute(CharTermAttribute.class);
		while (tokenStream.incrementToken()) {
			CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
			System.out.println(termAttribute.toString());
		}
	}
}
