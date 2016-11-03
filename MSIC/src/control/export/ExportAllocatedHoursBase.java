package control.export;

import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class ExportAllocatedHoursBase {
	
	public ArrayList<Font> tableBodyFonts;
	
	public void createParagraph(String text, Document doc, Font font, int align) throws DocumentException, IOException
    {
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(align);
        doc.add(p);
    }
    
    public PdfPCell CreateHeaderCell(String text, BaseColor backgroundColor)
    {
        PdfPCell cell = new PdfPCell(new Phrase(text, tableBodyFonts.get(11)));
        cell.setBackgroundColor(backgroundColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }
    
    public PdfPCell CreateBodyCell(String text, int align, Font font)
    {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }
    
    public void createTableOneAndTwoHeader(PdfPTable table) throws DocumentException
    {
        BaseColor color = new BaseColor(0x16, 0x7f, 0x92);
        table.setWidthPercentage(100.0f);
        table.addCell(CreateHeaderCell("Nume", color));
        table.addCell(CreateHeaderCell("CNP", color));
        table.addCell(CreateHeaderCell("Departament", color));
        table.addCell(CreateHeaderCell("Ore Repartizate", color));
        table.setHeaderRows(1);
        table.setSplitLate(false);
        float[] columnWidths = new float[] {16f, 13f, 13f, 60f};
        table.setWidths(columnWidths);
    }
    
    public void createTableThreeHeader(PdfPTable table) throws DocumentException
    {
        BaseColor color = new BaseColor(0x16, 0x7f, 0x92);
        table.setWidthPercentage(100.0f);
        table.addCell(CreateHeaderCell("Nume", color));
        table.addCell(CreateHeaderCell("CNP", color));
        table.addCell(CreateHeaderCell("Departament", color));
        table.setHeaderRows(1);
        table.setSplitLate(false);
        float[] columnWidths = new float[] {40f, 13f, 47f};
        table.setWidths(columnWidths);
    }
    
    public void createTableFourHeader(PdfPTable table) throws DocumentException
    {
        BaseColor color = new BaseColor(0x16, 0x7f, 0x92);
        table.setWidthPercentage(100.0f);
        table.addCell(CreateHeaderCell("Id", color));
        table.addCell(CreateHeaderCell("Departament", color));
        table.addCell(CreateHeaderCell("Formatiune", color));
        table.addCell(CreateHeaderCell("Materie", color));
        table.addCell(CreateHeaderCell("Activitate", color));
        table.addCell(CreateHeaderCell("Materii Comune", color));
        table.addCell(CreateHeaderCell("Ore", color));
        table.setHeaderRows(1);
        table.setSplitLate(false);
        float[] columnWidths = new float[] {3f, 15f, 17f, 16f, 10f, 35f, 4f};
        table.setWidths(columnWidths);
    }
    
    public void addEmptyRow(PdfPTable table)
    {
        for (int i = 0; i < table.getNumberOfColumns(); i++)
        {
            table.addCell(CreateBodyCell(" ", Element.ALIGN_LEFT, tableBodyFonts.get(2)));
        }
    }
}
