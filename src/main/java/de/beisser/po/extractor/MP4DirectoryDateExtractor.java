package de.beisser.po.extractor;

import com.drew.metadata.Metadata;
import com.drew.metadata.mp4.Mp4Directory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.drew.metadata.mp4.Mp4Directory.TAG_CREATION_TIME;
import static com.drew.metadata.mp4.Mp4Directory.TAG_MODIFICATION_TIME;
import static de.beisser.po.PhotoOrganizerConstants.TIME_ZONE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Component
public class MP4DirectoryDateExtractor implements IDateExtractor {
    @Override
    public List<Date> fetchDates(Metadata metadata) {
        if (!metadata.containsDirectoryOfType(Mp4Directory.class)) {
            return emptyList();
        }

        Mp4Directory directory = metadata.getFirstDirectoryOfType(Mp4Directory.class);
        final Date date1 = directory.getDate(TAG_CREATION_TIME, TIME_ZONE);
        final Date date2 = directory.getDate(TAG_MODIFICATION_TIME, TIME_ZONE);

        return asList(date1, date2);
    }
}
