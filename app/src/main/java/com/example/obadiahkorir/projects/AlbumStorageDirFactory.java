package com.example.obadiahkorir.projects;

/**
 * Created by anipal on 24/1/18.
 */

import java.io.File;

abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}
