package net.brord.schuifpuzzel;

import net.brord.schuifpuzzel.POD.DrawData;

/**
 * Created by Brord on 4/9/2016.
 */
public interface DrawListener {
    void sendDrawUpdate(java.util.List<DrawData> data);
}
