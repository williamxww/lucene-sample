package com.bow.lucene.sample.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.store.FSDirectory;

public class SearchUtil {
	public static final Analyzer analyzer = new StandardAnalyzer();

	/**
	 * 获取IndexSearcher对象（适合单索引目录查询使用）
	 * 
	 * @param indexPath 索引目录
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static IndexSearcher getIndexSearcher(String indexPath, ExecutorService service, boolean realtime)
			throws IOException, InterruptedException {
		DirectoryReader reader = DirectoryReader.open(IndexUtil.getIndexWriter(indexPath, true));
		IndexSearcher searcher = new IndexSearcher(reader, service);
		if (service != null) {
			service.shutdown();
		}
		return searcher;
	}

	/**
	 * 多目录多线程查询
	 * 
	 * @param parentPath 父级索引目录
	 * @param service 多线程查询
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static IndexSearcher getMultiSearcher(String parentPath, ExecutorService service, boolean realtime)
			throws IOException, InterruptedException {
		MultiReader multiReader;
		File file = new File(parentPath);
		File[] files = file.listFiles();
		IndexReader[] readers = new IndexReader[files.length];
		if (!realtime) {
			for (int i = 0; i < files.length; i++) {
				readers[i] = DirectoryReader.open(FSDirectory.open(Paths.get(files[i].getPath())));
			}
		} else {
			for (int i = 0; i < files.length; i++) {
				readers[i] = DirectoryReader.open(IndexUtil.getIndexWriter(files[i].getPath(), true));
			}
		}

		multiReader = new MultiReader(readers);
		IndexSearcher searcher = new IndexSearcher(multiReader, service);
		if (service != null) {
			service.shutdown();
		}
		return searcher;
	}

	/**
	 * 对多个条件进行排序构建排序条件
	 * 
	 * @param fields
	 * @param types
	 * @param reverses
	 * @return
	 */
	public static Sort getSortInfo(String[] fields, Type[] types, boolean[] reverses) {
		SortField[] sortFields = null;
		int fieldLength = fields.length;
		int typeLength = types.length;
		int reverLength = reverses.length;
		if (!(fieldLength == typeLength) || !(fieldLength == reverLength)) {
			return null;
		} else {
			sortFields = new SortField[fields.length];
			for (int i = 0; i < fields.length; i++) {
				sortFields[i] = new SortField(fields[i], types[i], reverses[i]);
			}
		}
		return new Sort(sortFields);
	}

	/**
	 * 根据查询器、查询条件、每页数、排序条件进行查询
	 * 
	 * @param query 查询条件
	 * @param first 起始值
	 * @param max 最大值
	 * @param sort 排序条件
	 * @return
	 */
	public static TopDocs getScoreDocsByPerPageAndSortField(IndexSearcher searcher, Query query, int first, int max,
			Sort sort) {
		try {
			if (query == null) {
				System.out.println(" Query is null return null ");
				return null;
			}
			TopFieldCollector collector = null;
			if (sort != null) {
				collector = TopFieldCollector.create(sort, first + max, false, false, false);
			} else {
				sort = new Sort(new SortField[] { new SortField("modified", Type.LONG) });
				collector = TopFieldCollector.create(sort, first + max, false, false, false);
			}
			searcher.search(query, collector);
			return collector.topDocs(first, max);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}

	/**
	 * 获取上次索引的id,增量更新使用
	 * 
	 * @return
	 */
	public static Integer getLastIndexBeanID(IndexReader multiReader) {
		Query query = new MatchAllDocsQuery();
		IndexSearcher searcher = null;
		searcher = new IndexSearcher(multiReader);
		SortField sortField = new SortField("id", Type.INT, true);
		Sort sort = new Sort(new SortField[] { sortField });
		TopDocs docs = getScoreDocsByPerPageAndSortField(searcher, query, 0, 1, sort);
		ScoreDoc[] scoreDocs = docs.scoreDocs;
		int total = scoreDocs.length;
		if (total > 0) {
			ScoreDoc scoreDoc = scoreDocs[0];
			Document doc = null;
			try {
				doc = searcher.doc(scoreDoc.doc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new Integer(doc.get("id"));
		}
		return 0;
	}
}
