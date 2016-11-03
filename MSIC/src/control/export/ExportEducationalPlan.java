package control.export;

import Models.EducationalPlan;
import Models.EducationalPlanHoursAndCredits;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class ExportEducationalPlan extends PdfPageEventHelper { 
    ArrayList<Font> tableBodyFonts;
    String departament_name;
    String departament_id;
    String specializare;
    public String facultate;
    String ani;
    public String rector;
    public String director_departament;
    public String decan;
    String[] romanDigits;
    
    public ExportEducationalPlan(String departament_name, String departament_id, String specializare, int ani)
    {
    	tableBodyFonts = new ArrayList<Font>();
        tableBodyFonts.add(FontFactory.getFont("Arial", 9, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 8, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 8, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 8, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 8, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 8, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 14, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 12, Font.BOLD));
        tableBodyFonts.add(FontFactory.getFont("Arial", 9, Font.BOLD, BaseColor.WHITE));
        tableBodyFonts.get(2).setColor(new BaseColor(0xff, 0x00, 0x00));
        tableBodyFonts.get(3).setColor(new BaseColor(0x00, 0x00, 0xff));
        tableBodyFonts.get(4).setColor(new BaseColor(0x8a, 0x2b, 0xe2));
        tableBodyFonts.get(5).setColor(new BaseColor(0x98, 0x5f, 0x0d));
        this.departament_name = departament_name;
        this.departament_id = departament_id;
        this.specializare = specializare;
        this.ani = Integer.toString(ani);
        if (ani == 1)
        {
            this.ani += " an";
        }
        else
        {
            this.ani += " ani";
        }
        romanDigits = new String[]{"I", "II", "III", "IV", "V", "VI"};
    }
    
    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("UNIVERSITATEA DIN CRAIOVA", tableBodyFonts.get(0)), 30, 810, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(facultate, tableBodyFonts.get(0)), 30, 795, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("Departamentul: " + departament_name + " (" + departament_id + ")", tableBodyFonts.get(0)), 30, 780, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("Programul de studii : " + specializare, tableBodyFonts.get(0)), 30, 765, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("Durata studiilor : " + ani, tableBodyFonts.get(0)), 30, 750, 0);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("RECTOR,", tableBodyFonts.get(0)), 30, 55, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("DIRECTOR DEPARTAMENT,", tableBodyFonts.get(0)), document.getPageSize().getWidth() / 2, 55, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("DECAN,", tableBodyFonts.get(0)), document.getPageSize().getWidth() - 30, 55, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(rector, tableBodyFonts.get(0)), 30, 40, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(director_departament, tableBodyFonts.get(0)), document.getPageSize().getWidth() / 2, 40, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase(decan, tableBodyFonts.get(0)), document.getPageSize().getWidth() - 30, 40, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(Integer.toString(document.getPageNumber())), document.getPageSize().getWidth() / 2, 15, 0);
    }
    
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
    
    public void createTableHeader(PdfPTable table) throws DocumentException
    {
        BaseColor color = new BaseColor(0x16, 0x7f, 0x92);
        table = new PdfPTable(10);
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
    
    public void AddTableRow(PdfPTable table, EducationalPlan row)
    {
    	table.addCell(CreateBodyCell(row.materie, Element.ALIGN_LEFT, tableBodyFonts.get(1)));
        table.addCell(CreateBodyCell(row.cod_disciplina, Element.ALIGN_CENTER, tableBodyFonts.get(1)));
        table.addCell(CreateBodyCell(row.denumire_tip_disciplina, Element.ALIGN_CENTER, tableBodyFonts.get(1)));
        table.addCell(CreateBodyCell(row.denumire_categorie_disciplina, Element.ALIGN_CENTER, tableBodyFonts.get(1)));
        if (row.curs == 0)
        {
            table.addCell(CreateBodyCell("", Element.ALIGN_CENTER, tableBodyFonts.get(2)));
        }
        else
        {
            table.addCell(CreateBodyCell(Integer.toString(row.curs), Element.ALIGN_CENTER, tableBodyFonts.get(2)));
        }
        if (row.seminar == 0)
        {
            table.addCell(CreateBodyCell("", Element.ALIGN_CENTER, tableBodyFonts.get(3)));
        }
        else
        {
            table.addCell(CreateBodyCell(Integer.toString(row.seminar), Element.ALIGN_CENTER, tableBodyFonts.get(3)));
        }
        if (row.lucrari_practice == 0)
        {
            table.addCell(CreateBodyCell("", Element.ALIGN_CENTER, tableBodyFonts.get(4)));
        }
        else
        {
            table.addCell(CreateBodyCell(Integer.toString(row.lucrari_practice), Element.ALIGN_CENTER, tableBodyFonts.get(4)));
        }
        if (row.proiect == 0.0f)
        {
            table.addCell(CreateBodyCell("", Element.ALIGN_CENTER, tableBodyFonts.get(5)));
        }
        else if (row.proiect - (int)row.proiect == 0.0f)
        {
            table.addCell(CreateBodyCell(Integer.toString((int)row.proiect), Element.ALIGN_CENTER, tableBodyFonts.get(5)));
        }
        else
        {
            table.addCell(CreateBodyCell(Float.toString(row.proiect), Element.ALIGN_CENTER, tableBodyFonts.get(5)));
        }
        table.addCell(CreateBodyCell(Integer.toString(row.numar_credite), Element.ALIGN_CENTER, tableBodyFonts.get(1)));
        table.addCell(CreateBodyCell(Character.toString(row.denumire_tip_examinare.charAt(0)), Element.ALIGN_CENTER, tableBodyFonts.get(1)));
    }
    
    public void CopyFile(String file, String webSiteLocation, String localPath)
    {
    	File source = new File(file);
        
        Path path = Paths.get(webSiteLocation);
        path = path.getParent().getParent();
        
        String tempPath = path.toAbsolutePath().toString() + "\\web" + localPath;
        File target = new File(tempPath);
        
        try
        {
            Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException ex)
        {
        }
    }
    
    public void ParseEducationalPlan(ArrayList<EducationalPlan> educationalPlan, ArrayList<EducationalPlanHoursAndCredits> educationalPlanHours,
    		Document document, PdfPTable table, int fullSemester) throws DocumentException, IOException
    {
    	int semester = 0, year = 0;
    	Font newLine = FontFactory.getFont("Arial", 2, Font.BOLD);
    	
        for (EducationalPlan row : educationalPlan)
        {
            if (semester != row.semestru)
            {
                if (semester != 0)
                {
                    createTableFooter(educationalPlanHours.get(fullSemester - 1), table);
                    document.add(table);
                    document.add(new Phrase("\n", newLine));
                }
                
                if (year != row.an)
                {
                    if (year != 0)
                    {
                        document.newPage();
                    }
                    year = row.an;
                    createParagraph("        ANUL " + romanDigits[year - 1], document, tableBodyFonts.get(6), Element.ALIGN_LEFT);
                }
                
                semester = row.semestru;
                fullSemester++;
                createParagraph("        SEMESTRUL " + romanDigits[semester - 1], document, tableBodyFonts.get(7), Element.ALIGN_LEFT);
                document.add(new Phrase("\n", newLine));
                createTableHeader(table);
            }
            
            AddTableRow(table, row);
        }
    }
    
    public String exportToPDF(ArrayList<EducationalPlan> educationalPlan, ArrayList<EducationalPlanHoursAndCredits> educationalPlanHours,
            String webSiteLocation, String localPath) throws DocumentException, FileNotFoundException
    {
        Document document = new Document(PageSize.A4, 40, 40, 110, 70); //left right top bottom
        PdfPTable table = null;
        String file = webSiteLocation + localPath;
        int fullSemester = 0;
        
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file)); 
        
        writer.setPageEvent(this);
        
        document.open();
        
        try
        {
			ParseEducationalPlan(educationalPlan, educationalPlanHours, document, table, fullSemester);
		}
        catch (IOException e)
        {
			return "Planul de invatamant nu a fost exportat in urma unei erori aparute";
		}
        
        
        createTableFooter(educationalPlanHours.get(fullSemester - 1), table);
        document.add(table);
        document.close();
        
        CopyFile(file, webSiteLocation, localPath);
        
        return null;
    }
}
