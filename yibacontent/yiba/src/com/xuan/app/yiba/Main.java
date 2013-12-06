package com.xuan.app.yiba;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xuan.app.yiba.utils.Utils;

@SuppressLint("NewApi")
public class Main extends Activity {

	private Button button; // 走一个按钮
	private EditText fromWordEditText;// 输入
	private TextView toWordTextView;// 输出
	private TextView explain;
	private TextView explainWord;

	private final Handler handler = new Handler();
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		button = (Button) findViewById(R.id.button);
		fromWordEditText = (EditText) findViewById(R.id.fromWord);
		toWordTextView = (TextView) findViewById(R.id.toWord);
		explain = (TextView) findViewById(R.id.explain);
		explainWord = (TextView) findViewById(R.id.explainWord);

		progressDialog = new ProgressDialog(this);

		explainWord.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (TextUtils.isEmpty(explainWord.getText().toString())) {
					return false;
				}

				ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cm.setText(explainWord.getText().toString());
				Toast.makeText(Main.this, "亲，别的解释已复制了哦，去别地黏贴吧",
						Toast.LENGTH_SHORT).show();
				return true;
			}
		});

		toWordTextView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (TextUtils.isEmpty(toWordTextView.getText().toString())) {
					return false;
				}

				ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cm.setText(toWordTextView.getText().toString());
				Toast.makeText(Main.this, "亲，译文已复制了哦，去别地黏贴吧",
						Toast.LENGTH_SHORT).show();
				return true;
			}
		});

		button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 判断网络
				if (!Utils.hasNetwork(Main.this)) {
					Toast.makeText(Main.this, "亲，网络不可用哦", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				// 隐藏键盘输入
				InputMethodManager imm = (InputMethodManager) v.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
							0);
				}

				// 处理翻译
				final String fromWord = fromWordEditText.getEditableText()
						.toString();

				if (TextUtils.isEmpty(fromWord)) {
					Toast.makeText(Main.this, "输入内容不能为空", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				Utils.showDialog("我在奋力翻译中...", progressDialog);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							final List<String> explainsList = new ArrayList<String>();
							final String toWord = Utils.getTranslateWord(
									fromWord, explainsList);// 查询有道翻译API
							handler.post(new Runnable() {
								@Override
								public void run() {
									explain.setText("");
									explainWord.setText("");

									toWordTextView.setText(toWord);

									// 显示解释
									if (!explainsList.isEmpty()) {
										explain.setText("一些别的解释：");
										StringBuilder sb = new StringBuilder();
										for (String explainWord : explainsList) {
											sb.append(explainWord).append("，");
										}

										explainWord.setText(sb.toString()
												.substring(0, sb.length() - 1));
									}
								}
							});
						} finally {
							Utils.dismissDialog(handler, progressDialog);
						}
					}
				}).start();
			}
		});
	}

	// 菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 1, 0, "关于");
		menu.add(Menu.NONE, 2, 0, "版本提示");
		menu.add(Menu.NONE, 3, 0, "退出");
		return super.onCreateOptionsMenu(menu);
	}

	// 菜单选中
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case 1:
			Utils.displayAlert(
					Main.this,
					"徐安2号出品",
					"这是一款绝对绿色，绝对简单方便的翻译软件。我的宗旨就是，没有广告，没有插件，简单实用，小巧精悍。如果有什么要的建议，欢迎交流。电：15858178400。非诚勿扰。",
					"晓得了");
			break;
		case 2:
			Utils.displayAlert(
					Main.this,
					"版本",
					"此版本应该是1.2版本了，支持了翻译结果的复制哦，长按文本就可以哦亲。并减肥了，应该要比之前版本跟小巧了吧，我觉得应该是的，哈哈。希望有建议的继续建议。一起把这个翻译，做到极致。",
					"亲，知道了");
			break;
		case 3:
			Main.this.finish();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
