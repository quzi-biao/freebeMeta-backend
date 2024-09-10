package com.freebe.code.business.meta.service.impl.lucene;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedNumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.freebe.code.business.meta.vo.BountyVO;
import com.freebe.code.common.KeyWordsQueryParam;

/**
 * Lucene 全文检索
 * @author zhengbiaoxie
 *
 */
@Component
public class BountyLuceneSearch extends AbstractLuceneSearch<BountyVO> {
	@Value("${lucene.taskIndexLibrary}")
	private String luceneIndex;
	
	public String getIndex() {
		return luceneIndex;
	}
	
	public void addIndex(BountyVO vo, Document doc) {
		 doc.add(new StringField("id", String.valueOf(vo.getId()), Field.Store.YES));
         doc.add(new TextField("title", vo.getTitle(), Field.Store.YES));
        
         if(!StringUtils.isEmpty(vo.getDescription())) {
         	doc.add(new TextField("description", vo.getDescription(), Field.Store.YES));
         }
         
         doc.add(new SortedNumericDocValuesField("createTime", vo.getCreateTime()));
	}
	
	
    /**
     * 构建查询参数
     * @param param
     * @param analyzer
     * @return
     * @throws ParseException
     */
    public Query buildQuery(KeyWordsQueryParam param, Analyzer analyzer) throws ParseException {
		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		QueryParser queryParser = new MultiFieldQueryParser(new String[]{"title", "description"}, analyzer);
        Query keyQuery = queryParser.parse(param.getKeyWords());
        
		queryBuilder.add(keyQuery, BooleanClause.Occur.MUST);
		
		return queryBuilder.build();
	}
    
}
