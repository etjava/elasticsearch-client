package com.etjava.es;

import java.net.InetAddress;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;

public class ESTest {

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
	 * 创建索引
	 * 索引创建方式 json串，String类型的json串，Map类型等 ，常用的JSON类型
	 */
	@Test
	public void createIndex() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", "ElasticSearch");
		jsonObject.addProperty("price", 100.00);
		jsonObject.addProperty("auth", "etjava");
		
		/*创建索引
		 * index 索引
 			type 索引类型
			id 索引ID 没有会自动创建
		 */
		IndexResponse response = client.prepareIndex("book","java","1")// 创建索引文档
			.setSource(jsonObject.toString(),XContentType.JSON) // 指定索引值及索引类型
			.get();
		
		System.out.println("name:"+response.getIndex());
		System.out.println("type:"+response.getType());
		System.out.println("ID:"+response.getId());
		System.out.println("status:"+response.status());// 第一次创建是CREATED状态
	}
	
	
	/**
	 * 	根据文档ID获取索引
	 */
	@Test
	public void getIndex() {
		GetResponse getResponse = client.prepareGet("book", "java", "1").get();
		System.out.println("id=1的索引值："+getResponse.getSourceAsString());
	}
	
	/**
	 * 修改索引
	 */
	@Test
	public void modifyIndex() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", "ElasticSearch");
		jsonObject.addProperty("price", 100.00);
		jsonObject.addProperty("auth", "etjava");
		
		UpdateResponse updateResponse = client.prepareUpdate("book", "java", "1")
			.setDoc(jsonObject.toString(), XContentType.JSON)
			.get();
		System.out.println("name:"+updateResponse.getIndex());
		System.out.println("type:"+updateResponse.getType());
		System.out.println("ID:"+updateResponse.getId());
		System.out.println("status:"+updateResponse.status());// status=OK
	}
	
	/**
	 * 删除索引
	 */
	@Test
	public void removeIndex() {
		DeleteResponse deleteResponse = client.prepareDelete("book", "java", "1").get();
		System.out.println("name:"+deleteResponse.getIndex());
		System.out.println("type:"+deleteResponse.getType());
		System.out.println("ID:"+deleteResponse.getId());
		System.out.println("status:"+deleteResponse.status());// status=OK
	}
	
	
}
