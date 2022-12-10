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
 * 	测试查询操作 - 查询数据
 * @author etjav
 *
 */
public class FilmSearchTest {

	public static final String URL="192.168.199.127";
	public static final Integer PORT=9300;
	
	private static final String CLUSTER_NAME="my-application"; // 集群名称
	private static Settings.Builder settings = Settings.builder().put("cluster.name",CLUSTER_NAME);
	
	private TransportClient client = null;
	
	/**
	 * 	创建连接客户端
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	@Before
	public void getClient() throws Exception{
		// 单节点连接
//		client = new PreBuiltTransportClient(Settings.EMPTY)
//		        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(URL), PORT));
		// 集群连接
		client = new PreBuiltTransportClient(settings.build())
		        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(URL), PORT));
	}
	
	/**
	 * 关闭连接
	 */
	@After
	public void close() {
		if(client!=null)
			client.close();
	}
	
	/**
	 * 	查询所有内容 restful风格
	 */
	@Test
	public void searchAll() {
		// 定义连接 prepareSearch可指定多个索引及指定多个类型
		// 相当于 http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		// 查询所有数据
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.matchAllQuery())
			.execute() // 执行查询
			.actionGet();// 返回数据
		
		// 遍历结果 返回的JSON串根节点叫做hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
		}
	}
	
	/**
	 * 	带分页的查询
	 */
	@Test
	public void searchWithLimit() {
		// 定义连接 prepareSearch可指定多个索引及指定多个类型
		// 相当于 http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		// 查询所有数据
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.matchAllQuery())
				.setFrom(0) // 起始页大小
				.setSize(2) // 每页记录数
				.execute() // 执行查询
				.actionGet();// 返回数据
		
		// 遍历结果 返回的JSON串根节点叫做hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
		}
	}
	
	/**
	 * 	带分页带排序的查询
	 */
	@Test
	public void searchWithLimitAndOrder() {
		// 定义连接 prepareSearch可指定多个索引及指定多个类型
		// 相当于 http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		// 查询所有数据
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.matchAllQuery())
				.setFrom(0) // 起始页大小
				.setSize(2) // 每页记录数
				.addSort("publishDate", SortOrder.DESC) // 降序排序
				.addSort("price", SortOrder.ASC) // 升序排序
				.execute() // 执行查询
				.actionGet();// 返回数据
		
		// 遍历结果 返回的JSON串根节点叫做hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
		}
	}
	
	
	/**
	 * 	只查询title和price两个列的数据
	 */
	@Test
	public void searchInclude() {
		// 定义连接 prepareSearch可指定多个索引及指定多个类型
		// 相当于 http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		// 查询所有数据
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.matchAllQuery())
				.setFrom(0) // 起始页大小
				.setSize(2) // 每页记录数
				.setFetchSource(new String[]{"title","price"}, null) // 只查询title,price两个列的数据
				.execute() // 执行查询
				.actionGet();// 返回数据
		
		// 遍历结果 返回的JSON串根节点叫做hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
		}
	}
	
	/**
	 * 	查询除title和price两个列外的数据
	 */
	@Test
	public void searchExclude() {
		// 定义连接 prepareSearch可指定多个索引及指定多个类型
		// 相当于 http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		// 查询所有数据
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.matchAllQuery())
				.setFrom(0) // 起始页大小
				.setSize(2) // 每页记录数
				// String[] includes, String[] excludes
				.setFetchSource(null,new String[]{"title","price"}) // 查询除title,price两个列外的数据
				.execute() // 执行查询
				.actionGet();// 返回数据
		
		// 遍历结果 返回的JSON串根节点叫做hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
		}
	}
	
	/**
	 * 	带条件查询
	 */
	@Test
	public void searchWithCondition() {
		// 定义连接 prepareSearch可指定多个索引及指定多个类型
		// 相当于 http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		// 查询所有数据
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.matchQuery("title", "战"))// 查询条件
				.setFrom(0) // 起始页大小
				.setSize(2) // 每页记录数
				// String[] includes, String[] excludes
				.setFetchSource(new String[]{"title","price"},null) // 只查询title,price两个列的数据
				.execute() // 执行查询
				.actionGet();// 返回数据
		
		// 遍历结果 返回的JSON串根节点叫做hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
		}
	}
	
	/**
	 * 	指定的字段高亮显示
	 */
	@Test
	public void searchHighlight() {
		// 定义连接 prepareSearch可指定多个索引及指定多个类型
		// 相当于 http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		
		// 设置高亮
		HighlightBuilder highlightBuilder=new HighlightBuilder();
	    highlightBuilder.preTags("<h2>");
	    highlightBuilder.postTags("</h2>");
	    highlightBuilder.field("title");
		
		// 查询所有数据
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.matchQuery("title", "战"))// 查询条件
				.highlighter(highlightBuilder)
				.setFrom(0) // 起始页大小
				.setSize(2) // 每页记录数
				// String[] includes, String[] excludes
				.setFetchSource(new String[]{"title","price"},null) // 只查询title,price两个列的数据
				.execute() // 执行查询
				.actionGet();// 返回数据
		
		// 遍历结果 返回的JSON串根节点叫做hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
			System.out.println(hit.getHighlightFields());
		}
	}
	
	/**
	 * 	多条件查询(包含) 高亮显示title
	 */
	@Test
	public void searchManyConditionWithHighlight() {
		// 定义连接 prepareSearch可指定多个索引及指定多个类型
		// 相当于 http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		
		// 设置高亮
		HighlightBuilder highlightBuilder=new HighlightBuilder();
	    highlightBuilder.preTags("<h2><font>");
	    highlightBuilder.postTags("</font></h2>");
	    highlightBuilder.field("title");
	    
	    // 查询条件 matchPhraseQuery 短词汇查询
		org.elasticsearch.index.query.QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("title", "战");
		org.elasticsearch.index.query.QueryBuilder queryBuilder2 = QueryBuilders.matchPhraseQuery("content", "星球");
		// 查询所有数据
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.boolQuery()
				.must(queryBuilder)
				.must(queryBuilder2))// 查询条件
				.highlighter(highlightBuilder)
				.setFrom(0) // 起始页大小
				.setSize(2) // 每页记录数
				// String[] includes, String[] excludes
				.setFetchSource(new String[]{"title","price"},null) // 只查询title,price两个列的数据
				.execute() // 执行查询
				.actionGet();// 返回数据
		
		// 遍历结果 返回的JSON串根节点叫做hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
			System.out.println(hit.getHighlightFields());
		}
	}
	
	/**
	 * 	多条件查询(不包含) 高亮显示title
	 */
	@Test
	public void searchManyConditionWithHighlightAndMustNot() {
		// 定义连接 prepareSearch可指定多个索引及指定多个类型
		// 相当于 http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		
		// 设置高亮
		HighlightBuilder highlightBuilder=new HighlightBuilder();
	    highlightBuilder.preTags("<h2><font>");
	    highlightBuilder.postTags("</font></h2>");
	    highlightBuilder.field("title");
	    
	    // 查询条件 matchPhraseQuery 短词汇查询
		org.elasticsearch.index.query.QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("title", "战");
		org.elasticsearch.index.query.QueryBuilder queryBuilder2 = QueryBuilders.matchPhraseQuery("content", "星球");
		// 查询所有数据
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.boolQuery()
				.must(queryBuilder)
				.mustNot(queryBuilder2))// 查询条件
				.highlighter(highlightBuilder)
				.setFrom(0) // 起始页大小
				.setSize(2) // 每页记录数
				// String[] includes, String[] excludes
				.setFetchSource(new String[]{"title","price"},null) // 只查询title,price两个列的数据
				.execute() // 执行查询
				.actionGet();// 返回数据
		
		// 遍历结果 返回的JSON串根节点叫做hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
			System.out.println(hit.getHighlightFields());
		}
	}
	
	/**
	 * 	多条件查询提高得分(类似精确度 也就是提高权重) 高亮显示title
	 */
	@Test
	public void searchShould() {
		// 定义连接 prepareSearch可指定多个索引及指定多个类型
		// 相当于 http://192.168.199.127:9200/film/_search/
		SearchRequestBuilder searchBuild = client.prepareSearch("film").setTypes("dongzuo");
		
		// 设置高亮
		HighlightBuilder highlightBuilder=new HighlightBuilder();
	    highlightBuilder.preTags("<h2><font>");
	    highlightBuilder.postTags("</font></h2>");
	    highlightBuilder.field("title");
	    
	    // 查询条件 matchPhraseQuery 短词汇查询
		org.elasticsearch.index.query.QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("title", "战");
		org.elasticsearch.index.query.QueryBuilder queryBuilder2 = QueryBuilders.matchPhraseQuery("content", "星球");
		// 范围查询 gt 小于
		org.elasticsearch.index.query.QueryBuilder queryBuilder3 = QueryBuilders.rangeQuery("publisDate").gt("2018-01-01");
		// 查询所有数据
		SearchResponse searchResponse = searchBuild.setQuery(QueryBuilders.boolQuery()
				.must(queryBuilder)
				.must(queryBuilder2)
				.should(queryBuilder2)
				.should(queryBuilder3))// 查询条件
				
				.highlighter(highlightBuilder)// 高亮
				.setFrom(0) // 起始页大小
				.setSize(2) // 每页记录数
				// String[] includes, String[] excludes
				.setFetchSource(new String[]{"title","price"},null) // 只查询title,price两个列的数据
				.execute() // 执行查询
				.actionGet();// 返回数据
		
		// 遍历结果 返回的JSON串根节点叫做hits
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits) {
			System.out.println("source:"+hit.getSourceAsString());
			System.out.println(hit.getHighlightFields());
		}
	}
	
	/**
	 * 	查询票价小于40的数据
	 * @throws Exception
	 */
	@Test
	public void searchMutil4()throws Exception{
	    SearchRequestBuilder srb=client.prepareSearch("film").setTypes("dongzuo");
	    QueryBuilder queryBuilder=QueryBuilders.matchPhraseQuery("title", "战");
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
