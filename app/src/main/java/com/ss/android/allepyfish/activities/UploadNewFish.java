package com.ss.android.allepyfish.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ss.android.allepyfish.R;
import com.ss.android.allepyfish.handlers.SQLiteHandler;
import com.ss.android.allepyfish.model.ContactInfo;
import com.ss.android.allepyfish.utils.AppConfig;
import com.ss.android.allepyfish.utils.AppController;
import com.ss.android.allepyfish.utils.NoDefaultSpinner;
import com.ss.android.allepyfish.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("ALL")
public class UploadNewFish extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG = UploadNewFish.class.getSimpleName();

    private Button btnSelect, register_button;
    private ImageView ivImage;
    private String userChoosenTask;

    private EditText dateAvailableEdt, locationEdt, qtyAvailabilityEdt, commentsEdt, marketRateEdt, rateQuotedEdt, pickUpTimedEdt, productNameEdt;

    int PICK_IMAGE_REQUEST = 9999;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1,REQUEST_CAMERA2 = 2,SELECT_FILE2 = 3, REQUEST_CAMERA3 = 4, REQUEST_CAMERA4 =6,SELECT_FILE3 = 5, SELECT_FILE4 = 7;
    
    Bitmap bitmap1 = null;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    String firmNameStr;
    String contactNoStr;

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    ImageView selectImage,imageViewCST,panImageView,canceledChequeImageView;   
    
    private Spinner locationNameSpinner;
    private Spinner fishItemsSpinner;
    private Spinner fishItemsEnglishSpinner;

    SQLiteHandler db;

    String selectedStateStr;
    String selectedFishLocalItem;

    ArrayAdapter<CharSequence> adapterTraderDeal;
    ArrayAdapter<CharSequence> adapterStateDeal;

    String englishFishNameSelectedStr;

    private Bitmap bitmap;
    private Bitmap bitmapCST,bitmapPan,bitmapChk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_fish);

        productNameEdt = (EditText) findViewById(R.id.productNameEdt);
        locationEdt = (EditText) findViewById(R.id.locationEdt);
        dateAvailableEdt = (EditText) findViewById(R.id.dateAvailableEdt);
        qtyAvailabilityEdt = (EditText) findViewById(R.id.qtyAvailabilityEdt);
        marketRateEdt = (EditText) findViewById(R.id.countPerKgEdt);
        rateQuotedEdt = (EditText) findViewById(R.id.rateQuotedEdt);
        pickUpTimedEdt = (EditText) findViewById(R.id.pickUpTimedEdt);
        commentsEdt = (EditText) findViewById(R.id.commentsEdt);


        locationNameSpinner = (Spinner) findViewById(R.id.locationNameSpinner);
        fishItemsSpinner = (Spinner) findViewById(R.id.fishItemsSpinner);

        adapterTraderDeal = ArrayAdapter.createFromResource(this, R.array.indian_states, android.R.layout.simple_spinner_item);
        adapterStateDeal = ArrayAdapter.createFromResource(this, R.array.english_fish_name, android.R.layout.simple_spinner_item);
        locationNameSpinner.setPrompt("Uploading from");

        locationNameSpinner.setAdapter(
                new NoDefaultSpinner(
                        adapterTraderDeal,
                        R.layout.fish_upload_prompt_text,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));

        db = new SQLiteHandler(getApplicationContext());

        Log.i("Reading: ", "Reading all contacts..");
        List<ContactInfo> contacts = db.getAllContacts();

        for (ContactInfo cn : contacts) {
            String log = "Id: "+cn.getId()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getPhone_no();
            // Writing Contacts to log
            Log.d("Name: ", log);
            firmNameStr = cn.getName();
            contactNoStr = cn.getPhone_no();
        }
        Log.i("Reading: ", "Reading all contacts..getPhone_no ... LandingScreen ..."+firmNameStr);

        locationNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                selectedStateStr = locationNameSpinner.getSelectedItem().toString().trim();

                if(position == 1){
                    selectedStateStr = String.valueOf(adapterTraderDeal.getItem(0));
                    Log.i("","Position Value "+selectedStateStr);
                    Resources res = getResources();

                    String[] biharStateValues = res.getStringArray(R.array.andhra_fish);

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UploadNewFish.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
//                    locationNameSpinner.setAdapter(dataAdapter);
                    fishItemsSpinner.setAdapter(dataAdapter);
                }
                if(position == 4){
                    selectedStateStr = String.valueOf(adapterTraderDeal.getItem(0));
                    Log.i("","Position Value "+selectedStateStr);
                    Resources res = getResources();

                    String[] biharStateValues = res.getStringArray(R.array.tamil_fish);

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UploadNewFish.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
//                    locationNameSpinner.setAdapter(dataAdapter);
                    fishItemsSpinner.setAdapter(dataAdapter);
                }

                if(position == 5){
                    selectedStateStr = String.valueOf(adapterTraderDeal.getItem(0));
                    Log.i("","Position Value "+selectedStateStr);
                    Resources res = getResources();

                    String[] biharStateValues = res.getStringArray(R.array.telangana_fish);

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UploadNewFish.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
//                    locationNameSpinner.setAdapter(dataAdapter);
                    fishItemsSpinner.setAdapter(dataAdapter);
                }
                if(position == 2){
                    selectedStateStr = String.valueOf(adapterTraderDeal.getItem(0));
                    Log.i("","Position Value "+selectedStateStr);
                    Resources res = getResources();

                    String[] biharStateValues = res.getStringArray(R.array.kannada_fish);

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UploadNewFish.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
//                    locationNameSpinner.setAdapter(dataAdapter);
                    fishItemsSpinner.setAdapter(dataAdapter);
                }

                if(position == 3){
                    selectedStateStr = String.valueOf(adapterTraderDeal.getItem(0));
                    Log.i("","Position Value "+selectedStateStr);
                    Resources res = getResources();

                    String[] biharStateValues = res.getStringArray(R.array.kerala_fish);

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UploadNewFish.this,
                            android.R.layout.simple_spinner_item, biharStateValues);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
//                    locationNameSpinner.setAdapter(dataAdapter);
                    fishItemsSpinner.setAdapter(dataAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fishItemsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedFishLocalItem = fishItemsSpinner.getSelectedItem().toString();

                Log.i("Selected Value in Spinner 2"," Item Fish Selected Value Fish :: "+selectedFishLocalItem);

                if(position == 0){
                    englishFishNameSelectedStr ="Anchovies";
                }
                if(position == 1){
                    englishFishNameSelectedStr = "Barracuda";
                }
                if(position == 2){
                    englishFishNameSelectedStr = "Bluefin Travelly";
                }
                if(position == 3){
                    englishFishNameSelectedStr = "Bombay Duck";
                }
                if(position == 4){
                    englishFishNameSelectedStr = "Butterfish";
                }
                if(position == 5){
                    englishFishNameSelectedStr = "Catfish";
                }

                Log.i("Selected Value in Spinner 2"," Item Selected Value :: "+englishFishNameSelectedStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dateAvailableEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });


//        dateAvailableEdt.setEnabled(false);
//        dateAvailableEdt.setClickable(true);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        register_button = (Button) findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(UploadNewFish.this,MainActivity.class));

                String locationStr = locationEdt.getText().toString().trim();
                String dateAvailableStr = dateAvailableEdt.getText().toString().trim();
                String qtyAvailabilityStr = qtyAvailabilityEdt.getText().toString().trim();
                String commentsStr = commentsEdt.getText().toString().trim();
                String marketRateStr = marketRateEdt.getText().toString().trim();
                String rateQuotedStr = rateQuotedEdt.getText().toString().trim();
                String pickUpTimedStr = pickUpTimedEdt.getText().toString().trim();
                String productNameStr = productNameEdt.getText().toString().trim();

                uploadNewFishDetails(selectedFishLocalItem, dateAvailableStr, qtyAvailabilityStr, commentsStr, marketRateStr, rateQuotedStr, pickUpTimedStr, productNameStr);
                if (bitmap == null) {
//                        Toast.makeText(getApplicationContext(),"Check First Image",Toast.LENGTH_LONG).show();
                    Log.e("Value 1", "Image Not Slected 1");
                } else {
                    uploadImage();
                }
                if (bitmapCST == null) {
//                        Toast.makeText(getApplicationContext(),"Check Second Image",Toast.LENGTH_LONG).show();
                    Log.e("Value 1", "Image Not Slected 2");
                } else {

                    uploadImage2();
                }
//                    uploadImage2();
                if (bitmapPan == null) {
//                        Toast.makeText(getApplicationContext(),"Check Second Image",Toast.LENGTH_LONG).show();
                    Log.e("Value 1", "Image Not Slected 3");
                } else {
                    uploadImage3();
                }
                if (bitmapChk == null) {
                    Log.e("Value 1", "Image Not Slected 4");
//                        Toast.makeText(getApplicationContext(),"Check Second Image",Toast.LENGTH_LONG).show();
                } else {
                    uploadImage4();
                }
            }
        });

        selectImage = (ImageView)findViewById(R.id.imageViewConnect);
        selectImage.setOnClickListener(this);

        imageViewCST = (ImageView)findViewById(R.id.imageViewCST);
        imageViewCST.setOnClickListener(this);

        panImageView = (ImageView)findViewById(R.id.panImageView);
        panImageView.setOnClickListener(this);

        canceledChequeImageView = (ImageView)findViewById(R.id.canceledChequeImageView);
        canceledChequeImageView.setOnClickListener(this);
    }



    private void uploadNewFishDetails(final String locationNameStr, final String dateAvailableStr, final String qtyAvailabilityStr, final String commentsStr, final String marketRateStr, final String rateQuotedStr, final String pickUpTimedStr, final String productNameStr) {

        String tag_string_req = "req_register";

//        pDialog.setMessage("Registering ...");
        final String is_working = "0";
//        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPLOAD_PRODUCT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("product_type");
                        String phone_no = user.getString("phone_no");
                        String is_Working = user.getString("city");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
//                        db.addUser(name, phone_no, uid, is_Working, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
//                        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
//                        startActivity(intent);
//                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {
            //            $name, $email, $phone_no, $password, $city, $address_1, $address_2, $profile_pic_url


            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                System.out.println("uuid = " + uuid);
//                String , String dateAvailableStr, String , String , String , String , String , final String

                Map<String, String> params = new HashMap<String, String>();
                params.put("unique_id", uuid);
//                params.put("product_local_name", productNameStr);
                params.put("product_local_name",locationNameStr );
                params.put("product_english_name", englishFishNameSelectedStr);
                params.put("quantity", qtyAvailabilityStr);
                params.put("product_available_from", dateAvailableStr);
                params.put("comments", commentsStr);
                params.put("product_location",selectedStateStr );
                params.put("market_rate", marketRateStr);
                params.put("rate_quoted", rateQuotedStr);
                params.put("pick_up_time", pickUpTimedStr);
                params.put("created_by", firmNameStr);
                params.put("contact_no", contactNoStr);
                params.put("approved_Status", "Pending");
                params.put("approved_by", "pending");
                params.put("product_pic1", "http://192.168.1.4/alleppyfish/uploads/"+firmNameStr+"_1.png");
                params.put("product_pic2", "http://192.168.1.4/alleppyfish/uploads/"+firmNameStr+"_2.png");
                params.put("product_pic3", "http://192.168.1.4/alleppyfish/uploads/"+firmNameStr+"_3.png");
                params.put("product_pic4", "http://192.168.1.4/alleppyfish/uploads/"+firmNameStr+"_4.png");
//                params.put("login_type",login_type);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

//    public void setDate(View view) {
//        showDialog(999);
//        Toast.makeText(getApplicationContext(), "ca",
//                Toast.LENGTH_SHORT)
//                .show();
//    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private void showDate(int year, int i, int day) {
        dateAvailableEdt.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadNewFish.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(UploadNewFish.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
            if (requestCode == SELECT_FILE2) {
                onSelectFromGalleryResult2(data);
            } else if (requestCode == REQUEST_CAMERA2) {
                onCaptureImageResult2(data);
            }
            if (requestCode == SELECT_FILE3) {
                onSelectFromGalleryResult3(data);
            } else if (requestCode == REQUEST_CAMERA3) {
                onCaptureImageResult3(data);
            }
            if (requestCode == SELECT_FILE4) {
                onSelectFromGalleryResult4(data);
            } else if (requestCode == REQUEST_CAMERA4) {
                onCaptureImageResult4(data);
            }
        }
    }

    private void onSelectFromGalleryResult2(Intent data) {
        if (data != null) {
            try {
                bitmapCST = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onSelectFromGalleryResult3(Intent data) {
        if (data != null) {
            try {
                bitmapPan = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onSelectFromGalleryResult4(Intent data) {
        if (data != null) {
            try {
                bitmapChk = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        selectImage.setImageBitmap(bitmap);
    }

    private void onCaptureImageResult2(Intent data) {
        bitmapCST = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmapCST.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageViewCST.setImageBitmap(bitmapCST);
    }
    private void onCaptureImageResult3(Intent data) {
        bitmapPan = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmapPan.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        panImageView.setImageBitmap(bitmapPan);
    }

    private void onCaptureImageResult4(Intent data) {
        bitmapChk = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmapChk.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        canceledChequeImageView.setImageBitmap(bitmapChk);
    }

    private void onSelectFromGalleryResult(Intent data) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageViewConnect:

                selectImageLoad();
                break;

            case R.id.imageViewCST:

                selectImageLoad2();
                break;
            case R.id.panImageView:
                selectImageLoad3();
                break;

            case R.id.canceledChequeImageView:
                selectImageLoad4();
                break;
        }
    }

    private void selectImageLoad4() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

//        marshMallowPermission = new MarshMallowPermission(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadNewFish.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(UploadNewFish.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
//                        galleryIntent();
                        cameraIntent4();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent4();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void selectImageLoad3() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

//        marshMallowPermission = new MarshMallowPermission(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadNewFish.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(UploadNewFish.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
//                        galleryIntent();
                        cameraIntent3();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent3();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }




    private void selectImageLoad2() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

//        marshMallowPermission = new MarshMallowPermission(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadNewFish.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(UploadNewFish.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
//                        galleryIntent();
                        cameraIntent2();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent2();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void uploadImage2(){

        String UPLOAD_URL = "http://192.168.1.4/alleppyfish/upload.php";
//        String UPLOAD_URL = "http://erp.grammisolutions.com/grammi/upload.php";
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
//                        Toast.makeText(AddProfile.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                        Log.i("Error 123", "Error in Volley " + volleyError.getMessage().toString().trim());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
//                String image = null;
//                if (bitmapCST == null){
//                    Log.i("Error Bitmap CST", "Error Bitmap CST ");
//                }else {
//                     image = getStringImageCST(bitmapCST);
//                }

                String image = getStringImageCST(bitmapCST);

                //Getting Image Name
//                    String name = firstNameEdt.getText().toString().trim();
                String name = firmNameStr + "_cst_no";



                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }public void uploadImage3(){

        String UPLOAD_URL = "http://192.168.1.4/alleppyfish/upload.php";
//        String UPLOAD_URL = "http://erp.grammisolutions.com/grammi/upload.php";
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
//                        Toast.makeText(AddProfile.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
//                        Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                        Log.i("Error 123", "Error in Volley " + volleyError.getMessage().toString().trim());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImagePan(bitmapPan);

                //Getting Image Name
//                    String name = firstNameEdt.getText().toString().trim();
                String name = firmNameStr + "_pan_no";

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
    public void uploadImage4(){

        String UPLOAD_URL = "http://192.168.1.4/alleppyfish/upload.php";
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
//                        Toast.makeText(AddProfile.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                        Log.i("Error 123", "Error in Volley " + volleyError.getMessage().toString().trim());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImageChk(bitmapChk);

                //Getting Image Name
//                    String name = firstNameEdt.getText().toString().trim();
                String name = firmNameStr + "_cancel_cheque";

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }


    private String getStringImageCST(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapCST.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private String getStringImageChk(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapChk.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private String getStringImagePan(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapPan.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {
        //Showing the progress dialog
        String UPLOAD_URL = "http://192.168.1.4/alleppyfish/upload.php";
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
//                        Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();

                        Log.i("Error 123", "Error in Volley " + volleyError.getMessage().toString().trim());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
//                    String name = firstNameEdt.getText().toString().trim();
                String name = firmNameStr + "_partner_photo";

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void selectImageLoad() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

//        marshMallowPermission = new MarshMallowPermission(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadNewFish.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(UploadNewFish.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
//                        galleryIntent();
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

   
    private void galleryIntent2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE2);
    }
    private void galleryIntent3() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE3);
    }
    private void galleryIntent4() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE4);
    }

   

    private void cameraIntent2() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA2);
    }

    private void cameraIntent3() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA3);
    }

    private void cameraIntent4() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA4);
    }

}
