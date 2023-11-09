package com.criiky0;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.criiky0.mapper.BlogMapper;
import com.criiky0.mapper.CommentMapper;
import com.criiky0.mapper.MenuMapper;
import com.criiky0.mapper.OssConfigMapper;
import com.criiky0.pojo.Blog;
import com.criiky0.pojo.BlogDoc;
import com.criiky0.pojo.OssConfig;
import com.criiky0.pojo.User;
import com.criiky0.pojo.dto.CommentDTO;
import com.criiky0.pojo.dto.MenuDTO;
import com.criiky0.service.MenuService;
import com.criiky0.utils.ElasticSearchUtil;
import com.criiky0.utils.JavaMailUtil;
import com.criiky0.utils.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;

@SpringBootTest
public class MyTest {

    @Autowired
    ElasticSearchUtil elasticSearchClientFactory;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private MenuService menuService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private OssConfigMapper ossConfigMapper;

    @org.junit.jupiter.api.Test
    public void test1() {
        Integer i = JavaMailUtil.sendCode("503094716@qq.com");
        System.out.println(i);
    }

    @org.junit.jupiter.api.Test
    public void test2() {
        System.out.println(System.getProperty("user.dir"));
    }

    @Test
    public void test4() {
        System.out.println(ElasticSearchUtil.client);
        System.out.println(ElasticSearchUtil.client);
        System.out.println(ElasticSearchUtil.client);
    }

    @Test
    public void test5() throws IOException {
        ElasticSearchUtil.client.indices().create(c -> c.index("users"));
    }

    @Test
    public void test6() {
        Result<HashMap<String, List<MenuDTO>>> menuOfCriiky0 = menuService.getMenuOfCriiky0();
        HashMap<String, List<MenuDTO>> data = menuOfCriiky0.getData();
        List<MenuDTO> menuDTOS = data.get("menus");
        System.out.println(menuDTOS);
    }

    @Test
    public void test7() throws IOException {
        ElasticsearchClient client = ElasticSearchUtil.client;
        client.indices().create(c -> c.index("blogs"));
    }

    @Test
    public void test8() throws IOException {
        User user = new User();
        user.setUsername("12231");
        user.setEmail("123312");
        IndexResponse response = ElasticSearchUtil.client.index(i -> i.index("products").id("12345").document(user));
        System.out.println(response.version());
        System.out.println(response.result());
        System.out.println(response.result().equals(co.elastic.clients.elasticsearch._types.Result.Created));
    }

    @Test
    public void test9() {
        List<Object> paramList = Arrays.asList(null, null);
        boolean isAllNull = paramList.stream().allMatch(Objects::isNull);
        System.out.println(isAllNull);
    }

    @Test
    public void test10() {
        ElasticsearchClient client = ElasticSearchUtil.client;
        UpdateResponse<BlogDoc> response;
        Blog blog = blogMapper.selectById(1718584592037732354L);
        // try {
        // BlogDoc blogDoc = new BlogDoc(blog.getTitle(), blog.getContent());
        // response = client.update(u -> u.index("blogs").id(blog.getBlogId().toString()).doc(blogDoc), BlogDoc.class);
        //
        // } catch (IOException e) {
        // throw new RuntimeException(e);
        // }

        // System.out.println(response.result());
    }

    @Test
    public void test11() throws IOException {
        ElasticsearchClient client = ElasticSearchUtil.client;
        Blog blog = blogMapper.selectById(1718584592037732354L);
        GetResponse<BlogDoc> response =
            client.get(g -> g.index("blogs").id(blog.getBlogId().toString()), BlogDoc.class);
        System.out.println(response.source());
    }

    @Test
    public void test12() throws IOException {
        ElasticsearchClient client = ElasticSearchUtil.client;
        client.indices().delete(c -> c.index("blogs"));
    }

    @Test
    public void test13() {
        Integer maxSort = blogMapper.findMaxSort(1718504282029727746L);
        System.out.println(maxSort);
    }

    @Test
    public void test14() {
        Page<Blog> myPage = new Page<>(1, 5);
        Page<Blog> blogPage = blogMapper.selectPage(myPage, null);
        System.out.println(blogPage.getRecords());
        System.out.println(blogPage.getPages());
        System.out.println(blogPage.getCurrent());
        System.out.println(blogPage.getTotal());
        System.out.println(blogPage.getSize());
    }

    @Test
    public void test15() {
        QueryWrapper<OssConfig> queryWrapper = new QueryWrapper<>();
        OssConfig config = ossConfigMapper.selectOne(queryWrapper);
        System.out.println(config);
    }

    @Test
    public void test16() {
        MenuDTO menuDTO = menuMapper.selectMenuWithDetails(1720737101854478337L);
        System.out.println(menuDTO);
    }

    @Test
    public void test17() throws IOException {
        String searchText = "mybatis";
        ElasticsearchClient client = ElasticSearchUtil.client;

        SearchResponse<BlogDoc> search =
            client.search(
                s -> s.index("blogs")
                    .query(q -> q.multiMatch(
                        t -> t.fields("content", "title").query(searchText).type(TextQueryType.BestFields))),
                BlogDoc.class);
        List<Hit<BlogDoc>> hits = search.hits().hits();
        System.out.println(hits);

    }

    @Test
    public void test18() {
        List<CommentDTO> commentDTOS = commentMapper.selectAllOfBlog(1721776501300305922L);
        System.out.println(commentDTOS);
    }
}
