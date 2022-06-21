package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import javax.inject.Inject;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion getBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;
    }

    // Returns null if no version exists for the provided bookId
    private CatalogItemVersion getLatestVersionOfBook(String bookId) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
            .withHashKeyValues(book)
            .withScanIndexForward(false)
            .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    // TODONE: 2022-06-07 new code added for MT1 - milestone 2
    public RemoveBookFromCatalogResponse removeBookFromCatalog(String bookId) {
        CatalogItemVersion bookToRemove = this.getBookFromCatalog(bookId);
        bookToRemove.setInactive(true);
        dynamoDbMapper.save(bookToRemove);
        return null;
    }

    // TODONE: 2022-06-09 new code added for S2-MT2
    public void validateBookExists(String bookId) {
        CatalogItemVersion bookToFind = this.getLatestVersionOfBook(bookId);

        if (bookToFind == null) {
            throw new BookNotFoundException("The provided book Id cannot be found.");
        }
    }
    // TODO: 2022-06-20 added new code for S3-MT4
    public CatalogItemVersion createOrUpdateBook(KindleFormattedBook book) {
        CatalogItemVersion updateVersion = getBookFromCatalog(book.getBookId());
        if (updateVersion.equals(book.getBookId())) {
            updateVersion.setVersion(updateVersion.getVersion()+1);
            updateVersion.setInactive(true);
            // Create a new book if the given id is null; generate a bookId for
            //          the new book, and save it to the CatalogItemVersion table.
        } else if (book.getBookId() == null) {
            String generatedBookId = KindlePublishingUtils.generateBookId();
            dynamoDbMapper.save(generatedBookId);
        }




        try {
            getBookFromCatalog(book.getBookId());
        } catch (BookNotFoundException e) {

        }






        

        // if creating a new book:
        //      - generate bookId by way of KindlePublishingUtils
        //              - KindlePublishingUtils returns bookId
        //       - adds CatalogItemVersion to dynamoDB
        //   else {
        //         update the existing book:
        //             - look up bookId from dynamoDB using one of the above ()s
        //              if book not found in catalog:
        //                  - throw BookNotFoundException --> run() catches it
        //                      and adds the item to the publishingStatusTable as FAILED
        //                      with an exception message using setPublishingStatus()
        //              else { catalogDao adds the catalogItemVersion to DDB table,
        //                      marks the previous version as Inactive
        //                      return catalogItemVersion to catalogDao and to BookPublishTask run()
        //  if any exception is caught in run() while processing {
        //                        run() should setPublsihgingStatus to FAILED
        //                              and publishingStatusDao adds that status to the table
        //      else {
        //          setPublishingStatus to SUCCESSFUL and add status to table.


        return null;
    }
}
