package com.keozhao.traversal;

import com.keozhao.API.MyRelationshipTypes;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;

/**
 * Created by Keo.Zhao on 2016/12/29.
 */
public class CustomNodeFilteringEvaluator implements Evaluator{
    private final Node userNode;
    //构造函数中为评估函数提供的起始节点
    public CustomNodeFilteringEvaluator (Node userNode){
        this.userNode = userNode;
    }
    @Override
    public Evaluation evaluate(Path path) {
        Node currentNode = path.endNode();
        if(!currentNode.hasProperty("type") ||
                !currentNode.getProperty("type").equals("Movie")){
            return Evaluation.EXCLUDE_AND_CONTINUE;//如果当前节点不是一部电影，丢弃它并继续
        }
        //通过当前电影节点所有内向的HAS_SEEN关系迭代
        for(Relationship r : currentNode.getRelationships(Direction.INCOMING, MyRelationshipTypes.HAS_SEEN)) {
            if (r.getStartNode().equals(userNode)){
                //如果关系的起始节点与用户当前节点详图，丢弃当前节点（因为用户已经看过），并继续
                return Evaluation.EXCLUDE_AND_CONTINUE;
            }
        }
        //否则，包含结果中的当前节点并继续
        return Evaluation.INCLUDE_AND_CONTINUE;

    }
}
