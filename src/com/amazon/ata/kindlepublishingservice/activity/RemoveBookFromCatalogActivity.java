package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;
import com.amazonaws.services.lambda.runtime.Context;

import javax.inject.Inject;
import java.security.InvalidParameterException;
import java.util.List;

public class RemoveBookFromCatalogActivity {

    private CatalogDao catalogDao;

    @Inject
    public RemoveBookFromCatalogActivity(CatalogDao catalogDao) {
        this.catalogDao = catalogDao;
    }


    // TODO: 2022-06-07 added new code:
    public RemoveBookFromCatalogResponse execute(RemoveBookFromCatalogRequest removeBookFromCatalogRequest) {

//        if (removeBookFromCatalogRequest.getBookId() == null) {
//            throw new InvalidParameterException();
//        }
//        CatalogItemVersion bookFromCatalog = catalogDao.getBookFromCatalog(removeBookFromCatalogRequest.getBookId());
//        if (bookFromCatalog.isInactive()) {
//            throw new BookNotFoundException();
//        }
       // CatalogItemVersion bookToRemove = catalogDao.removeBookFromCatalog(bookFromCatalog.getBookId());
        return catalogDao.removeBookFromCatalog(removeBookFromCatalogRequest.getBookId());
    }



}
