package de.beisser.po.extractor;

import com.drew.metadata.Metadata;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public interface IDateExtractor {
    List<Date> fetchDates(Metadata metadata);
}
