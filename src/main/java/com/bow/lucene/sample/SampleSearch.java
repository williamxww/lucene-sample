package com.bow.lucene.sample;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * @author vv
 * @since 2018/7/1.
 */
public class SampleSearch {

	public static void main(String arg[]) throws Exception {
		Directory directory = FSDirectory.open(Paths.get("D:/mapreduce-out/lucenetmp/cache.txt"));
		Analyzer analyzer = new StandardAnalyzer();
		analyzer = new CJKAnalyzer();
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(reader);

		// Term 是查询的基本单位
		// 1.termQuery
		Query termQuery = new TermQuery(new Term("newsSource", "网易"));
		System.out.println("--- termQuery : " + termQuery.toString());

		// 2.BooleanQuery ,类似还提供RangeQuery范围搜索; PrefixQuery 前缀搜索 ;FuzzyQuery
		// 模糊搜索 ..etc
		Query a = new TermQuery(new Term("newsSource", "网"));
		Query b = new TermQuery(new Term("newsSource", "易"));
		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
		booleanQuery.add(a, BooleanClause.Occur.MUST);
		booleanQuery.add(b, BooleanClause.Occur.MUST);
		System.out.println("--- booleanQuery :" + booleanQuery.toString());

		// 3.用QueryParser 切词出 query
		System.out.println("lucene的当前版本 ： " + Version.LATEST);
		QueryParser parser = new QueryParser("newsSource", analyzer);
		parser.setDefaultOperator(QueryParser.AND_OPERATOR);// 默认term之间是or关系
		Query parserQuery = parser.parse("java lucene");
		System.out.println("--- parserQuery : " + parserQuery.toString());

		// 4.利用MultiFieldQueryParser实现对多Field查询
		String[] fields = { "newsName", "newsSource" };
		MultiFieldQueryParser mparser = new MultiFieldQueryParser(fields, analyzer);
		Query mQuery = mparser.parse("江苏");
		System.out.println("---- mQuery :" + mQuery);

		ScoreDoc[] docs = isearcher.search(termQuery, 10).scoreDocs;
		for (int i = 0; i < docs.length; i++) {
			System.out.println(docs[i].doc);
			System.out.println("searcher score :" + docs[i].score);
			Document hitDoc = isearcher.doc(docs[i].doc);
			System.out.println("--- explain : " + isearcher.explain(termQuery, docs[i].doc));
			System.out.println("newsId:" + hitDoc.get("newsId"));
			System.out.println("newsName:" + hitDoc.get("newsName"));
			System.out.println("publishDate:" + hitDoc.get("publishDate"));
			System.out.println("newsSource:" + hitDoc.get("newsSource"));
			System.out.println("newssummay:" + hitDoc.get("newssummay"));
			System.out.println("------------------------------------------");
		}
	}

}
