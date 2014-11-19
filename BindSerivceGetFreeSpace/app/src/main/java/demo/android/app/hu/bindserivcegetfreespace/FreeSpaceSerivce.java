package demo.android.app.hu.bindserivcegetfreespace;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;

/**
 * Created by Peter on 2014.10.17..
 */
public class FreeSpaceSerivce extends Service {

    private final IBinder binder = new MyFreeSpaceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public long getFreeSpace() {
        StatFs stat = new StatFs(
                Environment.getExternalStorageDirectory().getAbsolutePath());
        stat.restat(Environment.getExternalStorageDirectory().getAbsolutePath());
        long available = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
        return available / 1024 /1024;
    }

    public class MyFreeSpaceBinder extends Binder {
        FreeSpaceSerivce getService() {
            return FreeSpaceSerivce.this;
        }
    }
}
