package com.zerti.utilities;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GenericUtilitiesService {
	
	private static String getRandomHexString(int numchars){
        StringBuilder sb = new StringBuilder();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(new Random().nextInt()));
        }

        return sb.substring(0, numchars);
    }
	
	public static String generarRequestId() {
		return getRandomHexString(8).concat("-").concat(getRandomHexString(4))
				.concat("-").concat(getRandomHexString(4)).concat("-")
				.concat(getRandomHexString(4)).concat("-").concat(getRandomHexString(12));
	}
}
