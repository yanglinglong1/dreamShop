package com.dream.main.tabme.record;


import com.dream.R;
import com.dream.main.base.BaseFragment;

/**
 * zhangyao
 * zhangyao@guoku.com
 * 15/9/9 25:12
 * 我的元梦购已经结束的
 */

public class MyDreamRecordunFragment extends BaseFragment {

	MyDreamRecordunFragmentPM fragmentPM;

	@Override
	public int getlayoutId() {
		return R.layout.fragment_two;
	}

	@Override
	public Object initPM() {
		if(fragmentPM == null){
			fragmentPM = new MyDreamRecordunFragmentPM();
		}
		return fragmentPM;
	}
}