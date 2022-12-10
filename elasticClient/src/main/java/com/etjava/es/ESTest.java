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
	 * ��������
	 * ����������ʽ json����String���͵�json����Map���͵� �����õ�JSON����
	 */
	@Test
	public void createIndex() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("name", "ElasticSearch");
		jsonObject.addProperty("price", 100.00);
		jsonObject.addProperty("auth", "etjava");
		
		/*��������
		 * index ����
 			type ��������
			id ����ID û�л��Զ�����
		 */
		IndexResponse response = client.prepareIndex("book","java","1")// ���������ĵ�
			.setSource(jsonObject.toString(),XContentType.JSON) // ָ������ֵ����������
			.get();
		
		System.out.println("name:"+response.getIndex());
		System.out.println("type:"+response.getType());
		System.out.println("ID:"+response.getId());
		System.out.println("status:"+response.status());// ��һ�δ�����CREATED״̬
	}
	
	
	/**
	 * 	�����ĵ�ID��ȡ����
	 */
	@Test
	public void getIndex() {
		GetResponse getResponse = client.prepareGet("book", "java", "1").get();
		System.out.println("id=1������ֵ��"+getResponse.getSourceAsString());
	}
	
	/**
	 * �޸�����
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
	 * ɾ������
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
