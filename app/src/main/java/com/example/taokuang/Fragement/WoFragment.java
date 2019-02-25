package com.example.taokuang.Fragement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taokuang.R;
import com.example.taokuang.SignUpActivity;
import com.example.taokuang.tool.BaseFragment;
import com.example.taokuang.wo.WofbActivity;
import com.example.taokuang.wo.WogmActivity;
import com.leon.lib.settingview.LSettingItem;

import cn.bmob.v3.BmobUser;

public class WoFragment extends BaseFragment {
    private ImageView woicon;
    private TextView woname;
    private LSettingItem wofb;
    private LSettingItem wogm;
    private Button wozx;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wo_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View v) {
        wofb = v.findViewById(R.id.wo_fb);
        wofb.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                Intent intentfb = new Intent(getContext(),WofbActivity.class);
                startActivity(intentfb);
            }
        });
        wogm = v.findViewById(R.id.wo_gm);
        wogm.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                Intent intentgm = new Intent(getContext(),WogmActivity.class);
                startActivity(intentgm);
            }
        });
        wozx=v.findViewById(R.id.wo_zx);
        wozx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut();
                Toast.makeText(getContext(), "注销成功",
                        Toast.LENGTH_SHORT).show();
                Log.d("注销", "注销成功");
            }
        });

        woicon = v.findViewById(R.id.wo_icon);
        woname = v.findViewById(R.id.wo_name);
    }


}
