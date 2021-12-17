package de.beisser.po.extractor;

import com.drew.metadata.Metadata;
import com.drew.metadata.mov.metadata.QuickTimeMetadataDirectory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.drew.metadata.mov.metadata.QuickTimeMetadataDirectory.*;
import static de.beisser.po.PhotoOrganizerConstants.TIME_ZONE;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Component
public class QuickTimeDirectoryDateExtractor implements IDateExtractor {
    @Override
    public List<Date> fetchDates(Metadata metadata) {
        if (!metadata.containsDirectoryOfType(QuickTimeMetadataDirectory.class)) {
            return emptyList();
        }

        QuickTimeMetadataDirectory directory = metadata.getFirstDirectoryOfType(QuickTimeMetadataDirectory.class);
        final Date date = directory.getDate(TAG_CREATION_DATE, TIME_ZONE);

        return singletonList(date);
    }
}
