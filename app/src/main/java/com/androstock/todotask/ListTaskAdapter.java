package com.androstock.todotask;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Ferdousur Rahman Sarker on 10/23/2017.
 */

public class ListTaskAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private TextToSpeech tts;
    public ListTaskAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
    }
    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ListTaskViewHolder holder = null;
        if (convertView == null) {
            holder = new ListTaskViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.task_list_row, parent, false);
            holder.task_image = (TextView) convertView.findViewById(R.id.task_image);
            holder.task_name = (TextView) convertView.findViewById(R.id.task_name);
            holder.task_date = (TextView) convertView.findViewById(R.id.task_date);
            holder.speak=convertView.findViewById(R.id.speak);
            convertView.setTag(holder);
        } else {
            holder = (ListTaskViewHolder) convertView.getTag();
        }
        holder.task_image.setId(position);
        holder.task_name.setId(position);
        holder.task_date.setId(position);
        holder.speak.setId(position);

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        try{
            holder.task_name.setText(song.get(TaskHome.KEY_TASK));
            holder.task_date.setText(song.get(TaskHome.KEY_DATE));

            /* Image */
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(getItem(position));
            holder.task_image.setTextColor(color);
            holder.task_image.setText(Html.fromHtml("&#11044;"));

            tts=new TextToSpeech(activity.getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status == TextToSpeech.SUCCESS) {
                                int ttsLang = tts.setLanguage(Locale.US);

                                if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                                        || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    Log.e("TTS", "The Language is not supported!");
                                } else {
                                    Log.i("TTS", "Language Supported.");
                                }
                                Log.i("TTS", "Initialization success.");
                            } else {
                                Toast.makeText(activity.getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            final ListTaskViewHolder finalHolder = holder;
            holder.speak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String data = finalHolder.task_name.getText().toString()+"on"+finalHolder.task_date.getText().toString();
                    Log.i("TTS", "button clicked: " + data);
                    int speechStatus = tts.speak(data, TextToSpeech.QUEUE_FLUSH, null);

                    if (speechStatus == TextToSpeech.ERROR) {
                        Log.e("TTS", "Error in converting Text to Speech!");
                    }
                }
                });

            /* Image */

            }catch(Exception e) {}
        return convertView;
    }
}

class ListTaskViewHolder {
    TextView task_image;
    TextView task_name, task_date;
    Button speak;
}