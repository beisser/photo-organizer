package de.beisser.po.extractor;

import com.drew.metadata.Metadata;
import com.drew.metadata.mov.QuickTimeDirectory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.drew.metadata.mov.QuickTimeDirectory.*;
import static de.beisser.po.PhotoOrganizerConstants.TIME_ZONE;
import static java.util.Collections.emptyList;

@Component
public class QuickTimeMetadataDirectoryDateExtractor implements IDateExtractor{
    @Override
    public List<Date> fetchDates(Metadata metadata) {
        if (!metadata.containsDirectoryOfType(QuickTimeDirectory.class)) {
            return emptyList();
        }

        QuickTimeDirectory directory = metadata.getFirstDirectoryOfType(QuickTimeDirectory.class);
        final Date date1 = directory.getDate(TAG_CREATION_TIME, TIME_ZONE);
        final Date date2 = directory.getDate(TAG_MODIFICATION_TIME, TIME_ZONE);

        return Arrays.asList(date1, date2);
    }
}
