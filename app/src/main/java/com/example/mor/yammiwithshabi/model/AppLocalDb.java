import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.example.mor.yammiwithshabi.MyApplication;

@Database(entities = {FeedItem.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract FeedItemDao studentDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository db = Room.databaseBuilder(MyApplication.context,
            AppLocalDbRepository.class,
            "dbFileName.db").fallbackToDestructiveMigration().build();
}
