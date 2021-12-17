package de.beisser.po.extractor;

import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.drew.metadata.exif.ExifDirectoryBase.TAG_DATETIME;
import static de.beisser.po.PhotoOrganizerConstants.TIME_ZONE;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@Component
public class ExifIFD0DirectoryDateExtractor implements IDateExtractor {
    @Override
    public List<Date> fetchDates(Metadata metadata) {
        if (!metadata.containsDirectoryOfType(ExifIFD0Directory.class)) {
            return emptyList();
        }
        final ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        final Date date = directory.getDate(TAG_DATETIME, TIME_ZONE);

        return singletonList(date);
    }
}
