package ru.treshchilin.training.h2jdbcsample.save.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import ru.treshchilin.training.h2jdbcsample.save.SaveDestination;

public class FileSaveDestination implements SaveDestination {

	private BufferedWriter writer;
	
	public FileSaveDestination(String fOutPath) throws IOException {
		writer = new BufferedWriter(new FileWriter(new File(fOutPath), StandardCharsets.UTF_8));
	}
	
	public FileSaveDestination(BufferedWriter writer) {
		this.writer = writer;
	}


	@Override
	public void save(String str) throws IOException {
			writer.append(str);
			writer.flush();
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

}
