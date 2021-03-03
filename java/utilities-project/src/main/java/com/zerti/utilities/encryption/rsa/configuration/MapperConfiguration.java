package com.zerti.utilities.encryption.rsa.configuration;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MapperConfiguration {

	public Configuration getConfiguration() throws IOException {
		
		String configFile = System.getenv("UTIL_CONFIG_FILE");
		File file = new File(configFile); 
		ObjectMapper om = new ObjectMapper(new YAMLFactory());
		return om.readValue(file, Configuration.class);
	}

}
