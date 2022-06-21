package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.converters.BookPublishRequestConverter;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

public class BookPublishTask implements Runnable{

    @Singleton
    private BookPublishRequestManager bookPublishRequestManager;
    private PublishingStatusDao publishingStatusDao;
    private CatalogDao catalogDao;


    @Inject
    public BookPublishTask(BookPublishRequestManager manager,
                           PublishingStatusDao publishingStatusDao,
                           CatalogDao catalogDao) {
        this.bookPublishRequestManager = manager;
        this.publishingStatusDao = publishingStatusDao;
        this.catalogDao = catalogDao;
    }

    public void publish(BookPublishRequestManager bookPublishRequestManager) {
        BookPublishRequest request = bookPublishRequestManager.getBookPublishRequestToProcess();
        if (request != null) {
            publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(),
                    PublishingRecordStatus.IN_PROGRESS,
                    request.getBookId());
            KindleFormattedBook book = KindleFormatConverter.format(request);
            try {
                CatalogItemVersion itemVersion = catalogDao.createOrUpdateBook(book);
                publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(),
                        PublishingRecordStatus.SUCCESSFUL,
                        itemVersion.getBookId());
            } catch (BookNotFoundException e) {
                publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(),
                        PublishingRecordStatus.FAILED,
                        request.getBookId(),
                        e.getMessage());
            }

        }
    }

    // TODONE: 2022-06-20 added new code for S3-MT4
    @Override
    public void run() {
            publish(bookPublishRequestManager);
    }

}

