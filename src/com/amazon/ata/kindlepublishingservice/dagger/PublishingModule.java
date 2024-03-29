package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazon.ata.kindlepublishingservice.publishing.BookPublishTask;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublisher;

import dagger.Module;
import dagger.Provides;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Singleton;

@Module
public class PublishingModule {

//    @Provides
//    @Singleton
//    public BookPublisher provideBookPublisher(ScheduledExecutorService scheduledExecutorService) {
//        return new BookPublisher(scheduledExecutorService, new BookPublishTask());
//    }

    @Provides
    @Singleton
    public ScheduledExecutorService provideBookPublisherScheduler() {
        return Executors.newScheduledThreadPool(1);
    }
}
