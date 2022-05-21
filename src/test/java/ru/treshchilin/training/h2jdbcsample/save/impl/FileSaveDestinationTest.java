package ru.treshchilin.training.h2jdbcsample.save.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;

import java.io.BufferedWriter;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileSaveDestinationTest {

	@Mock
	BufferedWriter bufferedWriter;
	
	@InjectMocks
	FileSaveDestination underTest;
	
	@Test
	void test() {
		assertThat(underTest).isNotNull();
	}
	
	@Test
	void save() throws IOException {
		String str = "to save";
				
		underTest.save(str);
		
		verify(bufferedWriter, atMostOnce()).append(str);
		verify(bufferedWriter, atMostOnce()).flush();
	}
	
	@Test
	void close() throws IOException {
		underTest.close();
		
		verify(bufferedWriter, atMostOnce()).close();
	}

}
