package com.pms.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pms.model.PurchaseHistory;

@Service
public class PdfService {

	public byte[] generatePurchasePdf(PurchaseHistory purchaseHistory) throws DocumentException, IOException {
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);
		document.open();

		// Title
		Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
		Paragraph title = new Paragraph("Purchase Receipt", titleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		title.setSpacingAfter(20);
		document.add(title);

		// Table for details
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);
		table.setSpacingAfter(10f);

		// Add cells with information
		addCellToTable(table, "Product ID:", String.valueOf(purchaseHistory.getProduct().getProdId()));
		addCellToTable(table, "Quantity Purchased:", String.valueOf(purchaseHistory.getQuantityPurchased()));
		addCellToTable(table, "Total Amount:", "INR " + String.valueOf(purchaseHistory.getTotalAmount()));
		addCellToTable(table, "Customer Name:", purchaseHistory.getCusName());
		addCellToTable(table, "Customer Email:", purchaseHistory.getCusEmail());
		addCellToTable(table, "Mobile Number:", purchaseHistory.getMobNum());
		addCellToTable(table, "Address:", purchaseHistory.getAddress());
		addCellToTable(table, "Purchase Date:", purchaseHistory.getPurchaseDate().toString());
		addCellToTable(table, "Product Name:", purchaseHistory.getProductName());
		addCellToTable(table, "Product Price:", "INR " + String.valueOf(purchaseHistory.getProductPrice()));
		addCellToTable(table, "Product Discount:", String.valueOf(purchaseHistory.getProductDiscount()) + "%");
		addCellToTable(table, "Description:", purchaseHistory.getDescription());

		document.add(table);
		document.close();
		return baos.toByteArray();
	}

	private void addCellToTable(PdfPTable table, String header, String value) {
		Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
		Font valueFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

		PdfPCell cellHeader = new PdfPCell(new Phrase(header, headerFont));
		cellHeader.setBorder(Rectangle.NO_BORDER);
		cellHeader.setPadding(5);
		table.addCell(cellHeader);

		PdfPCell cellValue = new PdfPCell(new Phrase(value, valueFont));
		cellValue.setBorder(Rectangle.NO_BORDER);
		cellValue.setPadding(5);
		table.addCell(cellValue);
	}
}
