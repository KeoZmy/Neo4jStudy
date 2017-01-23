package com.keozhao.index;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;

import java.io.File;

/**
 * Created by Keo.Zhao on 2016/12/24.
 */
public class IndexDemo {
    private static final File DB_PATH = new File("G:\\DevTool\\db\\Neo4jTest\\dir1");

    public static void main(final String[] args){
        GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        try(Transaction tx = graphDB.beginTx()){
            String johnEmail = "john@example.org";
            String kateEmail = "kage@example.org";
            String jackEmail = "jack@example.org";
            //获取用户节点，并且设置emial属性
            Node userJohn = graphDB.getNodeById(01);
            userJohn.setProperty("email",johnEmail);
            Node userKate = graphDB.getNodeById(11);
            userKate.setProperty("email",kateEmail);
            Node userJack = graphDB.getNodeById(21);
            userJack.setProperty("email",jackEmail);
            //获取索引管理器
            IndexManager indexManager = graphDB.index();
            //查找名称为user的索引，若不存在则创建一个
            Index<Node> userIndex = indexManager.forNodes("user");
            //以email为key，为users索引添加具体的索引项目
            userIndex.add(userJohn,"email",johnEmail);
            userIndex.add(userKate,"email",kateEmail);

            tx.success();
        }
            graphDB.shutdown();

        //通过邮件地址索引查找用户
        try(Transaction tx = graphDB.beginTx()){
            //或许索引管理器
            IndexManager indexManager = graphDB.index();
            //查找名称为users的索引
            Index<Node> userIndex = indexManager.forNodes("users");
            //获取索引命中的结果集
            IndexHits<Node> indexHits = userIndex.get("email","john@example.org");
            /**
             * 获取命中的节点，且要求命中节点只有一个，如果有多个则抛出NoSuchElementException("More than one element in...")
             * 若索引命中的结果集中不只一条是，秩序遍历indexHits即可
             * for(Node user : indexHits){
             *     System.out.println(user.getProperty("name"));
             * }
             */
            Node loggedOnUserNode = indexHits.getSingle();
            if(loggedOnUserNode != null){
                System.out.println(loggedOnUserNode.getProperty("name"));
            }
            tx.success();
          }


    }
}
