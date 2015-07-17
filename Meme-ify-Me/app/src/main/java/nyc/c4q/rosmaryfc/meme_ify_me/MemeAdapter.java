package nyc.c4q.rosmaryfc.meme_ify_me;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by c4q-ac35 on 7/17/15.
 */
public class MemeAdapter extends BaseAdapter {
    private List<Meme> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    @Bind (R.id.memeImage) ImageView mImageView;
    @Bind (R.id.memeName) TextView mTextView;

    public MemeAdapter(Context context,List<Meme> mList){
        this.mContext = context;
        this.mList = mList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Meme getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.list_item,parent,false);
        }

        ButterKnife.bind(this,convertView);
        Picasso.with(mContext).load(getItem(position).getPicture()).resize(200,200).centerCrop().into(mImageView);

        return convertView;
    }
}
