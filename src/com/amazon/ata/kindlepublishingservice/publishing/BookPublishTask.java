package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.converters.BookPublishRequestConverter;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import javax.inject.Inject;

public class BookPublishTask implements Runnable{

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
                catalogDao.createOrUpdateBook(book);
            } catch (BookNotFoundException e) {
                publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(),
                        PublishingRecordStatus.FAILED,
                        request.getBookId());
            }


    }

    // TODO: 2022-06-20 added new code for S3-MT4
    @Override
    public void run() {
        publish(bookPublishRequestManager);
//
//        if (bookPublishRequestManager.getBookPublishRequestToProcess() != null) {
//            // set the Publishing Status on the Publishing Status table to IN PROGRESS
//            publishingStatusDao.setPublishingStatus(bookPublishRequestManager.getBookPublishRequestToProcess().getPublishingRecordId(),
//                    PublishingRecordStatus.IN_PROGRESS,
//                    bookPublishRequestManager.getBookPublishRequestToProcess().getBookId());
//            // format the book fields in the BookPublishRequest into their kindle format version:
//            KindleFormattedBook book = KindleFormatConverter.format(BookPublishRequest.builder().build());
//            // pass it to the .createOrUpdateBook method in catalogDao
//            catalogDao.createOrUpdateBook(book);

        }




    }
}
