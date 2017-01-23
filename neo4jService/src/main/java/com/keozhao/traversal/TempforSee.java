package com.keozhao.traversal;


/**
 * Created by Keo.Zhao on 2016/12/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringbootSdnApplication.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;
    /**
     * 因为是通过http连接到Neo4j数据库的，所以要预先启动Neo4j：neo4j console
     */
    @Test
    public void testInitData(){
        userService.initData();
    }
    @Test
    public void testGetUserByName(){
        User user = userService.getUserByName("John Johnson");
        System.out.println(user);
    }
}