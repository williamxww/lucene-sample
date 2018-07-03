package com.bow.lucene.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


/**
 * @author vv
 * @since 2018/7/1.
 */
public class TextFileIndexer {
    public static void main(String[] args) throws IOException {
        //致命要索引文件夹的位置
        File fileDir = new File("D:/mapreduce-out/lucenetmp/demo1") ;

        //这里放索引文件的位置
        File indexDir = new File("D:/mapreduce-out/lucenetmp/demo2") ;
        //此处的indexDir应该是放置生成缓存的文件夹
        Directory docx = FSDirectory.open(indexDir);
        Analyzer luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_CURRENT) ;
        IndexWriter.MaxFieldLength mf = new MaxFieldLength(100);
        IndexWriter indexWriter = new IndexWriter(docx, luceneAnalyzer, mf) ;
        File[] textFiles = fileDir.listFiles();
        long startTime = new Date().getTime();

        for(int i=0;i<textFiles.length;i++) {
            if(textFiles[i].isFile() && textFiles[i].getName().endsWith(".txt")) {
                System.out.println("文件 " + textFiles[i].getCanonicalPath() + "正在呗索引") ;
                String temp = fileReaderAll(textFiles[i].getCanonicalPath(), "GBK") ;
                System.out.println("temp = " + temp);
                Document document = new Document();
                Field fieldPath = new Field("path", textFiles[i].getPath(),Field.Store.YES, Field.Index.NO) ;
                Field fieldBody = new Field("body", temp, Field.Store.YES, Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS) ;
                document.add(fieldPath);
                document.add(fieldBody);
                indexWriter.addDocument(document);
            }
        }

        //optimize()方法是对索引进行优化
        indexWriter.optimize();
        indexWriter.close();

        long endTime = new Date().getTime();
        System.out.println("这花费了" + (endTime - startTime) + " 毫秒来把文档增加到索引里面去!" + fileDir.getPath());
    }

    public static String fileReaderAll(String fileName, String charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),charset));

        String line = new String() ;
        String temp = new String() ;
        while((line = reader.readLine()) != null) {
            temp += line ;
        }
        reader.close();
        return temp ;
    }

}
