package com.personal.springframework.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static jdk.nashorn.internal.objects.NativeArray.sort;

/**
 * @program: springframework
 * @description: es
 * @author: 安少军
 * @create: 2022-01-10 10:46
 **/
@Slf4j
@Service
public class EScommonService {

    @Autowired
    @Qualifier(value = "client")
    private RestHighLevelClient restHighLevelClient;

    public String index(Object object, String index) throws IOException {
        IndexRequest indexRequest = new IndexRequest(index).source(JSONObject.fromObject(object).toString(), XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        log.info("ES index {}", indexResponse.status().toString());
        return indexResponse.status().toString();
    }

    public String batchIndex(List object, String index) throws IOException {
        BulkRequest request = new BulkRequest();
        object.forEach(o -> {
            IndexRequest indexRequest = new IndexRequest(index).source(JSONObject.fromObject(o).toString(), XContentType.JSON);
            request.add(indexRequest);
        });
        BulkResponse responses = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        log.info("ES indexes {}", responses.status().toString());
        return responses.status().toString();
    }

    public String delete(String index, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index, id);
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        log.info("ES delete {}", deleteResponse.status().toString());
        return deleteResponse.status().toString();
    }

    public String update(String index, String id, Map<String, Object> data) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(index, id).doc(data);
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        log.info("ES update {}", updateResponse.status().toString());
        return updateResponse.status().toString();
    }

    public JSONArray search(int start, int count, String sortColumn, String... index) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery())
                .from(start)
                .size(count)
                .postFilter(QueryBuilders.matchAllQuery());
                /*.sort(new FieldSortBuilder(sortColumn).order(SortOrder.DESC));*/
        //创建搜索请求
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        JSONArray array = new JSONArray();
        log.info("ES search result:");
        Arrays.asList(searchResponse.getHits().getHits()).forEach(o -> {
            JSONObject jsonObject = JSONObject.fromObject(o.getSourceAsMap());
            array.add(jsonObject);
            log.info(jsonObject.toString());
        });
        return array;
    }
}
