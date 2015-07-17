package nyc.c4q.rosmaryfc.meme_ify_me;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class DemotivationalMemeEdit extends ActionBarActivity {
    private TextView titleTextView;
    private TextView phraseTextView;
    private EditText titleEditText;
    private EditText phraseEditText;

    private RelativeLayout layout;
    private Bitmap viewbitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demotivational_meme_edit);

        Button titleEditTxtPreviewBtn = (Button) findViewById(R.id.title_editText_preview_btn);
        titleEditTxtPreviewBtn.setOnClickListener(titlePreviewBtnListener);

        Button phraseEditTxtPreviewBtn = (Button) findViewById(R.id.phrase_editText_preview_btn);
        phraseEditTxtPreviewBtn.setOnClickListener(phrasePreviewBtnListener);

        //Todo: some code for the save button that might be useful, is not working yet so left commented out
//        Button saveButton = (Button) findViewById(R.id.save_meme);
//        saveButton.setOnClickListener(saveBtnListener);

    }

//    private View.OnClickListener saveBtnListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//
//            layout = (RelativeLayout) findViewById(R.id.meme_preview_relative_layout);
//            layout.setDrawingCacheEnabled(true);
//            layout.buildDrawingCache();
//            viewbitmap = layout.getDrawingCache(true);
//
//            File f = new File(getApplicationContext().getFilesDir(), "memeEdit"+"1"+".jpg");
//
//            try {
//                viewbitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(f));
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    };

    private View.OnClickListener titlePreviewBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            titleEditText = (EditText) findViewById(R.id.title_editText);
            titleTextView = (TextView) findViewById(R.id.title_textView);
            titleTextView.setText(titleEditText.getText().toString());
        }
    };

    private View.OnClickListener phrasePreviewBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            phraseEditText = (EditText) findViewById(R.id.phrase_editText);
            phraseTextView = (TextView) findViewById(R.id.phrase_textView);
            phraseTextView.setText(phraseEditText.getText().toString());
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_demotivational_meme_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
