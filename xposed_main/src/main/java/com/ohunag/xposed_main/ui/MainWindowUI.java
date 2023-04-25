package com.ohunag.xposed_main.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
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
    private TextView tv_viewClick_xposed_all;
    private TextView tv_close_xposed;
    private TextView tv_getRootView_xposed;

    private WeakReference<View> selectView;//选中的View


    private ViewGroup fl_touch_xposed;

    private ViewGroup ll_clickView_xposed;
    private TextView tv_process_clickView_xposed;
    private TextView tv_openInLayout_clickView_xposed;
    private TextView tv_lastNode_clickView_xposed;
    private TextView tv_nextNode_clickView_xposed;
    private TextView tv_viewName_clickView_xposed;
    private TextView tv_close_xposed_clickView;
    private TextView tv_show_clickView_xposed;
    private DrawRectView drv_main_view;

    private ViewGroup ll_getRoot_xposed;
    private TextView tv_parentList_viewTree_xposed;
    private TextView tv_parentNode_viewTree_xposed;
    private TextView tv_childNode_viewTree_xposed;
    private ListView listView_viewTree_xposed;
    private TextView tv_close_xposed_viewTree_xposed;
    private TextView tv_show_viewTree_xposed;


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
    private boolean hideViewIsShow = false;//隐藏的View是否展示

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
        } else if (sate == 4) {// viewNode布局界面
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
            selectViewRoot(null);
            setSate(0);
        });
        tv_close_xposed_selectView.setOnClickListener(v -> setSate(0));
    }

    private void init_ll_getRoot_xposed(ViewGroup mainWindow) {
        View inflate = LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.ui_main_window_view_tree_xposed), null, false);
        ll_getRoot_xposed = mainWindow.findViewWithTag("ll_getRoot_xposed");
        ll_getRoot_xposed.addView(inflate);
        tv_parentList_viewTree_xposed = inflate.findViewWithTag("tv_parentList_viewTree_xposed");
        tv_parentNode_viewTree_xposed = inflate.findViewWithTag("tv_parentNode_viewTree_xposed");
        tv_childNode_viewTree_xposed = inflate.findViewWithTag("tv_childNode_viewTree_xposed");
        listView_viewTree_xposed = inflate.findViewWithTag("listView_viewTree_xposed");
        tv_close_xposed_viewTree_xposed = inflate.findViewWithTag("tv_close_xposed_viewTree_xposed");
        tv_show_viewTree_xposed = inflate.findViewWithTag("tv_show_viewTree_xposed");
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
        List<ViewRootMsg> viewRootMsgs=new ArrayList<>();
        ViewTreeUtil.getFragmentViewRootMsg(activity,viewRootMsgs);
        List<View> rootViews = UiHook.getRootViews();
        for (View view : rootViews) {
            if (view!=null) {
                viewRootMsgs.add(new ViewRootMsg(view.toString(), view));
            }
        }
        ViewRootListAdapter viewRootListAdapter = new ViewRootListAdapter(viewRootMsgs);
        listView_selectView_xposed.setAdapter(viewRootListAdapter);
        viewRootListAdapter.setListener(new ViewRootListAdapter.Listener() {
            @Override
            public void onShow(WeakReference<View> weakReference) {
                if (weakReference.get() != null) {
                    setSate(6);
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
            public void onSelect(WeakReference<View> weakReference) {
                if (weakReference.get() != null) {
                    selectViewRoot(weakReference.get());
                    setSate(0);
                } else {
                    ToastUtil.show(activity, "当前ViewRoot不存在");
                    set_ll_selectView_xposed();
                }

            }
        });

    }

    private void set_ll_viewMsg_xposed(ViewNode viewNode, View.OnClickListener listener) {

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
                setSate(6);
                set_ll_imgView_xposed(viewNode.getView(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSate(3);
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

    private void set_ll_imgView_xposed(View view, View.OnClickListener listener) {
        iv_show.setImageDrawable(null);
        if (view!= null) {
            Bitmap bitmap = UiUtil.viewToBitmap(view);
            if (bitmap != null) {
                iv_show.setImageBitmap(bitmap);
            }
        }
        tv_close_xposed_imgView.setOnClickListener(listener);
    }

    private void findTouchView(float x, float y) {
        if (rootViewNode != null) {
            nodes.clear();
            ViewNode.ForeachCallBack foreachCallBack = viewNode -> {
                if (viewNode.getView() != null) {
                    int dt = 10;//防止点不准 设置误差距离
                    View view = viewNode.getView();
                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    if (x > location[0] - dt && y > location[1] - dt) {
                        if (x < location[0] + view.getWidth() + dt && y < location[1] + view.getHeight() + dt) {
                            nodes.add(viewNode);
                        }
                    }
                }
                return false;
            };
            if (!hideViewIsShow) {
                rootViewNode.afterTraversalVisibleView(foreachCallBack);
            } else {
                rootViewNode.afterTraversal(foreachCallBack);
            }
        }
        if (nodes.size() == 0) {
            Toast.makeText(activity, "点击的位置没有找到View", Toast.LENGTH_LONG).show();
            setSate(0);
        } else {
            set_ll_clickView_xposed(0);
            setSate(2);
        }
    }


    private void init_ll_activity_xposed() {
        ll_activity_xposed = rootView.findViewWithTag("ll_activity_xposed");
        tv_packageName_xposed = rootView.findViewWithTag("tv_packageName_xposed");
        tv_activityName_xposed = rootView.findViewWithTag("tv_activityName_xposed");
        tv_selectView_xposed = rootView.findViewWithTag("tv_selectView_xposed");
        tv_viewClick_xposed = rootView.findViewWithTag("tv_viewClick_xposed");
        tv_viewClick_xposed_all = rootView.findViewWithTag("tv_viewClick_xposed_all");
        tv_close_xposed = rootView.findViewWithTag("tv_close_xposed");
        tv_getRootView_xposed = rootView.findViewWithTag("tv_getRootView_xposed");
        tv_activityName_xposed.setText(activity.getClass().getName());
        tv_packageName_xposed.setText("包名:" + activity.getPackageName());
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
                hideViewIsShow = false;
                setSate(1);
            }
        });

        tv_viewClick_xposed_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshRootViewNode();
                hideViewIsShow = true;
                setSate(1);
            }
        });
        tv_getRootView_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshRootViewNode();
                set_ll_goRoot(rootViewNode, 0, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSate(0);
                    }
                });
                setSate(4);
            }
        });
        tv_close_xposed.setOnClickListener(v -> {
            hide();
        });
    }

    private void refreshRootViewNode() {
        if (selectView == null) {
            rootViewNode = ViewTreeUtil.getViewNode(activity);
        } else {
            if (selectView.get() != null) {
                rootViewNode = ViewTreeUtil.getViewNode(selectView.get(),null);
            } else {
                ToastUtil.show(activity, "当前ViewRoot不存在,自动选择activity");
                selectViewRoot(null);
                rootViewNode = ViewTreeUtil.getViewNode(activity);
            }
        }
    }

    private void selectViewRoot(View view) {
        if (view == null) {
            tv_selectView_xposed.setText("选择根View:当前activity");
            selectView = null;
        } else {
            tv_selectView_xposed.setText("选择根View:" + view.toString());
        }
        selectView = new WeakReference<>(view);
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
        tv_close_xposed_clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSate(0);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void set_ll_clickView_xposed(int position) {
        if (position >= nodes.size() || position < 0) {
            return;
        }
        View view = nodes.get(position).getView();
        tv_process_clickView_xposed.setText("进度" + position + "/" + nodes.size() + "  "
                + ViewTreeUtil.getViewType(view) + " Visibility:" + getVisibility(view.getVisibility())
                + " Alpha:" + view.getAlpha());
        tv_viewName_clickView_xposed.setText(nodes.get(position).getViewClassName());
        tv_openInLayout_clickView_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nodes.get(position) != null) {
                    setSate(4);
                    set_ll_goRoot(nodes.get(position), 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setSate(2);
                            set_ll_clickView_xposed(position);
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
                set_ll_clickView_xposed(p);
            }
        });
        tv_nextNode_clickView_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = position + 1;
                if (p >= nodes.size()) {
                    p = 0;
                }
                set_ll_clickView_xposed(p);
            }
        });
        tv_show_clickView_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSate(3);
                set_ll_viewMsg_xposed(nodes.get(position), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        set_ll_clickView_xposed(position);
                        setSate(2);
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

    private void set_ll_goRoot(ViewNode viewNode, int selectId, View.OnClickListener listener) {
        ViewTreeAdapter viewTreeAdapter = new ViewTreeAdapter(viewNode, selectId);
        listView_viewTree_xposed.setAdapter(viewTreeAdapter);
        showViewRect(viewNode.getView());
        tv_parentList_viewTree_xposed.setText(viewNode.getViewNodePath());
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
                    set_ll_goRoot(viewNode.getParent(), viewNode.inParentIndex() + 1, listener);
                    setSate(4);
                }
            }
        });
        tv_childNode_viewTree_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewTreeAdapter.getSelectNode() == null || viewTreeAdapter.getSelectNode() == viewNode) {
                    return;
                }
                set_ll_goRoot(viewTreeAdapter.getSelectNode(), 0, listener);
            }
        });
        tv_show_viewTree_xposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSate(3);
                set_ll_viewMsg_xposed(viewTreeAdapter.getSelectNode(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        set_ll_goRoot(viewNode, viewTreeAdapter.getSelectId(), listener);
                        setSate(4);
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
