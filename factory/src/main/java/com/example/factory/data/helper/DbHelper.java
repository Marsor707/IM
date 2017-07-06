package com.example.factory.data.helper;

import com.example.factory.model.db.AppDatabase;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.util.Arrays;

/**
 * 数据库的辅助工具类
 * 辅助完成 增删改
 * Created by marsor on 2017/7/6.
 */

public class DbHelper {
    private static final DbHelper instance;

    static {
        instance = new DbHelper();
    }

    private DbHelper() {

    }

    /**
     * 新增或修改的统一方法
     *
     * @param tClass  传递一个class信息
     * @param models  这个class对应的实例数组
     * @param <Model> 这个实例的范型，限定条件是BaseModel
     */
    public static <Model extends BaseModel> void save(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0)
            return;
        //当前数据库的一个管理者
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        //提交一个事物
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                //执行
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                //保存
                adapter.saveAll(Arrays.asList(models));
                //唤起通知
                instance.notifySave(tClass, models);
            }
        }).build().execute();
    }

    /**
     * 删除的统一方法
     *
     * @param tClass  传递一个class信息
     * @param models  这个class对应的实例数组
     * @param <Model> 这个实例的范型，限定条件是BaseModel
     */
    public static <Model extends BaseModel> void delete(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0)
            return;
        //当前数据库的一个管理者
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        //提交一个事物
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                //执行
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                //保存
                adapter.deleteAll(Arrays.asList(models));
                //唤起通知
                instance.notifyDelete(tClass, models);
            }
        }).build().execute();
    }

    /**
     * 进行通知调用
     *
     * @param tClass  传递一个class信息
     * @param models  这个class对应的实例数组
     * @param <Model> 这个实例的范型，限定条件是BaseModel
     */
    private final <Model extends BaseModel> void notifySave(final Class<Model> tClass, final Model... models) {
        //TODO
    }

    /**
     * 进行通知调用
     *
     * @param tClass  传递一个class信息
     * @param models  这个class对应的实例数组
     * @param <Model> 这个实例的范型，限定条件是BaseModel
     */
    private final <Model extends BaseModel> void notifyDelete(final Class<Model> tClass, final Model... models) {
        //TODO
    }
}
