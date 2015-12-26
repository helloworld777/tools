package com.example.uploaddemo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	String path = "/sdcard/lyw.txt";
	String urls = "http://10.10.3.81/goopai/LuServlet?flag=upload";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// JSONObject object=new JSONObject(path);

		// JsonObject jsonObject=new JsonObject();
		//
		// jsonObject.

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
//				uploadFile();
				try {
					upload(path);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}


	public boolean upload(String filepath) throws Exception {

		final String ENCORDING = "UTF-8";
		String boundary = "---------------------------7db1c523809b2";// +java.util.UUID.randomUUID().toString();//
		// 分割线
		File file = new File(filepath);

		String fileName = new String("哈哈嗨".getBytes(), "ISO-8859-1");
		// 用来解析主机名和端口
		URL url = new URL(
				urls);
		// 用来开启连接
		StringBuilder sb = new StringBuilder();
		// 用来拼装请求


		// 文件部分
		sb.append("--" + boundary + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
				+ "lyw.txt" + "\"" + "\r\n");
		sb.append("Content-Type: application/octet-stream" + "\r\n");
		sb.append("\r\n");

		// 将开头和结尾部分转为字节数组，因为设置Content-Type时长度是字节长度
		byte[] before = sb.toString().getBytes(ENCORDING);
		byte[] after = ("\r\n--" + boundary + "--\r\n").getBytes(ENCORDING);

		// 打开连接, 设置请求头
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(10000);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);
		conn.setRequestProperty("Content-Length", before.length + file.length()
				+ after.length + "");

		conn.setDoOutput(true);
		conn.setDoInput(true);

		// 获取输入输出流
		OutputStream out = conn.getOutputStream();
		FileInputStream fis = new FileInputStream(file);
		// 将开头部分写出
		out.write(before);

		// 写出文件数据
		byte[] buf = new byte[1024 * 5];
		int len;
		while ((len = fis.read(buf)) != -1)
			out.write(buf, 0, len);

		// 将结尾部分写出
		out.write(after);

		InputStream in = conn.getInputStream();
		InputStreamReader isReader = new InputStreamReader(in);
		BufferedReader bufReader = new BufferedReader(isReader);
		String line = null;
		String data = "getResult=";
		while ((line = bufReader.readLine()) != null)
			data += line;
		Log.i("System.out", "result=" + data);
		boolean sucess = conn.getResponseCode() == 200;
		in.close();
		fis.close();
		out.close();
		conn.disconnect();

		return sucess;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
