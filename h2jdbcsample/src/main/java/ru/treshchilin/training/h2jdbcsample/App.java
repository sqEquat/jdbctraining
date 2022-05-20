package ru.treshchilin.training.h2jdbcsample;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import ru.treshchilin.training.h2jdbcsample.dao.TablePKRepository;
import ru.treshchilin.training.h2jdbcsample.dao.impl.TablePKRepoH2Impl;
import ru.treshchilin.training.h2jdbcsample.model.TablePkDescription;
import ru.treshchilin.training.h2jdbcsample.save.SaveDestination;
import ru.treshchilin.training.h2jdbcsample.save.impl.FileSaveDestination;

/**
 * Java App interacts with DB and save data to a file.
 * 
 * App tries to get data from TABLE_LIST and TABLE_COLS tables in DB,
 * match primary key names from TABLE_LIST with its types from TABLE_COLS.
 * After get all data saves it to a file in format "table_name, pk_name, pk_type"
 * 
 * Table names, column names, pk names separator could be change in DAO implementations.
 * 
 * File path could be provided through argument:
 * >java App filePath
 * 
 * DB credentials could be provided through arguments:
 * >java App url user password
 * 
 * File path and DB credentials could be provided through arguments
 * >java App filePath url user password
 *
 */
public class App {
	static String url = "jdbc:h2:~/test";
	static String user = "sa";
	static String password = "";
	
	static String outFilePath = "./out.txt";
	
    public static void main( String[] args ) throws ClassNotFoundException, IOException {
    	
//    	If arguments provided parse it
    	if (args.length > 0) {
    		parseArguments(args);
    	}

//    	Get DB Connection Instance
    	TablePKRepository pkRepository = new TablePKRepoH2Impl();

//    	Get destination to save result instance
    	SaveDestination saveDestination = new FileSaveDestination(outFilePath);

    	try {
			pkRepository.connect(url, user, password);
			
//			Try to get tables primary keys types
			List<TablePkDescription> tablePkDescriptions = pkRepository.findAll();
			
//			Try to save every table primary keys description to a file
			tablePkDescriptions.forEach(descr -> {
				try {
					saveDestination.save(descr.toString());
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			});

		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			
			// Try to close connection and save destination
			try {
				pkRepository.close();
				saveDestination.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
    
    private static void parseArguments(String[] args) {
//    	File path provided by argument
    	if (args.length == 1) {
    		outFilePath = args[0];

//    	DB credentials provided by argument
    	} else if (args.length == 3) {
        		url = args[0];
        		user = args[1];
        		password = args[2];
        		
//      File path and DB credentials provided by argument 		
        } else {
        	outFilePath = args[0];
    		url = args[1];
    		user = args[2];
    		password = args[3];
        }
    }
}
