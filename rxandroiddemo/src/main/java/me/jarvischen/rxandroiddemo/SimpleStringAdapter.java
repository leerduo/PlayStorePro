package me.jarvischen.rxandroiddemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenfuduo on 2016/5/24.
 */
public class SimpleStringAdapter extends RecyclerView.Adapter<SimpleStringAdapter.ViewHolder> {

    private final Context context;
    private final List<String> strings = new ArrayList<>();

    public SimpleStringAdapter(Context context) {
        this.context = context;
    }

    public void setStrings(List<String> newStrings) {
        strings.clear();
        strings.addAll(newStrings);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.string_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mColorDisplay.setText(strings.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, strings.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mColorDisplay;

        public ViewHolder(View itemView) {
            super(itemView);
            mColorDisplay = (TextView) itemView.findViewById(R.id.color_display);
        }
    }

}
