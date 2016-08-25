package com.ccg.common.pdf.util;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pdfclown.bytes.Buffer;
import org.pdfclown.documents.Page;
import org.pdfclown.documents.contents.ITextString;
import org.pdfclown.documents.contents.TextChar;
import org.pdfclown.documents.interaction.annotations.TextMarkup;
import org.pdfclown.documents.interaction.annotations.TextMarkup.MarkupTypeEnum;
import org.pdfclown.files.SerializationModeEnum;
import org.pdfclown.tools.TextExtractor;
import org.pdfclown.util.math.Interval;
import org.pdfclown.util.math.geom.Quad;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PdfUtil {
	
	public static void extractSelectPage(InputStream is, OutputStream os,  List<Integer> pages) throws IOException, DocumentException{	
		PdfReader reader = new PdfReader(is);
		reader.selectPages(pages);
		PdfStamper stamper = new PdfStamper(reader, os);
		stamper.close();
	}

	public static void textHighlight(InputStream inputStream, OutputStream outputStream, String textRegEx) throws IOException{
			
		org.pdfclown.files.File file = new org.pdfclown.files.File(new Buffer(inputStream));
		
		Pattern pattern = Pattern.compile(textRegEx, Pattern.CASE_INSENSITIVE);
		TextExtractor textExtractor = new TextExtractor(true, true);
		
		for (final Page page : file.getDocument().getPages()) {
			//System.out.println("\nScanning page " + (page.getIndex() + 1) + "...\n");

			// 2.1. Extract the page text!
			Map<Rectangle2D, List<ITextString>> textStrings = textExtractor.extract(page);

			// 2.2. Find the text pattern matches!
			final Matcher matcher = pattern.matcher(TextExtractor.toString(textStrings));

			// 2.3. Highlight the text pattern matches!
			textExtractor.filter(textStrings, new TextExtractor.IIntervalFilter() {
				@Override
				public boolean hasNext() {
					return matcher.find();
				}

				@Override
				public Interval<Integer> next() {
					return new Interval<Integer>(matcher.start(), matcher.end());
				}

				@Override
				public void process(Interval<Integer> interval, ITextString match) {
					// Defining the highlight box of the text pattern match...
					List<Quad> highlightQuads = new ArrayList<Quad>();
					{
						/*
						 * NOTE: A text pattern match may be split across
						 * multiple contiguous lines, so we have to define a
						 * distinct highlight box for each text chunk.
						 */
						Rectangle2D textBox = null;
						for (TextChar textChar : match.getTextChars()) {
							Rectangle2D textCharBox = textChar.getBox();
							if (textBox == null) {
								textBox = (Rectangle2D) textCharBox.clone();
							} else {
								if (textCharBox.getY() > textBox.getMaxY()) {
									highlightQuads.add(Quad.get(textBox));
									textBox = (Rectangle2D) textCharBox.clone();
								} else {
									textBox.add(textCharBox);
								}
							}
						}
						highlightQuads.add(Quad.get(textBox));
					}
					// Highlight the text pattern match!
					new TextMarkup(page, null, MarkupTypeEnum.Highlight, highlightQuads);
				}
			});
			
			file.save(new org.pdfclown.bytes.OutputStream(outputStream), SerializationModeEnum.Standard);
			file.close();
			//file.save(outputStream, SerializationModeEnum.Standard);
			//file.sa
			//IOutputStream output = new org.pdfclown.bytes.OutputStream(outputStream);
			//file.close();
		}

	}
	
	
	
	public static void main(String[] args) throws DocumentException, IOException {
			
		///// test pdf text highlight //////
		String originalFilePath = "/Users/zchen323/codebase/test_doc/partical.pdf";
		String highlightedFilePath = "/Users/zchen323/codebase/test_doc/partical_highlighted3.pdf";
		String textRegEx = "Troubleshooting|specialization|alleviate the potential";
		
		OutputStream os = new FileOutputStream(highlightedFilePath);
		InputStream is = new FileInputStream(new File(originalFilePath));
		
		PdfUtil.textHighlight(is, os, textRegEx);
		is.close();
		os.flush();
		os.close();
			
			
			
			
	
	
	
	}

}