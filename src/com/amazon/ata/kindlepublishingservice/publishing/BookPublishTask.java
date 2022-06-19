package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

public class BookPublishTask implements Runnable{

    private BookPublishRequestManager bookPublishRequestManager;
    private PublishingStatusDao publishingStatusDao;
    private CatalogDao catalogDao;

    public BookPublishTask(BookPublishRequestManager manager,
                           PublishingStatusDao publishingStatusDao,
                           CatalogDao catalogDao) {
        this.bookPublishRequestManager = manager;
        this.publishingStatusDao = publishingStatusDao;
        this.catalogDao = catalogDao;
    }


    @Override
    public void run() {
        //manager.getBookPublishRequestToProcess();
        PublishingStatusItem item = publishingStatusDao.setPublishingStatus(bookPublishRequestManager.getBookPublishRequestToProcess().getPublishingRecordId(),
                PublishingRecordStatus.IN_PROGRESS,
                bookPublishRequestManager.getBookPublishRequestToProcess().getBookId());



    }
}
