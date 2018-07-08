package com.bow.lucene.sample.analyzer;

import java.io.IOException;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

/**
 * @see StopFilter
 * @see FilteringTokenFilter
 * @author vv
 * @since 2018/7/8.
 */
public class MyStopFilter extends TokenFilter {

	private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final CharArraySet stopWords;
	private int skippedPositions;

	public MyStopFilter(TokenStream in, CharArraySet stopWords) {
		super(in);
		this.stopWords = stopWords;
	}

//	@Override
//	public boolean incrementToken() throws IOException {
//		while (input.incrementToken()) {
//			if (accept()) {
//				int wordPos = posIncrAtt.getPositionIncrement();
//				System.out.println(wordPos);
//				return true;
//			}
//		}
//		return false;
//	}

	@Override
	public final boolean incrementToken() throws IOException {
		skippedPositions = 0;
		while (input.incrementToken()) {
			System.out.println(">>"+skippedPositions);
			if (accept()) {
				if (skippedPositions != 0) {
					posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement() + skippedPositions);
				}
				return true;
			}
			skippedPositions += posIncrAtt.getPositionIncrement();
		}

		return false;
	}


	private boolean accept() {
		return !stopWords.contains(termAtt.buffer(), 0, termAtt.length());
	}
}
