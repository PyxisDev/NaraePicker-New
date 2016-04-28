package com.github.windsekirun.narae.picker;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.palette_dream_ui_typeface_library.PaletteDREAMUI;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView list;

    GridLayoutManager mLayoutManager;
    ArrayList<Pair<ImageItem, Boolean>> itemSet;
    ArrayList<String> fileList = new ArrayList<>();
    ImageGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        list = (RecyclerView) findViewById(R.id.list);

        list.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 3);
        list.setLayoutManager(mLayoutManager);

        toolbarSetting();

        new LoadAllGalleryList().execute();
    }

    public void toolbarSetting() {
        toolbar.setTitle("미디어를 선택하세요.");
        toolbar.setBackgroundColor(Material.getMaterialLightBlueColor(500));
        toolbar.setTitleTextColor(Material.getWhite());
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings({"Convert2streamapi", "ConstantConditions"})
    public class LoadAllGalleryList extends AsyncTask<Void, Void, Void> {
        ArrayList<String> albumNameList = new ArrayList<>();
        ArrayList<ImageItem> tempSet = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... params) {
            // 전체 경로 파악
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA},
                    null, null, MediaStore.Images.Media.DATE_ADDED);

            if (cursor.moveToLast()) {
                do {
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    if (!albumNameList.contains(album)) {
                        albumNameList.add(album);
                    }
                } while (cursor.moveToPrevious());
            }

            cursor.close();

            // 전체 경로 기반으로 각각의 이미지 로드
            for (String albumName : albumNameList) {
                File file;
                Cursor cursor2 = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA},
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?", new String[]{albumName}, MediaStore.Images.Media.DATE_ADDED);
                if (cursor2.moveToLast()) {
                    do {
                        String image = cursor2.getString(cursor2.getColumnIndex(MediaStore.Images.Media.DATA));
                        file = new File(image);
                        if (file.exists())
                            tempSet.add(new ImageItem(image));
                    } while (cursor2.moveToPrevious());
                }
                cursor2.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            itemSet = new ArrayList<>();

            itemSet.add(new Pair<>(new ImageItem("camera"), false));
            itemSet.add(new Pair<>(new ImageItem("gallery"), false));

            for (ImageItem item : tempSet) {
                itemSet.add(new Pair<>(item, false));
            }

            adapter = new ImageGridAdapter(itemSet);
            list.setAdapter(adapter);
            tempSet.clear();
            tempSet.trimToSize();
        }
    }

    public class ItemClickListener implements View.OnClickListener {
        int position;
        ImageView check;

        public ItemClickListener(int i, ImageView c) {
            position = i;
            check = c;
        }

        @Override
        public void onClick(View v) {
            String filePath = itemSet.get(position).first.imagePath;
            Log.d("NaraePicker", "select image: " + filePath);
            if (fileList.contains(filePath)) {
                fileList.remove(filePath);
                Pair<ImageItem, Boolean> newData = new Pair<>(itemSet.get(position).first, false);
                itemSet.set(position, newData);
            } else {
                if (fileList.size() < 4) {
                    fileList.add(filePath);
                    Pair<ImageItem, Boolean> newData = new Pair<>(itemSet.get(position).first, true);
                    itemSet.set(position, newData);
                } else {
                    Toast.makeText(MainActivity.this, "최대 업로드 수를 초과했습니다!", Toast.LENGTH_SHORT).show();
                }
            }
            adapter.notifyItemChanged(position);
        }
    }


    @SuppressWarnings("Convert2Lambda")
    public class ImageGridAdapter extends RecyclerView.Adapter<ImageHolder> {
        ArrayList<Pair<ImageItem, Boolean>> dataSet;
        Context c;

        public ImageGridAdapter(ArrayList<Pair<ImageItem, Boolean>> itemSet) {
            dataSet = itemSet;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            c = parent.getContext();
            View v = LayoutInflater.from(c).inflate(R.layout.row_image, parent, false);
            return new ImageHolder(v);
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            ImageItem data = dataSet.get(position).first;
            if (data.imagePath.equals("camera")) {
                holder.check.setVisibility(View.GONE);
                holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER);
                holder.thumbnail.setImageDrawable(new IconicsDrawable(MainActivity.this, PaletteDREAMUI.Icon.Palette_camera).color(Material.getWhite()).sizeDp(48));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 카메라로부터 데이터 가져오기
                    }
                });
            } else if (data.imagePath.equals("gallery")) {
                holder.check.setVisibility(View.GONE);
                holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER);
                holder.thumbnail.setImageDrawable(new IconicsDrawable(MainActivity.this, PaletteDREAMUI.Icon.Palette_gallery).color(Material.getWhite()).sizeDp(48));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 갤러리로부터 데이터 가져오기
                    }
                });
            } else {
                holder.check.setVisibility(View.VISIBLE);
                holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.itemView.setOnClickListener(new ItemClickListener(position, holder.check));
                File file = new File(data.imagePath);
                holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(MainActivity.this).load(file).thumbnail(0.5f).into(holder.thumbnail);
                if (dataSet.get(position).second) {
                    holder.check.setImageResource(R.drawable.check_ok);
                } else {
                    holder.check.setImageResource(R.drawable.check_blank);
                }
            }
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }


    public class ImageItem {
        public String imagePath;

        public ImageItem(String imagePath) {
            this.imagePath = imagePath;
        }
    }


    public class ImageHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public ImageView check;

        public ImageHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            check = (ImageView) itemView.findViewById(R.id.img_check);
        }
    }
}
