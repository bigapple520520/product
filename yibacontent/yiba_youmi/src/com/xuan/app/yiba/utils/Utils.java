package com.xuan.app.yiba.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.TextUtils;

/**
 * 工具类
 * 
 * @author xuan
 */
public abstract class Utils {

    /**
     * 判断网络
     * 
     * @param context
     * @return
     */
    public static boolean hasNetwork(Context context) {
        ConnectivityManager connectManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
        if (networkInfo == null || !connectManager.getActiveNetworkInfo().isAvailable()
                || !connectManager.getActiveNetworkInfo().isConnected()) {
            return false;
        }

        return true;
    }

    /**
     * 显示提示
     * 
     * @param title
     */
    public static void showDialog(String title, ProgressDialog progressDialog) {
        progressDialog.setTitle(title);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * 隐藏提示
     * 
     * @param handler
     */
    public static void dismissDialog(Handler handler, final ProgressDialog progressDialog) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 显示alert
     * 
     * @param title
     * @param message
     * @param buttonText
     */
    public static void displayAlert(Context context, String title, String message, String buttonText) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setTitle(title).setMessage(message).create();

        alertDialog.show();
    }

    /**
     * Http请求
     * 
     * @param url
     * @return
     * @throws IOException
     */
    public static String requestURL(String url) throws IOException {
        String result = null;
        BufferedReader reader = null;
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setConnectTimeout(1000 * 12);
            connection.setReadTimeout(1000 * 12);

            connection.connect();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"), 8 * 1024);
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            result = stringBuilder.toString();
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e1) {
                    // Ignore
                }
            }
        }

        return result;
    }

    /**
     * 获取译文
     * 
     * @param fromWord
     * @param explainsList
     * @return
     */
    public static String getTranslateWord(String fromWord, List<String> explainsList) {
        String result = "";
        String resultTemp = "";

        if (TextUtils.isEmpty(fromWord)) {
            return result;
        }

        try {
            String fromWordEncoder = URLEncoder.encode(fromWord);
            String url = "http://fanyi.youdao.com/openapi.do?keyfrom=xuanner&key=1726129807&type=data&doctype=json&version=1.1&q="
                    + fromWordEncoder;
            resultTemp = Utils.requestURL(url);
        }
        catch (Exception e) {
            // Ignore
        }

        // 从Json串中解析出译文
        try {
            JSONObject obj = new JSONObject(resultTemp);
            JSONArray array = obj.getJSONArray("translation");
            result = array.get(0).toString();

            // 解析出解析的翻译
            if (obj.has("basic")) {
                JSONObject basic = obj.getJSONObject("basic");
                JSONArray explains = basic.getJSONArray("explains");
                for (int i = 0; i < explains.length(); i++) {
                    explainsList.add(explains.get(i).toString());
                }
            }
        }
        catch (Exception e) {
            result = "不好意思出错了，请联系我的主人：15858178400";
        }

        return result;
    }

}
