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

/**
 * @PACKAGE com.lchr.common.util
 * @DESCRIPTION: TODO
 * @AUTHOR dongen_wang
 * @DATE 15/8/21 12:40
 * @VERSION V1.0
 */
public class TypeFaceEvent {

    public static enum TYPE_FACE_ENUM{
        DEFAULT,OTHER
    }

    public TYPE_FACE_ENUM type_face;

    public TypeFaceEvent(){
        type_face = TYPE_FACE_ENUM.DEFAULT;
    }

    public TypeFaceEvent(TYPE_FACE_ENUM face){
        type_face = face;
    }
}
