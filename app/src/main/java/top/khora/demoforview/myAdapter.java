package top.khora.demoforview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class myAdapter extends RecyclerView.Adapter{
    private List<String> viewContentList;
    private Context mContext;

    public myAdapter(List<String> viewList, Context context) {
        this.viewContentList = viewList;
        this.mContext=context;
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView item_tv;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            item_tv = itemView.findViewById(R.id.tv_label_name);
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//缓存拿不到vh才调用
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent,false);
        myViewHolder viewHolder = new myViewHolder(inflate);
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
    }

    @Override
    public int getItemCount() {
        return viewContentList.size();
    }
}
