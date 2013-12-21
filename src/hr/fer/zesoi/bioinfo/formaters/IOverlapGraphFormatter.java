package hr.fer.zesoi.bioinfo.formaters;

import hr.fer.zesoi.bioinfo.models.OverlapGraph;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

public interface IOverlapGraphFormatter {
	
	/**
	 * Reads and creates an OverlapGraph object from given file
	 * @param file File from witch to read from
	 * @return Newly created OverlapGraph object
	 * @throws IOException throws an exception if a reading error occurs
	 */
	OverlapGraph overlapGraphFromFile(File file) throws IOException;
	
	/**
	 * Formats and writes a given OverlapGrap to given Writer
	 * @param graph Graph to format and write
	 * @param writer Writer to write to
	 * @throws IOException throws an exception if a writing error occurs
	 */
	void formatAndWriteOverlapGraph(OverlapGraph graph, Writer writer) throws IOException;
}
