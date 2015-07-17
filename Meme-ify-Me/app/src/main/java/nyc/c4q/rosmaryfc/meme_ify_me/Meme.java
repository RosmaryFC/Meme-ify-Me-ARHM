package nyc.c4q.rosmaryfc.meme_ify_me;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by c4q-ac35 on 7/17/15.
 */
@DatabaseTable
public class Meme  {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private int picture;
    @DatabaseField
    private String title;

    public Meme(){

    }

    public Meme (Integer picture, String title){
        this.picture = picture;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

