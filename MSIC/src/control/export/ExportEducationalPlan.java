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
    Font headerFont;
    Font yearFont;
    Font semesterFont;
    Font tableHeaderFont;
    Font tableBodyFont1;
    Font tableBodyFont2;
    Font tableBodyFont3;
    Font tableBodyFont4;
    Font tableBodyFont5;
    String departament_name;
    String departament_id;
    String specializare;
    public String facultate;
    String ani;
    public String rector;
    public String director_departament;
    public String decan;
    String[] romanDigits;
    PdfPTable table;
    
    public ExportEducationalPlan(String departament_name, String departament_id, String specializare, int ani)
    {
        headerFont = FontFactory.getFont("Arial", 9, Font.BOLD);
        yearFont = FontFactory.getFont("Arial", 14, Font.BOLD);
        semesterFont = FontFactory.getFont("Arial", 12, Font.BOLD);
        tableHeaderFont = FontFactory.getFont("Arial", 9, Font.BOLD, BaseColor.WHITE);
        tableBodyFont1 = FontFactory.getFont("Arial", 8, Font.BOLD);
        tableBodyFont2 = FontFactory.getFont("Arial", 8, Font.BOLD);
        tableBodyFont3 = FontFactory.getFont("Arial", 8, Font.BOLD);
        tableBodyFont4 = FontFactory.getFont("Arial", 8, Font.BOLD);
        tableBodyFont5 = FontFactory.getFont("Arial", 8, Font.BOLD);
        tableBodyFont2.setColor(new BaseColor(0xff, 0x00, 0x00));
        tableBodyFont3.setColor(new BaseColor(0x00, 0x00, 0xff));
        tableBodyFont4.setColor(new BaseColor(0x8a, 0x2b, 0xe2));
        tableBodyFont5.setColor(new BaseColor(0x98, 0x5f, 0x0d));
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
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("UNIVERSITATEA DIN CRAIOVA", headerFont), 30, 810, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(facultate, headerFont), 30, 795, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("Departamentul: " + departament_name + " (" + departament_id + ")", headerFont), 30, 780, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("Programul de studii : " + specializare, headerFont), 30, 765, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("Durata studiilor : " + ani, headerFont), 30, 750, 0);
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("RECTOR,", headerFont), 30, 55, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("DIRECTOR DEPARTAMENT,", headerFont), document.getPageSize().getWidth() / 2, 55, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("DECAN,", headerFont), document.getPageSize().getWidth() - 30, 55, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(rector, headerFont), 30, 40, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(director_departament, headerFont), document.getPageSize().getWidth() / 2, 40, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase(decan, headerFont), document.getPageSize().getWidth() - 30, 40, 0);
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
        PdfPCell cell = new PdfPCell(new Phrase(text, tableHeaderFont));
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
    
    public void createTableHeader() throws DocumentException
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
    
    public void createTableFooter(EducationalPlanHoursAndCredits hours)
    {
        BaseColor color = new BaseColor(0x9a, 0xcd, 0x32);
        table.addCell(CreateEmptyCell());
        table.addCell(CreateEmptyCell());
        table.addCell(CreateEmptyCell());
        PdfPCell totalCell = CreateFooterCell("TOTAL", tableBodyFont1, color);
        totalCell.setRowspan(2);
        table.addCell(totalCell);
        table.addCell(CreateFooterCell(Integer.toString(hours.course), tableBodyFont2, color));
        table.addCell(CreateFooterCell(Integer.toString(hours.seminar), tableBodyFont3, color));
        table.addCell(CreateFooterCell(Integer.toString(hours.laboratory), tableBodyFont4, color));
        table.addCell(CreateFooterCell(Integer.toString(hours.project), tableBodyFont5, color));
        table.addCell(CreateFooterCell(Integer.toString(hours.credits), tableBodyFont1, color));
        table.addCell(CreateEmptyCell());
        
        table.addCell(CreateEmptyCell());
        table.addCell(CreateEmptyCell());
        table.addCell(CreateEmptyCell());
        PdfPCell sumCell = CreateFooterCell(Integer.toString(hours.course + hours.seminar + hours.laboratory + hours.project), tableBodyFont1, color);
        sumCell.setColspan(4);
        table.addCell(sumCell);
        table.addCell(CreateEmptyCell());
        table.addCell(CreateEmptyCell());
    }
    
    public String exportToPDF(ArrayList<EducationalPlan> educationalPlan, ArrayList<EducationalPlanHoursAndCredits> educationalPlanHours,
            String webSiteLocation, String localPath) throws DocumentException, FileNotFoundException
    {
        Document document = new Document(PageSize.A4, 40, 40, 110, 70); //left right top bottom
        
        String file = webSiteLocation + localPath;
        
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file)); 
        Font newLine = FontFactory.getFont("Arial", 2, Font.BOLD);
        writer.setPageEvent(this);
        
        document.open();
        
        int semester = 0;
        int year = 0;
        int fullSemester = 0;

        for (EducationalPlan row : educationalPlan)
        {
            if (semester != row.semestru)
            {
                if (semester != 0)
                {
                    createTableFooter(educationalPlanHours.get(fullSemester - 1));
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
                    try
                    {
                        createParagraph("        ANUL " + romanDigits[year - 1], document, yearFont, Element.ALIGN_LEFT);
                    }
                    catch (IOException ex)
                    {
                        return "Planul de invatamant nu a fost exportat in urma unei erori aparute";
                    }
                    document.add(new Phrase("\n", newLine));
                }
                
                semester = row.semestru;
                fullSemester++;
                try
                {
                    createParagraph("        SEMESTRUL " + romanDigits[semester - 1], document, semesterFont, Element.ALIGN_LEFT);
                }
                catch (IOException ex)
                {
                    return "Planul de invatamant nu a fost exportat in urma unei erori aparute";
                }
                document.add(new Phrase("\n", newLine));
                createTableHeader();
            }
            
            table.addCell(CreateBodyCell(row.materie, Element.ALIGN_LEFT, tableBodyFont1));
            table.addCell(CreateBodyCell(row.cod_disciplina, Element.ALIGN_CENTER, tableBodyFont1));
            table.addCell(CreateBodyCell(row.denumire_tip_disciplina, Element.ALIGN_CENTER, tableBodyFont1));
            table.addCell(CreateBodyCell(row.denumire_categorie_disciplina, Element.ALIGN_CENTER, tableBodyFont1));
            if (row.curs == 0)
            {
                table.addCell(CreateBodyCell("", Element.ALIGN_CENTER, tableBodyFont2));
            }
            else
            {
                table.addCell(CreateBodyCell(Integer.toString(row.curs), Element.ALIGN_CENTER, tableBodyFont2));
            }
            if (row.seminar == 0)
            {
                table.addCell(CreateBodyCell("", Element.ALIGN_CENTER, tableBodyFont3));
            }
            else
            {
                table.addCell(CreateBodyCell(Integer.toString(row.seminar), Element.ALIGN_CENTER, tableBodyFont3));
            }
            if (row.lucrari_practice == 0)
            {
                table.addCell(CreateBodyCell("", Element.ALIGN_CENTER, tableBodyFont4));
            }
            else
            {
                table.addCell(CreateBodyCell(Integer.toString(row.lucrari_practice), Element.ALIGN_CENTER, tableBodyFont4));
            }
            if (row.proiect == 0.0f)
            {
                table.addCell(CreateBodyCell("", Element.ALIGN_CENTER, tableBodyFont5));
            }
            else if (row.proiect - (int)row.proiect == 0.0f)
            {
                table.addCell(CreateBodyCell(Integer.toString((int)row.proiect), Element.ALIGN_CENTER, tableBodyFont5));
            }
            else
            {
                table.addCell(CreateBodyCell(Float.toString(row.proiect), Element.ALIGN_CENTER, tableBodyFont5));
            }
            table.addCell(CreateBodyCell(Integer.toString(row.numar_credite), Element.ALIGN_CENTER, tableBodyFont1));
            table.addCell(CreateBodyCell(Character.toString(row.denumire_tip_examinare.charAt(0)), Element.ALIGN_CENTER, tableBodyFont1));
        }
        createTableFooter(educationalPlanHours.get(fullSemester - 1));
        document.add(table);
        document.close();
        
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
        
        return null;
    }
}
