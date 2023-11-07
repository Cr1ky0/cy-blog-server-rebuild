package com.criiky0.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.criiky0.pojo.BlogDoc;
import com.criiky0.pojo.dto.ESDTO;
import com.criiky0.service.MenuService;
import com.criiky0.utils.ElasticSearchUtil;
import com.criiky0.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/es")
public class ESController {
    private MenuService menuService;

    @Autowired
    public ESController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 生成基于menuTitle的分类List
     * 
     * @param docs
     * @return
     */
    private HashMap<String, List<ESDTO>> filterByMenuTitle(List<ESDTO> docs) {
        HashMap<String, List<ESDTO>> map = new HashMap<>();
        for (ESDTO doc : docs) {
            String menuTitle = doc.getMenuTitle();
            boolean contained = map.containsKey(menuTitle);
            // Map不存在menuTitle分类
            if (!contained) {
                ArrayList<ESDTO> list = new ArrayList<>();
                list.add(doc);
                map.put(menuTitle, list);
            } else {
                List<ESDTO> list = map.get(menuTitle);
                list.add(doc);
                map.put(menuTitle, list);
            }
        }
        return map;
    }

    /**
     * search
     * 
     * @param field
     * @return
     */
    @GetMapping("/search")
    public Result searchEs(@RequestParam("field") String field) {
        ElasticsearchClient client = ElasticSearchUtil.client;
        SearchResponse<BlogDoc> search = null;
        try {
            search =
                client
                    .search(
                        s -> s.index("blogs")
                            .query(q -> q.multiMatch(
                                t -> t.fields("content", "title").query(field).type(TextQueryType.BestFields))),
                        BlogDoc.class);
        } catch (IOException e) {
            return Result.build(null, 400, "查询出现未知错误，请重新再试！");
        }
        List<Hit<BlogDoc>> hits = search.hits().hits();
        List<ESDTO> result = new ArrayList<>();
        for (Hit<BlogDoc> hit : hits) {
            BlogDoc source = hit.source();
            String id = hit.id();
            assert source != null;
            if (source.getTitle().toUpperCase().contains(field.toUpperCase())
                || source.getContent().toUpperCase().contains(field.toUpperCase())) {
                result.add(new ESDTO(Long.valueOf(id), source.getTitle(), source.getContent(), source.getMenuTitle()));
            }
        }
        HashMap<String, List<ESDTO>> map = filterByMenuTitle(result);
        HashMap<String, Object> res = new HashMap<>();
        res.put("result", map);
        return Result.ok(res);
    }
}
