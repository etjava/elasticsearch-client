package com.etjava.es;

import java.net.InetAddress;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 	���Բ�ѯ���� - ��ʼ������
 * 	smartcn ���ķִ�
 * @author etjav
 *
 */
public class Film2Test {

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
		JsonArray jsonArray=new JsonArray();
		
		JsonObject jsonObject=new JsonObject();
    	jsonObject.addProperty("title", "ǰ��3���ټ�ǰ��");
    	jsonObject.addProperty("publishDate", "2017-12-29");
    	jsonObject.addProperty("content", "һ�Ժû������ƣ����� �Σ�����ɣ�֣�� �Σ���Ů�Ѷ���Ϊһ��С��������֣����ҡ��ܾ���أ������ϴ�����������ҹ�ꡢ�ɶ��뽻�������Ϸŷ������ڶ�����������ף���ƽ����ڡ����Ӷ�������һϵ�к�Ц�Ĺ��¡�������Ů��ͬ�ʹ���ȴ���ӡ�����֮�����������Ů�����������మ��ɱ�޾��ڡ�Ȼ����ʵ�ġ�������ȴ����⧲�������һ������������������һ�Ծ�������ȫ�������������˶���������յ�ѡ�����ٴ������������Ҳ������");
    	jsonObject.addProperty("director", "������");
    	jsonObject.addProperty("price", "35");
    	jsonArray.add(jsonObject);
    	
    	
    	JsonObject jsonObject2=new JsonObject();
    	jsonObject2.addProperty("title", "����֮Ѫ");
    	jsonObject2.addProperty("publishDate", "2017-12-29");
    	jsonObject2.addProperty("content", "2007�꣬Dr.James�ڰ뵺�����̵�֧�����о������ˡ��о������У������˰����ҷ�������ͻ���ɱ�䣬���뵺������ɱ��������������֯���ӹ������˵��о���Dr.James����������ֻ��Ѱ�󾯷��ı������ع��ֶ������� �Σ��������뿪������Σ��СŮ������������֤�˱�������...ʮ�����һ���ƻ�С˵������֮Ѫ���ĳ��������˺�����������֯�����غ�����ɭ����־�� �Σ�����ɱ���İ뵺�����̵Ķ��ӣ����Լ������������ֶ���������������ʼ�ӽ�һ������ͨ��Ů��Nancy��ŷ������ �Σ��������Ҫ�õ������ϵ����ܡ���������Ļ���������ض���İ�����Ҳ�ٴγ��֣��ڶ�β���֮������ץ��Nancy���ֶ�����ɭ�����ò���������һͬǰȥ��ȣ��ؼ�ʱ��ȴ������ɭ��Ȼ�Ǳ�ɱ���İ뵺�����̵Ķ��ӣ������˵�ʵ���¼Ҳ��������ɭ֮��......");
    	jsonObject2.addProperty("director", "������");
    	jsonObject2.addProperty("price", "45");
    	jsonArray.add(jsonObject2);
    	
    	JsonObject jsonObject3=new JsonObject();
    	jsonObject3.addProperty("title", "�����ս8�����ľ�����ʿ");
    	jsonObject3.addProperty("publishDate", "2018-01-05");
    	jsonObject3.addProperty("content", "�������ս�����ľ�����ʿ���н�ǰ���������ս��ԭ�����ѡ��ľ��飬������һ����ȫ����Ϯ֮�£��������������׵��� Daisy Ridley �Σ����Ҷ���Լ������Ү�� John Boyega �Σ���������Ĭ������˹���������� Oscar Isaac �Σ���λ�������Ǹ��Եľ� ���ð�չ��¡�ǰ���о���ǿ��ԭ������������Ѱ�����ӵľ��ش�ʦ¬�ˡ������ߣ����ˡ����׶� Mark Hamill �Σ����ں��ߵ�ָ���½���ԭ��ѵ�����Ҷ�������һ�����������ɵ�����Ϊ�������ò��´���Ӫ������Լ��Ĺ�ȥ��������Ĭ����Ҫ��Ӧ��սʿ������Ľ�ɫת������һ��������Ҳ������һЩѪ�Ľ�ѵ��");
    	jsonObject3.addProperty("director", "������Լ��ѷ");
    	jsonObject3.addProperty("price", "55");
    	jsonArray.add(jsonObject3);
    	
    	JsonObject jsonObject4=new JsonObject();
    	jsonObject4.addProperty("title", "���ߵ���ȭ");
    	jsonObject4.addProperty("publishDate", "2017-12-29");
    	jsonObject4.addProperty("content", "�����ȭ�����ӵİ����������� �Σ��������������ʮ�������������С������ �Σ���һ��ԩ�ң�û�뵽��Ϊһ������ĵ������Ů���廥�����Ա���Һ����˻��ӻ�����������ȭ̳�Ĵ����Ҳ�ҿ��˼�ȭ������ܣ�����һ���鷳�����������ڡ������š������������ǣ����� �Σ���ָ���£�����������������ߵ���ȭ��");
    	jsonObject4.addProperty("director", "���� / �ų���");
    	jsonObject4.addProperty("price", "35");
    	jsonArray.add(jsonObject4);
    	
    	JsonObject jsonObject5=new JsonObject();
    	jsonObject5.addProperty("title", "ս��2");
    	jsonObject5.addProperty("publishDate", "2017-07-27");
    	jsonObject5.addProperty("content", "���·����ڷ��޸����Ĵ��ϣ����˹���棨�⾩ �Σ�������������¬����������������������Ư��һ��������������������ô����ʱ��һ��ͻ��������������������ļƻ���ͻȻ��������һ�����޹������ң������԰�ȫ���룬ȴ���޷���������Ϊ���˵�ʹ�����������ճ��������������������ɱ�е�ͬ��������չ���������������Ŷ����ĳ��������ڵ������𽥸��գ����չ�������ս������Ϊͬ����ս����");
    	jsonObject5.addProperty("director", "�⾩");
    	jsonObject5.addProperty("price", "38");
    	jsonArray.add(jsonObject5);
    	
    	for(int i=0;i<jsonArray.size();i++){
    		JsonObject jo=jsonArray.get(i).getAsJsonObject();
    		IndexResponse response=client.prepareIndex("film2", "dongzuo")// �������� film�������ƣ�dongzuo��������
    				.setSource(jo.toString(), XContentType.JSON).get();
    		System.out.println("�������ƣ�"+response.getIndex());
    		System.out.println("���ͣ�"+response.getType());
    		System.out.println("�ĵ�ID��"+response.getId());
    		System.out.println("��ǰʵ��״̬��"+response.status());
    	}
	}
	
	/**
	 * 	�����ִʲ�ѯ - matchQuery
	 */
	@Test
	public void searchContation() {
		SearchRequestBuilder srb = client.prepareSearch("film2").setTypes("dongzuo");
		SearchResponse searchResponse = srb.setQuery(QueryBuilders.matchQuery("title", "������").analyzer("smartcn"))
			.setFetchSource(new String[] {"title","price","content"},null)
			.execute() // ִ�в�ѯ
			.actionGet(); // ���ؽ��
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits){
	        System.out.println(hit.getSourceAsString());
	    }
	}
	
	/**
	 * 	���ֶηִʲ�ѯ - multiMatchQuery
	 */
	@Test
	public void searchContation2() {
		SearchRequestBuilder srb = client.prepareSearch("film2").setTypes("dongzuo");
		SearchResponse searchResponse = srb.setQuery(QueryBuilders.multiMatchQuery("����������ȭ", "title","content"))
			.setFetchSource(new String[] {"title","price","content"},null)
			.addSort("price", SortOrder.ASC) // ����
			.execute() // ִ�в�ѯ
			.actionGet(); // ���ؽ��
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits){
	        System.out.println(hit.getSourceAsString());
	    }
	}
}