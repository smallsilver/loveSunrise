/*
 *
 * Created by smallsilver on 1/6/16 3:19 PM
 * Email dongen_wang@163.com
 *
 * Copyright 2016 SmallSilver Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package com.silver.droid.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class HeightAnimation extends Animation {

    protected final int originalHeight;

    protected final View view;

    protected float perValue;



    public HeightAnimation(View view, int fromHeight, int toHeight) {

        this.view = view;

        this.originalHeight = fromHeight;

        this.perValue = (toHeight - fromHeight);

    }



    @Override

    protected void applyTransformation(float interpolatedTime, Transformation t) {

        view.getLayoutParams().height = (int) (originalHeight + perValue * interpolatedTime);

        view.requestLayout();

    }



    @Override

    public boolean willChangeBounds() {

        return true;

    }

}