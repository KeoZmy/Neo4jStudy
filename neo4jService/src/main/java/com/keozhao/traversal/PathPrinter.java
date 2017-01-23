package com.keozhao.traversal;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.Paths;

/**
 * Created by Keo.Zhao on 2016/12/23.
 */
public class PathPrinter implements Paths.PathDescriptor<Path>{

    private final String nodePropertyKey;
    public PathPrinter(String nodePropertyKey){
        this.nodePropertyKey = nodePropertyKey;
    }
    @Override
    public String nodeRepresentation(Path propertyContainers, Node node) {

        return "(" + node.getProperty(nodePropertyKey,"") + ")";
    }

    @Override
    public String relationshipRepresentation(Path propertyContainers, Node node, Relationship relationship) {
       String prefix = "--",suffix = "--";
       if(node.equals(relationship.getEndNode())){
           prefix = "<--";
       }else{
           suffix = "-->";
       }


        return prefix + "[" + relationship.getType().name() + "]" +suffix;
    }
}
