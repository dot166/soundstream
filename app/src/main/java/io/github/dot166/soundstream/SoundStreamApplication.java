package io.github.dot166.soundstream;

import io.github.dot166.jlib.app.jLIBCoreApp;
import io.github.dot166.jlib.service.MediaPlayerService;

public class SoundStreamApplication extends jLIBCoreApp {
    @Override
    public MediaPlayerService getMediaPlayerService() {
        return new RadioService();
    }
}
