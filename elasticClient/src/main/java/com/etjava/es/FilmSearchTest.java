package com.etjava.es;

import java.net.InetAddress;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 	���Բ�ѯ���� - ��ѯ����
 * @author etjav
 *
 */
public class FilmSearchTest {

	public static final String URL="192.168.199.127";
	public static final Integer PORT=9300;
	
	private static final String CLUSTER_NAME="my-application"; // ��Ⱥ����
	private static Settings.Builder settings = Settings.builder().put("cluster.name",CLUSTER_NAME);
	
	private TransportClient client = null;
	
	/**
	 * 	�������ӿͻ���
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	@Before
	public void getClient() throws Exception{
		// ���ڵ�����
//		client = new PreBuiltTransportClient(Settings.EMPTY)
//		        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(URL), PORT));
		// ��Ⱥ����
		client = new PreBuiltTransportClient(settings.build())
		        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(URL), PORT));
	}
	
	/**
	 * �ر�����
	 */
	@After
	public void close() {
		if(client!=null)
			client.close();
	}
	
	/**
	 * 	��ѯ�������� restful���
	 */
	@Test
	public void searchAll() {
		// �������� prepareSearch��ָ�����������ָ���������
		// �൱�� http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		// ��ѯ��������
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.matchAllQuery())
			.execute() // ִ�в�ѯ
			.actionGet();// ��������
		
		// ������� ���ص�JSON�����ڵ����hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
		}
	}
	
	/**
	 * 	����ҳ�Ĳ�ѯ
	 */
	@Test
	public void searchWithLimit() {
		// �������� prepareSearch��ָ�����������ָ���������
		// �൱�� http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		// ��ѯ��������
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.matchAllQuery())
				.setFrom(0) // ��ʼҳ��С
				.setSize(2) // ÿҳ��¼��
				.execute() // ִ�в�ѯ
				.actionGet();// ��������
		
		// ������� ���ص�JSON�����ڵ����hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
		}
	}
	
	/**
	 * 	����ҳ������Ĳ�ѯ
	 */
	@Test
	public void searchWithLimitAndOrder() {
		// �������� prepareSearch��ָ�����������ָ���������
		// �൱�� http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		// ��ѯ��������
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.matchAllQuery())
				.setFrom(0) // ��ʼҳ��С
				.setSize(2) // ÿҳ��¼��
				.addSort("publishDate", SortOrder.DESC) // ��������
				.addSort("price", SortOrder.ASC) // ��������
				.execute() // ִ�в�ѯ
				.actionGet();// ��������
		
		// ������� ���ص�JSON�����ڵ����hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
		}
	}
	
	
	/**
	 * 	ֻ��ѯtitle��price�����е�����
	 */
	@Test
	public void searchInclude() {
		// �������� prepareSearch��ָ�����������ָ���������
		// �൱�� http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		// ��ѯ��������
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.matchAllQuery())
				.setFrom(0) // ��ʼҳ��С
				.setSize(2) // ÿҳ��¼��
				.setFetchSource(new String[]{"title","price"}, null) // ֻ��ѯtitle,price�����е�����
				.execute() // ִ�в�ѯ
				.actionGet();// ��������
		
		// ������� ���ص�JSON�����ڵ����hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
		}
	}
	
	/**
	 * 	��ѯ��title��price�������������
	 */
	@Test
	public void searchExclude() {
		// �������� prepareSearch��ָ�����������ָ���������
		// �൱�� http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		// ��ѯ��������
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.matchAllQuery())
				.setFrom(0) // ��ʼҳ��С
				.setSize(2) // ÿҳ��¼��
				// String[] includes, String[] excludes
				.setFetchSource(null,new String[]{"title","price"}) // ��ѯ��title,price�������������
				.execute() // ִ�в�ѯ
				.actionGet();// ��������
		
		// ������� ���ص�JSON�����ڵ����hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
		}
	}
	
	/**
	 * 	��������ѯ
	 */
	@Test
	public void searchWithCondition() {
		// �������� prepareSearch��ָ�����������ָ���������
		// �൱�� http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		// ��ѯ��������
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.matchQuery("title", "ս"))// ��ѯ����
				.setFrom(0) // ��ʼҳ��С
				.setSize(2) // ÿҳ��¼��
				// String[] includes, String[] excludes
				.setFetchSource(new String[]{"title","price"},null) // ֻ��ѯtitle,price�����е�����
				.execute() // ִ�в�ѯ
				.actionGet();// ��������
		
		// ������� ���ص�JSON�����ڵ����hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
		}
	}
	
	/**
	 * 	ָ�����ֶθ�����ʾ
	 */
	@Test
	public void searchHighlight() {
		// �������� prepareSearch��ָ�����������ָ���������
		// �൱�� http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		
		// ���ø���
		HighlightBuilder highlightBuilder=new HighlightBuilder();
	    highlightBuilder.preTags("<h2>");
	    highlightBuilder.postTags("</h2>");
	    highlightBuilder.field("title");
		
		// ��ѯ��������
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.matchQuery("title", "ս"))// ��ѯ����
				.highlighter(highlightBuilder)
				.setFrom(0) // ��ʼҳ��С
				.setSize(2) // ÿҳ��¼��
				// String[] includes, String[] excludes
				.setFetchSource(new String[]{"title","price"},null) // ֻ��ѯtitle,price�����е�����
				.execute() // ִ�в�ѯ
				.actionGet();// ��������
		
		// ������� ���ص�JSON�����ڵ����hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
			System.out.println(hit.getHighlightFields());
		}
	}
	
	/**
	 * 	��������ѯ(����) ������ʾtitle
	 */
	@Test
	public void searchManyConditionWithHighlight() {
		// �������� prepareSearch��ָ�����������ָ���������
		// �൱�� http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		
		// ���ø���
		HighlightBuilder highlightBuilder=new HighlightBuilder();
	    highlightBuilder.preTags("<h2><font>");
	    highlightBuilder.postTags("</font></h2>");
	    highlightBuilder.field("title");
	    
	    // ��ѯ���� matchPhraseQuery �̴ʻ��ѯ
		org.elasticsearch.index.query.QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("title", "ս");
		org.elasticsearch.index.query.QueryBuilder queryBuilder2 = QueryBuilders.matchPhraseQuery("content", "����");
		// ��ѯ��������
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.boolQuery()
				.must(queryBuilder)
				.must(queryBuilder2))// ��ѯ����
				.highlighter(highlightBuilder)
				.setFrom(0) // ��ʼҳ��С
				.setSize(2) // ÿҳ��¼��
				// String[] includes, String[] excludes
				.setFetchSource(new String[]{"title","price"},null) // ֻ��ѯtitle,price�����е�����
				.execute() // ִ�в�ѯ
				.actionGet();// ��������
		
		// ������� ���ص�JSON�����ڵ����hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
			System.out.println(hit.getHighlightFields());
		}
	}
	
	/**
	 * 	��������ѯ(������) ������ʾtitle
	 */
	@Test
	public void searchManyConditionWithHighlightAndMustNot() {
		// �������� prepareSearch��ָ�����������ָ���������
		// �൱�� http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		
		// ���ø���
		HighlightBuilder highlightBuilder=new HighlightBuilder();
	    highlightBuilder.preTags("<h2><font>");
	    highlightBuilder.postTags("</font></h2>");
	    highlightBuilder.field("title");
	    
	    // ��ѯ���� matchPhraseQuery �̴ʻ��ѯ
		org.elasticsearch.index.query.QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("title", "ս");
		org.elasticsearch.index.query.QueryBuilder queryBuilder2 = QueryBuilders.matchPhraseQuery("content", "����");
		// ��ѯ��������
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.boolQuery()
				.must(queryBuilder)
				.mustNot(queryBuilder2))// ��ѯ����
				.highlighter(highlightBuilder)
				.setFrom(0) // ��ʼҳ��С
				.setSize(2) // ÿҳ��¼��
				// String[] includes, String[] excludes
				.setFetchSource(new String[]{"title","price"},null) // ֻ��ѯtitle,price�����е�����
				.execute() // ִ�в�ѯ
				.actionGet();// ��������
		
		// ������� ���ص�JSON�����ڵ����hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
			System.out.println(hit.getHighlightFields());
		}
	}
	
	/**
	 * 	��������ѯ��ߵ÷�(���ƾ�ȷ�� Ҳ�������Ȩ��) ������ʾtitle
	 */
	@Test
	public void searchShould() {
		// �������� prepareSearch��ָ�����������ָ���������
		// �൱�� http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		
		// ���ø���
		HighlightBuilder highlightBuilder=new HighlightBuilder();
	    highlightBuilder.preTags("<h2><font>");
	    highlightBuilder.postTags("</font></h2>");
	    highlightBuilder.field("title");
	    
	    // ��ѯ���� matchPhraseQuery �̴ʻ��ѯ
		org.elasticsearch.index.query.QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("title", "ս");
		org.elasticsearch.index.query.QueryBuilder queryBuilder2 = QueryBuilders.matchPhraseQuery("content", "����");
		// ��Χ��ѯ gt С��
		org.elasticsearch.index.query.QueryBuilder queryBuilder3 = QueryBuilders.rangeQuery("publisDate").gt("2018-01-01");
		// ��ѯ��������
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.boolQuery()
				.must(queryBuilder)
				.must(queryBuilder2)
				.should(queryBuilder2)
				.should(queryBuilder3))// ��ѯ����
				
				.highlighter(highlightBuilder)// ����
				.setFrom(0) // ��ʼҳ��С
				.setSize(2) // ÿҳ��¼��
				// String[] includes, String[] excludes
				.setFetchSource(new String[]{"title","price"},null) // ֻ��ѯtitle,price�����е�����
				.execute() // ִ�в�ѯ
				.actionGet();// ��������
		
		// ������� ���ص�JSON�����ڵ����hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
			System.out.println(hit.getHighlightFields());
		}
	}
	
	/**
	 * 	��ѯƱ��С��40������
	 * @throws Exception
	 */
	@Test
	public void searchMutil4()throws Exception{
	    SearchRequestBuilder srb=client.prepareSearch("film").setTypes("dongzuo");
	    QueryBuilder queryBuilder=QueryBuilders.matchPhraseQuery("title", "ս");
	    QueryBuilder queryBuilder2=QueryBuilders.rangeQuery("price").lte(40);
	    SearchResponse sr=srb.setQuery(QueryBuilders.boolQuery()
	            .must(queryBuilder)
	            .filter(queryBuilder2))
	        .execute()
	        .actionGet(); 
	    SearchHits hits=sr.getHits();
	    for(SearchHit hit:hits){
	        System.out.println(hit.getSourceAsString());
	    }
	}
}
