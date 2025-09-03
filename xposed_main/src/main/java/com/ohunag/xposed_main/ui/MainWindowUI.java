package com.ohunag.xposed_main.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.bean.ViewRootMsg;
import com.ohunag.xposed_main.util.ToastUtil;
import com.ohunag.xposed_main.util.UiUtil;
import com.ohunag.xposed_main.view.DrawRectView;
import com.ohunag.xposed_main.view.HookRootFrameLayout;
import com.ohunag.xposed_main.view.MyListView;
import com.ohunag.xposed_main.viewTree.ViewNode;
import com.ohunag.xposed_main.viewTree.ViewTreeUtil;
import com.ohunag.xposed_main.viewTree.intercept.ViewNodeValueIntercept;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainWindowUI {
    private HookRootFrameLayout rootView;
    private Activity activity;
    private WindowManager.LayoutParams layoutParams;
    public boolean isShow = false;
    private boolean isInit = false;
    private ViewGroup ll_activity_xposed;
    private TextView tv_packageName_xposed;
    private TextView tv_activityName_xposed;
    private TextView tv_selectView_xposed;
    private TextView tv_viewClick_xposed;
    private TextView tv_viewRule_xposed_all;
    private TextView tv_viewRule_show_xposed;

    private TextView tv_close_xposed;
    private TextView tv_getRootView_xposed;

    private TextView tv_getId_xposed;

    private WeakReference<View> selectView;//选中的View
    private ViewRootMsg viewRootMsg;//选中的View信息


    private ViewGroup fl_touch_xposed;

    private ViewGroup ll_clickView_xposed;
    private TextView tv_process_clickView_xposed;
    private TextView tv_openInLayout_clickView_xposed;
    private TextView tv_lastNode_clickView_xposed;
    private TextView tv_nextNode_clickView_xposed;
    private TextView tv_viewName_clickView_xposed;
    private TextView tv_close_xposed_clickView;
    private TextView tv_show_clickView_xposed;
    private TextView tv_showImg_xposed_clickView;
    private DrawRectView drv_main_view;

    private ViewGroup ll_getRoot_xposed;

    private HorizontalScrollView hs_parentList_viewTree_xposed;
    private LinearLayout ll_parentList_viewTree_xposed;
    private TextView tv_parentNode_viewTree_xposed;
    private TextView tv_childNode_viewTree_xposed;
    private ListView listView_viewTree_xposed;
    private TextView tv_close_xposed_viewTree_xposed;
    private TextView tv_show_viewTree_xposed;
    private TextView tv_showImg_viewTree_xposed;


    private ViewGroup ll_viewMsg_xposed;
    private MyListView listView_viewMsg;
    private TextView tv_close_xposed_viewMsg;
    private TextView tv_edit_viewMsg_xposed;
    private TextView tv_show_xposed_viewMsg;

    private ViewMsgEditDialog viewMsgEditDialog;


    private ViewGroup ll_selectView_xposed;
    private TextView tv_select_activity_selectView_xposed;
    private ListView listView_selectView_xposed;
    private TextView tv_close_xposed_selectView;


    private ViewNode rootViewNode;
    private final List<ViewNode> nodes = new ArrayList<>();


    private ViewGroup ll_imgView_xposed;
    private ImageView iv_show;
    private TextView tv_close_xposed_imgView;

    public MainWindowUI(Activity activity) {
        this.activity = activity;
    }

    public void show() {
        if (!isInit) {
            init();
            isInit = true;
        }
        if (!isShow) {
            isShow = true;
            getWindowManager(activity).addView(rootView, layoutParams);
//            mainDialog.show();
            setSate(0);
        }
    }


    public void hide() {
        if (!isInit) {
            return;
        }
        if (isShow) {
            isShow = false;
            getWindowManager(activity).removeView(rootView);
//            mainDialog.dismiss();
        }
    }

    /**
     * @param sate
     */
    private void setSate(int sate) {

        ll_activity_xposed.setVisibility(View.GONE);
        ll_clickView_xposed.setVisibility(View.GONE);
        fl_touch_xposed.setVisibility(View.GONE);
        drv_main_view.setVisibility(View.GONE);
        ll_viewMsg_xposed.setVisibility(View.GONE);
        ll_getRoot_xposed.setVisibility(View.GONE);
        ll_selectView_xposed.setVisibility(View.GONE);
        ll_imgView_xposed.setVisibility(View.GONE);
        if (sate == 0) { //默认状态
            ll_activity_xposed.setVisibility(View.VISIBLE);
        } else if (sate == 1) { //点击位置界面
            fl_touch_xposed.setVisibility(View.VISIBLE);
        } else if (sate == 2) {// 点击位置的viewNode界面
            ll_clickView_xposed.setVisibility(View.VISIBLE);
            drv_main_view.setVisibility(View.VISIBLE);
        } else if (sate == 3) {// viewNode信息界面
            ll_viewMsg_xposed.setVisibility(View.VISIBLE);
            drv_main_view.setVisibility(View.VISIBLE);
        } else if (sate == 4) {// viewNode查看布局界面
            ll_getRoot_xposed.setVisibility(View.VISIBLE);
            drv_main_view.setVisibility(View.VISIBLE);
        } else if (sate == 5) { //选择viewRoot
            ll_selectView_xposed.setVisibility(View.VISIBLE);
        } else if (sate == 6) {//显示View预览
            ll_imgView_xposed.setVisibility(View.VISIBLE);
        }
    }


    private void init() {

        rootView = new HookRootFrameLayout(activity);
//        mainDialog=new AlertDialog.Builder(activity)
//                .setCancelable(false)
//                .setView(rootView)
//                .create();
        layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        layoutParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        if (UiHook.xpRes == null) {
            return;
        }
        ViewGroup mainWindow = (ViewGroup) LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.ui_main_window_xposed), null, false);
        rootView.addView(mainWindow);
        fl_touch_xposed = mainWindow.findViewWithTag("fl_touch_xposed");
        drv_main_view = new DrawRectView(activity);
        drv_main_view.setBackgroundColor(0x20000000);
        mainWindow.addView(drv_main_view, 0);
        fl_touch_xposed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (rootViewNode == null) {
                            setSate(0);
                            Toast.makeText(activity, "获取不到View", Toast.LENGTH_LONG).show();
                        } else {
                            float rawX = event.getRawX();
                            float rawY = event.getRawY();
                            findTouchView(rawX, rawY);
                        }
                        break;
                }
                return true;
            }
        });
        init_ll_activity_xposed();
        init_clickView(mainWindow);
        init_ll_viewMsg_xposed(mainWindow);
        init_ll_getRoot_xposed(mainWindow);
        init_ll_selectView_xposed(mainWindow);
        init_ll_imgView_xposed(mainWindow);
    }

    private void init_ll_imgView_xposed(ViewGroup mainWindow) {
        View inflate = LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.ui_main_window_img_view), null, false);
        ll_imgView_xposed = mainWindow.findViewWithTag("ll_imgView_xposed");
        ll_imgView_xposed.addView(inflate);
        iv_show = inflate.findViewWithTag("iv_show");
        tv_close_xposed_imgView = inflate.findViewWithTag("tv_close_xposed_imgView");
        tv_close_xposed_imgView.setOnClickListener(v -> {
            setSate(0);
        });

    }

    private void init_ll_selectView_xposed(ViewGroup mainWindow) {
        View inflate = LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.ui_main_window_select_view_xposed), null, false);
        ll_selectView_xposed = mainWindow.findViewWithTag("ll_selectView_xposed");
        ll_selectView_xposed.addView(inflate);
        tv_select_activity_selectView_xposed = inflate.findViewWithTag("tv_select_activity_selectView_xposed");
        listView_selectView_xposed = inflate.findViewWithTag("listView_selectView_xposed");
        tv_close_xposed_selectView = inflate.findViewWithTag("tv_close_xposed_selectView");
        tv_select_activity_selectView_xposed.setOnClickListener(v -> {
            selectViewRoot(null, null);
            setSate(0);
        });
        tv_close_xposed_selectView.setOnClickListener(v -> setSate(0));
    }

    private void init_ll_getRoot_xposed(ViewGroup mainWindow) {
        View inflate = LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.ui_main_window_view_tree_xposed), null, false);
        ll_getRoot_xposed = mainWindow.findViewWithTag("ll_getRoot_xposed");
        ll_getRoot_xposed.addView(inflate);
        ll_parentList_viewTree_xposed = inflate.findViewWithTag("ll_parentList_viewTree_xposed");
        hs_parentList_viewTree_xposed = inflate.findViewWithTag("hs_parentList_viewTree_xposed");
        tv_parentNode_viewTree_xposed = inflate.findViewWithTag("tv_parentNode_viewTree_xposed");
        tv_childNode_viewTree_xposed = inflate.findViewWithTag("tv_childNode_viewTree_xposed");
        listView_viewTree_xposed = inflate.findViewWithTag("listView_viewTree_xposed");
        tv_close_xposed_viewTree_xposed = inflate.findViewWithTag("tv_close_xposed_viewTree_xposed");
        tv_show_viewTree_xposed = inflate.findViewWithTag("tv_show_viewTree_xposed");
        tv_showImg_viewTree_xposed = inflate.findViewWithTag("tv_showImg_viewTree_xposed");
        tv_close_xposed_viewTree_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSate(0);
            }
        });

    }

    private void init_ll_viewMsg_xposed(ViewGroup mainWindow) {

        View inflate = LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.ui_main_window_view_msg_xposed), null, false);
        ll_viewMsg_xposed = mainWindow.findViewWithTag("ll_viewMsg_xposed");
        ll_viewMsg_xposed.addView(inflate);
        ViewGroup srv_viewMsg = ll_viewMsg_xposed.findViewWithTag("scv_viewMsg_xposed");
        listView_viewMsg = new MyListView(activity);
        srv_viewMsg.addView(listView_viewMsg, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv_close_xposed_viewMsg = ll_viewMsg_xposed.findViewWithTag("tv_close_xposed_viewMsg");
        tv_edit_viewMsg_xposed = ll_viewMsg_xposed.findViewWithTag("tv_edit_viewMsg_xposed");
        tv_show_xposed_viewMsg = ll_viewMsg_xposed.findViewWithTag("tv_show_xposed_viewMsg");
        tv_close_xposed_viewMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }

    private void set_ll_selectView_xposed() {
        List<ViewRootMsg> viewRootMsgs = new ArrayList<>();
        ViewTreeUtil.getFragmentViewRootMsg(activity, viewRootMsgs);
        List<View> rootViews = UiHook.getRootViews();
        for (View view : rootViews) {
            if (view != null) {
                viewRootMsgs.add(new ViewRootMsg(view.toString(), view, view));
            }
        }
        viewRootMsgs.addAll(UiHook.getDialogs());
        ViewRootListAdapter viewRootListAdapter = new ViewRootListAdapter(viewRootMsgs);
        listView_selectView_xposed.setAdapter(viewRootListAdapter);
        viewRootListAdapter.setListener(new ViewRootListAdapter.Listener() {
            @Override
            public void onShow(WeakReference<View> weakReference) {
                if (weakReference.get() != null) {
                    set_ll_imgView_xposed(weakReference.get(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setSate(5);
                        }
                    });
                } else {
                    ToastUtil.show(activity, "当前ViewRoot不存在");
                    set_ll_selectView_xposed();
                }
            }

            @Override
            public void onSelect(ViewRootMsg weakReference) {
                if (weakReference.getView() != null) {
                    selectViewRoot(weakReference.getView(), weakReference);
                    setSate(0);
                } else {
                    ToastUtil.show(activity, "当前ViewRoot不存在");
                    set_ll_selectView_xposed();
                }

            }
        });

    }

    private void set_ll_viewMsg_xposed(ViewNode viewNode, View.OnClickListener listener) {
        setSate(3);
        listView_viewMsg.setAdapter(new ViewMsgAdapter(viewNode));
        tv_edit_viewMsg_xposed.setVisibility(View.VISIBLE);
        tv_show_xposed_viewMsg.setVisibility(View.VISIBLE);
        tv_edit_viewMsg_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_ll_viewMsg_xposed_edit(viewNode);
            }
        });
        tv_show_xposed_viewMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_ll_imgView_xposed(viewNode.getView(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        set_ll_viewMsg_xposed(viewNode, listener);
                    }
                });
            }
        });
        tv_close_xposed_viewMsg.setOnClickListener(listener);
    }

    private void set_ll_viewMsg_xposed_edit(ViewNode viewNode) {
        if (viewMsgEditDialog == null) {
            viewMsgEditDialog = new ViewMsgEditDialog(activity);
        }
        viewMsgEditDialog.set_ll_viewMsg_xposed_edit(viewNode);
        viewMsgEditDialog.show();
    }

    /**
     * @param view
     * @param listener
     */
    private void set_ll_imgView_xposed(View view, View.OnClickListener listener) {
        setSate(6);
        iv_show.setImageDrawable(null);
        if (view != null) {
            Bitmap bitmap = UiUtil.getDownscaledBitmapForView(view);
            if (bitmap != null) {
                iv_show.setImageBitmap(bitmap);
            }
        }
        iv_show.setBackgroundColor(0x00ffffff);
        iv_show.setSelected(false);
        iv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_show.isSelected()) {
                    iv_show.setBackgroundColor(0x00ffffff);
                    iv_show.setSelected(false);
                } else {
                    iv_show.setBackgroundColor(0xffffffff);
                    iv_show.setSelected(true);
                }
            }
        });
        tv_close_xposed_imgView.setOnClickListener(listener);
    }

    ViewShowRuleDialog.ViewShowRule viewShowRule = new ViewShowRuleDialog.ViewShowRule();

    private void findTouchView(float x, float y) {
        if (rootViewNode != null) {
            nodes.clear();
            ViewNode.ForeachCallBack foreachCallBack = viewNode -> {
                if (viewNode.getView() != null) {
                    int xdt = 20;//防止点不准 设置误差距离
                    int ydt = 20;//防止点不准 设置误差距离
                    View view = viewNode.getView();
                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    if (x > location[0] - xdt && y > location[1] - ydt) {
                        if (x < location[0] + view.getWidth() + xdt && y < location[1] + view.getHeight() + ydt) {
                            if (viewShowRule.isAddViewForRule(viewNode)) {
                                nodes.add(viewNode);
                            }
                        }
                    }
                }
                return false;
            };
            if (viewShowRule.onlyVisibility) {
                rootViewNode.afterTraversalVisibleView(foreachCallBack);
            } else {
                rootViewNode.afterTraversal(foreachCallBack);
            }
        }
        if (nodes.size() == 0) {
            Toast.makeText(activity, "当前位置没有限定的View", Toast.LENGTH_LONG).show();
            setSate(0);
        } else {
            showSelectViewList(0);

        }
    }

    private void findAllView() {
        refreshRootViewNode();
        if (rootViewNode != null) {
            nodes.clear();
            ViewNode.ForeachCallBack foreachCallBack = viewNode -> {
                if (viewNode.getView() != null) {
                    if (viewShowRule.isAddViewForRule(viewNode)) {
                        nodes.add(viewNode);
                    }
                }
                return false;
            };
            if (viewShowRule.onlyVisibility){
                rootViewNode.afterTraversalVisibleView(foreachCallBack);
            }else {
                rootViewNode.afterTraversal(foreachCallBack);
            }

        }
        if (nodes.size() == 0) {
            Toast.makeText(activity, "没有找到View", Toast.LENGTH_LONG).show();
            setSate(0);
        } else {
            showSelectViewList(0);
        }
    }

    /**
     * 一开始的按钮
     */
    private void init_ll_activity_xposed() {
        ll_activity_xposed = rootView.findViewWithTag("ll_activity_xposed");
        tv_packageName_xposed = rootView.findViewWithTag("tv_packageName_xposed");
        tv_activityName_xposed = rootView.findViewWithTag("tv_activityName_xposed");
        tv_selectView_xposed = rootView.findViewWithTag("tv_selectView_xposed");
        tv_viewClick_xposed = rootView.findViewWithTag("tv_viewClick_xposed");
        tv_viewRule_xposed_all = rootView.findViewWithTag("tv_viewRule_xposed_all");
        tv_viewRule_show_xposed = rootView.findViewWithTag("tv_viewRule_show_xposed");

        tv_close_xposed = rootView.findViewWithTag("tv_close_xposed");
        tv_getRootView_xposed = rootView.findViewWithTag("tv_getRootView_xposed");
        tv_getId_xposed = rootView.findViewWithTag("tv_getId_xposed");
        tv_activityName_xposed.setText(activity.getClass().getName());
        tv_packageName_xposed.setText("包名:" + activity.getPackageName());
        tv_activityName_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewRootMsg != null && viewRootMsg.getObjectWeakReference() != null) {
                    ObjectMsgDailog objectMsgDailog = new ObjectMsgDailog(activity);
                    objectMsgDailog.setObject(viewRootMsg.getObjectWeakReference());
                    objectMsgDailog.show();
                } else {
                    ObjectMsgDailog objectMsgDailog = new ObjectMsgDailog(activity);
                    objectMsgDailog.setObject(activity);
                    objectMsgDailog.show();
                }
            }
        });
        tv_selectView_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_ll_selectView_xposed();
                setSate(5);
            }
        });
        tv_viewClick_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshRootViewNode();
                setSate(1);
            }
        });

        tv_viewRule_xposed_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewShowRuleDialog viewShowRuleDialog = new ViewShowRuleDialog(activity, viewShowRule, new ViewShowRuleDialog.OnRuleListener() {
                    @Override
                    public void onRule(ViewShowRuleDialog.ViewShowRule viewShowRule) {
                        MainWindowUI.this.viewShowRule = viewShowRule;
                    }
                });
                viewShowRuleDialog.show();
            }
        });
        tv_viewRule_show_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAllView();//查找所有View
            }
        });

        tv_getRootView_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshRootViewNode();
                showTreeView(rootViewNode, 0, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSate(0);
                    }
                });

            }
        });
        tv_getId_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputIdDialog();
            }
        });
        tv_close_xposed.setOnClickListener(v -> {
            hide();
        });
    }

    InputIdTextDialog inputIdDialog = null;

    private void showInputIdDialog() {
        if (inputIdDialog == null) {
            inputIdDialog = new InputIdTextDialog(activity, "根据id查找", "请输入View idName", new InputIdTextDialog.OnInputTextListener() {
                @Override
                public void onInputText(String text, boolean isVisible) {
                    showViewById(text, isVisible);
                }
            });
        }
        inputIdDialog.show();
    }

    private void showViewById(String string, boolean isVisible) {
        refreshRootViewNode();
        if (rootViewNode != null) {
            int id = 0;
            try {
                id = activity.getResources().getIdentifier(string, "id", activity.getPackageName());
            } catch (Exception e) {
            }
            if (id == 0) {
                ToastUtil.show(activity, "id=" + string + "不存在");
                return;
            }
            nodes.clear();
            int finalId = id;
            ViewNode.ForeachCallBack call = new ViewNode.ForeachCallBack() {
                @Override
                public boolean onIntercept(ViewNode viewNode) {
                    if (viewNode.getView().getId() == finalId) {
                        nodes.add(viewNode);
                    }
                    return false;
                }
            };
            if (isVisible) {
                rootViewNode.frontTraversalVisibleView(call);
            } else {
                rootViewNode.frontTraversal(call);
            }

            if (nodes.isEmpty()) {
                ToastUtil.show(activity, "未找到View");
            } else {
                showSelectViewList(0);
            }
        } else {
            ToastUtil.show(activity, "viewRoot不存在");
        }

    }

    /**
     * 刷新viewNode
     */
    private void refreshRootViewNode() {
        if (selectView == null) {
            rootViewNode = ViewTreeUtil.getViewNode(activity);
        } else {
            if (selectView.get() != null) {
                rootViewNode = ViewTreeUtil.getViewNode(selectView.get(), null);
            } else {
                ToastUtil.show(activity, "当前ViewRoot不存在,自动选择activity");
                selectViewRoot(null, null);
                rootViewNode = ViewTreeUtil.getViewNode(activity);
            }
        }
    }

    /**
     * 设置根view
     *
     * @param view
     * @param vr
     */
    private void selectViewRoot(View view, ViewRootMsg vr) {
        if (view == null) {
            tv_selectView_xposed.setText("选择根View:当前activity");
            selectView = null;
            viewRootMsg = null;
            tv_activityName_xposed.setText(activity.getClass().getName());
        } else {
            tv_selectView_xposed.setText("选择根View:" + view.toString());
            selectView = new WeakReference<>(view);
            if (vr != null) {
                viewRootMsg = vr;
                tv_activityName_xposed.setText(vr.getViewName());
            } else {
                viewRootMsg = null;
                tv_activityName_xposed.setText(activity.getClass().getName());
            }
        }
    }

    private void init_clickView(ViewGroup mainWindow) {
        View inflate = LayoutInflater.from(activity.getApplicationContext()).inflate(UiHook.xpRes.getLayout(R.layout.ui_main_window_click_view_xposed), null, false);

        ll_clickView_xposed = mainWindow.findViewWithTag("ll_clickView_xposed");
        ll_clickView_xposed.addView(inflate);
        tv_process_clickView_xposed = inflate.findViewWithTag("tv_process_clickView_xposed");
        tv_openInLayout_clickView_xposed = inflate.findViewWithTag("tv_openInLayout_clickView_xposed");
        tv_lastNode_clickView_xposed = inflate.findViewWithTag("tv_lastNode_clickView_xposed");
        tv_nextNode_clickView_xposed = inflate.findViewWithTag("tv_nextNode_clickView_xposed");
        tv_viewName_clickView_xposed = inflate.findViewWithTag("tv_viewName_clickView_xposed");

        tv_close_xposed_clickView = inflate.findViewWithTag("tv_close_xposed_clickView");
        tv_show_clickView_xposed = inflate.findViewWithTag("tv_show_clickView_xposed");
        tv_showImg_xposed_clickView = inflate.findViewWithTag("tv_showImg_xposed_clickView");
        tv_close_xposed_clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSate(0);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showSelectViewList(int position) {
        setSate(2);
        if (nodes.isEmpty()) {
            return;
        }
        if (position >= nodes.size() || position < 0) {
            return;
        }
        View view = nodes.get(position).getView();
        tv_process_clickView_xposed.setText("进度" + (position+1) + "/" + nodes.size() + "  "
                + ViewTreeUtil.getViewType(view) + " Visibility:" + getVisibility(view.getVisibility())
                + " " + ViewNodeValueIntercept.getViewIdName(view));
        tv_viewName_clickView_xposed.setText(nodes.get(position).getViewClassName());
        tv_openInLayout_clickView_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nodes.get(position) != null) {

                    showTreeView(nodes.get(position), 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showSelectViewList(position);
                        }
                    });
                }
            }
        });
        drv_main_view.clear();
        showViewRect(view);
        tv_lastNode_clickView_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = position - 1;
                if (p < 0) {
                    p = nodes.size() - 1;
                }
                showSelectViewList(p);
            }
        });
        tv_nextNode_clickView_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = position + 1;
                if (p >= nodes.size()) {
                    p = 0;
                }
                showSelectViewList(p);
            }
        });
        tv_show_clickView_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set_ll_viewMsg_xposed(nodes.get(position), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSelectViewList(position);

                    }
                });
            }
        });
        tv_showImg_xposed_clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set_ll_imgView_xposed(nodes.get(position).getView(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSelectViewList(position);

                    }
                });
            }
        });
    }

    private void showViewRect(View view) {
        if (view != null) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            drv_main_view.setRect(location[0], location[1], location[0] + view.getWidth(), location[1] + view.getHeight());
        }
    }

    /**
     *  显示View树
     * @param viewNode
     * @param selectIndex
     * @param listener
     */
    private void showTreeView(ViewNode viewNode, int selectIndex, View.OnClickListener listener) {
        setSate(4);
        ViewTreeAdapter viewTreeAdapter = new ViewTreeAdapter(viewNode, selectIndex);
        listView_viewTree_xposed.setAdapter(viewTreeAdapter);
        ViewNode selectNode = viewTreeAdapter.getSelectNode();
        if (selectNode != null) {
            showViewRect(selectNode.getView());
        }
        List<ViewNode> viewNodesPath = new ArrayList<>();
        viewNode.getViewNodePath(viewNodesPath);
        ll_parentList_viewTree_xposed.removeAllViews();
        for (ViewNode node : viewNodesPath) {
            TextView textView = new TextView(activity);
            textView.setText("[" + node.inParentIndex() + "]" + node.getViewClassName() + ">");
            textView.setTextColor(0xffffffff);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTreeView(node, 0, listener);

                }
            });
            ll_parentList_viewTree_xposed.addView(textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        ll_parentList_viewTree_xposed.post(new Runnable() {
            @Override
            public void run() {
                hs_parentList_viewTree_xposed.scrollTo(ll_parentList_viewTree_xposed.getWidth(), 0);
            }
        });

        viewTreeAdapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewNode selectNode = viewTreeAdapter.getSelectNode();
                if (selectNode != null) {
                    showViewRect(selectNode.getView());
                }
            }
        });
        tv_parentNode_viewTree_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewNode.getParent() != null) {
                    showTreeView(viewNode.getParent(), viewNode.inParentIndex() + 1, listener);

                }
            }
        });
        tv_childNode_viewTree_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewTreeAdapter.getSelectNode() == null || viewTreeAdapter.getSelectNode() == viewNode) {
                    return;
                }
                showTreeView(viewTreeAdapter.getSelectNode(), 0, listener);
            }
        });
        tv_show_viewTree_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set_ll_viewMsg_xposed(viewTreeAdapter.getSelectNode(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTreeView(viewNode, viewTreeAdapter.getSelectId(), listener);
                    }
                });
            }
        });
        tv_showImg_viewTree_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_ll_imgView_xposed(viewTreeAdapter.getSelectNode().getView(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showTreeView(viewNode, viewTreeAdapter.getSelectId(), listener);
                    }
                });
            }
        });
        tv_close_xposed_viewTree_xposed.setOnClickListener(listener);

    }

    public static String getVisibility(int visibly) {
        if (visibly == View.VISIBLE) {
            return "VISIBLE";
        } else if (visibly == View.GONE) {
            return "GONE";
        } else if (visibly == View.INVISIBLE) {
            return "INVISIBLE";
        }
        return "UnKnow";
    }


    public WindowManager getWindowManager(Activity context) {
        WindowManager systemService = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return systemService;
    }


}
