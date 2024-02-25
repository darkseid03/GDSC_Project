package com.example.legal;
//    private int[] imageResources = {R.drawable.image7, R.drawable.image2, R.drawable.image3, R.drawable.image4 , R.drawable.image5, R.drawable.image6};
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

public class CustomPagerAdapter extends PagerAdapter {

    private Context context;
    private int[] imageResources = {R.drawable.image7, R.drawable.image2, R.drawable.image3, R.drawable.image4 , R.drawable.image5, R.drawable.image6};

    public CustomPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.slide_item, container, false);

        ImageView imageView = view.findViewById(R.id.image_slide);
        View darkOverlay = view.findViewById(R.id.dark_overlay);
        Glide.with(context).load(imageResources[position]).into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public static class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;
        private static final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            View darkOverlay = view.findViewById(R.id.dark_overlay);
            darkOverlay.setAlpha(Math.max(MIN_ALPHA, 1 - position));

            if (position < -1) {
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]

                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                     view.setAlpha(Math.max(MIN_ALPHA, 1 - position));


                view.setTranslationX(pageWidth * -position);


                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else {
                view.setAlpha(0f);
            }
        }
    }

}
