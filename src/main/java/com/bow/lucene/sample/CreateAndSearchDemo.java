package com.bow.lucene.sample;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import com.bow.lucene.sample.model.Article;

/**
 * 创建一个索引库，把一个信息加入到索引库中、把信息从索引库中检索出来
 */
public class CreateAndSearchDemo {
	/**
	 * 创建索引
	 */
	@Test
	public void createIndex() throws Exception {
		// 1创建article对象
		Article article = new Article();
		article.setId(1L);
		article.setTitle("NBA TEST");
		article.setContent("LBJ KB TMAC TEST words");

		// 创建索引库,创建IndexWriter对象
		Directory directory = FSDirectory.open(Paths.get("index"));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		IndexWriter writer = new IndexWriter(directory, iwc);

		// 把article对象转化成document
		Document document = new Document();

		Field id = new LongPoint("id", article.getId());
		Field title = new StringField("title", article.getTitle(), Store.YES);
		Field content = new TextField("contents", article.getContent(), Store.YES);
		document.add(id);
		document.add(title);
		document.add(content);

		// 信息放入到索引库
		writer.addDocument(document);
		writer.close();
	}

	/**
	 * 进行检索
	 */
	@Test
	public void searchIndex() throws Exception {
		// 创建IndexSearch对象
		Directory directory = FSDirectory.open(Paths.get("index"));
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(reader);

		// 创建query
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser queryParser = new QueryParser("contents", analyzer);
		Query query = queryParser.parse("words");

		// 第二个参数为n，提取前n条记录
		TopDocs results = indexSearcher.search(query, 5);
		// 根据关键词检索出来的总的记录数
		System.out.println("Find " + results.totalHits);
		ScoreDoc[] scoreDocs = results.scoreDocs;
		for (int i = 0; i < scoreDocs.length; i++) {
			int index = scoreDocs[i].doc;
			Document document = indexSearcher.doc(index);
			System.out.println(document);
		}
	}
}