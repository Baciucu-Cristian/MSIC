package control.export;

import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import Models.EducationalPlanHoursAndCredits;

public class ExportEducationalPlanBase {
	ArrayList<Font> tableBodyFonts;
	
    public void createParagraph(String text, Document doc, Font font, int align) throws DocumentException, IOException
    {
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(align);
        doc.add(p);
    }
    
    public PdfPCell CreateHeaderCell(String text, BaseColor backgroundColor)
    {
        PdfPCell cell = new PdfPCell(new Phrase(text, tableBodyFonts.get(8)));
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
    
    public PdfPCell CreateEmptyCell() 
    {
        PdfPCell cell = new PdfPCell(new Phrase(""));
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
    
    public PdfPCell CreateFooterCell(String text, Font font, BaseColor backgroundColor) 
    {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(backgroundColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }
    
    public PdfPTable createTableHeader() throws DocumentException
    {
        BaseColor color = new BaseColor(0x16, 0x7f, 0x92);
        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100.0f);
        table.addCell(CreateHeaderCell("Materie", color));
        table.addCell(CreateHeaderCell("Cod", color));
        table.addCell(CreateHeaderCell("Tip Disciplina", color));
        table.addCell(CreateHeaderCell("Categorie", color));
        table.addCell(CreateHeaderCell("C", color));
        table.addCell(CreateHeaderCell("S", color));
        table.addCell(CreateHeaderCell("L", color));
        table.addCell(CreateHeaderCell("P", color));
        table.addCell(CreateHeaderCell("Credite", color));
        table.addCell(CreateHeaderCell("Examinare", color));
        table.setHeaderRows(1);
        table.setSplitLate(false);
        float[] columnWidths = new float[] {31f, 10f, 13f, 13f, 4f, 4f, 4f, 4f, 7f, 10f};
        table.setWidths(columnWidths);
        return table;
    }
    
    public void createTableFooter(EducationalPlanHoursAndCredits hours, PdfPTable table)
    {
        BaseColor color = new BaseColor(0x9a, 0xcd, 0x32);
        table.addCell(CreateEmptyCell());
        table.addCell(CreateEmptyCell());
        table.addCell(CreateEmptyCell());
        PdfPCell totalCell = CreateFooterCell("TOTAL", tableBodyFonts.get(1), color);
        totalCell.setRowspan(2);
        table.addCell(totalCell);
        table.addCell(CreateFooterCell(Integer.toString(hours.course), tableBodyFonts.get(2), color));
        table.addCell(CreateFooterCell(Integer.toString(hours.seminar), tableBodyFonts.get(3), color));
        table.addCell(CreateFooterCell(Integer.toString(hours.laboratory), tableBodyFonts.get(4), color));
        table.addCell(CreateFooterCell(Integer.toString(hours.project), tableBodyFonts.get(5), color));
        table.addCell(CreateFooterCell(Integer.toString(hours.credits), tableBodyFonts.get(1), color));
        table.addCell(CreateEmptyCell());
        
        table.addCell(CreateEmptyCell());
        table.addCell(CreateEmptyCell());
        table.addCell(CreateEmptyCell());
        PdfPCell sumCell = CreateFooterCell(Integer.toString(hours.course + hours.seminar + hours.laboratory + hours.project), tableBodyFonts.get(1), color);
        sumCell.setColspan(4);
        table.addCell(sumCell);
        table.addCell(CreateEmptyCell());
        table.addCell(CreateEmptyCell());
    }
}
