package com.ohunag.xposed_main.viewTree.intercept;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import com.ohunag.xposed_main.util.RefInvoke;
import com.ohunag.xposed_main.viewTree.NodeValue;
import com.ohunag.xposed_main.viewTree.ViewNode;
import com.ohunag.xposed_main.viewTree.ViewTreeUtil;

import java.util.Map;
import java.util.Objects;

public class ViewNodeValueIntercept implements ViewNode.NodeValueIntercept {
    @Override
    public boolean onIntercept(Map<String, NodeValue> map, View view, ViewNode viewNode) {
        if (view == null) {
            return false;
        }
        map.put("viewType", NodeValue.createNode(ViewTreeUtil.getViewType(view)));
        map.put("Alpha", NodeValue.createNode(view.getAlpha()));
        map.put("isClickable", NodeValue.createNode(view.isClickable()));
        map.put("isEnabled", NodeValue.createNode(view.isEnabled()));
        map.put("isFocusable", NodeValue.createNode(view.isFocusable()));
        map.put("isFocused", NodeValue.createNode(view.isFocused()));
        map.put("visibility", NodeValue.createNode(getVisibility(view.getVisibility())));
        map.put("id", NodeValue.createNode(view.getId()));
        map.put("width-height", NodeValue.createNode(view.getWidth() + "," + view.getHeight()));
        map.put("locationOnScreen", NodeValue.createNode(getLocationInScreen(view)));
        if (view.getContentDescription() != null) {
            map.put("ContentDescription", NodeValue.createNode(view.getContentDescription().toString()));
        }
        if (view.getId() != 0) {
            map.put("id-string", NodeValue.createNode(getStringId(view)));
        }
        if (getOnClickListener(view) != null) {
            map.put("OnClickListener", NodeValue.createNode(getOnClickListener(view)));
        }
        if (getOnTouchListener(view) != null) {
            map.put("OnTouchListener", NodeValue.createNode(getOnTouchListener(view)));
        }
        if (getOnLongClickListener(view) != null) {
            map.put("OnLongClickListener", NodeValue.createNode(getOnLongClickListener(view)));
        }
        if (getOnKeyListener(view) != null) {
            map.put("OnKeyListener", NodeValue.createNode(getOnKeyListener(view)));
        }
        if (getOnContextClickListener(view) != null) {
            map.put("OnContextClickListener", NodeValue.createNode(getOnContextClickListener(view)));
        }
        if (getOnCreateContextMenuListener(view) != null) {
            map.put("OnCreateContextMenuListener", NodeValue.createNode(getOnCreateContextMenuListener(view)));
        }
        if (getAdapter(view) != null) {
            map.put("adapter", NodeValue.createNode(getAdapter(view).getClass().getName()));
        }
        if (view.getLayoutParams() != null) {
            map.put("layoutParams-width", NodeValue.createNode(getLayoutParamsString(view.getLayoutParams().width)));
        }
        if (view.getLayoutParams() != null) {
            map.put("layoutParams-height", NodeValue.createNode(getLayoutParamsString(view.getLayoutParams().height)));
        }
        map.put("padding-top,left,right,bottom", NodeValue.createNode(view.getPaddingTop() + "," + view.getPaddingLeft() + "," + view.getPaddingRight() + "," + view.getPaddingBottom()));
        if (view.getTranslationX()!=0F) {
            map.put("TranslationX", NodeValue.createNode(view.getTranslationX()));
        }
        if (view.getTranslationY()!=0F) {
            map.put("TranslationY", NodeValue.createNode(view.getTranslationY()));
        }
        if(view.getScrollX()!=0){
            map.put("ScrollX", NodeValue.createNode(view.getScrollX()));
        }
        if(view.getScrollY()!=0){
            map.put("ScrollY", NodeValue.createNode(view.getScrollY()));
        }

        return false;
    }

    private String getLayoutParamsString(int v) {
        if (v == ViewGroup.LayoutParams.MATCH_PARENT) {
            return "MATCH_PARENT";
        } else if (v == ViewGroup.LayoutParams.WRAP_CONTENT) {
            return "WRAP_CONTENT";
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
        view.getLocationInWindow(ints);
        return "x:" + ints[0] + " y:" + ints[1];
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

    public static String getStringId(View view) {
        StringBuilder out = new StringBuilder();
        final int id = view.getId();
        if (id != 0) {
            out.append(" 0x");
            out.append(Integer.toHexString(id));
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
                    out.append(" ");
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
