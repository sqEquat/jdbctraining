package ru.treshchilin.training.h2jdbcsample.save;

import java.io.IOException;

public interface SaveDestination {
	void save(String str) throws IOException;
	void close() throws IOException;
}
