package com.keozhao.API;

import org.neo4j.graphdb.RelationshipType;

/**
 * Created by Keo.Zhao on 2016/12/23.
 * 关系类型枚举类
 */
public enum MyRelationshipTypes implements RelationshipType{
    IS_FRIEND_OF,
    HAS_SEEN
}
