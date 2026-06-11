package com.ohunag.xposed_main.viewTree.intercept;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.ohunag.xposed_main.UiHook;
import com.ohunag.xposed_main.util.RefInvoke;
import com.ohunag.xposed_main.util.UiUtil;
import com.ohunag.xposed_main.viewTree.NodeValue;
import com.ohunag.xposed_main.viewTree.ViewNode;
import com.ohunag.xposed_main.viewTree.ViewTreeUtil;

import java.util.Arrays;
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

        map.put("viewType", NodeValue.createNodeBold(ViewTreeUtil.getViewType(view)));
        map.put("Alpha", NodeValue.createNodeBold(view.getAlpha()));
        map.put("visibility", NodeValue.createNodeBold(getVisibility(view.getVisibility())));
        map.put("width-height", NodeValue.createNodeHighlight(view.getWidth() + "," + view.getHeight()));

        if (getOnClickListener(view) != null) {
            map.put("OnClickListener", NodeValue.createNodeHighlight(getOnClickListener(view)));
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
            map.put("adapter", NodeValue.createNodeHighlight(getAdapter(view).getClass().getName()));
        }

        if (view.getLayoutParams() != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            map.put("layoutParams-width",
                    NodeValue.createNodeHighlight(
                            getLayoutParamsString(layoutParams.width)));
            map.put("layoutParams-height",
                    NodeValue.createNodeHighlight(
                            getLayoutParamsString(layoutParams.height)));

            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                map.put("layoutMargin", NodeValue.createNodeBold(
                        "top=" + marginLayoutParams.topMargin +
                                ",\nleft=" + marginLayoutParams.leftMargin +
                                ",\nright=" + marginLayoutParams.rightMargin +
                                ",\nbottom=" + marginLayoutParams.bottomMargin));
            }
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
        map.put("locationInParent", NodeValue.createNode(getLocationInParent(view)));
        if (view.getContentDescription() != null) {
            map.put("ContentDescription", NodeValue.createNode(view.getContentDescription().toString()));
        }

        map.put("density", NodeValue.createNode(getDensity(view)));

        if (view.getTranslationX() != 0F) {
            map.put("TranslationX", NodeValue.createNode(view.getTranslationX()));
        }
        if (view.getTranslationY() != 0F) {
            map.put("TranslationY", NodeValue.createNode(view.getTranslationY()));
        }
        if (view.getScrollX() != 0) {
            map.put("ScrollX", NodeValue.createNode(view.getScrollX()));
        }
        if (view.getScrollY() != 0) {
            map.put("ScrollY", NodeValue.createNode(view.getScrollY()));
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

                backgroundInfo(map, background);
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

    }

    private void backgroundInfo(Map<String, NodeValue> map, Drawable mBackground) {
        Drawable background=mBackground;
        int num=0;
        while (background.getCurrent()!=null&&background!=background.getCurrent()&&num<10){
            num++;
            background=background.getCurrent();
        }

        if (background instanceof GradientDrawable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String colorStr = ""; //颜色值
                int[] colors = ((GradientDrawable) background).getColors();
                ColorStateList color = ((GradientDrawable) background).getColor();
                if (colors != null && colors.length > 0) {
                    String[] arr = new String[colors.length];
                    for (int i = 0; i < colors.length; i++) {
                        arr[i] = colorIntToString(colors[i]);
                    }
                    colorStr = Arrays.toString(arr);
                } else if (color != null) {
                    colorStr = colorIntToString(color.getDefaultColor());
                }

                String radiusStr = getRadiusStr((GradientDrawable) background);
                if (!TextUtils.isEmpty(colorStr)) {
                    map.put("backgroundColor", NodeValue.createNodeBold(colorStr));
                }
                if (!TextUtils.isEmpty(radiusStr)) {
                    map.put("backgroundRadius", NodeValue.createNodeBold(radiusStr));
                }

            }
        } else if (background instanceof ColorDrawable) {
            int color = ((ColorDrawable) background).getColor();
            map.put("backgroundColor", NodeValue.createNodeBold(colorIntToString(color)));
        }
    }

    @NonNull
    private static String getRadiusStr(GradientDrawable background) {
        String radiusStr = "";
        float[] radii = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                radii = background.getCornerRadii();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (radii != null && radii.length == 8) {
            // radii 数组包含 8 个值：每个角有 [X半径, Y半径]
            // 顺序：左上、右上、右下、左下

            float topLeftX = radii[0];     // 左上角 X 轴半径
            float topLeftY = radii[1];     // 左上角 Y 轴半径
            if (topLeftX == topLeftY) {
                radiusStr += ("topLeft=" + topLeftX);
            } else {
                radiusStr += ("topLeftXY=" + topLeftX + "," + topLeftY);
            }


            float topRightX = radii[2];    // 右上角 X 轴半径
            float topRightY = radii[3];    // 右上角 Y 轴半径
            if (topRightX == topRightY) {
                radiusStr += ("\ntopRight=" + topRightX);
            } else {
                radiusStr += ("\ntopRightXY=" + topRightX + "," + topRightY);
            }


            float bottomRightX = radii[4]; // 右下角 X 轴半径
            float bottomRightY = radii[5]; // 右下角 Y 轴半径
            if (bottomRightX == bottomRightY) {
                radiusStr += ("\nbottomRight=" + bottomRightX);
            } else {
                radiusStr += ("\nbottomRightXY=" + bottomRightX + "," + bottomRightY);
            }


            float bottomLeftX = radii[6];  // 左下角 X 轴半径
            float bottomLeftY = radii[7];  // 左下角 Y 轴半径
            if (bottomLeftX == bottomLeftY) {
                radiusStr += ("\nbottomLeft=" + bottomLeftX);
            } else {
                radiusStr += ("\nbottomLeftXY=" + bottomLeftX + "," + bottomLeftY);
            }

        } else {
            float radius = 0f;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                radius = background.getCornerRadius();
            }
            if (radius != 0f) {
                radiusStr = radius + "";
            }
        }
        return radiusStr;
    }


    public String colorIntToString(int colorInt) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#");
        stringBuilder.append(Integer.toHexString(colorInt));
        return stringBuilder.toString();
    }

    public String getIdString(int id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("0x");
        stringBuilder.append(Integer.toHexString(id));
        return stringBuilder.toString();
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
