package com.bow.lucene.sample.demo;

import java.io.IOException;
import java.util.concurrent.Executors;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import com.lucene.search.SearchUtil;

public class TestSearch {
	public static void main(String[] args) {
		try {
			IndexSearcher searcher = SearchUtil.getMultiSearcher("index", Executors.newCachedThreadPool(), false);
			Query query = SearchUtil.getQuery("content", "string", "lucene", false);
			TopDocs topDocs = SearchUtil.getScoreDocsByPerPageAndSortField(searcher, query, 0, 20, null);
			System.out.println("符合条件的数据总数："+topDocs.totalHits);
			System.out.println("本次查询到的数目为："+topDocs.scoreDocs.length);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (ScoreDoc scoreDoc : scoreDocs) {
				Document doc = searcher.doc(scoreDoc.doc);
				System.out.println(doc.get("path"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
