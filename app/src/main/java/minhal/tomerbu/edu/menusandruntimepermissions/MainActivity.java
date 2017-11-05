package minhal.tomerbu.edu.menusandruntimepermissions;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //findViewById for the toolbar (toolbar is the new menu)

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

        //setActionBar(toolbar)
        setSupportActionBar(toolbar);

    }


    //create the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Callback for menu items click!
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Toast.makeText(this, "Goto Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_call:
                call();
                return true;
            case R.id.action_photo:
                //Some view that is inside the coordinator layout
                //Snackbar.make(toolbar, "Hi, Snack", BaseTransientBottomBar.LENGTH_LONG/*view:?*/).show();
                takePicture();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final int RC_CAMERA = 2;

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //if we don't have permission -> request permission and return from the method (don't continue!)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, RC_CAMERA);
            return;
            //return
        }
        startActivityForResult(intent, RC_CAMERA);
    }


    //The Result?
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_CAMERA && resultCode == RESULT_OK){
            //camera, positive!

            Bitmap userPhoto = data.getParcelableExtra("data");
            ivPhoto.setImageBitmap(userPhoto);
        }

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == RC_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            takePicture();
        }




        if (requestCode == RC_CALL && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            call();
        } else if (requestCode == RC_CALL && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "No Permission", Toast.LENGTH_SHORT).show();
        }
    }



    private final static int RC_CALL = 1;

    private void call() {
        Uri uri = Uri.parse("tel:050712312");
        Intent intent = new Intent(Intent.ACTION_CALL, uri);

        //if no permission? Return away from this method , but first we ask for permission.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, RC_CALL);
            return;
        }
        startActivity(intent);
    }
}




