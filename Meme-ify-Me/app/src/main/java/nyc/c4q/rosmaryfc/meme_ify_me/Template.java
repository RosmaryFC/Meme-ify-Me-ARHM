package nyc.c4q.rosmaryfc.meme_ify_me;

/**
 * Created by c4q-Allison on 6/12/15.
*/

import android.app.Dialog;

package accesscode.c4q.nyc.memeifyme;

        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.Typeface;
        import android.graphics.drawable.Drawable;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.support.v7.app.ActionBarActivity;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.CompoundButton;
        import android.widget.EditText;
        import android.widget.FrameLayout;
        import android.widget.ImageView;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.widget.ToggleButton;
        import android.widget.ViewSwitcher;


public class Template extends ActionBarActivity {
    private Spinner drop;
    private ViewSwitcher switcher;
    private ImageView camera_image_vanilla, camera_image_demotivational;
    private TextView caption_top_vanilla, caption_top_demotivational, caption_bottom_vanilla, caption_bottom_demotivational;
    private Button btn_save, btn_share;
    private ToggleButton toggle;
    private Bitmap photo;
    private static final String photoSave = "photo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        initializeViews();
        setTypeAssets();

        // Set up spinner and set drawables on select
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.memeArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop.setAdapter(adapter);
        drop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str = adapterView.getItemAtPosition(i).toString().toLowerCase();
                Drawable d;
                switch (str) {
                    case "cool":
                        d = getResources().getDrawable(R.drawable.cool);
                        draw(d);
                        break;
                    case "yao ming":
                        d = getResources().getDrawable(R.drawable.yaoming);
                        draw(d);
                        break;
                    case "evil plotting raccoon":
                        d = getResources().getDrawable(R.drawable.evilplottingraccoon);
                        draw(d);
                        break;
                    case "philosoraptor":
                        d = getResources().getDrawable(R.drawable.philosoraptor);
                        draw(d);
                        break;
                    case "socially awkward penguin":
                        d = getResources().getDrawable(R.drawable.sociallyawkwardpenguin);
                        draw(d);
                        break;
                    case "success kid":
                        d = getResources().getDrawable(R.drawable.successkid);
                        draw(d);
                        break;
                    case "scumbag steve":
                        d = getResources().getDrawable(R.drawable.scumbagsteve);
                        draw(d);
                        break;
                    case "one does not simply":
                        d = getResources().getDrawable(R.drawable.onedoesnotsimply);
                        draw(d);
                        break;
                    case "i don't always":
                        d = getResources().getDrawable(R.drawable.idontalways);
                        draw(d);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Listeners to pop-up dialog and get input
        caption_top_vanilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTextDialog(caption_top_vanilla).show();
            }
        });

        caption_bottom_vanilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTextDialog(caption_bottom_vanilla).show();
            }
        });

        caption_top_demotivational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTextDialog(caption_top_demotivational).show();
            }
        });

        caption_bottom_demotivational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTextDialog(caption_bottom_demotivational).show();
            }
        });

        //Used ViewSwitcher to toggle between vanilla and demotivational meme views
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    switcher.showNext();
                else
                    switcher.showPrevious();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameLayout meme = (FrameLayout) findViewById(R.id.meme);
                SaveMeme sm = new SaveMeme();
                Bitmap bitmap = sm.loadBitmapFromView(meme);
                sm.saveMeme(bitmap, "meme", getContentResolver());
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory()))); // Dison is fixing this.
                Toast.makeText(getApplicationContext(), "Meme saved!", Toast.LENGTH_LONG).show();
            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameLayout meme = (FrameLayout) findViewById(R.id.meme);
                SaveMeme sm = new SaveMeme();
                Bitmap bitmap = sm.loadBitmapFromView(meme);
                sm.saveMeme(bitmap, "meme", getContentResolver());

                String pathBm = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "meme", null);
                Uri bmUri = Uri.parse(pathBm);

                Intent attachIntent = new Intent(Intent.ACTION_SEND);
                attachIntent.putExtra(Intent.EXTRA_STREAM, bmUri);
                attachIntent.setType("image/png");
                startActivity(attachIntent);
            }
        });
    }

    public void draw(Drawable d) {
        camera_image_vanilla.setImageDrawable(d);
        camera_image_demotivational.setImageDrawable(d);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(photoSave, photo);
    }

    private void initializeViews() {
        drop = (Spinner) findViewById(R.id.sp);
        switcher = (ViewSwitcher) findViewById(R.id.switcher);
        camera_image_vanilla = (ImageView) findViewById(R.id.camera_image_vanilla);
        camera_image_demotivational = (ImageView) findViewById(R.id.camera_image_demotivational);
        caption_top_vanilla = (TextView) findViewById(R.id.caption_top_vanilla);
        caption_top_demotivational = (TextView) findViewById(R.id.caption_top_demotivational);
        caption_bottom_vanilla = (TextView) findViewById(R.id.caption_bottom_vanilla);
        caption_bottom_demotivational = (TextView) findViewById(R.id.caption_bottom_demotivational);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_share = (Button) findViewById(R.id.btn_share);
        toggle = (ToggleButton) findViewById(R.id.toggle);
    }

    private void setTypeAssets() {
        Typeface impact = Typeface.createFromAsset(getAssets(), "Impact.ttf");
        caption_top_vanilla.setTypeface(impact);
        caption_bottom_vanilla.setTypeface(impact);

        Typeface times = Typeface.createFromAsset(getAssets(), "TimesNewRoman.ttf");
        caption_top_demotivational.setTypeface(times);
        caption_bottom_demotivational.setTypeface(times);
    }

    private Dialog myTextDialog(final TextView tv) {
        final View layout = View.inflate(this, R.layout.dialog_edit_caption, null);
        final EditText et_input = (EditText) layout.findViewById(R.id.et_input);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(0);

        builder.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Set caption to inputted text
                String input = et_input.getText().toString().trim();
                tv.setText(input);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });
        builder.setView(layout);
        return builder.create();
    }
}
