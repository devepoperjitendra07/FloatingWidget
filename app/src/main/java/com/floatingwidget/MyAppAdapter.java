package com.floatingwidget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Mayk on 9/26/2017.
 */

public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.MyViewHolder> {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private List<String> appList;
    private List<AppGS> modelApp;
    AppGS appGS;
    int mLastPosition =0;

    public MyAppAdapter(Context mContext, List<String> appList) {
        this.mContext = mContext;
        this.appList = appList;
        mLayoutInflater=LayoutInflater.from(mContext);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mLayoutInflater.inflate(R.layout.recycler_content,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(mContext);

        final String ApplicationPackageName = (String) appList.get(position);
        String ApplicationLabelName = apkInfoExtractor.GetAppName(ApplicationPackageName);
        Drawable drawable = apkInfoExtractor.getAppIconByPackageName(ApplicationPackageName);


        holder.textView.setText(ApplicationLabelName);
        holder.imageView.setImageDrawable(drawable);
        holder.checkBox.setOnCheckedChangeListener(null);

//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(mContext);
//                Drawable selected = apkInfoExtractor.getAppIconByPackageName(ApplicationPackageName);
//
//            }
//        });
//        holder.ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(ApplicationPackageName);
                if(intent != null){

                    mContext.startActivity(intent);

                }
                else {

                    Toast.makeText(mContext,ApplicationPackageName + " Error, Please Try Again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout;
        ImageView imageView;
        CheckBox checkBox;
        TextView textView;
        ImageButton ok;

        public MyViewHolder(final View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.App_icon);
            checkBox =(CheckBox) itemView.findViewById(R.id.checkbox);
            textView = (TextView) itemView.findViewById(R.id.App_list);
            mainLayout = (LinearLayout)itemView.findViewById(R.id.mainLayout);
            ok =(ImageButton) itemView.findViewById(R.id.recycler_ok);


        }



    }
}
