# elasticsearch-client
客户端测试elasticsearch
ElatsicSearch5.5
简介
ElasticSearch是一个基于Lucene的搜索服务器。它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。
Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。
我们建立一个网站或应用程序，并要添加搜索功能，但是想要完成搜索工作的创建是非常困难的。我们希望搜索解决方案要运行速度快，我们希望能有一个零配置和一个完全免费的搜索模式，我们希望能够简单地使用JSON通过HTTP来索引数据，我们希望我们的搜索服务器始终可用，我们希望能够从一台开始并扩展到数百台，我们要实时搜索，我们要简单的多租户，我们希望建立一个云的解决方案。因此我们利用Elasticsearch来解决所有这些问题以及可能出现的更多其它问题。
安装
物理机安装
1、必须要有JRE的支持(version8.0或以上)
 ![image](https://user-images.githubusercontent.com/47961027/206829975-84db67f4-b05b-4441-a05f-b233cec50aa9.png)

2、下载安装包
https://www.elastic.co/downloads/elasticsearch
![image](https://user-images.githubusercontent.com/47961027/206829983-6b2daeb7-57a9-41be-804b-accc596619b7.png)


3、关闭防火墙
# 查看防火墙状态
firewall-cmd --state
# 关闭防火墙
systemctl stop firewalld.service
# 禁止开机启动
systemctl disable firewalld.service
4、将下载的上传至服务器
 ![image](https://user-images.githubusercontent.com/47961027/206829989-b260aae4-bd2f-408c-a3ff-a855936dd9e9.png)

5、复制到opt目录下es中
如果没有则新建
 ![image](https://user-images.githubusercontent.com/47961027/206829994-9404a305-be5d-400b-9ed1-ec627c4f19cd.png)

6、解压缩
tar -zxvf elasticsearch-5.5.2.tar.gz
7、尝试启动
sh /opt/es5.5/elasticsearch-5.5.2/bin/elasticsearch
会抛出如下异常 这是由于ES不可以使用root权限启动导致的
 ![image](https://user-images.githubusercontent.com/47961027/206829998-cc9be2f6-4e16-4989-b51d-1e5eaab2a5b9.png)

8、新建用户
useradd elastic
chown -R elastic:elastic /opt/es5.5/elasticsearch-5.5.2/
9、再次启动
启动之前先切换用户
su elastic
 ![image](https://user-images.githubusercontent.com/47961027/206830002-943902b6-4286-4739-aeaf-ec75a8e4a7bd.png)

启动时如果出现上述问题是由于最大文件和最大虚拟内存小导致
解决：
修改配置文件，需要先切换到root用户
[1]: max file descriptors [4096] for elasticsearch process is too low, increase to at least [65536]
修改 /etc/security/limits.conf 添加如下内容
*        * hard    nofile           65536
*        * soft    nofile           65536
 ![image](https://user-images.githubusercontent.com/47961027/206830005-59b023fc-7c32-4cf9-8ded-0d60c29e256b.png)

[2]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
临时修改：执行 sudo sysctl -w vm.max_map_count=2621441
永久修改：vim /etc/sysctl.conf 添加vm.max_map_count=2621441
 ![image](https://user-images.githubusercontent.com/47961027/206830023-fddb9ef3-cb18-4d23-9bd9-1468a300982a.png)

修改完成后需要使其生效
sudo sysctl -p /etc/sysctl.conf
 ![image](https://user-images.githubusercontent.com/47961027/206830028-efc530d4-53fa-436b-9d99-cc6d96fe2f9c.png)

10、后台启动
sh /opt/es5.5/elasticsearch-5.5.2/bin/elasticsearch -d
 ![image](https://user-images.githubusercontent.com/47961027/206830032-93390976-0b02-4eeb-bec9-ee2ea3450605.png)

11、访问
使用局域网IP无法访问 原因是没有打开局域网IP
 ![image](https://user-images.githubusercontent.com/47961027/206830039-56394a9d-8c58-4a2a-b27c-eb922aa8ce7a.png)

12、修改配置文件
vim /opt/es5.5/elasticsearch-5.5.2/config/ elasticsearch.yml
 ![image](https://user-images.githubusercontent.com/47961027/206830042-0177b2de-2132-4251-8d83-8684157e0709.png)

13、再次访问
 ![image](https://user-images.githubusercontent.com/47961027/206830044-0507e441-63d2-46c9-af8f-8f049b631f14.png)
![image](https://user-images.githubusercontent.com/47961027/206830049-8164afb9-67b4-4fef-9597-57b36364fac4.png)

 
14、Cannot allocate memory
Java HotSpot(TM) 64-Bit Server VM warning: INFO: os::commit_memory(0x0000000085330000, 2060255232, 0) failed; error='Cannot allocate memory' (errno=12)
由于elasticsearch5.5默认分配jvm空间大小为2g，修改jvm空间分配
vi /home/es/elasticsearch-5.5.2/config/jvm.options 
默认配置 
-Xms2g  
-Xmx2g  

改成
-Xms512m  
-Xmx512m
容器中搭建
docker run -it --rm --name elastic -p 9200:9200 -p 9300:9300 elasticsearch:5.5.2
 ![image](https://user-images.githubusercontent.com/47961027/206830064-910c3ae7-4c2e-45b8-bef2-5b6acbea6a19.png)
其它配置后续补充...

ES基本操作
java 客户端
添加依赖
  	<dependency>
	    <groupId>org.elasticsearch.client</groupId>
	    <artifactId>transport</artifactId>
	    <version>5.5.2</version>
	</dependency>
    
    <dependency>
	    <groupId>com.google.code.gson</groupId>
	    <artifactId>gson</artifactId>
	    <version>2.8.2</version>
	</dependency>
1、索引的基础操作
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
	
	private TransportClient client = null;
	
	/**
	 * 	创建连接客户端
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	@Before
	public void getClient() throws Exception{
		client = new PreBuiltTransportClient(Settings.EMPTY)
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


基于Springboot
图形化操作
Head插件安装与使用
1、安装node.js
官方下载地址
https://nodejs.org/en/download/
 ![image](https://user-images.githubusercontent.com/47961027/206830101-3c257ccc-3aab-458f-beb9-b9a963acf017.png)

1.1、下载
右键复制连接 然后使用wget方式直接下载到服务器
wget https://nodejs.org/dist/v8.9.1/node-v8.9.1-linux-x64.tar.xz
 ![image](https://user-images.githubusercontent.com/47961027/206830103-136d0643-6009-48a0-a79c-b57c3f32445c.png)

1.2、迁移目录
将下载的安装包迁移到/usr/local/es-plugis下 没有则新建目录
迁移后需要解压(解压时注意 使用 tar -xJf)
tar -xJf node-v8.9.1-linux-x64.tar.xz
 ![image](https://user-images.githubusercontent.com/47961027/206830107-ced0c58b-69f6-4e91-b6ca-31ad1e2392a6.png)

1.3、修改环境变量
vim /etc/profile
在文件末尾添加如下内容
export NODE_HOME=/usr/local/es-plugins/node-v8.9.1-linux-x64
export PATH=$NODE_HOME/bin:$PATH
 ![image](https://user-images.githubusercontent.com/47961027/206830109-514db13d-e7fd-4192-89cf-7e51240fadde.png)

1.4、使其生效
source /etc/profile
 ![image](https://user-images.githubusercontent.com/47961027/206830111-3ffc5908-bcd3-4839-86e1-19b9a7ffb3a7.png)

1.5、验证是否安装成功
node -v
 ![image](https://user-images.githubusercontent.com/47961027/206830113-9fdc5575-2f9a-43ca-8bff-3878aa88e148.png)

2、安装git
由于es插件存放到git远程库中，可以直接使用git命令clone
2.1、yum 安装git
yum -y install git
 ![image](https://user-images.githubusercontent.com/47961027/206830117-2091629f-b4b6-454e-92cd-c0ac5f0a6d09.png)

3、下载插件
插件地址 https://github.com/mobz/elasticsearch-head
 ![image](https://user-images.githubusercontent.com/47961027/206830124-b980aa02-b87e-4db2-ba79-555c3b1100ca.png)

进入到/usr/local/es-plugins下进行下载
git clone git://github.com/mobz/elasticsearch-head.git
如果github一直下载失败 则可以使用国内gitee下载
git clone https://gitee.com/githubd/elasticsearch-head.git
3.1、下载后进入到目录进行安装
cd elasticsearch-head/
3.2、执行npm install
 ![image](https://user-images.githubusercontent.com/47961027/206830129-b5619f89-6b03-407c-97cd-3a768d2ceff8.png)

3.3、配置es运行访问
vim /opt/es5.5/elasticsearch-5.5.2/config/elasticsearch.yml
在末尾添加
http.cors.enabled: true
http.cors.allow-origin: "*"
 ![image](https://user-images.githubusercontent.com/47961027/206830130-8b1e098c-0233-494b-8c87-dbf8b2e58924.png)

修改后需要重启elastic
先kill之前的 然后切换到elastic用户进行启动
sh /opt/es5.5/elasticsearch-5.5.2/bin/elasticsearch -d
 ![image](https://user-images.githubusercontent.com/47961027/206830135-d4fa852b-afa0-494c-98bc-3d385a42ea58.png)

3.4、启动并访问
npm run start
 ![image](https://user-images.githubusercontent.com/47961027/206830136-cae78d31-162c-46d9-ab61-be0ee3aee09d.png)
![image](https://user-images.githubusercontent.com/47961027/206830153-956f1230-0879-4039-845a-3c1db81ff4a3.png)

   
4、使用图形化操作索引
4.1、新建索引
 ![image](https://user-images.githubusercontent.com/47961027/206830178-216d8f40-e64b-4352-a7c5-b28b41080925.png)

 
4.1.1、测试双倍磁片和备份设置
 ![image](https://user-images.githubusercontent.com/47961027/206830184-c97f9919-6215-4577-9c8a-5ecfcc092ff0.png)
![image](https://user-images.githubusercontent.com/47961027/206830199-86f1de35-d4e1-478b-b744-2be0699d1e8b.png)
![image](https://user-images.githubusercontent.com/47961027/206830210-b2b9a585-0bae-4cac-a285-36262f924dc5.png)

 
4.2、添加数据
 ![image](https://user-images.githubusercontent.com/47961027/206830217-19725cc5-917c-4fcb-82e7-a08f89183458.png)

4.3、预览索引
 ![image](https://user-images.githubusercontent.com/47961027/206830223-6c97cf57-5ce2-4c00-b043-a2223ed62bd3.png)

4.4、索引修改
索引修改需要提供索引ID，类型，名称
 ![image](https://user-images.githubusercontent.com/47961027/206830233-706ffef4-a1bd-4894-b93e-3ac925251410.png)

4.5、查询索引
查询使用get请求 并提供索引ID，类型，名称
 ![image](https://user-images.githubusercontent.com/47961027/206830245-590ab913-b6a1-4012-9700-66863ef4400c.png)

4.6、删除索引
方式1
 ![image](https://user-images.githubusercontent.com/47961027/206830260-fc8cac6f-7ceb-4cdd-a820-29202c5c4a51.png)

方式2
 ![image](https://user-images.githubusercontent.com/47961027/206830265-57e6e860-166c-4849-9401-987bca2fde72.png)

4.7、关闭索引
 ![image](https://user-images.githubusercontent.com/47961027/206830267-0966f87b-dbe3-47a3-9c3b-b89f0556480a.png)

4.8、索引类型
常用类型 ：
keyword 为短词片 通常用作标题或简短的描述文字分词
integer 为int类型数据
floa 是浮点类型数据 也可以使用double
text 长文本词片
date 日期类型




创建新的索引 user
{
  "mappings": {
    "first": {
      "properties": {
        "name": {
          "type": "keyword"
        },
        "age": {
          "type": "integer"
        },
        "score": {
          "type": "fload"
        }
      }
    }
  }
}
![image](https://user-images.githubusercontent.com/47961027/206830276-d4bf2ca0-e6c2-41ee-ae23-c3d309248e95.png)

 
添加分词数据
{"name":"tom","age":12,"score":9.5,"desc":"xxxxx"}
 ![image](https://user-images.githubusercontent.com/47961027/206830281-63966d9b-b7b6-4a10-ac06-d6c52fe4c9ad.png)

预览
 ![image](https://user-images.githubusercontent.com/47961027/206830285-ab9a495b-5cae-4179-ac17-305bf860e330.png)

集群
配置文件详解
vim /opt/es5.5/elasticsearch-5.5.2/config/elasticsearch.yml
# ======================== Elasticsearch Configuration =========================
#
# NOTE: Elasticsearch comes with reasonable defaults for most settings.
#       Before you set out to tweak and tune the configuration, make sure you
#       understand what are you trying to accomplish and the consequences.
#
# The primary way of configuring a node is via this file. This template lists
# the most important settings you may want to configure for a production cluster.
#
# Please consult the documentation for further information on configuration options:
# https://www.elastic.co/guide/en/elasticsearch/reference/index.html
#
# ---------------------------------- Cluster[集群相关配置] -----------------------------------
#
# Use a descriptive name for your cluster:
# 集群名称 多台服务器时 该名称必须保持一致
#cluster.name: my-application
#
# ------------------------------------ Node[集群节点] ------------------------------------
#
# Use a descriptive name for the node:
# 配置集群节点名称，每台机器对应的名称不能相同
#node.name: node-1
#
# Add custom attributes to the node:
# 节点相关备注信息等
#node.attr.rack: r1
#
# ----------------------------------- Paths[数据存放的路径] ------------------------------------
#
# Path to directory where to store the data (separate multiple locations by comma):
# 数据存放的路径 默认/es_home/data
#path.data: /path/to/data
#
# Path to log files:
# 指定es日志存放目录，默认在es_home/data/logs下
#path.logs: /path/to/logs
#
# ----------------------------------- Memory[内存设置] -----------------------------------
#
# Lock the memory on startup:
# 锁定物理内存地址，防止es内存被交换出去，也就是禁止es使用swap分区
#bootstrap.memory_lock: true
# 确保ES_HEAP_SIZE参数设置为系统可用内存的一半左右
# Make sure that the heap size is set to about half the memory available
# on the system and that the owner of the process is allowed to use this
# limit.
# 当系统进行内存交换的时候 es的性能很差
# Elasticsearch performs poorly when the system is swapping the memory.
#
# ---------------------------------- Network[网络设置] -----------------------------------
# 绑定IP地址 默认127.0.0.1 也就是只有本地才可以访问，可以改为0.0.0.0 运行全部访问 或改为具体的IP地址
# Set the bind address to a specific IP (IPv4 or IPv6):
#
network.host: 192.168.199.127
#
# Set a custom port for HTTP:
# 为es定义web端(浏览器)访问端口 默认9200
http.port: 9200
#
# For more information, consult the network module documentation.
#
# --------------------------------- Discovery [发现配置]----------------------------------
# 启动新节点时 通过IP列表进行发现节点 组件集群
# 默认节点列表
# 127.0.0.1 表示IPV4的回环地址
# [::1] 表示IPV6的回环地址
#
# es1.x中使用组播(multicast)协议 默认自动发现同一网段中的es节点并组件集群
# es2.x中使用单播(unicast)协议 需要手动指定要发现的节点信息
# 注意：如果是发现其它服务器上的es服务 可以不指定端口9300 但如果发现同一台服务器中的es服务 需要指定9300端口
#
# Pass an initial list of hosts to perform discovery when new node is started:
# The default list of hosts is ["127.0.0.1", "[::1]"]
# 这里设置集群的主节点
#discovery.zen.ping.unicast.hosts: ["host1", "host2"]
#
#
# 通过这个参数防止集群脑裂现象  (集群节点数/2)+1
# Prevent the "split brain" by configuring the majority of nodes (total number of master-eligible nodes / 2 + 1):
#
#discovery.zen.minimum_master_nodes: 3
#
# For more information, consult the zen discovery module documentation.
#
# ---------------------------------- Gateway[网关配置] -----------------------------------
#
# Block initial recovery after a full cluster restart until N nodes are started:
# 一个集群中的N个节点启动后 才运行进行数据恢复处理  默认是1
#gateway.recover_after_nodes: 3
#
# For more information, consult the gateway module documentation.
#
# ---------------------------------- Various -----------------------------------
# 一台服务器上禁止启动多个es服务
# Require explicit names when deleting indices:
#
#action.destructive_requires_name: true
#

http.cors.enabled: true

http.cors.allow-origin: "*"


集群搭建
配置要求
JDK8.0以上
配置集群之前  先把要加群集群的节点的里的data目录下的Node目录 删除，否则集群建立会失败
配置概述：
两台集群的cluster.name必须保持一致，这样才算做一个集群
node.name节点名称每台取不同的名称，用来表示不同的集群节点
network.host配置成自己的局域网IP
http.port端口就固定9200
discovery.zen.ping.unicast.hosts主动发现节点我们都配置成127节点IP
192.168.199.127[主机]
192.168.199.126[从机]
127主机配置
# ======================== Elasticsearch Configuration =========================
#
# NOTE: Elasticsearch comes with reasonable defaults for most settings.
#       Before you set out to tweak and tune the configuration, make sure you
#       understand what are you trying to accomplish and the consequences.
#
# The primary way of configuring a node is via this file. This template lists
# the most important settings you may want to configure for a production cluster.
#
# Please consult the documentation for further information on configuration options:
# https://www.elastic.co/guide/en/elasticsearch/reference/index.html
#
# ---------------------------------- Cluster -----------------------------------
#
# Use a descriptive name for your cluster:
# 多个es服务时 名称必须一致
cluster.name: my-application
#
# ------------------------------------ Node ------------------------------------
#
# Use a descriptive name for the node:
# 每台es服务器 节点名称必须不一致
node.name: node-1
#
# Add custom attributes to the node:
#
#node.attr.rack: r1
#
# ----------------------------------- Paths ------------------------------------
#
# Path to directory where to store the data (separate multiple locations by comma):
#
#path.data: /path/to/data
#
# Path to log files:
#
#path.logs: /path/to/logs
#
# ----------------------------------- Memory -----------------------------------
#
# Lock the memory on startup:
#
#bootstrap.memory_lock: true
#
# Make sure that the heap size is set to about half the memory available
# on the system and that the owner of the process is allowed to use this
# limit.
#
# Elasticsearch performs poorly when the system is swapping the memory.
#
# ---------------------------------- Network -----------------------------------
#
# Set the bind address to a specific IP (IPv4 or IPv6):
#
network.host: 192.168.199.127
#
# Set a custom port for HTTP:
#
http.port: 9200
#
# For more information, consult the network module documentation.
#
# --------------------------------- Discovery ----------------------------------
#
# Pass an initial list of hosts to perform discovery when new node is started:
# The default list of hosts is ["127.0.0.1", "[::1]"]
# 需要指定主节点IP地址
discovery.zen.ping.unicast.hosts: ["192.168.199.127"]
#
# Prevent the "split brain" by configuring the majority of nodes (total number of master-eligible nodes / 2 + 1):
#
#discovery.zen.minimum_master_nodes: 3
#
# For more information, consult the zen discovery module documentation.
#
# ---------------------------------- Gateway -----------------------------------
#
# Block initial recovery after a full cluster restart until N nodes are started:
#
#gateway.recover_after_nodes: 3
#
# For more information, consult the gateway module documentation.
#
# ---------------------------------- Various -----------------------------------
#
# Require explicit names when deleting indices:
#
#action.destructive_requires_name: true
#

http.cors.enabled: true

http.cors.allow-origin: "*"
126从机配置
# ======================== Elasticsearch Configuration =========================
#
# NOTE: Elasticsearch comes with reasonable defaults for most settings.
#       Before you set out to tweak and tune the configuration, make sure you
#       understand what are you trying to accomplish and the consequences.
#
# The primary way of configuring a node is via this file. This template lists
# the most important settings you may want to configure for a production cluster.
#
# Please consult the documentation for further information on configuration options:
# https://www.elastic.co/guide/en/elasticsearch/reference/index.html
#
# ---------------------------------- Cluster -----------------------------------
#
# Use a descriptive name for your cluster:
#
cluster.name: my-application
#
# ------------------------------------ Node ------------------------------------
#
# Use a descriptive name for the node:
#
node.name: node-126
#
# Add custom attributes to the node:
#
#node.attr.rack: r1
#
# ----------------------------------- Paths ------------------------------------
#
# Path to directory where to store the data (separate multiple locations by comma):
#
#path.data: /path/to/data
#
# Path to log files:
#
#path.logs: /path/to/logs
#
# ----------------------------------- Memory -----------------------------------
#
# Lock the memory on startup:
#
#bootstrap.memory_lock: true
#
# Make sure that the heap size is set to about half the memory available
# on the system and that the owner of the process is allowed to use this
# limit.
#
# Elasticsearch performs poorly when the system is swapping the memory.
#
# ---------------------------------- Network -----------------------------------
#
# Set the bind address to a specific IP (IPv4 or IPv6):
#
network.host: 192.168.199.126
#
# Set a custom port for HTTP:
#
http.port: 9200
#
# For more information, consult the network module documentation.
#
# --------------------------------- Discovery ----------------------------------
#
# Pass an initial list of hosts to perform discovery when new node is started:
# The default list of hosts is ["127.0.0.1", "[::1]"]
#
discovery.zen.ping.unicast.hosts: ["192.168.199.127"]
#
# Prevent the "split brain" by configuring the majority of nodes (total number of master-eligible nodes / 2 + 1):
#
#discovery.zen.minimum_master_nodes: 3
#
# For more information, consult the zen discovery module documentation.
#
# ---------------------------------- Gateway -----------------------------------
#
# Block initial recovery after a full cluster restart until N nodes are started:
#
#gateway.recover_after_nodes: 3
#
# For more information, consult the gateway module documentation.
#
# ---------------------------------- Various -----------------------------------
#
# Require explicit names when deleting indices:
#
#action.destructive_requires_name: true
#

http.cors.enabled: true
http.cors.allow-origin: "*"
两台集群配置完成后需要重启服务
使用es插件登录查看集群状态
 ![image](https://user-images.githubusercontent.com/47961027/206830338-29933704-310c-4883-911a-abcda429fd70.png)

Java操作
只是将单个节点改为集群模式 其它的创建，修改，查询，删除不变
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


ES查询操作
前期准备
新建索引
 ![image](https://user-images.githubusercontent.com/47961027/206830344-b012d8f9-6d3e-49e9-bb48-b759758e6261.png)

添加映射关系
http://192.168.199.127:9200/film/_mapping/dongzuo/  POST
{
  "properties": {
    "title": {
      "type": "text"
    },
    "publishDate": {
      "type": "date"
    },
    "content": {
      "type": "text"
    },
    "director": {
      "type": "keyword"
    },
    "price": {
      "type": "float"
    }
  }
}
 ![image](https://user-images.githubusercontent.com/47961027/206830353-73b5cfe3-e938-4709-9633-a79282f1e7a7.png)

添加数据到索引
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 	测试查询操作
 * @author etjav
 *
 */
public class FilmTest {

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
		JsonArray jsonArray=new JsonArray();
		
		JsonObject jsonObject=new JsonObject();
    	jsonObject.addProperty("title", "前任3：再见前任");
    	jsonObject.addProperty("publishDate", "2017-12-29");
    	jsonObject.addProperty("content", "一对好基友孟云（韩庚 饰）和余飞（郑恺 饰）跟女友都因为一点小事宣告分手，并且“拒绝挽回，死不认错”。两人在夜店、派对与交友软件上放飞人生第二春，大肆庆祝“黄金单身期”，从而引发了一系列好笑的故事。孟云与女友同甘共苦却难逃“五年之痒”，余飞与女友则棋逢敌手相爱相杀无绝期。然而现实的“打脸”却来得猝不及防：一对推拉纠结零往来，一对纠缠互怼全交代。两对恋人都将面对最终的选择：是再次相见？还是再也不见？");
    	jsonObject.addProperty("director", "田羽生");
    	jsonObject.addProperty("price", "35");
    	jsonArray.add(jsonObject);
    	
    	
    	JsonObject jsonObject2=new JsonObject();
    	jsonObject2.addProperty("title", "机器之血");
    	jsonObject2.addProperty("publishDate", "2017-12-29");
    	jsonObject2.addProperty("content", "2007年，Dr.James在半岛军火商的支持下研究生化人。研究过程中，生化人安德烈发生基因突变大开杀戒，将半岛军火商杀害，并控制其组织，接管生化人的研究。Dr.James侥幸逃生，只好寻求警方的保护。特工林东（成龙 饰）不得以离开生命垂危的小女儿西西，接受证人保护任务...十三年后，一本科幻小说《机器之血》的出版引出了黑衣生化人组织，神秘骇客李森（罗志祥 饰）（被杀害的半岛军火商的儿子），以及隐姓埋名的林东，三股力量都开始接近一个“普通”女孩Nancy（欧阳娜娜 饰）的生活，想要得到她身上的秘密。而黑衣人幕后受伤隐藏多年的安德烈也再次出手，在多次缠斗之后终于抓走Nancy。林东和李森，不得不以身犯险一同前去解救，关键时刻却发现李森竟然是被杀害的半岛军火商的儿子，生化人的实验记录也落入了李森之手......");
    	jsonObject2.addProperty("director", "张立嘉");
    	jsonObject2.addProperty("price", "45");
    	jsonArray.add(jsonObject2);
    	
    	JsonObject jsonObject3=new JsonObject();
    	jsonObject3.addProperty("title", "星球大战8：最后的绝地武士");
    	jsonObject3.addProperty("publishDate", "2018-01-05");
    	jsonObject3.addProperty("content", "《星球大战：最后的绝地武士》承接前作《星球大战：原力觉醒》的剧情，讲述第一军团全面侵袭之下，蕾伊（黛西·雷德利 Daisy Ridley 饰）、芬恩（约翰·博耶加 John Boyega 饰）、波·达默龙（奥斯卡·伊萨克 Oscar Isaac 饰）三位年轻主角各自的抉 择和冒险故事。前作中觉醒强大原力的蕾伊独自寻访隐居的绝地大师卢克·天行者（马克·哈米尔 Mark Hamill 饰），在后者的指导下接受原力训练。芬恩接受了一项几乎不可能完成的任务，为此他不得不勇闯敌营，面对自己的过去。波·达默龙则要适应从战士向领袖的角色转换，这一过程中他也将接受一些血的教训。");
    	jsonObject3.addProperty("director", "莱恩·约翰逊");
    	jsonObject3.addProperty("price", "55");
    	jsonArray.add(jsonObject3);
    	
    	JsonObject jsonObject4=new JsonObject();
    	jsonObject4.addProperty("title", "羞羞的铁拳");
    	jsonObject4.addProperty("publishDate", "2017-12-29");
    	jsonObject4.addProperty("content", "靠打假拳混日子的艾迪生（艾伦 饰），本来和正义感十足的体育记者马小（马丽 饰）是一对冤家，没想到因为一场意外的电击，男女身体互换。性别错乱后，两人互坑互害，引发了拳坛的大地震，也揭开了假拳界的秘密，惹来一堆麻烦，最终两人在“卷莲门”副掌门张茱萸（沈腾 饰）的指点下，向恶势力挥起了羞羞的铁拳。");
    	jsonObject4.addProperty("director", "宋阳 / 张吃鱼");
    	jsonObject4.addProperty("price", "35");
    	jsonArray.add(jsonObject4);
    	
    	JsonObject jsonObject5=new JsonObject();
    	jsonObject5.addProperty("title", "战狼2");
    	jsonObject5.addProperty("publishDate", "2017-07-27");
    	jsonObject5.addProperty("content", "故事发生在非洲附近的大海上，主人公冷锋（吴京 饰）遭遇人生滑铁卢，被“开除军籍”，本想漂泊一生的他，正当他打算这么做的时候，一场突如其来的意外打破了他的计划，突然被卷入了一场非洲国家叛乱，本可以安全撤离，却因无法忘记曾经为军人的使命，孤身犯险冲回沦陷区，带领身陷屠杀中的同胞和难民，展开生死逃亡。随着斗争的持续，体内的狼性逐渐复苏，最终孤身闯入战乱区域，为同胞而战斗。");
    	jsonObject5.addProperty("director", "吴京");
    	jsonObject5.addProperty("price", "38");
    	jsonArray.add(jsonObject5);
    	
    	for(int i=0;i<jsonArray.size();i++){
    		JsonObject jo=jsonArray.get(i).getAsJsonObject();
    		IndexResponse response=client.prepareIndex("film", "dongzuo")// 创建索引 film索引名称，dongzuo索引类型
    				.setSource(jo.toString(), XContentType.JSON).get();
    		System.out.println("索引名称："+response.getIndex());
    		System.out.println("类型："+response.getType());
    		System.out.println("文档ID："+response.getId());
    		System.out.println("当前实例状态："+response.status());
    	}
	}	
}
预览
![image](https://user-images.githubusercontent.com/47961027/206830377-3bf38f61-9e3d-46f1-ae49-7feca69aeead.png)
 
查询全部
restful风格 相当于http://192.168.199.127:9200/film/_search/
 ![image](https://user-images.githubusercontent.com/47961027/206830385-a18b9670-a528-4be7-91f5-797664bc4a6f.png)

package com.etjava.es;

import java.net.InetAddress;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
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
}
执行结果返回JSON串 可以通过JSONObject直接转成对象
 ![image](https://user-images.githubusercontent.com/47961027/206830423-f9226280-58a2-4210-b087-62203782a8d4.png)

分页查询
POST方式 http://192.168.199.127:9200/film/_search/ 
 ![image](https://user-images.githubusercontent.com/47961027/206830429-5f01fe43-7aac-4684-8893-d8a28e6427ee.png)

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
 ![image](https://user-images.githubusercontent.com/47961027/206830434-7e91a21d-f75e-49b7-b42b-b4f41a7e56de.png)

排序查询
{
  "sort": [
    {"publishDate": {"order": "desc" }},
    {"price": {"order": "asc"}}
  ],
  "from":0,
   "size":2
}
 ![image](https://user-images.githubusercontent.com/47961027/206830435-573ccaae-0af0-4dbd-a1a7-bd60cdb36077.png)

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
 ![image](https://user-images.githubusercontent.com/47961027/206830447-1f362aca-92f6-4126-b8ea-b5557c2a2956.png)

过滤条列查询
查询指定列使用include 过滤掉指定列使用exclude
POST方式 http://192.168.199.127:9200/film/_search/
{
  "from": 0,
  "size": 2,
  "_source":{
    "include":["title","price"]
  }
}
 ![image](https://user-images.githubusercontent.com/47961027/206830455-c74f9bef-3451-4d7d-b94e-742fb8dd7808.png)

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
 ![image](https://user-images.githubusercontent.com/47961027/206830462-79ddd363-f831-4ec9-a88c-e0d9804358be.png)

exclude 不包含的列
setFetchSource参数1 包含的列的数组，参数2是不包含的列的数组
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
 ![image](https://user-images.githubusercontent.com/47961027/206830468-cd93e065-4c77-4022-9a90-5de33e993ac7.png)

单条件查询
POST http://192.168.199.127:9200/film/_search/
{
  "query": {
    "match": {
      "title": "战"
    }
  }
}
 ![image](https://user-images.githubusercontent.com/47961027/206830474-822b4303-35c4-4df7-b665-c026c93136cd.png)

/**
	 * 	带条件查询 - 单条件查询
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
 ![image](https://user-images.githubusercontent.com/47961027/206830482-91ef4adc-aece-45fe-b213-1958cdd40187.png)

多条件查询
使用must 包含查询条件
POST http://192.168.199.127:9200/film/_search/
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "title": "战"
          }
        },
        {
          "match": {
            "content": "星球"
          }
        }
      ]
    }
  }
}
 ![image](https://user-images.githubusercontent.com/47961027/206830488-4bc80ac4-e37a-470d-bfb8-c829f397854f.png)

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
	    
	    // 查询条件matchPhraseQuery 短词汇查询
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
 ![image](https://user-images.githubusercontent.com/47961027/206830497-6e119b0e-1c60-4612-b8fc-a84376d90b26.png)

不包含查询
must_not 不包含查询
POST http://192.168.199.127:9200/film/_search/
{
  "query": {
    "bool": {
      "must": {
        "match": {
          "title": "战"
        }
      },
      "must_not": {
        "match": {
          "content": "武士"
        }
      }
    }
  }
}
 ![image](https://user-images.githubusercontent.com/47961027/206830502-d0fa0636-53e6-4134-a28a-57dbc1d5ea1c.png)

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
 ![image](https://user-images.githubusercontent.com/47961027/206830509-69be9b80-bace-48d2-b8c6-5839a3ac716f.png)

高亮显示
POST http://192.168.199.127:9200/film/_search/
{
  "query": {
    "match": {
      "title": "战"
    }
  },
  "_source": {
    "include": [
      "title",
      "price"
    ]
  },
  "highlight": {
    "fields": {
      "title": {}
    }
  }
}
 ![image](https://user-images.githubusercontent.com/47961027/206830516-40612fc6-07af-46e2-8624-e119073fa790.png)

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
 ![image](https://user-images.githubusercontent.com/47961027/206830519-fa4c1b53-1b7a-4979-894a-3854b66fa6e9.png)

提高得分
 ![image](https://user-images.githubusercontent.com/47961027/206830522-f4331fdd-8fcd-4b26-8ef2-cc9e47d8e9f6.png)

使用should提高得分
{
  "query": {
    "bool": {
      "must": {
        "match": {
          "title": "战"
        }
      },
      "should": [// 提高权重(得分)
        {
          "match": {
            "content": "星球"
          }
        },
        {
          "range": {// 范围
            "publishDate": {
              "gte": "2018-01-01"
            }
          }
        }
      ]
    }
  }
}
 ![image](https://user-images.githubusercontent.com/47961027/206830526-d645fa42-9847-468f-be76-f7641d3feb11.png)

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
		// 范围查询 gt 大于
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
 ![image](https://user-images.githubusercontent.com/47961027/206830534-247fa8d6-a310-465b-9597-d003887af439.png)

Filter过滤
票价必须少于40
POST http://192.168.199.127:9200/film/_search/
 ![image](https://user-images.githubusercontent.com/47961027/206830538-009768d1-4bae-4aa3-91d0-128935358e0c.png)

	/**
	 * 	查询票价小于40的数据
	 * @throws Exception
	 */	
@Test
	public void searchMutil4()throws Exception{
	    SearchRequestBuilder srb=client.prepareSearch("film").setTypes("dongzuo");
	    QueryBuilder queryBuilder=QueryBuilders.matchPhraseQuery("title", "战");
	    QueryBuilder queryBuilder2=QueryBuilders.rangeQuery("price").lte(40);// 小于
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
 ![image](https://user-images.githubusercontent.com/47961027/206830542-66d9e16e-bc3d-45ba-9032-72cb4295c812.png)

中文分词
elasticsearch默认分词器会将中文拆分成单个的字进行分词 这样并不友好，有时我们需要将两个或多个字作为一个词片 有利于检索 例如 美国 应该是一个词片 而非两个独立的词片
上述问题可以通过smartcn插件来解决(smartcn是中科院发布的中文分词器插件)
安装smartcn插件
注意：集群方式 需要在每个节点机器上都进行安装
进入到elasticsearch的bin目录下 执行analysis-smartcn
cd /opt/es5.5/elasticsearch-5.5.2/bin/
sh elasticsearch-plugin install analysis-smartcn
 ![image](https://user-images.githubusercontent.com/47961027/206830548-df5e8585-b0a6-4750-9ed3-895dd473095f.png)

执行完成后需要重启服务
重启es服务
sh /opt/es5.5/elasticsearch-5.5.2/bin/elasticsearch -d
重启head插件
cd /usr/local/es-plugins/elasticsearch-head
npm run start
 ![image](https://user-images.githubusercontent.com/47961027/206830557-b6f3942e-5190-41df-afc6-c7a732452e1c.png)
![image](https://user-images.githubusercontent.com/47961027/206830560-aca1f5ff-3290-403c-a875-5e9209bd11fb.png)

 
head插件测试smartcn插件
标准分词器测试
POST http://192.168.199.127:9200/_analyze/
{
  "analyzer": "standard",
  "text": "我是中国人"
}
 ![image](https://user-images.githubusercontent.com/47961027/206830562-52d13ab0-2561-4ee7-9514-3c8c5fc5949e.png)

smartcn分词器
 ![image](https://user-images.githubusercontent.com/47961027/206830567-2e3b3690-d3c9-45a4-94ad-267f876f57e2.png)

Java测试smartcn
新建索引
 ![image](https://user-images.githubusercontent.com/47961027/206830570-b2eeb033-5a9c-4e64-b605-ee16e245dbbe.png)

添加映射
POST http://192.168.199.127:9200/film2/_mapping/dongzuo/
{
  "properties": {
    "title": {
      "type": "text",
      "analyzer": "smartcn"
    },
    "publishDate": {
      "type": "date"
    },
    "content": {
      "type": "text",
      "analyzer": "smartcn"
    },
    "director": {
      "type": "keyword"
    },
    "price": {
      "type": "float"
    }
  }
}
 ![image](https://user-images.githubusercontent.com/47961027/206830578-d24efa1a-d2a8-4814-86dc-5f1da7dc8c24.png)

初始化数据并测试查询
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
 * 	测试查询操作 - 初始化数据
 * 	smartcn 中文分词
 * @author etjav
 *
 */
public class Film2Test {

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
		JsonArray jsonArray=new JsonArray();
		
		JsonObject jsonObject=new JsonObject();
    	jsonObject.addProperty("title", "前任3：再见前任");
    	jsonObject.addProperty("publishDate", "2017-12-29");
    	jsonObject.addProperty("content", "一对好基友孟云（韩庚 饰）和余飞（郑恺 饰）跟女友都因为一点小事宣告分手，并且“拒绝挽回，死不认错”。两人在夜店、派对与交友软件上放飞人生第二春，大肆庆祝“黄金单身期”，从而引发了一系列好笑的故事。孟云与女友同甘共苦却难逃“五年之痒”，余飞与女友则棋逢敌手相爱相杀无绝期。然而现实的“打脸”却来得猝不及防：一对推拉纠结零往来，一对纠缠互怼全交代。两对恋人都将面对最终的选择：是再次相见？还是再也不见？");
    	jsonObject.addProperty("director", "田羽生");
    	jsonObject.addProperty("price", "35");
    	jsonArray.add(jsonObject);
    	
    	
    	JsonObject jsonObject2=new JsonObject();
    	jsonObject2.addProperty("title", "机器之血");
    	jsonObject2.addProperty("publishDate", "2017-12-29");
    	jsonObject2.addProperty("content", "2007年，Dr.James在半岛军火商的支持下研究生化人。研究过程中，生化人安德烈发生基因突变大开杀戒，将半岛军火商杀害，并控制其组织，接管生化人的研究。Dr.James侥幸逃生，只好寻求警方的保护。特工林东（成龙 饰）不得以离开生命垂危的小女儿西西，接受证人保护任务...十三年后，一本科幻小说《机器之血》的出版引出了黑衣生化人组织，神秘骇客李森（罗志祥 饰）（被杀害的半岛军火商的儿子），以及隐姓埋名的林东，三股力量都开始接近一个“普通”女孩Nancy（欧阳娜娜 饰）的生活，想要得到她身上的秘密。而黑衣人幕后受伤隐藏多年的安德烈也再次出手，在多次缠斗之后终于抓走Nancy。林东和李森，不得不以身犯险一同前去解救，关键时刻却发现李森竟然是被杀害的半岛军火商的儿子，生化人的实验记录也落入了李森之手......");
    	jsonObject2.addProperty("director", "张立嘉");
    	jsonObject2.addProperty("price", "45");
    	jsonArray.add(jsonObject2);
    	
    	JsonObject jsonObject3=new JsonObject();
    	jsonObject3.addProperty("title", "星球大战8：最后的绝地武士");
    	jsonObject3.addProperty("publishDate", "2018-01-05");
    	jsonObject3.addProperty("content", "《星球大战：最后的绝地武士》承接前作《星球大战：原力觉醒》的剧情，讲述第一军团全面侵袭之下，蕾伊（黛西·雷德利 Daisy Ridley 饰）、芬恩（约翰·博耶加 John Boyega 饰）、波·达默龙（奥斯卡·伊萨克 Oscar Isaac 饰）三位年轻主角各自的抉 择和冒险故事。前作中觉醒强大原力的蕾伊独自寻访隐居的绝地大师卢克·天行者（马克·哈米尔 Mark Hamill 饰），在后者的指导下接受原力训练。芬恩接受了一项几乎不可能完成的任务，为此他不得不勇闯敌营，面对自己的过去。波·达默龙则要适应从战士向领袖的角色转换，这一过程中他也将接受一些血的教训。");
    	jsonObject3.addProperty("director", "莱恩·约翰逊");
    	jsonObject3.addProperty("price", "55");
    	jsonArray.add(jsonObject3);
    	
    	JsonObject jsonObject4=new JsonObject();
    	jsonObject4.addProperty("title", "羞羞的铁拳");
    	jsonObject4.addProperty("publishDate", "2017-12-29");
    	jsonObject4.addProperty("content", "靠打假拳混日子的艾迪生（艾伦 饰），本来和正义感十足的体育记者马小（马丽 饰）是一对冤家，没想到因为一场意外的电击，男女身体互换。性别错乱后，两人互坑互害，引发了拳坛的大地震，也揭开了假拳界的秘密，惹来一堆麻烦，最终两人在“卷莲门”副掌门张茱萸（沈腾 饰）的指点下，向恶势力挥起了羞羞的铁拳。");
    	jsonObject4.addProperty("director", "宋阳 / 张吃鱼");
    	jsonObject4.addProperty("price", "35");
    	jsonArray.add(jsonObject4);
    	
    	JsonObject jsonObject5=new JsonObject();
    	jsonObject5.addProperty("title", "战狼2");
    	jsonObject5.addProperty("publishDate", "2017-07-27");
    	jsonObject5.addProperty("content", "故事发生在非洲附近的大海上，主人公冷锋（吴京 饰）遭遇人生滑铁卢，被“开除军籍”，本想漂泊一生的他，正当他打算这么做的时候，一场突如其来的意外打破了他的计划，突然被卷入了一场非洲国家叛乱，本可以安全撤离，却因无法忘记曾经为军人的使命，孤身犯险冲回沦陷区，带领身陷屠杀中的同胞和难民，展开生死逃亡。随着斗争的持续，体内的狼性逐渐复苏，最终孤身闯入战乱区域，为同胞而战斗。");
    	jsonObject5.addProperty("director", "吴京");
    	jsonObject5.addProperty("price", "38");
    	jsonArray.add(jsonObject5);
    	
    	for(int i=0;i<jsonArray.size();i++){
    		JsonObject jo=jsonArray.get(i).getAsJsonObject();
    		IndexResponse response=client.prepareIndex("film2", "dongzuo")// 创建索引 film索引名称，dongzuo索引类型
    				.setSource(jo.toString(), XContentType.JSON).get();
    		System.out.println("索引名称："+response.getIndex());
    		System.out.println("类型："+response.getType());
    		System.out.println("文档ID："+response.getId());
    		System.out.println("当前实例状态："+response.status());
    	}
	}
	
	/**
	 * 	条件分词查询 - matchQuery
	 */
	@Test
	public void searchContation() {
		SearchRequestBuilder srb = client.prepareSearch("film2").setTypes("dongzuo");
		SearchResponse searchResponse = srb.setQuery(QueryBuilders.matchQuery("title", "狼星球").analyzer("smartcn"))
			.setFetchSource(new String[] {"title","price","content"},null)
			.execute() // 执行查询
			.actionGet(); // 返回结果
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits){
	        System.out.println(hit.getSourceAsString());
	    }
	}
	
	/**
	 * 	多字段分词查询 - multiMatchQuery
	 */
	@Test
	public void searchContation2() {
		SearchRequestBuilder srb = client.prepareSearch("film2").setTypes("dongzuo");
		SearchResponse searchResponse = srb.setQuery(QueryBuilders.multiMatchQuery("非洲星球铁拳", "title","content"))
			.setFetchSource(new String[] {"title","price","content"},null)
			.addSort("price", SortOrder.ASC) // 排序
			.execute() // 执行查询
			.actionGet(); // 返回结果
		SearchHits hits = searchResponse.getHits();
		for(SearchHit hit:hits){
	        System.out.println(hit.getSourceAsString());
	    }
	}
}
![image](https://user-images.githubusercontent.com/47961027/206830594-2c456eef-49f1-4d34-8789-f5a0711504f8.png)

![image](https://user-images.githubusercontent.com/47961027/206830602-891dc318-d7ea-40dc-83f8-6a883c952bcf.png)
![image](https://user-images.githubusercontent.com/47961027/206830604-88f5fd1a-12da-4493-978e-2c6e28b29a83.png)

 
 
 
