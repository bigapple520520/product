/* 
 * @(#)CropImage.java    Created on 2014-4-11
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.tuya.view.crop;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.xuan.tuya.R;

/**
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-11 下午3:54:58 $
 */
public class CropImage extends Activity {
    private static final String TAG = "CropImage";
    private Bitmap.CompressFormat mSaveFormat = Bitmap.CompressFormat.JPEG; // only used with mSaveUri
    private Uri mSaveUri = null;

    private int mAspectX = 1;
    private int mAspectY = 1;
    private int mOutputX = 500;
    private int mOutputY = 500;
    private boolean mScale = true;
    private boolean mScaleUp = true;
    private boolean mDoFaceDetection = true;

    private boolean mCircleCrop = false;
    public boolean mWaitingToPick;
    public boolean mSaving;

    CropImageView mImageView;
    ContentResolver mContentResolver;

    Bitmap mBitmap;
    Bitmap mCroppedImage;
    HighlightView mCrop;

    private void fillCanvas(int width, int height, Canvas c) {
        Paint paint = new Paint();
        paint.setColor(0x00000000); // pure alpha
        paint.setStyle(android.graphics.Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        c.drawRect(0F, 0F, width, height, paint);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.cropimage);
        mContentResolver = getContentResolver();
        mImageView = (CropImageView) findViewById(R.id.image);

        try {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mImageView.setImageBitmapResetBase(mBitmap, true, true);
                    if (mImageView.getScale() == 1F) {
                        mImageView.center(true, true, false);
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Bitmap b = mBitmap;
                            if (Config.isDebug) {
                                Log.v(TAG, "back from mImage.fullSizeBitmap(500) with bitmap of size " + b.getWidth()
                                        + " / " + b.getHeight());
                            }
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (b != mBitmap && b != null) {
                                        mBitmap = b;
                                        mImageView.setImageBitmapResetBase(b, true, false);
                                    }
                                    if (mImageView.getScale() == 1F) {
                                        mImageView.center(true, true, false);
                                    }

                                    new Thread(mRunFaceDetection).start();
                                }
                            });
                        }
                    }).start();
                }
            }, 100);
        }
        catch (Exception e) {
            Log.e(TAG, "Failed to load bitmap", e);
            finish();
        }

        // 取消
        findViewById(R.id.discard).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 保存
        findViewById(R.id.save).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO this code needs to change to use the decode/crop/encode single
                // step api so that we don't require that the whole (possibly large) bitmap
                // doesn't have to be read into memory
                mSaving = true;
                if (mCroppedImage == null) {
                    if (mCrop == null) {
                        if (Config.isDebug) {
                            Log.v(TAG, "no cropped image...");
                        }
                        return;
                    }

                    Rect r = mCrop.getCropRect();

                    int width = r.width();
                    int height = r.height();

                    // if we're circle cropping we'll want alpha which is the third param here
                    mCroppedImage = Bitmap.createBitmap(width, height, mCircleCrop ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
                    Canvas c1 = new Canvas(mCroppedImage);
                    c1.drawBitmap(mBitmap, r, new Rect(0, 0, width, height), null);

                    if (mCircleCrop) {
                        // OK, so what's all this about?
                        // Bitmaps are inherently rectangular but we want to return something
                        // that's basically a circle. So we fill in the area around the circle
                        // with alpha. Note the all important PortDuff.Mode.CLEAR.
                        Canvas c = new Canvas(mCroppedImage);
                        android.graphics.Path p = new android.graphics.Path();
                        p.addCircle(width / 2F, height / 2F, width / 2F, android.graphics.Path.Direction.CW);
                        c.clipPath(p, Region.Op.DIFFERENCE);

                        fillCanvas(width, height, c);
                    }
                }

                /* If the output is required to a specific size then scale or fill */
                if (mOutputX != 0 && mOutputY != 0) {

                    if (mScale) {

                        /* Scale the image to the required dimensions */
                        // mCroppedImage = ImageLoader
                        // .transform(new Matrix(), mCroppedImage, mOutputX, mOutputY, mScaleUp);
                    }
                    else {

                        /*
                         * Don't scale the image crop it to the size requested. Create an new image with the cropped
                         * image in the center and the extra space filled.
                         */

                        /* Don't scale the image but instead fill it so it's the required dimension */
                        Bitmap b = Bitmap.createBitmap(mOutputX, mOutputY, Bitmap.Config.RGB_565);
                        Canvas c1 = new Canvas(b);

                        /* Draw the cropped bitmap in the center */
                        Rect r = mCrop.getCropRect();
                        int left = (mOutputX / 2) - (r.width() / 2);
                        int top = (mOutputY / 2) - (r.width() / 2);
                        c1.drawBitmap(mBitmap, r, new Rect(left, top, left + r.width(), top + r.height()), null);

                        /* Set the cropped bitmap as the new bitmap */
                        mCroppedImage = b;
                    }
                }

                Bundle myExtras = getIntent().getExtras();

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if (mSaveUri != null) {
                            OutputStream outputStream = null;
                            try {
                                String scheme = mSaveUri.getScheme();
                                if (scheme.equals("file")) {
                                    outputStream = new FileOutputStream(mSaveUri.toString().substring(
                                            scheme.length() + ":/".length()));
                                }
                                else {
                                    outputStream = mContentResolver.openOutputStream(mSaveUri);
                                }
                                if (outputStream != null) {
                                    mCroppedImage.compress(mSaveFormat, 75, outputStream);
                                }

                            }
                            catch (IOException ex) {
                                if (Config.isDebug) {
                                    Log.v(TAG, "got IOException " + ex);
                                }
                            }
                            finally {
                                if (outputStream != null) {
                                    try {
                                        outputStream.close();
                                    }
                                    catch (IOException ex) {

                                    }
                                }
                            }
                            Bundle extras = new Bundle();
                            setResult(RESULT_OK, (new Intent()).setAction(mSaveUri.toString()).putExtras(extras));
                        }
                        else {
                            Bundle extras = new Bundle();
                            extras.putString("rect", mCrop.getCropRect().toString());

                            // this is the "new image" case
                            // java.io.File oldPath = new java.io.File(mImage.getDataPath());
                            java.io.File oldPath = null;
                            java.io.File directory = new java.io.File(oldPath.getParent());

                            int x = 0;
                            String fileName = oldPath.getName();
                            fileName = fileName.substring(0, fileName.lastIndexOf("."));

                            while (true) {
                                x += 1;
                                String candidate = directory.toString() + "/" + fileName + "-" + x + ".jpg";
                                if (Config.isDebug) {
                                    Log.v(TAG, "candidate is " + candidate);
                                }
                                boolean exists = (new java.io.File(candidate)).exists();
                                if (!exists) {
                                    break;
                                }
                            }

                            try {
                                // Uri newUri = ImageManager.instance().addImage(CropImage.this, getContentResolver(),
                                // mImage.getTitle(), mImage.getDescription(), mImage.getDateTaken(), null, // TODO
                                // // this
                                // // null
                                // // is
                                // // going
                                // // to
                                // // cause
                                // // us
                                // // to
                                // // lose
                                // // the
                                // // location
                                // // (gps)
                                // 0, // TODO this is going to cause the orientation to reset
                                // directory.toString(), fileName + "-" + x + ".jpg");

                                // ImageManager.IAddImage_cancelable cancelable = ImageManager.instance().storeImage(
                                // newUri, CropImage.this, getContentResolver(), 0, // TODO fix
                                // // this
                                // // orientation
                                // mCroppedImage, null);
                                //
                                // cancelable.get();
                                // setResult(RESULT_OK, (new Intent()).setAction(newUri.toString()).putExtras(extras));
                            }
                            catch (Exception ex) {
                                // basically ignore this or put up
                                // some ui saying we failed
                            }
                        }
                        finish();
                    }
                };
                Thread t = new Thread(r);
                t.start();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    Handler mHandler = new Handler();

    Runnable mRunFaceDetection = new Runnable() {
        float mScale = 1F;
        RectF mUnion = null;
        Matrix mImageMatrix;
        FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
        int mNumFaces;

        private void handleFace(FaceDetector.Face f) {
            PointF midPoint = new PointF();

            int r = ((int) (f.eyesDistance() * mScale)) * 2;
            f.getMidPoint(midPoint);
            midPoint.x *= mScale;
            midPoint.y *= mScale;

            int midX = (int) midPoint.x;
            int midY = (int) midPoint.y;

            HighlightView hv = makeHighlightView();

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            RectF faceRect = new RectF(midX, midY, midX, midY);
            faceRect.inset(-r, -r);
            if (faceRect.left < 0) {
                faceRect.inset(-faceRect.left, -faceRect.left);
            }

            if (faceRect.top < 0) {
                faceRect.inset(-faceRect.top, -faceRect.top);
            }

            if (faceRect.right > imageRect.right) {
                faceRect.inset(faceRect.right - imageRect.right, faceRect.right - imageRect.right);
            }

            if (faceRect.bottom > imageRect.bottom) {
                faceRect.inset(faceRect.bottom - imageRect.bottom, faceRect.bottom - imageRect.bottom);
            }

            hv.setup(mImageMatrix, imageRect, faceRect, mCircleCrop, mAspectX != 0 && mAspectY != 0);

            if (mUnion == null) {
                mUnion = new RectF(faceRect);
            }
            else {
                mUnion.union(faceRect);
            }

            mImageView.add(hv);
        }

        private HighlightView makeHighlightView() {
            return new HighlightView(mImageView);
        }

        private void makeDefault() {
            HighlightView hv = makeHighlightView();

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            int cropHeight = cropWidth;

            if (mAspectX != 0 && mAspectY != 0) {
                if (mAspectX > mAspectY) {
                    cropHeight = cropWidth * mAspectY / mAspectX;
                    // Log.v(TAG, "adjusted cropHeight to " + cropHeight);
                }
                else {
                    cropWidth = cropHeight * mAspectX / mAspectY;
                    // Log.v(TAG, "adjusted cropWidth to " + cropWidth);
                }
            }

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop, mAspectX != 0 && mAspectY != 0);
            mImageView.add(hv);
        }

        private Bitmap prepareBitmap() {
            if (mBitmap == null) {
                return null;
            }

            // scale the image down for faster face detection
            // 256 pixels wide is enough.
            if (mBitmap.getWidth() > 256) {
                mScale = 256.0F / mBitmap.getWidth();
            }
            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            Bitmap faceBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix,
                    true);
            return faceBitmap;
        }

        @Override
        public void run() {
            mImageMatrix = mImageView.getImageMatrix();
            Bitmap faceBitmap = prepareBitmap();

            mScale = 1.0F / mScale;
            if (faceBitmap != null && mDoFaceDetection) {
                FaceDetector detector = new FaceDetector(faceBitmap.getWidth(), faceBitmap.getHeight(), mFaces.length);
                mNumFaces = detector.findFaces(faceBitmap, mFaces);
                if (Config.isDebug) {
                    Log.v(TAG, "numFaces is " + mNumFaces);
                }
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWaitingToPick = mNumFaces > 1;
                    if (mNumFaces > 0) {
                        for (int i = 0; i < mNumFaces; i++) {
                            handleFace(mFaces[i]);
                        }
                    }
                    else {
                        makeDefault();
                    }
                    mImageView.invalidate();
                    if (mImageView.mHighlightViews.size() == 1) {
                        mCrop = mImageView.mHighlightViews.get(0);
                        mCrop.setFocus(true);
                    }

                    if (mNumFaces > 1) {
                        // Toast t = Toast.makeText(CropImage.this, R.string.multiface_crop_help, Toast.LENGTH_SHORT);
                        // t.show();
                    }
                }
            });

        }
    };

    @Override
    public void onStop() {
        super.onStop();
    }

}
