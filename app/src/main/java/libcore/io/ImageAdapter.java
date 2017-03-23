package libcore.io;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gree.bitmap.MainActivity;
import com.gree.bitmap.R;
import com.gree.bitmap.SquareImageView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by JackHou on 2017/3/23.
 */
public class ImageAdapter extends BaseAdapter
{
    private LayoutInflater mInlater;
    private Context mContext;
    private List<String> mUrList = new LinkedList<>();
    private ImageLoader mImageLoader;
    /*每一屏幕显示的item数*/
    private static final int ONE_SCREEN_ITEMS = 18;

    public ImageAdapter(Context context, List list)
    {
        mContext = context;
        mInlater = LayoutInflater.from(context);
        mUrList = list;
        mImageLoader = ImageLoader.build(context);
    }

    @Override
    public int getCount()
    {
        return mUrList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mUrList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if(convertView == null)
        {
            convertView = mInlater.inflate(R.layout.gridview_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageView imageView = holder.imageView;
        final String uri = (String) getItem(position);

        if(MainActivity.mIsGridViewIdle)
        {
            Log.i("zxxx","停止滑动时候的更新");
            imageView.setTag(uri);
            mImageLoader.bindBitmap(uri, imageView, 300, 300);
        }
        /*当滑动到最顶部和最底部时候必须刷新*/
        else if(position < ONE_SCREEN_ITEMS || position > mUrList.size() - ONE_SCREEN_ITEMS)
        {
            Log.i("zxxx","滑动到最底部或者最顶部时候的更新");
            imageView.setTag(uri);
            mImageLoader.bindBitmap(uri, imageView, 300, 300);
        }

        return convertView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        SquareImageView imageView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            imageView = (SquareImageView) itemView.findViewById(R.id.img_item);
        }
    }
}


