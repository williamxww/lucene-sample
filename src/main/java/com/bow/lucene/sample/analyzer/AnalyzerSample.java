package com.bow.lucene.sample.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;
import java.io.StringReader;

/**
 * Lucene分词器解析 分词器就是把给定的文本按照某种规则分解成最小词汇 Analyzer是所有分词器的抽象父类<br/>
 * TokenSteam是抽象父类中一个final方法，该方法用于把文本内容分解成最小单元SteamToken<br/>
 * StreamToken是分解成的词汇单元，它有两个实现类Tokenizer和TokenFilter<br/>
 * Tokenizer用于分解词汇单元<br/>
 * TokenFilter用于过滤词汇单元<br/>
 * StreamToken.incrementToken 判断是否还有下个词汇单元，属于生产者<br/>
 * CharTermAttribute 词汇属性<br/>
 * PositionIncrementAttribute 位置增量属性<br/>
 * OffsetAttribute 偏移量属性<br/>
 * TypeAttribute 分词类型属性<br/>
 * 
 * @author vv
 * @since 2018/7/8.
 */
public class AnalyzerSample {

	public static void main(String[] args) {
		String text = "What you think,you become. Hello baby!";
//		displayTokenInfo(text, new StandardAnalyzer());
//		displayTokenInfo(text, new SimpleAnalyzer());
		displayTokenInfo(text, new MyStopAnalyzer());
	}

	public static void displayTokenInfo(String text, Analyzer analyzer) {
		// 获取词汇流
		TokenStream stream = analyzer.tokenStream("", new StringReader(text));
		// 查看词汇属性
		CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
		// 位置偏移量属性
		PositionIncrementAttribute pia = stream.addAttribute(PositionIncrementAttribute.class);
		// 偏移量属性
		OffsetAttribute oa = stream.addAttribute(OffsetAttribute.class);
		// 查看分词类型属性
		TypeAttribute ta = stream.addAttribute(TypeAttribute.class);
		System.out.println("--------------------" + analyzer.getClass().getSimpleName() + "--------------------");
		try {
			// 重置下streamToken对象
			stream.reset();
			// 判断是否还有下一个token
			while (stream.incrementToken()) {
				System.out.println("Type:" + ta.type() + "     Position:" + pia.getPositionIncrement() + "     Offset:["
						+ oa.startOffset() + "-" + oa.endOffset() + "]    CharTerm:" + cta);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
