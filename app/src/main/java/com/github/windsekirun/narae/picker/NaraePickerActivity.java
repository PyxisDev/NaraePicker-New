package com.github.windsekirun.narae.picker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.windsekirun.narae.picker.util.FrescoUtils;
import com.github.windsekirun.narae.picker.util.ImageHolder;
import com.github.windsekirun.narae.picker.util.ImageItem;
import com.github.windsekirun.narae.picker.util.IntentUtils;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.palette_dream_ui_typeface_library.PaletteDREAMUI;

import java.io.File;
import java.util.ArrayList;


@SuppressWarnings({"ConstantConditions", "Convert2Lambda", "SimplifiableIfStatement"})
public class NaraePickerActivity extends AppCompatActivity implements PickerConstants {
    Toolbar toolbar;
    RecyclerView list;

    GridLayoutManager mLayoutManager;
    ArrayList<Pair<ImageItem, Boolean>> itemSet;
    ArrayList<String> fileList = new ArrayList<>();
    ArrayList<String> toSendList = new ArrayList<>();
    ImageGridAdapter adapter;

    String cameraPicPath;

    int CAMERA_OUTPUT = 1;
    int GALLERY_OUTPUT = 2;
    int CAMERA_GALLERY_AVAIRY = 3;
    int SELECT_AVAIRY = 4;
    int FINISH_ALL_WORK = 72;

    public static final String GETTING_IMAGES = "GETTING_IMAGE";

    String[] toGrantPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    int PERMISSION_ACCEPT = 84;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        int theme = getIntent().getIntExtra(THEME, 0);

        setTheme(theme);
        setStatusNavBarColor(getWindow(), getPrimaryBoldColor());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        list = (RecyclerView) findViewById(R.id.list);

        list.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 3);
        list.setLayoutManager(mLayoutManager);

        toolbarSetting();

        if (isGrantPermission()) {
            new LoadAllGalleryList().execute();
        } else {
            Toast.makeText(NaraePickerActivity.this, R.string.no_grant_permission, Toast.LENGTH_SHORT).show();
        }
    }

    public void toolbarSetting() {
        toolbar.setTitle(R.string.select_image);
        toolbar.setBackgroundColor(getPrimaryColor());
        toolbar.setTitleTextColor(0xffffffff);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:
                if (fileList.size() > 0) {
                    Uri imageUri2 = Uri.parse(fileList.get(0));
                    Intent imageEditorIntent = new AdobeImageIntent.Builder(this).setData(imageUri2).build();
                    startActivityForResult(imageEditorIntent, SELECT_AVAIRY);
                } else {
                    Toast.makeText(NaraePickerActivity.this, R.string.no_select_image, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_OUTPUT) {
                String imageFilePath = null;
                if (new File(cameraPicPath).exists()) {
                    imageFilePath = cameraPicPath;
                }
                Uri imageUri = Uri.parse(imageFilePath);
                Intent imageEditorIntent = new AdobeImageIntent.Builder(this).setData(imageUri).build();
                startActivityForResult(imageEditorIntent, CAMERA_GALLERY_AVAIRY);
            } else if (requestCode == GALLERY_OUTPUT) {
                Uri imageUri = IntentUtils.getPickImageResultUri(data);
                if (imageUri != null) {
                    Intent imageEditorIntent = new AdobeImageIntent.Builder(this).setData(imageUri).build();
                    startActivityForResult(imageEditorIntent, CAMERA_GALLERY_AVAIRY);
                } else {
                    if (cameraPicPath != null) {
                        Uri imageUri2 = Uri.parse(cameraPicPath);
                        Intent imageEditorIntent = new AdobeImageIntent.Builder(this).setData(imageUri2).build();
                        startActivityForResult(imageEditorIntent, CAMERA_GALLERY_AVAIRY);
                    } else {
                        Toast.makeText(NaraePickerActivity.this, R.string.file_not_found, Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == CAMERA_GALLERY_AVAIRY) {
                Uri editedImageUri = data.getData();
                toSendList.add(editedImageUri.toString());
                sendImages();
            } else {
                switch (requestCode) {
                    case 4:
                        Uri editedImageUri = data.getData();
                        toSendList.add(editedImageUri.toString());
                        if (fileList.size() != 1) {
                            Uri imageUri2 = Uri.parse(fileList.get(1));
                            Intent imageEditorIntent = new AdobeImageIntent.Builder(this).setData(imageUri2).build();
                            startActivityForResult(imageEditorIntent, SELECT_AVAIRY + 1);
                        } else {
                            sendImages();
                        }
                        break;
                    case 5:
                        Uri editedImageUri2 = data.getData();
                        toSendList.add(editedImageUri2.toString());
                        if (fileList.size() != 2) {
                            Uri imageUri2 = Uri.parse(fileList.get(2));
                            Intent imageEditorIntent = new AdobeImageIntent.Builder(this).setData(imageUri2).build();
                            startActivityForResult(imageEditorIntent, SELECT_AVAIRY + 2);
                        } else {
                            sendImages();
                        }
                        break;
                    case 6:
                        Uri editedImageUri3 = data.getData();
                        toSendList.add(editedImageUri3.toString());
                        if (fileList.size() != 3) {
                            Uri imageUri2 = Uri.parse(fileList.get(3));
                            Intent imageEditorIntent = new AdobeImageIntent.Builder(this).setData(imageUri2).build();
                            startActivityForResult(imageEditorIntent, SELECT_AVAIRY + 3);
                        } else {
                            sendImages();
                        }
                        break;
                    case 7:
                        Uri editedImageUri4 = data.getData();
                        toSendList.add(editedImageUri4.toString());
                        sendImages();
                        break;
                }
            }
        }
    }

    public boolean isGrantPermission() {
        boolean toReturn;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 일부 권한이라도 통과되지 않은 상태임
                // 따라서 따로 요청함
                toReturn = requestPermissions();
            } else {
                toReturn = true;
            }
        } else {
            toReturn = true;
        }
        return toReturn;
    }

    public boolean requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // 요청 다이얼로그 보여줘야 함
            new MaterialDialog.Builder(this)
                    .title(R.string.permission_dialog_title)
                    .content(R.string.permission_dialog_content)
                    .positiveText(R.string.permission_dialog_grant)
                    .negativeText(R.string.permission_dialog_cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            ActivityCompat.requestPermissions(NaraePickerActivity.this, toGrantPermission, PERMISSION_ACCEPT);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(this, toGrantPermission, PERMISSION_ACCEPT);
        }
        return false;
    }

    public boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }


        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_ACCEPT) {
            if (verifyPermissions(grantResults)) {
                Toast.makeText(NaraePickerActivity.this, R.string.grant_permission_success, Toast.LENGTH_SHORT).show();
                new LoadAllGalleryList().execute();
            } else {
                Toast.makeText(NaraePickerActivity.this, R.string.grant_permission_fail, Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void sendImages() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(GETTING_IMAGES, toSendList);
        setResult(FINISH_ALL_WORK, intent);
        finish();
    }

    @SuppressWarnings({"Convert2streamapi", "ConstantConditions"})
    public class LoadAllGalleryList extends AsyncTask<Void, Void, Void> {
        ArrayList<String> albumNameList = new ArrayList<>();
        ArrayList<ImageItem> tempSet = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... params) {
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
                    Toast.makeText(NaraePickerActivity.this, R.string.over_image_limit, Toast.LENGTH_SHORT).show();
                }
            }
            adapter.notifyItemChanged(position);
        }
    }


    @SuppressWarnings({"Convert2Lambda", "deprecation"})
    public class ImageGridAdapter extends RecyclerView.Adapter<ImageHolder> {
        ArrayList<Pair<ImageItem, Boolean>> dataSet;
        Context c;

        public ImageGridAdapter(ArrayList<Pair<ImageItem, Boolean>> itemSet) {
            dataSet = itemSet;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            c = parent.getContext();
            View v = LayoutInflater.from(c).inflate(R.layout.row_picker, parent, false);
            return new ImageHolder(v);
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            ImageItem data = dataSet.get(position).first;
            switch (data.imagePath) {
                case "camera":
                    holder.check.setVisibility(View.GONE);
                    holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER);
                    holder.thumbnail.setBackgroundResource(R.color.dark);
                    holder.thumbnail.setImageDrawable(new IconicsDrawable(NaraePickerActivity.this, PaletteDREAMUI.Icon.Palette_camera).color(0xffffffff).sizeDp(48));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @SuppressWarnings("ResultOfMethodCallIgnored")
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraPicPath = IntentUtils.getGeneratedPicPath();
                            File targetFile = new File(cameraPicPath);
                            if (!targetFile.canWrite()) {
                                File targetDirectory = new File(cameraPicPath.substring(0, cameraPicPath.lastIndexOf(File.separator)));
                                targetDirectory.mkdirs();
                            }
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(targetFile));
                            startActivityForResult(intent, CAMERA_OUTPUT);
                        }
                    });
                    break;
                case "gallery":
                    holder.check.setVisibility(View.GONE);
                    holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER);
                    holder.thumbnail.setBackgroundResource(R.color.dark);
                    holder.thumbnail.setImageDrawable(new IconicsDrawable(NaraePickerActivity.this, PaletteDREAMUI.Icon.Palette_gallery).color(0xffffffff).sizeDp(48));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(IntentUtils.getPickImageChooserIntent(getPackageManager()), GALLERY_OUTPUT);
                        }
                    });
                    break;
                default:
                    holder.check.setVisibility(View.VISIBLE);
                    holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    holder.itemView.setOnClickListener(new ItemClickListener(position, holder.check));
                    File file = new File(data.imagePath);

                    holder.thumbnail.setImageURI(Uri.fromFile(file));
                    holder.thumbnail.setHierarchy(FrescoUtils.setCenterCrop(holder.thumbnail));

                    if (dataSet.get(position).second) {
                        holder.check.setImageResource(R.drawable.check_ok);
                    } else {
                        holder.check.setImageResource(R.drawable.check_blank);
                    }
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }

    public int getPrimaryColor() {
        TypedValue value = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }

    public int getPrimaryBoldColor() {
        TypedValue value = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, value, true);
        return value.data;
    }

    public void setStatusNavBarColor(Window w, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.setStatusBarColor(color);
            w.setNavigationBarColor(color);
        }
    }

}
