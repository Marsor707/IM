package com.example.factory.model.db;

import com.example.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * APP中一个基础的BaseDbModel
 * 基础了数据库框架DbFLow中的基础类
 * 同时定义了我们需要的方法
 * Created by marsor on 2017/7/10.
 */

public abstract class BaseDbModel<Model> extends BaseModel implements DiffUiDataCallback.UiDataDiffer<Model>{
}
