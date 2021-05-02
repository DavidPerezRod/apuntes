package com.zerti.utilities.log.repository;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "#{@restClientLogCollectionName.getCollectionName()}")
public interface LogUtilities {
}
