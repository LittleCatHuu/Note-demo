package com.example.n9262.note_demo.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.n9262.note_demo.R;
import com.example.n9262.note_demo.bean.Note;
import com.example.n9262.note_demo.db.NoteDAO;
import com.example.n9262.note_demo.utils.TextFormatUtil;

import java.util.Date;
public class NoteDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SENDED_NOTE_ID = "note_id";
    private EditText mEtTitle;
    private EditText mEtContent;
    private Button mBtnModify;
    private Toolbar mToolbar;
    private NoteDAO mNoteDAO;
    private Cursor mCursor;
    private Note mNote;
    private int mNoteID = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar_detail);
        mToolbar.setTitle("Node Detail");
        // 显示返回按钮
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // 监听Back键,必须放在设置back键后面
        mToolbar.setNavigationOnClickListener(this);
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        mNote = new Note("", "", TextFormatUtil.formatDate(new Date()));
        mNoteID = intent.getIntExtra(SENDED_NOTE_ID, -1);
        // 如果有ID参数,从数据库职工获取信息
        mNoteDAO = new NoteDAO(this);
        if (mNoteID != -1) {
            // 进行查询必须使用?匹配参数
            mCursor = mNoteDAO.queryNote("_id=?", new String[]{mNoteID + ""});
            if (mCursor.moveToNext()) {
                mNote.setTitle(mCursor.getString(mCursor.getColumnIndex("title")));
                mNote.setContent(mCursor.getString(mCursor.getColumnIndex("content")));
                mNote.setCreateTime(mCursor.getString(mCursor.getColumnIndex("create_time")));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void initView() {
        mEtTitle = (EditText) findViewById(R.id.id_et_title);
        mEtContent = (EditText) findViewById(R.id.id_et_content);
        mBtnModify = (Button) findViewById(R.id.id_btn_modify);
        mEtTitle.setText(mNote.getTitle());
        mEtContent.setText(mNote.getContent());
        mBtnModify.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_btn_modify) {
            String title = mEtTitle.getText().toString();
            String content = mEtContent.getText().toString();
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("content", content);
            values.put("create_time", mNote.getCreateTime());
            int rowID = -1;
            // 向数据库添加或者更新已有记录
            if (mNoteID == -1) {
                rowID = (int) mNoteDAO.insertNote(values);
            } else {
                rowID = mNoteDAO.updateNote(values, "_id=?", new String[]{mNoteID + ""});
            }
            if (rowID != -1) {
                Toast.makeText(this, "修改或添加成功", Toast.LENGTH_SHORT).show();
                getContentResolver().notifyChange(Uri.parse("content://com.terry.NoteBook"), null);
                finish();
            }
        } else {
            onBackPressed();
        }
    }
}
