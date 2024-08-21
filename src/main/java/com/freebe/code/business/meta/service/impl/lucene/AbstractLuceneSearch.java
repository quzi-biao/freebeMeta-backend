package com.freebe.code.business.meta.service.impl.lucene;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.freebe.code.business.base.vo.BaseVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.KeyWordsQueryParam;
import com.freebe.code.util.PageUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class AbstractLuceneSearch<T extends BaseVO> {
	
	@SuppressWarnings("rawtypes")
	private Class clazz;
	
	/**
	 * 获取索引
	 * @return
	 */
	public abstract String getIndex();
	
	/**
	 * 添加索引
	 * @return
	 */
	public abstract void addIndex(T vo, Document doc);
	
	protected abstract Query buildQuery(KeyWordsQueryParam param, Analyzer analyzer) throws ParseException;
	
	/**
	 * 添加索引
	 * @param entity
	 * @throws CustomException 
	 */
	public void addOrUpdateIndex(T vo) throws CustomException {
		IndexWriter indexWriter = null;
        IndexReader indexReader = null;
        Directory directory = null;
        Analyzer analyzer = null;
        try {
            //创建索引目录文件
            File indexFile = new File(getIndex());
            File[] files = indexFile.listFiles();
            analyzer = new IKAnalyzer();
            directory = FSDirectory.open(Paths.get(getIndex()));
            IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory, writerConfig);
            
            Document doc = new Document();
            //查询是否有该索引，没有添加，有则更新
            TopDocs topDocs = null;
            if (files != null && files.length != 0) {
                Query query = new TermQuery(new Term("id", String.valueOf(vo.getId())));
                indexReader = DirectoryReader.open(directory);
                IndexSearcher indexSearcher = new IndexSearcher(indexReader);
                topDocs = indexSearcher.search(query, 1);
            }
			
            this.addIndex(vo, doc);
            
            //如果没有查询结果，添加
            if (topDocs == null || topDocs.totalHits == 0) {
                indexWriter.addDocument(doc);
            } else {
                indexWriter.updateDocument(new Term("id", String.valueOf(vo.getId())), doc);
            }
        } catch (Exception e) {
            throw new CustomException("添加索引库出错：" + e.getMessage());
        } finally {
            closeResources(indexWriter, indexReader, directory, analyzer);
        }
	}
	
	/**
	 * 删除索引
	 * @param entity
	 * @throws CustomException 
	 */
    public void deleteIndex(T entity) throws CustomException {
        //删除全文检索
        IndexWriter indexWriter = null;
        Directory directory = null;
        try (Analyzer analyzer = new IKAnalyzer()) {
            directory = FSDirectory.open(Paths.get(getIndex()));
            IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory, writerConfig);
            //根据id字段进行删除
            indexWriter.deleteDocuments(new Term("id", String.valueOf(entity.getId())));
        } catch (Exception e) {
            throw new CustomException("删除索引库出错：" + e.getMessage());
        } finally {
        	closeResources(indexWriter, null, directory, null);
        }
    }
    
    /**
	 * 全文搜索
	 * @param keyWord
	 * @param page
	 * @param limit
	 * @return
	 * @throws CustomException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<T> fullTextSearch(KeyWordsQueryParam param) throws CustomException {
        List<T> searchList = new ArrayList<>(10);
        File indexFile = new File(this.getIndex());
        File[] files = indexFile.listFiles();
        
        //沒有索引文件，不然沒有查詢結果
        if (files == null || files.length == 0) {
        	return new PageImpl(new ArrayList());
        }
        
        Sort sort = null;
        if(null != param.getOrder() && param.getOrder().length() == 0) {
        	sort = new Sort(new SortField(param.getOrder(), SortField.Type.LONG, true));
        	
        }
        IndexReader indexReader = null;
        Directory directory = null;
        try (Analyzer analyzer = new IKAnalyzer()) {
            directory = FSDirectory.open(Paths.get(this.getIndex()));
            //多项查询条件
            Query query = buildQuery(param, analyzer);
            indexReader = DirectoryReader.open(directory);
            //索引查询对象
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            TopDocs topDocs = sort == null ? indexSearcher.search(query, 1) : indexSearcher.search(query, 1, sort);
            //获取条数
            int total = (int) topDocs.totalHits;
            //获取结果集
            ScoreDoc lastSd = null;
            if (param.getCurrPage() > 1) {
                int num = (int) (param.getLimit() * param.getCurrPage());
                TopDocs tds = sort == null ? indexSearcher.search(query, num) : indexSearcher.search(query, num, sort);
                lastSd = tds.scoreDocs[num - 1];
            }
            //通过最后一个元素去搜索下一页的元素 如果lastSd为null，查询第一页
            TopDocs tds = sort == null ? indexSearcher.searchAfter(lastSd, query, (int) param.getLimit()) : indexSearcher.searchAfter(lastSd, query, (int) param.getLimit(), sort);
            //遍历查询结果 把标题和内容替换为带高亮的最佳摘要
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = indexSearcher.doc(sd.doc);
                T t = (T) this.getClazz().newInstance();
                
                Integer id = Integer.parseInt(doc.get("id"));
                t.setId(id.longValue());
                
                searchList.add(t);
            }
            
            PageRequest pageInfo = PageUtils.toPageRequest(param);
            PageImpl<T> pageData = new PageImpl<>(searchList, pageInfo, total);
            return pageData;
        } catch (Exception e) {
            throw new CustomException("全文检索出错：" + e.getMessage(), e);
        } finally {
        	closeResources(null, indexReader, directory, null);
        }
    }


	protected void closeResources(IndexWriter indexWriter, IndexReader indexReader, Directory directory, Analyzer analyzer) {
		if (indexWriter != null) {
		    try {
		        indexWriter.close();
		    } catch (IOException e) {
		    	log.error(e.getMessage(), e);
		    }
		}
		if (indexReader != null) {
		    try {
		        indexReader.close();
		    } catch (IOException e) {
		    	log.error(e.getMessage(), e);
		    }
		}
		if (directory != null) {
		    try {
		        directory.close();
		    } catch (IOException e) {
		    	log.error(e.getMessage(), e);
		    }
		}
		if (analyzer != null) {
		    analyzer.close();
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private <S extends T> Class<S> getClazz() {
		if (this.clazz == null) {
			Type genType = this.getClass().getGenericSuperclass();
			if (!(genType instanceof ParameterizedType)) {
				return null;
			}
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
			if (!(params[0] instanceof Class)) {
				return null;
			}
			return (Class<S>) params[0];
		}
		return this.clazz;
	}
	
}
