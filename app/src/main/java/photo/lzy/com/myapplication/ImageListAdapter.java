package photo.lzy.com.myapplication;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lushijie on 2018/4/20.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.Holder>{
    private final List<Image> imageList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_list,parent,false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Image image = imageList.get(position);
        Bitmap thumb = BitmapCache.getInstance().getBitmap("thumb_" + image.getPath());
        if(thumb == null){
            thumb = MediaStore.Images.Thumbnails.getThumbnail  (GalleryApplication.getAppContext().getContentResolver(),  image.getId(), MediaStore.Images.Thumbnails.MICRO_KIND, null);
            BitmapCache.getInstance().putBitmap("thumb_" + image.getPath(),thumb);
        }
        holder.imageThumbnail.setImageBitmap(thumb);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void addItem(Image image){
        imageList.add(image);
        notifyItemInserted(imageList.size());
    }

    public void removeItem(){

    }

    public final class Holder extends RecyclerView.ViewHolder{
        private ImageView imageThumbnail;
        public Holder(View itemView) {
            super(itemView);
            imageThumbnail = itemView.findViewById(R.id.image_thumbnail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(imageList.get(getAdapterPosition()),getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(Image image,int position);
    }
}
