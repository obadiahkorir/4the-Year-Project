package com.example.obadiahkorir.projects;

import java.util.HashMap;

/**
 * Created by Obadiah Korir on 9/2/2018.
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import java.util.HashMap;
public class SliderImageActivity extends AppCompatActivity
        implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener
{

    SliderLayout sliderLayout ;

    HashMap<String, String> HashMapForURL ;

    HashMap<String, Integer> HashMapForLocalRes ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_image);

        sliderLayout = (SliderLayout)findViewById(R.id.slider);

        //Call this method if you want to add images from URL .
        AddImageUrlFormLocalRes();

        //Call this method to add images from local drawable folder .
        //AddImageUrlFormLocalRes();

        //Call this method to stop automatic sliding.
        //sliderLayout.stopAutoCycle();

        for(String name : HashMapForLocalRes.keySet()){

            TextSliderView textSliderView = new TextSliderView(SliderImageActivity.this);

            textSliderView
                    .description(name)
                    .image(HashMapForLocalRes.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            textSliderView.bundle(new Bundle());

            textSliderView.getBundle()
                    .putString("extra",name);

            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        sliderLayout.setCustomAnimation(new DescriptionAnimation());

        sliderLayout.setDuration(3000);

        sliderLayout.addOnPageChangeListener(SliderImageActivity.this);
    }

    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void AddImagesUrlOnline(){

        HashMapForURL = new HashMap<String, String>();

        HashMapForURL.put("Lets StopChild Abuse", "https://chemisoftsolutions.000webhostapp.com/android/images/childabuse.jpg");
        HashMapForURL.put("Report Criminals", "https://chemisoftsolutions.000webhostapp.com/android/images/criminals.jpg");
        HashMapForURL.put("Fire Response, Kindly Raise and Alert", "https://chemisoftsolutions.000webhostapp.com/android/images/fire.jpg");
        HashMapForURL.put("Visit the Nearest Huduma Center for Help", "https://chemisoftsolutions.000webhostapp.com/android/images/Huduma-Kenya.png");
        HashMapForURL.put("Contact RedCross For Help", "https://chemisoftsolutions.000webhostapp.com/android/images/redcross.png");
    }

    public void AddImageUrlFormLocalRes(){

        HashMapForLocalRes = new HashMap<String, Integer>();

        HashMapForLocalRes.put("Lets StopChild Abuse", R.drawable.chidrens);
        HashMapForLocalRes.put("Report Criminals", R.drawable.criminalis);
        HashMapForLocalRes.put("Fire Response, Kindly Raise and Alert", R.drawable.fires);
        HashMapForLocalRes.put("Visit the Nearest Huduma Center for Help", R.drawable.huduma);
        HashMapForLocalRes.put("Contact RedCross For Help", R.drawable.redcrossi);

    }
}
