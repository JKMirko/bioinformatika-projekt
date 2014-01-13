package hr.fer.zesoi.bioinfo.formaters;

import hr.fer.zesoi.bioinfo.models.OverlapGraph;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * Interface representing a formatter.
 * Formatter is used for parsing input data and formating output data.
 * @author Bioinfo team
 *
 */
public interface IOverlapGraphFormatter {
		
	/**
	 * Reads and creates an OverlapGraph object from given file
	 * @param overlapsFile File from witch to read the overlaps from
	 * @param readsFile File from witch to read the reads from
	 * @return Newly created OverlapGraph object
	 * @throws IOException throws an exception if a reading error occurs
	 * @throws FormatterException throws an formatter exception if any format specific problem occur
	 */
	OverlapGraph overlapGraphFromOverlapFileAndReadsFile(File overlapsFile, File readsFile) throws IOException, FormatterException;
	
	/**
	 * Formats and writes a given OverlapGrap to given Writer
	 * @param graph Graph to format and write
	 * @param layoutInformationWriter Writer to write the layout information to
	 * @param overlapInformationWriter Writer to write the overlap information to
	 * @throws IOException throws an exception if a writing error occurs
	 * @throws FormatterException throws an formatter exception if any format specific problem occur
	 */
	void formatAndWriteOverlapGraph(OverlapGraph graph, Writer layoutInformationWriter, Writer overlapInformationWriter) throws IOException, FormatterException;
}
