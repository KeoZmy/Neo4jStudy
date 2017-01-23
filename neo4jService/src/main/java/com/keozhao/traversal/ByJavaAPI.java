package com.keozhao.traversal;

import com.keozhao.API.MyRelationshipTypes;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Keo.Zhao on 2016/12/23.
 * 利用JAVAAPI对上一个类中所构造的数据结构进行遍历
 */
public class ByJavaAPI {
    public  static  void  main(String[] args){
        /**
         * 指定neo4j的存储路径
         */
        File file = new File("G:\\DevTool\\db\\Neo4jTest\\dir2");
        GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(file);

        /**
         * 遍历John看过的电影
         */
        try(Transaction tx = graphDB.beginTx()){
            //在管理界面可以查出john节点的id为0
            Node userJohn = graphDB.getNodeById(01);
            //获取从userJon节点处的HAS_SEEN关系
            Iterable<Relationship> allRelationShips = userJohn.getRelationships(Direction.OUTGOING, MyRelationshipTypes.HAS_SEEN);
            allRelationShips.forEach(relationship ->
                    System.out.println("user has seen movie:" + relationship.getEndNode().getProperty("name"))
            );
            System.out.println(" ");
            tx.success();
        }

        /**
         * 遍历John的朋友喜欢而John还没看过的电影
         */
        try(Transaction tx = graphDB.beginTx()){
            Node userJohn = graphDB.getNodeById(01);
            //遍历处John的朋友看过的电影
            Set<Node> moviesFriendsLike = new HashSet<>();
            userJohn.getRelationships(MyRelationshipTypes.IS_FRIEND_OF).forEach(friendRelation -> {
                //获取该关系上的除指定节点外的其他节点
                Node friend = friendRelation.getOtherNode(userJohn);
                friend.getRelationships(Direction.OUTGOING, MyRelationshipTypes.HAS_SEEN).forEach(seenMovie ->
                        moviesFriendsLike.add(seenMovie.getEndNode()));
            });
             //遍历出John看过的电影
            Set<Node> moviesJohnLike = new HashSet<>();
            userJohn.getRelationships(Direction.OUTGOING,MyRelationshipTypes.HAS_SEEN).forEach(movieJohnLike ->
                    moviesFriendsLike.add(movieJohnLike.getEndNode())
            );
            moviesJohnLike.forEach(movie
                    -> System.out.println("John like movie:" + movie.getProperty("name"))
            );
            System.out.print("");

            //过滤John看过的电影
            moviesFriendsLike.removeAll(moviesJohnLike);
            moviesFriendsLike.forEach(movie ->
            System.out.println("Recommend movie to John:" + movie.getProperty("name"))
            );
            System.out.println("");
            tx.success();
        }
        graphDB.shutdown();
    }
}
