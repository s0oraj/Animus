package blog.cosmos.home.animus;

import static org.opencv.core.Core.LUT;
import static org.opencv.core.CvType.CV_8UC1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.segmentation.Segmentation;
import com.google.mlkit.vision.segmentation.SegmentationMask;
import com.google.mlkit.vision.segmentation.Segmenter;
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AvatarMaker {


    Segmenter segmenter;

   ImageView imageView;
    Bitmap bitmap;
    Bitmap bitmap2;

    Button removeBg, animateImg;
    Activity activity;

      public AvatarMaker(Bitmap bitmap, ImageView imageView, Activity activity){
        this.bitmap = bitmap;
        this.imageView = imageView;
        this.activity=activity;
           bitmap2 = bitmap.copy(Bitmap.Config.ARGB_8888, true);

           OpenCVLoader.initDebug(); // if this isnt called then this error shows  java.lang.UnsatisfiedLinkError:   No implementation found for long org.opencv.core.Mat.n_Mat()
           //bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

          /*
                removeBackground();

                animateImage();

             changeImageBackground();

           */


    }

    public void animateImage() {

        cartoonImage();
    }

    public Bitmap changeImageBackground(){
          /*

        Bitmap bitmap1 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap1);
        bitmap1.eraseColor(getResources().getColor(R.color.teal_700));
        bitmap2.setWidth(((BitmapDrawable) imageView.getDrawable()).getBitmap().getWidth());
        bitmap2.setHeight(((BitmapDrawable) imageView.getDrawable()).getBitmap().getHeight());
        imageView.setImageBitmap(
                mergeToPinBitmap2(((BitmapDrawable) imageView.getDrawable()).getBitmap()
                        , bitmap2));

           */
        /*
        imageView.setImageBitmap(
                mergeToPinBitmap2(((BitmapDrawable) imageView.getDrawable()).getBitmap()
                        , bitmap1));

         */

        Bitmap bitmap1 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap1);
        bitmap1.eraseColor(activity.getResources().getColor(R.color.teal_700));
        imageView.setImageBitmap(
                mergeToPinBitmap2(((BitmapDrawable) imageView.getDrawable()).getBitmap()
                        , bitmap1));
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();




    }

    public Bitmap getBitmap(){
           return bitmap;
    }

    public void cartoonImage() {
        //  BitmapFactory.Options options = new BitmapFactory.Options();
        // options.inScaled = false; // Leaving it to true enlarges the decoded image size.
        //  Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.img, options);

        Bitmap original = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Mat img1 = new Mat();
        Utils.bitmapToMat(original, img1);
        Imgproc.cvtColor(img1, img1, Imgproc.COLOR_BGRA2BGR);

        Mat result = cartoon(img1, 80, 15, 10);

        Bitmap imgBitmap = Bitmap.createBitmap(result.cols(), result.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(result, imgBitmap);

        //  ImageView imageView = findViewById(R.id.opencvImg);
        imageView.setImageBitmap(imgBitmap);

    }


    Mat cartoon(Mat img, int numRed, int numGreen, int numBlue) {
        Mat reducedColorImage = reduceColors(img, numRed, numGreen, numBlue);

        Mat result = new Mat();
        Imgproc.cvtColor(img, result, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(result, result, 15);

        Imgproc.adaptiveThreshold(result, result, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 2);

        Imgproc.cvtColor(result, result, Imgproc.COLOR_GRAY2BGR);

        Log.d("PPP", result.height() + " " + result.width() + " " + reducedColorImage.type() + " " + result.channels());
        Log.d("PPP", reducedColorImage.height() + " " + reducedColorImage.width() + " " + reducedColorImage.type() + " " + reducedColorImage.channels());

        Core.bitwise_and(reducedColorImage, result, result);

        return result;
    }

    Mat reduceColors(Mat img, int numRed, int numGreen, int numBlue) {
        Mat redLUT = createLUT(numRed);
        Mat greenLUT = createLUT(numGreen);
        Mat blueLUT = createLUT(numBlue);

        List<Mat> BGR = new ArrayList<>(3);
        Core.split(img, BGR); // splits the image into its channels in the List of Mat arrays.

        LUT(BGR.get(0), blueLUT, BGR.get(0));
        LUT(BGR.get(1), greenLUT, BGR.get(1));
        LUT(BGR.get(2), redLUT, BGR.get(2));

        Core.merge(BGR, img);

        return img;
    }

    Mat createLUT(int numColors) {
        // When numColors=1 the LUT will only have 1 color which is black.
        if (numColors < 0 || numColors > 256) {
            System.out.println("Invalid Number of Colors. It must be between 0 and 256 inclusive.");
            return null;
        }

        Mat lookupTable = Mat.zeros(new Size(1, 256), CV_8UC1);

        int startIdx = 0;
        for (int x = 0; x < 256; x += 256.0 / numColors) {
            lookupTable.put(x, 0, x);

            for (int y = startIdx; y < x; y++) {
                if (lookupTable.get(y, 0)[0] == 0) {
                    lookupTable.put(y, 0, lookupTable.get(x, 0));
                }
            }
            startIdx = x;
        }
        return lookupTable;
    }

    /**
     * https://stackoverflow.com/questions/73357098/remove-background-of-image-with-ai-or-automatically-android-studio-with-100-acc
     */
    public void removeBackground() {

        SelfieSegmenterOptions options =
                new SelfieSegmenterOptions.Builder()
                        .setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE)
                        .build();

        Bitmap bitmapFromContentUri = bitmap;

        InputImage image = InputImage.fromBitmap(bitmap, 0);
        segmenter = Segmentation.getClient(options);
        segmenter.process(image)
                .addOnSuccessListener(new OnSuccessListener<SegmentationMask>() {
                    @Override
                    public void onSuccess(SegmentationMask segmentationMask) {

                        ByteBuffer buffer = segmentationMask.getBuffer();
                        int width = segmentationMask.getWidth();
                        int height = segmentationMask.getHeight();
                        Bitmap createBitmap = Bitmap.createBitmap(bitmapFromContentUri.getWidth(), bitmapFromContentUri.getHeight()
                                , bitmapFromContentUri.getConfig());


                        for (int y = 0; y < height; y++) {
                            for (int x = 0; x < width; x++) {
                                // Gets the confidence of the (x,y) pixel in the mask being in the foreground.
                                double d = (double) buffer.getFloat();
                                createBitmap.setPixel(x, y, Color.argb((int) ((1.0 - d) * 255.0), 0, 0, 0));
                            }
                        }

                        buffer.rewind();

                        Bitmap autoeraseimage = mergeToPinBitmap(bitmapFromContentUri, createBitmap);

                        if (autoeraseimage != null) {
                            imageView.setImageBitmap(autoeraseimage);
                            //bitmap= autoeraseimage;
                        } else {
                            //Toast.makeText(MainActivity.this, "Please Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                // ...
                               // Toast.makeText(getConte, "Listener Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });

    }

    public Bitmap mergeToPinBitmap(Bitmap bitmap, Bitmap bitmap2) {

        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawBitmap(bitmap, new Matrix(), null);
        canvas.drawBitmap(bitmap2, 0, 0, paint);
        paint.setXfermode(null);
        return createBitmap;

    }

    public Bitmap mergeToPinBitmap2(Bitmap secondImage, Bitmap firstImage) {

        Bitmap result = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight(), firstImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(firstImage, new Matrix(), null);
        canvas.drawBitmap(secondImage, 0, 0, null);
        return result;

    }




}
