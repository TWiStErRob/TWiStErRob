package net.twisterrob.glide.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorUriLoader;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import net.twisterrob.glide.gallery.wrappers.ModelLoaderFactoryWrapper;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends ActionBarActivity implements ModelLoaderFactoryWrapper.ExceptionHandler {
    private static final int REQUEST_GET_PICTURE = RESULT_FIRST_USER + 1;
    private static final int REQUEST_TAKE_PICTURE = RESULT_FIRST_USER + 2;

    private EditText uri;
    private TextView status;
    private File picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setSubtitle(getString(R.string.subtitle, Build.VERSION.RELEASE, Build.VERSION.SDK_INT));
        setContentView(R.layout.activity_main);

        uri = (EditText) findViewById(R.id.uri);
        status = (TextView) findViewById(R.id.status);
        status.setMovementMethod(new ScrollingMovementMethod());
    }

    public void refresh() {
        glide(findViewById(R.id.image));
    }

    public void glide(View v) {
        String uri = this.uri.getText().toString();
        Glide.with(this.getApplicationContext()).load(uri).listener(new StatusLogger()).into((ImageView) v);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();

        FileDescriptorUriLoader.Factory originalFactory = new FileDescriptorUriLoader.Factory();
        ModelLoaderFactory<Uri, ParcelFileDescriptor> wrapperFactory = new ModelLoaderFactoryWrapper<>(originalFactory, this);
        Glide.get(this).register(Uri.class, ParcelFileDescriptor.class, wrapperFactory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_gallery:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_GET_PICTURE);
                return true;
            case R.id.action_take:
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                picture = new File(storageDir, timeStamp + ".jpg");

                Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
                startActivityForResult(pictureIntent, REQUEST_TAKE_PICTURE);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_GET_PICTURE:
                if (resultCode == RESULT_OK) {
                    uri.setText(data.getDataString());
                    status.setText("New uri: " + data.getDataString());
                    refresh();
                }
                break;
            case REQUEST_TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    uri.setText(Uri.fromFile(picture).toString());
                    status.setText("New file: " + picture);
                    refresh();
                }
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class StatusLogger implements RequestListener<String, GlideDrawable> {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            StringBuilder sb = new StringBuilder();
            sb.append("onException(...,");
            sb.append("\n    ").append("model: \"").append(model).append("\"");
            sb.append("\n    ").append("target: ").append(target);
            sb.append("\n    ").append("isFirstResource: ").append(isFirstResource);
            sb.append("\n)");
            sb.append("\n\n").append(getStack(e));
            status.setText(sb);
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            StringBuilder sb = new StringBuilder();
            sb.append("onResourceReady(");
            sb.append("\n    ").append("resource: ").append(resource);
            sb.append("\n    ").append("model: \"").append(model).append("\"");
            sb.append("\n    ").append("target: ").append(target);
            sb.append("\n    ").append("isFromMemoryCache: ").append(isFromMemoryCache);
            sb.append("\n    ").append("isFirstResource: ").append(isFirstResource);
            sb.append("\n)");
            status.setText(sb);
            return false;
        }
    }

    private String getStack(Throwable t) {
        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer, true));
        return writer.toString();
    }

    @Override
    public void handle(final Throwable t) {
        t.printStackTrace();
        status.post(new Runnable() {
            @Override
            public void run() {
                status.setText(getStack(t));
            }
        });
    }
}
