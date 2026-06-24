package com.ohunag.xposed_main.viewTree.intercept;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.util.RefInvoke;
import com.ohunag.xposed_main.util.UiUtil;
import com.ohunag.xposed_main.viewTree.NodeValue;
import com.ohunag.xposed_main.viewTree.ViewNode;
import com.ohunag.xposed_main.viewTree.ViewTreeUtil;

import java.util.Map;

public class ViewNodeValueIntercept implements ViewNode.NodeValueIntercept {
    @Override
    public boolean onIntercept(Map<String, NodeValue> map, View view, ViewNode viewNode) {
        if (view == null) {
            return false;
        }

        map.put("context", NodeValue.createNodeHighlight(view.getContext().getClass().getName()));
        if (view.getId() != 0) {
            map.put("id", NodeValue.createNodeHighlight(UiUtil.getViewId(view)));
        } else {
            map.put("id", NodeValue.createNodeHighlight("no id"));
        }

        map.put("ViewType", NodeValue.createNodeBold(ViewTreeUtil.getViewType(view)));
        map.put("Alpha", NodeValue.createNodeBold(view.getAlpha()));
        map.put("visibility", NodeValue.createNodeBold(getVisibility(view.getVisibility())));
        map.put("width-height", NodeValue.createNodeHighlight(view.getWidth() + "," + view.getHeight()));
        if (view.getLayoutParams() != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            map.put("layoutParams-width,height",
                    NodeValue.createNodeHighlight(
                            getLayoutParamsString(layoutParams.width)+",\n"+getLayoutParamsString(layoutParams.height)));

            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                map.put("layoutMargin", NodeValue.createNodeBold(
                        "top=" + marginLayoutParams.topMargin +
                                ",\nleft=" + marginLayoutParams.leftMargin +
                                ",\nright=" + marginLayoutParams.rightMargin +
                                ",\nbottom=" + marginLayoutParams.bottomMargin));
            }
        }


        if (getOnClickListener(view) != null) {
            map.put("OnClickListener", NodeValue.createNodeHighlight(getOnClickListener(view)));
        }
        if (getOnTouchListener(view) != null) {
            map.put("OnTouchListener", NodeValue.createNodeHighlight(getOnTouchListener(view)));
        }
        if (getOnLongClickListener(view) != null) {
            map.put("OnLongClickListener", NodeValue.createNodeHighlight(getOnLongClickListener(view)));
        }
        if (getOnKeyListener(view) != null) {
            map.put("OnKeyListener", NodeValue.createNodeHighlight(getOnKeyListener(view)));
        }
        if (getOnContextClickListener(view) != null) {
            map.put("OnContextClickListener", NodeValue.createNodeHighlight(getOnContextClickListener(view)));
        }
        if (getOnCreateContextMenuListener(view) != null) {
            map.put("OnCreateContextMenuListener", NodeValue.createNodeHighlight(getOnCreateContextMenuListener(view)));
        }
        if (getAdapter(view) != null) {
            map.put("adapter", NodeValue.createNodeHighlight(getAdapter(view).getClass().getName()));
        }



        map.put("padding", NodeValue.createNodeBold(
                "top=" + view.getPaddingTop() +
                        ",\nleft=" + view.getPaddingLeft() +
                        ",\nright=" + view.getPaddingRight() +
                        ",\nbottom=" + view.getPaddingBottom()));

        try {
            background(map, view);
        } catch (Exception e) {
            e.printStackTrace();
        }


        map.put("locationOnScreen", NodeValue.createNode(getLocationInScreen(view)));
        map.put("locationInWindow", NodeValue.createNode(getLocationInWindow(view)));
        map.put("locationInParent", NodeValue.createNode(getLocationInParent(view)));
        if (view.getContentDescription() != null) {
            map.put("ContentDescription", NodeValue.createNode(view.getContentDescription().toString()));
        }

        map.put("density", NodeValue.createNode(getDensity(view)));

        if (view.getTranslationX() != 0F||view.getTranslationY() != 0F) {
            map.put("translation-x,y", NodeValue.createNode(view.getTranslationX()+","+view.getTranslationY()));
        }

        if (view.getScrollX() != 0||view.getScrollY() != 0) {
            map.put("scroll-x,y", NodeValue.createNode(view.getScrollX()+","+view.getScrollY()));
        }

        if (view.getScaleX()!=1f||view.getScaleY()!=1f){
            map.put("scale-x,y", NodeValue.createNode(view.getScaleX()+","+view.getScaleY()));
        }

        map.put("isClickable", NodeValue.createNode(view.isClickable()));
        map.put("isEnabled", NodeValue.createNode(view.isEnabled()));
        map.put("isFocusable", NodeValue.createNode(view.isFocusable()));
        map.put("isFocused", NodeValue.createNode(view.isFocused()));
        return false;
    }

    private void background(Map<String, NodeValue> map, View view) {
        Drawable background = view.getBackground();


        if (background != null) {
            try {
                DrawableInfoUtil.drawableInfo(map, background, "background", NodeValue.Type.bold);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                int id = (int) RefInvoke.getFieldOjbect(UiHook.classLoader, View.class.getName(), view, "mBackgroundResource");
                if (id != 0) {
                    Resources res = view.getResources();
                    String fullName = UiUtil.getResIdName(res, id);
                    map.put("backgroundResource", NodeValue.createNodeBold(fullName));
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Drawable foreground = view.getForeground();

            if (foreground != null) {
                try {
                    DrawableInfoUtil.drawableInfo(map, foreground, "foreground", NodeValue.Type.bold);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private String getDensity(View view) {
        return "" + view.getResources().getDisplayMetrics().density;
    }

    private String getLayoutParamsString(int v) {
        if (v == ViewGroup.LayoutParams.MATCH_PARENT) {
            return "match_parent";
        } else if (v == ViewGroup.LayoutParams.WRAP_CONTENT) {
            return "wrap_content";
        }
        return "" + v;
    }

    private Object getAdapter(View view) {
        try {
            return view.getClass().getMethod("getAdapter").invoke(view);
        } catch (Throwable e) {

        }
        return null;
    }

    public String getLocationInScreen(View view) {
        int[] ints = new int[2];
        view.getLocationOnScreen(ints);
        return "x:" + ints[0] + " y:" + ints[1];
    }

    public String getLocationInWindow(View view) {
        int[] ints = new int[2];
        view.getLocationInWindow(ints);
        return "x:" + ints[0] + " y:" + ints[1];
    }

    public String getLocationInParent(View view) {
        return "x:" + view.getX() + " y:" + view.getY();
    }

    public String getOnTouchListener(View view) {
        Object mListenerInfo = RefInvoke.getFieldOjbect(View.class.getName(), view, "mListenerInfo");
        if (mListenerInfo != null) {
            Object onTouchListener = RefInvoke.getFieldOjbect(mListenerInfo.getClass().getName(), mListenerInfo, "mOnTouchListener");
            if (onTouchListener != null) {
                return onTouchListener.getClass().getName();
            }
        }

        return null;
    }

    public String getOnClickListener(View view) {
        Object mListenerInfo = RefInvoke.getFieldOjbect(View.class.getName(), view, "mListenerInfo");
        if (mListenerInfo != null) {
            Object onTouchListener = RefInvoke.getFieldOjbect(mListenerInfo.getClass().getName(), mListenerInfo, "mOnClickListener");
            if (onTouchListener != null) {
                return onTouchListener.getClass().getName();
            }
        }

        return null;
    }

    public String getOnLongClickListener(View view) {
        Object mListenerInfo = RefInvoke.getFieldOjbect(View.class.getName(), view, "mListenerInfo");
        if (mListenerInfo != null) {
            Object onTouchListener = RefInvoke.getFieldOjbect(mListenerInfo.getClass().getName(), mListenerInfo, "mOnLongClickListener");
            if (onTouchListener != null) {
                return onTouchListener.getClass().getName();
            }
        }

        return null;
    }

    public String getOnKeyListener(View view) {
        Object mListenerInfo = RefInvoke.getFieldOjbect(View.class.getName(), view, "mListenerInfo");
        if (mListenerInfo != null) {
            Object onTouchListener = RefInvoke.getFieldOjbect(mListenerInfo.getClass().getName(), mListenerInfo, "mOnKeyListener");
            if (onTouchListener != null) {
                return onTouchListener.getClass().getName();
            }
        }

        return null;
    }

    public String getOnContextClickListener(View view) {
        Object mListenerInfo = RefInvoke.getFieldOjbect(View.class.getName(), view, "mListenerInfo");
        if (mListenerInfo != null) {
            Object onTouchListener = RefInvoke.getFieldOjbect(mListenerInfo.getClass().getName(), mListenerInfo, "mOnContextClickListener");
            if (onTouchListener != null) {
                return onTouchListener.getClass().getName();
            }
        }

        return null;
    }

    public String getOnCreateContextMenuListener(View view) {
        Object mListenerInfo = RefInvoke.getFieldOjbect(View.class.getName(), view, "mListenerInfo");
        if (mListenerInfo != null) {
            Object onTouchListener = RefInvoke.getFieldOjbect(mListenerInfo.getClass().getName(), mListenerInfo, "mOnCreateContextMenuListener");
            if (onTouchListener != null) {
                return onTouchListener.getClass().getName();
            }
        }

        return null;
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

    public static String getViewIdName(View view) {
        StringBuilder out = new StringBuilder();
        final int id = view.getId();
        if (id != 0) {
            final Resources r = view.getResources();
            if (id > 0 && r != null) {
                try {
                    String pkgname;
                    switch (id & 0xff000000) {
                        case 0x7f000000:
                            pkgname = "app";
                            break;
                        case 0x01000000:
                            pkgname = "android";
                            break;
                        default:
                            pkgname = r.getResourcePackageName(id);
                            break;
                    }
                    String typename = r.getResourceTypeName(id);
                    String entryname = r.getResourceEntryName(id);
                    out.append(pkgname);
                    out.append(":");
                    out.append(typename);
                    out.append("/");
                    out.append(entryname);
                } catch (Resources.NotFoundException e) {
                }
            }
        }
        return out.toString();
    }


}
