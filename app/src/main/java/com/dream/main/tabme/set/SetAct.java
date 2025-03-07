package com.dream.main.tabme.set;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dream.R;
import com.dream.main.DreamApplication;
import com.dream.main.base.BaseActView;
import com.dream.main.base.BaseActivity;

import butterknife.Bind;

/**
 * zhangyao
 * zhangyao@guoku.com
 * 15/9/13 22:51
 */
public class SetAct extends BaseActivity implements SetView {

    @Bind(R.id.btLogOut)
    TextView btLogOut;

    SetPM setPM;

    @Override
    public void setOnClickView(View view) {
    }

    @Override
    public void onClickView() {
        finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    public Object initPM() {
        if(setPM == null){
            setPM = new SetPM(this, this);
        }
        return setPM;
    }

    @Override
    public void initView() {
        if (DreamApplication.getApp().getUser() != null) {
            btLogOut.setVisibility(View.VISIBLE);
        }else{
            btLogOut.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (DreamApplication.getApp().eventBus() != null) {
            DreamApplication.getApp().eventBus().unregister(this);
        }
    }
}
