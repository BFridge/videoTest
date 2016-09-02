package fridge.com.videotest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private VideoLayout videoLayout;
    private List<Integer> list = new ArrayList<>();
    private VideoContainerLayout containerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        containerLayout = (VideoContainerLayout) findViewById(R.id.main_layout);
        listView = (ListView) findViewById(R.id.list_view);
        videoLayout = (VideoLayout) findViewById(R.id.video_layout);
        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoLayout.isPlaying){
                    videoLayout.isPlaying = false;
                    videoLayout.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                }else{
                    videoLayout.isPlaying = true;
                    videoLayout.setImageDrawable(getResources().getDrawable(R.drawable.playing));
                }
            }
        });
        for(int i =0 ; i < 30; i++ ){
            list.add(i);
        }
        listView.setAdapter(new MyAdapter());
        //主要逻辑
        containerLayout.setListView(listView);
        containerLayout.setVideoLayout(videoLayout);

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
