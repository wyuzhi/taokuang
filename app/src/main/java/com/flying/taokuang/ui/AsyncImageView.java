package com.flying.taokuang.ui;

import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * 不支持wrap_content 见:https://www.fresco-cn.org/docs/wrap-content.html
 */
public class AsyncImageView extends SimpleDraweeView {
    public AsyncImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public AsyncImageView(Context context) {
        super(context);
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 图片url
     *
     * @param url
     */
    public void setUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        setImageURI(url);
    }

    /**
     * 占位图
     *
     * @param imgId
     */
    public void setPlaceholderImage(@DrawableRes int imgId) {
        getHierarchy().setPlaceholderImage(imgId);
    }

    /**
     * 占位图
     *
     * @param imgId
     */
    public void setPlaceholderImage(@DrawableRes int imgId, ScalingUtils.ScaleType scaleType) {
        getHierarchy().setPlaceholderImage(imgId, scaleType);
    }

    /**
     * 失败图
     *
     * @param imgId
     */
    public void setFailureImage(@DrawableRes int imgId) {
        getHierarchy().setFailureImage(imgId);
    }

    /**
     * scaleType
     *
     * @param type
     */
    public void setActualImageScaleType(ScalingUtils.ScaleType type) {
        if (type == null) {
            return;
        }
        getHierarchy().setActualImageScaleType(type);
    }


    /**
     * 图片设置圆形
     */
    public void setRoundAsCircle() {
        //只有占位图片和实际图片可以实现圆角
        RoundingParams roundingParams = RoundingParams.asCircle();
        getHierarchy().setRoundingParams(roundingParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new CircleOutlineProvider());
            setClipToOutline(true);
        }
    }

    /**
     * 图片设置圆角
     *
     * @param radius
     */
    public void setRoundingRadius(float radius) {
        if (radius < 0.0F) {
            return;
        }
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(radius);
        getHierarchy().setRoundingParams(roundingParams);
    }

    /**
     * 设置图片,支持wrap_content(不一定成功)
     *
     * @param url
     */
    public void setUrl2(String url) {
        if (!(getControllerBuilder() instanceof PipelineDraweeControllerBuilder)) {
            return;
        }
        DraweeController controller = ((PipelineDraweeControllerBuilder) getControllerBuilder())
                .setControllerListener(listener)
                .setUri(url)
                .setOldController(getController())
                .build();
        setController(controller);
    }

    /**
     * 支持根据宽高进行缩放,建议用这个
     *
     * @param url
     * @param width
     * @param height
     */
    public void setUrl(String url, int width, int height) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setResizeOptions(new ResizeOptions(width, height))
                .setProgressiveRenderingEnabled(true)
                .setAutoRotateEnabled(true)
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(getController())
                .setAutoPlayAnimations(true)
                .build();
        setController(controller);
    }

    private final ControllerListener listener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
            updateViewSize(imageInfo);
        }

        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
            updateViewSize(imageInfo);
        }
    };


    private void updateViewSize(ImageInfo imageInfo) {
        if (imageInfo != null) {
            setAspectRatio((float) imageInfo.getWidth() / imageInfo.getHeight());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static class CircleOutlineProvider extends ViewOutlineProvider {
        @Override
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, view.getWidth(), view.getHeight());
        }
    }
}
