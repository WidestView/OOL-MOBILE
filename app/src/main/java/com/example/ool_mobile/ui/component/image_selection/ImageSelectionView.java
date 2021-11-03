

package com.example.ool_mobile.ui.component.image_selection;

public class ImageSelectionView extends FrameLayout {

    public ImageSelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @BindingMethod("url")
    public void setImageUrl(Uri uri) {

    }

    @BindingMethod("fallback")
    public void setFallbackImage(Drawable drawable) {
        
    }

    @BindingMethod("onImageClick")
    public void setName() {

    }

    Uri getImageUrl(Uri uri) {

    }

    Drawable getFallbackImage(Drawable drawable) {

    }

}