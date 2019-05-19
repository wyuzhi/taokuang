package com.flying.taokuang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.flying.taokuang.base.BaseToolbarActivity;
import com.flying.taokuang.entity.Comment;
import com.flying.taokuang.entity.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class CommentActivity extends BaseToolbarActivity {
    private EditText Econtent;
    private Button Efabu;
    private String content;
    private String authorID;
    private String commentatorID;
    RatingBar ratingBar1;
    RatingBar ratingBar2;
    Button comtbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_comment);
        ratingBar1 = findViewById(R.id.m_ratingBar_fuwu);
        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                Toast.makeText(CommentActivity.this, rating+"", Toast.LENGTH_SHORT).show();
            }
        });
        //ratingBar2 =findViewById(R.id.m_ratingBar_miaoshu);
        initView();

    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_comment;
    }

    private void initView() {
        Intent intent = getIntent();
        authorID=intent.getStringExtra("评价人");
        commentatorID=intent.getStringExtra("被评人");
        Econtent=findViewById(R.id.m_edit);
        Efabu=findViewById(R.id.m_commit);
        Efabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = String.valueOf(Econtent.getText().toString());
                User author = new User();
                User commentator= new User();
                author.setObjectId(authorID);
                commentator.setObjectId(commentatorID);
                final Comment comment = new Comment();
                comment.setAuthor(author);
                comment.setCommentator(commentator);
                comment.setContent(content);
                comment.save(new SaveListener<String>() {

                    @Override
                    public void done(String objectId, BmobException e) {
                        Toast.makeText(CommentActivity.this,"评价成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CommentActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });

    }

    /*当我不存在,原本是想把评论弄为弹窗的
    点击交易成功的按钮
     btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow(v);
            }
        });
    }


    private void initPopWindow(View v) {
    //应该传入comment，以下是comment的逻辑代码？？？
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_popup, null, false);
        //提交评价button
        Button btn_comment = (Button) view.findViewById(R.id.btn_);

        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效


        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, 50, 0);

        //设置popupWindow里的按钮的事件
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                popWindow.dismiss();
            }
        });
      }
}*/
}
