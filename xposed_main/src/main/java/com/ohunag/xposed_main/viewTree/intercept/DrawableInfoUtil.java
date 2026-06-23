package com.ohunag.xposed_main.viewTree.intercept;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.ohunag.xposed_main.util.RefInvoke;
import com.ohunag.xposed_main.viewTree.NodeValue;

import java.util.Arrays;
import java.util.Map;

public class DrawableInfoUtil {

    public static void drawableInfo(Map<String, NodeValue> map, Drawable mBackground, String startName) {
        if (mBackground == null) {
            return;
        }
        Drawable background = mBackground;
        int num = 0;
        while (background.getCurrent() != null && background != background.getCurrent() && num < 10) {
            num++;
            background = background.getCurrent();
        }
        map.put(startName + "Drawable", NodeValue.createNodeBold(background.getClass().getName()));

        if (background instanceof GradientDrawable) {
            handleGradientDrawable(map, (GradientDrawable) background, startName);
        } else if (background instanceof RippleDrawable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            handleRippleDrawable(map, (RippleDrawable) background, startName);
        } else if (background instanceof ColorDrawable) {
            handleColorDrawable(map, (ColorDrawable) background, startName);
        } else if (background instanceof BitmapDrawable) {
            handleBitmapDrawable(map, (BitmapDrawable) background, startName);
        } else if (background instanceof ShapeDrawable) {
            handleShapeDrawable(map, (ShapeDrawable) background, startName);
        } else if (background instanceof InsetDrawable) {
            handleInsetDrawable(map, background, startName);
        } else if (background instanceof LayerDrawable) {
            handleLayerDrawable(map, (LayerDrawable) background, startName);
        } else if (background instanceof VectorDrawable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            handleVectorDrawable(map, background, startName);
        } else if (background instanceof StateListDrawable) {
            handleStateListDrawable(map, (StateListDrawable) background, startName);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && background instanceof AdaptiveIconDrawable ) {
            handleAdaptiveIconDrawable(map, (AdaptiveIconDrawable) background, startName);
        }
    }

    private static void handleGradientDrawable(Map<String, NodeValue> map, GradientDrawable drawable, String startName) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return;
        }
        // 颜色值
        String colorStr = "";
        int[] colors = drawable.getColors();
        ColorStateList color = drawable.getColor();

        if (colors != null && colors.length > 0) {
            String[] arr = new String[colors.length];
            for (int i = 0; i < colors.length; i++) {
                arr[i] = colorIntToString(colors[i]);
            }
            colorStr = Arrays.toString(arr);
        } else if (color != null) {
            colorStr = colorIntToString(color.getDefaultColor());
        }

        // 圆角
        String radiusStr = getRadiusStr(drawable);
        if (!TextUtils.isEmpty(colorStr)) {
            map.put(startName + "Color", NodeValue.createNodeBold(colorStr));
        }
        if (!TextUtils.isEmpty(radiusStr)) {
            map.put(startName + "Radius", NodeValue.createNodeBold(radiusStr));
        }

        // 描边 (mGradientState.mStrokeWidth / mStrokeColors)
        try {
            Object gradientState = RefInvoke.getFieldOjbect(
                    "android.graphics.drawable.GradientDrawable", drawable, "mGradientState");
            if (gradientState != null) {
                String stateClassName = gradientState.getClass().getName();
                int strokeWidth = (int) RefInvoke.getFieldOjbect(stateClassName, gradientState, "mStrokeWidth");
                ColorStateList strokeColor = (ColorStateList) RefInvoke.getFieldOjbect(stateClassName, gradientState, "mStrokeColors");
                if (strokeWidth > 0) {
                    map.put(startName + "StrokeWidth", NodeValue.createNodeBold(String.valueOf(strokeWidth)));
                }
                if (strokeColor != null) {
                    map.put(startName + "StrokeColor", NodeValue.createNodeBold(colorIntToString(strokeColor.getDefaultColor())));
                }
            }
        } catch (Throwable e) {
            // ignore
        }

        // 渐变类型
        try {
            int gradientType = drawable.getGradientType();
            String typeStr;
            switch (gradientType) {
                case GradientDrawable.LINEAR_GRADIENT:
                    typeStr = "LINEAR";
                    break;
                case GradientDrawable.RADIAL_GRADIENT:
                    typeStr = "RADIAL";
                    break;
                case GradientDrawable.SWEEP_GRADIENT:
                    typeStr = "SWEEP";
                    break;
                default:
                    typeStr = "NONE";
                    break;
            }
            map.put(startName + "GradientType", NodeValue.createNodeBold(typeStr));

            // 渐变中心点
            float centerX = drawable.getGradientCenterX();
            float centerY = drawable.getGradientCenterY();
            if (centerX != 0.5f || centerY != 0.5f) {
                map.put(startName + "GradientCenter", NodeValue.createNodeBold(centerX + "," + centerY));
            }

            // 径向渐变半径
            if (gradientType == GradientDrawable.RADIAL_GRADIENT) {
                float gradientRadius = drawable.getGradientRadius();
                map.put(startName + "GradientRadius", NodeValue.createNodeBold(String.valueOf(gradientRadius)));
            }
        } catch (Throwable e) {
            // ignore
        }

        // 渐变起始/结束颜色 (mGradientState.mGradientColors)
        try {
            Object gradientState = RefInvoke.getFieldOjbect(
                    "android.graphics.drawable.GradientDrawable", drawable, "mGradientState");
            if (gradientState != null) {
                String stateClassName = gradientState.getClass().getName();
                ColorStateList[] gradientColors = (ColorStateList[]) RefInvoke.getFieldOjbect(
                        stateClassName, gradientState, "mGradientColors");
                if (gradientColors != null && gradientColors.length > 0) {
                    int startColor = gradientColors[0] != null ? gradientColors[0].getDefaultColor() : 0;
                    int endColor = 0;
                    if (gradientColors.length >= 3 && gradientColors[2] != null) {
                        endColor = gradientColors[2].getDefaultColor();
                    } else if (gradientColors.length >= 2 && gradientColors[1] != null) {
                        endColor = gradientColors[1].getDefaultColor();
                    }
                    if (startColor != 0 || endColor != 0) {
                        map.put(startName + "GradientStartColor", NodeValue.createNodeBold(colorIntToString(startColor)));
                        map.put(startName + "GradientEndColor", NodeValue.createNodeBold(colorIntToString(endColor)));
                    }
                }
            }
        } catch (Throwable e) {
            // ignore
        }

        // 尺寸 (mGradientState.mWidth / mHeight)
        try {
            Object gradientState = RefInvoke.getFieldOjbect(
                    "android.graphics.drawable.GradientDrawable", drawable, "mGradientState");
            if (gradientState != null) {
                String stateClassName = gradientState.getClass().getName();
                int width = (int) RefInvoke.getFieldOjbect(stateClassName, gradientState, "mWidth");
                int height = (int) RefInvoke.getFieldOjbect(stateClassName, gradientState, "mHeight");
                if (width > 0 && height > 0) {
                    map.put(startName + "Size", NodeValue.createNodeBold(width + "x" + height));
                }
            }
        } catch (Throwable e) {
            // ignore
        }

        // 方向
        try {
            GradientDrawable.Orientation orientation = drawable.getOrientation();
            if (orientation != null) {
                map.put(startName + "Orientation", NodeValue.createNodeBold(orientation.name()));
            }
        } catch (Throwable e) {
            // ignore
        }
    }

    private static void handleRippleDrawable(Map<String, NodeValue> map, RippleDrawable drawable, String startName) {
        // 水波纹颜色 (mState.mColor)
        try {
            Object rippleState = RefInvoke.getFieldOjbect(
                    "android.graphics.drawable.RippleDrawable", drawable, "mState");
            if (rippleState != null) {
                String stateClassName = rippleState.getClass().getName();
                ColorStateList rippleColor = (ColorStateList) RefInvoke.getFieldOjbect(
                        stateClassName, rippleState, "mColor");
                if (rippleColor != null) {
                    map.put(startName + "RippleColor", NodeValue.createNodeBold(colorIntToString(rippleColor.getDefaultColor())));
                }
            }
        } catch (Throwable e) {
            // ignore
        }
        // 图层数量
        try {
            int layerCount = drawable.getNumberOfLayers();
            map.put(startName + "LayerCount", NodeValue.createNodeBold(String.valueOf(layerCount)));
        } catch (Throwable e) {
            // ignore
        }
    }

    private static void handleColorDrawable(Map<String, NodeValue> map, ColorDrawable drawable, String startName) {
        int color = drawable.getColor();
        map.put(startName + "Color", NodeValue.createNodeBold(colorIntToString(color)));
    }

    private static void handleBitmapDrawable(Map<String, NodeValue> map, BitmapDrawable drawable, String startName) {
        Bitmap bitmap = drawable.getBitmap();
        if (bitmap != null) {
            map.put(startName + "BitmapSize", NodeValue.createNodeBold(bitmap.getWidth() + "x" + bitmap.getHeight()));
            Bitmap.Config config = bitmap.getConfig();
            if (config != null) {
                map.put(startName + "BitmapConfig", NodeValue.createNodeBold(config.name()));
            }
        }
    }

    private static void handleShapeDrawable(Map<String, NodeValue> map, ShapeDrawable drawable, String startName) {
        // 形状类型
        if (drawable.getShape() != null) {
            map.put(startName + "ShapeType", NodeValue.createNodeBold(drawable.getShape().getClass().getSimpleName()));
        }
        // 填充颜色
        int shapeColor = drawable.getPaint().getColor();
        if (shapeColor != 0) {
            map.put(startName + "Color", NodeValue.createNodeBold(colorIntToString(shapeColor)));
        }
    }

    private static void handleInsetDrawable(Map<String, NodeValue> map, Drawable drawable, String startName) {
        // 内嵌间距
        Rect rect = new Rect();
        if (drawable.getPadding(rect)) {
            map.put(startName + "Inset", NodeValue.createNodeBold(
                    "left=" + rect.left + ", top=" + rect.top +
                            ", right=" + rect.right + ", bottom=" + rect.bottom));
        }
        // 内部 Drawable
        try {
            Drawable innerDrawable = ((InsetDrawable) drawable).getDrawable();
            if (innerDrawable != null) {
                map.put(startName + "InnerDrawable", NodeValue.createNodeBold(innerDrawable.getClass().getSimpleName()));
            }
        } catch (Throwable e) {
            // getDrawable() 在 API 23 以下不可用
        }
    }

    private static void handleLayerDrawable(Map<String, NodeValue> map, LayerDrawable drawable, String startName) {
        // 图层数量
        try {
            int layerCount = drawable.getNumberOfLayers();
            map.put(startName + "LayerCount", NodeValue.createNodeBold(String.valueOf(layerCount)));
        } catch (Throwable e) {
            // ignore
        }
    }

    private static void handleVectorDrawable(Map<String, NodeValue> map, Drawable drawable, String startName) {
        // 矢量图固有尺寸
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width > 0 && height > 0) {
            map.put(startName + "Size", NodeValue.createNodeBold(width + "x" + height));
        }
    }

    private static void handleStateListDrawable(Map<String, NodeValue> map, StateListDrawable drawable, String startName) {
        // 状态数量
        int stateCount = drawable.getStateCount();
        map.put(startName + "StateCount", NodeValue.createNodeBold(String.valueOf(stateCount)));
    }

    private static void handleAdaptiveIconDrawable(Map<String, NodeValue> map, AdaptiveIconDrawable drawable, String startName) {
        // 前景/背景图层类型
        Drawable foreground = drawable.getForeground();
        Drawable bg = drawable.getBackground();
        if (foreground != null) {
            map.put(startName + "Foreground", NodeValue.createNodeBold(foreground.getClass().getSimpleName()));
        }
        if (bg != null) {
            map.put(startName + "AdaptiveBg", NodeValue.createNodeBold(bg.getClass().getSimpleName()));
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

    public static String colorIntToString(int colorInt) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#");
        stringBuilder.append(Integer.toHexString(colorInt));
        return stringBuilder.toString();
    }
}
