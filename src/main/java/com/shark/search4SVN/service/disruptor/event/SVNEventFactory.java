package com.shark.search4SVN.service.disruptor.event; /**
 * Created by ethan-liu on 15/6/20.
 */
import com.lmax.disruptor.EventFactory;

public class SVNEventFactory implements EventFactory<SVNEvent>
{
    public SVNEvent newInstance()
    {
        return new SVNEvent();
    }
}
