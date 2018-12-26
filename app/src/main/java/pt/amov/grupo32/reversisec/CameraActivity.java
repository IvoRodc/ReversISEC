package pt.amov.grupo32.reversisec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import pt.amov.grupo32.reversisec.ReversISEC.SharedPreferences.GlobalProfile;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private int camIndex;
    private static final String TAG = "CameraActivity";
    private TextureView textureView;
    private static final SparseArray ORIENTATIONS_FRONT = new SparseArray();
    static {
        ORIENTATIONS_FRONT.append(Surface.ROTATION_0, 270);
        ORIENTATIONS_FRONT.append(Surface.ROTATION_90, 0);
        ORIENTATIONS_FRONT.append(Surface.ROTATION_180, 90);
        ORIENTATIONS_FRONT.append(Surface.ROTATION_270, 180);
    }

    private static final SparseArray ORIENTATIONS_BACK = new SparseArray();
    static {
        ORIENTATIONS_BACK.append(Surface.ROTATION_0, 90);
        ORIENTATIONS_BACK.append(Surface.ROTATION_90, 180);
        ORIENTATIONS_BACK.append(Surface.ROTATION_180, 270);
        ORIENTATIONS_BACK.append(Surface.ROTATION_270, 0);
    }

    private String cameraID;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSession;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder capBuilder;
    private Size imageSize;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private CameraManager manager;

    private GlobalProfile globalProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        textureView = findViewById(R.id.texture);
        textureView.setSurfaceTextureListener(textureListener);
        globalProfile= (GlobalProfile)getApplicationContext();
    }


    void goBack(View v){
        finish();
    }

    void goTirarFoto(View v){
        tirarFotografia();
    }

    void goSwitchCamera(View v){
        try {
            switchCamera();
        } catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //ABRIR A CAMERA AQUI
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            //ISTO É CHAMADO QUANDO A CAMERA É ABERTA
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
        }
    };

    final CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            createCameraPreview();
        }
    };

    protected void startBackgroudnThread(){
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread(){
        mBackgroundThread.quitSafely();
        try{
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    protected void createCameraPreview(){
        try{
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(imageSize.getWidth(), imageSize.getHeight());
            Surface surface = new Surface(texture);
            capBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            capBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    //A CAMERA JÁ ESTÁ FECHADA
                    if(cameraDevice == null){
                        return;
                    }
                    cameraCaptureSession = session;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Toast.makeText(CameraActivity.this, "Configurações mudaram", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    private void openCamera(){
        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try{
            cameraID = manager.getCameraIdList()[camIndex];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraID);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            imageSize = map.getOutputSizes(SurfaceTexture.class)[0];
            //PEDIR PERMISSÃO PARA UTILIZAR A CAMERA
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
            }
            manager.openCamera(cameraID, stateCallback, null);


        } catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    protected void updatePreview(){
        if(cameraDevice == null){
            Log.e(TAG, "updatePreview error");
        }
        capBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try{
            cameraCaptureSession.setRepeatingRequest(capBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    private void closeCamera(){
        if(cameraDevice != null){
            cameraDevice.close();
            cameraDevice = null;
        }
        if(imageReader != null){
            imageReader.close();
            imageReader = null;
        }
    }

    private void reopenCamera(){
        if(textureView.isAvailable()){
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    private void switchCamera() throws CameraAccessException{
        if(cameraID.equals("0")){
            camIndex = 1;
            closeCamera();
            reopenCamera();
        } else if(cameraID.equals("1")){
            camIndex = 0;
            closeCamera();
            reopenCamera();
        }
    }

    protected void tirarFotografia(){
        if(cameraDevice == null){
            return;
        }

        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try{
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpedSizes = null;
            if(characteristics != null){
                jpedSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;

            if(jpedSizes!= null && 0 < jpedSizes.length){
                width = jpedSizes[0].getWidth();
                height = jpedSizes[0].getHeight();
            }

            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outpSurfaces = new ArrayList<>(2);
            outpSurfaces.add(reader.getSurface());
            outpSurfaces.add(new Surface(textureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            int rotationScreen = getWindowManager().getDefaultDisplay().getRotation();
            int rotation;
            if(camIndex == 1){
                rotation = (int)ORIENTATIONS_FRONT.get(rotationScreen);
            } else {
                rotation = (int)ORIENTATIONS_BACK.get(rotationScreen);
            }
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, rotation);

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try{
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];

                        buffer.get(bytes);
                        ///GUARDAR FOTO NO SHAREDPREFERENCES
                        globalProfile.saveFotografia(bytes);
                    } finally {
                        if(image != null){
                            finish();
                            image.close();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    createCameraPreview();
                }
            };

            cameraDevice.createCaptureSession(outpSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try{
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, mBackgroundHandler);


        } catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_DENIED){
            //SE PERMISSÃO NEGADA FECHA A ATIVIDADE
            finish();
        }
    }

    @Override
    protected void onResume() {
        camIndex = 1;
        super.onResume();
        startBackgroudnThread();
        if(textureView.isAvailable()){
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopBackgroundThread();
    }
}
