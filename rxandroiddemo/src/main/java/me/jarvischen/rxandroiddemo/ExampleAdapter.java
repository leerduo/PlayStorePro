package me.jarvischen.rxandroiddemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chenfuduo on 2016/5/24.
 */
public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ViewHolder>{
    private Context context;
    private List<ExampleActivityAndName> mExamples;

    public ExampleAdapter(Context context, List<ExampleActivityAndName> mExamples) {
        this.context = context;
        this.mExamples = mExamples;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  v = LayoutInflater.from(context).inflate(R.layout.example_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mNameDisplay.setText(mExamples.get(position).mExampleName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, mExamples.get(position).mExampleActivityClass);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExamples.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView mNameDisplay;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameDisplay = (TextView) itemView.findViewById(R.id.name_display);
        }
    }
}
