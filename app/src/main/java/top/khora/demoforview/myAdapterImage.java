package top.khora.demoforview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;


class myAdapterImage extends RecyclerView.Adapter{
    private List<String> viewContentList;
    private Context mContext;

    public myAdapterImage(List<String> viewList, Context context) {
        this.viewContentList = viewList;
        this.mContext=context;
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView item_tv;
        ImageView item_iv;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            item_tv = itemView.findViewById(R.id.tv_label_name);
            item_iv = itemView.findViewById(R.id.iv_item);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//缓存拿不到vh才调用
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent,false);
        RecyclerView.ViewHolder viewHolder = new myViewHolder(inflate);
        return viewHolder;
    }

    /**
     * 自定义viewType设置不同类型的viewHolder
     * */
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {//缓存中拿到vh调用
        myViewHolder mHolder= (myViewHolder) holder;
        mHolder.item_tv.setText(viewContentList.get(position));
        mHolder.item_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,
                        viewContentList.get(position),Toast.LENGTH_SHORT).show();
            }
        });
        Glide.with(mContext)
                .load("http://cnd.khora.top/pictures/p1603279451539txiuoenjbzsaeufrzjzv_lite.png")
                .override(400,200)
                .fitCenter()//裁剪，即缩放图像让图像都测量出来等于或小于 ImageView 的边界范围。
                // 该图像将会完全显示，但可能不会填满整个 ImageView。
                .placeholder(R.drawable.ic_launcher_background)//占位图
                .into(mHolder.item_iv);
    }

    @Override
    public int getItemCount() {
        return viewContentList.size();
    }
}
