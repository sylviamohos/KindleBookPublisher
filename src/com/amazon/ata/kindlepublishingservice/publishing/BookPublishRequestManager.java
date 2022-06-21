package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
public class BookPublishRequestManager {
    private Queue<BookPublishRequest> publishRequestQueue;

    @Inject
    public BookPublishRequestManager() {
        publishRequestQueue = new ConcurrentLinkedQueue<>();
    }

    public void addBookPublishRequest(BookPublishRequest bookPublishRequest) {
        publishRequestQueue.add(bookPublishRequest);
    }

    public BookPublishRequest getBookPublishRequestToProcess() {
        BookPublishRequest request = publishRequestQueue.peek();
        if(request != null) {
            return publishRequestQueue.remove();
        }
        return null;
    }

}
