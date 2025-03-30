package net.youshallnottick;

import net.createmod.ponder.foundation.PonderIndex;
import net.youshallnottick.compat.ponder.YouShallNotTickPonderPlugin;

public class YouShallNotTickClient {

    public static void initialiseClient(){
        PonderIndex.addPlugin(new YouShallNotTickPonderPlugin());
    }
}
