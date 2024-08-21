package com.freebe.code.util.code.generator.doclet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SrcFileWriter {
	private BufferedWriter writer;
	
	public SrcFileWriter(String fileName) throws IOException {
		writer = new BufferedWriter(new FileWriter(fileName));
	}
	
	public void writeImport(String clazz) throws IOException {
		this.write("import " + clazz + ";");
	}

	public void writePackage(String pkg) throws IOException {
		this.write("package " + pkg + ";");
	}

	public void write(String line) throws IOException {
		writer.write(line);
		writer.newLine();
	}
	
	public void writeEmptyLine() throws IOException {
		this.write("");
	}
	
	public void flush() throws IOException {
		this.writer.flush();
	}
	
	public void close() throws IOException {
		if(null != this.writer) {
			this.writer.close();;
		}
	}
}
