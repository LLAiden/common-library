package com.example.commonlibrary;

import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

    public static String getFilePath(String fileSuffix) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd/HH-mm-ss", Locale.CHINA);
        return getRootPath() +
                mSimpleDateFormat.format(new Date()) +
                fileSuffix;
    }

    @NonNull
    public static String getRootPath() {
        String externalStorageState = Environment.getExternalStorageDirectory().getPath();
        return externalStorageState +
                File.separator +
                "ACommonLibrary" +
                File.separator;
    }
}
