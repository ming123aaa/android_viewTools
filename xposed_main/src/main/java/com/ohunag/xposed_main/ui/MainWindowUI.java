package com.ohunag.xposed_main.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ohunag.xposed_main.R;
import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.view.DrawRectView;
import com.ohunag.xposed_main.view.HookRootFrameLayout;
import com.ohunag.xposed_main.view.MyListView;
import com.ohunag.xposed_main.viewTree.ViewNode;
import com.ohunag.xposed_main.viewTree.ViewTreeUtil;

import java.util.ArrayList;
import java.util.List;

public class MainWindowUI {
    private ViewGroup rootView;
    private Activity activity;
    private WindowManager.LayoutParams layoutParams;
    private boolean isShow = false;
    private boolean isInit = false;
    private ViewGroup ll_activity;
    private TextView tv_packageName;
    private TextView tv_activityName;
    private TextView tv_viewClick;
    private TextView tv_viewClick_all;
    private TextView tv_close;
    private TextView tv_getRootView;


    private ViewGroup fl_touch;

    private ViewGroup ll_clickView;
    private TextView tv_process_clickView;
    private TextView tv_openInLayout_clickView;
    private TextView tv_lastNode_clickView;
    private TextView tv_nextNode_clickView;
    private TextView tv_viewName_clickView;
    private TextView tv_close_clickView;
    private TextView tv_show_clickView;
    private DrawRectView drv_main_view;

    private ViewGroup ll_getRoot;
    private TextView tv_parentList_viewTree;
    private TextView tv_parentNode_viewTree;
    private TextView tv_childNode_viewTree;
    private ListView listView_viewTree;
    private TextView tv_close_viewTree;
    private TextView tv_show_viewTree;


    private ViewGroup ll_viewMsg;
    private MyListView listView_viewMsg;
    private TextView tv_close_viewMsg;
    private TextView tv_edit_viewMsg;
    private ViewMsgEditDialog viewMsgEditDialog;


    private ViewNode rootViewNode;
    private List<ViewNode> nodes = new ArrayList<>();
    private boolean hideViewIsShow=false;//隐藏的View是否展示

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

        ll_activity.setVisibility(View.GONE);
        ll_clickView.setVisibility(View.GONE);
        fl_touch.setVisibility(View.GONE);
        drv_main_view.setVisibility(View.GONE);
        ll_viewMsg.setVisibility(View.GONE);
        ll_getRoot.setVisibility(View.GONE);
        if (sate == 0) {
            ll_activity.setVisibility(View.VISIBLE);
        } else if (sate == 1) {
            fl_touch.setVisibility(View.VISIBLE);
        } else if (sate == 2) {
            ll_clickView.setVisibility(View.VISIBLE);
            drv_main_view.setVisibility(View.VISIBLE);
        } else if (sate == 3) {
            ll_viewMsg.setVisibility(View.VISIBLE);
            drv_main_view.setVisibility(View.VISIBLE);
        } else if (sate == 4) {
            ll_getRoot.setVisibility(View.VISIBLE);
            drv_main_view.setVisibility(View.VISIBLE);
        }
    }

       private AlertDialog mainDialog;
    private void init() {

        rootView = new HookRootFrameLayout(activity);
//        mainDialog=new AlertDialog.Builder(activity)
//                .setCancelable(false)
//                .setView(rootView)
//                .create();
        layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        layoutParams.softInputMode =  WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        if (UiHook.xpRes == null) {
            return;
        }
        ViewGroup mainWindow = (ViewGroup) LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.ui_main_window), null, false);
        rootView.addView(mainWindow);
        fl_touch = mainWindow.findViewById(R.id.fl_touch);
        drv_main_view = new DrawRectView(activity);
        drv_main_view.setBackgroundColor(0x20000000);
        mainWindow.addView(drv_main_view, 0);
        fl_touch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
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
                return false;
            }
        });
        init_ll_activity();
        init_clickView(mainWindow);
        init_ll_viewMsg(mainWindow);
        init_ll_getRoot(mainWindow);
    }

    private void init_ll_getRoot(ViewGroup mainWindow) {
        View inflate = LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.ui_main_window_view_tree), null, false);
        ll_getRoot = mainWindow.findViewById(R.id.ll_getRoot);
        ll_getRoot.addView(inflate);
        tv_parentList_viewTree = inflate.findViewById(R.id.tv_parentList_viewTree);
        tv_parentNode_viewTree = inflate.findViewById(R.id.tv_parentNode_viewTree);
        tv_childNode_viewTree = inflate.findViewById(R.id.tv_childNode_viewTree);
        listView_viewTree = inflate.findViewById(R.id.listView_viewTree);
        tv_close_viewTree = inflate.findViewById(R.id.tv_close_viewTree);
        tv_show_viewTree = inflate.findViewById(R.id.tv_show_viewTree);
        tv_close_viewTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSate(0);
            }
        });

    }

    private void init_ll_viewMsg(ViewGroup mainWindow) {
        View inflate = LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.ui_main_window_view_msg), null, false);
        ll_viewMsg = mainWindow.findViewById(R.id.ll_viewMsg);
        ll_viewMsg.addView(inflate);
        ViewGroup srv_viewMsg = ll_viewMsg.findViewById(R.id.scv_viewMsg);
        listView_viewMsg = new MyListView(activity);
        srv_viewMsg.addView(listView_viewMsg, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv_close_viewMsg = ll_viewMsg.findViewById(R.id.tv_close_viewMsg);
        tv_edit_viewMsg = ll_viewMsg.findViewById(R.id.tv_edit_viewMsg);
        tv_close_viewMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }


    private void set_ll_viewMsg(ViewNode viewNode, View.OnClickListener listener) {

        listView_viewMsg.setAdapter(new ViewMsgAdapter(viewNode));
        tv_edit_viewMsg.setVisibility(View.VISIBLE);
        tv_edit_viewMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_ll_viewMsg_edit(viewNode, listener);
            }
        });
        tv_close_viewMsg.setOnClickListener(listener);
    }

    private void set_ll_viewMsg_edit(ViewNode viewNode, View.OnClickListener listener) {
        if (viewMsgEditDialog==null){
            viewMsgEditDialog=new ViewMsgEditDialog(activity);
        }
        viewMsgEditDialog.set_ll_viewMsg_edit(viewNode);
        viewMsgEditDialog.show();
    }

    private void findTouchView(float x, float y) {
        if (rootViewNode != null) {
            nodes.clear();
            ViewNode.ForeachCallBack foreachCallBack = viewNode -> {
                if (viewNode.getView() != null) {
                    int dt = 5;//防抖
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
            }else {
                rootViewNode.afterTraversal(foreachCallBack);
            }
        }
        if (nodes.size() == 0) {
            Toast.makeText(activity, "点击的位置没有找到View", Toast.LENGTH_LONG).show();
            setSate(0);
        } else {
            set_ll_clickView(0);
            setSate(2);
        }
    }


    private void init_ll_activity() {
        ll_activity = rootView.findViewById(R.id.ll_activity);
        tv_packageName = rootView.findViewById(R.id.tv_packageName);
        tv_activityName = rootView.findViewById(R.id.tv_activityName);
        tv_viewClick = rootView.findViewById(R.id.tv_viewClick);
        tv_viewClick_all = rootView.findViewById(R.id.tv_viewClick_all);
        tv_close = rootView.findViewById(R.id.tv_close);
        tv_getRootView = rootView.findViewById(R.id.tv_getRootView);
        tv_activityName.setText(activity.getClass().getName());
        tv_packageName.setText("包名:" + activity.getPackageName());
        tv_viewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootViewNode = ViewTreeUtil.getViewNode(activity);
                hideViewIsShow=false;
                setSate(1);
            }
        });
        tv_viewClick_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootViewNode = ViewTreeUtil.getViewNode(activity);
                hideViewIsShow=true;
                setSate(1);
            }
        });
        tv_getRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootViewNode = ViewTreeUtil.getViewNode(activity);
                set_ll_goRoot(rootViewNode, 0, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSate(0);
                    }
                });
                setSate(4);
            }
        });
        tv_close.setOnClickListener(v -> {
            hide();
        });
    }

    private void init_clickView(ViewGroup mainWindow) {
        View inflate = LayoutInflater.from(activity).inflate(UiHook.xpRes.getLayout(R.layout.ui_main_window_click_view), null, false);

        ll_clickView = mainWindow.findViewById(R.id.ll_clickView);
        ll_clickView.addView(inflate);
        tv_process_clickView = mainWindow.findViewById(R.id.tv_process_clickView);
        tv_openInLayout_clickView = mainWindow.findViewById(R.id.tv_openInLayout_clickView);
        tv_lastNode_clickView = mainWindow.findViewById(R.id.tv_lastNode_clickView);
        tv_nextNode_clickView = mainWindow.findViewById(R.id.tv_nextNode_clickView);
        tv_viewName_clickView = mainWindow.findViewById(R.id.tv_viewName_clickView);
        tv_close_clickView = mainWindow.findViewById(R.id.tv_close_clickView);
        tv_show_clickView = mainWindow.findViewById(R.id.tv_show_clickView);
        tv_close_clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSate(0);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void set_ll_clickView(int position) {
        if (position >= nodes.size() || position < 0) {
            return;
        }
        View view = nodes.get(position).getView();
        tv_process_clickView.setText("进度" + position + "/" + nodes.size() + "  "
                + ViewTreeUtil.getViewType(view) + " Visibility:" + getVisibility(view.getVisibility())
                + " Alpha:" + view.getAlpha());
        tv_viewName_clickView.setText(nodes.get(position).getViewClassName());
        tv_openInLayout_clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nodes.get(position) != null) {
                    setSate(4);
                    set_ll_goRoot(nodes.get(position), 0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setSate(2);
                            set_ll_clickView(position);
                        }
                    });
                }
            }
        });
        drv_main_view.clear();
        showViewRect(view);
        tv_lastNode_clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = position - 1;
                if (p < 0) {
                    p = nodes.size() - 1;
                }
                set_ll_clickView(p);
            }
        });
        tv_nextNode_clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = position + 1;
                if (p >= nodes.size()) {
                    p = 0;
                }
                set_ll_clickView(p);
            }
        });
        tv_show_clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSate(3);
                set_ll_viewMsg(nodes.get(position), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        set_ll_clickView(position);
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
        listView_viewTree.setAdapter(viewTreeAdapter);
        showViewRect(viewNode.getView());
        tv_parentList_viewTree.setText(viewNode.getViewNodePath());
        viewTreeAdapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewNode selectNode = viewTreeAdapter.getSelectNode();
                if (selectNode != null) {
                    showViewRect(selectNode.getView());
                }
            }
        });
        tv_parentNode_viewTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewNode.getParent() != null) {
                    set_ll_goRoot(viewNode.getParent(), viewNode.inParentIndex() + 1, listener);
                    setSate(4);
                }
            }
        });
        tv_childNode_viewTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewTreeAdapter.getSelectNode() == null || viewTreeAdapter.getSelectNode() == viewNode) {
                    return;
                }
                set_ll_goRoot(viewTreeAdapter.getSelectNode(), 0, listener);
            }
        });
        tv_show_viewTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSate(3);
                set_ll_viewMsg(viewTreeAdapter.getSelectNode(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        set_ll_goRoot(viewNode, viewTreeAdapter.getSelectId(), listener);
                        setSate(4);
                    }
                });
            }
        });
        tv_close_viewTree.setOnClickListener(listener);

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


    public IBinder getDialogToken() {
        EditText editText = new EditText(activity);
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setTitle("asaa")
                .setView(editText)
                .create();
        return alertDialog.getWindow().getDecorView().getWindowToken();
    }

    public WindowManager getWindowManager(Activity context) {
        WindowManager systemService = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return systemService;
    }


}
