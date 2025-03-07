package com.dream.main;

import android.app.Application;
import android.content.Context;
import android.view.View;

import com.dream.alipay.AilPay;
import com.dream.bean.AuthUser;
import com.dream.db.DreamDB;
import com.dream.db.SPUtils;
import com.dream.db.table.Login;
import com.dream.net.DreamNet;
import com.dream.views.imageview.DreamImageView;
import com.dream.views.imageview.DreamImageViewBinding;
import com.dream.views.layout.LayoutItemEdit;
import com.dream.views.layout.LayoutItemEditBinding;
import com.dream.views.progressbar.ProgressBarDeterminateVB;
import com.dream.views.progressbar.XProgressBar;
import com.dream.views.uitra.MaterialPullRefresh;
import com.dream.views.uitra.MaterialPullRefreshVB;
import com.dream.views.xviews.gridview.XGridViewVB;
import com.dream.views.xviews.listview.XListViewVB;
import com.dream.views.xviews.scrollview.XScrollViewVB;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.github.snowdream.android.util.Log;
import com.litesuits.orm.db.DataBase;
import com.paging.gridview.PagingGridView;
import com.slib.pulltoviews.xviews.widget.XListView;
import com.slib.pulltoviews.xviews.widget.XScrollView;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import control.EBEventBus;

/**
 * Created by yangll on 15/8/2.
 */
public class DreamApplication extends Application {

    private static DreamApplication app = null;

    /**
     * 事件总线
     * 用于发送数据在应用中传递
     */
    private static EBEventBus eventBus = null;

    /**
     * 数据库操作
     */
    private static DreamDB db = null;

    /**
     * mvvm 框架
     */
    BinderFactory bf = null;

    /**
     * 网络控制类
     */
    DreamNet dreamNet = null;

    /**
     * SharedPreferences 操作类
     */
    SPUtils spUtils = null;

    final String TAG = "DREAMSHOP";

    /**
     * 登录成功后的用户
     */
    AuthUser user = null;

    /**
     * 购物车
     */


    /**
     * 初始化支付宝支付
     */

    private static AilPay ailPay;

    /**
     * 登录成功后用户所有信息,主要取圆梦币的 除数 fufen_yuan
     */
    Login loginBean;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        eventBus = EBEventBus.createEventBus(true);
        db = new DreamDB(getApplicationContext());
        dreamNet = new DreamNet(getApplicationContext());
        spUtils = new SPUtils(getApplicationContext());
        //初始化自定义绑定视图
        bf = new BinderFactoryBuilder()
                .add(new DreamImageViewBinding().forView(DreamImageView.class))
                .add(new MaterialPullRefreshVB().forView(MaterialPullRefresh.class))
                .add(new XGridViewVB().extend(PagingGridView.class))
                .add(new XListViewVB().extend(XListView.class))
                .add(new XScrollViewVB().extend(XScrollView.class))
                .add(new ProgressBarDeterminateVB().forView(XProgressBar.class))
                .add(new LayoutItemEditBinding().forView(LayoutItemEdit.class))
                .build();

        //初始化图片处理
        Fresco.initialize(getApplicationContext());

        Log.setEnabled(true);
        Log.setGlobalTag(TAG);

    }

    public Login getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(Login loginBean) {
        this.loginBean = loginBean;
    }

    public DreamNet getDreamNet() {
        return dreamNet;
    }

    private ViewBinder getViewBinder(Context ctx) {

        return bf.createViewBinder(ctx, true);
    }

    public View inflateViewAndBind(Context ctx, int layoutId, Object pm) {
        return getViewBinder(ctx).inflateAndBind(layoutId, pm);
    }

    public static DreamApplication getApp() {
        return app;
    }

    public EBEventBus eventBus() {
        return eventBus;
    }

    public DataBase getdb() {
        return db.getdb();
    }

    public AuthUser getUser() {
        return user;
    }

    public void setAuthUser(AuthUser authUser) {
        user = authUser;
    }

    public SPUtils getSharedPreferences() {
        return spUtils;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    public static AilPay ailPay(){
        if(ailPay == null){
            ailPay = new AilPay(getApp());
        }
        return ailPay;
    }


}
