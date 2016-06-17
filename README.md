# NaraePicker-New
NaraePicker, Open Source Library with Multi Gallery Select Function

## Usages
Import NaraePicker.aar which can download [Release Page]

Add Activity in your AndroidManifest.xml

    <activity android:name="moe.palette.naraepicker.NaraePickerActivity"
    android:label="@string/select_image"
    android:theme="@style/Theme.AppCompat.Light.DarkActionBar"/>

Add Intent in your Activity

    Intent pickerIntent = new Intent(this, NaraePickerActivity.class);
    startActivityForResult(pickerIntent, NaraePickerActivity.FINISH_ALL_WORK);

Add Callback methods in your Activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NaraePickerActivity.FINISH_ALL_WORK && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(GETTING_IMAGES);
            new NaraeAsync(new ImageUploadTask(images)).execute();
        }
    }
    
That's It! 
