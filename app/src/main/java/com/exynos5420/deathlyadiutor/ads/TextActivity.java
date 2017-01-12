/*
 * Copyright (C) 2015 Willi Ye
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exynos5420.deathlyadiutor.ads;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.exynos5420.deathlyadiutor.ads.R;

public class TextActivity extends BaseActivity {

    public static final String ARG_TEXT = "text";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textView.setTextSize(getResources().getDisplayMetrics().density * 7);
        textView.setGravity(Gravity.CENTER);
        textView.setText(getIntent().getExtras().getString(ARG_TEXT));
    }

    @Override
    public int getParentViewId() {
        return 0;
    }

    @Override
    public View getParentView() {
        return textView = new TextView(this);
    }

    @Override
    public int getLightTheme() {
        return R.style.AppThemeActionBarLight;
    }

    @Override
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public boolean getDisplayHomeAsUpEnabled() {
        return false;
    }
}