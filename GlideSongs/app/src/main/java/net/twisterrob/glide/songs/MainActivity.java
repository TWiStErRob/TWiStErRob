package net.twisterrob.glide.songs;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends ListActivity {
    private ImageView image;
    private SongCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.image);

        setListAdapter(adapter = new SongCursorAdapter(this, generateSongs()));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        select(position);
    }

    public void mediaNext(View v) {
        select(adapter.getCurrent() + 1);
    }

    public void mediaPrev(View v) {
        select(adapter.getCurrent() - 1);
    }


    private void select(int position) {
        position = Math.max(Math.min(position, adapter.getCount() - 1), 0);
        Cursor cursor = (Cursor) adapter.getItem(position);
        String icon = cursor.getString(cursor.getColumnIndexOrThrow("icon"));
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));

        //noinspection ConstantConditions
        getActionBar().setSubtitle(getString(R.string.title_playing, title));
        Glide.with(this)
                .load(icon)
                .placeholder(image.getDrawable())
                .crossFade(1000)
                .into(image)
        ;
        adapter.setCurrent(position);
        getListView().smoothScrollToPosition(position);
    }

    private Cursor generateSongs() {
        MatrixCursor c = new MatrixCursor(new String[]{"_id", "icon", "artist", "title"});
        try {
            InputStream adjectives = getResources().openRawResource(R.raw.adjectives);
            InputStream adverbs = getResources().openRawResource(R.raw.adverbs);
            InputStream nouns = getResources().openRawResource(R.raw.nouns);
            InputStream verbs = getResources().openRawResource(R.raw.verbs);
            SongRandomizer randomizer = SongRandomizer.from(adjectives, adverbs, nouns, verbs, null);

            Random RND = new Random(System.currentTimeMillis() / 1000 / 60 / 2); // different every two minutes
            float hsv[] = {0, 0.5f, 1.0f};
            for (int i = 0; i < 20; ++i) {
                hsv[0] = RND.nextInt(360);
                int color = Color.HSVToColor(0, hsv);
                String url = String.format(Locale.ROOT, "http://placehold.it/350x350/%06x/000000/.png&text=%d", color, i + 1);
                c.addRow(new Object[]{i, url, randomizer.randomArtist(), randomizer.randomTitle()});
            }
        } catch (Exception ex) {
            Log.e("IO", "Cannot read a word file", ex);
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }
        return c;
    }

    private static class SongCursorAdapter extends CursorAdapter {
        public SongCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, false);
        }

        private int current = -1;

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
            notifyDataSetChanged();
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            view.setTag(new Holder(view));
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String icon = cursor.getString(cursor.getColumnIndexOrThrow("icon"));
            String artist = cursor.getString(cursor.getColumnIndexOrThrow("artist"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));

            view.setBackgroundColor(current == cursor.getPosition() ? 0x88666666 : Color.TRANSPARENT);

            Holder holder = (Holder) view.getTag();
            holder.artist.setText(artist);
            holder.title.setText(title);
            Glide.with(context)
                    .load(icon)
                    .placeholder(R.drawable.ic_launcher)
                    .crossFade(2000)
                    .into(holder.icon)
            ;
        }

        private static class Holder {
            ImageView icon;
            TextView artist;
            TextView title;

            public Holder(View view) {
                icon = (ImageView) view.findViewById(R.id.icon);
                artist = (TextView) view.findViewById(R.id.artist);
                title = (TextView) view.findViewById(R.id.title);
            }
        }
    }
}
