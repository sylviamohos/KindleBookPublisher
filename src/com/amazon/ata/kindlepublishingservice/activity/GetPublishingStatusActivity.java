package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.aws.dynamodb.DynamoDbClientProvider;
import com.amazon.ata.kindlepublishingservice.converters.BookPublishRequestConverter;
import com.amazon.ata.kindlepublishingservice.converters.PublishingRecordConverter;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.exceptions.PublishingStatusNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.lambda.runtime.Context;

import javax.inject.Inject;
import java.util.List;

public class GetPublishingStatusActivity {
    // TODONE: 2022-06-13 added new code:
    private PublishingStatusDao publishingStatusDao;
    @Inject
    public GetPublishingStatusActivity(PublishingStatusDao publishingStatusDao) {
        this.publishingStatusDao = publishingStatusDao;
    }
    // TODONE: 2022-06-13 added new code:
    public GetPublishingStatusResponse execute(GetPublishingStatusRequest publishingStatusRequest) {
        // Lets a client check on the status of their publishing request.
        // retrieve each corresponding item to the request's publishingRecordId (HASH) --> PublishingStatusItem table
        List<PublishingStatusItem> publishingStatusItems = publishingStatusDao.getPublishingStatus(publishingStatusRequest.getPublishingRecordId());
        List<PublishingStatusRecord> publishingStatusRecords = PublishingRecordConverter.convert(publishingStatusItems);
        return GetPublishingStatusResponse.builder().withPublishingStatusHistory(publishingStatusRecords).build();
    }
}

//        PublishingStatusItem publishingStatus = new PublishingStatusItem();
//        publishingStatus.setPublishingRecordId(publishingStatusRequest.getPublishingRecordId());
//        DynamoDBQueryExpression<PublishingStatusItem> expression = new DynamoDBQueryExpression<PublishingStatusItem>()
//                .withHashKeyValues(publishingStatus);
//        DynamoDBMapper mapper = new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient());
//        PaginatedQueryList<PublishingStatusItem> statusItems = mapper.query(PublishingStatusItem.class, expression);
//
//        // if no publishing status items are found, throw PublishingStatusNotFoundException
//        if (publishingStatusRequest.getPublishingRecordId() == null) {
//            throw new PublishingStatusNotFoundException("No publishing status items have been found with the given Publishing Record Id");
//        }
//        return GetPublishingStatusResponse.builder()
//                .withPublishingStatusHistory(statusItems.)
//                .withPublishingStatusHistory()
//                .build();
// Return publishing status items --> <PublishingStatusItem>
//          convert to a list of PublishingStatusRecords (see GetBookActivity)

//return publishingStatusDao.getPublishingStatus(publishingStatusRequest.getPublishingRecordId());
