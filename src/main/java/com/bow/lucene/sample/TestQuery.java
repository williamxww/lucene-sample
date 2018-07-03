package com.bow.lucene.sample;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


/**
 * @author vv
 * @since 2018/7/1.
 */
public class TestQuery {

    public static void main(String[] args) throws IOException {
        TopDocs topDoc = null ;
        String queryString = "中华" ;
        Query query = null ;
        Directory directory = FSDirectory.open(new File("D:/mapreduce-out/lucenetmp/demo2"));
        IndexSearcher search = new IndexSearcher(directory) ;

        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

        try {
            QueryParser qp = new QueryParser("body", analyzer) ;
            query = qp.parse(queryString);
        } catch (ParseException e) {
            e.printStackTrace() ;
        }
        if(search != null) {
            topDoc = search.search(query, 100);
            if (topDoc.getMaxScore() > 0) {
                System.out.println("topDoc.totalHits" + topDoc.totalHits);
                System.out.println("topDoc.getMaxScore()" + topDoc.getMaxScore());
                System.out.println("topDoc.toString()" + topDoc.toString());
            } else {
                System.out.println("没有查询到结果");
            }
        }

    }

}
