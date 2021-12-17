package de.beisser.po.extractor;

import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static de.beisser.po.PhotoOrganizerConstants.TIME_ZONE;
import static java.util.Collections.emptyList;

@Component
public class ExifSubIFDDirectoryDateExtractor implements IDateExtractor{
    @Override
    public List<Date> fetchDates(Metadata metadata) {
        if(!metadata.containsDirectoryOfType(ExifSubIFDDirectory.class)) {
            return emptyList();
        }
        final ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        final Date date1 = directory.getDateDigitized(TIME_ZONE);
        final Date date2 = directory.getDateOriginal(TIME_ZONE);
        final Date date3 = directory.getDateModified(TIME_ZONE);


        return Arrays.asList(date1, date2, date3);
    }
}
