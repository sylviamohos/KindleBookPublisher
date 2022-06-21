package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.Queue;

public class BookPublishRequestManager {
    private Queue<BookPublishRequest> publishRequestQueue;

    @Inject
    public BookPublishRequestManager() {
        publishRequestQueue = new LinkedList<>();
    }

    public void addBookPublishRequest(BookPublishRequest bookPublishRequest) {
        publishRequestQueue.add(bookPublishRequest);
    }

    public BookPublishRequest getBookPublishRequestToProcess() {
        // Replacing original .peek() with .remove():
        return publishRequestQueue.peek();
        // TODONE: 2022-06-19 - removes an item from the queue if the queue is not empty and returns it
        //                      to BookPublishTask's run().
        //                      If the queue is empty, BookPublishTask should return
        //                      immediately without taking action.
//        if (publishRequestQueue.isEmpty()) {
//            return null;
//        }
//        return publishRequestQueue.remove();
    }

}
