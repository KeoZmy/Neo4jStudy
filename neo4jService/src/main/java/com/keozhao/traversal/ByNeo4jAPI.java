package com.keozhao.traversal;

import com.keozhao.API.MyRelationshipTypes;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.*;

import java.io.File;

/**
 * Created by Keo.Zhao on 2016/12/23.
 */
public class ByNeo4jAPI {
    public static void main(String[] args){
        File file = new File("G:\\DevTool\\db\\Neo4jTest\\dir2");
        GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(file);
        /**
         * Evaluators.atDepth(2)列出深度为2的节点，userJohn节点的深度为0
         * NODE_GLOBAL，全局相同的节点将只遍历一次
         * NODE_PATH，同一路径下，相同的节点只遍历一次
         * NODE_LEVEL，同一层级下，相同的节点只遍历一次
         */
        try(Transaction tx =  graphDB.beginTx()){
            Node userJoohn = graphDB.getNodeById(01);
            TraversalDescription traversalMoviesFriendsLike = graphDB.traversalDescription()
                     .relationships(MyRelationshipTypes.IS_FRIEND_OF)
                     .relationships(MyRelationshipTypes.HAS_SEEN, Direction.OUTGOING)
                     .uniqueness(Uniqueness.NODE_PATH)
                     .evaluator(Evaluators.atDepth(2));

            Traverser traverser = traversalMoviesFriendsLike.traverse(userJoohn);
            Iterable<Node> moviesFriendsLike = traverser.nodes();
            moviesFriendsLike.forEach(movie -> System.out.println(movie.getProperty("name")));

            PathPrinter pathPrinter = new PathPrinter("name");
            String output = "";
            for(Path path :traverser){
                output += Paths.pathToString(path,pathPrinter);
                output += "\n";
            }
            System.out.println(output);

            tx.success();
        }
        graphDB.shutdown();
    }
}
