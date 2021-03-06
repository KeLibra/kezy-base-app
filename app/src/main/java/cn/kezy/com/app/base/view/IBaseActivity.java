package cn.kezy.com.app.base.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.kezy.libs.common.base.CommonBaseActivity;
import cn.kezy.libs.common.utils.LogUtils;
import cn.kezy.libs.common.utils.PermissionUtils;
import cn.kezy.libs.common.utils.ToastUtil;
import cn.kezy.libs.common.view.base.DefaultTitleView;

import static android.view.View.OnClickListener;


/**
 * 基础activity
 *
 */
public abstract class IBaseActivity extends CommonBaseActivity {
    private boolean isForeground;

    private Unbinder unbinder;

    @Override
    protected void initContentView() {
        super.initContentView();
        unbinder = ButterKnife.bind(this);
        initView();

    }

    @Override
    protected void initData() {
        super.initData();
        loadData();
    }

    @Override
    protected boolean hasTitle() {
        return true;
    }

    protected boolean hasBack() {
        return true;
    }

    protected DefaultTitleView fmTitleView = new DefaultTitleView(this);

    @Override
    protected DefaultTitleView getTitleView() {
        return fmTitleView;
    }

    protected void setBack(final OnClickListener onClickListener) {
        if (fmTitleView != null && fmTitleView.getBackView() != null) {
            fmTitleView.getBackView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(v);
                }
            });
        }
    }

    protected String getPageName() {
        return null;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        if (fmTitleView != null && hasTitle()) {
            if (!hasBack()) {
                fmTitleView.setImageBackShow(false);
            }
            if (getPageName() != null) {
                fmTitleView.setTitle(getPageName());
            }
        }
    }

    protected void setTitle(String title) {
        if (fmTitleView != null && hasTitle() && !TextUtils.isEmpty(title)) {
            fmTitleView.setTitle(title);
        }
    }


    protected void setImageBackIconRes(int rightIconRes) {
        if (fmTitleView != null) {
            fmTitleView.setImageBackIconRes(rightIconRes);
        }
    }

    protected void setRightIcon(int rightIconRes, OnClickListener rightIconClickListener) {
        if (fmTitleView != null) {
            fmTitleView.setRightIcon(rightIconRes, rightIconClickListener);
        }
    }

    protected void setRightTitle(String rightTitle, OnClickListener rightIconClickListener) {
        if (fmTitleView != null) {
            fmTitleView.setRightTitle(rightTitle, rightIconClickListener);
        }
    }


    @Override
    protected void onErrorRefresh() {
        super.onErrorRefresh();
        loadData();
    }

    public FragmentActivity getActivity() {
        if (getParent() != null) {
            return (FragmentActivity) getParent();
        }
        return this;
    }

    @Override
    public void onResume() {
        super.onResume();
        isForeground = true;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        isForeground = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (onRequestPermissionListener != null) {
            onRequestPermissionListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        PermissionUtils.dispatchPermissionResult(this, requestCode, permissions, grantResults, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private OnRequestPermissionListener onRequestPermissionListener;

    public void setOnRequestPermissionListener(OnRequestPermissionListener onRequestPermissionListener) {
        this.onRequestPermissionListener = onRequestPermissionListener;
    }

    public interface OnRequestPermissionListener {
        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }

    protected boolean umengPage() {
        return true;
    }

    public void dofinish() {
        finish();
    }


    public void showToast(String msg) {
        ToastUtil.show(msg);
    }

    public void showLoading(final String msg) {
        showProgressDialog(msg);
    }

    public void hideLoading() {
        dismissProgressDialog();
    }

    public Context getContext() {
        return this;
    }

    public Intent _getIntent() {
        return getIntent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (unbinder != null) {
            unbinder.unbind();
        }
        /**
         * 如果当前类注册了event bus， 就注销掉
         */
        if (EventBus.getDefault().isRegistered(this)) {
            LogUtils.i("=====msg_event", "unregister : " + this.getClass().getSimpleName());
            EventBus.getDefault().unregister(this);
        }
    }

    protected boolean isForeground() {
        return isForeground;
    }

    public void nextActivity(Class<? extends IBaseActivity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }


    /**
     * 给当前activity添加控制事件
     */
    protected abstract void initView();

    /**
     * 加载数据
     */
    protected abstract void loadData();

    public void hideEmptyPage() {
        showContent();
    }

    public void showEmptyPage(int errorCode) {
        if (errorCode == -1) {
            showErrorPage(errorCode);
        }
    }


    /**
     * 传递onActivityResult
     */
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 注册 event bus， 用于通信
     */
    public void registerEventBus() {
        LogUtils.i("=====msg_event", "register : " + this.getClass().getSimpleName());
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    //	/**
//	 * 响应 eventbus 消息， 在主线程处理
//	 */
//	@Subscribe(threadMode = ThreadMode.MAIN)
//	public void responseEventBusMainThread() {
//
//	}
}
