package com.dream.main.seach;

import android.view.View;

import com.alibaba.fastjson.JSON;
import com.dream.bean.Category;
import com.dream.bean.SeachGood;
import com.dream.main.DreamApplication;
import com.dream.main.titlebar.TitleBarPM;
import com.dream.net.NetResponse;
import com.dream.net.business.ProtocolUrl;
import com.dream.util.ToastUtil;
import com.dream.views.xviews.XLoadEvent;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.widget.adapterview.ItemClickEvent;
import org.robobinding.widget.view.ClickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import control.annotation.Subcriber;
import eb.eventbus.ThreadMode;
import rx.Observable;

/**
 * Created by yangll on 15/9/9.
 */
@PresentationModel
public class SeachPM extends TitleBarPM {

    private String input;

    private final String SEACHTAG = "SEACHTAG";
    List<SeachGood> goodList = new ArrayList<>();

    private List<String> histroySeach = new ArrayList<>();
    private int showempty = View.VISIBLE;
    private int showdataview = View.VISIBLE;


    int page = 1;
    int count = 20;
    int total = 0;
    SeachView view;

    public SeachPM(SeachView view) {
        this.view = view;
        DreamApplication.getApp().eventBus().register(this);
        setTitleBar("搜索");
    }

    private void getSeach() {
        if (StringUtils.isBlank(input)) {
            ToastUtil.show("请输入搜索内容");
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("key", input);
        params.put("page", page);
        params.put("size", count);
        DreamApplication.getApp().getDreamNet().netJsonPost(SEACHTAG, ProtocolUrl.SEACH, params);
    }


    //搜索按钮
    public void seach(ClickEvent event) {
        goodList.clear();
        getSeach();
    }

    @ItemPresentationModel(value = SeachEmptyItemPM.class)
    public List<String> getHistroySeach() {
        return histroySeach;
    }

    public void emptyclickItem(ItemClickEvent event){
        Category c = (Category)event.getParent().getAdapter().getItem(event.getPosition());
        seachStr(c.getName());
    }
    private Observable<List<String>> addData(Category category){
        histroySeach.add(category.getName());
        return Observable.just(histroySeach);
    }
    public int getShowempty() {
        return showempty;
    }

    @Subcriber(tag = SEACHTAG, threadMode = ThreadMode.MainThread)
    public void respHandler(NetResponse response) {
        if (response.getRespType() == NetResponse.SUCCESS) {

            try {
                JSONObject obj = (JSONObject) response.getResp();
                JSONObject dataObj = obj.getJSONObject("data");
                total = dataObj.getInt("total");
                if(total == 0){
                    ToastUtil.show("无结果");
                    goodList.clear();
                    showempty = View.VISIBLE;
                    showdataview = View.GONE;
                    pmRefresh("goodList");
                    pmRefresh("showempty");
                    pmRefresh("showdataview");
                }
                String strArray = dataObj.getJSONArray("list").toString();

                List<SeachGood> goodList = JSON.parseArray(strArray, SeachGood.class);
                this.goodList.addAll(goodList);
                showempty = View.GONE;
                showdataview = View.VISIBLE;
                pmRefresh("showempty");
                pmRefresh("showdataview");
                pmRefresh("goodList");
            } catch (JSONException ex) {
                ToastUtil.show("结果解析失败");
            }
        } else {
            ToastUtil.show("获取结果失败");
        }
        view.stopLoad();
    }

    @ItemPresentationModel(value = SeachItemPM.class)
    public List<SeachGood> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<SeachGood> goodList) {
        this.goodList = goodList;
    }

    //加载更多
    public void onGridLoad(XLoadEvent event) {
        if (page * count >= total){
            ToastUtil.show("没有更多了");
            return;
        }
        page++;
        getSeach();
    }

    public int getShowdataview() {
        return showdataview;
    }

    private void seachStr(String str){
        input = str;
        page = 1;
        goodList.clear();
        showempty = View.VISIBLE;
        showdataview = View.GONE;
        pmRefresh("goodList");
        pmRefresh("showempty");
        pmRefresh("showdataview");
        getSeach();
    }


    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    //点击搜索结果
    public void clickItem(ItemClickEvent clickEvent) {
        SeachGood info = (SeachGood) clickEvent.getParent().getAdapter().getItem(clickEvent.getPosition());
        view.onItenClick(clickEvent.getView(), info);
    }

}
