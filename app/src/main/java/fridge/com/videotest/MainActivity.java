package fridge.com.videotest;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ImageView videoLayout;
    private List<Integer> list = new ArrayList<>();
    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        listView = (ListView) findViewById(R.id.list_view);
        videoLayout = (ImageView) findViewById(R.id.video_layout);
        for(int i =0 ; i < 30; i++ ){
            list.add(i);
        }
        listView.setAdapter(new MyAdapter());
//        AnimationSet set = new AnimationSet(this,null);
        final ObjectAnimator animator3 = n ObjectAnimator.ofFloat(videoLayout, "scaleY",1f,0.5f);
        animator3.setRepeatCount(0);
        animator3.setStartDelay(1000);
        animator3.start();
        animator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mainLayout.requestLayout();
                listView.requestLayout();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });



    }

    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_item_layout,null,false);
            TextView tv = (TextView)view.findViewById(R.id.sub_tittle);
            tv.setText("" + list.get(position));
            return view;
        }
    }

}
