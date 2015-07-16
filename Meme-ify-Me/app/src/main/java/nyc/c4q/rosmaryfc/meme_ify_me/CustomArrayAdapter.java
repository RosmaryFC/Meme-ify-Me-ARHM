package nyc.c4q.rosmaryfc.meme_ify_me;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by c4q-ac35 on 7/15/15.
 */
public class CustomArrayAdapter extends ArrayAdapter {
    List<String> memeNames= null;
    List<Integer> memeImages = null;
    private static LayoutInflater inflater = null;

    CustomArrayAdapter(Context context, List<String> names, List<Integer> images) {
        super(context, R.layout.list_item, names);
        memeNames = names;
        memeImages = images;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {
        if (convertView==null) {
            convertView=newView(parent);
        }

        bindView(position, convertView);

        return(convertView);
    }

    private View newView(ViewGroup parent) {
        return inflater.inflate(R.layout.list_item, parent, false);
    }

    private void bindView(int position, View row) {
        TextView label=(TextView)row.findViewById(R.id.memeName);

        label.setText(memeNames.get(position));

        ImageView icon=(ImageView)row.findViewById(R.id.memeImage);

        icon.setImageResource(memeImages.get(position));
    }
}
